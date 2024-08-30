package org.example.project_5_safetynet.Models;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class MedicalRecord {
    public String firstName;
    public String lastName;
    public String birthdate;
    public List<String> medications;
    public List<String> allergies;

    public String getFullName(){
        return firstName+' '+lastName;
    }
    public int getAge() {
        return Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy")),LocalDate.now()).getYears();
    }
}
