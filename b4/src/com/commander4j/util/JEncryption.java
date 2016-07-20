package com.commander4j.util;

/**
 */
public class JEncryption
{

	/**
	 * Field from. Value: {@value from}
	 */
	public static String from = "abcdefghijklmnopqrstuvwxyz1234567890";
	/**
	 * Field to. Value: {@value to}
	 */
	public static String to = "mnopqrstuvwxyz1234567890abcdefghijkl";

	/**
	 * Method encrypt.
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String encrypt(String s) {
		String result = "";

		if (s.equals(null) == false)
		{
			int temp = s.length();
			for (int p = 0; p < temp; p++)
			{
				char chr = s.charAt(p);
				int i = (int) chr;
				String hexstr = Integer.toString(i, 16);
				result = result + hexstr;
			}
		}

		result = substitute(result, from, to);
		return result;
	}

	/**
	 * Method decrypt.
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String decrypt(String s) {
		String result = "";

		try
		{
			s = substitute(s, to, from);

			if (s.equals(null) == false)
			{
				int len = s.length();
				for (int p = 0; p < len; p = p + 2)
				{
					String hexstr = s.substring(p, p + 2);
					int i = Integer.valueOf(hexstr, 16).intValue();
					String chr = new Character((char) i).toString();
					result = result + chr;
				}
			}
		}
		catch (Exception e)
		{
			result = "error";
		}

		return result;
	}

	/**
	 * Method substitute.
	 * 
	 * @param s
	 *            String
	 * @param from
	 *            String
	 * @param to
	 *            String
	 * @return String
	 */
	private static String substitute(String s, String from, String to) {
		String result = "";

		for (int i = 0; i < s.length(); i = i + 1)
		{ // for as many letters
			// as there are
			char ch = s.charAt(i); // access each letter in message
			int index = from.indexOf(ch); // find its position in alphabet
			if (index == -1)
			{ // if it's not a capital letter,
				result = result + ch; // then leave it as is & add
			} // otherwise,
			else
			{ // find the corresponding
				result = result + to.charAt(index); // letter in the key & add
			}
		}
		return result;
	}

}
