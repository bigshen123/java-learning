package com.bigshen.learningDemo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FFmpegUtils {

	private static final Log logger = LogFactory.getLog(FFmpegUtils.class);


	public static byte[] scallImage(File scallImage) throws Exception {
		 byte[] scallContent = null;
		 try{
			String os = System.getProperty("os.name").toLowerCase();
	           logger.info("操作系統："+os);
			if (os.indexOf("windows") >= 0) {			
			
			} else if (os.indexOf("linux") >= 0) {
				BufferedImage bufferImage=ImageIO.read(scallImage);
		        int iconWidth = bufferImage.getWidth();
		        int iconHeight = bufferImage.getHeight();
		        if(iconWidth<300){
		        	iconWidth=300;
		        }
		        iconHeight=iconHeight/(iconWidth/300);
		        if(iconHeight<50){
		        	iconHeight=200;
		        }
				String cmd="/usr/local/ffmpeg/bin/ffmpeg -i \""+scallImage.getPath()+"\" -y -s 300x"+iconHeight+" \"" +scallImage.getPath()+"\"";
				logger.info("截屏指令：" + cmd);
				CmdUtil.executeCmd(cmd);
			}
			scallContent = input2byte(new FileInputStream(scallImage));
		 }catch(Exception e){
			 logger.info(e.getMessage());
		 }finally{
			 try{
			 scallImage.delete();
			 }catch(Exception e){
				 logger.info(e.getMessage());
			 }
		 }
		 return scallContent;
	}
	
	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}
}
