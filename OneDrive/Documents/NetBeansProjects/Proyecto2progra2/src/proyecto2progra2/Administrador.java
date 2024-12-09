/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2progra2;

/**
 *
 * @author josue
 */
import java.io.File;

public class Administrador {
    private File directorioUsuarios;

    public Administrador() {
        directorioUsuarios = new File("usuarios");
        if (!directorioUsuarios.exists()) {
            directorioUsuarios.mkdir();  
        }
    }

    // Obtener los usuarios desde las carpetas del directorio
    public String[] obtenerUsuarios() {
        File[] usuarios = directorioUsuarios.listFiles(File::isDirectory); 
        String[] nombresUsuarios = new String[usuarios.length];
        
        for (int i = 0; i < usuarios.length; i++) {
            nombresUsuarios[i] = usuarios[i].getName();
        }
        
        return nombresUsuarios;
    }

    // Obtener la ruta de la biblioteca musical de un usuario
    public File obtenerBibliotecaMusical(String usuario) {
        File usuarioDir = new File(directorioUsuarios, usuario);
        File musicaDir = new File(usuarioDir, "Música");
        return musicaDir.exists() ? musicaDir : null;
    }

    // Obtener la ruta de la biblioteca de juegos de un usuario
    public File obtenerBibliotecaJuegos(String usuario) {
        File usuarioDir = new File(directorioUsuarios, usuario);
        File juegosDir = new File(usuarioDir, "Juegos");
        return juegosDir.exists() ? juegosDir : null;
    }

    // Eliminar un usuario y sus archivos
    public boolean eliminarUsuario(String nombreUsuario) {
        File usuarioDir = new File(directorioUsuarios, nombreUsuario);
        if (usuarioDir.exists()) {
            eliminarRecursivo(usuarioDir); 
            return true;
        }
        return false;
    }

    // Eliminar archivos dentro de una carpeta de forma recursiva
    private void eliminarRecursivo(File archivo) {
        if (archivo.isDirectory()) {
            File[] hijos = archivo.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    eliminarRecursivo(hijo);  // Recursión
                }
            }
        }
        archivo.delete();
    }

    // Eliminar archivos de la biblioteca (musical o de juegos)
    public boolean eliminarArchivo(String usuario, String tipoArchivo) {
        File biblioteca = tipoArchivo.equals("Música") ? obtenerBibliotecaMusical(usuario) : obtenerBibliotecaJuegos(usuario);
        
        if (biblioteca != null && biblioteca.exists()) {
            for (File archivo : biblioteca.listFiles()) {
                archivo.delete();  // Eliminar los archivos dentro de la carpeta
            }
            return true;
        }
        return false;
    }
}

