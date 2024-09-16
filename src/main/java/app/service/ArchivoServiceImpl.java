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
import org.springframework.core.io.ClassPathResource;
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

import app.config.GoogleDriveConfig;
import app.repository.CatalogoRepository;
import app.util.URIS;



@Service
public class ArchivoServiceImpl implements ArchivoService {

   @Autowired
    private GoogleDriveConfig googleDriveConfig;
	
	
	private String ruta_logos="/static/logos/";
	private String ruta_socio_logos="/static/socioslogos/";
//	private String ruta_logos=".//src//main//resources//static//logos//";
//	private String ruta_catalogos=".//src//main//resources//static//catalogos//";
	private String ruta_catalogos="/static/catalogosimg/";
//	private ResourceLoader resourceLoader=new DefaultResourceLoader();
	private ResourceLoader resourceLoader=new DefaultResourceLoader();
	
	
	//drive
    private Drive getDriveService() throws IOException, GeneralSecurityException {
        return googleDriveConfig.getDriveService();
    }
    
    public String getOrCreateFolder(String folderName) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        
        // Search for the folder
        FileList result = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (files.isEmpty()) {
            // Folder does not exist, create it
            File fileMetadata = new File();
            fileMetadata.setName(folderName);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            File folder = driveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();

            return folder.getId();
        } else {
            // Folder exists
            return files.get(0).getId();
        }
    }
    
    public String obtenerIdArchivoDrivePorNombre(String nombreArchivo, String folderId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        
        // Buscar el archivo dentro de la carpeta usando folderId directamente
        FileList result = driveService.files().list()
                .setQ("mimeType!='application/vnd.google-apps.folder' and name='" + nombreArchivo + "' and '" + folderId + "' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (!files.isEmpty()) {
            return files.get(0).getId();  // Devolver el ID del archivo encontrado
        }
        
        return null;  // Retorna null si no se encuentra el archivo
    }


    public String guargarArchivoDriveFile(String nameFolder, java.io.File archivo, String nombreArchivo) throws IOException, GeneralSecurityException {
        // Obtén el servicio de Google Drive (asegúrate de que este método esté implementado y devuelva una instancia válida de Drive)
        Drive driveService = getDriveService();

        // Obtén o crea la carpeta en Google Drive
        String folderId = getOrCreateFolder(nameFolder);
        System.out.println("ID CARPETA: " + folderId);

        // Crea los metadatos para el archivo que se subirá a Google Drive
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(nombreArchivo);
        fileMetadata.setParents(Collections.singletonList(folderId)); // Asigna el archivo a la carpeta especificada

        // Convierte el java.io.File a un Path
        Path filePath = Paths.get(archivo.getAbsolutePath());

        // Determina el tipo de contenido del archivo (tipo MIME)
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Tipo MIME predeterminado si no se detecta ninguno
        }

        // Crea el FileContent con el tipo MIME y el archivo real
        FileContent mediaContent = new FileContent(mimeType, archivo);

        // Sube el archivo a Google Drive
        com.google.api.services.drive.model.File fileUploaded = driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id") // Devuelve solo el ID del archivo
                .execute();

        // Devuelve el ID del archivo subido
        return fileUploaded.getId();
    }

    
    public String guargarArchivoDrive(String nameFolder, MultipartFile archivo, String nombre) throws IOException, GeneralSecurityException {
        if (!archivo.isEmpty()) {
            Drive driveService = getDriveService();

            // Obtener o crear la carpeta
            String folderId = getOrCreateFolder(nameFolder);
            System.out.println("ID CARPETA: " + folderId);

            // Crear metadatos del archivo
            File fileMetadata = new File();
            fileMetadata.setName(nombre);
            fileMetadata.setParents(Collections.singletonList(folderId));

            // Definir el contenido del archivo usando InputStreamContent
            InputStream inputStream = archivo.getInputStream();
            InputStreamContent mediaContent = new InputStreamContent(archivo.getContentType(), inputStream);

            // Subir el archivo
            File fileUploaded = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // Cerrar el InputStream
            inputStream.close();

            return fileUploaded.getId();  // Devolver el ID del archivo o URL si es necesario
        } else {
            System.out.println("El archivo está vacío.");
            return null;
        }
    }
	
	
	 public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
        try { 
            Resource resource = resourceLoader.getResource("classpath:static/" + nombreCarpeta);
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
            return null;
        }
    }

//	 public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
//		    try {
//		        // Carga la carpeta de recursos desde el classpath
////		        ClassPathResource resource = new ClassPathResource("static/" + nombreCarpeta);
//		        ClassPathResource resource = new ClassPathResource("static/" + nombreCarpeta);
//		        // Retorna la ruta absoluta del archivo
//		        return resource.getFile().getAbsolutePath();
//		    } catch (Exception e) {
//		        System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
//		        return null;
//		    }
//		}
	 
