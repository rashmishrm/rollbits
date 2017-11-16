package com.sjsu.rollbits.yml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
//import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Loadyaml {

	public static void main(String[] args)
	{
		
			 
	}
	
	public static String getProperty(String property)
	{
		  final String fileName = "./src/main/resources/config.yml";
		  
		    Yaml yaml = new Yaml();

		    try {
		    	
		        InputStream ios = new FileInputStream(new File(fileName));
		        //System.out.println(new File(fileName).getAbsolutePath());
		        // Parse the YAML file and return the output as a series of Maps and Lists
		        Map< String, String> result = (Map< String, String>) yaml.load(ios);
		        	
		        String values=result.get(property);
		        return values;
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			return null; 

	}
	
	
}
