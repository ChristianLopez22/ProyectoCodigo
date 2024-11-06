import java.util.ArrayList;
import java.util.List;

public class Prestado {
    private int fecha;
    private String usuario;
    private String producto;
    private boolean regresado;
    private int diasPrestamo; // Nuevo atributo para almacenar la cantidad de días de préstamo

    public Prestado(int fecha, String usuario, String producto, boolean regresado, int diasPrestamo) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
        this.regresado = regresado;
        this.diasPrestamo = diasPrestamo; // Asignación del nuevo atributo
    }

    // Getters y setters
    public int getFecha() { return fecha; }
    public void setFecha(int fecha) { this.fecha = fecha; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public boolean isRegresado() { return regresado; }
    public void setRegresado(boolean regresado) { this.regresado = regresado; }
    public int getDiasPrestamo() { return diasPrestamo; }
    public void setDiasPrestamo(int diasPrestamo) { this.diasPrestamo = diasPrestamo; }

    // Método para cargar préstamos desde la base de datos en memoria
    public static List<Prestado> cargarPrestamosDesdeBase(BaseDeDatos bd, String archivo) {
        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Prestado> prestamos = new ArrayList<>();

        for (String[] linea : datos) {
            int fecha = Integer.parseInt(linea[0].trim());
            String usuario = linea[1].trim();
            String producto = linea[2].trim();
            boolean regresado = Boolean.parseBoolean(linea[3].trim());
            int diasPrestamo = Integer.parseInt(linea[4].trim());

            Prestado prestamo = new Prestado(fecha, usuario, producto, regresado, diasPrestamo);
            prestamos.add(prestamo);
        }
        return prestamos;
    }

    // Método para guardar préstamos en la base de datos en memoria
    public static void guardarPrestamosEnBase(BaseDeDatos bd, String archivo, List<Prestado> prestamos) {
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
        List<Prestado> prestamos = cargarPrestamosDesdeBase(bd, archivo);
        for (Prestado prestamo : prestamos) {
            if (prestamo.getUsuario().equals(usuario) && prestamo.getProducto().equals(producto)) {
                prestamo.setRegresado(true);
                System.out.println("El préstamo del producto '" + producto + "' por el usuario '" + usuario + "' ha sido marcado como regresado.");
                break;
            }
        }
        guardarPrestamosEnBase(bd, archivo, prestamos);
    }
}
