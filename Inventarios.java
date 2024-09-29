import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;  // Importar la excepción CsvValidationException
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventarios {
    private int cantidad;
    private String material;

    public Inventarios(int cantidad, String material) {
        this.cantidad = cantidad;
        this.material = material;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    // Método para cargar inventarios desde un archivo CSV
    public static List<Inventarios> cargarInventarioDesdeCSV(String archivo) {
        List<Inventarios> inventarios = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) {
            System.out.println("El archivo " + archivo + " no existe. Por favor, cree el archivo primero.");
            return inventarios; 
        }

        try (CSVReader reader = new CSVReader(new FileReader(archivo))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                Inventarios inventario = new Inventarios(Integer.parseInt(linea[0]), linea[1]);
                inventarios.add(inventario);
            }
        } catch (IOException | CsvValidationException e) {  
            e.printStackTrace(); 
        }
        return inventarios;
    }

    // Método para escribir el inventario en un archivo CSV
    public static void escribirInventarioEnCSV(String archivo, List<Inventarios> inventarios) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo))) {
            for (Inventarios inventario : inventarios) {
                String[] datos = {
                        String.valueOf(inventario.getCantidad()),
                        inventario.getMaterial()
                };
                writer.writeNext(datos);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}