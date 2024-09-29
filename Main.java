import java.util.List;

public class Main {
    public static void main(String[] args) {
        String archivoInventario = "inventario.csv";
        String archivoPrestamos = "prestamos.csv";

        System.out.println("Cargando inventarios desde el archivo CSV...");
        List<Inventarios> inventarios = Inventarios.cargarInventarioDesdeCSV(archivoInventario);

        if (inventarios.isEmpty()) {
            System.out.println("No hay inventarios cargados.");
        } else {
            Inventarios.verDisponibilidad(inventarios);
        }

        System.out.println("Guardando inventarios en el archivo CSV...");
        Inventarios.escribirInventarioEnCSV(archivoInventario, inventarios);
    }
}