# Gestión de Productos — Evaluación Corte 2

Aplicación de escritorio para administrar el catálogo de productos de una papelería, desarrollada con **JavaFX** y **PostgreSQL**.

Permite registrar, consultar, actualizar y eliminar productos mediante una interfaz gráfica, con validaciones de datos y almacenamiento en una base de datos relacional.

## Funcionalidades

- Registro de productos con código, nombre, categoría, precio y stock.
- Consulta de productos en una tabla con contador de registros visibles.
- Búsqueda por coincidencias parciales en código o nombre, sin distinguir mayúsculas y minúsculas.
- Filtro por categoría: **Papelería**, **Tecnología** y **Accesorios**.
- Filtro para mostrar únicamente productos con stock disponible.
- Combinación de búsqueda y filtros.
- Actualización de productos seleccionados.
- Eliminación con confirmación previa.
- Limpieza del formulario y de los filtros.
- Mensajes de validación y manejo de errores de base de datos.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| JavaFX 21.0.6 | Interfaz gráfica |
| FXML | Definición de la vista |
| PostgreSQL | Base de datos |
| JDBC | Conexión y operaciones SQL |
| Maven | Gestión de dependencias y compilación |

## Estructura del repositorio

```text
Evaluacion-Corte2/
├── Evaluacion-Corte2/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── src/main/
│       ├── java/
│       │   ├── module-info.java
│       │   └── org/example/evaluacioncorte2/
│       │       ├── ProductoApplication.java
│       │       ├── connection/ConexionBD.java
│       │       ├── controller/ProductoController.java
│       │       ├── DAO/ProductoDAO.java
│       │       └── model/Producto.java
│       └── resources/org/example/evaluacioncorte2/
│           └── Producto-view.fxml
├── Evidencias Evaluacion Corte 2/
├── script_papeleria.sql
└── README.md
```

El proyecto separa sus responsabilidades en:

- **Modelo:** representa los datos del producto.
- **Vista:** define la interfaz mediante FXML.
- **Controlador:** gestiona las acciones del usuario y valida los datos.
- **DAO:** ejecuta las consultas y operaciones sobre PostgreSQL.
- **Conexión:** centraliza el acceso a la base de datos.

## Requisitos

- JDK 21.
- Maven instalado y disponible en la terminal.
- PostgreSQL instalado y en ejecución.
- Git para clonar el repositorio.
- pgAdmin o un cliente SQL para preparar la base de datos.

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/roberto7503/Evaluacion-Corte2.git
cd Evaluacion-Corte2
```

### 2. Preparar la base de datos

El archivo `script_papeleria.sql` contiene la creación de la base de datos, el esquema, la tabla y 12 productos de prueba.

Para ejecutarlo desde pgAdmin:

1. Conectarse al servidor PostgreSQL.
2. Ejecutar únicamente esta instrucción, fuera de una transacción:

   ```sql
   CREATE DATABASE papeleria_campus;
   ```

3. Abrir una nueva consulta conectada a `papeleria_campus`.
4. Ejecutar el resto del archivo, desde `CREATE SCHEMA` hasta la inserción de los productos.

> **Atención:** el script incluye `TRUNCATE TABLE registros.producto RESTART IDENTITY`, que elimina los productos existentes y reinicia sus identificadores antes de cargar los datos de prueba.

### 3. Configurar la conexión

Editar el archivo:

```text
Evaluacion-Corte2/src/main/java/org/example/evaluacioncorte2/connection/ConexionBD.java
```

Ajustar los valores según la instalación local:

```java
private static final String URL =
        "jdbc:postgresql://localhost:5432/papeleria_campus";
private static final String USER = "postgres";
private static final String PASSWORD = "TU_CONTRASEÑA";
```

### 4. Ajustar la clase de inicio

En `Evaluacion-Corte2/pom.xml`, dentro de la configuración de `javafx-maven-plugin`, reemplazar el contenido de `<mainClass>` por:

```xml
<mainClass>org.example.evaluacioncorte2/org.example.evaluacioncorte2.ProductoApplication</mainClass>
```

Este ajuste es necesario porque la configuración actual referencia una clase de otro proyecto.

### 5. Ejecutar la aplicación

Desde la raíz del repositorio, entrar a la carpeta que contiene `pom.xml`:

```bash
cd Evaluacion-Corte2
mvn clean javafx:run
```

También se puede importar esa carpeta como proyecto Maven en un IDE, configurar el JDK 21 y ejecutar `ProductoApplication`.

## Uso

### Registrar un producto

1. Presionar **Nuevo** para limpiar el formulario.
2. Ingresar código, nombre, categoría, precio y stock.
3. Presionar **Guardar**.

### Buscar productos

1. Escribir parte del código o nombre.
2. Seleccionar una categoría si se desea.
3. Activar el filtro de stock disponible si corresponde.
4. Presionar **Buscar**.

**Limpiar filtros** restablece la consulta completa. **Recargar** actualiza la tabla conservando los filtros seleccionados.

### Actualizar un producto

1. Seleccionar un producto de la tabla.
2. Modificar los datos del formulario.
3. Presionar **Actualizar**.

### Eliminar un producto

1. Seleccionar un producto de la tabla.
2. Presionar **Eliminar**.
3. Confirmar la operación.

## Validaciones

| Campo | Reglas |
|---|---|
| Código | Obligatorio, único y de 1 a 10 caracteres. Se eliminan los espacios de los extremos y se convierte a mayúsculas. |
| Nombre | Entre 3 y 80 caracteres después de eliminar espacios de los extremos. |
| Categoría | Papelería, Tecnología o Accesorios. |
| Precio | Entre `0.01` y `99999999.99`, con un máximo de 2 decimales. |
| Stock | Número entero entre `0` y `2147483647`. |

Para ingresar precios decimales, utilizar punto; por ejemplo, `72.50`.

La base de datos también aplica restricciones de integridad para evitar códigos duplicados y valores fuera de los rangos permitidos.

## Base de datos

- **Base de datos:** `papeleria_campus`
- **Esquema:** `registros`
- **Tabla:** `producto`

| Columna | Tipo | Descripción |
|---|---|---|
| `id_producto` | INTEGER IDENTITY | Identificador generado automáticamente |
| `codigo` | VARCHAR(10) | Código único |
| `nombre` | VARCHAR(80) | Nombre del producto |
| `categoria` | VARCHAR(20) | Categoría del catálogo |
| `precio` | NUMERIC(10,2) | Precio del producto |
| `stock` | INTEGER | Existencias disponibles |

## Evidencias

Las capturas de pantalla del proyecto se encuentran en la carpeta [Evidencias Evaluacion Corte 2](./Evidencias%20Evaluacion%20Corte%202).

## Solución de problemas

- **Error de conexión:** verificar que PostgreSQL esté activo y que la URL, el usuario y la contraseña sean correctos.
- **La tabla no existe:** comprobar que el esquema y la tabla se hayan creado dentro de `papeleria_campus`.
- **Código duplicado:** utilizar un código que no esté registrado.
- **No se encuentra la clase principal:** revisar el ajuste de `<mainClass>` descrito en la instalación.
- **Error de versión de Java:** comprobar que Maven y el IDE utilicen JDK 21.

## Autor

[roberto7503](https://github.com/roberto7503)

Proyecto académico correspondiente a la **Evaluación del Corte 2**.
