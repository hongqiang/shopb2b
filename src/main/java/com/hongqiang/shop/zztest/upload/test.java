package com.hongqiang.shop.zztest.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class test {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void testw() {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		List<String> strings = new ArrayList<String>();
		strings.add("1");
		strings.add("2");
		String aString = "Integer";
		localHashMap.put(aString, strings);
		Set<String> key = localHashMap.keySet();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println("key="+string);
			List<String> localstrings = (List<String>)localHashMap.get(string);
			System.out.println(localstrings);
			for (String s : localstrings) {
				System.out.println("value="+s);
			}
		}
		List<String> sss2 = new ArrayList<String>();
		sss2.add("3");
		sss2.add("4");
		localHashMap.put(aString, sss2);
	    key = localHashMap.keySet();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println("key="+string);
			List<String> localstrings = (List<String>)localHashMap.get(string);
			System.out.println(localstrings);
			for (String s : localstrings) {
				System.out.println("value="+s);
			}
		}
	}
	
	public static void main(String[] args){
//		for(int i=1;i<301;i++){
//			String aString="UPDATE hq_product set image = "+
//					"(select image from xx_product where id = "+i+"), introduction ="+
//					"(select introduction from xx_product where id = "+i+") WHERE id = "+i+";";
//			System.out.println(aString);
//		}
		testw();
		
	}
}
