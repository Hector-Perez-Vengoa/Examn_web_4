package com.tecsup.edu.pe.exam_perez.service;

import com.tecsup.edu.pe.exam_perez.entity.Product;
import com.tecsup.edu.pe.exam_perez.entity.User;
import com.tecsup.edu.pe.exam_perez.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de productos
 * Incluye operaciones CRUD completas y lógica de negocio específica
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Crear un nuevo producto
     */
    public Product createProduct(Product product, User createdBy) {
        // Validaciones de negocio
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (product.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        // Establecer valores por defecto
        if (product.getActive() == null) {
            product.setActive(true);
        }

        // Establecer quien creó el producto
        product.setCreatedBy(createdBy);

        return productRepository.save(product);
    }

    /**
     * Obtener todos los productos
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Obtener productos activos
     */
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    /**
     * Obtener productos con stock disponible
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsWithStock() {
        return productRepository.findActiveProductsWithStock();
    }

    /**
     * Obtener producto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Actualizar producto
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Validaciones
        if (productDetails.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (productDetails.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        // Actualizar campos
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());
        product.setImageUrl(productDetails.getImageUrl());

        if (productDetails.getActive() != null) {
            product.setActive(productDetails.getActive());
        }

        return productRepository.save(product);
    }

    /**
     * Eliminar producto (soft delete)
     */
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        product.setActive(false);
        productRepository.save(product);
    }

    /**
     * Eliminar producto definitivamente
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Buscar productos por nombre
     */
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Buscar productos por nombre o descripción
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.findByNameOrDescriptionContaining(searchTerm);
    }

    /**
     * Obtener productos por categoría
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    /**
     * Obtener productos por marca
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    /**
     * Obtener productos en rango de precios
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Obtener todas las categorías
     */
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    /**
     * Obtener todas las marcas
     */
    @Transactional(readOnly = true)
    public List<String> getAllBrands() {
        return productRepository.findDistinctBrands();
    }

    /**
     * Actualizar stock de producto
     */
    public Product updateStock(Long id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (newStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        product.setStock(newStock);
        return productRepository.save(product);
    }

    /**
     * Reducir stock (para ventas)
     */
    public Product reduceStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + product.getStock() + ", Solicitado: " + quantity);
        }

        product.reduceStock(quantity);
        return productRepository.save(product);
    }

    /**
     * Obtener productos con stock bajo
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsWithLowStock() {
        return productRepository.findProductsWithLowStock();
    }

    /**
     * Contar productos por categoría
     */
    @Transactional(readOnly = true)
    public long countProductsByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    /**
     * Contar productos activos
     */
    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return productRepository.countByActiveTrue();
    }
}
