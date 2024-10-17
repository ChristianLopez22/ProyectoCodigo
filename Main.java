import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Archivos CSV de prueba
        String archivoInventario = "inventario.csv";
        String archivoPrestamos = "prestamos.csv";
        String archivoUsuarios = "usuarios.csv";  

        // Inicializar Scanner
        Scanner scanner = new Scanner(System.in);

        // Menú principal
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Ver disponibilidad de materiales");
            System.out.println("2. Registrar un nuevo préstamo");
            System.out.println("3. Registrar un nuevo usuario");
        
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 1:
                    // Ver disponibilidad de materiales
                    System.out.println("\n--- Ver Disponibilidad de Materiales ---");
                    List<Inventarios> inventarios = Inventarios.cargarInventarioDesdeCSV(archivoInventario);
                    if (inventarios.isEmpty()) {
                        System.out.println("No hay inventarios cargados.");
                    } else {
                        Inventarios.verDisponibilidad(inventarios);
                    }
                    break;

                case 2:
                    // Registrar un nuevo préstamo
                    System.out.println("\n--- Registrar un Nuevo Préstamo ---");
                    System.out.print("Ingrese la fecha del préstamo (formato: YYYYMMDD): ");
                    int fecha = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea

                    System.out.print("Ingrese el nombre del usuario: ");
                    String usuario = scanner.nextLine();

                    System.out.print("Ingrese el nombre del producto prestado: ");
                    String producto = scanner.nextLine();

                    Prestado nuevoPrestamo = new Prestado(fecha, usuario, producto);
                    nuevoPrestamo.registrarPrestamo(archivoPrestamos);
                    break;

                case 3:
                    // Registrar un nuevo usuario
                    System.out.println("\n--- Registrar un Nuevo Usuario ---");
                    System.out.print("Ingrese el carnet del usuario: ");
                    int carne = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea después de nextInt()

                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Ingrese el correo electrónico del usuario: ");
                    String correoElectronico = scanner.nextLine();

                    System.out.print("Ingrese la contraseña del usuario: ");
                    String contrasena = scanner.nextLine();

                    Usuario nuevoUsuario = new Usuario(carne, nombre, correoElectronico, contrasena);
                    nuevoUsuario.registrarUsuario(archivoUsuarios);
                    break;

                

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }

        // Cerrar el Scanner
        scanner.close();
    }
}
