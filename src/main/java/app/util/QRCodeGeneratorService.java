package app.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import app.service.ArchivoService;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.GeneralSecurityException;


import javax.imageio.ImageIO;


@Service
public class QRCodeGeneratorService {

	

    private static final String CHARSET = "UTF-8";
    private static final String FILE_EXTENSION = "png";
    private static final int QR_WIDTH = 600;
    private static final int QR_HEIGHT = 600;

    @Autowired
    private ArchivoService archivoService; // Servicio para manejar archivos en Google Drive

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String ruta_logos = "";

    /**
     * Método para generar un código QR y guardarlo localmente y en Google Drive.
     *
     * @param message Mensaje que se codificará en el QR.
     * @param nombre Nombre del archivo del QR.
     */
    
    
    public void generateQRCode(String message, String nombre) {
        System.out.println("### Generating QRCode ###");
        try {
            String finalMessage = message;
            System.out.println("Final Input Message: " + finalMessage);
            String outputPath = prepareOutputFileName(nombre); // Ruta local

            // Procesa y guarda el QR localmente
            processQRCode(finalMessage, outputPath, CHARSET, QR_WIDTH, QR_HEIGHT);

            // Guarda el QR en Google Drive
            File qrFile = new File(outputPath);
            archivoService.guargarArchivoDriveFile(Constantes.nameFolderQrSocio, qrFile, nombre + "." + FILE_EXTENSION);

        } catch (WriterException | IOException e) {
            e.printStackTrace(); 
        } catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    

    /**
     * Prepara el nombre del archivo de salida y la ruta de almacenamiento.
     *
     * @param nombre Nombre del archivo del QR.
     * @return Ruta completa donde se guardará el archivo QR.
     * @throws IOException Si hay un error al crear la carpeta o la ruta.
     */
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

    /**
     * Genera la imagen del QR y la guarda en un archivo.
     *
     * @param data Datos a codificar en el QR.
     * @param path Ruta donde se guardará el QR.
     * @param charset Conjunto de caracteres.
     * @param height Altura del QR.
     * @param width Anchura del QR.
     * @throws WriterException Si hay un error en la escritura del QR.
     * @throws IOException Si hay un error en el manejo del archivo.
     */
    private void processQRCode(String data, String path, String charset, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToFile(matrix, FILE_EXTENSION, new File(path));
    }

    /**
     * Obtiene la ruta de almacenamiento según el sistema operativo.
     *
     * @param carpeta Nombre de la carpeta de almacenamiento.
     * @return Ruta de almacenamiento.
     */
    public String obtenerRutaArchivos(String carpeta) {
        URIS uris = new URIS();
        String sistemaOperativo = uris.checkOS();
        System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);
        String rutaCarpeta = "";

        try {
            if (sistemaOperativo.contains("Linux")) {
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

    /**
     * Asigna permisos a la carpeta en Linux.
     *
     * @param rutaBase Ruta base de la carpeta.
     * @throws IOException Si hay un error al asignar permisos.
     */
    private void darPermisosCarpeta(String rutaBase) throws IOException {
        Process p = Runtime.getRuntime().exec("chmod -R 755 " + rutaBase); // Permisos más seguros
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el contenido de un QR existente o lo crea si no existe.
     *
     * @param newContent Nuevo contenido del QR.
     * @param nombre Nombre del archivo del QR.
     */
    public void updateQRCodeContent(String newContent, String nombre) {
        try {
            String rutaCatalogos = obtenerRutaArchivos(Constantes.nameFolderQrSocio);

            if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
                throw new IOException("No se pudo determinar la ruta de almacenamiento.");
            }

            Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();
            if (!Files.exists(rutaDirectorio)) {
                Files.createDirectories(rutaDirectorio);
            }

            ruta_logos = Paths.get(rutaCatalogos).toAbsolutePath().resolve(nombre + "." + FILE_EXTENSION).toString();

            if (!ruta_logos.isEmpty()) {
                File qrCodeFile = new File(ruta_logos);
                if (qrCodeFile.exists()) {
                    BufferedImage qrCodeImage = ImageIO.read(qrCodeFile);
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(newContent, BarcodeFormat.QR_CODE, qrCodeImage.getWidth(), qrCodeImage.getHeight());
                    try (FileOutputStream fos = new FileOutputStream(qrCodeFile)) {
                        MatrixToImageWriter.writeToStream(bitMatrix, FILE_EXTENSION, fos);
                    }
                    System.out.println("QR code updated successfully.");
                } else {
                    System.out.println("El archivo QR no existe, generando uno nuevo.");
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
