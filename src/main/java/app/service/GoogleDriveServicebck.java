package app.service;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import app.config.GoogleDriveConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
/*
@Service
public class GoogleDriveServicebck {

//    private final Drive driveService;
//
//    public GoogleDriveService() throws GeneralSecurityException, IOException {
//        this.driveService = GoogleDriveConfig.getDriveService();
//    }
//
//    public String createFolder(String folderName) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName(folderName);
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//
//        File file = driveService.files().create(fileMetadata)
//                .setFields("id")
//                .execute();
//        return file.getId();
//    }
//	
//	 private final Drive driveService;
//
//	    @Autowired
//	    public GoogleDriveService(Drive driveService) {
//	        this.driveService = driveService;
//	    }
//
//	    public String createFolder(String folderName) throws IOException {
//	        File fileMetadata = new File();
//	        fileMetadata.setName(folderName);
//	        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//
//	        File file = driveService.files().create(fileMetadata)
//	                .setFields("id")
//	                .execute();
//	        return file.getId();
//	    }
	
	
	 private final Drive driveService;

	    @Autowired
	    public GoogleDriveServicebck(Drive driveService) {
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

	    // Ejemplo de crear una carpeta y luego hacerla pública
	    public String createFolderAndMakePublic(String folderName) throws IOException {
	        String folderId = createFolder(folderName);
	        makePublic(folderId);
	        return folderId;
	    }
}*/