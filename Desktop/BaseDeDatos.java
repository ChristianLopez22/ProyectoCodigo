
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
        List<String[]> contenido = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(","); // Separador por comas, ajustar según formato
                contenido.add(campos);
            }
            datosEnMemoria.put(archivo, contenido);
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo " + archivo + ": " + e.getMessage());
        }
    }

    // Guardar un archivo CSV desde memoria
    public void guardarCSV(String archivo) {
        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                for (String[] linea : contenido) {
                    bw.write(String.join(",", linea)); // Unir campos con comas
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo " + archivo + ": " + e.getMessage());
            }
        } else {
            System.out.println("No se ha cargado ningún contenido en memoria para el archivo " + archivo);
        }
    }

    // Obtener datos de un archivo CSV en memoria
    public List<String[]> obtenerDatos(String archivo) {
        return datosEnMemoria.getOrDefault(archivo, new ArrayList<>());
    }

    // Agregar una nueva línea al archivo CSV en memoria
    public void agregarLinea(String archivo, String[] datos) {
        datosEnMemoria.computeIfAbsent(archivo, k -> new ArrayList<>()).add(datos);
    }

    // Método para actualizar los datos de un archivo en memoria
    public void actualizarDatos(String archivo, List<String[]> nuevosDatos) {
        datosEnMemoria.put(archivo, nuevosDatos);
    }

    // Ejemplo de modificacion de una línea especifica segun una condición 
    public void modificarLinea(String archivo, int columna, String valor, String[] nuevosDatos) {
        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido != null) {
            for (int i = 0; i < contenido.size(); i++) {
                if (contenido.get(i)[columna].equals(valor)) {
                    contenido.set(i, nuevosDatos);
                    break;
                }
            }
        }
    }

    // Ejemplo de eliminación de una línea específica según una condición
    public void eliminarLinea(String archivo, int columna, String valor) {
        List<String[]> contenido = datosEnMemoria.get(archivo);
        if (contenido != null) {
            contenido.removeIf(linea -> linea[columna].equals(valor));
        }
    }
}
