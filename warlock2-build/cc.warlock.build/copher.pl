#!/usr/bin/perl
# This code is placed under the GPL version 2. See http://www.gnu.org for details.
# See http://meatsmith.sourceforge.net for more information on copher.

use strict;
use WWW::Mechanize;
use Net::FTP;
use Carp;
use File::Basename;

my $debug = 1;
###########
# 'Constants'
use constant {
    TRUE => 1,
    FALSE => 0
    };

# Sourceforge
my $sfurl = 'https://sourceforge.net/account/login.php';
my $login_form_action = 'https://sourceforge.net/account/login.php';
my $sfproject_url = 'https://sourceforge.net/projects/';
my $sfhome = 'https://sourceforge.net';
my $sf_ftp = 'upload.sourceforge.net';
my $sf_ftp_user = 'anonymous';
my @sf_ftp_path = qw(incoming);     # path to enter before uploading file

###########
# To be set by the script when they're determined:
my $group_id;
my $package_id;
my $release_id;

###########
# Configuration information

my %sfuser;
my $project_name;
my $package_name;
my $release_name;
my $release_date;;
my @release_files;

my ($notes_file, $changelog_file);

my %release_files;
my %opt;
# Some default values:
$opt{release_exists} = 0;           # Default to creating a new release, not editing an existing one (override with -E)
$opt{active} = 1;
$opt{noupload} = 0;

my $debug = 0;   # print debuggning messages
###########

sub print_usage {
    print "Usage: $0 [options] [files]\n";
    print "option format: --option=VALUE or -o VALUE\n" .
    print "\noptions:\n" .
	"--user, -u         specify login name\n" .
	"--password, -w     specify password\n" .
	"--project, -p      project name (unixname) to work with\n" .
	"--package, -k      name of the package to release in (should already exist)\n" .
	"--release, -r      name for the new or existing release\n" .
	"--date, -d         release date (YYYY-MM-DD)\n" .
	"--group-id, -G     group id (i.e. project id) to use (faster if specified)\n" .
	"--package-id, -P   package id to use (faster if specified)\n" .
	"--release-id, -R   release id of existing release to modify (implies -E)\n" .
	"--exists, -E       don't create a new release, use existing (use with -r)\n" .
	# if name matches > 1 release, what to do? ask, choose first (configurable) TODO
	"--hidden, -H       set release as not visible to the public\n" .                  # -H sets active = 0
	"--active, -A       set release as active [default for new releases]\n" .
	"--notes, -N        file containing release notes\n" .
	"--changelog, -C    file containing changelog\n" .
	"--noupload         don't upload files (assume they already were)\n";
}

sub debug ($) {
    return -1 unless $debug;
    print STDERR shift;
}

sub debugv () {
    return -1 unless $debug;
    my $debug_join = "|";
    print STDERR "debug: " . join($debug_join, @_) . "\n";
    return;
}

###########
# Read in .netrc file in home directory, if it exists
if (open(RC, "$ENV{HOME}/.netrc")) {
    my $machine;
    foreach (<RC>) {
	if (/^machine (\S+)/) { $machine = $1; next; }
	if (/^\s*login (\S+)/ && $machine eq 'sourceforge') { $sfuser{loginname} = $1; next; }
	if (/^\s*password (\S+)/ && $machine eq 'sourceforge') { $sfuser{password} = $1; last; }
    }
    close RC;
}

# Read in copherrc file in current directory if it exists
if (open(PROJ, "./copherrc")) {
    foreach (<PROJ>) {
	if (/^project_name\s*[=:\s]\s*(\S*)$/) {
	    $project_name = $1;
	} elsif (/^group_id\s*[=:\s]\s*(\S*)$/) {
	    $group_id = $1;
	} elsif (/^package_name\s*[=:\s]\s*(\S*)$/) {
	    $package_name = $1;
	} elsif (/^package_id\s*[=:\s]\s*(\S*)$/) {
	    $package_id = $1;
	} elsif (/^release_name\s*[=:\s]\s*(\S*)$/) {
	    $release_name = $1;
	} elsif (/^release_id\s*[=:\s]\s*(\S*)$/) {
	    $release_id = $1;
	}
    }
close PROJ;
}

