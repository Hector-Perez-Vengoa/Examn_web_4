package com.tecsup.edu.pe.exam_perez.repository;

import com.tecsup.edu.pe.exam_perez.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad User
 * Incluye métodos personalizados para consultas específicas
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Buscar usuario por nombre de usuario
     */
    Optional<User> findByUsername(String username);

    /**
     * Buscar usuario por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verificar si existe un usuario con el username dado
     */
    boolean existsByUsername(String username);

    /**
     * Verificar si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Buscar usuarios activos
     */
    List<User> findByActiveTrue();

    /**
     * Buscar usuarios por rol
     */
    List<User> findByRole(User.Role role);

    /**
     * Buscar usuarios por nombre o apellido (búsqueda parcial)
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByFirstNameOrLastNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Buscar usuarios activos por rol
     */
    List<User> findByRoleAndActiveTrue(User.Role role);

    /**
     * Contar usuarios por rol
     */
    long countByRole(User.Role role);

    /**
     * Contar usuarios activos
     */
    long countByActiveTrue();
}
