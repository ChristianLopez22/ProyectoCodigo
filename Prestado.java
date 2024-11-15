import java.util.ArrayList;
import java.util.List;

public class Prestado {
    private int fecha;
    private String usuario;
    private String producto;
    private boolean regresado;
    private int diasPrestamo;

    public Prestado(int fecha, String usuario, String producto, boolean regresado, int diasPrestamo) {
        if (fecha <= 0) throw new IllegalArgumentException("La fecha debe ser un valor positivo.");
        if (usuario == null || usuario.trim().isEmpty()) throw new IllegalArgumentException("El usuario no puede estar vacío.");
        if (producto == null || producto.trim().isEmpty()) throw new IllegalArgumentException("El producto no puede estar vacío.");
        if (diasPrestamo <= 0) throw new IllegalArgumentException("Los días de préstamo deben ser un valor positivo.");

        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
        this.regresado = regresado;
        this.diasPrestamo = diasPrestamo;
    }

    // Getters y setters con validaciones
    public int getFecha() { return fecha; }
    public void setFecha(int fecha) {
        if (fecha <= 0) throw new IllegalArgumentException("La fecha debe ser un valor positivo.");
        this.fecha = fecha;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) throw new IllegalArgumentException("El usuario no puede estar vacío.");
        this.usuario = usuario;
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) {
        if (producto == null || producto.trim().isEmpty()) throw new IllegalArgumentException("El producto no puede estar vacío.");
        this.producto = producto;
    }

    public boolean isRegresado() { return regresado; }
    public void setRegresado(boolean regresado) { this.regresado = regresado; }

    public int getDiasPrestamo() { return diasPrestamo; }
    public void setDiasPrestamo(int diasPrestamo) {
        if (diasPrestamo <= 0) throw new IllegalArgumentException("Los días de préstamo deben ser un valor positivo.");
        this.diasPrestamo = diasPrestamo;
    }

    // Método para cargar préstamos desde la base de datos en memoria
    public static List<Prestado> cargarPrestamosDesdeBase(BaseDeDatos bd, String archivo) {
        if (bd == null) throw new IllegalArgumentException("La base de datos no puede ser nula.");
        if (archivo == null || archivo.trim().isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacío.");

        List<String[]> datos = bd.obtenerDatos(archivo);
        if (datos == null) throw new IllegalArgumentException("Los datos obtenidos no pueden ser nulos.");

        List<Prestado> prestamos = new ArrayList<>();

        for (String[] linea : datos) {
            try {
                if (linea.length < 5) throw new IllegalArgumentException("Cada línea debe tener al menos 5 elementos.");

                int fecha = Integer.parseInt(linea[0].trim());
                String usuario = linea[1].trim();
                String producto = linea[2].trim();
                boolean regresado = Boolean.parseBoolean(linea[3].trim());
                int diasPrestamo = Integer.parseInt(linea[4].trim());

                prestamos.add(new Prestado(fecha, usuario, producto, regresado, diasPrestamo));
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear datos: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Datos inválidos: " + e.getMessage());
            }
        }
        return prestamos;
    }

    // Método para guardar préstamos en la base de datos en memoria
    public static void guardarPrestamosEnBase(BaseDeDatos bd, String archivo, List<Prestado> prestamos) {
        if (bd == null) throw new IllegalArgumentException("La base de datos no puede ser nula.");
        if (archivo == null || archivo.trim().isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacío.");
        if (prestamos == null) throw new IllegalArgumentException("La lista de préstamos no puede ser nula.");

        List<String[]> datos = new ArrayList<>();
        for (Prestado prestamo : prestamos) {
            String[] linea = {
                String.valueOf(prestamo.getFecha()),
                prestamo.getUsuario(),
                prestamo.getProducto(),
                String.valueOf(prestamo.isRegresado()),
                String.valueOf(prestamo.getDiasPrestamo())
            };
            datos.add(linea);
        }
        bd.actualizarDatos(archivo, datos);
    }

    // Método para marcar préstamo como regresado
    public static void marcarComoRegresado(BaseDeDatos bd, String archivo, String usuario, String producto) {
        if (bd == null) throw new IllegalArgumentException("La base de datos no puede ser nula.");
        if (archivo == null || archivo.trim().isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacío.");
        if (usuario == null || usuario.trim().isEmpty()) throw new IllegalArgumentException("El usuario no puede estar vacío.");
        if (producto == null || producto.trim().isEmpty()) throw new IllegalArgumentException("El producto no puede estar vacío.");

        List<Prestado> prestamos = cargarPrestamosDesdeBase(bd, archivo);
        boolean encontrado = false;
        
        for (Prestado prestamo : prestamos) {
            if (prestamo.getUsuario().equals(usuario) && prestamo.getProducto().equals(producto)) {
                if (prestamo.isRegresado()) {
                    System.out.println("El préstamo ya ha sido marcado como regresado.");
                    return;
                }
                prestamo.setRegresado(true);
                System.out.println("El préstamo del producto '" + producto + "' por el usuario '" + usuario + "' ha sido marcado como regresado.");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró un préstamo del producto '" + producto + "' por el usuario '" + usuario + "'.");
        } else {
            guardarPrestamosEnBase(bd, archivo, prestamos);
        }
    }
}
