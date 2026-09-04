/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package supermercados;

/*La cadena de supermercados ABC ha decidido incorporar los clientes de la tienda MercaYA, una de sus tiendas aliadas en Colombia, siempre y cuando estén registrados con ABC. La empresa ABC tiene un archivo con los datos de sus clientes: Cédula, Nombre, Ciudad, Correo, Fecha de la última compra y Puntos acumulados. La tienda MercaYA tiene un archivo con los mismos datos para sus clientes. 
La empresa ABC le ha solicitado actualizar su archivo de Clientes de la siguiente manera: 
•	Si la fecha de la última compra en ABC fue hace más de un año se debe eliminar.
•	Si el cliente de ABC también está registrado en MercaYA, entonces se deben sumar sus puntos acumulados.
 */
/**
 *
 * @author Acer
 */
import java.io.*;
import java.util.Scanner;

public class Supermercados {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;

        do {
            System.out.println("\n===========================================================");
            System.out.println("     SUPERMERCADOS ABC - ACTUALIZACION DE CLIENTES");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar clientes ABC (con MercaYA)");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarClientes();
                    break;
                case 2:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opc != 2);

        leer.close();
    }

    public static void actualizarClientes() {
        try {
            BufferedReader brABC = new BufferedReader(new FileReader("clientes_abc.txt"));
            PrintWriter pwABCNew = new PrintWriter(new FileWriter("clientes_abc_actualizado.txt"));
            
            int fechaActual = 20240312;
            String lineaABC;

            int totalEliminados = 0;
            int totalMantenidos = 0;
            int totalSumados = 0;

            System.out.println("\n --- PROCESANDO CLIENTES ABC ---");

            while ((lineaABC = brABC.readLine()) != null) {
                String[] abcData = lineaABC.split("\t");

                String cedula = abcData[0];
                String nombre = abcData[1];
                String ciudad = abcData[2];
                String correo = abcData[3];
                int fechaCompra = Integer.parseInt(abcData[4]);
                double puntos = Double.parseDouble(abcData[5]);

                int diferencia = fechaActual - fechaCompra;

                System.out.println("\n Cliente: " + nombre + " (" + cedula + ")");
                System.out.println(" Última compra: " + fechaCompra + "(diás sin comprar: " + diferencia + ")");

                if (diferencia < 365) {
                    BufferedReader brMercaYA = new BufferedReader(new FileReader("clientes_mercaya.txt"));
                    String lineaMY;
                    boolean encontrado = false;
                    double puntosMY = 0;

                    while((lineaMY = brMercaYA.readLine()) != null) {
                        String[] myData = lineaMY.split("\t");
                        String cedulaMY = myData[0];

                        if (cedulaMY.equals(cedula)) {
                            puntosMY = Double.parseDouble(myData[5]);
                            encontrado = true;
                            break;
                        }
                    }
                    brMercaYA.close();

                    double puntosNuevos = puntos;
                    if (encontrado) {
                        puntosNuevos = puntos + puntosMY;
                        totalSumados++;
                        System.out.println("Encontrado en MercaYA: " + puntosMY + " puntos");
                    } else {
                        System.out.println("No encontrado en MercaYA");
                    }
                    pwABCNew.println(cedula + "\t" + nombre + "\t" + ciudad + "\t" + correo + "\t" + fechaCompra + "\t" + puntosNuevos);
                    totalMantenidos++;
                    System.out.println("Cliente mantenido (nuevos puntos: " + puntosNuevos + ")");
                } else {
                    totalEliminados++;
                    System.out.println("Cliente ELIMINADO");//Más de un año sin comprar
                }
            }
            brABC.close();
            pwABCNew.close();

            // Mostrar resultados
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Clientes mantenidos: " + totalMantenidos);
            System.out.println("Clientes eliminados: " + totalEliminados);
            System.out.println("Clientes con puntos sumados (MercaYA): " + totalSumados);
            System.out.println("Archivo generado: clientes_abc_actualizado.txt");
            System.out.println("===========================================================");

        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
            
        }
    }
}
