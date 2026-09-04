/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package puebloescondido;

/**
 *
 * @author Estudiante
 */

/*1. (2.0) Usted ha sido designado para actualizar los datos de los
subsidios otorgados a menores de edad en el municipio de Pueblo
Escondido. Actualmente se tienen 2 archivos, uno donde se guardan
los registros de los habitantes del municipio y un segundo archivo
con los datos de quienes reciben el subsidio. La actualización
solicitada consiste en eliminar del beneficio de subsidio a aquellos
pobladores que hayan cumplido la mayoría de edad.

El archivo de los habitantes tiene los siguientes datos: Número de
identificación, Nombre, fecha de nacimiento (en el formato AAAAMMDD),
género, dirección de residencia, teléfono de contacto.

El archivo de los subsidios tiene los siguientes datos: Número de
identificación, Nombre, estrato socioeconómico y valor del subsidio.
 */
import java.io.*;
import java.util.Scanner;

public class PuebloEscondido {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("     PUEBLO ESCONDIDO - ACTUALIZACION DE SUBSIDIOS");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar subsidios (eliminar mayores de edad)");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarSubsidios();
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
    // ACTUALIZAR SUBSIDIOS (ELIMINAR MAYORES DE EDAD)
    // ================================================================
    public static void actualizarSubsidios() {
        
        try {
            // ============================================================
            // PASO 1: ABRIR ARCHIVOS
            // ============================================================
            // Abrir SUBSIDIOS(Entrada)
            BufferedReader brSubsidios = new BufferedReader(new FileReader("subsidios.txt"));
            
            // Abrir SUBSIDIOS_NEW(Salida)
            PrintWriter pwSubsidiosNew = new PrintWriter(new FileWriter("subsidios_actualizado.txt"));
            
            // ============================================================
            // PASO 2: DEFINIR FECHA ACTUAL (2023-10-01)
            // ============================================================
            int anioActual = 2023;
            int mesActual = 10;
            int diaActual = 1;
            
            String lineaSubsidio;
            int totalEliminados = 0;
            int totalMantenidos = 0;
            
            System.out.println("\n--- PROCESANDO BENEFICIARIOS ---");
            
            // ============================================================
            // PASO 3: PROCESAR CADA BENEFICIARIO
            // ============================================================
            while ((lineaSubsidio = brSubsidios.readLine()) != null) {
                // Leer datos del beneficiario
                String[] subsidioData = lineaSubsidio.split("\t");
                
                String identificacion = subsidioData[0];
                String nombre = subsidioData[1];
                String estrato = subsidioData[2];
                String valorSubsidio = subsidioData[3];
                
                // ============================================================
                // PASO 4: BUSCAR EL HABITANTE EN EL ARCHIVO DE HABITANTES
                // ============================================================
                BufferedReader brHabitantes = new BufferedReader(new FileReader("habitantes.txt"));
                
                boolean esMayorEdad = false;
                String fechaNacimiento = "";
                
                // Leer el archivo de habitantes línea por línea
                while (true) {
                    String lineaHabitante = brHabitantes.readLine();
                    if (lineaHabitante == null) break;
                    
                    String[] habitanteData = lineaHabitante.split("\t");
                    
                    String idHabitante = habitanteData[0];
                    
                    // Si encontramos el habitante, guardamos su fecha de nacimiento
                    if (idHabitante.equals(identificacion)) {
                        fechaNacimiento = habitanteData[2];
                        break;
                    }
                }
                brHabitantes.close();
                
                // ============================================================
                // PASO 5: CALCULAR EDAD
                // ============================================================
                if (!fechaNacimiento.isEmpty()) {
                    // Extraer año, mes y día de la fecha de nacimiento
                    int anioNacimiento = Integer.parseInt(fechaNacimiento.substring(0, 4));
                    int mesNacimiento = Integer.parseInt(fechaNacimiento.substring(4, 6));
                    int diaNacimiento = Integer.parseInt(fechaNacimiento.substring(6, 8));
                    
                    // Calcular edad (año actual - año de nacimiento)
                    int edad = anioActual - anioNacimiento;
                    
                    // Verificar si ya cumplió años este año
                    // Si aún no ha cumplido años, restamos 1
                    if (mesActual < mesNacimiento || 
                        (mesActual == mesNacimiento && diaActual < diaNacimiento)) {
                        edad = edad - 1;
                    }
                    
                    // ============================================================
                    // PASO 6: VERIFICAR SI ES MAYOR DE EDAD (edad >= 18)
                    // ============================================================
                    if (edad >= 18) {
                        esMayorEdad = true;
                    }
                    
                    System.out.println("  - " + identificacion + "\t" + nombre + 
                                     "\tEdad: " + edad + 
                                     "\t" + (esMayorEdad ? " ELIMINADO" : " MANTIENE"));
                } else {
                    System.out.println("  - " + identificacion + "\t" + nombre + 
                                     "\t No encontrado en habitantes");
                }
                
                // ============================================================
                // PASO 7: GUARDAR O ELIMINAR SEGÚN EDAD
                // ============================================================
                if (!esMayorEdad) {
                    // Si NO es mayor de edad, mantiene el subsidio
                    pwSubsidiosNew.println(identificacion + "\t" + nombre + "\t" + 
                                          estrato + "\t" + valorSubsidio);
                    totalMantenidos++;
                } else {
                    // Si ES mayor de edad, se elimina el subsidio (no se escribe)
                    totalEliminados++;
                }
            }
            
            // ============================================================
            // PASO 8: CERRAR ARCHIVOS
            // ============================================================
            brSubsidios.close();        // Cerrar SUBSIDIOS
            pwSubsidiosNew.close();     // Cerrar SUBSIDIOS_NEW
            
            // ============================================================
            // PASO 9: MOSTRAR RESULTADO
            // ============================================================
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Beneficiarios que mantienen el subsidio: " + totalMantenidos);
            System.out.println("Beneficiarios eliminados (mayores de edad): " + totalEliminados);
            System.out.println("Archivo generado: subsidios_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}