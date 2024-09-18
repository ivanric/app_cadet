package app.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import app.config.GoogleDriveConfig;
import app.util.URIS;

@Service
public class ArchivoServiceImpl implements ArchivoService {
	
    @Autowired
    private GoogleDriveConfig googleDriveConfig;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    
    
    String folderIdroot = "172Z1cDf3Wa4W9ByDxcF8oG9kx5Kn_XAC";
    private Drive getDriveService() throws IOException, GeneralSecurityException {
        return googleDriveConfig.getDriveService();
    }

    private void makePublic(String fileId) throws IOException, GeneralSecurityException {
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");
        getDriveService().permissions().create(fileId, permission).execute();
    }

    @Override
    public String guargarArchivo(String nameFolder, MultipartFile archivo, String nombre) throws IOException {
        if (!archivo.isEmpty()) {
            String rutaCatalogos = obtenerRutaArchivos(nameFolder);

            if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
                throw new IOException("No se pudo determinar la ruta de almacenamiento.");
            }

            Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();

            if (!Files.exists(rutaDirectorio)) {
                Files.createDirectories(rutaDirectorio);
            }

            Path rutaArchivo = rutaDirectorio.resolve(nombre);
            System.out.println("Guardando archivo en la ruta: " + rutaArchivo);

            Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            return nombre;
        } else {
            System.out.println("El archivo está vacío.");
            return null;
        }
    }

    @Override
    public String guargarMultipleArchivos(List<MultipartFile> archivos) throws IOException {
        for (MultipartFile archivo : archivos) {
            guargarArchivo("multiple_files_folder", archivo, archivo.getOriginalFilename());
        }
        return "Archivos guardados exitosamente";
    }

    @Override
    public String getOrCreateFolder(String folderName) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

//        FileList result = driveService.files().list()
//                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and trashed=false")
//                .setSpaces("drive")
//                .setFields("files(id, name)")
//                .execute();
        
//         Buscar la carpeta por nombre dentro del folderIdroot
        FileList result = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and trashed=false and '" + folderIdroot + "' in parents")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (!files.isEmpty()) {
            String folderId = files.get(0).getId();
            System.out.println("***********LA CARPETA YA EXISTE");
