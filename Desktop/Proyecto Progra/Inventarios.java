import java.util.ArrayList;
import java.util.List;

public class Inventarios {
    private int cantidad;
    private String material;
    private int id;

    // Constructor
    public Inventarios(int id, int cantidad, String material) {
        this.id = id;
        this.cantidad = cantidad;
        this.material = material;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    // Método para verificar disponibilidad de un material por su ID
    public boolean isDisponible() {
        return cantidad > 0;
    }

    // Método para reducir la cantidad disponible en caso de préstamo
    public void reducirCantidad(int cantidadPrestada) {
        if (cantidad >= cantidadPrestada) {
            cantidad -= cantidadPrestada;
        } else {
            System.out.println("No hay suficiente cantidad de " + material);
        }
    }

    // Método para cargar inventarios desde la base de datos en memoria
    public static List<Inventarios> cargarInventarioDesdeBase(BaseDeDatos bd, String archivo) {
        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Inventarios> inventarios = new ArrayList<>();

        for (String[] linea : datos) {
            int id = Integer.parseInt(linea[0].trim());
            int cantidad = Integer.parseInt(linea[1].trim());
            String material = linea[2].trim();

            Inventarios inventario = new Inventarios(id, cantidad, material);
            inventarios.add(inventario);
        }
        return inventarios;
    }

    // Método para guardar inventarios en la base de datos en memoria
    public static void guardarInventarioEnBase(BaseDeDatos bd, String archivo, List<Inventarios> inventarios) {
        List<String[]> datos = new ArrayList<>();
        for (Inventarios inventario : inventarios) {
            String[] linea = {
                String.valueOf(inventario.getId()),
                String.valueOf(inventario.getCantidad()),
                inventario.getMaterial()
            };
            datos.add(linea);
        }
        bd.actualizarDatos(archivo, datos);
    }

    // Método para verificar la disponibilidad de todos los materiales
    public static void verDisponibilidad(BaseDeDatos bd, String archivo) {
        List<Inventarios> inventarios = cargarInventarioDesdeBase(bd, archivo);
        for (Inventarios inventario : inventarios) {
            if (inventario.isDisponible()) {
                System.out.println("ID: " + inventario.getId() + " - Material: " + inventario.getMaterial() +
                        " disponible con cantidad: " + inventario.getCantidad());
            } else {
                System.out.println("ID: " + inventario.getId() + " - Material: " + inventario.getMaterial() + " está agotado.");
            }
        }
    }
}
