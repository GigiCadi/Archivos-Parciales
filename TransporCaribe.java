/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package transporcaribe;

/**
 *
 * @author Estudiante
 */

/*2. (2.0) Una empresa de transportes TransporCaribe requiere su apoyo
para reorganizar las rutas de sus buses en temporada de vacaciones.
Actualmente cuenta con un archivo con los datos de los buses
organizado por capacidad de pasajeros. Hay tres tipos de buses:
minibus con capacidad para 15 personas, mediano con capacidad para
30 personas y Gran bus con capacidad para 60 personas.

La modificación que debe realizarse al archivo es la siguiente:
asignar la ruta Barranquilla-Cartagena-Barranquilla a los buses que
sumen una capacidad de 105 pasajeros al día y el resto de los buses
serán asignados con la ruta Barranquilla-Santa Marta-Barranquilla.

El archivo con los datos de los buses tiene los siguientes datos:
código del bus, capacidad del bus, kilómetros recorridos y ruta.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TransporCaribe {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;

        do {
            System.out.println("\n===========================================================");
            System.out.println("        TRANSPORCARIBE - ASIGNACION DE RUTAS");
            System.out.println("===========================================================");
            System.out.println("1. Asignar rutas a buses");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine();

            switch (opc) {
                case 1:
                    asignarRutas();
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
    // ASIGNAR RUTAS A BUSES
    // ================================================================
    public static void asignarRutas() {

        try {
            // ============================================================
            // PASO 1: ABRIR ARCHIVO DE BUSES
            // ============================================================
            BufferedReader brBuses = new BufferedReader(new FileReader("buses.txt"));

            // ============================================================
            // PASO 2: LEER TODOS LOS BUSES Y GUARDARLOS EN LISTAS
            // ============================================================
            ArrayList<String> codigos = new ArrayList<>();
            ArrayList<Integer> capacidades = new ArrayList<>();
            ArrayList<Double> kilometros = new ArrayList<>();
            ArrayList<String> rutas = new ArrayList<>();

            String lineaBus;

            System.out.println("\n--- LEYENDO BUSES ---");

            while ((lineaBus = brBuses.readLine()) != null) {
                String[] busData = lineaBus.split("\t");

                String codigo = busData[0];
                int capacidad = Integer.parseInt(busData[1]);
                double km = Double.parseDouble(busData[2]);
                String ruta = busData[3];

                codigos.add(codigo);
                capacidades.add(capacidad);
                kilometros.add(km);
                rutas.add(ruta);

                System.out.println("  - " + codigo + "\tCap: " + capacidad + "\tKm: " + km + "\tRuta: " + ruta);
            }
            brBuses.close();

            // ============================================================
            // PASO 3: ASIGNAR RUTAS SEGÚN CAPACIDAD
            // ============================================================
            int totalCapacidad = 0;
            int busesEnGrupo = 0;
            int totalBuses = codigos.size();

            // Nueva lista para guardar las rutas asignadas
            ArrayList<String> nuevasRutas = new ArrayList<>();

            System.out.println("\n--- ASIGNANDO RUTAS ---");
            for (int i = 0; i < codigos.size(); i++) {
                nuevasRutas.add("");  // Agregar un elemento vacío por cada bus
            }
            for (int i = 0; i < totalBuses; i++) {
                int capacidad = capacidades.get(i);

                // Si al sumar la capacidad del bus actual no supera 105
                if (totalCapacidad + capacidad <= 105) {
                    totalCapacidad += capacidad;
                    busesEnGrupo++;

                    // Si se alcanza exactamente 105, asignar ruta larga
                    if (totalCapacidad == 105) {
                        for (int j = i - busesEnGrupo + 1; j <= i; j++) {
                            nuevasRutas.set(j, "Barranquilla-Cartagena-Barranquilla");
                        }
                        System.out.println("   Grupo de 105 pasajeros formado con buses: "
                                + codigos.subList(i - busesEnGrupo + 1, i + 1));
                        totalCapacidad = 0;
                        busesEnGrupo = 0;
                    }
                } else {
                    // Si se pasa de 105, asignar ruta corta a los buses acumulados
                    for (int j = i - busesEnGrupo; j < i; j++) {
                        nuevasRutas.set(j, "Barranquilla-Santa Marta-Barranquilla");
                    }
                    System.out.println("  Grupo incompleto (" + totalCapacidad + "): ruta corta");

                    // Reiniciar con el bus actual
                    totalCapacidad = capacidad;
                    busesEnGrupo = 1;
                }
            }

            // Asignar rutas a los buses restantes
            if (busesEnGrupo > 0) {
                for (int i = totalBuses - busesEnGrupo; i < totalBuses; i++) {
                    nuevasRutas.set(i, "Barranquilla-Santa Marta-Barranquilla");
                }
                System.out.println("   Ultimo grupo (" + totalCapacidad + "): ruta corta");
            }

            // ============================================================
            // PASO 4: ESCRIBIR EL NUEVO ARCHIVO
            // ============================================================
            PrintWriter pwBusesNew = new PrintWriter(new FileWriter("buses_actualizado.txt"));

            for (int i = 0; i < totalBuses; i++) {
                pwBusesNew.println(codigos.get(i) + "\t" + capacidades.get(i) + "\t"
                        + kilometros.get(i) + "\t" + nuevasRutas.get(i));
            }
            pwBusesNew.close();

            // ============================================================
            // PASO 5: MOSTRAR RESULTADO
            // ============================================================
            System.out.println("\n--- NUEVAS RUTAS ASIGNADAS ---");
            for (int i = 0; i < totalBuses; i++) {
                System.out.println(codigos.get(i) + "\tCap: " + capacidades.get(i)
                        + "\tRuta: " + nuevasRutas.get(i));
            }

            System.out.println("\n===========================================================");
            System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
            System.out.println("===========================================================");
            System.out.println("Total de buses procesados: " + totalBuses);
            System.out.println("Archivo generado: buses_actualizado.txt");
            System.out.println("===========================================================");

        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
