package com.commander4j.labeller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LabellerIntelHex
{

	public static final int NO_CONVERSION = 0;
	public static final int CR_TO_CRLF = 1;
	public static final int LF_TO_CRLF = 2;
	public static final int CRLF_TO_CR = 3;
	public static final int CRLF_TO_LF = 4;

	private int byteSum = 0;
	private int inputIndex = 0;
	private DataInputStream inputDataStream;
	private int textConversion;

	private boolean currentLeftoverByte;
	private boolean delimiterHasStarted = false;
	private byte currentByte = ' ';

	public LabellerIntelHex()
	{
		this.byteSum = 0;

	}

	private int appendIntelHex(byte number, byte[] out, int index)
	{
		if ((int) number < 0)
		{
			return appendIntelHex(number + 256, out, index);
		}
		else
		{
			return appendIntelHex((int) number, out, index);
		}
	}

	private int appendIntelHex(int number, byte[] out, int index)
	{
		byte tempValue;

		this.byteSum += number;
		tempValue = (byte) ((number >> 4) % 16);
		out[index++] = (tempValue >= 10) ? (byte) (tempValue + 0x41 - 10) : (byte) (tempValue + 0x30);
		tempValue = (byte) (number % 16);
		out[index++] = (tempValue >= 10) ? (byte) (tempValue + 0x41 - 10) : (byte) (tempValue + 0x30);
		return (index);
	}

	public synchronized byte[] convertBlockFromIntelHex(byte[] input, int startIndex, int textConversion, boolean allowTrailingBytes) throws Exception
	{
		int bytesToBeRead; 

		boolean delimiterStarted = false;
		this.inputIndex = startIndex;
		if ((input.length - this.inputIndex) < 13)
		{
			throw new LabellerIntelHexError(994, "Less than 13 characters found in line, abborting.");
		}

		if (input[this.inputIndex++] != 0x3A)
		{
			throw new LabellerIntelHexError(994, "Expected : at the start of Intel Hex line, found " + input[this.inputIndex - 1]);
		}

		this.byteSum = 0;
		bytesToBeRead = fromHex(input, this.inputIndex);
		this.inputIndex += 2;

		fromHex(input, this.inputIndex);
		this.inputIndex += 2;
		fromHex(input, this.inputIndex);
		this.inputIndex += 2;
		fromHex(input, this.inputIndex);
		this.inputIndex += 2;

		if ((input.length - this.inputIndex) < bytesToBeRead + 2)
		{
			throw new LabellerIntelHexError(994, "Less than " + (bytesToBeRead + 2) + " characters found after record type, abborting.");
		}

		String outString = "";
		byte[] convertedBlock = new byte[bytesToBeRead];

		for (int i = 0; i < bytesToBeRead; i++)
		{
			int ch = fromHex(input, this.inputIndex);
			this.inputIndex += 2;
			switch (textConversion)
			{
			case LabellerIntelHex.NO_CONVERSION:
				convertedBlock[i] = (byte) ch;
				break;

			case LabellerIntelHex.CRLF_TO_CR:
				if (ch == 0x0D)
				{
					delimiterStarted = true;
					outString += (char) ch;
				}
				else if ((ch == 0xA) && delimiterStarted)
				{
					delimiterStarted = false; 
				}
				else
				{
					delimiterStarted = false;
					outString += (char) ch;
				}
				break;

			case LabellerIntelHex.CRLF_TO_LF:
				if (!delimiterStarted)
				{
					if (ch == 0x0D)
					{
						delimiterStarted = true;
					}
					else
					{
						outString += (char) ch; 
					}
				}
				else
				{ 
					if (ch == 0x0A)
					{
						delimiterStarted = false;
						outString += (char) ch;
					}
					else
					{
						outString += (char) 0x0D;
						if (ch != 0x0D)
						{
							delimiterStarted = false;
							outString += (char) ch;
						} 
					}
				} 
				break;

			case LabellerIntelHex.CR_TO_CRLF:
				outString += (char) ch;
				if (ch == 0x0D)
				{
					outString += (char) 0x0A;
				}
				break;

			case LabellerIntelHex.LF_TO_CRLF:
				if (ch == 0x0A)
				{
					outString += (char) 0x0D;
				}
				outString += (char) ch;
				break;
			}
		}

		int checkSum = (256 - (this.byteSum % 256)) % 256;
		if (fromHex(input, this.inputIndex) != checkSum)
		{
			throw new LabellerIntelHexError(993, "Checksum does not match, read " + fromHex(input, this.inputIndex) + ", computed " + checkSum);
		}
		this.inputIndex += 2;

		if ((input[this.inputIndex++] != 0x0D) || (input[this.inputIndex++] != 0x0A))
		{
			throw new LabellerIntelHexError(994, "Line not terminated with <CR><LF>");
		}
		if ((!allowTrailingBytes) && (input.length > inputIndex))
		{
			throw new LabellerIntelHexError(994, "Unexpected characters after <CR><LF>");
		}
		if (textConversion == LabellerIntelHex.NO_CONVERSION)
		{
			return convertedBlock;
		}
		return outString.getBytes();
	}

	private int fromHex(byte[] input, int index) throws Exception
	{
		byte highByte;
		byte lowByte;
		int returnValue;
		int i = index;

		if ((input.length - index) < 2)
		{
			throw new LabellerIntelHexError(994, "expected 2 bytes for conversion from hex to ansi, received " + (input.length - index));
		}

		if ((input[i] >= 0x30) && (input[i] <= 0x39))
		{
			highByte = (byte) (input[i] - 0x30);
		}
		else if ((input[i] >= 0x41) && (input[i] <= 0x46))
		{
			highByte = (byte) (input[i] - 0x41 + 10);
		}
		else if ((input[i] >= 0x61) && (input[i] <= 0x66))
		{
			highByte = (byte) (input[i] - 0x61 + 10);
		}
		else
		{
			throw new LabellerIntelHexError(994, "Unexpected value " + input[i] + " as high byte in intel hex value");
		}

		i++;
		if ((input[i] >= 0x30) && (input[i] <= 0x39))
		{
			lowByte = (byte) (input[i] - 0x30);
		}
		else if ((input[i] >= 0x41) && (input[i] <= 0x46))
		{
			lowByte = (byte) (input[i] - 0x41 + 10);
		}
		else if ((input[i] >= 0x61) && (input[i] <= 0x66))
		{
			lowByte = (byte) (input[i] - 0x61 + 10);
		}
		else
		{
			throw new LabellerIntelHexError(994, "Unexpected value " + input[i] + " as low byte in intel hex value");
		}

		returnValue = (highByte * 16 + lowByte);
		this.byteSum += returnValue;
		return (returnValue);
	}

	public synchronized void fromIntelHex(byte[] input, File outputFile, int textConversion) throws Exception
	{
		byte[] block;
		
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile)); // output
																										// stream

		boolean terminatorFound = false;
		try
		{
			this.inputIndex = 0;
			while (!terminatorFound)
			{
				if ((input.length - inputIndex) == 0)
				{
					outputStream.close();
					throw new LabellerIntelHexError(994, "Terminator block missing at the end of file, abborting.");
				}

				if ((block = convertBlockFromIntelHex(input, inputIndex, textConversion, true)).length == 0)
				{
					terminatorFound = true;
				}
				outputStream.write(block);

			}
			outputStream.close();
		}
		catch (Exception e)
		{
			outputStream.close();
			outputFile.delete();
			throw e;
		}

	}

	public void fromIntelHex(File inputFile, File outputFile) throws Exception
	{		
		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] input = new byte[inputStream.available()];
		inputStream.read(input);
		fromIntelHex(input, outputFile, NO_CONVERSION);
		inputStream.close();
	}


	public String getEOFBlock()
	{

		this.byteSum = 0;
		byte[] out = new byte[13];
		int outIndex = 0;
		out[outIndex++] = 0x3A;
		outIndex = appendIntelHex(0, out, outIndex);
		outIndex = appendIntelHex(0, out, outIndex);
		outIndex = appendIntelHex(0, out, outIndex);
		outIndex = appendIntelHex(0, out, outIndex);
		outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex);
		out[outIndex++] = 0x0D; // write end of line (<CR><LF>
		out[outIndex++] = 0x0A;
		return new String(out, 0, outIndex);
	}

	public String readIntelHexBlock() throws Exception
	{
		String block = null;
		final int LINE_SIZE = 1 + 2 + 4 + 2 + 510 + 2 + 2; 
		try
		{
			byte[] inputBuffer = new byte[255]; 
			byte[] out = new byte[LINE_SIZE];
			int outIndex = 0;

			int inputBufferIndex = 0;

			boolean bufferFull = false;
			if (inputDataStream.available() > 0)
			{
				bufferFull = false;
				inputBufferIndex = 0;
				
				while ((inputDataStream.available() > 0) && (inputBufferIndex < 255) && (!bufferFull))
				{
					if (!currentLeftoverByte)
					{
						currentByte = (byte) inputDataStream.read();
					}
					else
					{
						currentLeftoverByte = false;
					}
					
					switch (textConversion)
					{
					case LabellerIntelHex.NO_CONVERSION:
						inputBuffer[inputBufferIndex++] = currentByte;
						break;

					case LabellerIntelHex.CRLF_TO_CR:
						if (currentByte == 0x0D)
						{
							delimiterHasStarted = true;
							inputBuffer[inputBufferIndex++] = currentByte;
						}
						else if ((currentByte == 0xA) && delimiterHasStarted)
						{
							delimiterHasStarted = false; // just skip LF
						}
						else
						{
							delimiterHasStarted = false;
							inputBuffer[inputBufferIndex++] = currentByte;
						}
						break;

					case LabellerIntelHex.CR_TO_CRLF:
						inputBuffer[inputBufferIndex++] = currentByte;
						if (currentByte == 0x0D)
						{
							if (inputBufferIndex >= 255)
							{
								currentByte = 0x0A;
								currentLeftoverByte = true; 
								bufferFull = true;
							}
							else
							{
								inputBuffer[inputBufferIndex++] = 0x0A;
							}
						}
						break;

					case LabellerIntelHex.CRLF_TO_LF:
						if (delimiterHasStarted && (currentByte != 0x0A))
						{ 
							delimiterHasStarted = false;
							inputBuffer[inputBufferIndex++] = 0x0D; 
							if (inputBufferIndex >= 255)
							{
								currentLeftoverByte = true;
								bufferFull = true;
							}
							else
							{
								inputBuffer[inputBufferIndex++] = currentByte;
							}
						}
						else if (currentByte == 0x0D)
						{
							if (delimiterHasStarted)
							{
								inputBuffer[inputBufferIndex++] = 0x0D;
							}
							delimiterHasStarted = true;
						}
						else
						{
							delimiterHasStarted = false;
							inputBuffer[inputBufferIndex++] = currentByte;
						}
						break;

					case LabellerIntelHex.LF_TO_CRLF:
						if ((currentByte == 0x0A) && !(delimiterHasStarted))
						{
							inputBuffer[inputBufferIndex++] = 0x0D;
							if (inputBufferIndex >= 255)
							{
								currentLeftoverByte = true;
								bufferFull = true;
								delimiterHasStarted = true;
							}
							else
							{
								inputBuffer[inputBufferIndex++] = currentByte;
								delimiterHasStarted = false;
							}
						}
						else
						{
							inputBuffer[inputBufferIndex++] = currentByte;
							delimiterHasStarted = false;
						}
						break;
					}
				}

				if (inputBufferIndex == 0)
				{
					return null;
				}

				this.byteSum = 0;
				out[outIndex++] = 0x3A;
				outIndex = appendIntelHex(inputBufferIndex, out, outIndex); 
				outIndex = appendIntelHex(0, out, outIndex);
				outIndex = appendIntelHex(0, out, outIndex);
				outIndex = appendIntelHex(0, out, outIndex);
				for (int i = 0; i < inputBufferIndex; i++)
				{
					outIndex = appendIntelHex(inputBuffer[i], out, outIndex); 
				}
				outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex);
				out[outIndex++] = 0x0D; 
				out[outIndex++] = 0x0A;
				block = new String(out, 0, outIndex);
			}
		}
		catch (Exception e)
		{

			throw e;
		}
		return block;
	}

	public void prepareToConvertToIntelHex(File inputFile, int textConversion) throws Exception
	{
		try
		{
			inputDataStream = new DataInputStream(new FileInputStream(inputFile));
			this.textConversion = textConversion;
			this.delimiterHasStarted = false;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public byte[] toIntelHex(File inputFile) throws Exception
	{
		return (toIntelHex(inputFile, NO_CONVERSION));
	}

	public void toIntelHex(File inputFile, File outputFile) throws Exception
	{
		toIntelHex(inputFile, outputFile, NO_CONVERSION);
	}

	public void toIntelHex(File inputFile, File outputFile, int textConversion) throws Exception
	{
		byte[] out = toIntelHex(inputFile, textConversion);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		outputStream.write(out);
		outputStream.close();
	}

	public synchronized byte[] toIntelHex(File inputFile, int textConversion) throws Exception
	{
		final int LINE_SIZE = 1 + 2 + 4 + 2 + 510 + 2 + 2;

		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
		try
		{
			byte[] inputBuffer = new byte[255];

			byte[] out = new byte[LINE_SIZE];
			byte[] tmp;
			int outIndex = 0;

			byte ch = ' ';
			boolean leftoverByte = false;
			int inputBufferIndex = 0;
			boolean delimiterStarted = false;
			boolean bufferFull = false;
			while (inputStream.available() > 0)
			{
				bufferFull = false;
				inputBufferIndex = 0;
				while ((inputStream.available() > 0) && (inputBufferIndex < 255) && (!bufferFull))
				{
					if (!leftoverByte)
					{
						ch = (byte) inputStream.read();
					}
					else
					{ 
						leftoverByte = false;
					}
					switch (textConversion)
					{
					case LabellerIntelHex.NO_CONVERSION:
						inputBuffer[inputBufferIndex++] = ch;
						break;

					case LabellerIntelHex.CRLF_TO_CR:
						if (ch == 0x0D)
						{
							delimiterStarted = true;
							inputBuffer[inputBufferIndex++] = ch;
						}
						else if ((ch == 0xA) && delimiterStarted)
						{
							delimiterStarted = false; // skip LF
						}
						else
						{
							delimiterStarted = false;
							inputBuffer[inputBufferIndex++] = ch;
						}
						break;

					case LabellerIntelHex.CRLF_TO_LF:
						if (ch == 0x0D)
						{
							delimiterStarted = true;
							inputBuffer[inputBufferIndex++] = ch; 
						}
						else if ((ch == 0xA) && delimiterStarted)
						{
							delimiterStarted = false;
							inputBuffer[inputBufferIndex - 1] = ch;
						}
						else
						{
							delimiterStarted = false;
							inputBuffer[inputBufferIndex++] = ch;
						}
						break;

					case LabellerIntelHex.CR_TO_CRLF:
						inputBuffer[inputBufferIndex++] = ch;
						if (ch == 0x0D)
						{
							if (inputBufferIndex >= 255)
							{
								leftoverByte = true; 
								bufferFull = true;
							}
							else
							{
								inputBuffer[inputBufferIndex++] = 0x0A;
							}
						}
						break;

					case LabellerIntelHex.LF_TO_CRLF:
						if (ch == 0x0A)
						{
							if (inputBufferIndex >= 254)
							{
								leftoverByte = true;
								bufferFull = true;
							}
							else
							{
								inputBuffer[inputBufferIndex++] = 0x0D;
								inputBuffer[inputBufferIndex++] = ch;
							}
						}
						else
						{
							inputBuffer[inputBufferIndex++] = ch;
						}
						break;
					}
				}

				this.byteSum = 0;
				out[outIndex++] = 0x3A;
				outIndex = appendIntelHex(inputBufferIndex, out, outIndex);
				outIndex = appendIntelHex(0, out, outIndex);
				outIndex = appendIntelHex(0, out, outIndex);
				outIndex = appendIntelHex(0, out, outIndex);
				for (int i = 0; i < inputBufferIndex; i++)
				{
					outIndex = appendIntelHex(inputBuffer[i], out, outIndex);
				}
				outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex);

				out[outIndex++] = 0x0D; 
				out[outIndex++] = 0x0A;

				tmp = out;
				out = new byte[outIndex + LINE_SIZE];
				for (int i = 0; i < outIndex; i++)
				{
					out[i] = tmp[i];
				}

			}

			this.byteSum = 0;
			out[outIndex++] = 0x3A;
			outIndex = appendIntelHex(0, out, outIndex);
			outIndex = appendIntelHex(0, out, outIndex);

			outIndex = appendIntelHex(0, out, outIndex);
			outIndex = appendIntelHex(0, out, outIndex);
			outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex);
			out[outIndex++] = 0x0D;
			out[outIndex++] = 0x0A;
			inputStream.close();

			tmp = out;
			out = new byte[outIndex];
			for (int i = 0; i < outIndex; i++)
			{
				out[i] = tmp[i];
			}
			return (out);
		} // try
		catch (Exception e)
		{

			throw e;
		}
	}

	public void toIntelHex(String inputPathFilename, String outputPathFilename) throws Exception
	{
		toIntelHex(inputPathFilename, outputPathFilename, NO_CONVERSION);
	}

	public void toIntelHex(String inputPathFilename, String outputPathFilename, int textConversion) throws Exception
	{
		File inputFile = new File(inputPathFilename);
		File outputFile = new File(outputPathFilename);
		toIntelHex(inputFile, outputFile, textConversion);
	}

	public void toIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename) throws Exception
	{
		toIntelHex(inputPath, inputFilename, outputPath, outputFilename, NO_CONVERSION);
	}

	public void toIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename, int textConverstion) throws Exception
	{
		File inputFile = new File(inputPath, inputFilename);
		File outputFile = new File(outputPath, outputFilename);
		toIntelHex(inputFile, outputFile, textConverstion);
	}

}