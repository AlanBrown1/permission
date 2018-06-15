package com.mmall.util;

import java.util.Date;
import java.util.Random;

/**
 * @author Alan Brown
 * @date 2018/5/25 18:07
 */

public class PasswordUtil {

	private static final String[] word = {
			"a", "b", "c", "d", "e", "f", "g",
			"h", "j", "k", "m", "n",
			"p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z",
			"A", "B", "C", "D", "E", "F", "G",
			"H", "J", "K", "M", "N",
			"P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z"
	};

	private final static String[] num = {
			"2", "3", "4", "5", "6", "7", "8", "9"
	};

	// 产生一个随机的密码
	public static String randomPassword(){
		StringBuffer sb = new StringBuffer();
		// 把当前时间作为种子，这样每次的密码都肯定不同
		Random random = new Random(new Date().getTime());
		boolean flag = false;
		int length = random.nextInt(3) + 8; // 长度为8, 9, 10
		for(int i = 0; i < length; i++){
			if(flag){
				sb.append(num[random.nextInt(num.length)]);
			}else{
				sb.append(word[random.nextInt(word.length)]);
			}
			flag = !flag;
		}
		return sb.toString();
	}

}
