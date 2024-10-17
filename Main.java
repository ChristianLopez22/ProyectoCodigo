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
            System.out.println("4. Modificar usuario existente");
            System.out.println("5. Eliminar un usuario existente");
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

                case 4:
                    // Modificar un usuario existente
                    System.out.println("\n--- Modificar un Usuario Existente ---");
                    int carneBuscado = -1;
                    boolean entradaValida = false;

                    while (!entradaValida) {
                        try {
                            System.out.print("Ingrese el carnet del usuario que desea modificar: ");
                            String input = scanner.nextLine();
                            carneBuscado = Integer.parseInt(input);
                            entradaValida = true;  // Si el parse es exitoso, la entrada es válida
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada no válida. Por favor ingrese un número entero para el carnet.");
                        }
                    }

                    System.out.print("Ingrese el nuevo nombre del usuario: ");
                    String nuevoNombre = scanner.nextLine();

                    System.out.print("Ingrese el nuevo correo electrónico del usuario: ");
                    String nuevoCorreo = scanner.nextLine();

                    System.out.print("Ingrese la nueva contraseña del usuario: ");
                    String nuevaContrasena = scanner.nextLine();

                    Usuario.modificarUsuario(archivoUsuarios, carneBuscado, nuevoNombre, nuevoCorreo, nuevaContrasena);
                    break;
                case 5:
                    // Eliminar un usuario existente
                    System.out.println("\n--- Eliminar un Usuario ---");
                    int carneEliminar = -1;
                    boolean entradaEliminarValida = false;

                    while (!entradaEliminarValida) {
                        try {
                            System.out.print("Ingrese el carnet del usuario que desea eliminar: ");
                            String input = scanner.nextLine();
                            carneEliminar = Integer.parseInt(input);
                            entradaEliminarValida = true;  // Si el parse es exitoso, la entrada es válida
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada no válida. Por favor ingrese un número entero para el carnet.");
                        }
                    }

                    Usuario.eliminarUsuario(archivoUsuarios, carneEliminar);
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