# Read in command-line options
use Getopt::Long;
Getopt::Long::Configure(qw(gnu_getopt permute bundling));
GetOptions(
	   'help|h' => sub {
	       &print_usage;
	       exit 0;
	   },
	   'user|loginname|u:s' => \$sfuser{loginname},
	   'password|pw|P:s' => \$sfuser{password},
	   'project|pj|j|p:s' => \$project_name,
	   'package|pkg|b|k:s' => \$package_name,
	   'release|r:s' => \$release_name,
	   'date|d:s' => sub {
	       $release_date = $_[1];
	       die "Release date format must be YYYY-MM-DD. Offending datum: $release_date\n" unless $release_date =~ /\d{4}-\d{2}-\d{2}/;
	   },
	   'group-id|group_id|gid|G:s' => \$group_id,
	   'package-id|package_id|pid|K:s' => \$package_id,
	   'release-id|release_id|rid|R:s' => sub { $release_id = $_[1]; $opt{release_exists} = 1; },
	   'noupload|no-upload' => \$opt{noupload},       # don't upload files (assume they already were?)
	   # 'newpackage'     # force creation of a new package
	   # 'newrelease'     # force creation of a new release
	   'notes|N:s' => sub {
	       if (! -f $_[1]) {
		   die "no such file for release notes: $_[1]\n";
	       }
	       $notes_file = $_[1];
	   },
	   'changelog|C:s' => sub {
	       if (! -f $_[1]) {
		   die "no such file for changelog: $_[1]\n";
	       }
	       $changelog_file = $_[1];
	   },
	   'debug' => \$debug
	   );

debug("$project_name | $group_id | $package_name | $package_id | $release_name | $release_id\n");

if (@ARGV) {    # if there's anything left in the arg list, treat it as a list of files to add to the release
    @release_files = @ARGV;
}

###########
# Make sure all necessary parameters were specified, either on the command line, in a file,
# or on stdin
if ($sfuser{loginname} eq "" or $sfuser{password} eq "") {
    die "Need loginname (-u) and password (-P) to continue.\n";
}
if ($project_name eq '' and $group_id eq '') {
    die "Need project name (-p) or group id (-G) to continue.\n";
}
if ($package_name eq '' and $package_id eq '') {
    die "Need package name (-k) or package id (-K) for existing package to continue.\n";
}
if ($release_name eq '' and $release_id eq '') {
    die "Need release name (-r) for new or existing release; or release id for existing release.\n";
}

if ($release_id) { # The release already exists (or at least the user thinks it does?)
    $opt{release_exists} = 1;
}

###########
# Note: If the information %type_id and %arch_id is instead stored in a file,
# this file can be updated if changes/additions are detected. [todo]
my %type_id = (
	       '.deb' => 1000,
	       '.rpm' => 2000,
	       '.zip' => 3000,
	       '.bz2' => 3001,
	       '.gz' => 3002,
	       'Source .zip' => 5000,
	       'Source .bz2' => 5001,
	       'Source .gz' => 5002,
	       'Source .rpm' => 5100,
	       'Other Source File' => 5900,
	       '.jpg' => 8000,
	       'text' => 8001,
	       'html' => 8002,
	       'pdf' => 8003,
	       'Other' => 9999,
	       '.sit' => 3003,
	       '.nbz' => 3004,
	       '.exe (DOS)' => 2500,
	       '.exe (16-bit Windows)' => 2501,
	       '.exe (32-bit Windows)' => 2502,
	       '.exe (OS/2)' => 2600,
	       '.dmg' => 3005,
	       '.jar' => 2601,
	       'Source Patch/Diff' => 5901,
	       '.prc (PalmOS)' => 2700,
	       '.iso' => 3006,
	       'Source .Z' => 5003,
	       '.bin (MacBinary)' => 2650,
	       '.ps (PostScript)' => 8004,
	       '.msi (Windows installer)' => 2503,
	       'Other Binary Package' => 4000
	       );
