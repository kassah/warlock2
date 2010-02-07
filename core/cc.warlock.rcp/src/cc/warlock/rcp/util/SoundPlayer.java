/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class SoundPlayer
{
	private class PlayFile implements Runnable {
		private String strFilename;
		
		public void run()
		{
			System.out.println("SoundPlayer.playFile: Attempting to play: "+ strFilename);
			if (System.getProperty("os.name").equals("Linux")) {
				Boolean extPlay = true;
				try {
					Process aplay = Runtime.getRuntime().exec("aplay "+strFilename);
					aplay.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
					extPlay = false;
				}
				
				// If External Play was successful, then we continue
				if (extPlay) {
					System.out.println("Played file successfully via aplay");
					return;
				}
			}
			/*
			  Now, that we're shure there is an argument, we
			  take it as the filename of the soundfile
			  we want to play.
			*/
			//String	strFilename = args[0];
			File	soundFile = new File(strFilename);
		
			/*
			  We have to read in the sound file.
			*/
			AudioInputStream	audioInputStream = null;
			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			}
			catch (Exception e)
			{
				/*
				  In case of an exception, we dump the exception
				  including the stack trace to the console output.
				*/
				e.printStackTrace();
				return;
			}
			PlayStream stream = new PlayStream();
			stream.audioInputStream = audioInputStream;
			stream.run();
		}
	}
	
	class PlayStream implements Runnable {
		private static final int	EXTERNAL_BUFFER_SIZE = 128000;
		private AudioInputStream audioInputStream;
			
		public void run() {
			/*
			  From the AudioInputStream, i.e. from the sound file,
			  we fetch information about the format of the
			  audio data.
			  These information include the sampling frequency,
			  the number of
			  channels and the size of the samples.
			  These information
			  are needed to ask Java Sound for a suitable output line
			  for this audio file.
			*/
			AudioFormat	audioFormat = audioInputStream.getFormat();

			/*
			  Asking for a line is a rather tricky thing.
			  We have to construct an Info object that specifies
			  the desired properties for the line.
			  First, we have to say which kind of line we want. The
			  possibilities are: SourceDataLine (for playback), Clip
			  (for repeated playback)	and TargetDataLine (for
			  recording).
			  Here, we want to do normal playback, so we ask for
			  a SourceDataLine.
			  Then, we have to pass an AudioFormat object, so that
			  the Line knows which format the data passed to it
			  will have.
			  Furthermore, we can give Java Sound a hint about how
			  big the internal buffer for the line should be. This
			  isn't used here, signaling that we
			  don't care about the exact size. Java Sound will use
			  some default value for the buffer size.
			*/
			SourceDataLine	line = null;
			DataLine.Info	info = new DataLine.Info(SourceDataLine.class,
													 audioFormat);
			try
			{
				line = (SourceDataLine) AudioSystem.getLine(info);

				/*
				  The line is there, but it is not yet ready to
				  receive audio data. We have to open the line.
				*/
				line.open(audioFormat);
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*
			  Still not enough. The line now can receive data,
			  but will not pass them on to the audio output device
			  (which means to your sound card). This has to be
			  activated.
			*/
			line.start();

			/*
			  Ok, finally the line is prepared. Now comes the real
			  job: we have to write data to the line. We do this
			  in a loop. First, we read data from the
			  AudioInputStream to a buffer. Then, we write from
			  this buffer to the Line. This is done until the end
			  of the file is reached, which is detected by a
			  return value of -1 from the read method of the
			  AudioInputStream.
			*/
			int	nBytesRead = 0;
			byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
			while (nBytesRead != -1)
			{
				try
				{
					nBytesRead = audioInputStream.read(abData, 0, abData.length);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				if (nBytesRead >= 0)
				{
					//int	nBytesWritten = 
					line.write(abData, 0, nBytesRead);
				}
			}

			/*
			  Wait until all data are played.
			  This is only necessary because of the bug noted below.
			  (If we do not wait, we would interrupt the playback by
			  prematurely closing the line and exiting the VM.)
			 
			  Thanks to Margie Fitch for bringing me on the right
			  path to this solution.
			*/
			line.drain();

			/*
			  All data are played. We can close the shop.
			*/
			line.close();
			/*
			  There is a bug in the jdk1.3/1.4.
			  It prevents correct termination of the VM.
			  So we have to exit ourselves.
			*/

		}
		
	}
	
	class QueueRunner extends Thread {
		public boolean running = false;
		
		public void run() {
			
			setName("SoundPlayer: QueueRunner");
			running = true;
			
			while (running) {
				try {
					Runnable entry = queue.poll(60, TimeUnit.SECONDS);
					if (entry == null) {
						synchronized(runner) {
							if(queue.isEmpty())
								running = false;
						}
					} else {
						entry.run();
					}
				} catch (InterruptedException e) {
					running = false;
				}
			}
			/* We're done running, dereference the SoundPlayer class so it can be garbage collected. */
			instance = null;
		}
	}
	
	protected static SoundPlayer instance = null;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20);;
	private QueueRunner runner = new QueueRunner();
	
	synchronized private static SoundPlayer getInstance(){
		if (instance == null) {
			instance = new SoundPlayer();
		}
		return instance;
	}
	
	public static void play(AudioInputStream audioStream) {
		getInstance().playStream(audioStream);
	}
	
	public static void play(InputStream stream) {
		AudioInputStream audioInputStream = null;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(stream);
			getInstance().playStream(audioInputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public static void play(String strFilename){
		getInstance().playFile(strFilename);
	}
	
	synchronized protected void playSound(Runnable entry) {
		synchronized(runner) {
			if (!queue.offer(entry)) {
				System.err.println("Sound queue full, not playing sound.");
			}
			if(!runner.running) {
				runner.start();
			}
		}
	}
	
	private void playFile(String strFilename) {
		PlayFile player = new PlayFile();
		player.strFilename = strFilename;
		playSound(player);
	}
	
	private void playStream(AudioInputStream audioStream) {
		PlayStream player = new PlayStream();
		player.audioInputStream = audioStream;
		playSound(player);
	}
}