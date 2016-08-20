/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintrecognition;

/* 
 Biometric SDK
 Version 1.3
 This file contains functions that manipulate , extract features and match
 fingerprint images.
 
 Copyright (C) 2005  Scott Johnston
 Email :  moleisking@googlemail.com
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 */

/*
 * A lot of work needs to be done here.
 * 
 * What needs to be done:
 * 
 * This class is ment for code that is going to do preprocesing of the image before we
 * pass it to the CFingerPrint Class.The Sample pictures need to be transformed from what they look
 * like in Sample1 to what they look like in ProcessedSample.
 * 
 * This will require the use of a edge detetection algorithim and some fillters, so if you can help 
 * and submit the code and the results of your processed samples to me it would be much apreciated.
 * 
 * */
import java.awt.image.BufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
//must remove latter
import javax.swing.JOptionPane;

public class CFingerPrintGraphics {

    public int FP_IMAGE_WIDTH = 323;
    public int FP_IMAGE_HEIGHT = 352;

    public CFingerPrintGraphics() {

    }

    public CFingerPrintGraphics(int width, int height) {
        FP_IMAGE_WIDTH = width;
        FP_IMAGE_HEIGHT = height;
    }

    public BufferedImage BinerizeImage(BufferedImage m_image, int max, int min) {
        BufferedImage m_ImageBuffer = new BufferedImage(FP_IMAGE_WIDTH, FP_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i <= FP_IMAGE_WIDTH - 1; i++) {
            for (int j = 0; j <= FP_IMAGE_HEIGHT - 1; j++) {
                Color c = new Color(m_image.getRGB(i, j));
                if ((c.getBlue() <= max) && (c.getBlue() >= min) && (c.getRed() <= max) && (c.getRed() >= min) && (c.getGreen() <= max) && (c.getGreen() >= min)) {
                    m_ImageBuffer.setRGB(i, j, 0);
                } else {
                    m_ImageBuffer.setRGB(i, j, Color.white.getRGB());
                }
            }
        }
        return m_ImageBuffer;
    }

    public BufferedImage makeGray(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                img.setRGB(x, y, gray);
            }
        }
        return img;
    }

    public BufferedImage getGreyFingerPrintImage(BufferedImage m_original_image) {
        BufferedImage m_result_image = new BufferedImage(FP_IMAGE_WIDTH, FP_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        float mask[] = {-1f / 5, 1f / 5, -1f / 5, 1f / 5, 9f / 5, 1f / 5, -1f / 5, 1f / 5, -1f / 5};

    //     float mask[] = { 
        //       5f/100,10f/100,5f/100
        //      ,10f/100,40f/100,10f/100
        //      ,5f/100,10f/100,5f/100
        //                 };    
      //float mask[] = {  -1f/4,0,0,0,0,0,0
        //                  ,0,-1f/2,0,0,0,0,0
        //                  ,0,0,-1f/4,0,0,0,0
        //                  ,0,0,0,1,0,0,0
        //                  ,0,0,0,0,0,0,0
        //                  ,0,0,0,0,0,0,0
        //                  ,0,0,0,0,0,0,0
     // };    
    //     float mask[] = {  1f/16,0f,-1f/16
        //                      ,2f/16,0f,-2f/16
        //                      ,1f/16,0f,-1f/16
        //                  
        //  };
        /*
         int d = 530;
         float mask[] = {  0,1f/d/d/d,1f/d,1f/d,1f/d,1f/d,1f/d,1f/d,0
         ,1f/d,5f/d,5f/d,5f/d,5f/d,5f/d,5f/d,5f/d,1f/d
         ,1f/d,5f/d,11f/d,11f/d,11f/d,11f/d,11f/d,5f/d,1f/d
         ,1f/d,5f/d,11f/d,22f/d,22f/d,22f/d,11f/d,5f/d,1f/d
         ,1f/d,5f/d,11f/d,22f/d,32f/d,22f/d,11f/d,5f/d,1f/d
         ,1f/d,5f/d,11f/d,22f/d,22f/d,22f/d,11f/d,5f/d,1f/d
         ,1f/d,5f/d,11f/d,11f/d,11f/d,11f/d,11f/d,5f/d,1f/d
         ,1f/d,5f/d,5f/d,5f/d,5f/d,5f/d,5f/d,5f/d,1f/d
         ,0,1f/d,1f/d,1f/d,1f/d,1f/d,1f/d,1f/d,0
                                      
         };    
         */
        Kernel k = new Kernel(3, 3, mask);
        BufferedImageOp con = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
        con.filter(m_original_image, m_result_image);

       //binerize image
        // m_result_image = this.BinerizeImage(m_result_image, 180);
        /*
         for (int i = 0; i<= FP_IMAGE_WIDTH-1;i++)
         {
         for (int j = 0;j<= FP_IMAGE_HEIGHT-1;j++)
         {
         Color c = new Color(m_result_image.getRGB(i,j));
         if ((c.getBlue()  == 0) && (c.getRed()  == 0) && (c.getGreen()  == 0))
         {
         m_result_image.setRGB(i,j,Color.blue.getRGB());
         }
         else
         {
         m_result_image.setRGB(i,j,Color.white.getRGB());
         }
         }//end for j
         }//end for i
         */
        return m_result_image;
    }//getGreyFingerPrintImage

}
