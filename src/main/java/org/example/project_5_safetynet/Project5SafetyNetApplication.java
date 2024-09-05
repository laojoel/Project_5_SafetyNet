package org.example.project_5_safetynet;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import java.io.IOException;

@SpringBootApplication
public class Project5SafetyNetApplication {
    public static final Logger logger = LoggerFactory.getLogger(Project5SafetyNetApplication.class);

    public static void main(String[] args) throws IOException {
        logger.info("Initializing SafetyNet System");

        System.out.println("Log4j configuration file location: " + System.getProperty("log4j2.configurationFile"));

        SpringApplication.run(Project5SafetyNetApplication.class, args);
        DataDAO.initWithFilePath("src/main/resources/data.json");
    }

}
