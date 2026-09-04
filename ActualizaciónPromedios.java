/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package actualizaciónpromedios;

import java.io.*;
/**
 *
 * @author Acer
 */
public class ActualizaciónPromedios {

    /*1.	(2.0) Un profesor tiene un archivo con los datos de sus estudiantes
    en la asignatura que dicta y un segundo archivo donde registra la participación
    de los estudiantes en actividades complementarias de carácter opcional.
    El profesor se ha dado cuenta que varios de sus estudiantes de octavo grado tienen
    una nota promedio por debajo de 3.0 y ha decidido sumar el 10% de la nota de cada
    actividad complementaria a su promedio. Para esto le pide a usted que actualice 
    la información de los promedios de los estudiantes a partir de la información sobre
    la actividad complementaria.

Por ejemplo, para el caso de Pedro que tiene una nota promedio de 3.0 y participó
    en una actividad obteniendo una nota de 4.0 y en una segunda actividad con una
    nota de 5.0, el profesor le sumará un 0.9 a su nota promedio quedando en 3.9.
    Tener en cuenta que no todos los estudiantes participaron en la actividad opcional
    y que un estudiante pudo haber participado en varias actividades. 
El archivo de estudiantes tiene los siguientes datos: Código del estudiante, Nombre
    del estudiante, edad, curso y nota promedio. 
El archivo de la actividad complementaria tiene los siguientes datos: Código del estudiante,
    Nombre del estudiante, actividad (son en total 3 actividades) y la nota obtenida.
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //ABRIR ARCHIVOS(el porfesor YA los tiene)

            //ABRIR ESTUDIANTES(Entrada)
            BufferedReader brEstudiantes = new BufferedReader(new FileReader("estudiantes.txt"));

            //ABRIR ESTUDIANTES_NEW(Salida)
            PrintWriter pwEstudiantesNew = new PrintWriter(new FileWriter("estudiantes_actualizado.txt"));

            String lineaEstudiante;
            String lineaActividad;

            //PROCESAR CADA ESTUDIANTE
            while ((lineaEstudiante = brEstudiantes.readLine()) != null) {
                //Leer datos del estudiante
                String[] estudianteData = lineaEstudiante.split("\t");
                long codigo = Long.parseLong(estudianteData[0]);
                String nombre = estudianteData[1];
                int edad = Integer.parseInt(estudianteData[2]);
                int curso = Integer.parseInt(estudianteData[3]);
                double nota = Double.parseDouble(estudianteData[4]);

                //SOLO PARA ESTUDIANTES DE OCTAVO GRADO CON NOTA < 3.0
                if (curso == 8 && nota < 3.0) {
                    double sumaActividades = 0.0;

                    //BUSCAR ACTIVIDADES DE ESTE ESTUDIANTE
                    //ABRIR ACTIVIDADES(ENTRADA) y leer todas la actividades
                    BufferedReader brActividades = new BufferedReader(new FileReader("actividades.txt"));

                    while ((lineaActividad = brActividades.readLine()) != null) {
                        String[] actividadData = lineaActividad.split("\t");
                        long codigoAct = Long.parseLong(actividadData[0]);
                        double notaAct = Double.parseDouble(actividadData[3]);

                        //Si la actividad si es de este estudiante
                        if (codigoAct == codigo) {
                            sumaActividades += (notaAct * 0.10);
                        }
                    }

                    brActividades.close(); //Cerrar ACtividades

                    //ACTUALIZAR LA NOTA
                    nota = nota + sumaActividades;
                }
                // ============================================================
                // ESCRIBIR EN EL NUEVO ARCHIVO
                // Registro_Estudiante_New <- Registro_Estudiante
                // Grabar(ESTUDIANTES_NEW, Registro_Estudiante_New)
                // ============================================================

                pwEstudiantesNew.println(codigo + "\t" + nombre + "\t" + edad + "\t" + curso + "\t" + nota);
            }
            //CERRAR ARCHIVOS
            brEstudiantes.close(); //Cerrar ESTUDIANTES
            pwEstudiantesNew.close();  // Cerrar ESTUDIANTES_NEW

            System.out.println("Archivo actualizado exitosamente!!!");
            System.out.println("Archivo generado: estudiantes_actualizado.txt");
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
        }

    }

}