my %arch_id = (
	       'i386' => 1000,
	       'IA64' => 6000,
	       'Alpha' => 7000,
	       'Any' => 8000,
	       'PPC' => 2000,
	       'MIPS' => 3000,
	       'Sparc' => 4000,
	       'UltraSparc' => 5000,
	       'Other' => 9999,
	       'Platform-Independent' => 8500,
	       'Platform Independent' => 8500,         # alias for convenience; not in sourceforge's list
	       'ARM' => 3001,
	       'SH3' => 3002,
	       'AMD64' => 6001,
	       'PPC64' => 2001
	       );

#%release_files         # Each element is a reference to a 'file_struct' type:
# %file_struct = (         # (generic structure definition)
# 		name => 'name',
# 		file_id => undef,   # set when it's known
# 		added => 0,         # set to 1 when it's confirmed to be added to the release
#               class               # specified by user; 'Source' iff file is a source file, 'Binary' iff binary; or 'Other'
# 		type => 'type',
# 		typenum => 1000,    # SF's number for the type (value of the form input)
# 		arch => 'arch'
# 		archnum => 1000,    # SF's number for the arch
# 		);

print STDERR "Files in release: @release_files\n";
foreach my $file (@release_files) {
    my $basename = basename($file);   
    my $class;   # Source/Binary/Other

    $class = 'source';    #temp; will be an option possibly defaulting to 'source'
    my $filetype = guess_file_type($file, $class);
    my $filetype_id = $type_id{$filetype} || '9999';
    my $arch = 'Platform-Independent';
    $release_files{$basename} = {
	name => $basename,
	fullpath => $file,
	file_id => undef,
	added => 0,
	type => $filetype,
	type_id => $type_id{$filetype},
	arch => $arch,
	arch_id => $arch_id{$arch}	
    };
 
    print STDERR "Filetype: $file is a $filetype ($filetype_id)\n";
}

###########

sub guess_file_type ($$) {
    my $filename = shift;
    my $class = shift;
    my $type;


    my $typemagic = `file $filename`;
    $typemagic =~ s/$filename:\s*//;
    $_ = $typemagic;
    if (/^(bzip2|gzip) compressed data/) {
	$type = ($1 eq 'gzip' ? '.gz' : '.bz2');
	if ($class =~ /source/i) {
	    $type = 'Source ' . $type;
	}
    } elsif (/^compress\'d data/) {
	$type = '.Z';
	if ($class =~ /source/i) {
	    $type = 'Source ' . $type;
	}

    } elsif (/^Zip archive data/) {
	$type = '.zip';
	if ($class =~ /source/i) {
	    $type = 'Source ' . $type;
	}

    } elsif (/^RPM \w+ bin/) {
	$type = '.rpm';
    } elsif (/^RPM/) {
	$type = '.rpm';
	if ($class =~ /source/i) {
	    $type = 'Source ' . $type;
	}
    } elsif ($class =~ /script text executable/i) {
	$type = 'Other Source File';
    } elsif ($class =~ /source/i) {
	$type = 'Other Source File';
    } elsif (/^JPEG/) {
	$type = '.jpg';
    } elsif (/^HTML document text/) {
	$type = 'html';
    } elsif (/^PDF document/) {
	$type = 'pdf';
    } elsif (/^ASCII \w+ text/) { # e.g. ASCII English text
	$type = 'text';
    } elsif (/^\'diff\' output text/) {  # only seems to catch a few patches
	$type = 'Source Patch/Diff';
    } elsif (/^ISO 9660 CD-ROM/) { # ISO 9660 CD-ROM filesystem data
	$type = '.iso';
    } elsif (/^PostScript document/) {
	$type = '.ps';
    } elsif (/^data\s*$/) {
	$type = 'Other Binary Package';
    }

    my $ext = $filename;
    $ext =~ s/(.*)\.//;
    if ($ext eq 'jar' and $type =~ /zip/i) {
	$type = '.jar';
    }

    return $type || 'Other';
}

