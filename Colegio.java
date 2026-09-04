/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package colegio;

/**
 *
 * @author Estudiante
 */

/*2. (2.0) Un profesor le ha pedido su apoyo para actualizar un archivo
con el reporte de notas de sus alumnos. El profesor ha tomado varias
notas durante el semestre y para computar el promedio final, ha
decidido eliminar la nota más baja de cada estudiante y promediar
con las notas restantes. El archivo consiste en un listado de los
estudiantes digitado cada vez que presentaba una tarea, es decir,
los estudiantes se repiten por cada tarea.

El archivo de estudiantes tiene los siguientes datos: Código del
estudiante, Nombre, tarea, fecha de entrega y nota.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Colegio {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       COLEGIO - ACTUALIZACIÓN DE NOTAS");
            System.out.println("===========================================================");
            System.out.println("1. Eliminar nota más baja y calcular promedios");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    eliminarNotaMasBaja();
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
    // ELIMINAR NOTA MÁS BAJA Y CALCULAR PROMEDIOS
    // ================================================================
    public static void eliminarNotaMasBaja() {
        
        try {
            // Abrir archivo de notas
            BufferedReader brNotas = new BufferedReader(new FileReader("notas.txt"));
            
            // Listas para agrupar por estudiante
            ArrayList<String> codigos = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<ArrayList<String>> tareas = new ArrayList<>();
            ArrayList<ArrayList<Double>> notas = new ArrayList<>();
            
            String lineaNota;
            
            System.out.println("\n--- LEYENDO NOTAS ---");
            
            while ((lineaNota = brNotas.readLine()) != null) {
                String[] notaData = lineaNota.split("\t");
                
                String codigo = notaData[0];
                String nombre = notaData[1];
                String tarea = notaData[2];
                String fecha = notaData[3];
                double nota = Double.parseDouble(notaData[4]);
                
                // Buscar si el estudiante ya existe en las listas
                int posicion = codigos.indexOf(codigo);
                
                if (posicion == -1) {
                    // Nuevo estudiante
                    codigos.add(codigo);
                    nombres.add(nombre);
                    
                    ArrayList<String> tareasEstudiante = new ArrayList<>();
                    tareasEstudiante.add(tarea + " (" + fecha + ")");
                    tareas.add(tareasEstudiante);
                    
                    ArrayList<Double> notasEstudiante = new ArrayList<>();
                    notasEstudiante.add(nota);
                    notas.add(notasEstudiante);
                } else {
                    // Estudiante ya existe
                    tareas.get(posicion).add(tarea + " (" + fecha + ")");
                    notas.get(posicion).add(nota);
                }
            }
            brNotas.close();
            
            // ============================================================
            // PROCESAR CADA ESTUDIANTE
            // ============================================================
            System.out.println("\n--- PROCESANDO ESTUDIANTES ---");
            
            PrintWriter pwNotasNew = new PrintWriter(new FileWriter("notas_actualizado.txt"));
            PrintWriter pwPromedios = new PrintWriter(new FileWriter("promedios_finales.txt"));
            
            pwPromedios.println("CODIGO\tNOMBRE\tNOTA_BAJA\tPROMEDIO_FINAL\tTAREAS");
            
            for (int i = 0; i < codigos.size(); i++) {
                String codigo = codigos.get(i);
                String nombre = nombres.get(i);
                ArrayList<String> tareasEstudiante = tareas.get(i);
                ArrayList<Double> notasEstudiante = notas.get(i);
                
                System.out.println("\n  Estudiante: " + nombre + " (" + codigo + ")");
                System.out.println("    Notas: " + notasEstudiante);
                
                // Encontrar la nota más baja
                double notaMasBaja = Double.MAX_VALUE;
                int posicionNotaBaja = -1;
                
                for (int j = 0; j < notasEstudiante.size(); j++) {
                    if (notasEstudiante.get(j) < notaMasBaja) {
                        notaMasBaja = notasEstudiante.get(j);
                        posicionNotaBaja = j;
                    }
                }
                
                System.out.println("    Nota más baja eliminada: " + notaMasBaja);
                
                // Calcular promedio con las notas restantes
                double suma = 0;
                int contador = 0;
                
                for (int j = 0; j < notasEstudiante.size(); j++) {
                    if (j != posicionNotaBaja) {
                        suma += notasEstudiante.get(j);
                        contador++;
                        // Escribir en el nuevo archivo (todas las notas excepto la más baja)
                        pwNotasNew.println(codigo + "\t" + nombre + "\t" + 
                                         tareasEstudiante.get(j) + "\t" + notasEstudiante.get(j));
                    }
                }
                
                double promedioFinal = (contador > 0) ? suma / contador : 0;
                
                System.out.println("    Promedio final (sin nota más baja): " + String.format("%.2f", promedioFinal));
                
                // Escribir en el archivo de promedios
                pwPromedios.println(codigo + "\t" + nombre + "\t" + notaMasBaja + "\t" + 
                                   String.format("%.2f", promedioFinal) + "\t" + contador);
            }
            
            pwNotasNew.close();
            pwPromedios.close();
            
            // ============================================================
            // MOSTRAR RESULTADO
            // ============================================================
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Archivo generado: notas_actualizado.txt (sin notas más bajas)");
            System.out.println("Archivo generado: promedios_finales.txt (promedios calculados)");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}