package app.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import app.repository.CatalogoRepository;



@Service
public class ArchivoServiceImpl implements ArchivoService {

	private String ruta_logos="/static/logos/";
	private String ruta_socio_logos="/static/socioslogos/";
//	private String ruta_logos=".//src//main//resources//static//logos//";
//	private String ruta_catalogos=".//src//main//resources//static//catalogos//";
	private String ruta_catalogos="/static/catalogosimg/";
//	private ResourceLoader resourceLoader=new DefaultResourceLoader();
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
	
	@Override
	public String guargarLogo(MultipartFile archivo,String nombre) throws IOException {
 
		if (!archivo.isEmpty()) {  
			
//			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
//			nombreArchivo=archivo.getOriginalFilename();
			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
			System.out.println("OBTENER RUTAAAA LOGOS:"+obtenerRutaCarpetaRecursos("logos")); 
			System.out.println("RUTAAA LOGOOO:"+Paths.get(ruta_logos).toAbsolutePath().resolve(nombre));
			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("logos")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
		}
		return nombre;
	}
	@Override
	public String guargarSocioLogo(MultipartFile archivo,String nombre) throws IOException {
 
		if (!archivo.isEmpty()) {  
			
//			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
//			nombreArchivo=archivo.getOriginalFilename();
			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
			System.out.println("OBTENER RUTAAAA LOGOS:"+obtenerRutaCarpetaRecursos("socioslogos")); 
			System.out.println("RUTAAA LOGOOO:"+Paths.get(ruta_socio_logos).toAbsolutePath().resolve(nombre));
			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("socioslogos")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
		}
		return nombre;
	}

	public String guargarCatalogo(MultipartFile archivo,String nombre) throws IOException {
		// TODO Auto-generated method stub

		if (!archivo.isEmpty()) {
			
//			nombreArchivo="cod-"+this.catalogoRepository.getCodigo()+archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
//			nombreArchivo=archivo.getOriginalFilename();
			// StandardCopyOption.REPLACE_EXISTING si hay un archivo con ese mismo nombre lo reemplazara
			System.out.println("OBTENER RUTAAAA CATALOGOS:"+obtenerRutaCarpetaRecursos("catalogosimg"));
			System.out.println("RUTA CATALOGOOOOOOOO:"+Paths.get(obtenerRutaCarpetaRecursos("catalogosimg")).toAbsolutePath().resolve(nombre));
			Files.copy(archivo.getInputStream(),Paths.get(obtenerRutaCarpetaRecursos("catalogosimg")).toAbsolutePath().resolve(nombre),StandardCopyOption.REPLACE_EXISTING);
		}
		return nombre; 
	}  
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
	public Path linkArchivoLogo(String nombreArchivo) {
		// TODO Auto-generated method stub
		
		
		return Paths.get(obtenerRutaCarpetaRecursos("logos")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
	}
	@Override
	public Path linkArchivoSocioLogo(String nombreArchivo) {
		// TODO Auto-generated method stub
		
		
		return Paths.get(obtenerRutaCarpetaRecursos("socioslogos")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
	}
	@Override
	public Path linkArchivoCatalogo(String nombreArchivo) {
		// TODO Auto-generated method stub
	
		return Paths.get(obtenerRutaCarpetaRecursos("catalogosimg")).toAbsolutePath().resolve(nombreArchivo);//almacenamos el archivo
	}
	@Override
	public void eliminarArchivoLogo(String nombreArchivo) {
		// TODO Auto-generated method stub
		Path archivo=linkArchivoLogo(nombreArchivo);
		try {
			FileSystemUtils.deleteRecursively(archivo);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	@Override
	public void eliminarArchivoSocioLogo(String nombreArchivo) {
		// TODO Auto-generated method stub
		Path archivo=linkArchivoSocioLogo(nombreArchivo);
		try {
			FileSystemUtils.deleteRecursively(archivo);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	@Override
	public void eliminarArchivoCatalogo(String nombreArchivo) {
		// TODO Auto-generated method stub
		Path archivo=linkArchivoCatalogo(nombreArchivo);
		try {
			FileSystemUtils.deleteRecursively(archivo);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

}
