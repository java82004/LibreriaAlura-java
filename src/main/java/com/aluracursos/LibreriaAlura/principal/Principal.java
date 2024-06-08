package com.aluracursos.LibreriaAlura.principal;

import com.aluracursos.LibreriaAlura.model.*;
import com.aluracursos.LibreriaAlura.repository.AutorRepository;
import com.aluracursos.LibreriaAlura.repository.LibroRepository;
import com.aluracursos.LibreriaAlura.service.ConsumoAPI;
import com.aluracursos.LibreriaAlura.service.ConvierteDatos;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static String URL_BASE = "https://gutendex.com/books/";
    private Set<String> librosRegistrados = new HashSet<>();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConsumoAPI consulta = new ConsumoAPI();
    private int opcionUsuario = -1;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    List<Autor> autores;
    List<Libro> libros;
    Scanner teclado = new Scanner(System.in);

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 Libros mas descargados
                    7 - Buscar libro por nombre de autor
                    8 - Generando Estadisticas de libros descargados
                    9 - Top 10 libros por Género Literario
                    0 - Salir                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                      mostrarLibrosRegistrados();
                    break;
                case 3:
                      mostrarAutoresRegistrados();
                    break;
                case 4:
                      mostrarAutoresPorAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    top10librosMasDescargados();
                    break;
                case 7:
                    buscarLibrosPorNombreAutor();
                    break;
                case 8:
                    calcularEstadisticasDescargas();
                    break;
                case 9:
                    top10LibrosPorGenero();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    public void muestraMenuIdiomas(){
        System.out.println("""                
                Ingrese el idioma para buscar los libros:                
                en- ingles
                es- español
                fr- frances
                pt- portugués                
                """);
    }

    private void buscarLibroWeb(){
        System.out.print("Ingrese el nombre del libro que desea buscar: ");
        String libroUsuario = teclado.nextLine();

        String busqueda = "?search=" + libroUsuario.replace(" ","+");
        var json = consulta.obtenerDatos(URL_BASE + busqueda);
//        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Verificar si hay resultados
        if (datos.resultados().isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }

        DatosLibros datoslibro = datos.resultados().get(0);
        Libro libro = new Libro(datoslibro);

        // Verificar si el libro ya está registrado
        if (librosRegistrados.contains(libro.getTitulo())) {
            System.out.println("No se puede registrar el mismo libro más de una vez.");
            return;
        }

        Autor autor = new Autor().obtenerPrimerAutor(datoslibro);

        //System.out.println(libro);
        // Formatear la salida del libro con autor
        System.out.println("\n----- LIBRO -----");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + autor.getNombre());
        System.out.println("Idioma: " + libro.getIdiomas());
        System.out.println("Número de Descargas: " + libro.getNumeroDeDescargas());
        System.out.println("-----------------");

        // Registrar el libro
        guardarLibroConAutor(libro, autor);
        librosRegistrados.add(libro.getTitulo());
    }

    private void guardarLibroConAutor(Libro libro, Autor autor){
        //buscar autores en DB por nombre
        Optional<Autor> autorBuscado = autorRepository.findByNombreContains(autor.getNombre());

        //guarda el autor si no existe
        if(autorBuscado.isPresent()){
            System.out.println("El autor ya existe");
            libro.setAutor(autorBuscado.get());
        } else {
            System.out.println("Nuevo autor");
            autorRepository.save(autor);
            libro.setAutor(autor);
        }

        // Guardar el libro
        try {
            libroRepository.save(libro);
        } catch (Exception e) {
            // Manejar otras excepciones
            System.out.println("Ocurrió un error al guardar el libro: " + e.getMessage());
        }
    }


    // listar autores buscados
    private void mostrarLibrosRegistrados() {
        //Aqui se consulta nuestra base de datos postreSql
        libros = libroRepository.findAll();

        //imprima los libros
        imprimeLibrosOrdenadosPorNombre(libros);
    }


    private void mostrarAutoresRegistrados() {
        //Aqui se consulta nuestra base de datos postreSql
        autores = autorRepository.findAll();

        //imprimia las series guardadas en la lista autores
        imprimeAutoresOrdenadosPorNombre(autores);
    }

    //listar autores vivos en un determinado año
    private void mostrarAutoresPorAño(){
        System.out.print("Ingrese el año vivo de autor (es) que deseas buscar: ");
        Integer anio = Integer.valueOf(teclado.nextLine());

        autores = autorRepository
                .findByFechaDeNacimientoLessThanEqualAndFechaDeMuerteGreaterThanEqual
                        (anio, anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año");
        } else {
            imprimeAutoresOrdenadosPorNombre(autores);
        }
    }

    @Transactional
    private void imprimeAutoresOrdenadosPorNombre(List<Autor> autores){
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void imprimeLibrosOrdenadosPorNombre(List<Libro> libros) {

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    Autor autor = libro.getAutor(); // Asumiendo que cada libro tiene un autor asociado
                    System.out.println("\n----- LIBRO -----");
                    System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("Idioma: " + libro.getIdiomas());
                    System.out.println("Número de Descargas: " + libro.getNumeroDeDescargas());
                    System.out.println("-----------------");
                });
    }

    private void listarLibrosPorIdioma(){
        muestraMenuIdiomas();
        String idioma = teclado.nextLine();

        String claveIdioma;
        if (idioma.length() >= 2) {
            claveIdioma = idioma.substring(0, 2);
        } else {
            // Manejar el caso donde el usuario no ingrese suficientes caracteres
            claveIdioma = idioma; // o manejar el error como prefieras
        }

        libros = libroRepository.findByIdiomasContains(claveIdioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma");
        } else {
            imprimeLibrosOrdenadosPorNombre(libros);
        }
    }

    private void top10librosMasDescargados() {
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

        if (top10Libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados.");
        } else {
            imprimeLibrosOrdenadosPorNumeroDeDescargas(top10Libros);
        }
    }

    private void imprimeLibrosOrdenadosPorNumeroDeDescargas(List<Libro> libros) {
        libros.forEach(libro -> {
            Autor autor = libro.getAutor(); // Asumiendo que cada libro tiene un autor asociado
            System.out.println("\n----- LIBRO -----");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Idioma: " + libro.getIdiomas());
            System.out.println("Número de Descargas: " + libro.getNumeroDeDescargas());
            System.out.println("-----------------");
        });
    }

    private void buscarLibrosPorNombreAutor() {
        System.out.print("Ingrese el nombre del autor para buscar libros: ");
        String nombreAutor = teclado.nextLine();

        // Buscar libros por el nombre del autor en la base de datos
        libros = libroRepository.findByAutorNombreContainingIgnoreCase(nombreAutor);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros para el autor especificado.");
        } else {
            imprimeLibrosOrdenadosPorNombre(libros);
        }
    }

    public void calcularEstadisticasDescargas() {
        // Consultar la base de datos para obtener todas las descargas de libros
        List<Double> descargas = libroRepository.findAllDescargas();

        // Calcular las estadísticas de descargas
        DoubleSummaryStatistics est = descargas.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        // Mostrar las estadísticas de descargas
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad Máxima de descargas: " + est.getMax());
        System.out.println("Cantidad Mínima de descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadísticas: " + est.getCount());
    }

    private void top10LibrosPorGenero() {
        System.out.print("Ingrese el Genero o topico que desea buscar: ");
        String libroGenero = teclado.nextLine();

        String busqueda = "?topic=" + libroGenero.replace(" ","+");
        var json = consulta.obtenerDatos(URL_BASE + busqueda);
//        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Verificar si hay resultados
        if (datos.resultados().isEmpty()) {
            System.out.println("No se encontraron libros para el género especificado.");
            return;
        }

        // Tomar solo los primeros 10 resultados
        List<DatosLibros> top10Libros = datos.resultados().stream()
                .limit(10).
                collect(Collectors.toList());
        System.out.println("\n----- TOP 10 LIBROS: -----");
        top10Libros.forEach(datoslibro -> {
            String titulo = datoslibro.titulo();
            String autor = datoslibro.autor().isEmpty() ? "Autor desconocido" : datoslibro.autor().get(0).nombre();
            String idioma = datoslibro.idiomas().isEmpty() ? "Idioma desconocido" : datoslibro.idiomas().get(0);
            Double numeroDescargas = datoslibro.numeroDeDescargas();

            // Formatear la salida del libro con autor
            System.out.println("Título: " + titulo);
            System.out.println("Autor: " + autor);
            System.out.println("Idioma: " + idioma);
            System.out.println("Número de Descargas: " + numeroDescargas);
            System.out.println("-----------------");
        });
    }



}
