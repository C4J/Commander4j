package com.commander4j.util;

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
	/**
	 * Field EXTERNAL_BUFFER_SIZE. (value is 128000) Value: {@value
	 * EXTERNAL_BUFFER_SIZE}
	 */
	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	private static boolean Enabled;

	/**
	 * Constructor for JPlaySound.
	 * 
	 * @param strFilename
	 *            String
	 */

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
