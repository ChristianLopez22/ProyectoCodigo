import java.util.List;

public class ReporteMultas {
    private static final double MULTA_POR_DIA = 5.0;

    // Método para calcular la multa basada en los días de retraso
    public static double calcularMulta(int diasRetraso) {
        if (diasRetraso <= 0) {
            return 0.0; // No hay multa si los días de retraso son 0 o menos
        }
        return diasRetraso * MULTA_POR_DIA;
    }

    // Genera un reporte de multas de todos los materiales en préstamo
    public static void generarReporteDeMultas(List<Prestado> prestamos, int fechaActual) {
        // Validación de la lista de préstamos
        if (prestamos == null || prestamos.isEmpty()) {
            System.out.println("No hay préstamos para calcular multas.");
            return;
        }

        // Validación de la fecha actual
        if (fechaActual <= 0) {
            System.out.println("Fecha actual inválida.");
            return;
        }

        System.out.println("--- Reporte de Multas ---");

        for (Prestado prestamo : prestamos) {
            // Verificar que el objeto prestamo no sea nulo
            if (prestamo == null) {
                System.out.println("Préstamo nulo encontrado, saltando al siguiente.");
                continue;
            }

            // Verificar que el préstamo no esté marcado como regresado
            if (!prestamo.isRegresado()) {
                int diasRetraso = prestamo.calcularDiasRetraso(fechaActual);

                // Validación de que los días de retraso sean razonables
                if (diasRetraso < 0) {
                    System.out.println("Días de retraso inválidos para el producto: " + prestamo.getProducto());
                    continue;
                }

                double multa = calcularMulta(diasRetraso);

                // Solo mostrar multas si hay un valor positivo
                if (multa > 0) {
                    System.out.println("Producto: " + (prestamo.getProducto() != null ? prestamo.getProducto() : "Desconocido") +
                            " | Usuario: " + (prestamo.getUsuario() != null ? prestamo.getUsuario() : "Desconocido") +
                            " | Días de retraso: " + diasRetraso +
                            " | Multa: Q" + multa);
                }
            }
        }
    }
}
