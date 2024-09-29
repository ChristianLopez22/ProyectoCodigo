import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Archivos CSV de prueba
        String archivoInventario = "inventario.csv";
        String archivoPrestamos = "prestamos.csv";

        System.out.println("Cargando inventarios desde el archivo CSV...");
        List<Inventarios> inventarios = Inventarios.cargarInventarioDesdeCSV(archivoInventario);

        if (inventarios.isEmpty()) {
            System.out.println("No hay inventarios cargados.");
        } else {
            System.out.println("Inventarios cargados:");
            for (Inventarios inventario : inventarios) {
                System.out.println("Material: " + inventario.getMaterial() + ", Cantidad: " + inventario.getCantidad());
            }
            Inventarios.verDisponibilidad(inventarios);
        }

        System.out.println("Guardando inventarios en el archivo CSV...");
        Inventarios.escribirInventarioEnCSV(archivoInventario, inventarios);

        System.out.println("Cargando préstamos desde el archivo CSV...");
        List<Prestado> prestamos = Prestado.cargarPrestamosDesdeCSV(archivoPrestamos);

        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
        } else {
            System.out.println("Préstamos cargados:");
            for (Prestado prestamo : prestamos) {
                System.out.println("Fecha: " + prestamo.getFecha() + ", Usuario: " + prestamo.getUsuario() + ", Producto: " + prestamo.getProducto());
            }
        }
        
        System.out.println("Guardando préstamos en el archivo CSV...");
        Prestado.escribirPrestamosEnCSV(archivoPrestamos, prestamos);
    }
}
