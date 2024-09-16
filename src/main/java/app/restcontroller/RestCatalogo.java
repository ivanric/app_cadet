package app.restcontroller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import app.config.GoogleDriveConfig;
import app.entity.CatalogoEntity;
import app.entity.SocioEntity;
import app.service.ArchivoService;
import app.service.CatalogoServiceImpl;
import app.util.Constantes;
import app.util.URIS;

@RestController
@RequestMapping("/RestCatalogos") 
public class RestCatalogo extends RestControllerGenericNormalImpl<CatalogoEntity,CatalogoServiceImpl> {

	@Autowired private ArchivoService archivoService;
	
	@Autowired
    private GoogleDriveConfig googleDriveConfig;
	
	//drive
    private Drive getDriveService() throws IOException, GeneralSecurityException {
        return googleDriveConfig.getDriveService();
    }
	
	@GetMapping("/listar")
	public ResponseEntity<?> getAll(HttpServletRequest request,@Param("draw")int draw,@Param("length")int length,@Param("start")int start,@Param("estado")int estado)throws IOException{
		String total="";
		Map<String, Object> Data = new HashMap<String, Object>();
		try {

			String search = request.getParameter("search[value]");
			int tot=Constantes.NUM_MAX_DATATABLE;
			System.out.println("tot:"+tot+"estado:"+estado+"search:"+search+"length:"+length+"start:"+start);
			List<?> lista= servicio.findAll(estado, search, length, start);
			System.out.println("listar:"+lista.toString()); 
			try {

					total=String.valueOf(servicio.getTotAll(search,estado));	
						
			} catch (Exception e) {
				total="0";
			}
			Data.put("draw", draw);
			Data.put("recordsTotal", total);
			Data.put("data", lista);
			if(!search.equals(""))
				Data.put("recordsFiltered", lista.size());
			else
				Data.put("recordsFiltered", total);
			
			return ResponseEntity.status(HttpStatus.OK).body(Data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Data);
		}
	}
	
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody CatalogoEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	servicio.updateStatus(entity.getEstado(), entity.getId());
        	CatalogoEntity entity2=servicio.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/guardar")
	public ResponseEntity<?> save(CatalogoEntity entidad,@RequestParam("logo") MultipartFile file,@RequestParam("catalogo") MultipartFile catalogo){
//	public ResponseEntity<?> save(CatalogoEntity entidad){
		System.out.println("EntidadSave LLEGO:"+entidad.toString());

		try {
			
			
			return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @PostMapping("/modificar/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,CatalogoEntity entidad ,@RequestParam("logo") MultipartFile file,@RequestParam("catalogo") MultipartFile catalogo){
		try {
			System.out.println("EntidadModificar LLEGO:"+entidad.toString());
			return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id){ 
        try { 
        	System.out.println("ID A BUSCAR");
        	CatalogoEntity entity=new CatalogoEntity();
        	entity=servicio.findById(Integer.parseInt(id));
        	if (entity!=null) {
        		System.out.println("Socio encontrado:"+entity.toString());	
			}
        	
            return ResponseEntity.status(HttpStatus.OK).body(entity);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    /*
    @GetMapping("/logo_empresa/{filename}")
    public ResponseEntity<Resource> getFile_logo_empresa(@PathVariable String filename) {
        try {
            System.out.println("ENTRO LOGO CATALOgo:" + filename);
            URIS uris = new URIS();
            String sistemaOperativo = uris.checkOS();
            System.out.println("INICIANDO APP");
            System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);

            Resource resource = null;

            try {
                Path filePath;
                if (sistemaOperativo.contains("Linux")) {
                    filePath = Paths.get("/home", Constantes.nameFolderLogoCatalogo).resolve(filename).normalize();
                    resource = new UrlResource(filePath.toUri());
                } else if (sistemaOperativo.contains("Windows")) {
                    filePath = Paths.get("C:\\", Constantes.nameFolderLogoCatalogo).resolve(filename).normalize();
                    resource = new UrlResource(filePath.toUri());
                }

                // Verifica si el recurso fue encontrado y es legible
                if (resource != null && resource.exists() && resource.isReadable()) {
                    String contentType = "application/octet-stream"; // Tipo de contenido por defecto
                    try {
                        contentType = Files.probeContentType(resource.getFile().toPath());
                    } catch (IOException ex) {
                        System.out.println("No se pudo determinar el tipo de archivo.");
                    }

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al obtener la ruta de archivos: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/img_catalogos_empresa/{filename}")
    public ResponseEntity<Resource> getFile_img_catalogos_empresa(@PathVariable String filename) {
        try {
            System.out.println("ENTRO CATALOGO EMPRESA:" + filename);
            URIS uris = new URIS();
            String sistemaOperativo = uris.checkOS();
            System.out.println("INICIANDO APP");
            System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);

            Resource resource = null;

            try {
                Path filePath;
                if (sistemaOperativo.contains("Linux")) {
                    filePath = Paths.get("/home", Constantes.nameFolderImgCatalogo).resolve(filename).normalize();
                    resource = new UrlResource(filePath.toUri());
                } else if (sistemaOperativo.contains("Windows")) {
                    filePath = Paths.get("C:\\", Constantes.nameFolderImgCatalogo).resolve(filename).normalize();
                    resource = new UrlResource(filePath.toUri());
                }

                // Verifica si el recurso fue encontrado y es legible
                if (resource != null && resource.exists() && resource.isReadable()) {
                    String contentType = "application/octet-stream"; // Tipo de contenido por defecto
                    try {
                        contentType = Files.probeContentType(resource.getFile().toPath());
                    } catch (IOException ex) {
                        System.out.println("No se pudo determinar el tipo de archivo.");
                    }

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al obtener la ruta de archivos: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    */
    
    

//    @GetMapping("/logo_empresa/{filename}")
//    public ResponseEntity<Resource> getFile_logo_empresa(@PathVariable String filename) {
//        try {
//            Drive driveService = getDriveService(); // Asegúrate de tener definido este método correctamente
//
//            // Obtener ID de la carpeta de logos
//            String folderId = archivoService.getOrCreateFolder(Constantes.nameFolderLogoCatalogo); 
//
//            // Buscar el archivo por nombre dentro de la carpeta
//            FileList result = driveService.files().list()
//                    .setQ("name='" + filename + "' and '" + folderId + "' in parents and trashed=false")
//                    .setFields("files(id, name)")
//                    .execute();
//
//            if (result.getFiles().isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//
//            String fileId = result.getFiles().get(0).getId();
//
//            // Obtener el archivo desde Google Drive
//            InputStream inputStream = driveService.files().get(fileId).executeMediaAsInputStream();
//            InputStreamResource resource = new InputStreamResource(inputStream);
//
//            // Definir headers de la respuesta
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"") // Uso correcto de CONTENT_DISPOSITION
//                    .body(resource);
//
//        } catch (IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    
    @GetMapping("/logo_empresa/{filename}")
    public ResponseEntity<Resource> getFile_logo_empresa(@PathVariable String filename) {
        try {
            // Obtener el ID de la carpeta de logos en Google Drive
            String folderId = archivoService.getOrCreateFolder(Constantes.nameFolderLogoCatalogo);
            
            // Obtener el ID del archivo en Google Drive utilizando el método existente
            String fileId = archivoService.obtenerIdArchivoDrivePorNombre(filename, folderId);
            
            if (fileId != null) {
                // Obtener el servicio de Google Drive
                Drive driveService = getDriveService();

                // Descargar el archivo desde Google Drive como InputStream
                InputStream inputStream = driveService.files().get(fileId).executeMediaAsInputStream();
                InputStreamResource resource = new InputStreamResource(inputStream);

                // Determinar el tipo de contenido del archivo
                String contentType = "application/octet-stream"; // Tipo por defecto
                try {
                    contentType = Files.probeContentType(Paths.get(filename));
                } catch (IOException e) {
                    System.out.println("No se pudo determinar el tipo de archivo.");
                }

                // Devolver el archivo como respuesta
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/img_catalogos_empresa/{filename}")
    public ResponseEntity<Resource> getFileImgCatalogosEmpresa(@PathVariable String filename) {
        try {
            // Obtener el ID de la carpeta en Google Drive
            String folderId = archivoService.getOrCreateFolder(Constantes.nameFolderImgCatalogo);
            
            // Obtener el ID del archivo en Google Drive
            String fileId = archivoService.obtenerIdArchivoDrivePorNombre(filename, folderId);
            
            if (fileId != null) {
                // Obtener el servicio de Google Drive
                Drive driveService = getDriveService();
                
                // Descargar el archivo
                OutputStream outputStream = new ByteArrayOutputStream();
                driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
                
                // Convertir el archivo descargado en un recurso
                ByteArrayInputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
                Resource resource = new InputStreamResource(inputStream);
                
                // Determinar el tipo de contenido del archivo
                String contentType = "application/octet-stream"; // Tipo por defecto
                try {
                    contentType = Files.probeContentType(Paths.get(filename));
                } catch (IOException e) {
                    System.out.println("No se pudo determinar el tipo de archivo.");
                }

                // Devolver el archivo como respuesta
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
