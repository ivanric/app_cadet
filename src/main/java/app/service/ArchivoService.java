package app.service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ArchivoService {
    public String guargarArchivo(String nameFolder, MultipartFile archivo, String nombre) throws IOException;
    public String guargarMultipleArchivos(List<MultipartFile> archivos) throws IOException;
    public String getOrCreateFolder(String folderName) throws IOException, GeneralSecurityException;
    public String obtenerIdArchivoDrivePorNombre(String nombreArchivo, String folderId) throws IOException, GeneralSecurityException;
    public Path linkArchivo(String folder, String nombreArchivo) throws IOException;
    public void eliminarArchivo(String folder, String nombreArchivo) throws IOException;
    public String guargarArchivoDrive(String nameFolder, MultipartFile archivo, String nombre) throws IOException, GeneralSecurityException;
    public String guargarArchivoDriveFile(String nameFolder, java.io.File archivo, String nombreArchivo) throws IOException, GeneralSecurityException;
    public void eliminarArchivoDrive(String nameFolder, String nombreArchivo) throws IOException, GeneralSecurityException;
    
    // Agregar este método a la interfaz si deseas que esté disponible en la implementación
    public String obtenerRutaCarpetaRecursos(String nombreCarpeta);

    String obtenerRutaArchivos(String carpeta);
	void eliminarCarpetaDrive(String folderId) throws IOException, GeneralSecurityException;
 

}
