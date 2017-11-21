package com.sjsu.rollbits.yml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
//import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

public class Loadyaml {
	protected static Logger logger = Logger.getLogger("Loadyaml");

	static Map<String, String> result = null;

	static {
		loadYaml();
	}

	public static void loadYaml() {

		logger.info("Loading Yaml file on startup");
		final String fileName = "./src/main/resources/config.yml";

		Yaml yaml = new Yaml();

		try {
			InputStream ios = new FileInputStream(new File(fileName));
			result = (Map<String, String>) yaml.load(ios);

		} catch (Exception e) {
			logger.error("Error while loading yaml file" + ExceptionUtils.getMessage(e));
		}

	}

	public static String getProperty(String property) {
		return result.get(property);

	}

}