//		public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
//		    try {
//		        // Carga el recurso desde el classpath
//		    	System.out.println("OBTENIENDO RUTAA carpeta:"+nombreCarpeta);
//		        Resource resource = new ClassPathResource("static/" + nombreCarpeta);
//		        // Devuelve un InputStream del recurso
//		        return resource.getInputStream().toString();
//		    } catch (Exception e) {
//		        System.out.println("Error al obtener el InputStream de la carpeta de recursos: " + e.getMessage());
//		        return null;
//		    }
//		}
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
	        p.waitFor();  // Esperar a que el comando termine
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	
	

//	@Override
//	public String guargarLogo(MultipartFile archivo,String nombre) throws IOException {
// 
//		if (!archivo.isEmpty()) {  
//			
////			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
////			nombreArchivo=archivo.getOriginalFilename();
//			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
//			System.out.println("OBTENER RUTAAAA CATA LOGOS:"+obtenerRutaCarpetaRecursos("logos")); 
//			System.out.println("RUTAAA LOGOOO:"+Paths.get(ruta_logos).toAbsolutePath().resolve(nombre));
//			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("logos")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
//		}
//		return nombre;
//	}

//	@Override
//	public String guargarLogo(MultipartFile archivo,String nombre) throws IOException {
// 
//		if (!archivo.isEmpty()) {  
//			
////			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
////			nombreArchivo=archivo.getOriginalFilename();
//			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
//			System.out.println("OBTENER RUTAAAA CATA LOGOS:"+obtenerRutaCarpetaRecursos("logos")); 
//			System.out.println("RUTAAA LOGOOO:"+Paths.get(ruta_logos).toAbsolutePath().resolve(nombre));
//			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("logos")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
//		}
//		return nombre;
//	}
//	@Override
//	public String guargarSocioLogo(MultipartFile archivo,String nombre) throws IOException {
// 
//		if (!archivo.isEmpty()) {  
//			
////			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
////			nombreArchivo=archivo.getOriginalFilename();
//			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
//			System.out.println("OBTENER RUTAAAA LOGOS:"+obtenerRutaCarpetaRecursos("socioslogos")); 
//			System.out.println("RUTAAA LOGOOO:"+Paths.get(ruta_socio_logos).toAbsolutePath().resolve(nombre));
//			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("socioslogos")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
//		}
//		return nombre;
//	}

//	public String guargarCatalogo(MultipartFile archivo,String nombre) throws IOException {
//		// TODO Auto-generated method stub
//
//		if (!archivo.isEmpty()) {
//			
////			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
////			nombreArchivo=archivo.getOriginalFilename();
//			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
//			System.out.println("OBTENER RUTAAAA CATALOGOS MOD:"+obtenerRutaArchivos("catalogosimg"));
//			System.out.println("RUTA CATALOGOOOOOOOO MOD:"+Paths.get(obtenerRutaArchivos("catalogosimg")).toAbsolutePath().resolve(nombre));
//			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaArchivos("catalogosimg")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
//		}
//		return nombre; 
//	} 
	
	public String guargarArchivo(String nameFolder,MultipartFile archivo, String nombre) throws IOException {
	    if (!archivo.isEmpty()) {
	        // Obtener la ruta de la carpeta de catalogos
	        String rutaCatalogos = obtenerRutaArchivos(nameFolder);
	        
	        if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
	            throw new IOException("No se pudo determinar la ruta de almacenamiento.");
	        }

	        // Construir la ruta completa para el archivo
	        Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();
	        
	        // Asegúrate de que la ruta sea válida y existe
	        if (!Files.exists(rutaDirectorio)) {
	            Files.createDirectories(rutaDirectorio);  // Crea el directorio si no existe
	        }

	        Path rutaArchivo = rutaDirectorio.resolve(nombre);
	        System.out.println("Guardando archivo en la ruta: " + rutaArchivo);

	        // Copiar el archivo a la ubicación deseada, reemplazando si ya existe
	        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
	        return nombre;  // Retorna el nombre del archivo guardado
	    } else {
	        System.out.println("El archivo está vacío.");
	        return null;  // Retorna null si el archivo está vacío
	    }
	}
//	public String guargarCatalogo(MultipartFile archivo, String nombre) throws IOException {
//	    if (!archivo.isEmpty()) {
//	        // Obtener la ruta de la carpeta de catalogos
//	        String rutaCatalogos = obtenerRutaArchivos("catalogosimg");
//	        
//	        if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
//	            throw new IOException("No se pudo determinar la ruta de almacenamiento.");
//	        }
//
//	        // Construir la ruta completa para el archivo
//	        Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();
//	        
//	        // Asegúrate de que la ruta sea válida y existe
//	        if (!Files.exists(rutaDirectorio)) {
//	            Files.createDirectories(rutaDirectorio);  // Crea el directorio si no existe
//	        }
//
//	        Path rutaArchivo = rutaDirectorio.resolve(nombre);
//	        System.out.println("Guardando archivo en la ruta: " + rutaArchivo);
//
//	        // Copiar el archivo a la ubicación deseada, reemplazando si ya existe
//	        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
//	        return nombre;  // Retorna el nombre del archivo guardado
//	    } else {
//	        System.out.println("El archivo está vacío.");
//	        return null;  // Retorna null si el archivo está vacío
//	    }
//	}
	
