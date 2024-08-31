package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CadetappApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CadetappApplication.class, args);
		BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
		String password= bCryptPasswordEncoder.encode("ivanric12");//encrypt password
		System.out.println(password);//$2a$10$nrjVExWrFoz7NGFYL82ix.O0zMFreoAsaLSGtpfhruCuq1BcY.gDm
		
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(CadetappApplication.class);
	}

}
