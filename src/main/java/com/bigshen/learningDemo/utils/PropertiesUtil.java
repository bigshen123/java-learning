package com.bigshen.learningDemo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	/** 静态变量：系统日志 */
	private static final Log logger = LogFactory.getLog(PropertiesUtil.class);
	
	/**
	 * 读取配置文件配置项
	 * @param key 配置项名
	 * @return 配置项值
	 */
	public static String getProperty(String filename, String key) {
		Properties p = null;
		
		//加载配置文件
		try {
			InputStream in = new PropertiesUtil().getClass().getClassLoader().getResourceAsStream(filename);			
			p = new Properties();			
			p.load(in);			
		} catch (IOException e) {	
			if (logger.isErrorEnabled()) {
				logger.error("Failed to load configuration file \"" + filename + "\": " + e.getMessage());
			}
			e.printStackTrace();
			
			p = null;
		}
		
		String value = null;
		if (p != null) {
			try {
				value = p.getProperty(key);
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("Failed to read configuration file \"" + filename + "\" property [SafetyRedLine]: " + e.getMessage());
				}
				e.printStackTrace();
			}
		}
		return value;
	}
	
}
