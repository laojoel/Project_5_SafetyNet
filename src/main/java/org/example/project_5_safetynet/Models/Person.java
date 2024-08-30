package org.example.project_5_safetynet.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String zip;
    public String phone;
    public String email;

    @JsonIgnore
    public String getFullName(){
        return firstName+' '+lastName;
    }
}
