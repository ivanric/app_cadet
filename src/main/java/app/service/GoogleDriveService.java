package app.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import app.config.GoogleDriveConfig;

import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveService {
	String folderId = "172Z1cDf3Wa4W9ByDxcF8oG9kx5Kn_XAC";
	
	private final Drive driveService;
	
    @Autowired
    public GoogleDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    public String createFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setParents(Collections.singletonList(folderId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return file.getId();
    }
    /*
    public String getOrCreateRootFolder() throws IOException {
        String folderName = "root"; // Nombre de la carpeta raíz

        // Buscar carpeta con el nombre especificado en la raíz
        FileList result = driveService.files().list()
                .setQ("name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder' and 'root' in parents")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> folders = result.getFiles();

        if (folders.isEmpty()) {
            // Si no existe, crea la carpeta
        	System.out.println("CREANDO CARPETA ROOT");
            return createFolder(folderName);
        } else {
        	System.out.println("EL ID DE LA CARPETA ROOT:"+folders.get(0).getId());
            // Si existe, retorna el ID de la carpeta
            return folders.get(0).getId();
        }
    }
*/
    
	/*
	
	
    private final Drive driveService;

    
    
    
    
    @Autowired
    public GoogleDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    // Método para crear una carpeta en Google Drive
    public String createFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return file.getId();
    }

    // Método para hacer un archivo o carpeta pública
    public void makePublic(String fileId) throws IOException {
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader"); // Permiso de solo lectura

        driveService.permissions().create(fileId, permission).execute();
    }

    // Método para compartir un archivo o carpeta con una cuenta específica (como la cuenta de servicio)
    public void shareWithServiceAccount(String fileId, String serviceAccountEmail) throws IOException {
        Permission permission = new Permission();
        permission.setType("user");
        permission.setRole("writer"); // Permiso de editor o puedes cambiarlo a "reader" si solo es lectura
        permission.setEmailAddress(serviceAccountEmail);

        driveService.permissions().create(fileId, permission).execute();
    }

    // Método para obtener o crear la carpeta raíz
    public String getOrCreateRootFolder() throws IOException {
        String folderName = "bbcadetsistema"; // Nombre de la carpeta raíz

        // Buscar carpeta con el nombre especificado en la raíz
        FileList result = driveService.files().list()
                .setQ("name = '" + folderName + "' and mimeType = 'application/vnd.google-apps.folder' and 'root' in parents")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> folders = result.getFiles();

        if (folders.isEmpty()) {
            // Si no existe, crea la carpeta
            return createFolder(folderName);
        } else {
            // Si existe, retorna el ID de la carpeta
            return folders.get(0).getId();
        }
    }

    // Método para listar archivos en la raíz del Drive
    public void listFiles() throws IOException {
        FileList result = driveService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }

    // Método para listar permisos de un archivo
    public void listPermissions(String fileId) throws IOException {
        File file = driveService.files().get(fileId).setFields("permissions").execute();
        List<Permission> permissions = file.getPermissions();
        for (Permission permission : permissions) {
            System.out.println("Permission ID: " + permission.getId() + ", Type: " + permission.getType() + ", Role: " + permission.getRole());
        }
    }*/
}
