# 🧾 Sistema de Facturación y Control de Cobros

Este es un sistema empresarial desarrollado con **Java Spring Boot** que permite registrar trabajos o servicios realizados, gestionar el estado de cobros (pendiente, pagado, vencido), emitir reportes y facilitar el control administrativo y financiero de una empresa prestadora de servicios técnicos.

---

## 🚧 Estado del Proyecto

> 🔧 Actualmente el proyecto se encuentra en desarrollo, en la **Fase 1** de varias planeadas.

---

## 🧩 Fases del Proyecto

| Fase | Descripción | Estado |
|------|-------------|--------|
| 1️⃣ Fase 1 | Registro de clientes, trabajos y control del estado de cobro (pendiente, pagado, vencido) y Dashboard ejecutivo con métricas financieras, gráficas interactivas, filtros avanzados. | ✅ En desarrollo |
| 2️⃣ Fase 2 | Reportes básicos, alertas por trabajos vencidos, exportación a PDF/Excel. | 🕐 Próximamente |
| 3️⃣ Fase 3 | Generación de facturas (PDF/XML), integración con CFDI (México), módulo de pagos. | 🔒 Planeado |
| 4️⃣ Fase 4 | Seguridad con roles (Administrador, Capturista), autenticación y autorización. | 🔒 Planeado |

---

## ⚙️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Thymeleaf**
- **Flyway (Migraciones de BD)**
- **MySQL**
- **Lombok**
- **Maven**
- **IA**
---

## 🏗️ Arquitectura del proyecto

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** y aplica los **principios SOLID** para lograr una arquitectura limpia, escalable y mantenible.

```
com.vargas.facturacion
│
├── config         # Configuración general
├── controller     # Controladores MVC
├── dto            # Objetos de transferencia de datos
├── exception      # Manejo de errores personalizados
├── mapper         # Conversión Entity ↔ DTO
├── model
│   ├── entity     # Entidades JPA
│   └── enums      # Enumeraciones
├── repository     # Repositorios JPA
├── service
│   ├── impl       # Implementaciones de lógica de negocio
│   └── interfaces # Interfaces de servicios
└── resources
    ├── db/migration  # Archivos de migración Flyway
    ├── templates     # Vistas Thymeleaf
    └── application.properties
```

---

## 🧪 Cómo probar el proyecto localmente

### 1. Clonar el repositorio
```bash
git clone https://github.com/tuusuario/facturacion-app.git
cd facturacion-app
```

### 2. Configurar la base de datos MySQL

- Crear una base de datos llamada `facturacion_db`:

```sql
CREATE DATABASE facturacion_db;
```

- Verifica en `application.properties` que las credenciales sean correctas.

### 3. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

> ⚠️ Flyway ejecutará las migraciones automáticamente y cargará datos iniciales.

### 4. Abrir en navegador

```txt
http://localhost:8080/trabajos
```

---

## ✨ Funcionalidades disponibles en la Fase 1

✅ Registro de clientes  
✅ Registro de trabajos/servicios realizados  
✅ Control del estado de cobro (pendiente, pagado, vencido)  
✅ Migraciones con Flyway  
✅ Carga de datos iniciales  

---

## 🔐 Futuras funcionalidades

- Reportes de ingresos y deudas
- Seguridad con roles
- Integración con timbrado CFDI

---

## 📂 Migraciones con Flyway

Las migraciones están ubicadas en:
```
src/main/resources/db/migration
```

Ejemplos:
- `V1__init.sql`: crea las tablas y carga datos iniciales.
- `V2__add_notas_to_trabajos.sql`: agrega columna `notas` a la tabla `trabajos`.

---

## 📞 Contacto del desarrollador

Desarrollado por **Francisco Vargas**  
📧 Email: eleute_@hotmail.com 


---

## 📃 Licencia

Este proyecto es de código privado en desarrollo. Su uso o distribución queda sujeto a los términos que el desarrollador defina posteriormente.
=======

