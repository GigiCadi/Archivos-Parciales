/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bancoabc;

/**
 *
 * @author Estudiante
 */

/**
 * El banco ABC está haciendo una campaña para ofrecer una tarjeta de crédito a 
 * sus clientes. Para esto, el banco cuenta con 2 archivos. Un primer archivo con
 * la información de los clientes que tienen una cuenta de ahorros en el banco, 
 * con los siguientes campos: cédula, nombre, teléfono, sucursal, fecha de apertura
 * y fecha de último movimiento. El segundo archivo tiene la información de los créditos,
 * con los siguientes campos: número de crédito, tipo de crédito (tarjeta, libre inversión,
 * hipotecario, etc), cédula, nombre del cliente y estado del crédito (al día o moroso).
Los clientes a los que se les va a ofrecer la tarjeta de crédito deben tener los siguientes
* requisitos: haber tenido algún movimiento en su cuenta en los últimos 3 meses, 
* no tener ningún tipo de crédito o si tiene alguno (o varios) estar al día en todos.
Se debe generar un nuevo archivo con la información de los clientes a los que les va
* a ofrecer la tarjeta de crédito (cédula, nombre, teléfono y cantidad de créditos 
* con el banco (0, 1 o varios).

 */
import java.io.*;
import java.util.Scanner;

public class BancoABC {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       BANCO ABC - OFERTA TARJETA DE CREDITO");
            System.out.println("===========================================================");
            System.out.println("1. Generar lista de clientes para tarjeta");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    generarOferta();
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
    // GENERAR OFERTA DE TARJETA
    // ================================================================
    public static void generarOferta() {
        
        try {
            // Abrir archivos
            BufferedReader brAhorros = new BufferedReader(new FileReader("ahorros.txt"));
            PrintWriter pwTarjeta = new PrintWriter(new FileWriter("tarjeta_oferta.txt"));
            
            String lineaAhorro;
            int fechaActual = 20240915;
            
            int totalClientes = 0;
            int aptos = 0;
            int noAptosMovimiento = 0;
            int noAptosCredito = 0;
            
            System.out.println("\n--- PROCESANDO CLIENTES ---");
            
            // MQ (Not(EOF(AHORROS)))
            while ((lineaAhorro = brAhorros.readLine()) != null) {
                String[] ahorroData = lineaAhorro.split("\t");
                
                String cedula = ahorroData[0];
                String nombre = ahorroData[1];
                String telefono = ahorroData[2];
                String sucursal = ahorroData[3];
                int fechaApertura = Integer.parseInt(ahorroData[4]);
                int fechaUltMov = Integer.parseInt(ahorroData[5]);
                
                totalClientes++;
                
                System.out.println("\n  Cliente: " + nombre + " (Cedula: " + cedula + ")");
                System.out.println("    Ultimo movimiento: " + fechaUltMov);
                
                boolean apto = true;
                int cantidadCreditos = 0;
                
                // Verificar movimiento en últimos 3 meses (90 días)
                int diasSinMovimiento = fechaActual - fechaUltMov;
                System.out.println("    Dias sin movimiento: " + diasSinMovimiento);
                
                if (diasSinMovimiento > 90) {
                    apto = false;
                    noAptosMovimiento++;
                    System.out.println("    NO APTO: Sin movimiento en ultimos 3 meses");
                } else {
                    System.out.println("    Movimiento reciente");
                    
                    // Verificar créditos
                    BufferedReader brCreditos = new BufferedReader(new FileReader("creditos.txt"));
                    String lineaCredito;
                    
                    while ((lineaCredito = brCreditos.readLine()) != null) {
                        String[] creditoData = lineaCredito.split("\t");
                        String cedulaCred = creditoData[2];
                        String estado = creditoData[4];
                        
                        if (cedulaCred.equals(cedula)) {
                            if (estado.equalsIgnoreCase("al dia")) {
                                cantidadCreditos++;
                            } else {
                                apto = false;
                                System.out.println("    NO APTO: Credito moroso encontrado");
                                break;
                            }
                        }
                    }
                    brCreditos.close();
                }
                
                if (apto) {
                    aptos++;
                    System.out.println("     APTO para tarjeta de credito (Creditos: " + cantidadCreditos + ")");
                    
                    // Escribir en el archivo de oferta
                    pwTarjeta.println(cedula + "\t" + nombre + "\t" + telefono + "\t" + cantidadCreditos);
                } else {
                    if (diasSinMovimiento <= 90) {
                        noAptosCredito++;
                    }
                }
            }
            
            brAhorros.close();
            pwTarjeta.close();
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de clientes procesados: " + totalClientes);
            System.out.println("  - Clientes aptos para tarjeta: " + aptos);
            System.out.println("  - No aptos (sin movimiento): " + noAptosMovimiento);
            System.out.println("  - No aptos (credito moroso): " + noAptosCredito);
            System.out.println("Archivo generado: tarjeta_oferta.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}