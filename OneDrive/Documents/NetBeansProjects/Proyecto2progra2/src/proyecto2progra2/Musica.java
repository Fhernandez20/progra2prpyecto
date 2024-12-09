package proyecto2progra2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Musica {

    private String titulo;
    private String artista;
    private String album;
    private int duracion; // en segundos
    private String rutaArchivo;

    // Constructor
    public Musica(String titulo, String artista, String album, int duracion, String rutaArchivo) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
        this.rutaArchivo = rutaArchivo;
    }

    // Métodos para leer y escribir los detalles de la canción
    public void escribir(RandomAccessFile archivo) throws IOException {
        archivo.writeUTF(titulo);
        archivo.writeUTF(artista);
        archivo.writeUTF(album);
        archivo.writeInt(duracion);
        archivo.writeUTF(rutaArchivo);
    }

    public static Musica leer(RandomAccessFile archivo) throws IOException {
        String titulo = archivo.readUTF();
        String artista = archivo.readUTF();
        String album = archivo.readUTF();
        int duracion = archivo.readInt();
        String rutaArchivo = archivo.readUTF();
        return new Musica(titulo, artista, album, duracion, rutaArchivo);
    }

    // Método para agregar una nueva canción
    public static void agregarCancion(File mp3File, String titulo, String artista, String album, int duracion) {
        // Verificar que el archivo mp3 tiene una extensión válida
        if (!mp3File.getName().endsWith(".mp3")) {
            System.out.println("El archivo debe ser un archivo MP3.");
            return;
        }

        // Crear carpeta 'songs' si no existe
        File songDirectory = new File("songs");
        if (!songDirectory.exists()) {
            boolean directoryCreated = songDirectory.mkdirs();
            if (!directoryCreated) {
                System.out.println("No se pudo crear el directorio para canciones.");
                return;
            }
        }

        // Guardar el archivo MP3 en la carpeta "songs"
        File newMp3File = new File(songDirectory, mp3File.getName());
        try {
            Files.copy(mp3File.toPath(), newMp3File.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo MP3 guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo MP3.");
            e.printStackTrace();
            return;
        }

        // Crear un objeto Musica con los detalles ingresados
        Musica nuevaCancion = new Musica(titulo, artista, album, duracion, newMp3File.getAbsolutePath());

        // Guardar el objeto Musica en un archivo binario
        File songInfoFile = new File(songDirectory, mp3File.getName().replace(".mp3", ".ext"));
        try (RandomAccessFile archivoBinario = new RandomAccessFile(songInfoFile, "rw")) {
            nuevaCancion.escribir(archivoBinario); // Escribe los detalles de la canción en el archivo binario
            System.out.println("Detalles de la canción guardados exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los detalles de la canción.");
            e.printStackTrace();
        }
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }
}
