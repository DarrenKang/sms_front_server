// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package ph.sinonet.vg.live.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {
	public static final Pattern P_URL = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");

	public static String formatNumberToDigits(Integer integer, Integer digits) {
		Integer length = Integer.valueOf(integer.toString().length());
		if (length.intValue() > digits.intValue())
			return integer.toString().substring(length.intValue() - digits.intValue());
		StringBuffer buffer = new StringBuffer(integer.toString());
		buffer.reverse();
		for (int i = 0; i < digits.intValue() - length.intValue(); i++)
			buffer.append("0");

		return buffer.reverse().toString();
	}

	public static String getRandomCharacter(int length) {
		StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
	}

	public static void main(String args[]) {
		System.out.println(StringUtil.makeRandom(100) + "");

	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));
		return sb.toString();
	}

	public static String getRandomNumber(int length) {
		StringBuffer buffer = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));
		return sb.toString();
	}

	public static Integer makeRandom(Integer maxValue) {
		Random r = new Random();
		return r.nextInt(maxValue);
	}

	public static String transform(String content) {
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("\n", "<br/>");
		return content;
	}

	public static String replaceAll(String str) {
		return StringUtils.trim(str).replaceAll("[\u0000-\u0010\u000B\u000E-\u001F\uD800-\uDFFF\uFFFE\uFFFF]", "");
	}

	public StringUtil() {
	}

	public static String arrayToString(String[] arr){
		String result = "";
		for (String str : arr) {
			result += str+",";
		}
		result = result.substring(0, result.length()-1);
		return result;
	}

	public static List<String> convertSmsBelongProjectToList(String str) {
		str = str.replace("channel2-", "");
		str = str.replace("both-", "");
		return convertStringToList(str);
	}

	public static List<String> convertStringToList(String str){
		String[] strArr = str.split(",");
		List<String> listStr = Arrays.asList(strArr);
		return listStr;
	}
}
