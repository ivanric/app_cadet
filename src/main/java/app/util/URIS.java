package app.util;



import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;




public class URIS {
	private ResourceLoader resourceLoader=new DefaultResourceLoader();;
	public static String jasperReport="/static/jasperreports/";
//	public static String jasperReport="/app/jasperreports/";

	public static String imgJasperReport="/static/img/report/";
//	public static String imgJasperReport="/app/img/report/";
	
	
//	public static String direccionFotos=".//src//main//resources//static//archivos//fotos//";
//	public static String direccionExcels=".//src//main//resources//static//archivos//excels//";
//	public static String direccionTextos=".//src//main//resources//static//archivos//textos//";
//	public static String direccionCsvs=".//src//main//resources//static//archivos//csvs//";

	 
	public String GetDirecccionCsvs(HttpServletRequest req) {
//		return req.getSession().getServletContext().getRealPath("/static/archivos/csvs/");
		return obtenerRutaCarpetaRecursos("csvs");
	}
	public String GetDirecccionExcels(HttpServletRequest req) {
//		return req.getSession().getServletContext().getRealPath("/static/archivos/excels/");
		return obtenerRutaCarpetaRecursos("excels");
	}
	public String GetDirecccionTextos(HttpServletRequest req) {
//		return req.getSession().getServletContext().getRealPath("/static/archivos/textos/");
		return obtenerRutaCarpetaRecursos("textos");
	}
	public String GetDirecccionFotos(HttpServletRequest req) {
//		return req.getSession().getServletContext().getRealPath("/static/archivos/fotos/");
		return obtenerRutaCarpetaRecursos("fotos");
	}
	
	
	
	 public String obtenerRutaCarpetaRecursos(String nombreCarpeta) {
        try { 
            Resource resource = resourceLoader.getResource("classpath:static/archivos/" + nombreCarpeta);
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Error al obtener la ruta de la carpeta de recursos: " + e.getMessage());
            return null;
        }
    }
	 
	 
	  public boolean setPermitionFolfer(String ruta) {
		  	//TODO: MAKE THIS DO A SWITCH STATEMETN INSTEAD OF A 4 IF STATEMENTS.
		        if (checkOS().contains("Windows")) {
		        	System.out.println("ENTRO WINDOWS");
		            try {
		                String line;
		                Process p = Runtime.getRuntime().exec("takeown /f "+ruta+ " /r");
		                return true;
		            }
		            catch (Exception err) {
		            	System.out.println(err.getMessage());
		            }
		            return false;
		        }

		        else if (checkOS().contains("Linux")) {
		            try {
		            	System.out.println("ENTRO linux permisos");
		                Process p = Runtime.getRuntime().exec("chmod -R 777 /opt/");
		                Process p1 = Runtime.getRuntime().exec("chown -R postgres:postgres /opt/");
		                return true;
		            }
		            catch (Exception err) {
		                System.out.println(err.getMessage());
		            }
		            return false;
		        }
		        else {
		            return false;
		        }
	  }
	 
	    public String checkOS() {
	        String os;
	        os = System.getProperty("os.name");
	        return os;
	    }
}
