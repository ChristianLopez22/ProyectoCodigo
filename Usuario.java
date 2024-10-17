import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Usuario {
    private int carne;
    private String nombre;
    private String correoElectronico;
    private String contrasena;

    // Constructor
    public Usuario(int carne, String nombre, String correoElectronico, String contrasena) {
        this.carne = carne;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
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
            System.out.println("Usuario registrado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

