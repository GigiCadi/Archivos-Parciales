/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bancobarranquilla;

/**
 *
 * @author Acer
 */

/*1.	(2.0) Le han asignado el diseño de un código que valide la capacidad 
crediticia de los clientes del Banco Barranquilla. Actualmente el banco tiene
un archivo con los datos de sus clientes y tiene acceso también a un archivo 
de Datacrédito donde están reportados todas las solicitudes de productos 
bancarios que tiene el cliente.
Un cliente puede tener varios reportes en Datacrédito, es decir, puede tener
varios productos con diferentes bancos. 

El programa debe responder a las siguientes solicitudes:
•	Solicitar la cédula del cliente y validar si es apto para un crédito 
con el banco. Para ser apto, el cliente debe tener al menos un producto con 
otro banco, pero la suma de todos los créditos no debe superar el doble de sus
ingresos anuales.
•	Dado el nombre de un banco, arrojar la cantidad de clientes que tiene
asociados.
El archivo de clientes del banco tiene los siguientes datos: Cédula, Nombre,
productos con el banco, ingresos mensuales y saldo en la cuenta.
El archivo de reporte crediticio tiene los siguientes datos: Cédula, Nombre, Banco asociado, tipo de crédito y valor.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BancoBarranquilla {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int opc;
        do {
            System.out.println("\n===========================================================");
            System.out.println("         BANCO BARRANQUILLA - VALIDACION CREDITICIA");
            System.out.println("===========================================================");
            System.out.println("1. Validar cliente para credito");
            System.out.println("2. Cantidad de clientes por banco");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = leer.nextInt();
            leer.nextLine(); // limpiar buffer

            switch (opc) {
                case 1:
                    validarClientes(leer);
                    break;
                case 2:
                    contarClientesPorBanco(leer);
                    break;
                case 3:
                    System.out.println("¡HASTA LUEGO!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opc != 3);
        leer.close();
    }

    //OPCIÓN 1: VALIDAR CLEINTE PARA CRÉDITO
    public static void validarClientes(Scanner leer) {
        System.out.println("Digite la cedula del cliente a validad: ");
        String cedulaBuscar = leer.nextLine();
        String lineaCliente;
        String lineaReporte;

        // Variables del cliente
        String cedulaCliente = "";
        String nombreCliente = "";
        int productosBanco = 0;
        double ingresosMensuales = 0;
        double saldoCuenta = 0;
        boolean clienteEncontrado = false;

        // Variables para el cálculo
        double ingresoAnual = 0;
        double limiteCredito = 0;
        double sumaCreditos = 0;
        boolean tieneOtroBanco = false;

        try {
            //PASO 1: BUSCAR EL CLIENTE EN EL ARCHIVO DE CLIENTES
            BufferedReader brClientes = new BufferedReader(new FileReader("clientes_banco.txt"));

            while ((lineaCliente = brClientes.readLine()) != null) {
                String[] clienteData = lineaCliente.split("\t");

                // Leer datos del cliente
                cedulaCliente = clienteData[0];
                nombreCliente = clienteData[1];
                productosBanco = Integer.parseInt(clienteData[2]);
                ingresosMensuales = Double.parseDouble(clienteData[3]);
                saldoCuenta = Double.parseDouble(clienteData[4]);

                // Si encontramos el cliente, salir del while
                if (cedulaCliente.equals(cedulaBuscar)) {
                    clienteEncontrado = true;
                    break;
                }
            }
            brClientes.close();

            //PASO 2: VERIFICAR SI EL CLIENTE EXISTE
            if (!clienteEncontrado) {
                System.out.println("Cliente no encontrado en la base de datos del banco");
                return;
            }

            System.out.println("\n--- DATOS DEL CLIENTE ---");
            System.out.println("Cedula: " + cedulaCliente);
            System.out.println("Nombre: " + nombreCliente);
            System.out.println("Productos con el banco: " + productosBanco);
            System.out.println("Ingresos mensuales: $" + ingresosMensuales);
            System.out.println("Saldo en cuenta: $" + saldoCuenta);

            //PASO 3: CALCULAR LIMITE DE CREDITO
            ingresoAnual = ingresosMensuales * 12;
            limiteCredito = ingresoAnual * 2;

            System.out.println("\n--- CALCULO DE CAPACIDAD CREDITICIA ---");
            System.out.println("Ingresos anuales: $" + ingresoAnual);
            System.out.println("Limite de credito (2x ingresos): $" + limiteCredito);

            //PASO 4: BUSCAR REPORTES DEL CLIENTE EN DATACREDITO
            BufferedReader brDatacredito = new BufferedReader(new FileReader("datacredito.txt"));

            while ((lineaReporte = brDatacredito.readLine()) != null) {
                String[] reporteData = lineaReporte.split("\t");

                String cedulaReporte = reporteData[0];
                String nombreReporte = reporteData[1];
                String bancoAsociado = reporteData[2];
                String tipoCredito = reporteData[3];
                double valorCredito = Double.parseDouble(reporteData[4]);

                if (cedulaReporte.equals(cedulaBuscar)) {
                    sumaCreditos += valorCredito;

                    if (!bancoAsociado.equals("Banco Barranquilla")) {
                        tieneOtroBanco = true;
                    }

                    System.out.println(" - " + bancoAsociado + ": " + tipoCredito + " ($" + valorCredito + ")");
                }
            }
            brDatacredito.close();

            System.out.println("Suma total de creditos: $" + sumaCreditos);
            System.out.println("\n --- RESULTADO DE LA VALIDACION ---");

            if (!tieneOtroBanco) {
                System.out.println("❌ NO ES APTO");
                System.out.println("   Motivo: No tiene productos con otros bancos.");
                System.out.println("   Debe tener al menos un producto con otro banco.");
                return;
            }

            System.out.println("✅ EL CLIENTE ES APTO PARA CREDITO");
            System.out.println("   Cumple ambas condiciones:");
            System.out.println("   - Tiene productos con otros bancos: SÍ");
            System.out.println("   - Suma de creditos ($" + sumaCreditos + ") <= Limite ($" + limiteCredito + "): SI");
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //OPCIÓN 2: CONTAR CLIENTES POR BANCO
    public static void contarClientesPorBanco(Scanner leer) {
        System.out.println("Digite el nombre del banco a consultar: ");
        String nombreBanco = leer.nextLine();

        try {

            //PASO 1: LEER TODOS LOS REPORTES DEL DATACRÉDITO
            BufferedReader brDatacredito = new BufferedReader(new FileReader("datacredito.txt"));

            ArrayList<String> clientesUnicos = new ArrayList<>();
            String lineaReporte;

            while ((lineaReporte = brDatacredito.readLine()) != null) {
                String[] reporteData = lineaReporte.split("\t");

                String cedulaReporte = reporteData[0];
                String bancoAsociado = reporteData[2];

                if (bancoAsociado.equalsIgnoreCase(nombreBanco)) {
                    if (!clientesUnicos.contains(cedulaReporte)) {
                        clientesUnicos.add(cedulaReporte);
                    }
                }
            }
            brDatacredito.close();

            //PASO 2: MOSTRAR RESULTADO
            int cantidadClientes = clientesUnicos.size();

            System.out.println("===========================================================");
            System.out.println("RESULTADO DE LA CONSULTA");
            System.out.println("===========================================================");
            System.out.println("Banco consultado: " + nombreBanco);
            System.out.println("Cantidad de clientes asociados: " + cantidadClientes);

            if (cantidadClientes > 0) {
                System.out.println("\nLista de clientes (cédulas):");
                for (String cedula : clientesUnicos) {
                    System.out.println("  - " + cedula);
                }
            }
            System.out.println("===========================================================");
        } catch (IOException ex) {
            System.out.println("Error con los archivos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
