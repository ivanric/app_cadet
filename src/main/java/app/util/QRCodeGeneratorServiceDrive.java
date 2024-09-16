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
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class QRCodeGeneratorServiceDrive {

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
            // Prepara la ruta de archivo local
            String outputPath = prepareOutputFileName(nombre);

            // Genera y guarda el QR localmente en la ruta temporal
            File localQRCodeFile = generateQRCodeFile(message, outputPath);

            // Guarda el QR en Google Drive
            archivoService.guargarArchivoDriveFile(Constantes.nameFolderQrSocio, localQRCodeFile, nombre + "." + FILE_EXTENSION);

            // Opcional: Elimina el archivo local después de subirlo a Google Drive, si ya no es necesario
//            Files.deleteIfExists(localQRCodeFile.toPath());

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
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
    private File generateQRCodeFile(String data, String path) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(CHARSET), CHARSET), BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        File qrFile = new File(path); // Ruta del archivo
        MatrixToImageWriter.writeToFile(matrix, FILE_EXTENSION, qrFile);
        return qrFile;
    }

    /**
     * Obtiene la ruta de almacenamiento según el sistema operativo.
     *
     * @param carpeta Nombre de la carpeta de almacenamiento.
     * @return Ruta de almacenamiento.
     */
    public String obtenerRutaArchivos(String carpeta) {
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);
        String rutaCarpeta = "";

        try {
            if (sistemaOperativo.contains("linux")) {
                rutaCarpeta = Paths.get("/home", carpeta).toString();
            } else if (sistemaOperativo.contains("windows")) {
                rutaCarpeta = Paths.get("C:\\", carpeta).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener la ruta de archivos: " + e.getMessage());
        }

        return rutaCarpeta;
    }
}
