package generator;

import java.util.Random;

public class RandomData {
	
	public static Object randomValue(final Object... array) {
		if(array.length == 0) {
			throw new IllegalArgumentException("Array must contain elements.");
		}
		return array[new Random().nextInt(array.length)];
	}

	public static String randomValueAsString(final Object... array) {
		return randomValue(array).toString();
	}

	public static int randomInteger(int min, int max) {
	    return (new Random()).nextInt((max - min) + 1) + min;
	}

	public static String randomIntegerAsString(int min, int max) {
	    return Integer.toString(randomInteger(min, max));
	}

}
