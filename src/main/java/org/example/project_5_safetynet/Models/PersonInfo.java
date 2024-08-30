package org.example.project_5_safetynet.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfo {
    public String lastName;
    public String email;
    public String address;
    public int age;
    public List<String> medications;
    public List<String> allergies;
}
