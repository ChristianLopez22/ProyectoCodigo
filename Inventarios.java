import java.util.ArrayList;
import java.util.List;

public class Inventarios {
    private int cantidad;
    private String material;
    private int id;

    // Constructor
    public Inventarios(int id, int cantidad, String material) {
        if (id < 0) throw new IllegalArgumentException("El ID no puede ser negativo.");
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        if (material == null || material.trim().isEmpty()) throw new IllegalArgumentException("El nombre del material no puede ser nulo o vacío.");

        this.id = id;
        this.cantidad = cantidad;
        this.material = material;
    }

    // Getters y setters con validaciones
    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("El ID no puede ser negativo.");
        this.id = id;
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        this.cantidad = cantidad;
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) {
        if (material == null || material.trim().isEmpty()) throw new IllegalArgumentException("El nombre del material no puede ser nulo o vacío.");
        this.material = material;
    }

    // Método para verificar si un material está disponible en el inventario
    public boolean isDisponible(int cantidadSolicitada) {
        if (cantidadSolicitada <= 0) {
            throw new IllegalArgumentException("La cantidad solicitada debe ser mayor a cero.");
        }
        return this.cantidad >= cantidadSolicitada;
    }

    // Método para reducir la cantidad en el inventario después de un préstamo
    public void reducirCantidad(int cantidadPrestada) {
        if (cantidadPrestada <= 0) {
            System.out.println("Error: La cantidad prestada debe ser mayor a cero.");
            return;
        }
        if (this.cantidad >= cantidadPrestada) {
            this.cantidad -= cantidadPrestada;
        } else {
            System.out.println("No hay suficiente cantidad de " + material);
        }
    }

    // Método para cargar inventarios desde la base de datos en memoria
    public static List<Inventarios> cargarInventarioDesdeBase(BaseDeDatos bd, String archivo) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }

        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Inventarios> inventarios = new ArrayList<>();

        for (String[] linea : datos) {
            if (linea.length < 3) {
                System.out.println("Advertencia: Línea en archivo " + archivo + " incompleta. Se omite.");
                continue;
            }
            try {
                int id = Integer.parseInt(linea[0].trim());
                int cantidad = Integer.parseInt(linea[1].trim());
                String material = linea[2].trim();
                inventarios.add(new Inventarios(id, cantidad, material));
            } catch (NumberFormatException e) {
                System.out.println("Error de formato en línea del archivo " + archivo + ": " + e.getMessage());
            }
        }
        return inventarios;
    }

    // Método para guardar inventarios en la base de datos en memoria
    public static void guardarInventarioEnBase(BaseDeDatos bd, String archivo, List<Inventarios> inventarios) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }
        if (inventarios == null || inventarios.isEmpty()) {
            System.out.println("Advertencia: La lista de inventarios está vacía. No se guarda nada en la base de datos.");
            return;
        }

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

    // Método para buscar un inventario por nombre de material
    public static Inventarios buscarInventarioPorNombre(List<Inventarios> inventarios, String nombreMaterial) {
        if (inventarios == null) {
            throw new IllegalArgumentException("La lista de inventarios no puede ser nula.");
        }
        if (nombreMaterial == null || nombreMaterial.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del material no puede ser nulo o vacío.");
        }

        for (Inventarios inventario : inventarios) {
            if (inventario.getMaterial().equalsIgnoreCase(nombreMaterial)) {
                return inventario;
            }
        }
        return null;
    }

    // Método para ver la disponibilidad de materiales
    public static void verDisponibilidad(BaseDeDatos bd, String archivo) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }

        List<Inventarios> inventarios = cargarInventarioDesdeBase(bd, archivo);
        if (inventarios.isEmpty()) {
            System.out.println("No hay inventarios disponibles en el archivo " + archivo);
            return;
        }

        for (Inventarios inventario : inventarios) {
            if (inventario.getCantidad() > 0) {
                System.out.println("ID: " + inventario.getId() + " - Material: " + inventario.getMaterial() +
                        " disponible con cantidad: " + inventario.getCantidad());
            } else {
                System.out.println("ID: " + inventario.getId() + " - Material: " + inventario.getMaterial() + " está agotado.");
            }
        }
    }
}
