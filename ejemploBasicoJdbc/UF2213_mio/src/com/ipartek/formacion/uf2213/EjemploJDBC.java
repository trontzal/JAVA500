package com.ipartek.formacion.uf2213;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

import com.ipartek.bibliotecas.Consola;
import com.mysql.cj.protocol.Message;

public class EjemploJDBC {
	private static final String URL = "jdbc:mysql://localhost:3306/manana_tienda";
	private static final String USER = "root";
	private static final String PASS = "1234";

	private static final String SQL_SELECT = "SELECT id, dni,  dni_diferencial, nombre, apellidos, fecha_nacimiento FROM clientes";
	private static final String SQL_SELECT_ID = SQL_SELECT + " WHERE id =?";
	private static final String SQL_SELECT_ID_FACTURAS = """
			SELECT
			    f.id, f.numero, f.fecha, c.id, c.dni, c.dni_diferencial, c.nombre, c.apellidos, c.fecha_nacimiento
			FROM
			    facturas f
			        JOIN
			    clientes c ON f.clientes_id = c.id
			WHERE
			    clientes_id = ?
			""";
	private static final String SQL_SELECT_ID_FACTURAS_CON_PRODUCTOS = """
						    SELECT
			    c.id, c.nombre, f.numero, p.nombre, fp.cantidad, p.precio ,(Select fp.cantidad*p.precio) as total_mismo_producto
			FROM
			    clientes AS c
			        JOIN
			    facturas AS f ON c.id = f.clientes_id
			        JOIN
			    facturas_has_productos AS fp ON f.id = fp.facturas_id
			        JOIN
			    productos AS p ON fp.productos_id = p.id
			where c.id=?
			ORDER BY f.numero
						""";
	private static final String SQL_SELECT_PRODUCTOS = "Select p.id, p.nombre, p.precio, p.stock from productos as p";
	private static final String SQL_INSERT = "INSERT INTO clientes(dni, dni_diferencial, nombre, apellidos, fecha_nacimiento) VALUES (?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE clientes SET dni=?, dni_diferencial=?, nombre=?, apellidos=?,fecha_nacimiento=? WHERE id =?";
	private static final String SQL_DELETE = "DELETE FROM clientes  WHERE id=?";
	private static final int listado = 1;
	private static final int buscar = 2;
	private static final int insertar = 3;
	private static final int modifica = 4;
	private static final int borrar = 5;
	private static final int facturas = 6;
	private static final int facturasConProductos = 7;
	private static final int verProductos = 8;
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
//			e.printStackTrace();
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
				6. Facturas
				7. Facturas con productos
				8. Ver productos

				0. SALIR
				""");

	}

	private static int pedirOpcion() {
		return Consola.leerInt("Introduce la opcion elegida");
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
		case facturas: {
			facturas();
			break;
		}
		case facturasConProductos: {
			facturasConProductos();
			break;
		}
		case verProductos: {
			verProductos();
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

	private static void verProductos() {
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL_SELECT_PRODUCTOS)) {
			System.out.println("Productos---");
			while (rs.next()) {
				System.out.printf("""

						id: 	%s
						Nombre: %s
						Precio: %s
						Stock:  %s
						""", rs.getLong("p.id"), rs.getString("p.nombre"), rs.getString("p.precio"),
						rs.getLong("p.stock"));
			}
		} catch (SQLException e) {
			System.err.println("Error al ver los productos");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void buscar() {
		long id = Consola.leerLong("Introduce el id a buscar");
		obtenerPorId(id);

	}

	private static void insertar() {

		String dni;
		Integer dniDiferenccial;
		String nombre;
		String apellidos;
		LocalDate fechaNacimiento;

		dni = Consola.leerString("DNI");
		dniDiferenccial = Consola.leerInt("DNI Diferencial");
		nombre = Consola.leerString("Nombre");
		apellidos = Consola.leerString("Apellidos");
		fechaNacimiento = Consola.leerFecha("Fecha de nacimiento");

		insertar(dni, dniDiferenccial, nombre, apellidos, fechaNacimiento);

	}

	private static void modificar() {
		Long id;
		String dni;
		Integer dniDiferenccial;
		String nombre;
		String apellidos;
		LocalDate fechaNacimiento;

		id = Consola.leerLong("ID");
		dni = Consola.leerString("DNI");
		dniDiferenccial = Consola.leerInt("DNI Diferencial");
		nombre = Consola.leerString("Nombre");
		apellidos = Consola.leerString("Apellidos");
		fechaNacimiento = Consola.leerFecha("Fecha de nacimiento");

		modificar(id, dni, dniDiferenccial, nombre, apellidos, fechaNacimiento);
	}

	private static void borrar() {
		Long id;
		id = Consola.leerLong("ID a eliminar");
		borrar(id);
	}

	private static void facturas() {
		long id = Consola.leerLong("Introduce el id del cliente a ver facturas");
		obtenerPorIdConFacturas(id);

	}

	private static void facturasConProductos() {
		long id = Consola.leerLong("Introduce el id para ver las facturas con los prodcutos");
		obtenerPorIdConFacturasYProductos(id);

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
				System.out.println("Producto con id " + id + " no encontrado");
			} else {
				System.out.println("Producto con id " + id + " eliminado correctamente");
			}

		} catch (SQLException e) {
			System.err.println("Error a la hora de borrar");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}

	}

	private static void obtenerPorIdConFacturas(long id) {
		boolean fichaClienteMostrada = false;

		try (PreparedStatement pst = con.prepareStatement(SQL_SELECT_ID_FACTURAS)) {
			pst.setLong(1, id);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					if (!fichaClienteMostrada) {
						System.out.printf("""

