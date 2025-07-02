package com.tecsup.edu.pe.exam_perez.service;

import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de usuarios
 * Incluye operaciones CRUD y lógica de negocio específica
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crear un nuevo usuario
     */
    public User createUser(User user) {
        // Validar que el username y email no existan
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Establecer valores por defecto
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        if (user.getActive() == null) {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    /**
     * Obtener todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtener usuarios activos
     */
    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Obtener usuario por username
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Obtener usuario por email
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Actualizar usuario
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar cambios de username y email
        if (!user.getUsername().equals(userDetails.getUsername()) &&
            userRepository.existsByUsername(userDetails.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (!user.getEmail().equals(userDetails.getEmail()) &&
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Actualizar campos
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());

        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        if (userDetails.getActive() != null) {
            user.setActive(userDetails.getActive());
        }

        // Solo actualizar contraseña si se proporciona una nueva
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Eliminar usuario (soft delete)
     */
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Eliminar usuario definitivamente
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Buscar usuarios por nombre o apellido
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String searchTerm) {
        return userRepository.findByFirstNameOrLastNameContainingIgnoreCase(searchTerm);
    }

    /**
     * Obtener usuarios por rol
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * Cambiar contraseña de usuario
     */
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Actualizar contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Contar usuarios por rol
     */
    @Transactional(readOnly = true)
    public long countUsersByRole(User.Role role) {
        return userRepository.countByRole(role);
    }

    /**
     * Contar usuarios activos
     */
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
    }
}
