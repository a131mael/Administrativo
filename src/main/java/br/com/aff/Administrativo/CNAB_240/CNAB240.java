/*
d * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.aff.Administrativo.CNAB_240;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.aaf.financeiro.sicoob.util.CNAB240_REMESSA_SICOOB;
import org.aaf.financeiro.util.ImportadorArquivo;

import br.com.administrativo.model.Boleto;
import br.com.administrativo.model.Pagador;
import br.com.service.escola.ConfiguracaoService;
import br.com.service.escola.FinanceiroEscolaService;
import br.com.service.escola.FinanceiroEscolarService;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class CNAB240 {

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private FinanceiroEscolaService financeiroEscolaService;

	@Inject
	private FinanceiroEscolarService financeiroEscolarService;

	// ADONAI E TEFAMEL
	public void importarBoletos(List<Pagador> boletosImportados, boolean extratoBancario) throws ParseException {

		for (Pagador pagador : boletosImportados) {
			Boleto boletoCNAB = pagador.getBoletos().get(0);
			String numeroDocumento = boletoCNAB.getNossoNumero();
			if (numeroDocumento != null && !numeroDocumento.equalsIgnoreCase("") && !numeroDocumento.contains("-")	&& !numeroDocumento.contains("/")) {
				try {
					numeroDocumento = numeroDocumento.trim().replace(" ", "").replace("/",	"".replace("-", "").replace(".", ""));
					if (numeroDocumento.matches("^[0-9]*$")) {
						Long numeroDocumentoLong = Long.parseLong(numeroDocumento);
						if (!extratoBancario) {
							if ( numeroDocumentoLong > 100000) {
								numeroDocumentoLong -= 100000;
							}else{
								numeroDocumentoLong -= 10000;
							}
						} else {
							String numeroDocumentoExtrato = String.valueOf(numeroDocumentoLong);
						}
						System.out.println(pagador.getNome() + "  " + numeroDocumentoLong);
						if(10027 == numeroDocumentoLong){
							System.out.println("fdff");
						}
						
						if (numeroDocumentoLong != null && numeroDocumentoLong > 0) {
							if (boletoCNAB.getNumeroDaConta() != null && boletoCNAB.getNumeroDaConta().equalsIgnoreCase("49469")) {
								if (!(boletoCNAB.isDecurso() != null && boletoCNAB.isDecurso())) {
									financeiroEscolaService.updateBoleto(numeroDocumentoLong, pagador.getNome(), boletoCNAB.getValorPago(), boletoCNAB.getDataPagamento(), extratoBancario);
									System.out.println("YESS, BOLETO PAGO da ADONAI");	
								}
								else if ((boletoCNAB.isDecurso() != null && boletoCNAB.isDecurso())) {
									financeiroEscolaService.updateBoletoProtesto(numeroDocumentoLong, pagador.getNome(), extratoBancario);
									System.out.println("DECURSO PQP");	
								}
							}

							if (boletoCNAB.getNumeroDaConta() != null	&& boletoCNAB.getNumeroDaConta().equalsIgnoreCase("77426")) {
								if (!(boletoCNAB.isDecurso() != null && boletoCNAB.isDecurso())) {
									financeiroEscolarService.updateBoleto(numeroDocumentoLong, pagador.getNome(), boletoCNAB.getValorPago(), boletoCNAB.getDataPagamento(), extratoBancario);
									System.out.println("YESS, BOLETO PAGO da TEFAMEL");	
								}else if ((boletoCNAB.isDecurso() != null && boletoCNAB.isDecurso())) {
									financeiroEscolarService.updateBoletoProtesto(numeroDocumentoLong, pagador.getNome(), extratoBancario);
									System.out.println("DECURSO PQP");	
								}
								
							}
						}
					}

				} catch (ClassCastException cce) {
					cce.printStackTrace();
				}
			}
		}

	}

	public void importarPagamentosCNAB240() {
		try {
			System.out.println("Lendo arquivos");
			String path = CONSTANTES.LOCAL_ARMAZENAMENTO_REMESSA;
			File arquivos[];
			File diretorio = new File(path);
			arquivos = diretorio.listFiles();

			Date hj = new Date();

			int qtdadeArquivosProcessados = arquivos.length;
			if (qtdadeArquivosProcessados > 1) {
				qtdadeArquivosProcessados = 1;
			}
			List<Pagador> boletosImportados = null;
			for (int i = 0; i < qtdadeArquivosProcessados; i++) {
				try {
					boletosImportados = CNAB240_RETORNO_SICOOB.imporCNAB240(path + arquivos[i].getName());
					System.out.println("QTADE boleto importado =  " + boletosImportados.size());

					importarBoletos(boletosImportados, false);
					System.out.println("Importou boletos e fez update");

					try {
						OfficeUtil.moveFile(path + arquivos[i].getName(),CONSTANTES.LOCAL_ARMAZENAMENTO_REMESSA_IMPORTADA + OfficeUtil.retornaDataSomenteNumeros(hj) + arquivos[i].getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {

				}
			}

		} catch (Exception e) {

		}
	}

	public void gerarArquivoBaixaBoletos(Boolean cancelado, int projeto) {
		List<Boleto> boletosCancelados = new ArrayList<Boleto>();

		if (CONSTANTES.projetoAdonai == projeto) {
			List<Boleto> boletosCanceladosADONAI = financeiroEscolaService.findBoletos(true, false);
			boletosCancelados.addAll(boletosCanceladosADONAI);
		} else if (CONSTANTES.projetoTefamel == projeto) {
			List<Boleto> boletosCanceladosTEFAMEL = financeiroEscolarService.findBoletos(true, false);
			boletosCancelados.addAll(boletosCanceladosTEFAMEL);
		}

		for (Boleto b : boletosCancelados) {
			gerarBaixaBoletosCancelados(b, projeto, CONSTANTES.PATH_ENVIAR_CNAB);
		}

	}

	private void gerarBaixaBoletosCancelados(Boleto b, int projeto, String path) {
		byte[] arquivo = gerarCNB240Baixa(projeto, b);
		String nomeArquivo = "CNAB240_" + b.getNossoNumero() + ".txt";
		ImportadorArquivo.geraArquivoFisico(arquivo, path + nomeArquivo);
		// financeiroService.save(b);

	}

	public byte[] gerarCNB240Baixa(int projeto, Boleto b) {
		try {

			String sequencialArquivo = configuracaoService.getSequencialArquivo() + "";

			Pagador pagador = new Pagador();
			pagador.setBairro("PALHOCA"); // TODO BUSCAR NO BANCO A CIDADE

			pagador.setCep(b.getCep());
			pagador.setCidade(b.getCidade() != null ? b.getCidade() : "PALHOCA");
			pagador.setCpfCNPJ(b.getCpfResponsavel());
			pagador.setEndereco(b.getEndereco());
			pagador.setNome(b.getNomeResponsavel());
			pagador.setNossoNumero(b.getNossonumero());
			pagador.setUF("SC");
			ArrayList<Boleto> boletos = new ArrayList();
			boletos.add(b);
			pagador.setBoletos(boletos);
			// CNAB240_REMESSA_SICOOB cnbRemessa = new
			// CNAB240_REMESSA_SICOOB(projeto);
			// byte[] arquivo = cnbRemessa.geraBaixa(pagador,
			// sequencialArquivo);

			try {
				configuracaoService.incrementaSequencialArquivoCNAB();
				// return arquivo;

				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void gerarCNAB240DeEnvio(int mes) {
		//PROJETO 	
		// 1 = Tefamel
		// 2 = Adonai
	CNAB240_REMESSA_SICOOB j = new CNAB240_REMESSA_SICOOB(1);
	}
	
	
}
