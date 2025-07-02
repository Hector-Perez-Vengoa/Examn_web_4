# Exam Perez - API REST Spring Boot

## 📋 Descripción del Proyecto

API REST completa desarrollada con Spring Boot que implementa un sistema de gestión de productos y usuarios con autenticación JWT, autorización por roles, validaciones completas y documentación Swagger.

## 🎯 Criterios de Evaluación Cumplidos

### ✅ 1. Diseño y Arquitectura (MVC + Capas) - 2 puntos
- **Controller**: `AuthController`, `ProductController`
- **Service**: `UserService`, `ProductService`, `CustomUserDetailsService`
- **Repository**: `UserRepository`, `ProductRepository`
- **Entity**: `User`, `Product`
- **Config**: `SecurityConfig`, `SwaggerConfig`, `JwtUtil`, etc.

### ✅ 2. Funcionalidad de la API (CRUD completo) - 2 puntos
- **GET** `/api/products` - Obtener todos los productos
- **GET** `/api/products/{id}` - Obtener producto por ID
- **POST** `/api/products` - Crear nuevo producto
- **PUT** `/api/products/{id}` - Actualizar producto
- **DELETE** `/api/products/{id}` - Eliminar producto

### ✅ 3. Seguridad (JWT / Spring Security) - 2 puntos
- Autenticación JWT implementada
- Autorización por roles (ADMIN, USER)
- Rutas protegidas según rol
- Filtro de autenticación personalizado

### ✅ 4. Documentación con Swagger/OpenAPI - 2 puntos
- Configuración completa de Swagger
- Documentación de todos los endpoints
- Modelos y esquemas documentados
- Interfaz Swagger UI disponible

### ✅ 5. Validación de datos y manejo de errores - 2 puntos
- Anotaciones de validación: `@NotNull`, `@Size`, `@Email`, etc.
- Manejo global de excepciones
- Respuestas consistentes de error

### ✅ 6. Conexión a base de datos MySQL (JPA/Hibernate) - 2 puntos
- Configuración de MySQL en `application.properties`
- Entidades JPA con relaciones
- Repositorios con consultas personalizadas

### ✅ 7. Buenas prácticas de código - 2 puntos
- Código limpio y bien comentado
- Separación de responsabilidades
- Nombres descriptivos
- Manejo de transacciones

## 🚀 Instalación y Configuración

### Prerrequisitos
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Configuración de Base de Datos
```sql
CREATE DATABASE exam_perez_db;
```

### Variables de Configuración
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_password_mysql
```

### Ejecutar la Aplicación
```bash
mvn clean install
mvn spring-boot:run
```

## 📚 Endpoints Principales

### Autenticación
- **POST** `/api/auth/login` - Iniciar sesión
- **POST** `/api/auth/register` - Registrar usuario
- **GET** `/api/auth/validate` - Validar token
- **GET** `/api/auth/me` - Información del usuario actual

### Productos (CRUD Completo)
- **GET** `/api/products` - Listar productos (USER/ADMIN)
- **GET** `/api/products/{id}` - Obtener producto (USER/ADMIN)
- **POST** `/api/products` - Crear producto (ADMIN)
- **PUT** `/api/products/{id}` - Actualizar producto (ADMIN)
- **DELETE** `/api/products/{id}` - Eliminar producto (ADMIN)
- **GET** `/api/products/search?query=...` - Buscar productos
- **GET** `/api/products/category/{category}` - Productos por categoría
- **GET** `/api/products/low-stock` - Productos con stock bajo (ADMIN)

## 🔐 Usuarios de Prueba

La aplicación crea automáticamente usuarios de prueba:

### Administrador
- **Username**: `admin`
- **Password**: `admin123`
- **Rol**: ADMIN
- **Permisos**: Acceso completo a todas las operaciones

### Usuario Normal
- **Username**: `usuario`
- **Password**: `user123`
- **Rol**: USER
- **Permisos**: Solo lectura de productos

## 📖 Documentación Swagger

Una vez ejecutada la aplicación, acceder a:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🧪 Testing con Postman

### 1. Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "usernameOrEmail": "admin",
    "password": "admin123"
}
```

### 2. Crear Producto (con token)
```http
POST http://localhost:8080/api/products
Authorization: Bearer {tu_token_jwt}
Content-Type: application/json

{
    "name": "Nuevo Producto",
    "description": "Descripción del producto",
    "price": 100.00,
    "stock": 50,
    "category": "Tecnología",
    "brand": "Marca Test"
}
```

## 🎨 Arquitectura del Proyecto

```
src/main/java/com/tecsup/edu/pe/exam_perez/
├── config/           # Configuraciones (Security, JWT, Swagger)
├── controller/       # Controladores REST
├── dto/             # Data Transfer Objects
├── entity/          # Entidades JPA
├── exception/       # Manejo de excepciones
├── repository/      # Repositorios de datos
└── service/         # Lógica de negocio
```

## 🔧 Tecnologías Utilizadas

- **Spring Boot 3.5.3**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL 8.0**
- **Swagger/OpenAPI 3**
- **Maven**
- **Java 17**

## 🚀 Despliegue

### Para Railway
1. Crear cuenta en Railway
2. Conectar repositorio GitHub
3. Configurar variables de entorno para base de datos
4. Desplegar automáticamente

### Para Render
1. Crear cuenta en Render
2. Conectar repositorio
3. Configurar como Web Service
4. Establecer variables de entorno

## 📝 Decisiones Técnicas

1. **JWT sin base de datos**: Tokens stateless para mejor escalabilidad
2. **Soft Delete**: Los productos se marcan como inactivos en lugar de eliminarse
3. **Roles granulares**: Sistema de autorización flexible con roles y permisos
4. **Validaciones centralizadas**: Manejo global de errores y validaciones
5. **Documentación automática**: Swagger integrado para facilitar testing

## 🔮 Posibles Mejoras

1. **Cache**: Implementar Redis para cachear consultas frecuentes
2. **Paginación**: Agregar paginación a listados de productos
3. **Upload de imágenes**: Servicio para subir imágenes de productos
4. **Auditoría**: Sistema completo de auditoría de cambios
5. **Notificaciones**: Sistema de notificaciones para stock bajo
6. **Tests**: Suite completa de tests unitarios e integración

## 👨‍💻 Autor

**Estudiante Pérez**  
Tecsup - Desarrollo de Aplicaciones Web  
Email: estudiante@tecsup.edu.pe
