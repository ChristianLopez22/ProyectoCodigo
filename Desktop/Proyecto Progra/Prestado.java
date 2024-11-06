import java.util.ArrayList;
import java.util.List;

public class Prestado {
    private int fecha;
    private String usuario;
    private String producto;
    private boolean regresado;
    private int diasPrestamo;

    public Prestado(int fecha, String usuario, String producto, boolean regresado, int diasPrestamo) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.producto = producto;
        this.regresado = regresado;
        this.diasPrestamo = diasPrestamo;
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

    // Método para cargar préstamos desde la base de datos en memoria con manejo de errores
    public static List<Prestado> cargarPrestamosDesdeBase(BaseDeDatos bd, String archivo) {
        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Prestado> prestamos = new ArrayList<>();

        for (String[] linea : datos) {
            try {
                if (linea.length < 5) {
                    System.out.println("Advertencia: Línea incompleta en el archivo '" + archivo + "'. Saltando esta línea.");
                    continue; // Saltar líneas incompletas
                }
                
                int fecha = Integer.parseInt(linea[0].trim());
                String usuario = linea[1].trim();
                String producto = linea[2].trim();
                boolean regresado = Boolean.parseBoolean(linea[3].trim());
                int diasPrestamo = Integer.parseInt(linea[4].trim());
                
                Prestado prestamo = new Prestado(fecha, usuario, producto, regresado, diasPrestamo);
                prestamos.add(prestamo);
                
            } catch (NumberFormatException e) {
                System.out.println("Error de formato en una línea de '" + archivo + "'. Saltando esta línea.");
            } catch (Exception e) {
                System.out.println("Error inesperado al procesar el archivo '" + archivo + "': " + e.getMessage());
            }
        }
        return prestamos;
    }

    // Método para guardar préstamos en la base de datos en memoria de forma automática y con formato correcto
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

    // Método para calcular días de retraso (ejemplo básico)
    public int calcularDiasRetraso(int fechaActual) {
        int diasTranscurridos = fechaActual - fecha; // Asumiendo que la fecha está en días enteros consecutivos
        return Math.max(0, diasTranscurridos - diasPrestamo); // Retorna el retraso en días
    }

    // Método para marcar un préstamo como regresado
    public static void marcarComoRegresado(BaseDeDatos bd, String archivo, String usuario, String producto) {
        List<Prestado> prestamos = cargarPrestamosDesdeBase(bd, archivo);
        for (Prestado prestamo : prestamos) {
            if (prestamo.getUsuario().equals(usuario) && prestamo.getProducto().equals(producto) && !prestamo.isRegresado()) {
                prestamo.setRegresado(true);
                System.out.println("El préstamo del producto '" + producto + "' por el usuario '" + usuario + "' ha sido marcado como regresado.");
                break;
            }
        }
        guardarPrestamosEnBase(bd, archivo, prestamos);
    }
}