								CLIENTE
								=======
								Id:                  %s
								DNI:                 %s
								DNI-Diferencial		 %s
								Nombre:              %s
								Apellidos:           %s
								Fecha de nacimiento: %s

								""",
								// System.out.printf("%2s %s %3s %-3s %-20s %s\n",
								rs.getString("c.id"), rs.getString("c.dni"), rs.getString("c.dni_diferencial"),
								rs.getString("c.nombre"), rs.getString("c.apellidos"),
								rs.getString("c.fecha_nacimiento"));
						fichaClienteMostrada = true;
					}

					System.out.printf("%s, %s, %s\n", rs.getString("f.id"), rs.getString("f.numero"),
							rs.getString("f.fecha"));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener por el id " + id);
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}

	private static void obtenerPorIdConFacturasYProductos(long id) {
		boolean personaMostrada = false;
		Long idFacturaEnCurso = null;
		Long idFactura = null;
		BigDecimal totalMismoProducto = BigDecimal.ZERO;
		BigDecimal totalMismaFactura = BigDecimal.ZERO;
		BigDecimal totalTodasFacturas = BigDecimal.ZERO;

		try {
			PreparedStatement pst = con.prepareStatement(SQL_SELECT_ID_FACTURAS_CON_PRODUCTOS);
			pst.setLong(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				idFactura = rs.getLong("f.numero");
				if (personaMostrada == false) {

					mostrarClienteConIdYNombre(rs);

					personaMostrada = true;
				}
				if (!Objects.equals(idFactura, idFacturaEnCurso)) {
					if (totalMismaFactura != BigDecimal.ZERO) {

						motrarTotalUnaFactura(totalMismaFactura);
					}
					
					mostrarNumeroFactura(rs);
					
					idFacturaEnCurso = idFactura;
					totalMismaFactura = BigDecimal.ZERO;

				}
				
				totalMismoProducto = motrarProductoConCantidadYTotalDelMismo(rs);
				totalMismaFactura = totalMismaFactura.add(totalMismoProducto);
				totalTodasFacturas = totalTodasFacturas.add(totalMismoProducto);

			}
			if (idFacturaEnCurso != null) {
				
				mostrarTotalUltimaFacturaYTotalTodasLasFacturas(totalMismaFactura, totalTodasFacturas);
				
			} else {
				System.out.println("Usuario sin facturas o inexistente");
			}
		} catch (SQLException e) {
			System.err.println("Error en las facturas del usuario " + id);
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}

	}

	private static void motrarTotalUnaFactura(BigDecimal totalMismaFactura) {
		System.out.println("Total de una factura (arreglar chapuza) " + totalMismaFactura);
	}

	private static void mostrarTotalUltimaFacturaYTotalTodasLasFacturas(BigDecimal totalMismaFactura,
			BigDecimal totalTodasFacturas) {
		System.out.println("Total de una factura (solo funciona en la ultima) " + totalMismaFactura);
		System.out.println();
		System.out.println("Total gatado por el usuario " + totalTodasFacturas);
		System.out.println();
	}

	private static BigDecimal motrarProductoConCantidadYTotalDelMismo(ResultSet rs) throws SQLException {
		BigDecimal totalMismoProducto;
		System.out.printf("""
				Nombre: %s
				Cantidad: %s
				Precio: %s
				Total mismo producto: %s
				""", rs.getString("p.nombre"), rs.getString("fp.cantidad"), rs.getString("p.precio"),
				rs.getString("total_mismo_producto"));
		totalMismoProducto = rs.getBigDecimal("total_mismo_producto");
		return totalMismoProducto;
	}

	private static void mostrarNumeroFactura(ResultSet rs) throws SQLException {
		System.out.printf("""

				-- Factura Numero: %s

				""", rs.getString("f.numero"));
	}

	private static void mostrarClienteConIdYNombre(ResultSet rs) throws SQLException {
		System.out.printf("""
				id: %s
				nombre: %s
				Facturas----
				""", rs.getString("c.id"), rs.getString("c.nombre"));
	}
}
