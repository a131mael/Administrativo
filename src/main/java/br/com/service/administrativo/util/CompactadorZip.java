/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.service.administrativo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompactadorZip {

	// Constantes
	static final int TAMANHO_BUFFER = 4096; // 4kb

	// método para compactar arquivo
	public static void compactarParaZip(String arqSaida, String arqEntrada) throws IOException {
		int cont;
		byte[] dados = new byte[TAMANHO_BUFFER];

		BufferedInputStream origem = null;
		FileInputStream streamDeEntrada = null;
		FileOutputStream destino = null;
		ZipOutputStream saida = null;
		ZipEntry entry = null;

		destino = new FileOutputStream(new File(arqSaida));
		saida = new ZipOutputStream(new BufferedOutputStream(destino));
		File file = new File(arqEntrada);
		if (file.isDirectory()) {
			for (File arquivos : file.listFiles()) {
				streamDeEntrada = new FileInputStream(arquivos);
				origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
				entry = new ZipEntry(arquivos.getName());
				saida.putNextEntry(entry);
				while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
					saida.write(dados, 0, cont);
				}
			}
		} else {
			streamDeEntrada = new FileInputStream(file);
			origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
			entry = new ZipEntry(file.getName());
			saida.putNextEntry(entry);
			while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
				saida.write(dados, 0, cont);
			}
		}

		if (origem != null) {
			origem.close();
		}

		if (saida != null) {
			saida.close();
		}

	}

	public static String[] getFiles(String path) {
		File pathMain = new File(path);
		return pathMain.list();
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	public static boolean createDir(String pathToDir) {
		Path path = Paths.get(pathToDir);
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static boolean createFile(String path) {
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}
	

}