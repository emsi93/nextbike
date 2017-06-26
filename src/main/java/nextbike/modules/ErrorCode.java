package nextbike.modules;

import java.util.Random;

public class ErrorCode {

	public static int getErrorCode()
	{
		Random rand = new Random();
		int number = rand.nextInt((500 - 100) + 1) + 100;
		return number;
	}
}
