import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeDatos {
    private Map<String, List<String[]>> datosEnMemoria;

    public BaseDeDatos() {
        datosEnMemoria = new HashMap<>();
    }

    // Cargar un archivo CSV en memoria
    public void cargarCSV(String archivo) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        
        List<String[]> contenido = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");
                contenido.add(campos);
            }
            datosEnMemoria.put(archivo, contenido);
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo " + archivo + ": " + e.getMessage());
        }
    }

    // Guardar un archivo CSV desde memoria
    public void guardarCSV(String archivo) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }

        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido == null || contenido.isEmpty()) {
            System.out.println("No hay datos en memoria para guardar en el archivo " + archivo);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String[] linea : contenido) {
                bw.write(String.join(",", linea));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo " + archivo + ": " + e.getMessage());
        }
    }

    // Obtener datos de un archivo CSV en memoria
    public List<String[]> obtenerDatos(String archivo) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return new ArrayList<>();
        }
        return datosEnMemoria.getOrDefault(archivo, new ArrayList<>());
    }

    // Agregar una nueva línea al archivo CSV en memoria
    public void agregarLinea(String archivo, String[] datos) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        
        if (datos == null || datos.length == 0) {
            System.out.println("Error: los datos no pueden ser nulos o vacíos.");
            return;
        }

        datosEnMemoria.computeIfAbsent(archivo, k -> new ArrayList<>()).add(datos);
    }

    // Actualizar los datos de un archivo en memoria
    public void actualizarDatos(String archivo, List<String[]> nuevosDatos) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        
        if (nuevosDatos == null) {
            System.out.println("Error: los nuevos datos no pueden ser nulos.");
            return;
        }

        datosEnMemoria.put(archivo, nuevosDatos);
    }

    // Modificar una línea específica según una condición
    public void modificarLinea(String archivo, int columna, String valor, String[] nuevosDatos) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        
        if (valor == null) {
            System.out.println("Error: el valor de búsqueda no puede ser nulo.");
            return;
        }
        
        if (nuevosDatos == null || nuevosDatos.length == 0) {
            System.out.println("Error: los nuevos datos no pueden ser nulos o vacíos.");
            return;
        }

        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido == null) {
            System.out.println("Error: no hay contenido en memoria para el archivo " + archivo);
            return;
        }

        if (columna < 0) {
            System.out.println("Error: el índice de la columna no puede ser negativo.");
            return;
        }

        for (int i = 0; i < contenido.size(); i++) {
            String[] linea = contenido.get(i);
            if (columna < linea.length && linea[columna].equals(valor)) {
                contenido.set(i, nuevosDatos);
                return;
            }
        }

        System.out.println("No se encontró ninguna línea que cumpla con la condición en el archivo " + archivo);
    }

    // Eliminar una línea específica según una condición
    public void eliminarLinea(String archivo, int columna, String valor) {
        if (archivo == null || archivo.isEmpty()) {
            System.out.println("Error: el nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        
        if (valor == null) {
            System.out.println("Error: el valor de búsqueda no puede ser nulo.");
            return;
        }

        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido == null) {
            System.out.println("Error: no hay contenido en memoria para el archivo " + archivo);
            return;
        }

        if (columna < 0) {
            System.out.println("Error: el índice de la columna no puede ser negativo.");
            return;
        }

        boolean eliminado = contenido.removeIf(linea -> columna < linea.length && linea[columna].equals(valor));

        if (!eliminado) {
            System.out.println("No se encontró ninguna línea que cumpla con la condición en el archivo " + archivo);
        }
    }
}
