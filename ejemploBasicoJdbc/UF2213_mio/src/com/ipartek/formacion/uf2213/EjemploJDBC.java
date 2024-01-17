package com.ipartek.formacion.uf2213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploJDBC {
	public static void main(String[] args) throws SQLException {
		final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
		final String USER = "root";
		final String PASS = "1234";

		String SQL_SELECT = "SELECT * FROM clientes";
//		String SQL_INSERT = "INSERT INTO clientes(dni, nombre, apellidos, fecha_nacimiento) VALUES ('12548796w', 'Gon', 'Lecumberri', '1996-08-19')";

		final Connection con = DriverManager.getConnection(URL, USER, PASS);
		final Statement st = con.createStatement();

//		int filasAfectadas = st.executeUpdate(SQL_INSERT);

//		if (filasAfectadas > 0) {
//			System.out.println("Insert exitoso");
//		} else {
//			System.out.println("Error al isnertar");
//		}

		ResultSet rs = st.executeQuery(SQL_SELECT);

		while (rs.next()) {
			System.out.printf("%2s %s %4s %-8s %-10s %s\n", rs.getLong("id"), rs.getString("dni"),
					rs.getString("dni_diferencial"), rs.getString("nombre"), rs.getString("apellidos"),
					rs.getString("fecha_nacimiento"));
		}
	}
}
