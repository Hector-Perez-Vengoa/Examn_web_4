package com.tecsup.edu.pe.exam_perez.dto;

/**
 * DTO para respuesta de autenticación JWT
 */
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Long expiresIn;

    // Constructores
    public JwtResponse() {}

    public JwtResponse(String token, String username, String email, String fullName, String role, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
