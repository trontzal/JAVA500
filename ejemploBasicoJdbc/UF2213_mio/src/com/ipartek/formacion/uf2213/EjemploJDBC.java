package com.ipartek.formacion.uf2213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class EjemploJDBC {
	private static final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
	private static final String USER = "root";
	private static final String PASS = "1234";

	private static final String SQL_SELECT = "SELECT id, dni,  dni_diferencial, nombre, apellidos, fecha_nacimiento FROM clientes";
	private static final String SQL_SELECT_ID = SQL_SELECT + " WHERE id =?";
	private static final String SQL_INSERT = "INSERT INTO clientes(dni, dni_diferencial, nombre, apellidos, fecha_nacimiento) VALUES (?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE clientes SET dni=?, dni_diferencial=?, nombre=?, apellidos=?,fecha_nacimiento=? WHERE id =?";
	private static final String SQL_DELETE = "DELETE FROM clientes  WHERE id=?";
	
	private static Connection con;

	public static void main(String[] args) {
		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			
			insertar("8888888o", 0, "Nuevo nombre", "Nuevo apellido", LocalDate.of(1996, 07, 19));
			
			modificar(2, "99999999p", 0, "Modificado", "Modificado", LocalDate.of(2000, 12, 3));
			
			obtenerPorId(3);
			
			eliminar(2);
			
			listado();
		} catch (SQLException e) {
			System.err.println("Error al conectarse con la base de datos");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
		
	}

	private static void obtenerPorId(long id) {
		try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_ID)) {
			pst.setLong(1, id);
			
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				System.out.printf(
						"""
						Id: %s 
						DNI: %s%s
						Nombre: %s 
						Apellidos: %s 
						Fecha de nacimiento: %s
						""",
						rs.getLong("id"),
						rs.getString("dni"),
						rs.getString("dni_diferencial"),
						rs.getString("nombre"),
						rs.getString("apellidos"),
						rs.getString("fecha_nacimiento"));
			}
		} catch (SQLException e) {
			System.err.println("Error a la hora de obtener por id");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	private static void insertar(String dni ,int dniDiferencial, String nombre, String apellidos, LocalDate fechaCaducidad) {
		try (PreparedStatement pst = con.prepareStatement(SQL_INSERT)) {
			pst.setString(1, dni);
			pst.setInt(2, dniDiferencial);
			pst.setString(3, nombre);
			pst.setString(4, apellidos);
			pst.setDate(5, java.sql.Date.valueOf(fechaCaducidad));
			
			int numeroRegistrosModificados = pst.executeUpdate();
			
			System.out.println(numeroRegistrosModificados);
		} catch (SQLException e) {
			System.err.println("Error al insertar");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	private static void modificar(long id, String dni ,int dniDiferencial, String nombre, String apellidos, LocalDate fechaCaducidad) {
		try (PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			pst.setString(1, dni);
			pst.setInt(2, dniDiferencial);
			pst.setString(3, nombre);
			pst.setString(4, apellidos);
			pst.setDate(5, java.sql.Date.valueOf(fechaCaducidad));
			
			pst.setLong(6, id);
			
			int numeroRegistrosModificados = pst.executeUpdate();
			
			
			System.out.println(numeroRegistrosModificados);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void eliminar(long id) {
		try (PreparedStatement pst = con.prepareStatement(SQL_DELETE)) {
			pst.setLong(1, id);
			
			int numeroRegistrosModificados = pst.executeUpdate(); 
			
			System.out.println(numeroRegistrosModificados);
		} catch (SQLException e) {
			System.err.println("Error a la hora de borrar");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
		
	}
	

	private static void listado(){
		try (Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(SQL_SELECT)) {
			while (rs.next()) {
				System.out.printf("%2s %s %4s %-8s %-10s %s\n", rs.getLong("id"), rs.getString("dni"),
						rs.getString("dni_diferencial"), rs.getString("nombre"), rs.getString("apellidos"),
						rs.getString("fecha_nacimiento"));
			}
		} catch (SQLException e) {
			System.err.println("Error en el listado");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}
}
