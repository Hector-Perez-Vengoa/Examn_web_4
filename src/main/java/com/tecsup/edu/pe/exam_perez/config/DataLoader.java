package com.tecsup.edu.pe.exam_perez.config;

import com.tecsup.edu.pe.exam_perez.entity.Product;
import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.repository.ProductRepository;
import com.tecsup.edu.pe.exam_perez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cargador de datos inicial para la aplicación
 * Crea usuarios y productos de ejemplo al iniciar la aplicación
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos
        if (userRepository.count() == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        // Crear usuario administrador
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@tecsup.edu.pe");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Administrador");
        admin.setLastName("Sistema");
        admin.setRole(User.Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);

        // Crear usuario normal
        User user = new User();
        user.setUsername("usuario");
        user.setEmail("usuario@tecsup.edu.pe");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("Usuario");
        user.setLastName("Prueba");
        user.setRole(User.Role.USER);
        user.setActive(true);
        userRepository.save(user);

        // Crear productos de ejemplo
        createSampleProducts(admin);

        System.out.println("=== DATOS INICIALES CARGADOS ===");
        System.out.println("Usuario Admin - Username: admin, Password: admin123");
        System.out.println("Usuario Normal - Username: usuario, Password: user123");
        System.out.println("Se han creado productos de ejemplo");
        System.out.println("===============================");
    }

    private void createSampleProducts(User admin) {
        // Productos de tecnología
        Product laptop = new Product();
        laptop.setName("Laptop HP Pavilion");
        laptop.setDescription("Laptop HP Pavilion 15 pulgadas, Intel i5, 8GB RAM, 512GB SSD");
        laptop.setPrice(new BigDecimal("2500.00"));
        laptop.setStock(15);
        laptop.setCategory("Tecnología");
        laptop.setBrand("HP");
        laptop.setImageUrl("https://example.com/laptop.jpg");
        laptop.setCreatedBy(admin);
        productRepository.save(laptop);

        Product smartphone = new Product();
        smartphone.setName("iPhone 14");
        smartphone.setDescription("iPhone 14 128GB, Pantalla 6.1 pulgadas, Cámara dual");
        smartphone.setPrice(new BigDecimal("3500.00"));
        smartphone.setStock(25);
        smartphone.setCategory("Tecnología");
        smartphone.setBrand("Apple");
        smartphone.setImageUrl("https://example.com/iphone.jpg");
        smartphone.setCreatedBy(admin);
        productRepository.save(smartphone);

        Product tablet = new Product();
        tablet.setName("Samsung Galaxy Tab");
        tablet.setDescription("Tablet Samsung Galaxy Tab A8, 10.5 pulgadas, 64GB");
        tablet.setPrice(new BigDecimal("800.00"));
        tablet.setStock(20);
        tablet.setCategory("Tecnología");
        tablet.setBrand("Samsung");
        tablet.setImageUrl("https://example.com/tablet.jpg");
        tablet.setCreatedBy(admin);
        productRepository.save(tablet);

        // Productos de hogar
        Product silla = new Product();
        silla.setName("Silla Ergonómica");
        silla.setDescription("Silla de oficina ergonómica con soporte lumbar");
        silla.setPrice(new BigDecimal("450.00"));
        silla.setStock(30);
        silla.setCategory("Hogar");
        silla.setBrand("Oficina Pro");
        silla.setImageUrl("https://example.com/silla.jpg");
        silla.setCreatedBy(admin);
        productRepository.save(silla);

        Product mesa = new Product();
        mesa.setName("Mesa de Escritorio");
        mesa.setDescription("Mesa de escritorio de madera, 120x60cm");
        mesa.setPrice(new BigDecimal("350.00"));
        mesa.setStock(12);
        mesa.setCategory("Hogar");
        mesa.setBrand("Muebles Lima");
        mesa.setImageUrl("https://example.com/mesa.jpg");
        mesa.setCreatedBy(admin);
        productRepository.save(mesa);

        // Productos con stock bajo para demostrar funcionalidad
        Product productoStockBajo = new Product();
        productoStockBajo.setName("Producto Stock Bajo");
        productoStockBajo.setDescription("Producto para demostrar alerta de stock bajo");
        productoStockBajo.setPrice(new BigDecimal("100.00"));
        productoStockBajo.setStock(5); // Stock bajo
        productoStockBajo.setCategory("Prueba");
        productoStockBajo.setBrand("Test");
        productoStockBajo.setCreatedBy(admin);
        productRepository.save(productoStockBajo);
    }
}
