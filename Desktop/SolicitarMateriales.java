
import java.util.List;

public class SolicitarMateriales {
    private String productoDeInteres;
    private String nombreDelInteresado;
    private int contacto;

    public SolicitarMateriales(String producto, String nombre, int contacto) {
        this.productoDeInteres = producto;
        this.nombreDelInteresado = nombre;
        this.contacto = contacto;
    }

    // Getters y setters
    public String getProductoDeInteres() { return productoDeInteres; }
    public void setProductoDeInteres(String producto) { this.productoDeInteres = producto; }
    public String getNombreDelInteresado() { return nombreDelInteresado; }
    public void setNombreDelInteresado(String nombre) { this.nombreDelInteresado = nombre; }
    public int getContacto() { return contacto; }
    public void setContacto(int contacto) { this.contacto = contacto; }

    // M3todo para realizar una solicitud
    public void realizarSolicitud(BaseDeDatos bd, String archivoSolicitudes) {
        String[] solicitud = { productoDeInteres, nombreDelInteresado, String.valueOf(contacto) };
        bd.agregarLinea(archivoSolicitudes, solicitud);
        System.out.println("Solicitud registrada correctamente.");
    }

    // Metodo para ver solicitudes
    public static void verSolicitudes(BaseDeDatos bd, String archivoSolicitudes) {
        List<String[]> datos = bd.obtenerDatos(archivoSolicitudes);
        System.out.println("\n--- Solicitudes de Materiales ---");
        for (String[] linea : datos) {
            System.out.println("Producto de interés: " + linea[0]);
            System.out.println("Nombre del interesado: " + linea[1]);
            System.out.println("Contacto: " + linea[2]);
            System.out.println("-----------------------------");
        }
    }
}
