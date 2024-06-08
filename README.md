<h1> # LibreriaAlura-java </h1>

Challenge desarrollado durante el curso de la formación Java y Spring Boot G6 - ONE de Alura

Librería Alura es una aplicación de consola para gestionar libros y autores, integrándose con la API de Gutendex para la búsqueda de libros. 
Esta aplicación permite registrar, listar, y analizar estadísticas de libros y autores en una base de datos.

![pantallaPpal](https://github.com/java82004/LibreriaAlura-java/assets/156710851/a303bb40-6aca-494f-97a0-bf6c0a5b2ab3)


## Objetivos del proyecto

* Aplicar conceptos avanzados de Java y Spring;
* Utilizar Spring Data JPA para persistir datos en la base de datos;
* Conocer varios tipos de bases de datos y utilizar PostgreSQL;
* Trabajar con varios tipos de consultas a la base de datos;
* Profundizar en la interfaz JPA Repository.

## Descripción del proyecto

* La API Gutendex es un catálogo de información de más de 70.000 libros presentes en Project Gutenberg (biblioteca en línea y gratuita).
* Enlace de API: https://gutendex.com/
* Repositorio de API*: https://github.com/garethbjohnson/gutendex

## Caracteristicas

1.	**Buscar libro por título:** Permite buscar libros en la web mediante el nombre y registrarlos en la base de datos si no están registrados.
2.	**Listar libros registrados:** Muestra todos los libros registrados en la base de datos.
3.	**Listar autores registrados:** Muestra todos los autores registrados en la base de datos.
4.	**Listar autores vivos en un determinado año:** Permite buscar y listar autores que estaban vivos en un año específico.
5.	**Listar libros por idioma:** Muestra libros registrados en la base de datos filtrados por el idioma especificado.
6.	**Top 10 Libros más descargados:** Muestra los 10 libros más descargados registrados en la base de datos.
7.	**Buscar libro por nombre de autor:** Permite buscar y listar libros por el nombre del autor.
8.	**Generar estadísticas de libros descargados:** Calcula y muestra estadísticas sobre el número de descargas de los libros registrados.
9.	**Top 10 libros por género literario:** Muestra los 10 libros más populares de un género específico consultando la API de Gutendex.
	
## Requisitos

•  Java 17
•  Maven
•  Base de datos PostgreSQL

## Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/libreria-alura.git
cd libreria-alura
```

## Configuración de la base de datos

1.	Crear una base de datos en PostgreSQL.
2.	Configurar las credenciales de la base de datos en src/main/resources/application.properties.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/libreria_alura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```
## Uso

Al ejecutar la aplicación, se muestra un menú interactivo en la consola donde el usuario puede seleccionar distintas opciones:

![Menú](https://github.com/java82004/LibreriaAlura-java/assets/156710851/4c9b60e0-3d81-404f-ac4a-4ab95437c76d)


Seleccione la opción deseada ingresando el número correspondiente y siga las instrucciones en pantalla.

## Estructura del Proyecto

•	principal/Principal.java: Clase principal que maneja la interacción con el usuario.      
•	model/Autor.java: Clase modelo para los autores.    
•	model/Libro.java: Clase modelo para los libros.      
•	repository/AutorRepository.java: Repositorio para operaciones CRUD con autores.    
•	repository/LibroRepository.java: Repositorio para operaciones CRUD con libros.    
•	service/ConsumoAPI.java: Servicio para consumir la API de Gutendex.    
•	service/ConvierteDatos.java: Servicio para convertir datos JSON a objetos Java.    


## Contribuir

¡Las contribuciones son bienvenidas! Si deseas contribuir, por favor sigue estos pasos:

1.	Haz un fork del repositorio.
2.	Crea una nueva rama (git checkout -b feature-nueva-funcionalidad).
3.	Realiza tus cambios y commitea (git commit -am 'Agregar nueva funcionalidad').
4.	Haz push a la rama (git push origin feature-nueva-funcionalidad).
5.	Abre un Pull Request para revisión.

¡Gracias por tomarte el tiempo de revisar Librería Alura!


Asegúrate de reemplazar `"tu-usuario"` y `"tu-contrasena"` con tus propios datos y credenciales. Además, puedes actualizar cualquier otra información que sea específica para tu proyecto.

