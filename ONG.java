/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ong;

/**
 *
 * @author Estudiante
 */

/*Una ONG internacional desea entregar unas ayudas humanitarias en las
zonas menos favorecidas de Barranquilla. Para esto le solicita a la
Secretaría de Educación un archivo con los datos de los niños
escolarizados que tiene los siguientes datos: Identificación, Nombre,
Edad, Colegio y Dirección de residencia. Además, cuenta con un segundo
archivo con los registros de beneficios del gobierno con los siguientes
datos: Identificación, Nombre, Edad, Tipo de beneficio (Subsidio
escolar, Subsidio alimentario o ambos). Si el niño solo tiene subsidio
escolar, entonces la ayuda será de $100.000 y si el niño no tiene
ningún subsidio, la ayuda será de $200.000. Para la entrega de las
ayudas, le han solicitado crear un archivo que contenga la siguiente
información de los niños escolarizados: Identificación, Nombre, Edad,
Dirección de residencia y Monto de la ayuda.
 */
import java.io.*;
import java.util.Scanner;

public class ONG {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       ONG - AYUDAS HUMANITARIAS");
            System.out.println("===========================================================");
            System.out.println("1. Generar archivo de ayudas");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    generarAyudas();
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
    // GENERAR ARCHIVO DE AYUDAS
    // ================================================================
    public static void generarAyudas() {
        
        try {
            // Abrir archivos
            BufferedReader brNinos = new BufferedReader(new FileReader("ninos_escolarizados.txt"));
            PrintWriter pwAyudas = new PrintWriter(new FileWriter("ayudas_generadas.txt"));
            
            String lineaNino;
            
            int totalAyudaEscolar = 0;
            int totalAyudaAlimentaria = 0;
            int totalAyudaSinSubsidio = 0;
            int totalAyudaTotal = 0;
            int montoAcumulado = 0;
            
            System.out.println("\n--- PROCESANDO NIÑOS ESCOLARIZADOS ---");
            
            // MQ (Not(EOF(Niños_Escolarizados)))
            while ((lineaNino = brNinos.readLine()) != null) {
                String[] ninoData = lineaNino.split("\t");
                
                int identificacion = Integer.parseInt(ninoData[0]);
                String nombre = ninoData[1];
                int edad = Integer.parseInt(ninoData[2]);
                String colegio = ninoData[3];
                String direccion = ninoData[4];
                
                System.out.println("\n  Niño: " + nombre + " (ID: " + identificacion + ")");
                System.out.println("    Edad: " + edad + ", Colegio: " + colegio);
                
                // Buscar en el archivo de beneficios
                BufferedReader brBeneficios = new BufferedReader(new FileReader("beneficios.txt"));
                String lineaBeneficio;
                boolean encontrado = false;
                String tipoBeneficio = "";
                int ayuda = 200000; // Valor por defecto (sin subsidio)
                
                while ((lineaBeneficio = brBeneficios.readLine()) != null) {
                    String[] beneficioData = lineaBeneficio.split("\t");
                    int idBeneficio = Integer.parseInt(beneficioData[0]);
                    
                    if (idBeneficio == identificacion) {
                        tipoBeneficio = beneficioData[3];
                        encontrado = true;
                        break;
                    }
                }
                brBeneficios.close();
                
                // Determinar monto de la ayuda según reglas
                if (encontrado) {
                    System.out.println("    Beneficio encontrado: " + tipoBeneficio);
                    
                    if (tipoBeneficio.equalsIgnoreCase("Escolar")) {
                        ayuda = 100000;
                        totalAyudaEscolar++;
                        System.out.println("    Solo subsidio escolar -> Ayuda: $100.000");
                    } else {
                        ayuda = 0;
                        totalAyudaAlimentaria++;
                        System.out.println("     Tiene subsidio alimentario -> No recibe ayuda");
                    }
                } else {
                    ayuda = 200000;
                    totalAyudaSinSubsidio++;
                    System.out.println("     Sin subsidio -> Ayuda: $200.000");
                }
                
                // Escribir en el archivo de ayudas
                pwAyudas.println(identificacion + "\t" + nombre + "\t" + edad + "\t" + direccion + "\t" + ayuda);
                totalAyudaTotal++;
                montoAcumulado += ayuda;
            }
            
            brNinos.close();
            pwAyudas.close();
            
            // Mostrar resultados
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de niños procesados: " + totalAyudaTotal);
            System.out.println("  - Con subsidio escolar (ayuda $100.000): " + totalAyudaEscolar);
            System.out.println("  - Con subsidio alimentario (ayuda $0): " + totalAyudaAlimentaria);
            System.out.println("  - Sin subsidio (ayuda $200.000): " + totalAyudaSinSubsidio);
            System.out.println("Monto total de ayudas: $" + montoAcumulado);
            System.out.println("Archivo generado: ayudas_generadas.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}