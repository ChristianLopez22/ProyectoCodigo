import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
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
    private boolean regresado;

    public Prestado(int fecha, String usuario, String producto, boolean regresado) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
        this.regresado = regresado; 
    }

    //////Getters y setters
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

    public boolean isRegresado() { 
        return regresado;
    }

    public void setRegresado(boolean regresado) { 
        this.regresado = regresado;
    }

    //////Método para cargar los préstamos desde un archivo CSV
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
                boolean regresado = Boolean.parseBoolean(linea[3]); 
                Prestado prestamo = new Prestado(Integer.parseInt(linea[0]), linea[1], linea[2], regresado);
                prestamos.add(prestamo);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    //////Método para escribir los préstamos en un archivo CSV
    public static void escribirPrestamosEnCSV(String archivo, List<Prestado> prestamos) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo))) {
            for (Prestado prestamo : prestamos) {
                String[] datos = {
                        String.valueOf(prestamo.getFecha()),
                        prestamo.getUsuario(),
                        prestamo.getProducto(),
                        String.valueOf(prestamo.isRegresado())
                };
                writer.writeNext(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////Método para registrar un nuevo préstamo
    public void registrarPrestamo(String archivo) {

        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo, true))) {
            String[] datos = {
                    String.valueOf(this.fecha),
                    this.usuario,
                    this.producto, 
                    String.valueOf(this.regresado)
            };
            writer.writeNext(datos);
            System.out.println("Préstamo registrado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    ////Método para marcar regresado
    public static void marcarComoRegresado(List<Prestado> prestamos, String usuario, String producto, String archivo) {
        for (Prestado prestamo : prestamos) {
            if (prestamo.getUsuario().equals(usuario) && prestamo.getProducto().equals(producto)) {
                prestamo.setRegresado(true); 
                System.out.println("El préstamo del producto '" + producto + "' por el usuario '" + usuario + "' ha sido marcado como regresado.");
                break;  
            }
        }
        escribirPrestamosEnCSV(archivo, prestamos);
    }
}