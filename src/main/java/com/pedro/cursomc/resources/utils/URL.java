package com.pedro.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static List<Integer> decodeIntList( String c ){
		List<Integer> list = new ArrayList<Integer>();
		
		if( !c.isEmpty() ) {
			String[] vet = c.split( "," );
			
			for( int i = 0; i < vet.length; i++ ) {
				list.add( Integer.parseInt( vet[i] ) );
			}
		}
		
		return list;
		//return Arrays.asList( c.split( "," )).stream().map( x -> Integer.parseInt( x )).collect( Collectors.toList() );
	}
	
	public static String decodeParam( String s) {
		try {
			return URLDecoder.decode( s, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			return "";
		} 
	}

}
