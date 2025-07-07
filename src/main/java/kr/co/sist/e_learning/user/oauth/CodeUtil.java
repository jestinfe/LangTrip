package kr.co.sist.e_learning.user.oauth;

import java.util.Random;

public class CodeUtil {
	public static String generateCode() {
		Random random = new Random();
		int code = 10000 + random.nextInt(90000);
		return String.valueOf(code);
	}
}
