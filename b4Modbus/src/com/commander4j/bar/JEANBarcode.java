package com.commander4j.bar;

public class JEANBarcode
{

	public String calcCheckdigit(String barcodeData) {
		String result = "";

		int barcodeSize = barcodeData.length();

		char barcodeCharacter;
		int barcodeASCIIvalue;
		int step1 = 0;
		int step2 = 0;
		int step3 = 0;
		int step4 = 0;
		int step5 = 0;

		for (int parseLocation = (barcodeSize - 1); parseLocation >= 0; parseLocation = parseLocation - 2) {
			barcodeCharacter = barcodeData.charAt(parseLocation);
			barcodeASCIIvalue = Integer.valueOf(String.valueOf(barcodeCharacter)).intValue();
			step1 = step1 + barcodeASCIIvalue;

			if (parseLocation > 0) {
				barcodeCharacter = barcodeData.charAt(parseLocation - 1);
				barcodeASCIIvalue = Integer.valueOf(String.valueOf(barcodeCharacter)).intValue();
				step3 = step3 + barcodeASCIIvalue;
			}
		}

		step2 = (step1 * 3);

		step4 = step2 + step3;

		while (((step4 + step5) % 10) != 0) {
			step5++;
		}

		result = String.valueOf(step5);

		return result;
	}
}
