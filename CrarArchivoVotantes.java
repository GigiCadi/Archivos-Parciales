/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package crararchivovotantes;

import java.io.BufferedReader;   // Para LEER texto de un archivo de forma eficiente (con buffer)
import java.io.FileReader;       // Para ABRIR un archivo y leerlo caracter por caracter
import java.io.FileWriter;       // Para ABRIR un archivo y escribir caracteres en él
import java.io.IOException;      // Para manejar ERRORES de entrada/salida (archivo no encontrado, etc.)
import java.io.PrintWriter;      // Para ESCRIBIR texto formateado en un archivo (println, print, etc.)
import java.util.Scanner;        // Para LEER datos desde el TECLADO (System.in)

/**
 *
 * @author Acer
 */
public class CrarArchivoVotantes {

    //MÉTODO PARA CREAR EL ARCHIVO DE VOTANTES
    public static void ArchivoVotantes(Scanner leer, String file_name) {
        String Codigo, Nombre, NroCandidato;
        try {
            //Abrir archivo en modo escritura (Salida-Escritura)
            //false  = sobreescribe el archivo si ya este existe
            FileWriter outFile = new FileWriter(file_name + ".txt", false);
            PrintWriter archivo_votos = new PrintWriter(outFile);

            String continuar;
            System.out.println("¿Desea ingresar votantes? (Si/No)");
            continuar = leer.nextLine();

            while (continuar.equalsIgnoreCase("si")) {
                //Leer datos del votante
                System.out.println("Ingrese codigo del estudiante(9 caracteres): ");
                Codigo = leer.nextLine();

                //Vlidación: código no puede estar vacio
                while (Codigo.isEmpty()) {
                    System.out.println("El codigo no puede estar vacio. Ingrese nuevamente: ");
                    Codigo = leer.nextLine();
                }
                System.out.println("Ingrese el nombre del estudiante(20 caracteres): ");
                Nombre = leer.nextLine();

                //Validación: nombre no puede estar vacio
                while (Nombre.isEmpty()) {
                    System.out.println("El nombre no puede estar vacio. Ingrese nuevamente:");
                    Nombre = leer.nextLine();
                }
                System.out.println("Ingrese numero del candidato (1- 12): ");
                NroCandidato = leer.nextLine();
                //Validación: candidadto debe ser entre 1 y 12
                while (Integer.parseInt(NroCandidato) < 1 || Integer.parseInt(NroCandidato) > 12) {
                    System.out.println("Numero de candidato inválido. Debe ser entre 1 y 12");
                    NroCandidato = leer.nextLine();
                }
                //Escribir el registro en el archivo (formato: CODIGO\ t NOMBRE\ t NROCANDIDATO)
                //VOTOS <- V
                //Se valida en la lógica que ni codigo,nombre ni nrocandidqato están vacios para evitar errores
                if (!Codigo.isEmpty() && !Nombre.isEmpty() && !NroCandidato.isEmpty()) {
                    archivo_votos.println(Codigo + "\t" + Nombre + "\t" + NroCandidato);
                    System.out.println("votante registrado exitosamente.");
                }
                System.out.println("¿Hay mas votantes?(Si/No): ");
                continuar = leer.nextLine();

            }
            //Cerrar archivo
            archivo_votos.close();
            System.out.println("Archivo creado exitosamente!!!!");
        } catch (IOException ex) {
            System.out.println("Error creando el archivo");
            ex.printStackTrace();
            /*ex es el nombre de la variable que captura la excepción (error).
        printStackTrace() es un método que imprime en la consola todo el "rastro"
        o "historial" de llamadas que llevaron al error.*/
        }
    }

    //MÉTODO PARA LEER EL ARCHIVO DE VOTANTES
    public static void LeerArchivoVotantes(Scanner leer, String file_name) {
        try {
            FileReader outFile = new FileReader(file_name + ".txt");
            BufferedReader BufferLectura = new BufferedReader(outFile);

            String line = null;
            while ((line = BufferLectura.readLine()) != null) {
                //Lee el archivo línea por línea hasta que no haya más líneas que leer.
                String temp[] = line.split("\t");
                System.out.println("Codigo: " + temp[0]);
                System.out.println("Nombre: " + temp[1]);
                System.out.println("Numero de candidato: " + temp[2]);
                System.out.println("__________________________________");
            }
            BufferLectura.close();
            System.out.println("Archivo leido completamente!!!");

        } catch (IOException ex) {
            System.out.println("No se enontro el archivo");
        }

    }