//            eliminarCarpetaDrive(folderId);
            return folderId;
        } else {
            System.out.println("****************CREANDO LA CARPETA EN DRIVE");
            File fileMetadata = new File();
            fileMetadata.setName(folderName);
            fileMetadata.setParents(Collections.singletonList(folderIdroot));
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            File folder = driveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();

            String folderId = folder.getId();
            makePublic(folderId);

            return folderId;
        }
    }
    @Override
    public void eliminarCarpetaDrive(String folderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        // Eliminar todos los archivos y carpetas dentro de la carpeta especificada
        eliminarArchivosYCarpetasEnCarpeta(driveService, folderId);

        // Eliminar la carpeta después de vaciarla
        try {
            driveService.files().delete(folderId).execute();
            System.out.println("Carpeta eliminada: " + folderId);
        } catch (IOException e) {
            System.err.println("Error al eliminar la carpeta: " + folderId);
            e.printStackTrace();
        }
    }

    private void eliminarArchivosYCarpetasEnCarpeta(Drive driveService, String folderId) throws IOException, GeneralSecurityException {
        // Buscar todos los archivos y carpetas dentro de la carpeta especificada
        FileList result = driveService.files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setSpaces("drive")
                .setFields("files(id, name, mimeType)")
                .execute();

        List<File> files = result.getFiles();

        for (File file : files) {
            if ("application/vnd.google-apps.folder".equals(file.getMimeType())) {
                // Es una subcarpeta, así que llamamos recursivamente
                eliminarCarpetaDrive(file.getId());
            } else {
                // Es un archivo, lo eliminamos directamente
                try {
                    driveService.files().delete(file.getId()).execute();
                    System.out.println("Archivo eliminado: " + file.getName() + " (ID: " + file.getId() + ")");
                } catch (IOException e) {
                    System.err.println("Error al eliminar el archivo: " + file.getName() + " (ID: " + file.getId() + ")");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String obtenerIdArchivoDrivePorNombre(String nombreArchivo, String folderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        
        FileList result = driveService.files().list()
                .setQ("mimeType!='application/vnd.google-apps.folder' and name='" + nombreArchivo + "' and '" + folderId + "' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (!files.isEmpty()) {
            return files.get(0).getId();
        }
        
        return null;
    }

    @Override
    public String guargarArchivoDrive(String nameFolder, MultipartFile archivo, String nombre) throws IOException, GeneralSecurityException {
        if (!archivo.isEmpty()) {
            Drive driveService = getDriveService();

            String folderId = getOrCreateFolder(nameFolder);
            System.out.println("ID CARPETA: " + folderId);

            File fileMetadata = new File();
            fileMetadata.setName(nombre);
            fileMetadata.setParents(Collections.singletonList(folderId));

            InputStream inputStream = archivo.getInputStream();
            InputStreamContent mediaContent = new InputStreamContent(archivo.getContentType(), inputStream);

            File fileUploaded = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            inputStream.close();

            return fileUploaded.getId();
        } else {
            System.out.println("El archivo está vacío.");
            return null;
        }
    }

    @Override
    public String guargarArchivoDriveFile(String nameFolder, java.io.File archivo, String nombreArchivo) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String folderId = getOrCreateFolder(nameFolder);
        System.out.println("ID CARPETA: " + folderId);

        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(nombreArchivo);
        fileMetadata.setParents(Collections.singletonList(folderId));

        Path filePath = Paths.get(archivo.getAbsolutePath());

        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        FileContent mediaContent = new FileContent(mimeType, archivo);

        com.google.api.services.drive.model.File fileUploaded = driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return fileUploaded.getId();
    }

    @Override
    public Path linkArchivo(String folder, String nombreArchivo) throws IOException {
        System.out.println("Nombre de archivo a buscar link antes de eliminar: " + nombreArchivo);
        String rutaCatalogos = obtenerRutaArchivos(folder);

        if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
            throw new IOException("No se pudo determinar la ruta de almacenamiento.");
        }

        Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();

        if (!Files.exists(rutaDirectorio)) {
            Files.createDirectories(rutaDirectorio);
        }

        if (nombreArchivo != null) {
            Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
            System.out.println("Ruta de archivo encontrado: " + rutaArchivo);
            if (Files.exists(rutaArchivo)) {
                return rutaArchivo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void eliminarArchivo(String folder, String nombreArchivo) throws IOException {
        System.out.println("INTENTANDO ELIMINAR ARCHIVO: " + nombreArchivo);
        Path archivo = linkArchivo(folder, nombreArchivo);
        System.out.println("INTENTANDO ELIMINAR PATH: " + archivo);
        try {
            if (archivo != null) {
                System.out.println("**********ELIMINANDO ARCHIVO: " + archivo);
                Files.deleteIfExists(archivo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void eliminarArchivoDrive(String folderName, String nombreArchivo) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String folderId = getOrCreateFolder(folderName);
        System.out.println("ID de la carpeta: " + folderId);
        
        FileList result = driveService.files().list()
                .setQ("mimeType!='application/vnd.google-apps.folder' and name='" + nombreArchivo + "' and '" + folderId + "' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (!files.isEmpty()) {
            for (File file : files) {
                try {
                    driveService.files().delete(file.getId()).execute();
                    System.out.println("Archivo eliminado: " + nombreArchivo + " (ID: " + file.getId() + ")");
                } catch (IOException e) {
                    System.err.println("Error al eliminar el archivo: " + nombreArchivo + " (ID: " + file.getId() + ")");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Archivo no encontrado en la carpeta: " + nombreArchivo);
        }
    }

    @Override
    public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/" + nombreCarpeta);
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
            return null;
        }
    }

    @Override
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
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
