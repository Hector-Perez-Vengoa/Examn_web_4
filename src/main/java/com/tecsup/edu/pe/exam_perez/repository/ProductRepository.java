package com.tecsup.edu.pe.exam_perez.repository;

import com.tecsup.edu.pe.exam_perez.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para la entidad Product
 * Incluye métodos personalizados para consultas específicas de productos
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Buscar productos activos
     */
    List<Product> findByActiveTrue();

    /**
     * Buscar productos por categoría
     */
    List<Product> findByCategory(String category);

    /**
     * Buscar productos por categoría y activos
     */
    List<Product> findByCategoryAndActiveTrue(String category);

    /**
     * Buscar productos por marca
     */
    List<Product> findByBrand(String brand);

    /**
     * Buscar productos con stock disponible
     */
    List<Product> findByStockGreaterThan(Integer stock);

    /**
     * Buscar productos en rango de precios
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Buscar productos por nombre (búsqueda parcial)
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Buscar productos por nombre o descripción
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm);

    /**
     * Buscar productos activos con stock disponible
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stock > 0")
    List<Product> findActiveProductsWithStock();

    /**
     * Buscar productos por categoría con stock disponible
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.stock > 0 AND p.active = true")
    List<Product> findByCategoryWithStock(@Param("category") String category);

    /**
     * Obtener todas las categorías únicas
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true")
    List<String> findDistinctCategories();

    /**
     * Obtener todas las marcas únicas
     */
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.active = true AND p.brand IS NOT NULL")
    List<String> findDistinctBrands();

    /**
     * Contar productos por categoría
     */
    long countByCategory(String category);

    /**
     * Contar productos activos
     */
    long countByActiveTrue();

    /**
     * Buscar productos con stock bajo (menos de 10 unidades)
     */
    @Query("SELECT p FROM Product p WHERE p.stock < 10 AND p.active = true")
    List<Product> findProductsWithLowStock();
}
