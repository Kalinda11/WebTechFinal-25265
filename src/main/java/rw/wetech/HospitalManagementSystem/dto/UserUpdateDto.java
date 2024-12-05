package rw.wetech.HospitalManagementSystem.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import rw.wetech.HospitalManagementSystem.model.Role;
@Getter
@Setter
public class UserUpdateDto {
    private Long id;
    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
