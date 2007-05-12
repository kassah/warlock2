<?php
	
	include '../../warlock.inc.php';
	head('downloads', 'Warlock Build ${warlock-version}');
	
?>
<h2>Downloads for Warlock ${warlock-version}</h2>
<div>
	<h2 class="title">Installers</h2>
	<div class="news-item">
	<table>
	<tr>
		<td><img class="platform" src="http://warlock.sf.net/images/win.png" border="0" /></td>
		<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-win32.win32.x86-installer.jar">Installer for Windows (32-bit)</a></td>
	</tr>
	<tr>
		<td><img class="platform" src="http://warlock.sf.net/images/linux.png" border="0" /></td>
		<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-linux.gtk.x86-installer.jar">Installer for Linux (x86)</a></td>
	</tr>
	<tr>
		<td><img class="platform" src="http://warlock.sf.net/images/linux.png" border="0" /></td>
		<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-linux.gtk.x86_64-installer.jar">Installer for Linux (x86_64)</a></td>
	</tr>
	<tr>
		<td><img class="platform" src="http://warlock.sf.net/images/mac.png" border="0" /></td>
		<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-macosx.carbon.ppc-installer.jar">Installer for Mac OS X (ppc)</a></td>
	</tr>
	</table>
	</div>
	
	<div>
		<h2 class="title">Binaries</h2>
		<div class="news-item">
		<table>
		<tr>
			<td><img class="platform" src="http://warlock.sf.net/images/win.png" border="0" /></td>
			<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-win32.win32.x86.zip">Windows (32-bit, zip)</a></td>
		</tr>
		<tr>
			<td><img class="platform" src="http://warlock.sf.net/images/linux.png" border="0" /></td>
			<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-linux.gtk.x86.zip">Linux (x86, zip)</a></td>
		</tr>
		<tr>
			<td><img class="platform" src="http://warlock.sf.net/images/linux.png" border="0" /></td>
			<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-linux.gtk.x86_64.zip">Linux (x86_64, zip)</a></td>
		</tr>
		<tr>
			<td><img class="platform" src="http://warlock.sf.net/images/mac.png" border="0" /></td>
			<td><a href="http://downloads.sourceforge.net/warlock/${buildId}-${warlock-version}-macosx.carbon.ppc.tar.gz">Mac OS X (ppc, tar.gz)</a></td>
		</tr>
		</table>
		</div>
	</div>
</div>

<?php foot(); ?>