    //MÉTODO PARA BUSCAR EL CANDIDATO GANADOR (EJERCICIO 2)
    public static void BuscarCandidatoGanador(Scanner leer, String file_votos, String file_candidatos) {
        //Arreglo para contar votos de los 12 candidatos (posiciones 1 a 12)
        int[] votos_candidato = new int[13]; //Indice 0 no se usa, indices 1-12 para los candidatos

        //PASO 1: CONTAR VOTOS DEL ARCHIVO VOTOS
        try {
            FileReader outFile = new FileReader(file_votos + ".txt");
            BufferedReader BufferLectura = new BufferedReader(outFile);

            String line = null;
            while ((line = BufferLectura.readLine()) != null) {
                String temp[] = line.split("\t");
                // temp[0] = Codigo, temp[1] = Nombre, temp[2] = NroCandidato
                int nroCandidato = Integer.parseInt(temp[2]);
                //incrementar el contador del candidato
                votos_candidato[nroCandidato] = votos_candidato[nroCandidato] + 1;
            }
            BufferLectura.close();
            System.out.println("Votos contados exitosamente!!!");
        } catch (IOException ex) {
            System.out.println("Error leyendo el archivo de votos");
            ex.printStackTrace();
            return; //Salir del método si hay error
        }

        //PASO 2:   ENCONTRAR EL CANDIDATO CON MÁS VOTOS
        int MAX_VOTOS = -1;
        int ganador = 0;

        System.out.println("\n--- CONTEO DE VOTOS POR CANDIDATO ---");
        for (int i = 1; i <= 12; i++) {
            System.out.println("Candidato" + i + ": " + votos_candidato[i] + "votos");

            if (votos_candidato[i] > MAX_VOTOS) {
                MAX_VOTOS = votos_candidato[i];
                ganador = i;
            }
        }

        System.out.println("\n--- RESULTADO ---");
        System.out.println("El candidato ganador es: " + ganador);
        System.out.println("Con " + MAX_VOTOS + " votos");

        //PASO 3: BUSCAR Y MOSTRAR DATOS DEL CADIDATO GANADOR
        try {
            FileReader outFile = new FileReader(file_candidatos + ".txt");
            BufferedReader BufferLectura = new BufferedReader(outFile);

            String line = null;
            boolean encontrado = false;

            while ((line = BufferLectura.readLine()) != null) {
                String temp[] = line.split("\t");
                /* temp[0] = Nombre_Candidato, temp[1] = Dir, temp[2] = Tel,
                temp[3] = Promedio, temp[4] = NroCandidato */

                int nroCandidato = Integer.parseInt(temp[4]);

                if (nroCandidato == ganador) {
                    System.out.println("\n--- DATOS DEL CANDIDATO GANADOR ---");
                    System.out.println("Nombre: " + temp[0]);
                    System.out.println("Direccion: " + temp[1]);
                    System.out.println("Telefono: " + temp[2]);
                    System.out.println("Promedio: " + temp[3]);
                    System.out.println("Numero de candidato: " + temp[4]);
                    System.out.println("Total de votos: " + MAX_VOTOS);
                    encontrado = true;
                    break; // Ya encontramos al ganador, salimos del ciclo
                }

            }
            BufferLectura.close();

            if (!encontrado) {
                System.out.println("No se encontraron datos del candidato ganador en el archivo candidatos");
            }
        } catch (IOException ex) {
            System.out.println("Erro leyendo el archivo de candidatos");
            ex.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Crear archivo de votantes (Ejercicio 1)");
            System.out.println("2. Buscar candidato ganador (Ejercicio 2)");
            System.out.println("3. Leer archivo de votantes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leer.nextInt();
            leer.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Digite nombre del archivo a crear (sin extensión): ");
                    String file_crear = leer.nextLine();
                    ArchivoVotantes(leer, file_crear);
                    break;

                case 2:
                    System.out.print("Digite nombre del archivo de votos (sin extensión): ");
                    String file_votos = leer.nextLine();
                    System.out.print("Digite nombre del archivo de candidatos (sin extensión): ");
                    String file_candidatos = leer.nextLine();
                    BuscarCandidatoGanador(leer, file_votos, file_candidatos);
                    break;

                case 3:
                    System.out.print("Digite nombre del archivo a leer (sin extensión): ");
                    String file_leer = leer.nextLine();
                    LeerArchivoVotantes(leer, file_leer);
                    break;

                case 4:
                    System.out.println("¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 4);

        leer.close();
    }

}
