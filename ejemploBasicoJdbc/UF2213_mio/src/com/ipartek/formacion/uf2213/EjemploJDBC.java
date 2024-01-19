package com.ipartek.formacion.uf2213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.ipartek.bibliotecas.Consola;

public class EjemploJDBC {
	private static final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
	private static final String USER = "root";
	private static final String PASS = "1234";

	private static final String SQL_SELECT = "SELECT id, dni,  dni_diferencial, nombre, apellidos, fecha_nacimiento FROM clientes";
	private static final String SQL_SELECT_ID = SQL_SELECT + " WHERE id =?";
	private static final String SQL_INSERT = "INSERT INTO clientes(dni, dni_diferencial, nombre, apellidos, fecha_nacimiento) VALUES (?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE clientes SET dni=?, dni_diferencial=?, nombre=?, apellidos=?,fecha_nacimiento=? WHERE id =?";
	private static final String SQL_DELETE = "DELETE FROM clientes  WHERE id=?";
	private static final int listado = 1;
	private static final int buscar = 2;
	private static final int insertar = 3;
	private static final int modifica = 4;
	private static final int borrar = 5;
	private static final int salir = 0;

	private static Connection con;

	public static void main(String[] args) {

		try {
			con = DriverManager.getConnection(URL, USER, PASS);

			int opcion;

			do {
				mostrarMenu();
				opcion = pedirOpcion();
				ejecutar(opcion);
			} while (opcion != 0);

		} catch (SQLException e) {
			System.err.println("Error al conectar a la base de datos");
			System.err.println(e.getMessage());
//			e.printStackTrace();S
		}

	}

	private static void mostrarMenu() {
		System.out.print("""
				Menu -----------

				1. Listado
				2. Buscar por id
				3. Insertar
				4. Modificar
				5. Eliminar

				0. SALIR
				""");

	}

	private static int pedirOpcion() {
		return Consola.LeerInt("Introduce la opcion elegida");
	}

	private static void ejecutar(int opcion) {
		System.out.println("Ejecutando opcion " + opcion);

		switch (opcion) {
		case listado: {
			listado();
			break;
		}
		case buscar: {
			buscar();
			break;
		}
		case insertar: {
			insertar();
			break;
		}
		case modifica: {
			modificar();
			break;
		}
		case borrar: {
			borrar();
			break;
		}
		case salir: {
			System.out.println("Hasta la proxima");
			break;
		}
		default:
			System.out.println("Tiene que ser un numero valido");
		}

	}

	private static void listado() {
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL_SELECT)) {
			while (rs.next()) {
				System.out.printf("%2s %s %4s %-8s %-10s %s\n", rs.getLong("id"), rs.getString("dni"),
						rs.getString("dni_diferencial"), rs.getString("nombre"), rs.getString("apellidos"),
						rs.getString("fecha_nacimiento"));
			}
		} catch (SQLException e) {
			System.err.println("Error en el listado");
			System.err.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	private static void buscar() {
		long id = Consola.LeerLong("Introduce el id a buscar");
		obtenerPorId(id);

	}

	private static void insertar() {

		String dni;
		Integer dniDiferenccial;
		String nombre;
		String apellidos;
		LocalDate fechaNacimiento;

		dni = Consola.LeerString("DNI");
		dniDiferenccial = Consola.LeerInt("DNI Diferencial");
		nombre = Consola.LeerString("Nombre");
		apellidos = Consola.LeerString("Apellidos");
		fechaNacimiento = Consola.LeerFecha("Fecha de nacimiento");

		insertar(dni, dniDiferenccial, nombre, apellidos, fechaNacimiento);

	}

	private static void modificar() {
		Long id;
		String dni;
		Integer dniDiferenccial;
		String nombre;
		String apellidos;
		LocalDate fechaNacimiento;
		
		id = Consola.LeerLong("ID");
		dni = Consola.LeerString("DNI");
		dniDiferenccial = Consola.LeerInt("DNI Diferencial");
		nombre = Consola.LeerString("Nombre");
		apellidos = Consola.LeerString("Apellidos");
		fechaNacimiento = Consola.LeerFecha("Fecha de nacimiento");

		modificar(id ,dni, dniDiferenccial, nombre, apellidos, fechaNacimiento);
	}

	private static void borrar() {
		Long id;
		id = Consola.LeerLong("Id a eliminar");
		borrar(id);
	}

	private static void obtenerPorId(long id) {
		try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_ID)) {
			pst.setLong(1, id);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				System.out.printf("""
						Id: %s
						DNI: %s%s
						Nombre: %s
						Apellidos: %s
						Fecha de nacimiento: %s
						""", rs.getLong("id"), rs.getString("dni"), rs.getString("dni_diferencial"),
						rs.getString("nombre"), rs.getString("apellidos"), rs.getString("fecha_nacimiento"));
			} else {
				System.out.println("Cliente con id " + id + " no encontrado.");
			}
		} catch (SQLException e) {
			System.err.println("Error a la hora de obtener por id");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}

	private static void insertar(String dni, Integer dniDiferencial, String nombre, String apellidos,
			LocalDate fechaNacimiento) {
		try (PreparedStatement pst = con.prepareStatement(SQL_INSERT)) {
			pst.setString(1, dni);
			pst.setInt(2, dniDiferencial);
			pst.setString(3, nombre);
			pst.setString(4, apellidos);
			pst.setDate(5, java.sql.Date.valueOf(fechaNacimiento));

			int numeroRegistrosModificados = pst.executeUpdate();

			if (numeroRegistrosModificados > 0) {
				System.out.println("Cliente con nombre " + nombre + " correctamente insertado.");
			}
		} catch (SQLException e) {
			System.err.println("Error al insertar");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}

	private static void modificar(long id, String dni, int dniDiferencial, String nombre, String apellidos,
			LocalDate fechaCaducidad) {
		try (PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			pst.setString(1, dni);
			pst.setInt(2, dniDiferencial);
			pst.setString(3, nombre);
			pst.setString(4, apellidos);
			pst.setDate(5, java.sql.Date.valueOf(fechaCaducidad));

			pst.setLong(6, id);

			int numeroRegistrosModificados = pst.executeUpdate();

//			System.out.println(numeroRegistrosModificados);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void borrar(long id) {
		try (PreparedStatement pst = con.prepareStatement(SQL_DELETE)) {
			pst.setLong(1, id);

			int numeroRegistrosModificados = pst.executeUpdate();

			if (numeroRegistrosModificados == 0) {
				System.out.println("Error al eliminar el producto con id " + id);
			} else {
				System.out.println("Producto " + id + " eliminado correctamente");
			}

		} catch (SQLException e) {
			System.err.println("Error a la hora de borrar");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}

	}
}
