package com.tecsup.edu.pe.exam_perez.service;

import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Servicio personalizado para cargar detalles del usuario
 * Implementa UserDetailsService de Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getActive(),
                true,
                true,
                true,
                getAuthorities(user)
        );
    }

    /**
     * Obtener autoridades basadas en el rol del usuario
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Agregar el rol del usuario como autoridad
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Agregar permisos específicos basados en el rol
        if (user.getRole() == User.Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN_READ"));
            authorities.add(new SimpleGrantedAuthority("ADMIN_WRITE"));
            authorities.add(new SimpleGrantedAuthority("ADMIN_DELETE"));
            authorities.add(new SimpleGrantedAuthority("USER_READ"));
            authorities.add(new SimpleGrantedAuthority("USER_WRITE"));
        } else if (user.getRole() == User.Role.USER) {
            authorities.add(new SimpleGrantedAuthority("USER_READ"));
            authorities.add(new SimpleGrantedAuthority("USER_WRITE"));
        }

        return authorities;
    }
}
