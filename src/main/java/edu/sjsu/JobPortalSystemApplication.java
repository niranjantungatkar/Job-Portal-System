package edu.sjsu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;



@Configuration
@SpringBootApplication
public class JobPortalSystemApplication {
		
	public static void main(String[] args) {
		SpringApplication.run(JobPortalSystemApplication.class, args);	
	}
}
