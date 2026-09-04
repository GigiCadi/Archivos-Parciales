/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package archivossupermercado;

import java.io.*;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Acer
 */
public class ArchivosSupermercado {

    /*2.(2.0) Por motivo de su aniversario, una tienda de supermercado le ha solicitado
    el diseño de un algoritmo para la premiación de clientes. Actualmente tiene 3 archivos,
    uno por cada sede de la tienda (Barranquilla, Santa Marta y Cartagena), con los datos
    de los clientes. Realizarán un sorteo aleatorio para premiar 5 clientes de cada sede.
    Usted debe entregarle un archivo con los 15 registros, que contenga la siguiente
    información: datos de los clientes ganadores y sede a la que pertenece. 
    El archivo de clientes tiene los siguientes datos: Cédula, Nombre, edad, teléfono, email. 
    Nota: para generar un número aleatorio entre 0 y X, utilice la instrucción RANDOM(X). La 
    selección del cliente ganador será de la siguiente manera: por ejemplo, si el número 
    aleatorio es 24, entonces el cliente ganador será el registro en la posición #24.
    Deben realizarse 5 sorteos por cada sede y validar que no se repitan los ganadores. 
    Por ejemplo, si una vez más sale el #24, debe volver a generar otro número para que 
    no se repita el ganador.

    
     */
    /**
     * @param args the command line arguments
     */
    // MÉTODO PRINCIPAL
    public static void main(String[] args) {
        // PASO 1: CREAR EL OBJETO RANDOM PARA CREAR NÚMEROS ALEATORIOS

        Random rand = new Random();

        //PASO 2: DEFINIR LAS SEDES Y SUS ARCHIVOS
        String[] sedes = {"Barranquilla", "Santa Marta", "Cartagena"};
        String[] archivos = {"clientes_Barranquilla.txt", "clientes_SantaMarta.txt", "clientes_Cartagena.txt"}; //PASO 3: ABRIR EL ARCHIVO DE GANADORES (SALIDA - ESCRITURA)
        try {
            PrintWriter pwGanadores = new PrintWriter(new FileWriter("ganadores.txt"));

            pwGanadores.println("CEDULA\tNOMBRE\tEDAD\tTELEFONO\tEMAIL\tSEDE");

            // PASO 4: PROCESAR CADA SEDE
            for (int i = 0; i < 3; i++) {
                String nombreSede = sedes[i];
                String nombreArchivo = archivos[i];

                System.out.println("PROCESANDO SEDE: " + nombreSede.toUpperCase());
                System.out.println("Archivo: " + nombreArchivo);

                //PASO 5: LEER TODOS LOS CLIENTES DE LA SEDE ACTUAL
                BufferedReader brClientes = new BufferedReader(new FileReader(nombreArchivo));

                ArrayList<String[]> clientes = new ArrayList<>();

                String lineaCliente;

                while ((lineaCliente = brClientes.readLine()) != null) {
                    String[] clienteData = lineaCliente.split("\t");
                    clientes.add(clienteData);
                }
                brClientes.close();

                //PASO 6: OBTENER EL TOTAL DE REGISTROS
                int totalRegistros = clientes.size();
                System.out.println("Total de clientes en: " + nombreSede + ": " + totalRegistros);

                //PASO 7: INICIALIZAR LISTA DE POSICIONES GANADORAS
                ArrayList<Integer> posicionesUsadas = new ArrayList<>();

                int numGanadores = 0;

                //PASO 8: SELECCIONAR 5 GANADORES ALEATORIOS (SIN REPETIR)
                while (numGanadores < 5) {
                    int pos = rand.nextInt(totalRegistros);
                    System.out.println("Sorteo #" + (numGanadores + 1) + "Numero aleatorio = " + pos);

                    if (posicionesUsadas.contains(pos)) {
                        System.out.println("Posicion " + pos + " ya fue seleccionada. Generando otro numero...");
                    } else {
                        posicionesUsadas.add(pos);
                        numGanadores++;

                        String[] clienteGanador = clientes.get(pos);

                        String cedula = clienteGanador[0];
                        String nombre = clienteGanador[1];
                        String edad = clienteGanador[2];
                        String telefono = clienteGanador[3];
                        String email = clienteGanador[4];

                        pwGanadores.println(cedula + "\t" + nombre + "\t" + edad + "\t" + telefono + "\t" + email + "\t" + nombreSede);
                        System.out.println("Ganador #" + numGanadores + ": " + nombre + " (Cedula: " + cedula + ")");
                    }
                }
                System.out.println("Ganadores de " + nombreSede + ": " + numGanadores);
                System.out.println("Posiciones sleccionadas: " + posicionesUsadas);
            }
            pwGanadores.close();

            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("TOtal de ganadores: 15 (5 por cada sede)");
            System.out.println("Archivo generado: ganadores.txt");
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
