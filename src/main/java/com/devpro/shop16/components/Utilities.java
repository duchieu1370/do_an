package com.devpro.shop16.components;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utilities {
	
	public static long getKilobytes(long bytes) {
		long kilobytes = (bytes / 1024);
		return kilobytes;
	}
	
	public static long getMegabytes(long bytes) {
		long megabytes = (getKilobytes(bytes) / 1024);
		return megabytes;
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder(4).encode("hieu123"));
	}
	
}
