package com.ipartek.formacion.uf2213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PracticaSQL {

	private static final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
	private static final String USER = "manana_tienda_user";
	private static final String PASS = "1234";

	private static Connection con;
	private static final Scanner sc = new Scanner(System.in);

	// Opciones generales
	private static final int out = 0;
	private static final int clientOptions = 1;
	private static final int productOptions = 2;

	// Opciones clientes
	private static final int clientsList = 1;
	
	// Opciones productos 
	// ...

	// SQL
	private static final String SQL_SELECT_CLIENTS = "SELECT c.id, c.dni, c.dni_diferencial, c.nombre, c.apellidos, c.fecha_nacimiento FROM clientes AS c";

	public static void main(String[] args) {
		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			int option;

			do {

				showMenu();
				option = askOption();
				executeFirstOption(option);

			} while (option != 0);

		} catch (SQLException e) {
			System.err.println("Error al conectarse a la base de datos");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void showMenu() {
		System.out.printf("""

				MENU General------

				1. Menu Clientes

				2. Menu Productos

				0. Cerrar

				""");

	}

	private static int askOption() {
		System.out.println("Elige una opcion: ");
		return sc.nextInt();
	}

	private static void executeFirstOption(int option) {
		System.out.println("Ejecutando opcion " + option);

		switch (option) {
		case out: { // 0
			System.out.println("Hasta la proxima");
			break;
		}
		case clientOptions: { // 1
			clientsOptions();
			break;
		}
		case productOptions: { // 2
			productOptions();
			break;
		}
		default: {
			System.out.println("Tiene que ser una opcion valida");
			break;
		}
		}
	}

	private static void clientsOptions() {
		int option;
		do {
			clientsMenu();
			option = askOption();
			executeClientsOption(option);
		} while (option != 0);
	}

	private static void productOptions() {
		System.out.println("Opciones de productos");

	}

	private static void clientsMenu() {
		System.out.printf("""

				Menu clientes ---

				1. Ver clientes

				0. Volver atras

				""");

	}

	private static void executeClientsOption(int option) {

		switch (option) {
		case out: { // 0
			System.out.println("Volviendo a el menu general");
			break;
		}
		case clientsList: { // 1
			clientsList();
			break;
		}
		default: {
			System.out.println("Tiene que ser una opcion valida");
			break;
		}
		}
	}

	private static void clientsList() {
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL_SELECT_CLIENTS)) {
			System.out.println("ID   |    DNI     |DIFCL|   NOMBRE      |  APELLIDOS   |   FECHA NACIMIENTO  ");
			while (rs.next()) {
				System.out.printf("""
						%-4s %11s   %-8s %-15s %-15s %s
						""", rs.getInt("c.id"), rs.getString("c.dni"), rs.getLong("c.dni_diferencial"),
						rs.getString("c.nombre"), rs.getString("c.apellidos"), rs.getDate("c.fecha_nacimiento"));
			}
		} catch (SQLException e) {
			System.err.println("Error al conseguir el listado");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
