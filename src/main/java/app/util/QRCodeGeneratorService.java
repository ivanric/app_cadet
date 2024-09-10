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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


@Service
public class QRCodeGeneratorService {


	 private String ruta_logos = "";

	    private static final String CHARSET = "UTF-8";
	    private static final String FILE_EXTENSION = "png";
	    private static final int QR_WIDTH = 600;
	    private static final int QR_HEIGHT = 600;

	    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	    public String obtenerRutaArchivos(String carpeta) {
	        URIS uris = new URIS();
	        String sistemaOperativo = uris.checkOS();
	        System.out.println("INICIANDO APP");
	        System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);
	        String rutaCarpeta = "";

	        try {
	            if (sistemaOperativo.contains("Linux")) {
	                System.out.println("DANDO PERMISOS A LA CARPETA DE ARCHIVOS");
	                darPermisosCarpeta("/home");
	                rutaCarpeta = Paths.get("/home", carpeta).toString();
	            } else if (sistemaOperativo.contains("Windows")) {
	                rutaCarpeta = Paths.get("C:\\", carpeta).toString();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error al obtener la ruta de archivos: " + e.getMessage());
	        }

	        return rutaCarpeta;
	    }

	    private void darPermisosCarpeta(String rutaBase) throws IOException {
	        Process p = Runtime.getRuntime().exec("chmod -R 777 " + rutaBase);
	        try {
	            p.waitFor(); // Esperar a que el comando termine
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

	    public void generateQRCode(String message, String nombre) {
	        System.out.println("### Generating QRCode ###");

	        try {
	            String finalMessage = message;
	            System.out.println("Final Input Message: " + finalMessage);
	            processQRCode(finalMessage, prepareOutputFileName(nombre), CHARSET, QR_WIDTH, QR_HEIGHT);

	        } catch (WriterException | IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private String prepareOutputFileName(String nombre) throws IOException {
	        String ruta = obtenerRutaArchivos(Constantes.nameFolderQrSocio);

	        if (ruta == null || ruta.isEmpty()) {
	            throw new IOException("No se pudo determinar la ruta de almacenamiento.");
	        }

	        Path rutaDirectorio = Paths.get(ruta).toAbsolutePath();

	        if (!Files.exists(rutaDirectorio)) {
	            System.out.println("*********CREANDO CARPETA DE QRS");
	            Files.createDirectories(rutaDirectorio); // Crea el directorio si no existe
	        }

	        ruta_logos = Paths.get(ruta).toAbsolutePath().resolve(nombre).toString();
	        System.out.println("RUTA DE QR FOLDER: " + ruta_logos);
	        return ruta_logos + "." + FILE_EXTENSION;
	    }

	    private void processQRCode(String data, String path, String charset, int height, int width) throws WriterException, IOException {
	        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
	        MatrixToImageWriter.writeToFile(matrix, FILE_EXTENSION, new File(path));
	    }

	    public void updateQRCodeContent(String newContent, String nombre) {
	        try {
	            String rutaCatalogos = obtenerRutaArchivos(Constantes.nameFolderQrSocio);

	            if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
	                throw new IOException("No se pudo determinar la ruta de almacenamiento.");
	            }

	            Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();
 
	            if (!Files.exists(rutaDirectorio)) {
	                Files.createDirectories(rutaDirectorio); // Crea el directorio si no existe
	            }

	            ruta_logos = Paths.get(rutaCatalogos).toAbsolutePath().resolve(nombre + "." + FILE_EXTENSION).toString();

	            if (!ruta_logos.isEmpty() && ruta_logos != null) {
	                File qrCodeFile = new File(ruta_logos);
	                System.out.println("******************FILE: " + qrCodeFile.toString());

	                if (qrCodeFile.exists()) {
	                    BufferedImage qrCodeImage = ImageIO.read(qrCodeFile);
	                    BitMatrix bitMatrix = new MultiFormatWriter().encode(newContent, BarcodeFormat.QR_CODE, qrCodeImage.getWidth(), qrCodeImage.getHeight());
	                    try (FileOutputStream fos = new FileOutputStream(qrCodeFile)) {
	                        MatrixToImageWriter.writeToStream(bitMatrix, FILE_EXTENSION, fos);
	                    }
	                    System.out.println("QR code updated successfully.");
	                } else {
	                    System.out.println("El archivo QR no existe en la ruta especificada: " + ruta_logos);
	                    generateQRCode(newContent, nombre);
	                }
	            } else {
	                System.out.println("NO SE ENCONTRO EL QR: " + ruta_logos);
	                generateQRCode(newContent, nombre);
	            }

	        } catch (IOException | WriterException e) {
	            e.printStackTrace();
	        }
	    }
	
//	 public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
//        try { 
//            Resource resource = resourceLoader.getResource("classpath:static/" + nombreCarpeta);
//            return resource.getFile().getAbsolutePath();
//        } catch (Exception e) {
//            System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
//            return null;
//        }
//    }
	 

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
