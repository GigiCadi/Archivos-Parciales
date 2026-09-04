/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vita;

/**
 *
 * @author Estudiante
 */

/*La compañía de seguros Vita desea actualizar su archivo de empleados.
Actualmente contiene los siguientes datos ordenados por el campo Nombre:
Cédula, Nombre, Correo, Salario, Cargo (junior, senior o coordinador) y
Fecha de ingreso. Se le ha solicitado crear un programa que realice lo
siguiente:
- Ingresar los datos de un nuevo empleado solicitados al usuario, para
  que el registro quede ordenado por el campo Nombre.
- Actualizar el salario de la siguiente manera: si tiene más de 5 años
  de trabajo y su cargo es junior, pasa a ser senior con un aumento del
  20% de su salario; si tiene más de 10 y su cargo es senior, pasa a ser
  coordinador con un aumento del 10% de su salario.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Vita {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("         VITA - ACTUALIZACIÓN DE EMPLEADOS");
            System.out.println("===========================================================");
            System.out.println("1. Insertar nuevo empleado y actualizar salarios");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    insertarYActualizar(leer);
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
    // INSERTAR NUEVO EMPLEADO Y ACTUALIZAR SALARIOS
    // ================================================================
    public static void insertarYActualizar(Scanner leer) {
        
        try {
            // ============================================================
            // LEER TODOS LOS EMPLEADOS ACTUALES
            // ============================================================
            BufferedReader brVita = new BufferedReader(new FileReader("empleados_vita.txt"));
            
            ArrayList<String> cedulas = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<String> correos = new ArrayList<>();
            ArrayList<Double> salarios = new ArrayList<>();
            ArrayList<String> cargos = new ArrayList<>();
            ArrayList<Integer> fechasIngreso = new ArrayList<>();
            
            String linea;
            
            while ((linea = brVita.readLine()) != null) {
                String[] data = linea.split("\t");
                cedulas.add(data[0]);
                nombres.add(data[1]);
                correos.add(data[2]);
                salarios.add(Double.parseDouble(data[3]));
                cargos.add(data[4]);
                fechasIngreso.add(Integer.parseInt(data[5]));
            }
            brVita.close();
            
            // ============================================================
            // MOSTRAR EMPLEADOS ACTUALES
            // ============================================================
            System.out.println("\n--- EMPLEADOS ACTUALES (ordenados por nombre) ---");
            for (int i = 0; i < nombres.size(); i++) {
                System.out.println((i+1) + ". " + nombres.get(i) + "\t" + cargos.get(i) + 
                                 "\t$" + salarios.get(i));
            }
            
            // ============================================================
            // INGRESAR NUEVO EMPLEADO
            // ============================================================
            System.out.println("\n--- INGRESE DATOS DEL NUEVO EMPLEADO ---");
            System.out.print("Cédula: ");
            String cedulaN = leer.nextLine();
            System.out.print("Nombre: ");
            String nombreN = leer.nextLine();
            System.out.print("Correo: ");
            String correoN = leer.nextLine();
            System.out.print("Salario: ");
            double salarioN = Double.parseDouble(leer.nextLine());
            System.out.print("Cargo (junior/senior/coordinador): ");
            String cargoN = leer.nextLine();
            System.out.print("Fecha de ingreso (AAAAMMDD): ");
            int fechaN = Integer.parseInt(leer.nextLine());
            
            // ============================================================
            // BUSCAR POSICIÓN DE INSERCIÓN (orden por nombre)
            // ============================================================
            int pos = 0;
            for (int i = 0; i < nombres.size(); i++) {
                if (nombreN.compareTo(nombres.get(i)) < 0) {
                    pos = i;
                    break;
                }
                pos = i + 1;
            }
            
            System.out.println("\n  Insertando en posición: " + pos);
            
            // ============================================================
            // INSERTAR EN LAS LISTAS
            // ============================================================
            cedulas.add(pos, cedulaN);
            nombres.add(pos, nombreN);
            correos.add(pos, correoN);
            salarios.add(pos, salarioN);
            cargos.add(pos, cargoN);
            fechasIngreso.add(pos, fechaN);
            
            // ============================================================
            // ACTUALIZAR SALARIOS Y CARGOS
            // ============================================================
            int fechaActual = 20240312;
            int actualizados = 0;
            
            System.out.println("\n--- ACTUALIZANDO SALARIOS ---");
            
            for (int i = 0; i < nombres.size(); i++) {
                String nombre = nombres.get(i);
                String cargo = cargos.get(i);
                double salario = salarios.get(i);
                int fechaIngreso = fechasIngreso.get(i);
                
                int diferencia = fechaActual - fechaIngreso;
                int anios = diferencia / 10000;
                
                boolean actualizado = false;
                
                // Si tiene más de 5 años y es junior → senior (+20%)
                if (anios > 5 && cargo.equalsIgnoreCase("junior")) {
                    salario = salario * 1.20;
                    cargos.set(i, "senior");
                    salarios.set(i, salario);
                    actualizado = true;
                    actualizados++;
                    System.out.println("  ✅ " + nombre + " → senior (+20%) - " + anios + " años");
                }
                // Si tiene más de 10 años y es senior → coordinador (+10%)
                else if (anios > 10 && cargo.equalsIgnoreCase("senior")) {
                    salario = salario * 1.10;
                    cargos.set(i, "coordinador");
                    salarios.set(i, salario);
                    actualizado = true;
                    actualizados++;
                    System.out.println("  ✅ " + nombre + " → coordinador (+10%) - " + anios + " años");
                }
                
                if (!actualizado) {
                    System.out.println("  - " + nombre + " (" + cargo + ") - " + anios + " años - sin cambios");
                }
            }
            
            // ============================================================
            // ESCRIBIR ARCHIVO ACTUALIZADO
            // ============================================================
            PrintWriter pwVitaNew = new PrintWriter(new FileWriter("empleados_vita_actualizado.txt"));
            
            for (int i = 0; i < nombres.size(); i++) {
                pwVitaNew.println(cedulas.get(i) + "\t" + nombres.get(i) + "\t" + 
                                 correos.get(i) + "\t" + salarios.get(i) + "\t" + 
                                 cargos.get(i) + "\t" + fechasIngreso.get(i));
            }
            pwVitaNew.close();
            
            // ============================================================
            // MOSTRAR RESULTADO
            // ============================================================
            System.out.println("\n--- EMPLEADOS ACTUALIZADOS (ordenados por nombre) ---");
            for (int i = 0; i < nombres.size(); i++) {
                System.out.println((i+1) + ". " + nombres.get(i) + "\t" + cargos.get(i) + 
                                 "\t$" + String.format("%.0f", salarios.get(i)));
            }
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de empleados: " + nombres.size());
            System.out.println("Empleados actualizados: " + actualizados);
            System.out.println("Nuevo empleado insertado: " + nombreN);
            System.out.println("Archivo generado: empleados_vita_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}