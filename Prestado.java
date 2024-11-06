import java.util.ArrayList;
import java.util.List;

public class Prestado {
    private int fecha;
    private String usuario;
    private String producto;
    private boolean regresado;

    public Prestado(int fecha, String usuario, String producto, boolean regresado) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
        this.regresado = regresado;
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

    // Metodo para cargar prestamos desde la base de datos en memoria
    public static List<Prestado> cargarPrestamosDesdeBase(BaseDeDatos bd, String archivo) {
        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Prestado> prestamos = new ArrayList<>();

        for (String[] linea : datos) {
            boolean regresado = Boolean.parseBoolean(linea[3]);
            Prestado prestamo = new Prestado(Integer.parseInt(linea[0]), linea[1], linea[2], regresado);
            prestamos.add(prestamo);
        }
        return prestamos;
    }

    // Metodo para guardar préstamos en la base de datos en memoria
    public static void guardarPrestamosEnBase(BaseDeDatos bd, String archivo, List<Prestado> prestamos) {
        List<String[]> datos = new ArrayList<>();
        for (Prestado prestamo : prestamos) {
            String[] linea = {
                String.valueOf(prestamo.getFecha()),
                prestamo.getUsuario(),
                prestamo.getProducto(),
                String.valueOf(prestamo.isRegresado())
            };
            datos.add(linea);
        }
        bd.actualizarDatos(archivo, datos);
    }

    // Metodo para marcar prestamo como regresado
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
