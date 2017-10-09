package net.jay.lang;

import java.io.File;

public class Validator {
	public static boolean validateFileExists(String fileName) {
		File file = new File(fileName);
		if(file.exists() && !file.isDirectory())
			return true;
		return false;
	}
}
