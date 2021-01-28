package com.baili.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class Md5Util {
	
	public static String  encode(String msg){
		 try {
			MessageDigest messageDigest=MessageDigest.getInstance("md5");
			return Base64.encodeBase64String(messageDigest.digest(msg.getBytes())) ;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 return null;
	}

	public static void main(String[] args) {
		System.out.println(encode("12345678"));
	}

}
