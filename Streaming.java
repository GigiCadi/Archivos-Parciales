/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package streaming;

/**
 *
 * @author Estudiante
 */

/*Una plataforma de streaming tiene los registros de su contenido en un
archivo con los siguientes datos, ordenados por código: Código, Nombre,
Género y Tipo (serie, miniserie y película). Recientemente ha recibido
un nuevo archivo de contenido para ingresar con los mismos campos del
archivo original.

La plataforma solo quiere ingresar contenidos de tipo serie y miniserie.
Para esto, se le ha solicitado crear un código que permita insertar los
nuevos registros de forma que el archivo original siga ordenado por el
campo código.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Streaming {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       STREAMING - ACTUALIZACIÓN DE CONTENIDO");
            System.out.println("===========================================================");
            System.out.println("1. Insertar nuevo contenido");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    insertarContenido();
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
    // INSERTAR NUEVO CONTENIDO
    // ================================================================
    public static void insertarContenido() {
        
        try {
            // ============================================================
            // LEER ARCHIVO ORIGINAL
            // ============================================================
            BufferedReader brStreaming = new BufferedReader(new FileReader("streaming.txt"));
            
            ArrayList<Integer> codigos = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<String> generos = new ArrayList<>();
            ArrayList<String> tipos = new ArrayList<>();
            
            String linea;
            
            while ((linea = brStreaming.readLine()) != null) {
                String[] data = linea.split("\t");
                codigos.add(Integer.parseInt(data[0]));
                nombres.add(data[1]);
                generos.add(data[2]);
                tipos.add(data[3]);
            }
            brStreaming.close();
            
            // ============================================================
            // LEER NUEVO CONTENIDO DE STREAMING2
            // ============================================================
            BufferedReader brStreaming2 = new BufferedReader(new FileReader("streaming2.txt"));
            
            System.out.println("\n--- CONTENIDO ACTUAL ---");
            for (int i = 0; i < codigos.size(); i++) {
                System.out.println(codigos.get(i) + "\t" + nombres.get(i) + "\t" + generos.get(i) + "\t" + tipos.get(i));
            }
            
            System.out.println("\n--- PROCESANDO NUEVO CONTENIDO ---");
            
            int insertados = 0;
            int ignorados = 0;
            
            while ((linea = brStreaming2.readLine()) != null) {
                String[] data = linea.split("\t");
                int codigoN = Integer.parseInt(data[0]);
                String nombreN = data[1];
                String generoN = data[2];
                String tipoN = data[3];
                
                System.out.println("\n  Nuevo: " + codigoN + "\t" + nombreN + "\t" + generoN + "\t" + tipoN);
                
                // Solo insertar si es serie o miniserie
                if (tipoN.equalsIgnoreCase("pelicula")) {
                    ignorados++;
                    System.out.println("    Ignorado (es película)");
                    continue;
                }
                
                System.out.println("    Insertando (serie o miniserie)");
                
                // Buscar posición de inserción (orden por código)
                int pos = 0;
                for (int i = 0; i < codigos.size(); i++) {
                    if (codigoN < codigos.get(i)) {
                        pos = i;
                        break;
                    }
                    pos = i + 1;
                }
                
                // Insertar en las listas
                codigos.add(pos, codigoN);
                nombres.add(pos, nombreN);
                generos.add(pos, generoN);
                tipos.add(pos, tipoN);
                insertados++;
                
                System.out.println("    Insertado en posición: " + pos);
            }
            brStreaming2.close();
            
            // ============================================================
            // ESCRIBIR ARCHIVO ACTUALIZADO
            // ============================================================
            PrintWriter pwStreamingNew = new PrintWriter(new FileWriter("streaming_actualizado.txt"));
            
            System.out.println("\n--- CONTENIDO ACTUALIZADO (ordenado por código) ---");
            for (int i = 0; i < codigos.size(); i++) {
                pwStreamingNew.println(codigos.get(i) + "\t" + nombres.get(i) + "\t" + generos.get(i) + "\t" + tipos.get(i));
                System.out.println(codigos.get(i) + "\t" + nombres.get(i) + "\t" + generos.get(i) + "\t" + tipos.get(i));
            }
            pwStreamingNew.close();
            
            // ============================================================
            // MOSTRAR RESULTADO
            // ============================================================
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de contenido original: " + (codigos.size() - insertados));
            System.out.println("Nuevos registros insertados: " + insertados);
            System.out.println("Registros ignorados (películas): " + ignorados);
            System.out.println("Total de contenido actual: " + codigos.size());
            System.out.println("Archivo generado: streaming_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}