/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bancobaq;

/**
 *
 * @author Estudiante
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BancoBAQ {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       BANCO BARRANQUILLA - SISTEMA DE CRÉDITOS");
            System.out.println("===========================================================");
            System.out.println("1. Insertar nuevo crédito");
            System.out.println("2. Consultar créditos por cédula");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    insertarCredito(leer);
                    break;
                case 2:
                    consultarCredito(leer);
                    break;
                case 3:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opc != 3);
        
        leer.close();
    }

    // ================================================================
    // OPCIÓN 1: INSERTAR NUEVO CRÉDITO
    // ================================================================
    public static void insertarCredito(Scanner leer) {
        
        try {
            // Leer archivo actual
            BufferedReader brBanco = new BufferedReader(new FileReader("banco_baq.txt"));
            
            ArrayList<String> codigos = new ArrayList<>();
            ArrayList<String> cedulas = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<String> correos = new ArrayList<>();
            ArrayList<String> tipos = new ArrayList<>();
            ArrayList<Double> montos = new ArrayList<>();
            
            String linea;
            
            while ((linea = brBanco.readLine()) != null) {
                String[] data = linea.split("\t");
                codigos.add(data[0]);
                cedulas.add(data[1]);
                nombres.add(data[2]);
                correos.add(data[3]);
                tipos.add(data[4]);
                montos.add(Double.parseDouble(data[5]));
            }
            brBanco.close();
            
            // Mostrar créditos actuales
            System.out.println("\n--- CRÉDITOS ACTUALES ---");
            for (int i = 0; i < codigos.size(); i++) {
                System.out.println(codigos.get(i) + "\t" + cedulas.get(i) + "\t" + nombres.get(i) + 
                                 "\t" + tipos.get(i) + "\t$" + montos.get(i));
            }
            
            // ============================================================
            // INGRESAR NUEVO CRÉDITO CON VALIDACIONES
            // ============================================================
            System.out.println("\n--- INGRESE DATOS DEL NUEVO CRÉDITO ---");
            
            // Código del crédito
            System.out.print("Código del crédito: ");
            String codigoN = leer.nextLine();
            while (codigoN.isEmpty()) {
                System.out.print("El código no puede estar vacío. Ingrese nuevamente: ");
                codigoN = leer.nextLine();
            }
            
            // Cédula del cliente
            System.out.print("Cédula del cliente: ");
            String cedulaN = leer.nextLine();
            while (cedulaN.isEmpty()) {
                System.out.print("La cédula no puede estar vacía. Ingrese nuevamente: ");
                cedulaN = leer.nextLine();
            }
            
            // Nombre del cliente
            System.out.print("Nombre del cliente: ");
            String nombreN = leer.nextLine();
            while (nombreN.isEmpty()) {
                System.out.print("El nombre no puede estar vacío. Ingrese nuevamente: ");
                nombreN = leer.nextLine();
            }
            
            // Correo del cliente
            System.out.print("Correo del cliente: ");
            String correoN = leer.nextLine();
            while (correoN.isEmpty()) {
                System.out.print("El correo no puede estar vacío. Ingrese nuevamente: ");
                correoN = leer.nextLine();
            }
            
            // Tipo de crédito
            System.out.print("Tipo de crédito (hipotecario/libre destino/tarjeta de credito): ");
            String tipoN = leer.nextLine();
            while (tipoN.isEmpty()) {
                System.out.print("El tipo no puede estar vacío. Ingrese nuevamente: ");
                tipoN = leer.nextLine();
            }
            
            // Monto del crédito (con validación)
            double montoN = 0;
            boolean montoValido = false;
            while (!montoValido) {
                System.out.print("Monto del crédito: ");
                String montoStr = leer.nextLine();
                if (montoStr.isEmpty()) {
                    System.out.println(" El monto no puede estar vacío. Intente nuevamente.");
                } else {
                    try {
                        montoN = Double.parseDouble(montoStr);
                        if (montoN < 0) {
                            System.out.println(" El monto no puede ser negativo. Intente nuevamente.");
                        } else {
                            montoValido = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(" Debe ingresar un número válido. Intente nuevamente.");
                    }
                }
            }
            
            // Buscar posición de inserción (orden por cédula)
            int pos = 0;
            for (int i = 0; i < cedulas.size(); i++) {
                if (cedulaN.compareTo(cedulas.get(i)) < 0) {
                    pos = i;
                    break;
                }
                pos = i + 1;
            }
            
            System.out.println("\n   Insertando en posición: " + pos);
            
            // Insertar en las listas
            codigos.add(pos, codigoN);
            cedulas.add(pos, cedulaN);
            nombres.add(pos, nombreN);
            correos.add(pos, correoN);
            tipos.add(pos, tipoN);
            montos.add(pos, montoN);
            
            // Escribir archivo actualizado
            PrintWriter pwBancoNew = new PrintWriter(new FileWriter("banco_baq_actualizado.txt"));
            
            System.out.println("\n--- CRÉDITOS ACTUALIZADOS (ordenados por cédula) ---");
            for (int i = 0; i < codigos.size(); i++) {
                pwBancoNew.println(codigos.get(i) + "\t" + cedulas.get(i) + "\t" + nombres.get(i) + "\t" + 
                                  correos.get(i) + "\t" + tipos.get(i) + "\t" + montos.get(i));
                System.out.println(codigos.get(i) + "\t" + cedulas.get(i) + "\t" + nombres.get(i) + 
                                 "\t" + tipos.get(i) + "\t$" + montos.get(i));
            }
            pwBancoNew.close();
            
            System.out.println("\n===========================================================");
            System.out.println("CRÉDITO INSERTADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Nuevo crédito: " + codigoN + " - " + nombreN + " - " + tipoN + " - $" + montoN);
            System.out.println("Archivo generado: banco_baq_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ================================================================
    // OPCIÓN 2: CONSULTAR CRÉDITOS POR CÉDULA
    // ================================================================
    public static void consultarCredito(Scanner leer) {
        
        System.out.print("\nIngrese la cédula del cliente a consultar: ");
        String cedulaBuscar = leer.nextLine();
        
        if (cedulaBuscar.isEmpty()) {
            System.out.println("La cédula no puede estar vacía.");
            return;
        }
        
        try {
            BufferedReader brBanco = new BufferedReader(new FileReader("banco_baq.txt"));
            
            String linea;
            boolean encontrado = false;
            int totalCreditos = 0;
            double totalMonto = 0;
            
            System.out.println("\n--- CRÉDITOS DEL CLIENTE ---");
            System.out.println("Cédula: " + cedulaBuscar);
            System.out.println("===========================================================");
            
            while ((linea = brBanco.readLine()) != null) {
                String[] data = linea.split("\t");
                String cedula = data[1];
                
                if (cedula.equals(cedulaBuscar)) {
                    encontrado = true;
                    totalCreditos++;
                    String codigo = data[0];
                    String nombre = data[2];
                    String correo = data[3];
                    String tipo = data[4];
                    double monto = Double.parseDouble(data[5]);
                    totalMonto += monto;
                    
                    System.out.println("  Crédito #" + totalCreditos);
                    System.out.println("    Código: " + codigo);
                    System.out.println("    Nombre: " + nombre);
                    System.out.println("    Correo: " + correo);
                    System.out.println("    Tipo: " + tipo);
                    System.out.println("    Monto: $" + monto);
                    System.out.println("  ---------------------------");
                }
            }
            brBanco.close();
            
            if (!encontrado) {
                System.out.println("No se encontraron créditos para la cédula " + cedulaBuscar);
            } else {
                System.out.println("  Total de créditos: " + totalCreditos);
                System.out.println("  Monto total: $" + totalMonto);
                System.out.println("===========================================================");
            }
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}