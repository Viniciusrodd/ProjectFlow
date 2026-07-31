
// packages
package com.example.ProjectFlow;

// imports
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class ProjectFlowApplication {

   // main
	public static void main(String[] args) {
		SpringApplication.run(ProjectFlowApplication.class, args);
	}

}