package proyecto2progra2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Usuario {

    private String username;
    private String password;
    private File carpetaUsuario;
    private boolean isAdmin; // Nuevo atributo para controlar si el usuario es admin


    // Constructor
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
        this.carpetaUsuario = new File("usuarios/" + username);
                this.isAdmin = false; // Por defecto no es admin

    }

    // Método para crear las carpetas del usuario y los archivos necesarios
    public boolean createArchives() throws IOException {
        File usersDir = new File("usuarios");

        // Si no existe la carpeta de usuarios, la crea
        if (!usersDir.exists()) {
            usersDir.mkdir();
        }

        // Verifica si la carpeta del usuario ya existe
        if (!carpetaUsuario.exists()) {
            if (carpetaUsuario.mkdir()) {
                System.out.println("Carpeta del usuario '" + username + "' creada.");

                // Crear archivo de contraseña
                File passwordFile = new File(carpetaUsuario, "password.txt");
                try (PrintWriter writer = new PrintWriter(new FileWriter(passwordFile))) {
                    writer.println(password);
                }

                // Crear carpetas de Música, Juegos y Chat dentro de la carpeta del usuario
                File carpetaMusica = new File(carpetaUsuario, "Música");
                File carpetaJuegos = new File(carpetaUsuario, "Juegos");
                File carpetaChat = new File(carpetaUsuario, "Chat");

                carpetaMusica.mkdir();
                carpetaJuegos.mkdir();
                carpetaChat.mkdir();

                System.out.println("Carpetas de Música, Juegos y Chat creadas.");

                return true;
            } else {
                System.out.println("Error al crear la carpeta del usuario.");
                return false;
            }
        } else {
            System.out.println("La carpeta del usuario '" + username + "' ya existe.");
            return false;
        }
    }

    // Verificar si la carpeta del usuario ya existe
    public boolean carpetaExistente() {
        return carpetaUsuario.exists();
    }

    // Validar contraseña del usuario
    public boolean validarContraseña() throws IOException {
        File passwordFile = new File(carpetaUsuario, "password.txt");
        if (passwordFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
                String storedPassword = reader.readLine();
                return storedPassword != null && storedPassword.equals(password);
            }
        }
        return false;
    }

    // Método para agregar una canción a la carpeta de música del usuario
    public void agregarCancion(Musica musica) throws IOException {
        File carpetaMusica = new File(carpetaUsuario, "Música");

        // Verificar si la carpeta de música existe, si no, crearla
        if (!carpetaMusica.exists()) {
            carpetaMusica.mkdir();
        }

        // Crear el archivo binario para la canción dentro de la carpeta de música
        File archivoCancion = new File(carpetaMusica, musica.getTitulo() + ".dat");

        try (RandomAccessFile archivo = new RandomAccessFile(archivoCancion, "rw")) {
            // Escribir la información de la canción en el archivo binario
            musica.escribir(archivo);
            System.out.println("Canción '" + musica.getTitulo() + "' agregada correctamente.");
        }
    }

    // Método para cargar canciones una por una desde los archivos binarios
    

    // Getter para la carpeta del usuario
    public File getCarpetaUsuario() {
        return carpetaUsuario;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Método para obtener si el usuario es admin
    public boolean isAdmin() {
        return isAdmin;
    }
    
}
