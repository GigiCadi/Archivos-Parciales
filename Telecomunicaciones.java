/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package telecomunicaciones;

/**
 *
 * @author Estudiante
 */

/**
 * Una empresa de telecomunicaciones mantiene un archivo llamado Clientes_Activos,
 * con la siguiente información: ID del cliente, Nombre, Plan contratado. El archivo
 * está ordenado por ID del cliente.
Cada mes, se genera un archivo Nuevos_Contratos, con los campos: ID del cliente, 
* Nombre, Plan contratado, Estado (Activo/Inactivo).
Se solicita un programa que actualice el archivo Clientes_Activos con los registros
* del archivo Nuevos_Contratos, de la siguiente forma:
1.	Si un cliente ya existe en Clientes_Activos y aparece como Inactivo en 
* Nuevos_Contratos, debe eliminarse.
2.	Si un cliente ya existe, pero aparece con un nuevo plan, debe actualizarse su información.
3.	Si es un cliente nuevo con estado Activo, debe insertarse manteniendo el orden por ID del cliente.

 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Telecomunicaciones {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        
        do {
            System.out.println("\n===========================================================");
            System.out.println("       TELECOMUNICACIONES - ACTUALIZACION DE CLIENTES");
            System.out.println("===========================================================");
            System.out.println("1. Actualizar clientes");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    actualizarClientes();
                    break;
                case 2:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        } while (opc != 2);
        
        leer.close();
    }

    // ================================================================
    // ACTUALIZAR CLIENTES
    // ================================================================
    public static void actualizarClientes() {
        
        try {
            // Leer clientes activos
            BufferedReader brActivos = new BufferedReader(new FileReader("clientes_activos.txt"));
            
            ArrayList<Integer> ids = new ArrayList<>();
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<String> planes = new ArrayList<>();
            
            String lineaActivo;
            
            while ((lineaActivo = brActivos.readLine()) != null) {
                String[] data = lineaActivo.split("\t");
                ids.add(Integer.parseInt(data[0]));
                nombres.add(data[1]);
                planes.add(data[2]);
            }
            brActivos.close();
            
            // Leer nuevos contratos
            BufferedReader brNuevos = new BufferedReader(new FileReader("nuevos_contratos.txt"));
            
            ArrayList<Integer> idsNuevos = new ArrayList<>();
            ArrayList<String> nombresNuevos = new ArrayList<>();
            ArrayList<String> planesNuevos = new ArrayList<>();
            ArrayList<String> estados = new ArrayList<>();
            
            String lineaNuevo;
            
            while ((lineaNuevo = brNuevos.readLine()) != null) {
                String[] data = lineaNuevo.split("\t");
                idsNuevos.add(Integer.parseInt(data[0]));
                nombresNuevos.add(data[1]);
                planesNuevos.add(data[2]);
                estados.add(data[3]);
            }
            brNuevos.close();
            
            int totalOriginal = ids.size();
            int totalNuevos = idsNuevos.size();
            int insertados = 0;
            int actualizados = 0;
            int eliminados = 0;
            
            System.out.println("\n--- CLIENTES ACTIVOS ORIGINALES ---");
            for (int i = 0; i < ids.size(); i++) {
                System.out.println(ids.get(i) + "\t" + nombres.get(i) + "\t" + planes.get(i));
            }
            
            System.out.println("\n--- NUEVOS CONTRATOS ---");
            for (int i = 0; i < idsNuevos.size(); i++) {
                System.out.println(idsNuevos.get(i) + "\t" + nombresNuevos.get(i) + 
                                 "\t" + planesNuevos.get(i) + "\t" + estados.get(i));
            }
            
            System.out.println("\n--- PROCESANDO ACTUALIZACIONES ---");
            
            // Procesar cada nuevo contrato
            for (int i = 0; i < totalNuevos; i++) {
                int idNuevo = idsNuevos.get(i);
                String nombreNuevo = nombresNuevos.get(i);
                String planNuevo = planesNuevos.get(i);
                String estado = estados.get(i);
                
                boolean encontrado = false;
                int posicion = -1;
                
                // Buscar si el cliente existe
                for (int j = 0; j < ids.size(); j++) {
                    if (ids.get(j) == idNuevo) {
                        encontrado = true;
                        posicion = j;
                        break;
                    }
                }
                
                if (encontrado) {
                    // Cliente existe
                    if (estado.equalsIgnoreCase("Inactivo")) {
                        // Eliminar cliente
                        ids.remove(posicion);
                        nombres.remove(posicion);
                        planes.remove(posicion);
                        eliminados++;
                        System.out.println("   Cliente " + idNuevo + " (" + nombreNuevo + ") ELIMINADO (Inactivo)");
                    } else {
                        // Actualizar plan
                        planes.set(posicion, planNuevo);
                        actualizados++;
                        System.out.println("   Cliente " + idNuevo + " (" + nombreNuevo + ") PLAN ACTUALIZADO: " + planNuevo);
                    }
                } else {
                    // Cliente nuevo
                    if (estado.equalsIgnoreCase("Activo")) {
                        // Insertar manteniendo orden por ID
                        int posInsert = 0;
                        for (int j = 0; j < ids.size(); j++) {
                            if (idNuevo < ids.get(j)) {
                                posInsert = j;
                                break;
                            }
                            posInsert = j + 1;
                        }
                        
                        ids.add(posInsert, idNuevo);
                        nombres.add(posInsert, nombreNuevo);
                        planes.add(posInsert, planNuevo);
                        insertados++;
                        System.out.println("   Nuevo cliente " + idNuevo + " (" + nombreNuevo + ") INSERTADO (Activo)");
                    } else {
                        System.out.println("   Cliente " + idNuevo + " (" + nombreNuevo + ") IGNORADO (Inactivo)");
                    }
                }
            }
            
            // Escribir archivo actualizado
            PrintWriter pwClientesNew = new PrintWriter(new FileWriter("clientes_actualizado.txt"));
            
            System.out.println("\n--- CLIENTES ACTUALIZADOS (ordenados por ID) ---");
            for (int i = 0; i < ids.size(); i++) {
                pwClientesNew.println(ids.get(i) + "\t" + nombres.get(i) + "\t" + planes.get(i));
                System.out.println(ids.get(i) + "\t" + nombres.get(i) + "\t" + planes.get(i));
            }
            pwClientesNew.close();
            
            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total clientes originales: " + totalOriginal);
            System.out.println("  - Clientes actualizados: " + actualizados);
            System.out.println("  - Clientes eliminados: " + eliminados);
            System.out.println("  - Clientes nuevos insertados: " + insertados);
            System.out.println("Total clientes finales: " + ids.size());
            System.out.println("Archivo generado: clientes_actualizado.txt");
            System.out.println("===========================================================");
            
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}