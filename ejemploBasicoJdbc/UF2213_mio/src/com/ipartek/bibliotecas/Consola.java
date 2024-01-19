package com.ipartek.bibliotecas;

import java.time.LocalDate;
import java.util.Scanner;

public class Consola {

	public static final Scanner sc = new Scanner(System.in);
	
	public static String LeerString(String mensaje) {
		System.out.println(mensaje + ": ");
		return sc.nextLine();
	}

	public static Integer LeerInt(String mensaje) {
		int i;
		String dato = LeerString(mensaje);
		i = Integer.parseInt(dato);
		return i;
	}
	
	public static Long LeerLong(String mensaje) {
		long l;
		String dato = LeerString(mensaje);
		l = Long.parseLong(dato);
		return l;
	}
	
	public static LocalDate LeerFecha(String mensaje) {
		LocalDate fecha = null;
		String dato = LeerString(mensaje + " [AAAA-MM-DD] ");
		if(dato.trim().length() == 0) {
			return null;
		}
		fecha = LocalDate.parse(dato);
		return fecha;
		
	}

}
