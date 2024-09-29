import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Archivos CSV de prueba
        String archivoInventario = "inventario.csv";
        String archivoPrestamos = "prestamos.csv";

        // Cargar inventarios desde el archivo CSV
        System.out.println("Cargando inventarios desde el archivo CSV...");
        List<Inventarios> inventarios = Inventarios.cargarInventarioDesdeCSV(archivoInventario);

        // Si el inventario está vacío, indicamos que no hay materiales cargados
        if (inventarios.isEmpty()) {
            System.out.println("No hay inventarios cargados.");
        } else {
            // Verificar la disponibilidad de los materiales
            Inventarios.verDisponibilidad(inventarios);
        }

        // Registrar un nuevo préstamo
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la fecha del préstamo (formato: YYYYMMDD):");
        int fecha = scanner.nextInt();
        scanner.nextLine();  // Consumir la nueva línea después de nextInt()

        System.out.println("Ingrese el nombre del usuario:");
        String usuario = scanner.nextLine();

        System.out.println("Ingrese el nombre del producto prestado:");
        String producto = scanner.nextLine();

        // Crear el nuevo préstamo y registrarlo
        Prestado nuevoPrestamo = new Prestado(fecha, usuario, producto);
        nuevoPrestamo.registrarPrestamo(archivoPrestamos);

        // Guardar inventarios en el archivo CSV (para probar la escritura)
        System.out.println("Guardando inventarios en el archivo CSV...");
        Inventarios.escribirInventarioEnCSV(archivoInventario, inventarios);

        scanner.close();
    }
}