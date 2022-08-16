package com.kakao.bank.place.util;

public class RemoveTag {

	public static String remove(String str) {
		return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}
	
}
