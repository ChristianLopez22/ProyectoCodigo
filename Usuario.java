import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int carne;
    private String nombre;
    private String correoElectronico;
    private String contrasena;

    public Usuario() {
    }

    // Constructor con argumentos
    public Usuario(int carne, String nombre, String correoElectronico, String contrasena) {
        this.carne = carne;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public int getCarne() {
        return carne;
    }

    public void setCarne(int carne) {
        this.carne = carne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método para registrar un nuevo usuario en el CSV
    public void registrarUsuario(String archivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo, true))) {
            String[] datos = {
                String.valueOf(this.carne),
                this.nombre,
                this.correoElectronico,
                this.contrasena
            };
            writer.writeNext(datos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para modificar un usuario existente en el CSV
    public static void modificarUsuario(String archivo, int carneBuscado, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        List<Usuario> usuarios = cargarUsuariosDesdeCSV(archivo);
        for (Usuario usuario : usuarios) {
            if (usuario.getCarne() == carneBuscado) {
                usuario.setNombre(nuevoNombre);
                usuario.setCorreoElectronico(nuevoCorreo);
                usuario.setContrasena(nuevaContrasena);
                break;
            }
        }
        escribirUsuariosEnCSV(archivo, usuarios);
    }

    // Método para cargar usuarios desde un archivo CSV
    public static List<Usuario> cargarUsuariosDesdeCSV(String archivo) {
        List<Usuario> usuarios = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) {
            return usuarios;
        }

        try (CSVReader reader = new CSVReader(new FileReader(archivo))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                Usuario usuario = new Usuario(Integer.parseInt(linea[0]), linea[1], linea[2], linea[3]);
                usuarios.add(usuario);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para escribir usuarios en un archivo CSV
    public static void escribirUsuariosEnCSV(String archivo, List<Usuario> usuarios) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivo))) {
            for (Usuario usuario : usuarios) {
                String[] datos = {
                    String.valueOf(usuario.getCarne()),
                    usuario.getNombre(),
                    usuario.getCorreoElectronico(),
                    usuario.getContrasena()
                };
                writer.writeNext(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un usuario del archivo CSV
    public static void eliminarUsuario(String archivo, int carneBuscado) {
        List<Usuario> usuarios = cargarUsuariosDesdeCSV(archivo);
        usuarios.removeIf(usuario -> usuario.getCarne() == carneBuscado);
        escribirUsuariosEnCSV(archivo, usuarios);
    }
}
