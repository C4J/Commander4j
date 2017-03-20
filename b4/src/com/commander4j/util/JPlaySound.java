package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JPlaySound.java
 * 
 * Package Name : com.commander4j.util
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 */
public class JPlaySound
{

	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	private static boolean Enabled;

	public static void enable() {
		Enabled = true;
	}

	public static void disable() {
		Enabled = false;
	}

	public static boolean isEnabled() {
		return Enabled;
	}

	public JPlaySound(String strFilename)
	{

		if (isEnabled() == true)
		{

			File soundFile = new File(strFilename);

			AudioInputStream audioInputStream = null;

			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
				AudioFormat audioFormat = audioInputStream.getFormat();
				SourceDataLine line = null;
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(audioFormat);
				line.start();

				int nBytesRead = 0;
				byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
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
						int nBytesWritten = line.write(abData, 0, nBytesRead);
						if (nBytesWritten > 0)
						{
							nBytesWritten = 0;
						}
					}
				}

				line.drain();
				line.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

}
