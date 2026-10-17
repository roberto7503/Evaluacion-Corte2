# Gestión de Productos — Evaluación Corte 2

Aplicación de escritorio para administrar el catálogo de productos de una papelería, desarrollada con **Java 21, JavaFX y PostgreSQL**.

Permite registrar, consultar, actualizar y eliminar productos mediante una interfaz gráfica, con validaciones de datos y almacenamiento persistente.

## Funcionalidades

- Registro de productos con código, nombre, categoría, precio y stock.
- Visualización de productos en una tabla con contador de registros.
- Búsqueda por coincidencias parciales en código o nombre, sin distinguir mayúsculas y minúsculas.
- Filtro por categoría: **Papelería**, **Tecnología** y **Accesorios**.
- Filtro **Solo con existencias**.
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
| PostgreSQL JDBC 42.7.13 | Conexión con la base de datos |
| Maven | Gestión de dependencias y compilación |

## Estructura del repositorio

```text
Evaluacion-Corte2/
├── .gitignore
├── README.md
├── script_papeleria.sql
├── Evidencias Evaluacion Corte 2/
└── Evaluacion-Corte2/
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    └── src/main/
        ├── java/
        │   ├── module-info.java
        │   └── org/example/evaluacioncorte2/
        │       ├── ProductoApplication.java
        │       ├── connection/ConexionBD.java
        │       ├── controller/ProductoController.java
        │       ├── DAO/ProductoDAO.java
        │       └── model/Producto.java
        └── resources/org/example/evaluacioncorte2/
            ├── Producto-view.fxml
            └── database.properties.example
```

El archivo `database.properties` se crea localmente a partir de la plantilla y queda excluido de Git.

## Organización del código

- **Modelo:** representa los datos de un producto.
- **Vista:** define la interfaz gráfica mediante FXML.
- **Controlador:** gestiona las acciones del usuario y valida los datos.
- **DAO:** realiza las consultas y operaciones SQL mediante sentencias preparadas.
- **Conexión:** carga la configuración local y establece la conexión con PostgreSQL.

## Requisitos

- JDK 21.
- Maven instalado y disponible en la terminal.
- PostgreSQL instalado y en ejecución.
- Git para clonar el repositorio.
- pgAdmin u otro cliente SQL para preparar la base de datos.

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/roberto7503/Evaluacion-Corte2.git
cd Evaluacion-Corte2
```

### 2. Preparar la base de datos

El archivo `script_papeleria.sql` contiene la creación de la base de datos, el esquema, la tabla y 12 productos de prueba.

Desde pgAdmin u otro cliente SQL:

1. Conectarse al servidor PostgreSQL.
2. Ejecutar esta instrucción fuera de una transacción:

   ```sql
   CREATE DATABASE papeleria_campus;
   ```

3. Abrir una nueva consulta conectada a `papeleria_campus`.
4. Ejecutar el resto del archivo `script_papeleria.sql`, desde `CREATE SCHEMA` hasta la inserción de los productos.

Si la base de datos ya existe, omitir su creación.

### 3. Configurar la conexión local

Abrir la carpeta:

```text
Evaluacion-Corte2/src/main/resources/org/example/evaluacioncorte2/
```

Copiar `database.properties.example` y nombrar la copia:

```text
database.properties
```

Conservar la plantilla original y editar únicamente la copia con las credenciales propias:

```properties
db.url=jdbc:postgresql://localhost:5432/papeleria_campus?currentSchema=registros
db.user=TU_USUARIO
db.password=TU_CONTRASEÑA
```

Si PostgreSQL utiliza otro servidor o puerto, ajustar también `db.url`.

No es necesario modificar `ConexionBD.java`. Guardar la configuración antes de compilar o iniciar la aplicación.

### 4. Ejecutar la aplicación

Desde la raíz del repositorio, entrar a la carpeta que contiene el `pom.xml`:

```bash
cd Evaluacion-Corte2
mvn clean javafx:run
```

También se puede importar esa carpeta como proyecto Maven en un IDE, seleccionar JDK 21 y ejecutar la clase `ProductoApplication`.

## Uso

### Registrar un producto

1. Presionar **Nuevo / Limpiar**.
2. Completar código, nombre, categoría, precio y stock.
3. Presionar **Guardar**.

El identificador se genera automáticamente.

### Buscar y filtrar

1. Escribir parte del código o nombre, si se desea.
2. Seleccionar una categoría o mantener **Todas**.
3. Marcar **Solo con existencias** para consultar productos con stock mayor que cero.
4. Presionar **Buscar**.

**Limpiar filtros** restablece la consulta completa. **Recargar** actualiza la tabla conservando los filtros actuales.

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

Los precios se muestran en **córdobas (C$)**. Para ingresar decimales, utilizar punto; por ejemplo, `72.50`.

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
| `categoria` | VARCHAR(20) | Categoría |
| `precio` | NUMERIC(10,2) | Precio del producto |
| `stock` | INTEGER | Existencias disponibles |

Los 12 registros iniciales incluyen productos de las tres categorías y ejemplos con stock igual a cero para comprobar los filtros.

## Configuración privada

El `.gitignore` de la raíz contiene:

```gitignore
**/target/
**/database.properties
```

Cada persona debe mantener sus credenciales en su propio `database.properties`. En el repositorio se conserva únicamente `database.properties.example`, con valores de ejemplo.

La carpeta `target/` se genera al compilar y queda excluida de Git.

## Evidencias

Las capturas de pantalla se encuentran en la carpeta [Evidencias Evaluacion Corte 2](./Evidencias%20Evaluacion%20Corte%202).

## Solución de problemas

- **Error de conexión:** verificar que PostgreSQL esté activo y revisar la URL, el usuario y la contraseña en `database.properties`.
- **No se encuentra la configuración:** comprobar que el archivo se llame exactamente `database.properties`, esté en la misma carpeta que la plantilla y volver a compilar.
- **La tabla no existe:** comprobar que el esquema `registros` y la tabla `producto` se hayan creado dentro de `papeleria_campus`.
- **Código duplicado:** utilizar un código que no esté registrado.
- **Precio inválido:** utilizar punto decimal y un máximo de dos decimales.
- **Error de versión de Java:** comprobar con `mvn -version` que Maven esté utilizando JDK 21.

## Autor

[roberto7503](https://github.com/roberto7503)

Proyecto académico correspondiente a la **Evaluación del Corte 2**.
