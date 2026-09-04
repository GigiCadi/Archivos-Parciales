/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reciclaje;

/**
 *
 * @author Estudiante
 */

/*La empresa de reciclaje ABC tiene un archivo con los registros de las
empresas a las que compra o recoge material reciclado con los siguientes
datos: Código, Nombre de la empresa, Dirección, Material (cartón, hierro
o ambos) y Frecuencia de recolección (número de días al mes que se hace
recolección). Este año ha decidido aliarse con otra empresa que recicla
sólo plástico, la cual tiene un segundo archivo con los datos de las
empresas que reciclan plástico: Código, Nombre de la empresa y
Dirección. Le han solicitado agregar estas nuevas empresas al archivo
original de la empresa ABC, teniendo en cuenta que el material a
reciclar es plástico y eliminar aquellas empresas con una frecuencia de
recolección menor a 4 días.
 */
import java.io.*;
import java.util.Scanner;

public class Reciclaje {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       RECICLAJE ABC - ACTUALIZACIÓN DE EMPRESAS");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar archivo de empresas");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarEmpresas();
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

    // ================================================================
    // ACTUALIZAR EMPRESAS
    // ================================================================
    public static void actualizarEmpresas() {
        
        try {
            // Abrir archivos
            BufferedReader brABC = new BufferedReader(new FileReader("abc.txt"));
            BufferedReader brPlastico = new BufferedReader(new FileReader("plastico.txt"));
            PrintWriter pwABCNew = new PrintWriter(new FileWriter("abc_actualizado.txt"));
            
            String lineaABC;
            String lineaPlastico;
            
            int totalABC = 0;
            int totalEliminados = 0;
            int totalMantenidos = 0;
            int totalPlasticos = 0;
            
            System.out.println("\n--- PROCESANDO EMPRESAS ABC ---");
            
            // Procesar empresas ABC (frecuencia >= 4)
            while ((lineaABC = brABC.readLine()) != null) {
                String[] abcData = lineaABC.split("\t");
                
                int codigo = Integer.parseInt(abcData[0]);
                String nombre = abcData[1];
                String direccion = abcData[2];
                String material = abcData[3];
                int frecuencia = Integer.parseInt(abcData[4]);
                
                totalABC++;
                
                System.out.println("\n  Empresa: " + nombre + " (Código: " + codigo + ")");
                System.out.println("    Material: " + material + ", Frecuencia: " + frecuencia + " días");
                
                if (frecuencia >= 4) {
                    // Mantener la empresa
                    pwABCNew.println(codigo + "\t" + nombre + "\t" + direccion + "\t" + material + "\t" + frecuencia);
                    totalMantenidos++;
                    System.out.println("     MANTENIDA (frecuencia >= 4)");
                } else {
                    // Eliminar la empresa
                    totalEliminados++;
                    System.out.println("     ELIMINADA (frecuencia < 4)");
                }
            }
            
            System.out.println("\n--- AGREGANDO EMPRESAS DE PLÁSTICO ---");
            
            // Agregar empresas de plástico
            while ((lineaPlastico = brPlastico.readLine()) != null) {
                String[] plasticoData = lineaPlastico.split("\t");
                
                int codigoP = Integer.parseInt(plasticoData[0]);
                String nombreP = plasticoData[1];
                String direccionP = plasticoData[2];
                
                totalPlasticos++;
                
                pwABCNew.println(codigoP + "\t" + nombreP + "\t" + direccionP + "\t" + "Plastico" + "\t" + 0);
                System.out.println("   Agregada: " + nombreP + " (Código: " + codigoP + ") - Material: Plastico, Frecuencia: 0");
            }
            
            brABC.close();
            brPlastico.close();
            pwABCNew.close();
            
            // Mostrar resultados
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Empresas ABC originales: " + totalABC);
            System.out.println("  - Mantenidas (frecuencia >= 4): " + totalMantenidos);
            System.out.println("  - Eliminadas (frecuencia < 4): " + totalEliminados);
            System.out.println("Empresas de plástico agregadas: " + totalPlasticos);
            System.out.println("Total de empresas en el nuevo archivo: " + (totalMantenidos + totalPlasticos));
            System.out.println("Archivo generado: abc_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}