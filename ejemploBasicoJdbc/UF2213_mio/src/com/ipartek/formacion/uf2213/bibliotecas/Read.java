package com.ipartek.formacion.uf2213.bibliotecas;

import java.util.Scanner;

public class Read {
	private static final Scanner sc = new Scanner(System.in);

	private static String readString(String mensaje) {
		String texto;
		System.out.println(mensaje + ": ");
		texto = sc.nextLine();
		return texto;
	}

	public static Integer readInt(String mensaje) {
		int i = 0;
		String dato = readString(mensaje);

		if (dato == null) {
			return null;
		}
		i = Integer.parseInt(dato);

		return i;
	}

	public static Long readLong(String mensaje) {
		long l = 0;
		String dato = readString(mensaje);
		l = Long.parseLong(dato);
		return l;
	}

}
