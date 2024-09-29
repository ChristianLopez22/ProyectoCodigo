import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;  // Importar la excepción CsvValidationException
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Prestado {
    private int fecha;
    private String usuario;
    private String producto;

    public Prestado(int fecha, String usuario, String producto) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
    }
    public int getFecha() {
        return fecha;
    }
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getProducto() {
        return producto;
    }
    public void setProducto(String producto) {
        this.producto = producto;
    }
    // Método para cargar los préstamos desde un archivo CSV
    public static List<Prestado> cargarPrestamosDesdeCSV(String archivo) {
        List<Prestado> prestamos = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) {
            System.out.println("El archivo " + archivo + " no existe. Por favor, cree el archivo primero.");
            return prestamos; 
        }

        try (CSVReader reader = new CSVReader(new FileReader(archivo))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                Prestado prestamo = new Prestado(Integer.parseInt(linea[0]), linea[1], linea[2]);
                prestamos.add(prestamo);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); 
        }
        return prestamos;
    }
    // Método para escribir los préstamos en un archivo CSV
    public static void escribirPrestamosEnCSV(String archivo, List<Prestado> prestamos) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo))) {
            for (Prestado prestamo : prestamos) {
                String[] datos = {
                        String.valueOf(prestamo.getFecha()),
                        prestamo.getUsuario(),
                        prestamo.getProducto()
                };
                writer.writeNext(datos);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}