package com.commander4j.util;

/**
 */
public class JUnique
{

	/**
	 * Field NUM_CHARS. (value is 26) Value: {@value NUM_CHARS}
	 */
	private static final int NUM_CHARS = 26;
	/**
	 * Field lastString. Value: {@value lastString}
	 */
	private static int[] lastString = new int[NUM_CHARS];
	/**
	 * Field CHARS. (value is
	 * ""0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"")
	 * Value: {@value CHARS}
	 */
	private static final String CHARS = "0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Method getUnique.
	 * 
	 * @return String
	 */
	public static String getUniqueID() {
		return java.util.UUID.randomUUID().toString();
	}

	static String getUnique() {

		char[] buf = new char[NUM_CHARS];

		carry(lastString, buf.length - 1);
		for (int i = 0; i < buf.length; i++)
		{
			buf[i] = CHARS.charAt(lastString[i]);
		}
		return new String(buf);
	}

	/**
	 * Method carry.
	 * 
	 * @param ca
	 *            int[]
	 * @param index
	 *            int
	 */
	private static void carry(int[] ca, int index) {
		if (ca[index] == (CHARS.length() - 1))
		{
			ca[index] = 0;
			carry(ca, --index);
		}
		else
		{
			ca[index] = ca[index] + 1;
		}
	}

}
