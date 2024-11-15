import java.util.List;

public class SolicitarMateriales {
    private String productoDeInteres;
    private String nombreDelInteresado;
    private int contacto;

    public SolicitarMateriales(String producto, String nombre, int contacto) {
        // Validación defensiva en el constructor
        if (producto == null || producto.isEmpty()) {
            throw new IllegalArgumentException("El producto de interés no puede estar vacío.");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del interesado no puede estar vacío.");
        }
        if (contacto <= 0) {
            throw new IllegalArgumentException("El número de contacto debe ser un número positivo.");
        }

        this.productoDeInteres = producto;
        this.nombreDelInteresado = nombre;
        this.contacto = contacto;
    }

    // Getters y setters con validaciones
    public String getProductoDeInteres() {
        return productoDeInteres;
    }

    public void setProductoDeInteres(String producto) {
        if (producto == null || producto.isEmpty()) {
            throw new IllegalArgumentException("El producto de interés no puede estar vacío.");
        }
        this.productoDeInteres = producto;
    }

    public String getNombreDelInteresado() {
        return nombreDelInteresado;
    }

    public void setNombreDelInteresado(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del interesado no puede estar vacío.");
        }
        this.nombreDelInteresado = nombre;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        if (contacto <= 0) {
            throw new IllegalArgumentException("El número de contacto debe ser un número positivo.");
        }
        this.contacto = contacto;
    }

    // Método para realizar una solicitud
    public void realizarSolicitud(BaseDeDatos bd, String archivoSolicitudes) {
        if (bd == null) {
            System.out.println("Error: La base de datos no puede ser nula.");
            return;
        }
        if (archivoSolicitudes == null || archivoSolicitudes.isEmpty()) {
            System.out.println("Error: El archivo de solicitudes no puede estar vacío.");
            return;
        }

        // Crear la solicitud solo si los datos son válidos
        String[] solicitud = { productoDeInteres, nombreDelInteresado, String.valueOf(contacto) };
        bd.agregarLinea(archivoSolicitudes, solicitud);
        System.out.println("Solicitud registrada correctamente.");
    }

    // Método para ver solicitudes
    public static void verSolicitudes(BaseDeDatos bd, String archivoSolicitudes) {
        if (bd == null) {
            System.out.println("Error: La base de datos no puede ser nula.");
            return;
        }
        if (archivoSolicitudes == null || archivoSolicitudes.isEmpty()) {
            System.out.println("Error: El archivo de solicitudes no puede estar vacío.");
            return;
        }

        List<String[]> datos = bd.obtenerDatos(archivoSolicitudes);
        if (datos == null || datos.isEmpty()) {
            System.out.println("No hay solicitudes registradas en el archivo.");
            return;
        }

        System.out.println("\n--- Solicitudes de Materiales ---");
        for (String[] linea : datos) {
            // Validación defensiva para cada línea de datos
            if (linea.length < 3) {
                System.out.println("Solicitud incompleta encontrada. Saltando...");
                continue;
            }
            System.out.println("Producto de interés: " + (linea[0] != null ? linea[0] : "Desconocido"));
            System.out.println("Nombre del interesado: " + (linea[1] != null ? linea[1] : "Desconocido"));
            System.out.println("Contacto: " + (linea[2] != null ? linea[2] : "Desconocido"));
            System.out.println("-----------------------------");
        }
    }
}
