package org.example.project_5_safetynet;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Services.FireStationService;
import org.example.project_5_safetynet.Services.MedicalRecordService;
import org.example.project_5_safetynet.Services.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Project5SafetyNetApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Project5SafetyNetApplication.class, args);
        DataDAO.initWithFilePath("src/main/resources/data.json");
    }

}
