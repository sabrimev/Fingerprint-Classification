/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintrecognition;



import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;



public class FingerprintRecognition {

    public static int width,width2;
    public static int height,height2;
    public static void main(String[] args) throws IOException, Exception {
        




        //copFile();
        FingerPrintRecognitionForm form = new FingerPrintRecognitionForm();
        form.setTitle("Fingerprint Recognition ~ by Sabri Meviş");
        form.setVisible(true);
        form.setResizable(false);
        form.setLocation(280, 140);//Set where screen should appear
        
        //Set icon
        String imagePath = "./src/icons/appicon.png";
        File file = new File(imagePath);
        BufferedImage myImg = ImageIO.read(file);
        form.setIconImage(myImg);
    }
    
    private static void copFile() throws FileNotFoundException, IOException{
        for (int i = 1; i <= 500; i++) {
            
            String textPath = "./src/NIST/sd04/png_txt/figs_1/f1111 ("+i+").txt";
            String imagePath = "./src/NIST/sd04/png_txt/figs_1/f1111 ("+i+").png";
            String arch = "./src/fingerprints/A/f1111 ("+i+").png";
            String tarch = "./src/fingerprints/T/f1111 ("+i+").png";
            String whorl = "./src/fingerprints/W/f1111 ("+i+").png";
            String lloop = "./src/fingerprints/L/f1111 ("+i+").png";
            String rloop = "./src/fingerprints/R/f1111 ("+i+").png";
            FileReader filetextPath = new FileReader(textPath);
            File fileimagePath = new File(imagePath);
            File filearch = new File(arch);
            File filetarch = new File(tarch);
            File filewhorl = new File(whorl);
            File filelloop = new File(lloop);
            File filerloop = new File(rloop);
            BufferedReader br = new BufferedReader(filetextPath);
            br.readLine();
            String type = br.readLine();
            type = type.substring(7);
            System.out.println("res:"+type);
            
            if(type.equals("W"))
                Files.copy(fileimagePath.toPath(), filewhorl.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            if(type.equals("A"))
                Files.copy(fileimagePath.toPath(), filearch.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            if(type.equals("T"))
                Files.copy(fileimagePath.toPath(), filetarch.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            if(type.equals("L"))
                Files.copy(fileimagePath.toPath(), filelloop.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            if(type.equals("R"))
                Files.copy(fileimagePath.toPath(), filerloop.toPath(), StandardCopyOption.REPLACE_EXISTING);
    
        }
    }

    private static void displayImage(BufferedImage img, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }
}