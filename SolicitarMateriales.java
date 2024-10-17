import java.io.*;
import java.util.*;

public class SolicitarMateriales {
    private String productodeinteres;
    private String nombredelinteresado;
    private int contacto;

    // Constructor
    public SolicitarMateriales(String producto, String nombre, int contacto) {
        this.productodeinteres = producto;
        this.nombredelinteresado = nombre;
        this.contacto = contacto;
    }

    // Métodos getters y setters
    public String getProductoDeInteres() {
        return productodeinteres;
    }

    public void setProductoDeInteres(String producto) {
        this.productodeinteres = producto;
    }

    public String getNombreDelInteresado() {
        return nombredelinteresado;
    }

    public void setNombreDelInteresado(String nombre) {
        this.nombredelinteresado = nombre;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    // Método para realizar una solicitud
    public void realizarSolicitud(String archivoSolicitudes) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivoSolicitudes, true))) {
            String[] solicitud = {productodeinteres, nombredelinteresado, String.valueOf(contacto)};
            writer.writeNext(solicitud);
            System.out.println("Solicitud registrada exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al registrar la solicitud: " + e.getMessage());
        }
    }

    // Método para ver las solicitudes
    public static void verSolicitudes(String archivoSolicitudes) {
        try (CSVReader reader = new CSVReader(new FileReader(archivoSolicitudes))) {
            String[] siguienteLinea;
            System.out.println("\n--- Solicitudes de Materiales ---");
            while ((siguienteLinea = reader.readNext()) != null) {
                System.out.println("Producto de interés: " + siguienteLinea[0]);
                System.out.println("Nombre del interesado: " + siguienteLinea[1]);
                System.out.println("Contacto: " + siguienteLinea[2]);
                System.out.println("-----------------------------");
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al leer las solicitudes: " + e.getMessage());
        }
    }
}
