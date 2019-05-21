package br.com.aff.Administrativo.CNAB_240;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.administrativo.model.Boleto;
import br.com.administrativo.model.Pagador;

public class CNAB240_RETORNO_SICOOB {

	public static List<Pagador> imporCNAB240(String arquivoPath) throws ParseException {
		System.out.println("Caminho dos arquivos para importar: " + arquivoPath);
		List<Pagador> pagadores = new ArrayList<Pagador>();

		List<String> arquivo = OfficeUtil.lerArquivo(arquivoPath);
		String numeroDaConta = getNumeroDaConta(arquivo.get(0));
		for (int j = 2; j <= arquivo.size() - 3; j += 2) {

			Boleto b = new Boleto();
			Pagador pagador = new Pagador();

			b.setNumeroDaConta(numeroDaConta);

			// segmento T
			pagador.setNome(getNomePagador(arquivo.get(j)));
			pagador.setCpfCNPJ(getCPFPagador(arquivo.get(j)));
			b.setMovimento(getMovimentoRetorno(arquivo.get(j)));
			b.setNossoNumero(getNumeroDocumento(arquivo.get(j)));
			b.setDecurso(getIsDecurso(arquivo.get(j)));

			// segmento U
			b.setValorPago(Double.parseDouble(getValorPago(arquivo.get(j + 1))) / 100);
			if ((b.getMovimento().equalsIgnoreCase("09") || b.getMovimento().equalsIgnoreCase("02"))
					&& b.getValorPago() == 0D) {
				b.setValorNominal(Double.parseDouble(getValorNominal(arquivo.get(j))));
				b.setValorPago(b.getValorNominal());
			}
			b.setDataPagamento(OfficeUtil.retornaData(getDataEvento(arquivo.get(j + 1))));
			List<Boleto> boletos = new ArrayList<Boleto>();
			boletos.add(b);
			pagador.setBoletos(boletos);
			pagadores.add(pagador);
		}
		
		return pagadores;
	}

	private static String getNumeroDaConta(String linha) {
		return getValor(linha, 66, 70);
	}

	public static List<Pagador> importarExtratoCNAB240(String path) throws ParseException {
		System.out.println("Caminho dos arquivos para importar: " + path);
		List<Pagador> pagadores = new ArrayList<Pagador>();
		File arquivos[];
		File diretorio = new File(path);
		arquivos = diretorio.listFiles();

		for (int i = 0; i < arquivos.length; i++) {
			List<String> arquivo = OfficeUtil.lerArquivo(path + arquivos[i].getName());
			for (int j = 2; j <= arquivo.size() - 3; j += 1) {
				Boleto b = new Boleto();
				Pagador pagador = new Pagador();

				// segmento E
				b.setNossoNumero(getNumeroDocumentoExtratoBancario(arquivo.get(j)));
				b.setValorPago(Double.parseDouble(getValorPagoExtratoBancario(arquivo.get(j))) / 100);
				b.setDataPagamento(OfficeUtil.retornaData(getDataEventoExtratoBancario(arquivo.get(j))));
				List<Boleto> boletos = new ArrayList<Boleto>();
				boletos.add(b);
				pagador.setBoletos(boletos);
				pagadores.add(pagador);
			}
		}
		return pagadores;
	}

	private static boolean getIsDecurso(String string) {
		String decurso = getValor(string, 222, 223);
		if (decurso.equalsIgnoreCase("13")) {
			return true;
		}
		return false;
	}

	private static String getValorNominal(String string) {
		return getValor(string, 82, 94);
	}

	public static String getValor(String linha, int inicio, int fim) {
		return linha.substring(inicio - 1, fim);
	}

	public static String getMovimentoRetorno(String linha) {
		return getValor(linha, 16, 17);
	}

	public static String getIdentificacaoTituloEmpresa(String linha) {
		return getValor(linha, 106, 130);
	}

	public static String getCPFPagador(String linha) {
		return getValor(linha, 134, 148);
	}

	public static String getNomePagador(String linha) {
		return getValor(linha, 149, 188);
	}

	public static String getNumeroContrato(String linha) {
		return getValor(linha, 189, 198);
	}

	public static String getNumeroDocumento(String linha) {
		return getValor(linha, 58, 73);
	}

	public static String getMotivoOcorrencia(String linha) {
		return getValor(linha, 214, 223);
	}

	public static String getValorAcrescimo(String linha) {
		return getValor(linha, 18, 32);
	}

	public static String getValorDesconto(String linha) {
		return getValor(linha, 33, 47);
	}

	public static String getValorPago(String linha) {
		return getValor(linha, 78, 92);
	}

	public static String getDataEvento(String linha) {
		return getValor(linha, 138, 145);
	}

	public static String getNumeroDocumentoExtratoBancario(String linha) {
		return getValor(linha, 202, 239);// nao importa o digito verificador
	}

	public static String getValorPagoExtratoBancario(String linha) {
		return getValor(linha, 151, 168);
	}

	public static String getDataEventoExtratoBancario(String linha) {
		return getValor(linha, 143, 150);
	}

}
