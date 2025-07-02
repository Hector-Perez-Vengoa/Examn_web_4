package com.tecsup.edu.pe.exam_perez.controller;

import com.tecsup.edu.pe.exam_perez.config.JwtUtil;
import com.tecsup.edu.pe.exam_perez.dto.ApiResponse;
import com.tecsup.edu.pe.exam_perez.dto.JwtResponse;
import com.tecsup.edu.pe.exam_perez.dto.LoginRequest;
import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para autenticación y autorización
 * Maneja login, registro y operaciones relacionadas con JWT
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación y autorización")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para iniciar sesión
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas")
    })
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Buscar usuario por username o email
            User user = userService.getUserByUsername(loginRequest.getUsernameOrEmail())
                    .or(() -> userService.getUserByEmail(loginRequest.getUsernameOrEmail()))
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generar token JWT
            String token = jwtUtil.generateToken(userDetails);

            // Crear respuesta
            JwtResponse jwtResponse = new JwtResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                jwtUtil.getExpirationTime()
            );

            return ResponseEntity.ok(ApiResponse.success("Login exitoso", jwtResponse));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    /**
     * Endpoint para registrar un nuevo usuario
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario",
               description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario registrado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de registro inválidos")
    })
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody User user) {
        try {
            // Establecer rol por defecto como USER
            user.setRole(User.Role.USER);

            User newUser = userService.createUser(user);

            // Remover password de la respuesta
            newUser.setPassword(null);

            return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", newUser));

        } catch (RuntimeException e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
        }
    }

    /**
     * Endpoint para validar token
     */
    @GetMapping("/validate")
    @Operation(summary = "Validar token",
               description = "Valida si un token JWT es válido y retorna información del usuario")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);

                if (jwtUtil.isTokenValid(jwtToken)) {
                    String username = jwtUtil.extractUsername(jwtToken);
                    return ResponseEntity.ok(ApiResponse.success("Token válido", "Usuario: " + username));
                }
            }

            return ResponseEntity.badRequest().body(ApiResponse.error("Token inválido"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al validar token"));
        }
    }

    /**
     * Endpoint para obtener información del usuario actual
     */
    @GetMapping("/me")
    @Operation(summary = "Información del usuario actual",
               description = "Retorna información del usuario autenticado")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String username = jwtUtil.extractUsername(jwtToken);

                User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Remover password de la respuesta
                user.setPassword(null);

                return ResponseEntity.ok(ApiResponse.success("Información del usuario", user));
            }

            return ResponseEntity.badRequest().body(ApiResponse.error("Token requerido"));

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información del usuario");
        }
    }
}