my $mech = WWW::Mechanize->new(
			       autocheck => 1    # autocheck for errors
			       );

# $response: an HTTP::Response object
# $sfurl could be a URL string, a URI object, or a WWW::Mechanize::Link object
print STDERR "Downloading $sfurl... ";
my $response = $mech->get($sfurl);
print STDERR &check_response($mech, $response, 1) . "\n";


if (!$mech->is_html) {
    die "$sfurl returned non-html ". $mech->ct() ." content-type";
}

my @forms = $mech->forms();   # a list of HTML::Form objects
my $form_number = 1;   # start at one, then iterate until we have the login form

foreach my $form (@forms) {
    if ($form->action eq $login_form_action) {
	last;
    }
    $form_number++;
}

$mech->form_number($form_number);

$mech->tick('stay_in_ssl', "1");    # enable "Stay in SSL"
$mech->untick('persistent_login', 1); # make sure this is disabled

print STDERR "Submitting form #$form_number to login as $sfuser{loginname}\n";
    
my $response = $mech->submit_form(
				   form_number => $form_number,
				   button => 'login',
				   fields => {
				       form_loginname => $sfuser{loginname},
				       form_pw => $sfuser{password}
				   },		   
				   );

if ($mech->success) {
    print STDERR "Logged in\n";
} else {
    die "failed posting login form\n";
}

if (! $group_id) {
    print STDERR "No group_id specified, fetching project page for project $project_name... ";
    my $reponse = &project_page($mech, $project_name);
    print STDERR &check_response($mech, $response, 1) . "\n";
    
    my $admin_link = $mech->find_link(url_regex => qr|^/project/admin/\?group_id=(.+)|);     # "should" still work if text => 'Admin' is added
    $admin_link->url =~ m|^/project/admin/\?group_id=(\d+)|;
    
    $group_id = $1;
    if (!length($group_id)) {
	die "error: no group_id in project page";
    }
}

if (! $package_id) {
    print STDERR "No package_id specified, fetching editpackages.php ('File Releases') page with group_id=$group_id... ";
    my $response = &editpackages_php($mech, $group_id);
    print STDERR &check_response($mech, $response, 1) . "\n";
      
    my @packages = &get_packages($mech);
    foreach my $pkg (@packages) {
	print STDERR "Package: name: " . $pkg->{package_name} .', package_id: '. $pkg->{package_id} .', group_id: '. $pkg->{group_id} ."\n";
	if ($pkg->{package_name} eq $package_name) {      # good!
	    $package_id = $pkg->{package_id};
	    # ensure $pkg->{group_id} eq $group_id? it "should"
	    unless ($pkg->{group_id} eq $group_id) {
		warn $pkg->{group_id} . " doesn't match known group ID " . $group_id . "\n";
	    }
	}
    }
    if (!$package_id) {
	print STDERR "Couldn't find a package with package_name $package_name\n";
	# future versions will be able to handle creation of new packages [todo]
	
	#print STDERR "Creating a new package...\n";
	exit 1; # not yet
    }
}

if (! $release_id) {
    print STDERR "No release_id specfied, fetching editreleases.php page with group_id=$group_id, package_id=$package_id... ";
    # or "Fetching list of file releases for $package_name (group_id=$group_id, package_id=$package_id)... ";
    my $response = &editreleases_php($mech, $group_id, $package_id);
    print STDERR &check_response($mech, $response, 1) . "\n";
    
    my @releases = &get_releases($mech);
    foreach my $rls (@releases) {
	print STDERR "Release: name: " . $rls->{release_name} .', release_id: '. $rls->{release_id} .', package_id: '. $rls->{package_id} ."\n";
	if ($rls->{release_name} eq $release_name) {
	    $release_id = $rls->{release_id};
	    print STDERR "Found release with name $release_name, release_id is $release_id. Using...\n";
	    # todo: sanity-check group_id and package_id?
	    #last;
	}
    }
    
    if (!$release_id) {
	print STDERR "Couldn't find a release with release_name $release_name, will create one...\n";
    }
}

