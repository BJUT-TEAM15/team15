package net.sfte.htlibrary.util;

import java.io.*;

public class StringN2A {
	public String toAscii(String s) {
		StringWriter out = new StringWriter();
		N2AFilter filter = new N2AFilter(out);
		
		String result = null;
		char[] buf = s.toCharArray();
		try {
			filter.write(buf, 0, buf.length);
			result = out.toString();
		} catch(IOException ex) {
			result = s;
		}
		return result;
	}
	
	public static void main(String[] args) {
		StringN2A n2a = new StringN2A();
		System.out.println(n2a.toAscii("\\abc"));
		System.out.println(n2a.toAscii("I am . /图书馆管理系统"));
		System.out.println(Integer.toHexString('\\'));
	}
}
