/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package transito;

/**
 *
 * @author Estudiante
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Transito {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       TRANSITO - ACTUALIZACIÓN DE MULTAS");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar multas");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarMultas();
                    break;
                case 2:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opc != 2);
        
        leer.close();
    }

    // ================================================================
    // ACTUALIZAR MULTAS
    // ================================================================
    public static void actualizarMultas() {
        
        try {
            // Leer multas registradas
            BufferedReader brMultas = new BufferedReader(new FileReader("multas_registradas.txt"));
            
            ArrayList<String> placas = new ArrayList<>();
            ArrayList<String> conductores = new ArrayList<>();
            ArrayList<String> infracciones = new ArrayList<>();
            ArrayList<Integer> fechasMulta = new ArrayList<>();
            ArrayList<Double> montos = new ArrayList<>();
            ArrayList<String> estados = new ArrayList<>();
            ArrayList<String> alertas = new ArrayList<>();
            
            String lineaMulta;
            
            while ((lineaMulta = brMultas.readLine()) != null) {
                String[] data = lineaMulta.split("\t");
                placas.add(data[0]);
                conductores.add(data[1]);
                infracciones.add(data[2]);
                fechasMulta.add(Integer.parseInt(data[3]));
                montos.add(Double.parseDouble(data[4]));
                estados.add(data[5]);
                alertas.add(data.length > 6 ? data[6] : "");
            }
            brMultas.close();
            
            int fechaActual = 20250915;
            int totalMultas = placas.size();
            int pagadas = 0;
            int parciales = 0;
            int vencidas = 0;
            
            System.out.println("\n--- PROCESANDO MULTAS ---");
            
            for (int i = 0; i < totalMultas; i++) {
                System.out.println("\n  Multa: " + placas.get(i) + " - " + infracciones.get(i));
                System.out.println("    Monto: $" + montos.get(i) + ", Estado: " + estados.get(i));
                
                double totalPagado = 0;
                boolean vencida = false;
                
                // Leer pagos de esta multa
                BufferedReader brPagos = new BufferedReader(new FileReader("pagos_multas.txt"));
                String lineaPago;
                
                while ((lineaPago = brPagos.readLine()) != null) {
                    String[] pagoData = lineaPago.split("\t");
                    String placaPago = pagoData[0];
                    int fechaPago = Integer.parseInt(pagoData[1]);
                    double montoPagado = Double.parseDouble(pagoData[2]);
                    
                    if (placaPago.equals(placas.get(i))) {
                        totalPagado += montoPagado;
                        
                        // Verificar si la multa está vencida (más de 90 días sin pago)
                        if (fechaActual - fechaPago > 90) {
                            vencida = true;
                        }
                    }
                }
                brPagos.close();
                
                // Actualizar estado y monto
                if (totalPagado >= montos.get(i)) {
                    estados.set(i, "Pagada");
                    montos.set(i, 0.0);
                    pagadas++;
                    System.out.println("    Multa PAGADA completamente");
                } else if (totalPagado > 0) {
                    estados.set(i, "Pendiente");
                    montos.set(i, montos.get(i) - totalPagado);
                    parciales++;
                    System.out.println("    Multa pagada parcialmente. Restante: $" + montos.get(i));
                }
                
                // Verificar alerta de multa vencida
                if (vencida) {
                    alertas.set(i, "Multa Vencida");
                    vencidas++;
                    System.out.println("    Alerta: Multa Vencida (mas de 90 dias sin pago)");
                }
            }
            
            // Eliminar pagos completamente pagados
            // Escribir nuevo archivo de multas
            PrintWriter pwMultasNew = new PrintWriter(new FileWriter("multas_actualizado.txt"));
            
            System.out.println("\n--- MULTAS ACTUALIZADAS ---");
            for (int i = 0; i < totalMultas; i++) {
                String alerta = alertas.get(i).isEmpty() ? "" : alertas.get(i);
                pwMultasNew.println(placas.get(i) + "\t" + conductores.get(i) + "\t" + 
                                   infracciones.get(i) + "\t" + fechasMulta.get(i) + "\t" + 
                                   montos.get(i) + "\t" + estados.get(i) + "\t" + alerta);
                System.out.println(placas.get(i) + "\t" + infracciones.get(i) + 
                                 "\t$" + montos.get(i) + "\t" + estados.get(i) + 
                                 (alerta.isEmpty() ? "" : "\t " + alerta));
            }
            pwMultasNew.close();
            
            // Escribir pagos actualizados (eliminar pagos completos)
            BufferedReader brPagos2 = new BufferedReader(new FileReader("pagos_multas.txt"));
            PrintWriter pwPagosNew = new PrintWriter(new FileWriter("pagos_actualizado.txt"));
            
            String lineaPago2;
            int pagosEliminados = 0;
            
            while ((lineaPago2 = brPagos2.readLine()) != null) {
                String[] pagoData = lineaPago2.split("\t");
                String placaPago = pagoData[0];
                double montoPagado = Double.parseDouble(pagoData[2]);
                
                // Verificar si la multa ya fue pagada completamente
                boolean pagadaCompleta = false;
                for (int i = 0; i < totalMultas; i++) {
                    if (placas.get(i).equals(placaPago) && estados.get(i).equals("Pagada")) {
                        pagadaCompleta = true;
                        break;
                    }
                }
                
                if (!pagadaCompleta) {
                    pwPagosNew.println(lineaPago2);
                } else {
                    pagosEliminados++;
                }
            }
            brPagos2.close();
            pwPagosNew.close();
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de multas procesadas: " + totalMultas);
            System.out.println("  - Multas pagadas completamente: " + pagadas);
            System.out.println("  - Multas con pagos parciales: " + parciales);
            System.out.println("  - Multas vencidas (alerta): " + vencidas);
            System.out.println("Pagos eliminados (multas completas): " + pagosEliminados);
            System.out.println("Archivo generado: multas_actualizado.txt");
            System.out.println("Archivo generado: pagos_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}