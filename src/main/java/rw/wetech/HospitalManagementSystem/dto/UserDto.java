package rw.wetech.HospitalManagementSystem.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import rw.wetech.HospitalManagementSystem.model.Role;
@Getter
@Setter
public class UserDto {

    private String username;
    private String password;
    private String email;

    private String phoneNumber;
    private String firstName;
    private String lastName;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String resetToken;
}
