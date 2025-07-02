package com.tecsup.edu.pe.exam_perez.controller;

import com.tecsup.edu.pe.exam_perez.config.JwtUtil;
import com.tecsup.edu.pe.exam_perez.dto.ApiResponse;
import com.tecsup.edu.pe.exam_perez.entity.Product;
import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.service.ProductService;
import com.tecsup.edu.pe.exam_perez.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para gestión de productos
 * Implementa CRUD completo con validaciones y documentación Swagger
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Operaciones CRUD para gestión de productos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Obtener todos los productos (solo usuarios autenticados)
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos",
               description = "Retorna la lista completa de productos activos",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autorizado")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success("Productos obtenidos exitosamente", products));
    }

    /**
     * Obtener producto por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID",
               description = "Retorna un producto específico por su ID",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @Parameter(description = "ID del producto") @PathVariable Long id) {

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        return ResponseEntity.ok(ApiResponse.success("Producto encontrado", product));
    }

    /**
     * Crear nuevo producto (solo administradores)
     */
    @PostMapping
    @Operation(summary = "Crear nuevo producto",
               description = "Crea un nuevo producto en el sistema (solo administradores)",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Producto creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de producto inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Valid @RequestBody Product product,
            @RequestHeader("Authorization") String token) {

        // Obtener usuario que crea el producto
        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Product newProduct = productService.createProduct(product, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Producto creado exitosamente", newProduct));
    }

    /**
     * Actualizar producto (solo administradores)
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto",
               description = "Actualiza un producto existente (solo administradores)",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {

        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", updatedProduct));
    }

    /**
     * Eliminar producto (solo administradores)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto",
               description = "Elimina un producto del sistema (solo administradores)",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProduct(
            @Parameter(description = "ID del producto") @PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
    }

    /**
     * Buscar productos por nombre
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar productos",
               description = "Busca productos por nombre o descripción",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(
            @Parameter(description = "Término de búsqueda") @RequestParam String query) {

        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(ApiResponse.success("Búsqueda completada", products));
    }

    /**
     * Obtener productos por categoría
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Obtener productos por categoría",
               description = "Retorna productos de una categoría específica",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(
            @Parameter(description = "Nombre de la categoría") @PathVariable String category) {

        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Productos por categoría obtenidos", products));
    }

    /**
     * Obtener productos en rango de precios
     */
    @GetMapping("/price-range")
    @Operation(summary = "Obtener productos por rango de precios",
               description = "Retorna productos dentro de un rango de precios",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByPriceRange(
            @Parameter(description = "Precio mínimo") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Precio máximo") @RequestParam BigDecimal maxPrice) {

        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success("Productos en rango de precio obtenidos", products));
    }

    /**
     * Obtener todas las categorías
     */
    @GetMapping("/categories")
    @Operation(summary = "Obtener todas las categorías",
               description = "Retorna lista de todas las categorías disponibles",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categorías obtenidas", categories));
    }

    /**
     * Obtener productos con stock bajo (solo administradores)
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Obtener productos con stock bajo",
               description = "Retorna productos con stock menor a 10 unidades (solo administradores)",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsWithLowStock() {
        List<Product> products = productService.getProductsWithLowStock();
        return ResponseEntity.ok(ApiResponse.success("Productos con stock bajo obtenidos", products));
    }

    /**
     * Actualizar stock de producto (solo administradores)
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock",
               description = "Actualiza el stock de un producto específico (solo administradores)",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> updateStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Nuevo stock") @RequestParam Integer stock) {

        Product updatedProduct = productService.updateStock(id, stock);
        return ResponseEntity.ok(ApiResponse.success("Stock actualizado exitosamente", updatedProduct));
    }
}