# Now upload files to be added to the release
if (!$opt{noupload} and (keys %release_files)) {
    print STDERR "Uploading files...\n"
    &upload_files(\%release_files);
}
print "Sleeping for 10 seconds to allow sourceforge to notice files...\n";
sleep 10;

if (! $release_id) {
    print STDERR "No release_id specified, fetching newrelease.php page... ";
    my $response = &newrelease_php($mech, $group_id, $package_id);
    print STDERR &check_response($mech, $response, 1) . "\n";

    print STDERR "Adding new release (submitting form on newrelease.php)... ";
    my $response =  &add_release($mech, $group_id, $package_id, $release_name);
    print STDERR &check_response($mech, $response, 1) . "\n";
} else {
    print STDERR "Fetching editreleases.php page, editing $release_id... ";
    my $response = &editreleases_php($mech, $group_id, $package_id, $release_id);
    print STDERR &check_response($mech, $response, 1) . "\n";
}


# Now add files to the release
my %release = (
	       id => $release_id,
	       date => $release_date,
	       name => $release_name,
	       active => $opt{active},
	       notes_file => $notes_file,
	       changelog_file => $changelog_file,
	       files => \%release_files
	       );

print STDERR "Carrying out Step 1: Edit Existing Release... ";
my $response = &edit_release_step1($mech, $group_id, $package_id, \%release);
print STDERR &check_response($mech, $response, 0) . "\n";

if (%release_files) {
    print STDERR "Carrying out Step 2: Add Files To This Releases... \n";
    my $response = &edit_release_step2($mech, $group_id, $package_id, \%release);
    print STDERR &check_response($mech, $response, 0) . "\n";
}

print STDERR "Carrying out Step 3: Edit Files In This Release...\n";
my $value = &edit_release_step3($mech, $group_id, $package_id, \%release);
if (!$value) {
    print STDERR "Error doing step 3. $value\n";
}

# Step 4. Email Release Notice: I can't write or test this yet as I've never seen what it looks like when someone is 'monitoring'

&close_copher;


###############
# Subroutines #
###############

sub close_copher {
    open(OUT,">out.html");
    print OUT $mech->content;
    close OUT;
    
    exit shift;
}

sub check_response ($$) {
    my ($mech, $response, $die_bool) = @_;
    if (!$response) {
	return "NOT found";
	die "no response, link/etc. not found?" if $die_bool;
    } elsif ($mech->success()) {
	return "followed";
    } else {
	return "found? but failed?";
	die "mechanize operation failed" if $die_bool;
    }
}

sub make_url ($) {
    # currently not used by anything
    my %arg = @_;
    if ($arg{type} eq 'newrelease') {
	return "newrelease.php?package_id=$arg{package_id}&group_id=$arg{group_id}";
    }
}

sub project_page ($$) {
    my ($mech, $project_name) = @_;
    my %base_url;
    $base_url{'projects'} = 'https://sourceforge.net/projects/';
    my $url = $base_url{'projects'}.$project_name.'/';
    my $response = $mech->get($url);
    return $response;
}

sub editpackages_php($$) {
    my ($mech, $group_id) = @_;

    my %base_url;
    $base_url{'editpackages.php'} = 'https://sourceforge.net/project/admin/editpackages.php';

    # Warning: any/some of these may be empty; $release_id in fact sometimes will be (?; when checking for what releases exist...)
    return $mech->get($base_url{'editpackages.php'}."?group_id=$group_id");
}

sub editreleases_php($$$$) {
    my ($mech, $group_id, $package_id, $release_id) = @_;

    my %base_url;
    $base_url{'editreleases.php'} = 'https://sourceforge.net/project/admin/editreleases.php';

    # Warning: any/some of these may be empty; $release_id in fact sometimes will be (?; when checking for what releases exist...)
    return $mech->get($base_url{'editreleases.php'}."?group_id=$group_id&package_id=$package_id&release_id=$release_id");
}

