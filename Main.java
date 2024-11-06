import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Crear instancia de BaseDeDatos
        BaseDeDatos bd = new BaseDeDatos();

        // Cargar archivos CSV en memoria
        bd.cargarCSV("inventario.csv");
        bd.cargarCSV("usuarios.csv");
        bd.cargarCSV("prestamos.csv");
        bd.cargarCSV("solicitudes.csv");

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
            System.out.println("6. Solicitar materiales");
            System.out.println("7. Ver solicitudes de materiales");
            System.out.println("8. Marcar préstamo como regresado");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 1:
                    // Ver disponibilidad de materiales
                    System.out.println("\n--- Ver Disponibilidad de Materiales ---");
                    Inventarios.verDisponibilidad(bd, "inventario.csv");
                    break;

                case 2:
                    // Registrar un nuevo préstamo
                    System.out.println("\n--- Registrar un Nuevo Préstamo ---");
                    System.out.print("Ingrese la fecha del préstamo (formato: YYYYMMDD): ");
                    int fecha = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Ingrese el nombre del usuario: ");
                    String usuario = scanner.nextLine();

                    System.out.print("Ingrese el nombre del producto prestado: ");
                    String producto = scanner.nextLine();

                    Prestado nuevoPrestamo = new Prestado(fecha, usuario, producto, false);
                    List<Prestado> prestamos = Prestado.cargarPrestamosDesdeBase(bd, "prestamos.csv");
                    prestamos.add(nuevoPrestamo);
                    Prestado.guardarPrestamosEnBase(bd, "prestamos.csv", prestamos);
                    break;

                case 3:
                    // Registrar un nuevo usuario
                    System.out.println("\n--- Registrar un Nuevo Usuario ---");
                    System.out.print("Ingrese el carnet del usuario: ");
                    int carne = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Ingrese el correo electrónico del usuario: ");
                    String correoElectronico = scanner.nextLine();

                    System.out.print("Ingrese la contraseña del usuario: ");
                    String contrasena = scanner.nextLine();

                    Usuario nuevoUsuario = new Usuario(carne, nombre, correoElectronico, contrasena);
                    List<Usuario> usuarios = Usuario.cargarUsuariosDesdeBase(bd, "usuarios.csv");
                    usuarios.add(nuevoUsuario);
                    Usuario.guardarUsuariosEnBase(bd, "usuarios.csv", usuarios);
                    System.out.println("Usuario registrado exitosamente.");
                    break;

                case 4:
                    // Modificar un usuario existente
                    System.out.println("\n--- Modificar un Usuario Existente ---");
                    System.out.print("Ingrese el carnet del usuario que desea modificar: ");
                    int carneBuscado = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Ingrese el nuevo nombre del usuario: ");
                    String nuevoNombre = scanner.nextLine();

                    System.out.print("Ingrese el nuevo correo electrónico del usuario: ");
                    String nuevoCorreo = scanner.nextLine();

                    System.out.print("Ingrese la nueva contraseña del usuario: ");
                    String nuevaContrasena = scanner.nextLine();

                    Usuario.modificarUsuario(bd, "usuarios.csv", carneBuscado, nuevoNombre, nuevoCorreo, nuevaContrasena);
                    System.out.println("Usuario modificado exitosamente.");
                    break;

                case 5:
                    // Eliminar un usuario existente
                    System.out.println("\n--- Eliminar un Usuario ---");
                    System.out.print("Ingrese el carnet del usuario que desea eliminar: ");
                    int carneEliminar = scanner.nextInt();
                    scanner.nextLine();

                    Usuario.eliminarUsuario(bd, "usuarios.csv", carneEliminar);
                    System.out.println("Usuario eliminado exitosamente.");
                    break;

                case 6:
                    // Solicitar materiales
                    System.out.println("\n--- Solicitar Materiales ---");
                    System.out.print("Ingrese el producto de interés: ");
                    String productoInteres = scanner.nextLine();

                    System.out.print("Ingrese su nombre: ");
                    String nombreInteresado = scanner.nextLine();

                    System.out.print("Ingrese su contacto: ");
                    int contacto = scanner.nextInt();
                    scanner.nextLine();

                    SolicitarMateriales nuevaSolicitud = new SolicitarMateriales(productoInteres, nombreInteresado, contacto);
                    nuevaSolicitud.realizarSolicitud(bd, "solicitudes.csv");
                    break;

                case 7:
                    // Ver solicitudes de materiales
                    System.out.println("\n--- Ver Solicitudes ---");
                    SolicitarMateriales.verSolicitudes(bd, "solicitudes.csv");
                    break;

                case 8:
                    System.out.println("\n--- Marcar Préstamo como Regresado ---");
                    System.out.print("Ingrese el nombre del usuario: ");
                    String usuarioRegreso = scanner.nextLine();

                    System.out.print("Ingrese el nombre del producto: ");
                    String productoRegreso = scanner.nextLine();

                    Prestado.marcarComoRegresado(bd, "prestamos.csv", usuarioRegreso, productoRegreso);
                    break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }

        // Guardar cambios en archivos
        bd.guardarCSV("inventario.csv");
        bd.guardarCSV("usuarios.csv");
        bd.guardarCSV("prestamos.csv");
        bd.guardarCSV("solicitudes.csv");

        // Cerrar el Scanner
        scanner.close();
    }
}

