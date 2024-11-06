import java.util.List;

public class ReporteMultas {
    private static final double MULTA_POR_DIA = 5.0;

    // Método para calcular la multa basada en los días de retraso
    public static double calcularMulta(int diasRetraso) {
        if (diasRetraso <= 0) {
            return 0.0;
        }
        return diasRetraso * MULTA_POR_DIA;
    }

    // Genera un reporte de multas de todos los materiales en préstamo
    public static void generarReporteDeMultas(List<Prestado> prestamos, int fechaActual) {
        System.out.println("--- Reporte de Multas ---");
        for (Prestado prestamo : prestamos) {
            if (!prestamo.isRegresado()) {
                int diasRetraso = prestamo.calcularDiasRetraso(fechaActual);
                double multa = calcularMulta(diasRetraso);
                if (multa > 0) {
                    System.out.println("Producto: " + prestamo.getProducto() +
                            " | Usuario: " + prestamo.getUsuario() +
                            " | Días de retraso: " + diasRetraso +
                            " | Multa: Q" + multa);
                }
            }
        }
    }
}
