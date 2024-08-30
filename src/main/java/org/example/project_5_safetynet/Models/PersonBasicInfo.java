package org.example.project_5_safetynet.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBasicInfo {
    String firstName;
    String lastName;
    String address;
    String phone;
}
