package app.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


@Service
public class QRCodeGeneratorService {



//    @Value("${qrcode.output.directory}")
//    private String outputLocation=".//src//main//resources//static//qrcodes//";
//	private String ruta_logos="/static/qrcodes/";
	private String ruta_logos="";

    private static final String charset = "UTF-8";

    private static final String strDateFormat = "yyyyMMddhhmmss";
	private ResourceLoader resourceLoader=new DefaultResourceLoader();

	 public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
        try { 
            Resource resource = resourceLoader.getResource("classpath:static/" + nombreCarpeta);
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
            return null;
        }
    }
    public void generateQRCode(String message,String nombre) {
//        log.info("### Generating QRCode ###");
        System.out.println("### Generating QRCode ###");

//        log.info("Output directory - {}", outputLocation);
//        System.out.println("Output directory - {}"+ outputLocation);
        try {
            String finalMessage =message;
//            log.info("Final Input Message - {}", finalMessage);
            System.out.println("Final Input Message - {}"+ finalMessage);
//            processQRcode(finalMessage, prepareOutputFileName(), charset, 400, 400);
            processQRcode(finalMessage, prepareOutputFileName(nombre), charset, 600, 600);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    private String prepareOutputFileName(String nombre) {

        StringBuilder sb = new StringBuilder();
//        try {
        	ruta_logos=Paths.get(obtenerRutaCarpetaRecursos("qrcodes")).toAbsolutePath().resolve(nombre).toString();
//		} catch (Exception e) {
			// TODO: handle exception
//			ruta_logos="";
//		}
        
        System.out.println("RUTA DE LOGOOOOOS:"+ruta_logos);
        sb.append(ruta_logos).append(".png");
//        log.info("Final output file - "+sb.toString());
//        System.out.println("Final output file - "+sb.toString());
        return sb.toString();
//        return ruta_logos;
    }

    private void processQRcode(String data, String path, String charset, int height, int width) throws WriterException, IOException {
        /*the BitMatrix class represents the 2D matrix of bits*/
       /* MultiFormatWriter is a factory class that finds the appropriate Writer subclass for
        the BarcodeFormat requested and encodes the barcode with the supplied contents.*/
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }
    
    public void updateQRCodeContent(String newContent, String nombre) {
        try {
//            String qrCodeFilePath = prepareOutputFileName(nombre); 
//            System.out.println("PATH QR IMAGEN PATH:"+qrCodeFilePath);
            try {
            	ruta_logos=Paths.get(obtenerRutaCarpetaRecursos("qrcodes")).toAbsolutePath().resolve(nombre+".png").toString();
    		} catch (Exception e) {
    			// TODO: handle exception
    			ruta_logos="";
    		}
            
            if (ruta_logos!="") {
                File qrCodeFile = new File(ruta_logos);
                System.out.println("******************FILE:"+qrCodeFile.toString());
                System.out.println("******************FILE:"+qrCodeFile.exists());
                if (qrCodeFile.exists()) {
                    BufferedImage qrCodeImage = ImageIO.read(qrCodeFile);
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(newContent, BarcodeFormat.QR_CODE, qrCodeImage.getWidth(), qrCodeImage.getHeight());
                    MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(qrCodeFile));
                    System.out.println("*********************QR ACTUALIZADO*******************.");
                } else {
                    System.out.println("El archivo QR no existe en la ruta especificada: " + ruta_logos);
                    generateQRCode(newContent, nombre);
                }
			}else {
				System.out.println("NO SE ENCONTRO EL QR: " + ruta_logos);
                generateQRCode(newContent, nombre);
			}
  
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
       /*
        try {
            String path = prepareOutputFileName(nombre);
            System.out.println("PATH MOFOOOOO:"+path); 
            if (Files.exists(Paths.get(path))) {
                // Actualizar el contenido del QR sin regenerarlo
                System.out.println("Actualizando contenido del código QR en: " + path);
                Files.writeString(Paths.get(path), newContent);
            } else {
                System.out.println("El código QR no existe en: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


}
