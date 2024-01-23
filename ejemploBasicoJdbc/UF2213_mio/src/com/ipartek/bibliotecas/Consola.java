package com.ipartek.bibliotecas;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Consola {

	public static final Scanner sc = new Scanner(System.in);
	public static final boolean OPCIONAL = true;
	public static final boolean OBLIGATORIO = false;

	public static String leerString(String mensaje) {
		System.out.println(mensaje + ": ");
		return sc.nextLine();
	}

	
	public static Long leerLong(String mensaje) {
		boolean hayError = true;
		long l = 0;

		do {
			try {
				String dato = leerString(mensaje);

				if (dato == null) {
					return null;
				}

				l = Long.parseLong(dato);
				hayError = false;
			} catch (NumberFormatException e) {
				System.out.println("El número debe ser un entero entre " + Long.MIN_VALUE + " y " + Long.MAX_VALUE);
			}
		} while (hayError);
		return l;
	}

	public static Integer leerInt(String mensaje) {
		int i;
		String dato = leerString(mensaje);
		i = Integer.parseInt(dato);
		return i;
	}

	public static LocalDate leerFecha(String mensaje) {
		boolean hayError = true;
		LocalDate fecha = null;

		do {
			try {
				String dato = leerString(mensaje + " [AAAA-MM-DD] ");

				if (dato.trim().length() == 0) {
					return null;
				}

				fecha = LocalDate.parse(dato);
				hayError = false;

			} catch (DateTimeParseException e) {
				System.out.println("La fecha debe de ser valida");
			}
		} while (hayError);

		return fecha;
	}

}
