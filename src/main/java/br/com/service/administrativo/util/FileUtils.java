/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.service.administrativo.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import org.apache.poi.util.IOUtils;

/**
 * metodos estaticos tipo handle de arquivos e dados
 * 
 * @author Fernando Zimmermann
 */
public class FileUtils {

	/**
	 * se caminho nao existe, cria-se diretorios
	 *
	 * @param path
	 * @return
	 */
	public static File criarDiretorioSeNaoExiste(String path) {
		File pathF = new File(path);
		if (!pathF.exists()) {
			pathF.mkdirs();
		}
		if (!pathF.canWrite()) {
			pathF.setWritable(true);
		}
		return pathF;
	}

	/**
	 * verifica se existe arquivo/diretorio
	 * 
	 * @param path
	 * @return
	 */
	public static boolean existeArquivoDiretorio(String path) {
		File pathF = new File(path);
		if (pathF.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * retorna o caminho da pasta temporaria do sistema
	 * 
	 * @return
	 */
	public static File getPastaTemporariaSistema() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * remove todos os arquivos de forma recursiva de um diretorio ou um arquivo
	 *
	 * @param inFile
	 */
	public static void removeArquivoDiretorioRecursivo(File inFile) {
		if (inFile.isFile()) {
			inFile.delete();
		} else {
			File files[] = inFile.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					removeArquivoDiretorioRecursivo(files[i]);
				}
			}
		}
	}

	/**
	 * a partir de um stream de texto string, verifica qual o encoding
	 *
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	/*
	 * public static String getTextEncoding(InputStream stream) throws
	 * IOException { CharsetDetector charsetDetector = new CharsetDetector();
	 * charsetDetector.setText(stream); return
	 * charsetDetector.detect().getName(); }
	 */
	/**
	 * a partir de um binario byte[] e de um caminho completo, ateh a extensao,
	 * escreve dados no disco
	 *
	 * @param arquivoBinario
	 * @param caminhoCompletoArquivo
	 * @throws FileNotFoundException
	 * @throws IOException
	 *             a
	 */
	public static void escreveBinarioEmDiretorio(byte[] arquivoBinario, File caminhoCompletoArquivo)
			throws FileNotFoundException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(arquivoBinario);
		FileOutputStream out = new FileOutputStream(caminhoCompletoArquivo);
		int n;
		int buffsize = 4096;
		byte[] buf = new byte[buffsize];
		while ((n = bais.read(buf, 0, buffsize)) > -1) {
			out.write(buf, 0, n);
		}
		bais.close();
		out.close();
	}

	public static void escreveBinarioEmDiretorio(byte[] arquivoBinario, String caminhoCompletoArquivo)
			throws FileNotFoundException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(arquivoBinario);
		FileOutputStream out = new FileOutputStream(caminhoCompletoArquivo);
		int n;
		int buffsize = 4096;
		byte[] buf = new byte[buffsize];
		while ((n = bais.read(buf, 0, buffsize)) > -1) {
			out.write(buf, 0, n);
		}
		bais.close();
		out.close();
	}

	/**
	 * retorna array de bytes com conteudo total do caminho de arquivo passado
	 * por parametro
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}

	/**
	 * retorna arquivo em forma de string
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getStringFromFile(File file) throws IOException {
		FileInputStream fis = null;
		byte[] buf = new byte[4 * 1024];
		StringBuilder bf = new StringBuilder();
		try {
			fis = new FileInputStream(file);
			while (fis.read(buf) >= 0) {
				String arquivo = new String(buf);
				bf.append(arquivo);
			}
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return bf.toString();
	}


	public static void inputStreamToFile(InputStream initialStream, String arquivoSaida) {
		try {
			File targetFile = new File(arquivoSaida);

			java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(initialStream);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