sub newrelease_php($$$) {
    my ($mech, $group_id, $package_id) = @_;

    my %base_url;
    $base_url{'newrelease.php'} = 'https://sourceforge.net/project/admin/newrelease.php';

    # Warning: any/some of these may be empty
    return $mech->get($base_url{'newrelease.php'}."?group_id=$group_id&package_id=$package_id");
}

# Get a list of packages from editpackages.php page (which must already be loaded in $mech)
sub get_packages ($) {
    my $mech = shift;
    my @packages;
    # could use |sig and match to the </A>...
    #$mech->content =~ m|<A HREF="editreleases.php\?package_id=(\d+)&amp;group_id=(\d+)"><B>\[Edit Releases\]</B>|ig
    my @forms = $mech->forms;
    foreach my $form (@forms) {
	if ($form->action =~ m|/project/admin/editpackages.php$| and (my $func = $form->find_input('func', 'hidden'))) {
	    next unless $func->value eq 'update_package';

	    my ($group_id, $package_id, $package_name);

	    my $input;
	    $input = $form->find_input('group_id', 'hidden');
	    if ($input) {
		$group_id = $input->value;
	    } else {
		warn "no group_id input?\n";
	    }
	    $input = $form->find_input('package_id', 'hidden');
	    if ($input) {
		$package_id = $input->value;
	    } else {
		warn "no package_id input?\n";
	    }
	    $input = $form->find_input('package_name', 'text');
	    if ($input) {
		$package_name = $input->value;
	    } else {
		warn "no package_name input?\n";
	    }
	    
	    push @packages, {
		'group_id' => $group_id,
		'package_name' => $package_name,
		'package_id' => $package_id
		};
	}
    }
    return @packages;
}

# Get a list of releases from editreleases.php page (which must already be loaded in $mech)
sub get_releases ($) {
    my $mech = shift;
    my @releases;
    my $content = $mech->content;      # This is necessary. Using $mech->content in the while () causes an infinite loop.
    while ($content =~ m|\s*(\S+)\s*<a href="editreleases.php\?package_id=(\d+)&amp;release_id=(\d+)&amp;group_id=(\d+)">\[Edit This Release\]</a>|g) {
	my ($release_name, $package_id, $release_id, $group_id) = ($1, $2, $3, $4);
	push @releases, {
	    'release_name' => $release_name,
	    'package_id' => $package_id,
	    'release_id' => $release_id,
	    'group_id' => $group_id
	    };
    }
    return @releases;
}

sub upload_files ($) {
    my $release_files = shift;
    
    my @fullpaths;
    foreach (keys %{$release_files}) {
	push @fullpaths, $release_files->{$_}->{'fullpath'};
    }

    print STDERR "Files to upload: @fullpaths.\n";
    print STDERR "Uploading files to $sf_ftp...\n";
    
    if (@fullpaths and !$opt{noupload}) {
	my $ftp = Net::FTP->new($sf_ftp);
	if (!$ftp) {
	    print STDERR "Error creating Net::FTP object: $!\n";	
	    die;
	} else {
	    $ftp->login($sf_ftp_user, 'anonymous@anonymous.net');
	    foreach (@sf_ftp_path) {    # Definitely need error checking here. not here yet, though
		$ftp->cwd($_);
	    }
	    $ftp->binary();
	    foreach my $file (@fullpaths) {
		$ftp->put($file);    # returns remote file name
		print STDERR "Uploaded $file\n";
		$release_files->{basename($file)}->{'uploaded'} = 1;     # keep track of which files we've uploaded
	    }
	    $ftp->quit;
	}
    }
}


