package com.bigshen.learningDemo.utils.file;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author byj
 * @date 2024/7/29
 * @Description
 */
public class PngToIco {

    public static void main(String[] args) {
        String pngName = "C:\\Users\\Lenovo\\Pictures\\Camera Roll\\PQC.png";
        String icoName = "C:\\Users\\Lenovo\\Pictures\\Camera Roll\\PQC.ico";

        try {
            // 读取PNG图片
            BufferedImage initImage = ImageIO.read(new File(pngName));
            if (initImage == null) {
                System.err.println("fail to read logo.png");
                return;
            }

            // 获取图片长宽
            int originalWidth = initImage.getWidth();
            int originalHeight = initImage.getHeight();
            int multiple = originalHeight / 32;
            int width = originalWidth / multiple;

            // 调整图片的长宽
            Image tmpImage = initImage.getScaledInstance(width, 32, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(width, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(tmpImage, 0, 0, null);
            g2d.dispose();

            // 将调整后的图像转换为ICO格式
            File icoFile = new File(icoName);
            Imaging.writeImage(resizedImage, icoFile, ImageFormats.ICO, null);
        } catch (IOException | ImagingException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
