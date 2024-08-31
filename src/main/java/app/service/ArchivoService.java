package app.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ArchivoService {
	public String guargarLogo(MultipartFile archivo,String nombre) throws IOException;
	public String guargarSocioLogo(MultipartFile archivo,String nombre) throws IOException;
	public String guargarCatalogo(MultipartFile archivo ,String nombre) throws IOException;
	public String guargarMultipleArchivos(List<MultipartFile> archivos) throws IOException;
	public Path linkArchivoLogo(String nombreArchivo ) throws IOException;;
	public Path linkArchivoSocioLogo(String nombreArchivo ) throws IOException;;
	public Path linkArchivoCatalogo(String nombreArchivo ) throws IOException;;
	public void eliminarArchivoLogo(String nombreArchivo) throws IOException;;
	public void eliminarArchivoSocioLogo(String nombreArchivo) throws IOException;;
	public void eliminarArchivoCatalogo(String nombreArchivo) throws IOException;;
}
