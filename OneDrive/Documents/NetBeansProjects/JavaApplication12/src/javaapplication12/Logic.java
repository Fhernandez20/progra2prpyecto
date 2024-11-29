/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication12;

/**
 *
 * @author josue
 */
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Logic {

    private File seleccionado;

    public void setSeleccionado(File seleccionado) {
        this.seleccionado = seleccionado;
    }

    public File getSeleccionado() {
        return seleccionado;
    }

    public File[] ordenarArchivosPorNombre() {
        File[] archivos = seleccionado.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos, Comparator.comparing(File::getName));
        }
        return archivos;
    }

    public File[] ordenarArchivosPorFecha() {
        File[] archivos = seleccionado.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));
        }
        return archivos;
    }

    public File[] organizarArchivosPorTipo() {
        File[] archivos = seleccionado.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos, Comparator.comparing(file -> getExtension(file.getName())));
        }
        return archivos;
    }

    public File[] ordenarArchivosPorTamano() {
        File[] archivos = seleccionado.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos, Comparator.comparingLong(File::length));
        }
        return archivos;
    }

    public void mostrarArchivosOrdenados(File[] archivos, String mensaje) {
        for (File archivo : archivos) {
            System.out.println(archivo.getName());
        }
    }

    public void crearCarpeta() {
    }

    public void renombrarArchivo() {
    }

    public void copiarArchivo() {
    }

    public void pegarArchivo() {
    }

    public void crearArchivoTexto() {
    }

    public void crearArchivoComercial() {
    }

    public void escribirEnArchivo() {
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index == -1) ? "" : fileName.substring(index + 1);
    }
}
