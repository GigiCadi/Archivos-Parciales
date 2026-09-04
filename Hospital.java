/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hospital;

/**
 *
 * @author Estudiante
 */

/**
 * Un hospital mantiene un archivo llamado Pacientes_Registrados, que almacena 
 * información sobre los pacientes atendidos en años anteriores. Este archivo 
 * contiene los siguientes campos: ID del paciente, Nombre, Fecha de nacimiento,
 * Tipo de sangre, Fecha de última consulta, Estado de afiliación (Activo/Inactivo). 
 * Adicionalmente, existe un archivo Consultas_Recientes, donde se registran las
 * nuevas visitas médicas realizadas en el año actual. Este archivo contiene los
 * siguientes campos: ID del paciente, Nombre, Fecha de consulta, Diagnóstico,
 * Tratamiento recomendado.
Se solicita un programa que:
1.	Actualice la fecha de última consulta en el archivo Pacientes_Registrados
* si el paciente aparece en Consultas_Recientes.
2.	Si un paciente tiene más de 3 años sin consultas, cambiar su Estado de 
* afiliación a Inactivo.
3.	Si un paciente con estado Inactivo registra una nueva consulta, cambiarlo a Activo.

 */
import java.io.*;
import java.util.Scanner;

public class Hospital {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       HOSPITAL - ACTUALIZACIÓN DE PACIENTES");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar pacientes");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarPacientes();
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
    // ACTUALIZAR PACIENTES
    // ================================================================
    public static void actualizarPacientes() {
        
        try {
            // Abrir archivos
            BufferedReader brPacientes = new BufferedReader(new FileReader("pacientes_registrados.txt"));
            PrintWriter pwPacientesNew = new PrintWriter(new FileWriter("pacientes_actualizado.txt"));
            
            String lineaPaciente;
            int fechaActual = 20250915;
            
            int totalPacientes = 0;
            int actualizados = 0;
            int inactivos = 0;
            int reactivados = 0;
            
            System.out.println("\n--- PROCESANDO PACIENTES ---");
            
            // MQ (Not(EOF(Pacientes_Registrados)))
            while ((lineaPaciente = brPacientes.readLine()) != null) {
                String[] pacienteData = lineaPaciente.split("\t");
                
                int id = Integer.parseInt(pacienteData[0]);
                String nombre = pacienteData[1];
                int fechaNacimiento = Integer.parseInt(pacienteData[2]);
                String tipoSangre = pacienteData[3];
                int fechaUltCons = Integer.parseInt(pacienteData[4]);
                String estado = pacienteData[5];
                
                totalPacientes++;
                
                System.out.println("\n  Paciente: " + nombre + " (ID: " + id + ")");
                System.out.println("    Estado actual: " + estado + ", Ultima consulta: " + fechaUltCons);
                
                String estadoNuevo = estado;
                int fechaUltConsNueva = fechaUltCons;
                boolean aparecio = false;
                
                // Buscar en consultas recientes
                BufferedReader brConsultas = new BufferedReader(new FileReader("consultas_recientes.txt"));
                String lineaConsulta;
                
                while ((lineaConsulta = brConsultas.readLine()) != null) {
                    String[] consultaData = lineaConsulta.split("\t");
                    int idConsulta = Integer.parseInt(consultaData[0]);
                    int fechaConsulta = Integer.parseInt(consultaData[2]);
                    
                    if (idConsulta == id) {
                        aparecio = true;
                        fechaUltConsNueva = fechaConsulta;
                        
                        // Si estaba inactivo, reactivarlo
                        if (estado.equalsIgnoreCase("Inactivo")) {
                            estadoNuevo = "Activo";
                            reactivados++;
                            System.out.println("    Reactivado (consulta reciente)");
                        } else {
                            estadoNuevo = "Activo";
                        }
                        System.out.println("     Nueva fecha de consulta: " + fechaConsulta);
                        break;
                    }
                }
                brConsultas.close();
                
                // Si no apareció en consultas recientes
                if (!aparecio) {
                    // Calcular años sin consulta
                    int diasSinConsulta = fechaActual - fechaUltCons;
                    int aniosSinConsulta = diasSinConsulta / 365;
                    
                    System.out.println("     Anos sin consulta: " + aniosSinConsulta);
                    
                    if (aniosSinConsulta >= 3) {
                        estadoNuevo = "Inactivo";
                        inactivos++;
                        System.out.println("     Cambiado a Inactivo (3+ años sin consulta)");
                    } else {
                        estadoNuevo = estado;
                    }
                }
                
                // Verificar si hubo cambios
                if (!estado.equals(estadoNuevo) || fechaUltCons != fechaUltConsNueva) {
                    actualizados++;
                }
                
                // Escribir en el nuevo archivo
                pwPacientesNew.println(id + "\t" + nombre + "\t" + fechaNacimiento + "\t" + 
                                      tipoSangre + "\t" + fechaUltConsNueva + "\t" + estadoNuevo);
            }
            
            brPacientes.close();
            pwPacientesNew.close();
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de pacientes procesados: " + totalPacientes);
            System.out.println("Pacientes actualizados: " + actualizados);
            System.out.println("  - Pacientes inactivados: " + inactivos);
            System.out.println("  - Pacientes reactivados: " + reactivados);
            System.out.println("Archivo generado: pacientes_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}