# Add a new release by submitting the form on the newrelease.php page
# Pre-condition: $mech->content is the newrelease.php page with correct package_id and group_id
#   passed in as CGI parameters
sub add_release ($$$$) {
    my ($mech, $group_id, $package_id, $release_name) = @_;
    
    my @forms = $mech->forms();
    my $form_number = 1;
    
    my $package_form;
    
    foreach my $form (@forms) {
	#print STDERR "FORM: " . $form->action . "...${sf_url}project/admin/editpackages.php\n";
	if ($form->action eq "${sfhome}/project/admin/newrelease.php") {
	    last;
	}
	$form_number++;
    }
    
    $mech->form_number($form_number);
    
    $mech->select('package_id', $package_id) or warn "can't select $package_id for select package_id";
    
    print STDERR "\nSubmitting form #$form_number to add a new release: $release_name... ";
	
	return $mech->submit_form(
				  form_number => $form_number,
				  fields => {
				      release_name => $release_name
				      },
				  button => 'submit'
				  );
}

# Edit a release by submitting the form on the editreleases.php page which had release_id, etc. passwd in
# But $release_id isn't necessarily set yet (isn't if this is a new release)
# Pre-condition: $mech-content is such a page
# Sets $release_id if $release{id} wasn't set
sub edit_release_step1($$$$) {
    my ($mech, $group_id, $package_id, $release) = @_;
    # %{$release} has: id, date, name, active (bool), notes_file, changelog_file, release_files => \%release_files

    my @forms = $mech->forms();
    my $form_number;
    
    for (my $formcount = 1; $formcount <= $#forms + 1; $formcount++) {
	my $form = $forms[$formcount-1];
	#print STDERR "FORM: " . $form->action . "...${sfhome}/project/admin/editpackages.php\n";
	if ($form->action eq "${sfhome}/project/admin/editreleases.php") {
	    if ($form->enctype eq "multipart/form-data" and !$form_number) {
		$form_number = $formcount;   # this is the "Step 1: Edit Existing Release" form
	    }
	}
    }
    
	# Now submit the first form
	$mech->form_number($form_number);
    
    if (!$release->{id}) {
	# If we didn't already know the release_id
	$release->{id} = $mech->current_form()->find_input('release_id', 'hidden')->value();    # declared at top
	$release_id = $release->{id};
    }
    
    my %fields;

    if ($release->{date} =~ /\d{4}-\d{2}-\d{2}/) {
	# note: release_date is rejected unless is conforms to a certain format, my guess on that format is above
	$fields{release_date} = $release->{date};
    } # otherwise: leave the default (i.e. current date if we're creating the release today) value filled in
    $fields{release_name} = $release->{name};       # this field should already be filled in, but let's make sure

    # input type="file" name="uploaded_notes"     -- release notes file to upload
    # input type="file" name="uploaded_changes"   -- changelog file to upload
    # textarea name="release_notes"               -- release notes
    # textarea name="release_changes"             -- changelog

    if ($release->{notes_file}) {
	open (IN, $release->{notes_file}) or die "can't open release notes file " . $release->{notes_file} . "\n";
	local $/ = undef;     # to read the whole file in with <>
	$fields{release_notes} = <IN>;
	close IN;
    }
    if ($release->{changelog_file}) {
	open (IN, $release->{changelog_file}) or die "can't open changelog file " . $release->{changelog_file} . "\n";
	local $/ = undef;     # to read the whole file in with <>
	$fields{release_changes} = <IN>;
	close IN;
    }

    my $preformatted = 1;   # temporary
    $mech->tick('preformatted', 1) if $preformatted;       # to be configurable...
    
    if ($release->{active} == 0) {
	$mech->select('status_id', 3);     # 1 is for Active, 3 is for Hidden (what about 2?)
    } else {
	$mech->select('status_id', 1);
    }

    return $mech->submit_form(
			      form_number => $form_number,
			      fields => \%fields,
			      button => 'submit'
			      );
    
    # Note: Need to check that our changes were accepted. this is not done here yet
}

