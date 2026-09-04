/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clinica;

/**
 *
 * @author Acer
 */

/*2. (2.0) Usted ha sido encargado de asistir a una clínica para la actualización 
de sus archivos. En uno de los archivos se han dado cuenta que hay varios registros 
para un solo paciente. Deben eliminarse los registros repetidos y dejar el registro 
que tenga la fecha más actual.

Cédula	Nombre	Ciudad	Fecha de afiliación	EPS
1234	Pedro Pérez	Barranquilla	20200130	TuSalud
5678	Juan Molina	Cartagena	20141209	MiSalud
1234	Pedro Pérez	Barranquilla	20211028	TuSalud
9012	Ana Gómez	Santa Marta	20190814	MiSalud
1234	Pedro Pérez	Barranquilla	20180130	TuSalud

Para este ejemplo, hay varios registros para el paciente Pedro Pérez, debe dejarse 
el registro con fecha de afiliación más actual y borrar los otros dos registros.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Clinica {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("         CLÍNICA - ELIMINACIÓN DE DUPLICADOS");
            System.out.println("===========================================================");
            System.out.println("1. Eliminar registros duplicados");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    eliminarDuplicados();
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
    // ELIMINAR REGISTROS DUPLICADOS
    // ================================================================
    public static void eliminarDuplicados() {
        
        try {
            // ============================================================
            // PASO 1: ABRIR ARCHIVO DE PACIENTES (Entrada - Lectura)
            // ============================================================
            BufferedReader brPacientes = new BufferedReader(new FileReader("pacientes.txt"));
            
            // ============================================================
            // PASO 2: CREAR LISTAS PARA ALMACENAR DATOS ÚNICOS
            // ============================================================
            // ArrayList para guardar las cédulas de pacientes únicos
            ArrayList<String> cedulasUnicas = new ArrayList<>();
            
            // ArrayList para guardar los nombres de pacientes únicos
            ArrayList<String> nombresUnicos = new ArrayList<>();
            
            // ArrayList para guardar las ciudades de pacientes únicos
            ArrayList<String> ciudadesUnicas = new ArrayList<>();
            
            // ArrayList para guardar las fechas más recientes de cada paciente
            ArrayList<String> fechasUnicas = new ArrayList<>();
            
            // ArrayList para guardar las EPS de pacientes únicos
            ArrayList<String> epsUnicas = new ArrayList<>();
            
            String lineaPaciente;
            
            // ============================================================
            // PASO 3: LEER TODOS LOS REGISTROS DEL ARCHIVO
            // ============================================================
            // MQ (Not(EOF(PACIENTES)))
            System.out.println("\n--- LEYENDO REGISTROS DEL ARCHIVO ---");
            
            while ((lineaPaciente = brPacientes.readLine()) != null) {
                
                // Leer datos del paciente
                String[] pacienteData = lineaPaciente.split("\t");
                
                String cedula = pacienteData[0];
                String nombre = pacienteData[1];
                String ciudad = pacienteData[2];
                String fechaAfiliacion = pacienteData[3];
                String eps = pacienteData[4];
                
                System.out.println("  - " + cedula + "\t" + nombre + "\t" + ciudad + "\t" + fechaAfiliacion + "\t" + eps);
                
                // ============================================================
                // PASO 4: BUSCAR SI LA CÉDULA YA EXISTE
                // ============================================================
                // Buscar la posición de la cédula en la lista
                int posicion = cedulasUnicas.indexOf(cedula);
                
                if (posicion == -1) {
                    // ============================================================
                    // CASO 1: LA CÉDULA NO EXISTE → AGREGAR NUEVO REGISTRO
                    // ============================================================
                    System.out.println("    → Nuevo paciente agregado: " + nombre);
                    cedulasUnicas.add(cedula);
                    nombresUnicos.add(nombre);
                    ciudadesUnicas.add(ciudad);
                    fechasUnicas.add(fechaAfiliacion);
                    epsUnicas.add(eps);
                } else {
                    // ============================================================
                    // CASO 2: LA CÉDULA YA EXISTE → COMPARAR FECHAS
                    // ============================================================
                    String fechaExistente = fechasUnicas.get(posicion);
                    
                    // Comparar fechas (formato AAAAMMDD, mayor número = fecha más reciente)
                    // Ejemplo: 20211028 > 20200130 → 20211028 es más reciente
                    if (fechaAfiliacion.compareTo(fechaExistente) > 0) {
                        // La nueva fecha es más reciente → actualizar el registro
                        System.out.println("    → Registro actualizado para: " + nombre + " (Fecha: " + fechaAfiliacion + " > " + fechaExistente + ")");
                        nombresUnicos.set(posicion, nombre);
                        ciudadesUnicas.set(posicion, ciudad);
                        fechasUnicas.set(posicion, fechaAfiliacion);
                        epsUnicas.set(posicion, eps);
                    } else {
                        // La nueva fecha es más antigua → ignorar
                        System.out.println("    → Registro ignorado (fecha más antigua): " + nombre + " (" + fechaAfiliacion + " < " + fechaExistente + ")");
                    }
                }
            }
            
            brPacientes.close();
            
            // ============================================================
            // PASO 5: MOSTRAR PACIENTES ÚNICOS
            // ============================================================
            System.out.println("\n--- PACIENTES ÚNICOS (FECHA MÁS RECIENTE) ---");
            for (int i = 0; i < cedulasUnicas.size(); i++) {
                System.out.println(cedulasUnicas.get(i) + "\t" + nombresUnicos.get(i) + "\t" + 
                                  ciudadesUnicas.get(i) + "\t" + fechasUnicas.get(i) + "\t" + epsUnicas.get(i));
            }
            
            // ============================================================
            // PASO 6: ESCRIBIR EN EL NUEVO ARCHIVO
            // ============================================================
            // Abrir PACIENTES_NEW(Salida)
            PrintWriter pwPacientesNew = new PrintWriter(new FileWriter("pacientes_actualizado.txt"));
            
            // Escribir cada paciente único en el nuevo archivo
            for (int i = 0; i < cedulasUnicas.size(); i++) {
                pwPacientesNew.println(cedulasUnicas.get(i) + "\t" + 
                                      nombresUnicos.get(i) + "\t" + 
                                      ciudadesUnicas.get(i) + "\t" + 
                                      fechasUnicas.get(i) + "\t" + 
                                      epsUnicas.get(i));
            }
            
            pwPacientesNew.close();
            
            // ============================================================
            // PASO 7: MOSTRAR RESULTADO FINAL
            // ============================================================
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Registros originales: " + (cedulasUnicas.size() + (contarRegistrosEliminados())));
            System.out.println("Registros únicos: " + cedulasUnicas.size());
            System.out.println("Registros eliminados: " + contarRegistrosEliminados());
            System.out.println("Archivo generado: pacientes_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // ================================================================
    // MÉTODO AUXILIAR: CONTAR REGISTROS ELIMINADOS
    // ================================================================
    public static int contarRegistrosEliminados() {
        int totalOriginal = 0;
        int totalUnicos = 0;
        
        try {
            // Contar registros originales
            BufferedReader brOriginal = new BufferedReader(new FileReader("pacientes.txt"));
            while (brOriginal.readLine() != null) {
                totalOriginal++;
            }
            brOriginal.close();
            
            // Contar registros únicos en el archivo actualizado
            BufferedReader brActualizado = new BufferedReader(new FileReader("pacientes_actualizado.txt"));
            while (brActualizado.readLine() != null) {
                totalUnicos++;
            }
            brActualizado.close();
            
        } catch (IOException ex) {
            // Si no existe el archivo actualizado, devolver 0
            return 0;
        }
        
        return totalOriginal - totalUnicos;
    }
}