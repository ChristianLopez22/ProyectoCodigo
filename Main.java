import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Crear instancia de BaseDeDatos
        BaseDeDatos bd = new BaseDeDatos();

        // Cargar archivos CSV en memoria con manejo de excepciones
        try {
            bd.cargarCSV("inventario.csv");
            bd.cargarCSV("usuarios.csv");
            bd.cargarCSV("prestamos.csv");
            bd.cargarCSV("solicitudes.csv");
        } catch (Exception e) {
            System.err.println("Error al cargar archivos CSV: " + e.getMessage());
            return;
        }

        // Inicializar el inventario si está vacío
        List<Inventarios> inventarios = Inventarios.cargarInventarioDesdeBase(bd, "inventario.csv");
        if (inventarios == null) {
            inventarios = new ArrayList<>();
        }
        if (inventarios.isEmpty()) {
            try {
                // Crear un inventario ficticio inicial
                inventarios.add(new Inventarios(1, 10, "Calculadora Científica"));
                inventarios.add(new Inventarios(2, 15, "Libro de Matemáticas"));
                inventarios.add(new Inventarios(3, 5, "Audífonos"));
                inventarios.add(new Inventarios(4, 8, "Diccionario de Inglés"));
                inventarios.add(new Inventarios(5, 20, "Lápiz Mecánico"));

                // Guardar el inventario inicial en la base de datos
                Inventarios.guardarInventarioEnBase(bd, "inventario.csv", inventarios);
                System.out.println("Inventario inicial ficticio creado.");
            } catch (Exception e) {
                System.err.println("Error al inicializar inventario: " + e.getMessage());
                return;
            }
        }

        // Inicializar Scanner
        Scanner scanner = new Scanner(System.in);

        // Cargar usuarios
        List<Usuario> usuarios = Usuario.cargarUsuariosDesdeBase(bd, "usuarios.csv");
        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }

        // Menú principal
        int opcion = -1;
        while (opcion != 0) {
            try {
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
                if (!scanner.hasNextInt()) {
                    System.out.println("Por favor, ingrese un número válido.");
                    scanner.nextLine(); // Limpiar entrada inválida
                    continue;
                }
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (opcion) {
                    case 1:
                        // Ver disponibilidad de materiales
                        System.out.println("\n--- Ver Disponibilidad de Materiales ---");
                        try {
                            Inventarios.verDisponibilidad(bd, "inventario.csv");
                        } catch (Exception e) {
                            System.err.println("Error al ver disponibilidad: " + e.getMessage());
                        }
                        break;

                    case 2:
                        // Registrar un nuevo préstamo con verificación de disponibilidad
                        System.out.println("\n--- Registrar un Nuevo Préstamo ---");
                        System.out.print("Ingrese el número de carnet del usuario: ");
                        if (!scanner.hasNextInt()) {
                            System.out.println("Carnet inválido. Intente de nuevo.");
                            scanner.nextLine();
                            break;
                        }
                        int numeroCarnet = scanner.nextInt();
                        scanner.nextLine();

                        // Verificar si el usuario existe
                        Usuario usuarioActual = null;
                        for (Usuario usuario : usuarios) {
                            if (usuario.getCarne() == numeroCarnet) {
                                usuarioActual = usuario;
                                break;
                            }
                        }

                        if (usuarioActual == null) {
                            System.out.println("Usuario no encontrado. Debe registrarse antes de poder realizar un préstamo.");
                            System.out.print("¿Desea registrar al usuario ahora? (si/no): ");
                            String respuesta = scanner.nextLine();
                            if (respuesta.equalsIgnoreCase("si")) {
                                System.out.print("Ingrese el nombre del usuario: ");
                                String nombre = scanner.nextLine();

                                System.out.print("Ingrese el correo electrónico del usuario: ");
                                String correoElectronico = scanner.nextLine();

                                System.out.print("Ingrese la contraseña del usuario: ");
                                String contrasena = scanner.nextLine();

                                try {
                                    Usuario nuevoUsuario = new Usuario(numeroCarnet, nombre, correoElectronico, contrasena);
                                    usuarios.add(nuevoUsuario);
                                    Usuario.guardarUsuariosEnBase(bd, "usuarios.csv", usuarios);
                                    usuarioActual = nuevoUsuario;
                                    System.out.println("Usuario registrado exitosamente.");
                                } catch (Exception e) {
                                    System.err.println("Error al registrar usuario: " + e.getMessage());
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                        // Mostrar materiales disponibles para préstamo
                        try {
                            inventarios = Inventarios.cargarInventarioDesdeBase(bd, "inventario.csv");
                            System.out.println("\nMateriales Disponibles:");
                            for (int i = 0; i < inventarios.size(); i++) {
                                Inventarios inventario = inventarios.get(i);
                                System.out.println((i + 1) + ". " + inventario.getMaterial() + " - Cantidad: " + inventario.getCantidad());
                            }

                            System.out.print("Seleccione el número del material que desea prestar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Selección inválida. Intente de nuevo.");
                                scanner.nextLine();
                                break;
                            }
                            int seleccionMaterial = scanner.nextInt() - 1;
                            scanner.nextLine();

                            if (seleccionMaterial < 0 || seleccionMaterial >= inventarios.size()) {
                                System.out.println("Selección inválida. Intente de nuevo.");
                                break;
                            }

                            Inventarios inventarioSeleccionado = inventarios.get(seleccionMaterial);

                            if (inventarioSeleccionado.isDisponible(1)) { // Verificar disponibilidad
                                System.out.print("Ingrese la fecha del préstamo (formato: YYYYMMDD): ");
                                if (!scanner.hasNextInt()) {
                                    System.out.println("Fecha inválida. Intente de nuevo.");
                                    scanner.nextLine();
                                    break;
                                }
                                int fecha = scanner.nextInt();
                                scanner.nextLine();

                                System.out.print("Ingrese la cantidad de días de préstamo: ");
                                String diasPrestamoInput = scanner.nextLine();
                                int diasPrestamo;
                                if (diasPrestamoInput.isEmpty()) {
                                    diasPrestamo = 7; // Valor predeterminado si el usuario no proporciona una cantidad de días
                                } else {
                                    try {
                                        diasPrestamo = Integer.parseInt(diasPrestamoInput);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Días de préstamo inválido. Usando el valor predeterminado de 7 días.");
                                        diasPrestamo = 7;
                                    }
                                }

                                // Reducir cantidad en inventario y registrar préstamo
                                inventarioSeleccionado.reducirCantidad(1);
                                Inventarios.guardarInventarioEnBase(bd, "inventario.csv", inventarios);

                                // Registrar el préstamo
                                Prestado nuevoPrestamo = new Prestado(fecha, usuarioActual.getNombre(), inventarioSeleccionado.getMaterial(), false, diasPrestamo);
                                List<Prestado> prestamos = Prestado.cargarPrestamosDesdeBase(bd, "prestamos.csv");
                                if (prestamos == null) {
                                    prestamos = new ArrayList<>();
                                }
                                prestamos.add(nuevoPrestamo);
                                Prestado.guardarPrestamosEnBase(bd, "prestamos.csv", prestamos);

                                System.out.println("Préstamo registrado exitosamente.");
                            } else {
                                System.out.println("El producto seleccionado no está disponible en el inventario.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error al registrar el préstamo: " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Registrar un nuevo usuario
                        System.out.println("\n--- Registrar un Nuevo Usuario ---");
                        try {
                            System.out.print("Ingrese el carnet del usuario: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Carnet inválido. Intente de nuevo.");
                                scanner.nextLine();
                                break;
                            }
                            int carne = scanner.nextInt();
                            scanner.nextLine();

                            System.out.print("Ingrese el nombre del usuario: ");
                            String nombre = scanner.nextLine();

                            System.out.print("Ingrese el correo electrónico del usuario: ");
                            String correoElectronico = scanner.nextLine();

                            System.out.print("Ingrese la contraseña del usuario: ");
                            String contrasena = scanner.nextLine();

                            Usuario nuevoUsuario = new Usuario(carne, nombre, correoElectronico, contrasena);
                            usuarios.add(nuevoUsuario);
                            Usuario.guardarUsuariosEnBase(bd, "usuarios.csv", usuarios);
                            System.out.println("Usuario registrado exitosamente.");
                        } catch (Exception e) {
                            System.err.println("Error al registrar el usuario: " + e.getMessage());
                        }
                        break;

                    case 4:
                        // Modificar un usuario existente
                        System.out.println("\n--- Modificar un Usuario Existente ---");
                        try {
                            System.out.print("Ingrese el carnet del usuario que desea modificar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Carnet inválido. Intente de nuevo.");
                                scanner.nextLine();
                                break;
                            }
                            int carneBuscado = scanner.nextInt();
                            scanner.nextLine();

                            Usuario usuarioAModificar = null;
                            for (Usuario usuario : usuarios) {
                                if (usuario.getCarne() == carneBuscado) {
                                    usuarioAModificar = usuario;
                                    break;
                                }
                            }

                            if (usuarioAModificar != null) {
                                System.out.print("Ingrese el nuevo nombre del usuario: ");
                                String nuevoNombre = scanner.nextLine();

                                System.out.print("Ingrese el nuevo correo electrónico del usuario: ");
                                String nuevoCorreo = scanner.nextLine();

                                System.out.print("Ingrese la nueva contraseña del usuario: ");
                                String nuevaContrasena = scanner.nextLine();

                                usuarioAModificar.setNombre(nuevoNombre);
                                usuarioAModificar.setCorreoElectronico(nuevoCorreo);
                                usuarioAModificar.setContrasena(nuevaContrasena);

                                Usuario.guardarUsuariosEnBase(bd, "usuarios.csv", usuarios);
                                System.out.println("Usuario modificado exitosamente.");
                            } else {
                                System.out.println("Usuario no encontrado.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error al modificar el usuario: " + e.getMessage());
                        }
                        break;

                    case 5:
                        // Eliminar un usuario existente
                        System.out.println("\n--- Eliminar un Usuario ---");
                        try {
                            System.out.print("Ingrese el carnet del usuario que desea eliminar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Carnet inválido. Intente de nuevo.");
                                scanner.nextLine();
                                break;
                            }
                            int carneEliminar = scanner.nextInt();
                            scanner.nextLine();

                            boolean usuarioEliminado = usuarios.removeIf(usuario -> usuario.getCarne() == carneEliminar);
                            if (usuarioEliminado) {
                                Usuario.guardarUsuariosEnBase(bd, "usuarios.csv", usuarios);
                                System.out.println("Usuario eliminado exitosamente.");
                            } else {
                                System.out.println("Usuario no encontrado.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error al eliminar el usuario: " + e.getMessage());
                        }
                        break;

                    case 6:
                        // Solicitar materiales
                        System.out.println("\n--- Solicitar Materiales ---");
                        try {
                            System.out.print("Ingrese el producto de interés: ");
                            String productoInteres = scanner.nextLine();

                            System.out.print("Ingrese su nombre: ");
                            String nombreInteresado = scanner.nextLine();

                            System.out.print("Ingrese su contacto: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Contacto inválido. Intente de nuevo.");
                                scanner.nextLine();
                                break;
                            }
                            int contacto = scanner.nextInt();
                            scanner.nextLine();

                            SolicitarMateriales nuevaSolicitud = new SolicitarMateriales(productoInteres, nombreInteresado, contacto);
                            nuevaSolicitud.realizarSolicitud(bd, "solicitudes.csv");
                        } catch (Exception e) {
                            System.err.println("Error al solicitar materiales: " + e.getMessage());
                        }
                        break;

                    case 7:
                        // Ver solicitudes de materiales
                        System.out.println("\n--- Ver Solicitudes ---");
                        try {
                            SolicitarMateriales.verSolicitudes(bd, "solicitudes.csv");
                        } catch (Exception e) {
                            System.err.println("Error al ver solicitudes: " + e.getMessage());
                        }
                        break;

                    case 8:
                        // Marcar préstamo como regresado
                        System.out.println("\n--- Marcar Préstamo como Regresado ---");
                        try {
                            System.out.print("Ingrese el nombre del usuario: ");
                            String usuarioRegreso = scanner.nextLine();

                            List<Prestado> prestamos = Prestado.cargarPrestamosDesdeBase(bd, "prestamos.csv");
                            if (prestamos == null) {
                                prestamos = new ArrayList<>();
                            }

                            List<Prestado> prestamosUsuario = new ArrayList<>();

                            // Filtrar los préstamos del usuario que no han sido regresados
                            for (Prestado prestamo : prestamos) {
                                if (prestamo.getUsuario().equals(usuarioRegreso) && !prestamo.isRegresado()) {
                                    prestamosUsuario.add(prestamo);
                                }
                            }

                            if (prestamosUsuario.isEmpty()) {
                                System.out.println("No se encontraron préstamos activos para el usuario especificado.");
                            } else {
                                System.out.println("\nPréstamos Activos del Usuario:");
                                for (int i = 0; i < prestamosUsuario.size(); i++) {
                                    Prestado prestamo = prestamosUsuario.get(i);
                                    System.out.println((i + 1) + ". Producto: " + prestamo.getProducto() + ", Fecha de préstamo: " + prestamo.getFecha());
                                }

                                System.out.print("Seleccione el número del préstamo que desea marcar como regresado: ");
                                if (!scanner.hasNextInt()) {
                                    System.out.println("Selección inválida. Intente de nuevo.");
                                    scanner.nextLine();
                                    break;
                                }
                                int seleccionPrestamo = scanner.nextInt() - 1;
                                scanner.nextLine();

                                if (seleccionPrestamo < 0 || seleccionPrestamo >= prestamosUsuario.size()) {
                                    System.out.println("Selección inválida. Intente de nuevo.");
                                } else {
                                    Prestado prestamoSeleccionado = prestamosUsuario.get(seleccionPrestamo);
                                    prestamoSeleccionado.setRegresado(true);

                                    // Aumentar la cantidad en el inventario al devolver el producto
                                    inventarios = Inventarios.cargarInventarioDesdeBase(bd, "inventario.csv");
                                    Inventarios itemDevuelto = Inventarios.buscarInventarioPorNombre(inventarios, prestamoSeleccionado.getProducto());
                                    if (itemDevuelto != null) {
                                        itemDevuelto.setCantidad(itemDevuelto.getCantidad() + 1);
                                        Inventarios.guardarInventarioEnBase(bd, "inventario.csv", inventarios);
                                    }

                                    System.out.println("El préstamo del producto '" + prestamoSeleccionado.getProducto() + "' por el usuario '" + usuarioRegreso + "' ha sido marcado como regresado.");
                                }
                                Prestado.guardarPrestamosEnBase(bd, "prestamos.csv", prestamos);
                            }
                        } catch (Exception e) {
                            System.err.println("Error al marcar préstamo como regresado: " + e.getMessage());
                        }
                        break;

                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Error en la opción del menú principal: " + e.getMessage());
            }
        }

        // Guardar cambios en archivos con manejo de excepciones
        try {
            bd.guardarCSV("inventario.csv");
            bd.guardarCSV("usuarios.csv");
            bd.guardarCSV("prestamos.csv");
            bd.guardarCSV("solicitudes.csv");
        } catch (Exception e) {
            System.err.println("Error al guardar archivos CSV: " + e.getMessage());
        }

        // Cerrar el Scanner
        scanner.close();
    }
}
