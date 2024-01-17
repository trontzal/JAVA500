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

		final Connection con = DriverManager.getConnection(URL, USER, PASS);
		final Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(SQL_SELECT);

		while (rs.next()) {
			System.out.printf("%2s %s %4s %-8s %-10s %s\n",
				rs.getLong("id"),
				rs.getString("dni"),
				rs.getString("dni_diferencial"),
				rs.getString("nombre"),
				rs.getString("apellidos"),
				rs.getString("fecha_nacimiento")
			);
		}
	}
}