# Step 2: Add Files To This Release
sub edit_release_step2 ($$$$) {
    my ($mech, $group_id, $package_id, $release) = @_;
    # %release has: id, date, name, active (bool), notes_file, changelog_file, files => \%release_files
    
    my @forms = $mech->forms();
    my $form_number;
    
    my $release_files = $release->{files};

    for (my $formcount = 1; $formcount <= $#forms + 1; $formcount++) {
	my $form = $forms[$formcount-1];
	#print STDERR "FORM: " . $form->action . "...${sfhome}/project/admin/editpackages.php\n";
	if ($form->action eq "${sfhome}/project/admin/editreleases.php") {
	    if ($form->enctype ne "multipart/form-data" and !$form_number) {
		# note: the multipart/form-data form is the 'step 1' form
		$form_number = $formcount;   # this is the "Step 2: Add Files To This Release" form
	    }
	}
    }

    $mech->form_number($form_number);
  
    foreach my $filename (keys %{$release_files}) {
	print STDERR "'Ticking' $filename in file list\n";
	# note: *should* check whether this is possible, first
	$mech->tick('file_list[]', $filename);
    }
    
    print STDERR "Submitting form #$form_number for Step 2: Add Files To This Release... ";
	return $mech->submit_form(
				  form_number => $form_number,
				  button => 'submit'
				  );  
}

# Step 3: Edit Files In This Release
sub edit_release_step3 ($$$$) {
    my ($mech, $group_id, $package_id, $release) = @_;
    # %release has: id, date, name, active (bool), notes_file, changelog_file, files => \%release_files
    
    my $files = $release->{files};
   
    my @form_numbers;
    my @forms = $mech->forms();

    if (!@forms) {
	print STDERR "ERROR: no forms\n";
	return;
    }
    
    for (my $formcount = 1; $formcount <= $#forms + 1; $formcount++) {
	my $form = $forms[$formcount-1];
	if ($form->action eq "${sfhome}/project/admin/editreleases.php") {
	if ($form->find_input('step3', 'hidden') and $form->find_input('submit', 'submit')->value() eq 'Update/Refresh') {
		push @form_numbers, $formcount;
	    }
	}
    }
    
    if (! @form_numbers) {
	print STDERR "no forms\n";
	return undef;
    }
    
# Note that this assumes that the results of submitting one form don't change another form, or the form numbering up to $form_number
    foreach my $form_number (@form_numbers) {
	$mech->form_number($form_number);
	
	my $file_id = $mech->current_form()->find_input('file_id', 'hidden')->value();
	
	# Extract the filename
	$mech->content =~ m|<input type="hidden" name="file_id" value="$file_id">.*?\w*?<input type="hidden" name="step3" value="1">.*?\w*?<tr bgcolor="#\w{6}">.*?\w*?<td nowrap><font size="-1">(.*?)</td>|s;
	
	my $filename = $1;

	if (! $files->{$filename}->{arch_id}) {
	    $files->{$filename}->{arch_id} = 8500; # 'Architecture Independent'
	}

	$mech->select('processor_id', $files->{$filename}->{arch_id});
	if (! $files->{$filename}->{type_id}) {
	    $files->{$filename}->{type_id} = 9999; # 'Other'
	}

	$mech->select('type_id', $files->{$filename}->{type_id});            # 5001=Source .bz2; 5002=Source .gz

	$mech->select('new_release_id', $release{id});    # this should already be selected by default, but let's make sure
	
	my $fields = {};
	if ($release{date} =~ /\d{4}-\d{2}-\d{2}/) {
	    print STDERR "setting release_time to $release{date} for $filename\n";
	    # note: release_time probably needs to conform to the same format as release_data (?)
	    ${$fields}{release_time} = $release{date};      # why do they call it release_time here?
	} # otherwise: leave the default (i.e. current date if we're creating the release today) value filled in
	
	print STDERR "Submitting form #$form_number for Step 3: Edit Files In This Release... ";
	    my $response = $mech->submit_form(
					      form_number => $form_number,
					      fields => $fields,
					      button => 'submit'
					      );
	print STDERR &check_response($mech, $response, 0) . "\n";
    }

    return TRUE;
}

# Delete a file from a release
# Pre-condition: $mech->content holds the edit release page for the release
sub delete_file () {
    # This is a stub. To be implemented later. [todo]
}

# Call delete_file for the next file while there's still file(s) in the release
sub delete_all_files () {
    # [todo]
}
