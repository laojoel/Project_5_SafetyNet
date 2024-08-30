package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class MedicalRecordService {
    public static void createNewMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        DataDAO.createNewMedicalRecord(medicalRecord);
    }
    public static boolean updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        return DataDAO.updateMedicalRecord(medicalRecord);
    }
    public static boolean deleteMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        return DataDAO.deleteMedicalRecord(medicalRecord);
    }
}
