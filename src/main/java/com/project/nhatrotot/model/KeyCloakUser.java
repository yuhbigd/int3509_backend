package com.project.nhatrotot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCloakUser {
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
