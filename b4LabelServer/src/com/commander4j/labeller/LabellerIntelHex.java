package com.commander4j.labeller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//import org.apache.logging.log4j.Logger;

public class LabellerIntelHex
{

	public static final int NO_CONVERSION = 0;
	public static final int CR_TO_CRLF = 1;
	public static final int LF_TO_CRLF = 2;
	public static final int CRLF_TO_CR = 3;
	public static final int CRLF_TO_LF = 4;

	private int byteSum = 0; // variable that counts the hex values of the bytes
								// written to the output string, used to compute
								// the check sum
	private int inputIndex = 0; // index of the current pointer of the input
								// byte array, incremented by
								// convertBlockFromIntelHex
	private DataInputStream ins;
	private int textConversion;
	//private File currentInputFile;
	private boolean currentLeftoverByte;
	private boolean delimiterHasStarted = false;
	private byte currentByte = ' '; // current byte read from the input stream
	// private Logger logger =
	// org.apache.logging.log4j.LogManager.getLogger((LabellerIntelHex.class));

	public LabellerIntelHex()
	{
		this.byteSum = 0;

	}

	/**
	 * Main method, used to debug the class only
	 *
	 * @param args,
	 *            array of argument strings
	 * @return void
	 */
	public static void main(String[] args)
	{
		LabellerIntelHex ihc = new LabellerIntelHex();
		try
		{
			String outfilename = System.getProperty("user.dir") + java.io.File.separator + "labeller_files" + java.io.File.separator + "out.hex";
			String infilename = System.getProperty("user.dir") + java.io.File.separator + "labeller_files" + java.io.File.separator + "file3.txt";
			DataOutputStream fouts = new DataOutputStream(new FileOutputStream(outfilename));

			ihc.prepareToConvertToIntelHex(new File(infilename), LabellerIntelHex.CRLF_TO_CR);
			String block = ihc.getNextIntelHexBlock();
			while (block != null)
			{
				fouts.writeBytes(block);
				System.out.println(block.toString());
				fouts.flush();
				block = ihc.getNextIntelHexBlock();
			}
			fouts.writeBytes(ihc.getEOFBlock());
			System.out.println(ihc.getEOFBlock());
			fouts.close();
			// test result: all output files ascii.* must be identical to the
			// input file ascii.input, as long as
			// ascii.input does not contain single CRs and LFs
		}
		catch (Exception e)
		{
			// logger.error(e.getMessage(), e);
		}
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputPathFilename
	 *            fully qualified name of the input file (path and filename)
	 * @param outputPathFilename
	 *            fully qualified name of the output file (path and filename)
	 * @param textConversion
	 *            indicator whether line termination should be changed
	 */
	public void convertToIntelHex(String inputPathFilename, String outputPathFilename, int textConversion) throws Exception
	{
		File inputFile = new File(inputPathFilename);
		File outputFile = new File(outputPathFilename);
		convertToIntelHex(inputFile, outputFile, textConversion);
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputPathFilename
	 *            fully qualified name of the input file (path and filename)
	 * @param outputPathFilename
	 *            fully qualified name of the output file (path and filename)
	 */
	public void convertToIntelHex(String inputPathFilename, String outputPathFilename) throws Exception
	{
		convertToIntelHex(inputPathFilename, outputPathFilename, NO_CONVERSION);
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputPath
	 *            path of the input file
	 * @param inputFilename
	 *            name of the input file
	 * @param outputPath
	 *            path of the output file
	 * @param outputFilename
	 *            name of the output file
	 * @param textConverstion
	 *            indicator whether line termination should be changed
	 */
	public void convertToIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename, int textConverstion) throws Exception
	{
		File inputFile = new File(inputPath, inputFilename);
		File outputFile = new File(outputPath, outputFilename);
		convertToIntelHex(inputFile, outputFile, textConverstion);
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputPath
	 *            path of the input file
	 * @param inputFilename
	 *            name of the input file
	 * @param outputPath
	 *            path of the output file
	 * @param outputFilename
	 *            name of the output file
	 */
	public void convertToIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename) throws Exception
	{
		convertToIntelHex(inputPath, inputFilename, outputPath, outputFilename, NO_CONVERSION);
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputFile
	 *            input file
	 * @param outputFile
	 *            output file
	 */
	public void convertToIntelHex(File inputFile, File outputFile) throws Exception
	{
		convertToIntelHex(inputFile, outputFile, NO_CONVERSION);
	}

	/**
	 * converts an input file into an output file with intel hex format
	 *
	 * @param inputFile
	 *            input file
	 * @param outputFile
	 *            output file
	 * @param textConversion
	 *            indicator whether line termination should be changed
	 */
	public void convertToIntelHex(File inputFile, File outputFile, int textConversion) throws Exception
	{
		byte[] out = convertToIntelHex(inputFile, textConversion);
		FileOutputStream outputStream = new FileOutputStream(outputFile); // output
																			// stream
		outputStream.write(out);
		outputStream.close();
	}

	/**
	 * converts an input file into an byte array with intel hex format
	 *
	 * @param inputFile
	 *            input file
	 * @returns byte[] out bytearray of converted data
	 */
	public byte[] convertToIntelHex(File inputFile) throws Exception
	{
		return (convertToIntelHex(inputFile, NO_CONVERSION));
	}

	/**
	 * converts an input file into an output file with intel hex format NOTE:
	 * the conversion does only cover the requirements for the LOGOPAK LEAP and
	 * PowerLEAP2 systems It does not set the memory addresses they are always
	 * set to "0000" Hence only one type of data is supported: 00: normal data
	 * with address "0000" The types 01, 03 and 04 are not used.
	 *
	 * General conversion scheme: read 255 bytes (or less for the last record)
	 * from the input file generate a record with the format
	 * :llaaaattdddddd.....ddccee the characters have the following meaning: :
	 * ":" ll # data bytes as ascii encoded number aaaa memory address of the
	 * record, here always "0000" tt record type "00" - data record "01" End of
	 * file record "03" and "04" are not supported dd ascii encoded hex number
	 * for a byte of the input file e.g. '0' -> "30", 'x' -> "78" cc check sum
	 *
	 * @param inputFile
	 *            input file
	 * @param textConversion
	 *            conversion of end-of-line chars
	 * @returns byte[] out bytearray of converted data
	 */
	public synchronized byte[] convertToIntelHex(File inputFile, int textConversion) throws Exception
	{
		final int LINE_SIZE = 1 + 2 + 4 + 2 + 510 + 2 + 2; // maximum size of
															// one line
		// : ll aaaa tt dd cc <CR><LF>
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile)); // input
																									// stream

		try
		{
			byte[] inputBuffer = new byte[255]; // buffer of unconverted bytes
												// read from the file
			byte[] out = new byte[LINE_SIZE];
			byte[] tmp; // temp array, used to enlarge and trim out
			int outIndex = 0;

			byte ch = ' '; // current byte read from the input stream
			boolean leftoverByte = false;
			int inputBufferIndex = 0;
			boolean delimiterStarted = false;
			boolean bufferFull = false;
			while (inputStream.available() > 0)
			{ // while there is still data in the input file
				bufferFull = false;
				inputBufferIndex = 0;
				// read one line at a time into the input buffer
				while ((inputStream.available() > 0) && (inputBufferIndex < 255) && (!bufferFull))
				{
					if (!leftoverByte)
					{
						ch = (byte) inputStream.read();
					}
					else
					{ // don't read from input, use the leftover byte from the
						// last record
						leftoverByte = false;
					}
					// ch is now the current character
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
							inputBuffer[inputBufferIndex++] = ch; // write CR
																	// for now
																	// might be
																	// overwritten
						}
						else if ((ch == 0xA) && delimiterStarted)
						{
							delimiterStarted = false;
							inputBuffer[inputBufferIndex - 1] = ch; // replace
																	// last CR
																	// by LF
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
								leftoverByte = true; // save the extra character
														// for the next record
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
								leftoverByte = true; // save the extra character
														// for the next record
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

				// now we have read <inputBufferIndex> bytes into inputBuffer
				// and did the linedelimiter conversions
				// write the intel hex record to the byte array out

				this.byteSum = 0; // reset the byteSum for the current line,
									// incremented in appendIntelHex
				out[outIndex++] = 0x3A; // write the ':' as start of the line
				outIndex = appendIntelHex(inputBufferIndex, out, outIndex); // write
																			// length
																			// ll
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (first part
																// of address
																// aaaa)
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (second part
																// of address
																// aaaa)
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (datatype tt
																// - data
																// record)
				for (int i = 0; i < inputBufferIndex; i++)
				{
					outIndex = appendIntelHex(inputBuffer[i], out, outIndex); // write
																				// data
																				// dd
				}
				outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex); // write
																								// the
																								// checksum
																								// cc
																								// =
																								// 2-complement
																								// of
																								// this.byteSum
				out[outIndex++] = 0x0D; // write end of line (<CR><LF>
				out[outIndex++] = 0x0A;

				// add space for another line at the end of the array
				tmp = out;
				out = new byte[outIndex + LINE_SIZE];
				for (int i = 0; i < outIndex; i++)
				{
					out[i] = tmp[i];
				}

			} // while still data in file
				// all data written, now write eof record
				// NOTE: this record is non standard. It also has the type "00"
				// not "01"!
			this.byteSum = 0; // reset the byteSum for the last line,
								// incremented in appendIntelHex
			out[outIndex++] = 0x3A; // write the ':' as start of the line
			outIndex = appendIntelHex(0, out, outIndex); // write length ll
															// ("00")
			outIndex = appendIntelHex(0, out, outIndex); // write "00" (first
															// part of address
															// aaaa)
			outIndex = appendIntelHex(0, out, outIndex); // write "00" (second
															// part of address
															// aaaa)
			outIndex = appendIntelHex(0, out, outIndex); // write "00" (datatype
															// tt - EOF record)
			outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex); // write
																							// the
																							// checksum
																							// cc
																							// =
																							// 2-complement
																							// of
																							// this.byteSum
			out[outIndex++] = 0x0D; // write end of line (<CR><LF>
			out[outIndex++] = 0x0A;
			inputStream.close();

			// trim the outputarray to the required size
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
			// logger.error("Failed to convert file {} to INTELHEX. Exception:
			// {} ", inputFile.getName(), e);
			throw e;
		}
	}

	/**
	 * convert an integer to a 2 digit unsigned hex number and appends it to the
	 * intel hex output array NOTE: increments this.byteSum as a side effect
	 *
	 * @param number
	 *            one byte to be converted to intel hex
	 * @param out
	 *            intel hex array, the data is to be appended
	 * @param index
	 *            index where the next byte should be written
	 * @return index to the next element in the output array
	 */
	private int appendIntelHex(int number, byte[] out, int index)
	{
		byte tempValue;

		this.byteSum += number; // increment the sum of bytes converted, used
								// for the checksum computation
		tempValue = (byte) ((number >> 4) % 16);
		out[index++] = (tempValue >= 10) ? (byte) (tempValue + 0x41 - 10) : (byte) (tempValue + 0x30);
		tempValue = (byte) (number % 16);
		out[index++] = (tempValue >= 10) ? (byte) (tempValue + 0x41 - 10) : (byte) (tempValue + 0x30);
		return (index);
	}

	/**
	 * convert a byte to a 2 digit unsigned hex number and return it in a byte
	 * array NOTE: increments this.byteSum as a side effect
	 *
	 * @param number
	 *            one byte to be converted to intel hex
	 * @param out
	 *            intel hex array, the data is to be appended
	 * @param index
	 *            index where the next byte should be written
	 * @return index to the next element in the output array
	 */
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

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputPathFilename
	 *            fully qualified name of the input file (path and filename)
	 * @param outputPathFilename
	 *            fully qualified name of the output file (path and filename)
	 * @param textConversion
	 *            indicator whether line termination should be changed
	 */
	public void convertFromIntelHex(String inputPathFilename, String outputPathFilename, int textConversion) throws Exception
	{
		File inputFile = new File(inputPathFilename);
		File outputFile = new File(outputPathFilename);
		convertFromIntelHex(inputFile, outputFile, textConversion);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputPathFilename
	 *            fully qualified name of the input file (path and filename)
	 * @param outputPathFilename
	 *            fully qualified name of the output file (path and filename)
	 */
	public void convertFromIntelHex(String inputPathFilename, String outputPathFilename) throws Exception
	{
		convertFromIntelHex(inputPathFilename, outputPathFilename, NO_CONVERSION);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputPath
	 *            path of the input file
	 * @param inputFilename
	 *            name of the input file
	 * @param outputPath
	 *            path of the output file
	 * @param outputFilename
	 *            name of the output file
	 * @param textConverstion
	 *            indicator whether line termination should be changed
	 */
	public void convertFromIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename, int textConverstion) throws Exception
	{
		File inputFile = new File(inputPath, inputFilename);
		File outputFile = new File(outputPath, outputFilename);
		convertFromIntelHex(inputFile, outputFile, textConverstion);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputPath
	 *            path of the input file
	 * @param inputFilename
	 *            name of the input file
	 * @param outputPath
	 *            path of the output file
	 * @param outputFilename
	 *            name of the output file
	 */
	public void convertFromIntelHex(String inputPath, String inputFilename, String outputPath, String outputFilename) throws Exception
	{
		convertFromIntelHex(inputPath, inputFilename, outputPath, outputFilename, NO_CONVERSION);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputFile
	 *            input file
	 * @param outputFile
	 *            output file
	 */
	public void convertFromIntelHex(File inputFile, File outputFile) throws Exception
	{
		convertFromIntelHex(inputFile, outputFile, NO_CONVERSION);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 *
	 * @param inputFile
	 *            input file
	 * @param outputFile
	 *            output file
	 * @param textConversion
	 *            indicator whether line termination should be changed
	 */
	public void convertFromIntelHex(File inputFile, File outputFile, int textConversion) throws Exception
	{
		FileInputStream inputStream = new FileInputStream(inputFile); // input
																		// stream
		byte[] input = new byte[inputStream.available()];
		inputStream.read(input); // read all bytes from the input stream to the
									// buffer
		convertFromIntelHex(input, outputFile, textConversion); // create an
																// ascii file
																// from the
																// buffer
		inputStream.close();
	}

	/**
	 * converts an input with intel hex format back into the original format
	 * without format conversion
	 *
	 * @param input
	 *            input byte in intel hex format
	 * @param outputFile
	 *            output file
	 */
	public void convertFromIntelHex(byte[] input, File outputFile) throws Exception
	{
		convertFromIntelHex(input, outputFile, NO_CONVERSION);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 * this reverses the method convertToIntelHex
	 *
	 * @param input
	 *            input byte in intel hex format
	 * @param outputFile
	 *            output file
	 * @param textConversion
	 *            conversion of end-of-line chars
	 */
	public synchronized void convertFromIntelHex(byte[] input, File outputFile, int textConversion) throws Exception
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

			} // read input line by line
			outputStream.close();
		}
		catch (Exception e)
		{ // clean up the files and stream and pass the exception on
			outputStream.close();
			outputFile.delete();
			throw e;
		}

	}

	/**
	 * converts an byte array with intel hex format back into the original
	 * format
	 *
	 * @param inputString
	 *            input bytes as String in intel hex format
	 * @param textConversion
	 *            conversion of end-of-line chars
	 * @returns String converted text
	 */
	public synchronized byte[] convertBlockFromIntelHex(String inputString, int textConversion) throws Exception
	{
		return convertBlockFromIntelHex(inputString.getBytes(), 0, textConversion, false);
	}

	/**
	 * converts an byte array with intel hex format back into the original
	 * format
	 *
	 * @param inputString
	 *            input bytes as String in intel hex format
	 * @param inputIndex
	 *            Start of the substring to be converted
	 * @param textConversion
	 *            conversion of end-of-line chars
	 * @param allowTrailingBytes
	 *            false: trailing unused bytes lead to an exception
	 * @returns String converted text
	 */
	public synchronized byte[] convertBlockFromIntelHex(String inputString, int inputIndex, int textConversion, boolean allowTrailingBytes) throws Exception
	{
		return convertBlockFromIntelHex(inputString.substring(inputIndex).getBytes(), inputIndex, textConversion, allowTrailingBytes);
	}

	/**
	 * converts an byte array with intel hex format back into the original
	 * format
	 *
	 * @param input
	 *            input byte in intel hex format
	 * @param textConversion
	 *            conversion of end-of-line chars
	 * @returns String converted text
	 */
	public synchronized byte[] convertBlockFromIntelHex(byte[] input, int textConversion) throws Exception
	{
		return convertBlockFromIntelHex(input, 0, textConversion, false);
	}

	/**
	 * converts an input with intel hex format back into the original format
	 * this reverses the method convertToIntelHex NOTE: the conversion does only
	 * cover the requirements for the LOGOPAK LEAP and PowerLEAP2 systems memory
	 * addesses are ignored, the lines are concatenated to create the new file
	 * also the data type is ignored, all datatypes are expected to be "00"
	 *
	 * General conversion scheme: read read as many bytes as specified in the ll
	 * section (255 chars max) and transform each two hex characters into the
	 * ansi character equivatlent.
	 *
	 * The lines read have the format :llaaaattdddddd.....ddccee the characters
	 * have the following meaning: : ":" ll # data bytes as ascii encoded number
	 * aaaa memory address of the record, here always "0000", skipped tt record
	 * type "00" - data record "01" End of file record "03" and "04" are not
	 * supported, skipped dd ascii encoded hex number for a byte of the input
	 * file e.g. "30" -> '0', "78" -> 'x' cc check sum 2s-complement of the sum
	 * of all values ll to dd <CR><LF> line terminator
	 *
	 * @param input
	 *            input byte in intel hex format
	 * @param startIndex
	 *            pointer to the fisrt character to be converted
	 * @param textConversion
	 *            conversion of end-of-line chars
	 * @param allowTrailingBytes
	 *            false: trailing unused bytes lead to an exception
	 * @returns byte[] converted block
	 */
	public synchronized byte[] convertBlockFromIntelHex(byte[] input, int startIndex, int textConversion, boolean allowTrailingBytes) throws Exception
	{
		int bytesToBeRead; // bytes to be read in the current go 0 - 255

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
		bytesToBeRead = convertFromHex(input, this.inputIndex); // now we know
																// the number of
																// records to be
																// read in this
																// record
		this.inputIndex += 2;

		convertFromHex(input, this.inputIndex); // skip 6 bytes address and
												// record type, but compute the
												// checksum
		this.inputIndex += 2;
		convertFromHex(input, this.inputIndex);
		this.inputIndex += 2;
		convertFromHex(input, this.inputIndex);
		this.inputIndex += 2;

		if ((input.length - this.inputIndex) < bytesToBeRead + 2)
		{
			throw new LabellerIntelHexError(994, "Less than " + (bytesToBeRead + 2) + " characters found after record type, abborting.");
		}

		String outString = "";
		byte[] convertedBlock = new byte[bytesToBeRead];

		for (int i = 0; i < bytesToBeRead; i++)
		{ // read the content and convert it to ansi
			int ch = convertFromHex(input, this.inputIndex);
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
					delimiterStarted = false; // skip LF
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
						delimiterStarted = true; // skip CR for now, will be
													// written later, if the
													// next byte is not LF
					}
					else
					{
						outString += (char) ch; // standard case copy byte
					}
				}
				else
				{ // delimiterStarted == true
					if (ch == 0x0A)
					{ // <CR> followed by <LF>
						delimiterStarted = false;
						outString += (char) ch;
					}
					else
					{ // <CR> not followed <LF>
						outString += (char) 0x0D; // write the <CR> that was
													// skipped before
						if (ch != 0x0D)
						{ // second byte not <CR> -> write to output
							delimiterStarted = false;
							outString += (char) ch;
						} // special case: <CR><CR>, skip the second <CR>, do
							// nothing
					}
				} // delimiterStarted == true
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
		// compare the checksum in the record with the value computed by
		// convertFromHex
		int checkSum = (256 - (this.byteSum % 256)) % 256;
		if (convertFromHex(input, this.inputIndex) != checkSum)
		{
			throw new LabellerIntelHexError(993, "Checksum does not match, read " + convertFromHex(input, this.inputIndex) + ", computed " + checkSum);
		}
		this.inputIndex += 2;

		// read the trailind <CR><LF>
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

	/**
	 * convert two bytes to the ansi character of the matching hex value
	 *
	 * @param input
	 *            input array with intel hex data
	 * @param index
	 *            position of the first of the two bytes to be converted
	 * @return byte[] inputs hex representation
	 */
	private int convertFromHex(byte[] input, int index) throws Exception
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

	/*
	 * This method gets the bytes from filestream and bytes from additionalText
	 * and puts it in a new stream.
	 *
	 * @param additionalText an byte[] with additional text
	 */
	public void prepareToConvertToIntelHex(File inputFile, int textConversion, byte[] additionalText) throws Exception
	{
		try
		{
			DataInputStream fileStream = new DataInputStream(new FileInputStream(inputFile));

			byte[] readAll = new byte[fileStream.available() + additionalText.length];
			byte[] fileRead = new byte[fileStream.available()];
			fileStream.readFully(fileRead);

			System.arraycopy(fileRead, 0, readAll, 0, fileRead.length - 1);
			System.arraycopy(additionalText, 0, readAll, fileRead.length - 1, additionalText.length - 1);
			ByteArrayInputStream bis = new ByteArrayInputStream(readAll);
			ins = new DataInputStream(bis);

			this.textConversion = textConversion;
			//this.currentInputFile = null;
			this.delimiterHasStarted = false;
			fileStream.close();
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public void prepareToConvertToIntelHex(File inputFile, int textConversion) throws Exception
	{
		try
		{
			ins = new DataInputStream(new FileInputStream(inputFile));
			this.textConversion = textConversion;
			//this.currentInputFile = inputFile;
			this.delimiterHasStarted = false;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public String getNextIntelHexBlock() throws Exception
	{
		String block = null;
		final int LINE_SIZE = 1 + 2 + 4 + 2 + 510 + 2 + 2; // maximum size of
															// one line
		// : ll aaaa tt dd cc <CR><LF>
		try
		{
			byte[] inputBuffer = new byte[255]; // buffer of unconverted bytes
												// read from the file
			byte[] out = new byte[LINE_SIZE];
			int outIndex = 0;

			int inputBufferIndex = 0;

			boolean bufferFull = false;
			if (ins.available() > 0)
			{ // if there is still data in the input file
				bufferFull = false;
				inputBufferIndex = 0;
				// read one line at a time into the input buffer
				while ((ins.available() > 0) && (inputBufferIndex < 255) && (!bufferFull))
				{
					if (!currentLeftoverByte)
					{
						currentByte = (byte) ins.read();
					}
					else
					{ // don't read from input, use the leftover byte from the
						// last record
						currentLeftoverByte = false;
					}
					// ch is now the current character
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
								currentLeftoverByte = true; // save the extra
															// character for the
															// next record
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
						{ // CR has been read before, current byte is no LF
							delimiterHasStarted = false;
							inputBuffer[inputBufferIndex++] = 0x0D; // write CR
																	// from
																	// previous
																	// reading
																	// operation
							if (inputBufferIndex >= 255)
							{ // check buffer size
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
						{ // LF found and leading CR has not been already
							// written to previous block
							inputBuffer[inputBufferIndex++] = 0x0D; // write
																	// leading
																	// CR
							if (inputBufferIndex >= 255)
							{ // check buffer size
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

				// now we have read <inputBufferIndex> bytes into inputBuffer
				// and did the linedelimiter conversions
				// write the intel hex record to the byte array out

				this.byteSum = 0; // reset the byteSum for the current line,
									// incremented in appendIntelHex
				out[outIndex++] = 0x3A; // write the ':' as start of the line
				outIndex = appendIntelHex(inputBufferIndex, out, outIndex); // write
																			// length
																			// ll
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (first part
																// of address
																// aaaa)
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (second part
																// of address
																// aaaa)
				outIndex = appendIntelHex(0, out, outIndex); // write "00"
																// (datatype tt
																// - data
																// record)
				for (int i = 0; i < inputBufferIndex; i++)
				{
					outIndex = appendIntelHex(inputBuffer[i], out, outIndex); // write
																				// data
																				// dd
				}
				outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex); // write
																								// the
																								// checksum
																								// cc
																								// =
																								// 2-complement
																								// of
																								// this.byteSum
				out[outIndex++] = 0x0D; // write end of line (<CR><LF>
				out[outIndex++] = 0x0A;
				block = new String(out, 0, outIndex);
			}
		}
		catch (Exception e)
		{
			// logger.error("Failed to convert block from file {} to INTELHEX.
			// Exception: {} ", currentInputFile.getName(), e);
			throw e;
		}
		return block;
	}

	public String getEOFBlock()
	{
		// all data written, now write eof record
		// NOTE: this record is non standard. It also has the type "00" not
		// "01"!
		this.byteSum = 0; // reset the byteSum for the last line, incremented in
							// appendIntelHex
		byte[] out = new byte[13];
		int outIndex = 0;
		out[outIndex++] = 0x3A; // write the ':' as start of the line
		outIndex = appendIntelHex(0, out, outIndex); // write length ll ("00")
		outIndex = appendIntelHex(0, out, outIndex); // write "00" (first part
														// of address aaaa)
		outIndex = appendIntelHex(0, out, outIndex); // write "00" (second part
														// of address aaaa)
		outIndex = appendIntelHex(0, out, outIndex); // write "00" (datatype tt
														// - EOF record)
		outIndex = appendIntelHex((256 - (this.byteSum % 256) % 256), out, outIndex); // write
																						// the
																						// checksum
																						// cc
																						// =
																						// 2-complement
																						// of
																						// this.byteSum
		out[outIndex++] = 0x0D; // write end of line (<CR><LF>
		out[outIndex++] = 0x0A;
		return new String(out, 0, outIndex);
	}

	public void closeCurrentInputFile()
	{
		try
		{
			ins.close();
		}
		catch (Exception ex)
		{
			// logger.error("Failed to close file {} after conversion to
			// IntelHex. Exception: {} ", currentInputFile.getName(), ex);
		}
	}
}