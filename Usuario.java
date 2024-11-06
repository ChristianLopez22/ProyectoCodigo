import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int carne;
    private String nombre;
    private String correoElectronico;
    private String contrasena;

    public Usuario(int carne, String nombre, String correoElectronico, String contrasena) {
        this.carne = carne;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public int getCarne() { return carne; }
    public void setCarne(int carne) { this.carne = carne; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // Metodos de manipulación de usuarios
    public static List<Usuario> cargarUsuariosDesdeBase(BaseDeDatos bd, String archivo) {
        List<String[]> datos = bd.obtenerDatos(archivo);
        List<Usuario> usuarios = new ArrayList<>();

        for (String[] linea : datos) {
            Usuario usuario = new Usuario(Integer.parseInt(linea[0]), linea[1], linea[2], linea[3]);
            usuarios.add(usuario);
        }
        return usuarios;
    }

    public static void guardarUsuariosEnBase(BaseDeDatos bd, String archivo, List<Usuario> usuarios) {
        List<String[]> datos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            String[] linea = {
                String.valueOf(usuario.getCarne()),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getContrasena()
            };
            datos.add(linea);
        }
        bd.actualizarDatos(archivo, datos);
    }

    public static void modificarUsuario(BaseDeDatos bd, String archivo, int carneBuscado, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        List<Usuario> usuarios = cargarUsuariosDesdeBase(bd, archivo);
        for (Usuario usuario : usuarios) {
            if (usuario.getCarne() == carneBuscado) {
                usuario.setNombre(nuevoNombre);
                usuario.setCorreoElectronico(nuevoCorreo);
                usuario.setContrasena(nuevaContrasena);
                break;
            }
        }
        guardarUsuariosEnBase(bd, archivo, usuarios);
    }

    public static void eliminarUsuario(BaseDeDatos bd, String archivo, int carneBuscado) {
        List<Usuario> usuarios = cargarUsuariosDesdeBase(bd, archivo);
        usuarios.removeIf(usuario -> usuario.getCarne() == carneBuscado);
        guardarUsuariosEnBase(bd, archivo, usuarios);
    }
}
