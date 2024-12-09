package proyecto2progra2;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Juego {

    private String nombre;
    private String genero;
    private String desarrollador;
    private String fechaLanzamiento;
    private String rutaImagen; // Ruta de la imagen del juego

    // Constructor
    public Juego(String nombre, String genero, String desarrollador, String fechaLanzamiento, String rutaImagen) {
        this.nombre = nombre;
        this.genero = genero;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.rutaImagen = rutaImagen;
    }

    // Métodos para leer y escribir los detalles del juego en archivos binarios
    public void escribir(RandomAccessFile archivo) throws IOException {
        archivo.writeUTF(nombre);
        archivo.writeUTF(genero);
        archivo.writeUTF(desarrollador);
        archivo.writeUTF(fechaLanzamiento);
        archivo.writeUTF(rutaImagen); // Guardar la ruta de la imagen del juego
    }

    public static Juego leer(RandomAccessFile archivo) throws IOException {
        String nombre = archivo.readUTF();
        String genero = archivo.readUTF();
        String desarrollador = archivo.readUTF();
        String fechaLanzamiento = archivo.readUTF();
        String rutaImagen = archivo.readUTF(); // Leer la ruta de la imagen del juego
        return new Juego(nombre, genero, desarrollador, fechaLanzamiento, rutaImagen);
    }

    // Método para agregar un nuevo juego
    public static void agregarJuego(File imagenJuego, String nombre, String genero, String desarrollador, String fechaLanzamiento) {
        // Verificar que el archivo es una imagen válida
        if (!(imagenJuego.getName().endsWith(".png") || imagenJuego.getName().endsWith(".jpeg") || imagenJuego.getName().endsWith(".jpg"))) {
            System.out.println("El archivo debe ser una imagen (.png, .jpeg, .jpg).");
            return;
        }

        // Crear carpeta 'games' si no existe
        File gameDirectory = new File("games");
        if (!gameDirectory.exists()) {
            boolean directoryCreated = gameDirectory.mkdirs();
            if (!directoryCreated) {
                System.out.println("No se pudo crear el directorio para juegos.");
                return;
            }
        }

        // Guardar la imagen del juego en la carpeta "games"
        File newGameImage = new File(gameDirectory, imagenJuego.getName());
        try {
            Files.copy(imagenJuego.toPath(), newGameImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Imagen del juego guardada exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar la imagen del juego.");
            e.printStackTrace();
            return;
        }

        // Crear un objeto Juego con los detalles ingresados
        Juego nuevoJuego = new Juego(nombre, genero, desarrollador, fechaLanzamiento, newGameImage.getAbsolutePath());

        // Guardar el objeto Juego en un archivo binario
        File gameInfoFile = new File(gameDirectory, imagenJuego.getName().replace(".png", ".ext").replace(".jpeg", ".ext").replace(".jpg", ".ext"));
        try (RandomAccessFile archivoBinario = new RandomAccessFile(gameInfoFile, "rw")) {
            nuevoJuego.escribir(archivoBinario); // Escribe los detalles del juego en el archivo binario
            System.out.println("Detalles del juego guardados exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los detalles del juego.");
            e.printStackTrace();
        }
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
}