//    @Override
//    public String guargarLogo(MultipartFile archivo, String nombre) throws IOException {
//        if (!archivo.isEmpty()) {
//            Resource resource = resourceLoader.getResource("classpath:/static/logos/" + nombre);
//            try (InputStream inputStream = archivo.getInputStream()) {
//                Files.copy(inputStream, resource.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
//            }
//        }
//        return nombre;
//    }
//
//    @Override
//    public String guargarCatalogo(MultipartFile archivo, String nombre) throws IOException {
//        if (!archivo.isEmpty()) {
//            Resource resource = resourceLoader.getResource("classpath:/static/catalogos/" + nombre);
//            try (InputStream inputStream = archivo.getInputStream()) {
//                Files.copy(inputStream, resource.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
//            }
//        }
//        return nombre;
//    }

	@Override
	public String guargarMultipleArchivos(List<MultipartFile> archivos)  throws IOException{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Path linkArchivo(String folder,String nombreArchivo) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Nombre de archvo a buscar link antes de eliminar:"+nombreArchivo);
        String rutaCatalogos = obtenerRutaArchivos(folder);
        
        if (rutaCatalogos == null || rutaCatalogos.isEmpty()) {
            throw new IOException("No se pudo determinar la ruta de almacenamiento.");
        }

        // Construir la ruta completa para el archivo
        Path rutaDirectorio = Paths.get(rutaCatalogos).toAbsolutePath();
        
        // Asegúrate de que la ruta sea válida y existe
        if (!Files.exists(rutaDirectorio)) {
            Files.createDirectories(rutaDirectorio);  // Crea el directorio si no existe
        }
        if (nombreArchivo!=null) {
        	Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
//          Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("ruta de archivo encontrado: " + rutaArchivo);
    		if (rutaArchivo!=null) {
    			
//    			return Paths.get(obtenerRutaArchivos(folder)).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
    			return rutaArchivo;//almacenamos el archivo
    		} else {
    			return null;
    		}
		}else {
			return null;
		}
        


	}
	
//	@Override
//	public Path linkArchivoLogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//		
//		
//		return Paths.get(obtenerRutaCarpetaRecursos("logos")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
//	}
//	@Override
//	public Path linkArchivoSocioLogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//		
//		
//		return Paths.get(obtenerRutaCarpetaRecursos("socioslogos")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
//	}
//	@Override
//	public Path linkArchivoCatalogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//	
//		return Paths.get(obtenerRutaCarpetaRecursos("catalogosimg")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
//	}
//	@Override
//	public void eliminarArchivoLogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//		Path archivo=linkArchivoLogo(nombreArchivo);
//		try {
//			FileSystemUtils.deleteRecursively(archivo);
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e);
//		}
//	}
//	@Override
//	public void eliminarArchivoSocioLogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//		Path archivo=linkArchivoSocioLogo(nombreArchivo);
//		try {
//			FileSystemUtils.deleteRecursively(archivo);
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e);
//		}
//	}
	
	
//	@Override
//	public void eliminarArchivoCatalogo(String nombreArchivo) {
//		// TODO Auto-generated method stub
//		Path archivo=linkArchivoCatalogo(nombreArchivo);
//		try {
//			FileSystemUtils.deleteRecursively(archivo);
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e);
//		}
//	}
	@Override
	public void eliminarArchivo(String folder,String nombreArchivo) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("INTENTANDO ELIMINADO ARCHJIVO:"+nombreArchivo);
		Path archivo=linkArchivo(folder,nombreArchivo);
		System.out.println("INTENTANOD PATH ELIMINAR ARCHIVO:"+archivo);
		try {
			if (archivo!=null) {
				System.out.println("**********ELIMINANDO ARCHIVOO"+archivo);
				FileSystemUtils.deleteRecursively(archivo);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	
	
	public void eliminarArchivoDrive(String folderName, String nombreArchivo) throws IOException, GeneralSecurityException {
	    Drive driveService = getDriveService();

	    // Obtener el ID de la carpeta
	    String folderId = getOrCreateFolder(folderName);
	    System.out.println("ID de la carpeta: " + folderId);
	    
	    // Buscar el archivo dentro de la carpeta
	    FileList result = driveService.files().list()
	            .setQ("mimeType!='application/vnd.google-apps.folder' and name='" + nombreArchivo + "' and '" + folderId + "' in parents and trashed=false")
	            .setSpaces("drive")
	            .setFields("files(id, name)")
	            .execute();

	    List<File> files = result.getFiles();
	    if (!files.isEmpty()) {
	        // El archivo existe, eliminarlo
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
	        System.out.println("Archivo no encontrado en la carpeta: " + nombreArchivo + " (Carpeta ID: " + folderId + ")");
	    }
	}


}
