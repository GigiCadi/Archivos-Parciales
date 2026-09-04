/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clinica1;

/**
 *
 * @author Estudiante
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Clinica1 {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       CLINICA ODONTOLOGICA - ACTUALIZACION DE PACIENTES");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar paciente");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarPaciente(leer);
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
    // ACTUALIZAR PACIENTE
    // ================================================================
    public static void actualizarPaciente(Scanner leer) {
        
        try {
            // Leer archivo actual
            BufferedReader brPacientes = new BufferedReader(new FileReader("pacientes.txt"));
            
            ArrayList<String> cedulas = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<String> telefonos = new ArrayList<>();
            ArrayList<String> tratamientos = new ArrayList<>();
            ArrayList<Integer> fechas = new ArrayList<>();
            
            String linea;
            
            while ((linea = brPacientes.readLine()) != null) {
                String[] data = linea.split("\t");
                cedulas.add(data[0]);
                nombres.add(data[1]);
                telefonos.add(data[2]);
                tratamientos.add(data[3]);
                fechas.add(Integer.parseInt(data[4]));
            }
            brPacientes.close();
            
            // Mostrar pacientes actuales
            System.out.println("\n--- PACIENTES ACTUALES ---");
            for (int i = 0; i < cedulas.size(); i++) {
                System.out.println(cedulas.get(i) + "\t" + nombres.get(i) + "\t" + 
                                 telefonos.get(i) + "\t" + tratamientos.get(i) + "\t" + fechas.get(i));
            }
            
            // Solicitar cédula del paciente
            System.out.print("\nIngrese la cedula del paciente: ");
            String cedulaBuscar = leer.nextLine();
            
            boolean encontrado = false;
            
            // Buscar si el paciente existe
            for (int i = 0; i < cedulas.size(); i++) {
                if (cedulas.get(i).equals(cedulaBuscar)) {
                    encontrado = true;
                    
                    System.out.println("\n--- PACIENTE ENCONTRADO ---");
                    System.out.println("Nombre: " + nombres.get(i));
                    System.out.println("Telefono: " + telefonos.get(i));
                    System.out.println("Tratamiento actual: " + tratamientos.get(i));
                    System.out.println("Fecha ultima consulta: " + fechas.get(i));
                    
                    // Actualizar datos
                    System.out.print("\nDigite el nuevo tipo de tratamiento: ");
                    String nuevoTratamiento = leer.nextLine();
                    System.out.print("Digite la nueva fecha de consulta (AAAAMMDD): ");
                    int nuevaFecha = Integer.parseInt(leer.nextLine());
                    
                    tratamientos.set(i, nuevoTratamiento);
                    fechas.set(i, nuevaFecha);
                    
                    System.out.println(" Paciente actualizado correctamente.");
                    break;
                }
            }
            
            if (!encontrado) {
                System.out.println("\n Paciente no encontrado. Se procedera a registrarlo.");
                
                // Insertar nuevo paciente (ordenado por cédula)
                System.out.print("Nombre: ");
                String nombre = leer.nextLine();
                System.out.print("Telefono: ");
                String telefono = leer.nextLine();
                System.out.print("Tipo de tratamiento: ");
                String tratamiento = leer.nextLine();
                System.out.print("Fecha de consulta (AAAAMMDD): ");
                int fecha = Integer.parseInt(leer.nextLine());
                
                // Buscar posición de inserción (orden por cédula)
                int posInsert = 0;
                for (int i = 0; i < cedulas.size(); i++) {
                    if (cedulaBuscar.compareTo(cedulas.get(i)) < 0) {
                        posInsert = i;
                        break;
                    }
                    posInsert = i + 1;
                }
                
                cedulas.add(posInsert, cedulaBuscar);
                nombres.add(posInsert, nombre);
                telefonos.add(posInsert, telefono);
                tratamientos.add(posInsert, tratamiento);
                fechas.add(posInsert, fecha);
                
                System.out.println(" Nuevo paciente registrado correctamente.");
            }
            
            // Escribir archivo actualizado
            PrintWriter pwPacientesNew = new PrintWriter(new FileWriter("pacientes_actualizado.txt"));
            
            System.out.println("\n--- PACIENTES ACTUALIZADOS (ordenados por cedula) ---");
            for (int i = 0; i < cedulas.size(); i++) {
                pwPacientesNew.println(cedulas.get(i) + "\t" + nombres.get(i) + "\t" + 
                                      telefonos.get(i) + "\t" + tratamientos.get(i) + "\t" + fechas.get(i));
                System.out.println(cedulas.get(i) + "\t" + nombres.get(i) + "\t" + 
                                 telefonos.get(i) + "\t" + tratamientos.get(i) + "\t" + fechas.get(i));
            }
            pwPacientesNew.close();
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Archivo generado: pacientes_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}