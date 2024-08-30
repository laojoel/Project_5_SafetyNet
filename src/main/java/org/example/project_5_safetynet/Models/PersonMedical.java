package org.example.project_5_safetynet.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonMedical {
    public String firstName;
    public String lastName;
    public String phone;
    public int age;
    public List<String> medications;
    public List<String> allergies;

}
