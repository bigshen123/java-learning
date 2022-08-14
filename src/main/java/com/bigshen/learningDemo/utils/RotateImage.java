
package com.bigshen.learningDemo.utils;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 图片旋转工具类
 */
public class RotateImage {
	private static final Log logger = LogFactory.getLog(RotateImage.class);

  

	
	
	/**
	 * 
	 * make by 
	 * @param src
	 * @param angel
	 * @return
	 */
	public static BufferedImage rotate(Image src, int angel) {  
        int src_width = src.getWidth(null);  
        int src_height = src.getHeight(null);  
        // calculate the new image size  
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(  
                src_width, src_height)), angel);  
  
        BufferedImage res = null;  
        res = new BufferedImage(rect_des.width, rect_des.height,  
                BufferedImage.TYPE_INT_RGB);  
        Graphics2D g2 = res.createGraphics();  
        // transform  
        g2.translate((rect_des.width - src_width) / 2,  
                (rect_des.height - src_height) / 2);  
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);  
  
        g2.drawImage(src, null, null);  
        return res;  
    }
  
	/**
	 * 
	 * make by dongxh 
	 * @param src
	 * @param angel
	 * @return
	 */
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {  
        // if angel is greater than 90 degree, we need to do some conversion  
        if (angel >= 90) {  
            if(angel / 90 % 2 == 1){  
                int temp = src.height;  
                src.height = src.width;  
                src.width = temp;  
            }  
            angel = angel % 90;  
        }  
  
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;  
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;  
        double angel_dalta_width = Math.atan((double) src.height / src.width);  
        double angel_dalta_height = Math.atan((double) src.width / src.height);  
  
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_width));  
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_height));  
        int des_width = src.width + len_dalta_width * 2;  
        int des_height = src.height + len_dalta_height * 2;  
        return new Rectangle(new Dimension(des_width, des_height));  
    }  
	
	/**
	 * 
	 * make by dongxh
	 * @param imgFile
	 * @return
	 */
	public static Integer getImgRotateAngle(File _img_file_){
		Integer angel = 0;
		Metadata metadata = null;
		try{
			if(!_img_file_.exists())return angel;
			metadata = JpegMetadataReader.readMetadata(_img_file_);
			
			
	        for (Directory directory : metadata.getDirectories()) {
	            for (Tag tag : directory.getTags()) {
	            	
	                //格式化输出[directory.getName()] - tag.getTagName() = tag.getDescription()
	                System.out.format("[%s] - %s = %s\n",
	                        directory.getName(), tag.getTagName(), tag.getDescription());
	                if(tag.getTagName().contains("Orientation")){
                	int orientation = directory.getInt(tag.getTagType());
	    				//  
	    	            if(6 == orientation ){  
	    	                //
	    	                angel = 90;  
	    	            }else if( 3 == orientation){  
	    	               //  
	    	                angel = 180;  
	    	            }else if( 8 == orientation){  
	    	               // 
	    	                angel = 270;  
	    	            }  
	                }
	                }
	            }
	         

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return angel;
	}
	
	/**
	 * µ÷ÕûÍ¼Æ¬½Ç¶È
	 * make by dongxh 2017Äê11ÔÂ1ÈÕÏÂÎç4:31:20
	 * @param imgFile
	 */
	public static BufferedImage rotateImage(String fileName,byte[] fileContent){
		BufferedImage des=null;
		File _img_file_ = new File(fileName);
		FileOutputStream fos = null;

		try {
			
			fos = new FileOutputStream(_img_file_);
			fos.write(fileContent);

				if(_img_file_.exists()){
					Integer angel = getImgRotateAngle(_img_file_);
					if(angel==0)
						return ImageIO.read(_img_file_);;
					 BufferedImage src = ImageIO.read(_img_file_);  
					 des = rotate(src, angel); 
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				_img_file_.delete();
				fos.close();
			}catch(Exception e){
				logger.info("get rotate fail:"+e.getMessage());
			}
		}
		return des;
	}
 

 

 
    
}
