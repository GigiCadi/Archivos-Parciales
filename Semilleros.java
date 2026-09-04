/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package semilleros;

/**
 *
 * @author Estudiante
 */

/*1. (2.0) Usted está encargado de apoyar la actualización tecnológica de
un colegio. Se le pide diseñar un programa que permita validar si un
estudiante es apto para inscribirse en algún semillero deportivo.
Para poder inscribirse a un determinado semillero debe cumplir con
un promedio mínimo exigido por cada semillero.

Las consultas que debe hacer son:
- Solicitar el código del estudiante y mostrar en pantalla a qué
  semilleros puede inscribirse si cumple con el promedio mínimo.
- Solicitar el nombre del semillero y mostrar los requisitos de
  inscripción.

Para esto se cuenta con 2 archivos:
Un archivo con los datos de los estudiantes: Código del estudiante,
Nombre del estudiante, edad, curso y nota promedio.
Un segundo archivo con los datos del semillero: Nombre del semillero,
profesor encargado, horario y promedio mínimo para inscribirse.
 */
import java.io.*;
import java.util.Scanner;

public class Semilleros {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       SEMILLEROS DEPORTIVOS - SISTEMA DE INSCRIPCION");
            System.out.println("===========================================================");
            System.out.println("1. Validar estudiante para semilleros");
            System.out.println("2. Mostrar requisitos de un semillero");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    validarEstudiante(leer);
                    break;
                case 2:
                    mostrarRequisitosSemillero(leer);
                    break;
                case 3:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opc != 3);
        
        leer.close();
    }

    // ================================================================
    // OPCIÓN 1: VALIDAR ESTUDIANTE PARA SEMILLEROS
    // ================================================================
    public static void validarEstudiante(Scanner leer) {
        
        System.out.print("Digite el codigo del estudiante: ");
        String codigoBuscar = leer.nextLine();
        
        try {
            // Abrir archivo de estudiantes
            BufferedReader brEstudiantes = new BufferedReader(new FileReader("estudiantes.txt"));
            
            String lineaEstudiante;
            boolean estudianteEncontrado = false;
            
            while ((lineaEstudiante = brEstudiantes.readLine()) != null) {
                String[] estudianteData = lineaEstudiante.split("\t");
                
                String codigo = estudianteData[0];
                String nombre = estudianteData[1];
                int edad = Integer.parseInt(estudianteData[2]);
                int curso = Integer.parseInt(estudianteData[3]);
                double notaPromedio = Double.parseDouble(estudianteData[4]);
                
                // Si encontramos el estudiante
                if (codigo.equals(codigoBuscar)) {
                    estudianteEncontrado = true;
                    
                    System.out.println("\n--- DATOS DEL ESTUDIANTE ---");
                    System.out.println("Codigo: " + codigo);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Edad: " + edad);
                    System.out.println("Curso: " + curso);
                    System.out.println("Nota promedio: " + notaPromedio);
                    
                    System.out.println("\n--- SEMILLEROS A LOS QUE PUEDE INSCRIBIRSE ---");
                    
                    // Buscar semilleros donde cumple el promedio mínimo
                    BufferedReader brSemilleros = new BufferedReader(new FileReader("semilleros.txt"));
                    String lineaSemillero;
                    boolean haySemilleros = false;
                    
                    while ((lineaSemillero = brSemilleros.readLine()) != null) {
                        String[] semilleroData = lineaSemillero.split("\t");
                        
                        String nombreSemillero = semilleroData[0];
                        String profesor = semilleroData[1];
                        String horario = semilleroData[2];
                        double promedioMinimo = Double.parseDouble(semilleroData[3]);
                        
                        // Si el estudiante cumple el promedio mínimo
                        if (notaPromedio >= promedioMinimo) {
                            System.out.println(nombreSemillero);
                            System.out.println("     Profesor: " + profesor);
                            System.out.println("     Horario: " + horario);
                            System.out.println("     Promedio minimo: " + promedioMinimo);
                            System.out.println("");
                            haySemilleros = true;
                        }
                    }
                    brSemilleros.close();
                    
                    if (!haySemilleros) {
                        System.out.println("  No cumple con el promedio mínimo para ningún semillero.");
                    }
                    
                    break;
                }
            }
            brEstudiantes.close();
            
            if (!estudianteEncontrado) {
                System.out.println("Estudiante no encontrado.");
            }
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ================================================================
    // OPCIÓN 2: MOSTRAR REQUISITOS DE UN SEMILLERO
    // ================================================================
    public static void mostrarRequisitosSemillero(Scanner leer) {
        
        System.out.print("Digite el nombre del semillero: ");
        String nombreSemillero = leer.nextLine();
        
        try {
            BufferedReader brSemilleros = new BufferedReader(new FileReader("semilleros.txt"));
            
            String lineaSemillero;
            boolean semilleroEncontrado = false;
            
            while ((lineaSemillero = brSemilleros.readLine()) != null) {
                String[] semilleroData = lineaSemillero.split("\t");
                
                String nombre = semilleroData[0];
                String profesor = semilleroData[1];
                String horario = semilleroData[2];
                double promedioMinimo = Double.parseDouble(semilleroData[3]);
                
                // Si encontramos el semillero
                if (nombre.equalsIgnoreCase(nombreSemillero)) {
                    semilleroEncontrado = true;
                    
                    System.out.println("\n--- REQUISITOS DEL SEMILLERO ---");
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Profesor encargado: " + profesor);
                    System.out.println("Horario: " + horario);
                    System.out.println("Promedio minimo requerido: " + promedioMinimo);
                    System.out.println("===========================================================");
                    break;
                }
            }
            brSemilleros.close();
            
            if (!semilleroEncontrado) {
                System.out.println("Semillero no encontrado.");
            }
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}