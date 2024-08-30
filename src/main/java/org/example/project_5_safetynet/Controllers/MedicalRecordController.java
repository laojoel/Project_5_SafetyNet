package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.Models.FireStation;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.example.project_5_safetynet.Services.FireStationService;
import org.example.project_5_safetynet.Services.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MedicalRecordController {
    @PostMapping("/medicalrecord")
    public ResponseEntity<String> Post_MedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        System.out.println("ENTRY OK");
        MedicalRecordService.createNewMedicalRecord(medicalRecord);
        return ResponseEntity.ok("New MedicalRecord Successfully created");
    }

    @PutMapping("/medicalrecord")
    public ResponseEntity<String> Put_MedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        if (MedicalRecordService.updateMedicalRecord(medicalRecord)){return ResponseEntity.ok("MedicalRecord Successfully updated");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

    }

    @DeleteMapping("/medicalrecord")
    public ResponseEntity<String> Delete_MedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        if (MedicalRecordService.deleteMedicalRecord(medicalRecord)){return ResponseEntity.ok("MedicalRecord Successfully Deleted");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
    }
}
