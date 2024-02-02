package com.ipartek.formacion.uf2213;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ipartek.formacion.uf2213.bibliotecas.Read;
import com.mysql.cj.jdbc.CallableStatement;

public class PracticaSQL {

	private static final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
	private static final String USER = "manana_tienda_user";
	private static final String PASS = "1234";

	private static Connection con;

	// Opciones generales
	private static final int out = 0;
	private static final int clientOptions = 1;
	private static final int productOptions = 2;

	// Opciones clientes
	private static final int clientsList = 1;
	private static final int clientWithId = 2;
	
	// Opciones productos 
	// ...

	// SQL
	private static final String SQL_SELECT_CLIENTS = "CALL select_clientes";
	private static final String SQL_SELECT_CLIENT_ID = "CALL select_cliente_id(?)";

	public static void main(String[] args) {
		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			int option;

			do {

				showMenuGeneral();
				option = askOption();
				executeGeneralMenuOption(option);

			} while (option != 0);

		} catch (SQLException e) {
			System.err.println("Error al conectarse a la base de datos");
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
	}

	private static void showMenuGeneral() {
		System.out.printf("""

				MENU General------

				1. Menu Clientes

				2. Menu Productos

				0. Cerrar

				""");

	}

	private static void showMenuClients() {
		System.out.printf("""
	
				Menu clientes ---
	
				1. Ver clientes
				2. Ver cliente con id
	
				0. Volver atras
	
				""");
	
	}

	private static int askOption() {
		return Read.readInt("Elige una opcion: ");
	}

	private static void executeGeneralMenuOption(int option) {
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
		case clientWithId: { // 2
			clientWithId();
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
			showMenuClients();
			option = askOption();
			executeClientsOption(option);
		} while (option != 0);
	}

	private static void productOptions() {
		System.out.println("Opciones de productos");

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

	private static void clientWithId() {
		Long id = Read.readLong("Introduce el id a buscar");
		getClientWithId(id);
	}

	private static void getClientWithId(Long id) {
		try (java.sql.CallableStatement cst = con.prepareCall(SQL_SELECT_CLIENT_ID)) {
			cst.setLong(1,  id);
			ResultSet rs = cst.executeQuery();	
			if(rs.next()) {
				System.out.println(rs);
			}else {
				System.out.println("No se ha podido encontrar ningun cliente con el id " + id);
			}
		} catch (SQLException e) {
			System.err.println("Error al conseguir cliente con id " + id);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
