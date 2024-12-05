package rw.wetech.HospitalManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;


public class LoginRequest {
    private String username;
    private String password;

    // Ensure getters and setters are present
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
