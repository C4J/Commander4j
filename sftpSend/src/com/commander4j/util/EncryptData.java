package com.commander4j.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EncryptData
{
	public static String key = "wedkjh78687hgaFHDKqNKJGyfed;lkj<";

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		System.out.println("Password Utility");
		System.out.println("");
		System.out.print("Input data to encrypt :");

		Scanner in = new Scanner(System.in);

		String s = in.nextLine();

		in.close();

		System.out.println("");
		System.out.println("You entered [" + s+"]");
		System.out.println("");
		
		JCipher cipher = new JCipher(EncryptData.key);

		String encrypted = cipher.encode(s);

		try
		{
			FileWriter myWriter = new FileWriter("password.txt");
			
			myWriter.write("Encrypted password is contained within the [ ] below.");
			myWriter.write(System.lineSeparator());
			myWriter.write(System.lineSeparator());
			myWriter.write("Copy and paste into the sftpSend.xml to protect authentication passwords.");
			myWriter.write(System.lineSeparator());
			myWriter.write(System.lineSeparator());
			myWriter.write("[" + encrypted + "]");
			myWriter.write(System.lineSeparator());
			myWriter.write(System.lineSeparator());
			myWriter.flush();
			myWriter.close();
			System.out.println("Encrypted password saved to password.txt");
		}
		catch (IOException e)
		{
			System.out.println("Error :"+e.getMessage());
		}
	}

}
