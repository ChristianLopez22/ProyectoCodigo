import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int carne;
    private String nombre;
    private String correoElectronico;
    private String contrasena;

    public Usuario(int carne, String nombre, String correoElectronico, String contrasena) {
        // Validación defensiva en el constructor
        if (carne <= 0) {
            throw new IllegalArgumentException("El número de carnet debe ser positivo.");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        this.carne = carne;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters y setters con validación defensiva
    public int getCarne() {
        return carne;
    }

    public void setCarne(int carne) {
        if (carne <= 0) {
            throw new IllegalArgumentException("El número de carnet debe ser positivo.");
        }
        this.carne = carne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío.");
        }
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        this.contrasena = contrasena;
    }

    // Métodos de manipulación de usuarios con programación defensiva
    public static List<Usuario> cargarUsuariosDesdeBase(BaseDeDatos bd, String archivo) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }

        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Usuario> usuarios = new ArrayList<>();

        if (datos != null) {
            for (String[] linea : datos) {
                if (linea.length >= 4) {
                    try {
                        int carne = Integer.parseInt(linea[0].trim());
                        String nombre = linea[1].trim();
                        String correo = linea[2].trim();
                        String contrasena = linea[3].trim();

                        // Validación antes de crear el usuario
                        if (carne > 0 && !nombre.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()) {
                            usuarios.add(new Usuario(carne, nombre, correo, contrasena));
                        } else {
                            System.out.println("Datos inválidos encontrados en la línea: " + String.join(",", linea));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir el carnet a número: " + e.getMessage());
                    }
                } else {
                    System.out.println("Línea incompleta encontrada: " + String.join(",", linea));
                }
            }
        } else {
            System.out.println("No se encontraron datos en el archivo " + archivo);
        }
        return usuarios;
    }

    public static void guardarUsuariosEnBase(BaseDeDatos bd, String archivo, List<Usuario> usuarios) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }
        if (usuarios == null) {
            throw new IllegalArgumentException("La lista de usuarios no puede ser nula.");
        }

        List<String[]> datos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario != null) {
                String[] linea = {
                    String.valueOf(usuario.getCarne()),
                    usuario.getNombre(),
                    usuario.getCorreoElectronico(),
                    usuario.getContrasena()
                };
                datos.add(linea);
            }
        }
        bd.actualizarDatos(archivo, datos);
    }

    public static void modificarUsuario(BaseDeDatos bd, String archivo, int carneBuscado, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }
        if (carneBuscado <= 0) {
            throw new IllegalArgumentException("El número de carnet debe ser positivo.");
        }
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío.");
        }
        if (nuevoCorreo == null || nuevoCorreo.isEmpty()) {
            throw new IllegalArgumentException("El nuevo correo electrónico no puede estar vacío.");
        }
        if (nuevaContrasena == null || nuevaContrasena.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
        }

        List<Usuario> usuarios = cargarUsuariosDesdeBase(bd, archivo);
        boolean usuarioEncontrado = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getCarne() == carneBuscado) {
                usuario.setNombre(nuevoNombre);
                usuario.setCorreoElectronico(nuevoCorreo);
                usuario.setContrasena(nuevaContrasena);
                usuarioEncontrado = true;
                break;
            }
        }

        if (!usuarioEncontrado) {
            System.out.println("Usuario con carnet " + carneBuscado + " no encontrado.");
        }

        guardarUsuariosEnBase(bd, archivo, usuarios);
    }

    public static void eliminarUsuario(BaseDeDatos bd, String archivo, int carneBuscado) {
        if (bd == null) {
            throw new IllegalArgumentException("La base de datos no puede ser nula.");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }
        if (carneBuscado <= 0) {
            throw new IllegalArgumentException("El número de carnet debe ser positivo.");
        }

        List<Usuario> usuarios = cargarUsuariosDesdeBase(bd, archivo);
        boolean usuarioEliminado = usuarios.removeIf(usuario -> usuario.getCarne() == carneBuscado);

        if (!usuarioEliminado) {
            System.out.println("Usuario con carnet " + carneBuscado + " no encontrado.");
        } else {
            System.out.println("Usuario con carnet " + carneBuscado + " eliminado correctamente.");
        }

        guardarUsuariosEnBase(bd, archivo, usuarios);
    }
}
