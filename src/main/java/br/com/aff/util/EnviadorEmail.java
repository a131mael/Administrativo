//package br.com.aff.util;
//
//import javax.ejb.LocalBean;
//import javax.ejb.Stateless;
//
//@Stateless
//@LocalBean
//public class EnviadorEmail {
//
////	@Inject
////	private FinanceiroEscolaService financeiroService;
////
////	public EnviadorEmail() {
////		if (financeiroService == null) {
////			try {
////				financeiroService = (FinanceiroEscolaService) ServiceLocator.getInstance().getFinanceiroService(
////						FinanceiroEscolaService.class.getSimpleName(), FinanceiroEscolaService.class.getName());
////			} catch (NamingException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////	}
////
////	public void enviarEmailBoletosMesAtual() {
////		Calendar c = Calendar.getInstance();
////		List<Boleto> boletos = financeiroService.getBoletoMes(c.get(Calendar.MONTH));
////		for (Boleto bol : boletos) {
////			enviarEmailBoletosMesAtual(bol);
////		}
////
////	}
////
////	public void enviarEmailBoletosMesAtual(Boleto bol) {
////		String destinatario = "";
////		if (bol.getPagador().getEmailMae() != null) {
////			destinatario += bol.getPagador().getEmailMae() + ",";
////		}
////		if (bol.getPagador().getEmailPai() != null) {
////			destinatario += bol.getPagador().getEmailPai() + ",";
////		}
////
////		byte[] anexoPDF = byteArrayPDFBoleto(getBoletoFinanceiro(bol), bol.getPagador(), bol.getContrato());
////
////		String corpoEmail = "<!DOCTYPE html><html><body><p><h2><center>Colégio Adonai.</center></h2><center>"
////				+ "<a href=\"https://ibb.co/mF1WjR\"><img src=\"https://preview.ibb.co/dPMmJm/logo.jpg\" "
////				+ "alt=\"logo\" border=\"0\" style=\"width:92px;height:92px;border:0;\" ></a><br/><br/></center>Prezado(a) #nomeResponsavel,"
////				+ "<br/><p><br/><br/>Você esta recebendo o seu boleto do Colégio Adonai referente ao mês de <b><font size=\"2\" color=\"blue\"> #mesBoleto</font>"
////				+ "</b> .<h3><center><font size=\"3\" color=\"blue\">Resumo da conta</font></center>"
////				+ "</h3>Vencimento  :<font size=\"3\" color=\"blue\"> #vencimentoBoleto</font>"
////				+ "<br/>Valor       :<font size=\"3\" color=\"blue\"> #valorAtualBoleto</font><br/><br/><br/><br/><center><h4>"
////				+ "<font size=\"3\" color=\"red\"> Caso já tenha efetuado o pagamento favor desconsiderar esse e-mail. </font></h4></center></p><br/>"
////				+ "<a href=\"https://ibb.co/jubLuR\"><img src=\"https://preview.ibb.co/bJMW16/assinatura_Email.png\" alt=\"assinatura_Email\" border=\"0\" "
////				+ "style=\"width:365px;height:126px;border:0;\"></a></body></html>";
////		corpoEmail = corpoEmail.replace("#vencimentoBoleto", Formatador.formataData(bol.getVencimento()));
////		corpoEmail = corpoEmail.replace("#valorAtualBoleto", Formatador.valorFormatado(Verificador.getValorFinal(bol)));
////		corpoEmail = corpoEmail.replace("#nomeResponsavel", bol.getContrato().getNomeResponsavel());
////		corpoEmail = corpoEmail.replace("#mesBoleto", Formatador.getMes(bol.getVencimento()));
////
////		ByteArrayInputStream bais = new ByteArrayInputStream(anexoPDF);
////		org.aaf.financeiro.util.EnviadorEmail.enviarEmail("Boleto - Colégio Adonai", corpoEmail, bais, destinatario,
////				CONSTANTES.emailFinanceiro, CONSTANTES.senhaEmailFinanceiro);
////
////	}
////
////	private org.aaf.financeiro.model.Boleto getBoletoFinanceiro(Boleto boleto) {
////		org.aaf.financeiro.model.Boleto boletoFinanceiro = new org.aaf.financeiro.model.Boleto();
////		boletoFinanceiro.setEmissao(boleto.getEmissao());
////		boletoFinanceiro.setId(boleto.getId());
////		boletoFinanceiro.setValorNominal(boleto.getValorNominal());
////		boletoFinanceiro.setVencimento(boleto.getVencimento());
////		boletoFinanceiro.setNossoNumero(String.valueOf(boleto.getNossoNumero()));
////		boletoFinanceiro.setDataPagamento(OfficeUtil.retornaDataSomenteNumeros(boleto.getDataPagamento()));
////		boletoFinanceiro.setValorPago(boleto.getValorPago());
////		return boletoFinanceiro;
////	}
////
////	public byte[] byteArrayPDFBoleto(org.aaf.financeiro.model.Boleto boleto, Aluno aluno, ContratoAluno contrato) {
////		Calendar c = Calendar.getInstance();
////		c.setTime(boleto.getVencimento());
////		CNAB240_SICOOB cnab = new CNAB240_SICOOB(2);
////		Pagador pagador = new Pagador();
////		pagador.setBairro(contrato.getBairro());
////		pagador.setCep(contrato.getCep());
////		pagador.setCidade(contrato.getCidade() != null ? contrato.getCidade() : "PALHOCA");
////		pagador.setCpfCNPJ(contrato.getCpfResponsavel());
////		pagador.setEndereco(contrato.getEndereco());
////		pagador.setNome(contrato.getNomeResponsavel());
////		pagador.setNossoNumero(boleto.getNossoNumero() + "");
////		pagador.setUF("SC");
////		List<org.aaf.financeiro.model.Boleto> boletos = new ArrayList<>();
////		boletos.add(boleto);
////		pagador.setBoletos(boletos);
////
////		byte[] pdf = cnab.getBoletoPDF(pagador);
////
////		return pdf;
////	}
////
////	public void enviarEmailBoletoAtrasado(Boleto bol, String remetente, String senhaRemetente) {
////		String destinatario = "";
////		if (bol.getPagador().getEmailMae() != null) {
////			destinatario += bol.getPagador().getEmailMae() + ",";
////		}
////		if (bol.getPagador().getEmailPai() != null) {
////			destinatario += bol.getPagador().getEmailPai() + ",";
////		}
////
////		String corpoEmail = "<!DOCTYPE html><html><body><center><a href=\"https://ibb.co/mF1WjR\"><img src=\"https://preview.ibb.co/dPMmJm/logo.jpg\" alt=\"logo\" border=\"0\" style=\"width:92px;height:92px;border:0;\" >"
////				+ "</a></center>Prezado(a) #nomeResponsavel,<br/><p>Verificamos em nosso sistema que que o boleto com vencimento em  <b>#vencimentoBoleto </b>ainda está em aberto, o boleto encontra-se anexo no e-mail, você pode paga-lo em qualquer agência da Sicoob ou diretamente na secretaria da escola."
////				+ "<br/></br>Caso deseje um boleto atualizado para pagamento em qualquer agência bancária ou pela internet entre em contato com a secretaria da escola e solicite."
////				+ "<br/><br/>O valor atual do boleto é <b>#valorAtualBoleto."
////				+ "</b><br/><h4>Caso já tenha efetuado o pagamento favor desconsiderar esse e-mail. </h4>"
////				+ "</p><br/><br/><a href=\"https://ibb.co/jubLuR\"><img src=\"https://preview.ibb.co/bJMW16/assinatura_Email.png\" alt=\"assinatura_Email\" border=\"0\" style=\"width:365px;height:126px;border:0;\">"
////				+ "</a></body></html>";
////
////		corpoEmail = corpoEmail.replace("#vencimentoBoleto", Formatador.formataData(bol.getVencimento()));
////		corpoEmail = corpoEmail.replace("#nomeResponsavel", bol.getContrato().getNomeResponsavel());
////		corpoEmail = corpoEmail.replace("#valorAtualBoleto", Formatador.valorFormatado(Verificador.getValorFinal(bol)));
////		byte[] anexoPDF = byteArrayPDFBoleto(getBoletoFinanceiro(bol), bol.getPagador(), bol.getContrato());
////
////		ByteArrayInputStream bais = new ByteArrayInputStream(anexoPDF);
////		org.aaf.financeiro.util.EnviadorEmail.enviarEmail("Colégio Adonai - Boleto Atrasado", corpoEmail, bais,
////				destinatario, remetente, senhaRemetente);
////		try {
////			Thread.sleep(3000);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void enviarEmailBoletoAtrasado(String remetente, String senhaRemetente) {
////		Calendar c = Calendar.getInstance();
////		List<Boleto> boletos = financeiroService.getBoletosAtrasados(c.get(Calendar.MONTH));
////		for (Boleto bol : boletos) {
////			enviarEmailBoletoAtrasado(bol, remetente, senhaRemetente);
////		}
////	}
////
////	public void enviarEmailBoletosMesAtualEAtrasados(Long idAluno) {
////		enviarEmailBoletosMesAtual(getBoletoMesAtual(idAluno));
////		
////		List<Boleto> boletosAtrasados = getBoletosAtrasados(idAluno);
////		if(boletosAtrasados != null && !boletosAtrasados.isEmpty()){
////			for(Boleto boleto : boletosAtrasados){
////				enviarEmailBoletoAtrasado(boleto, CONSTANTES.emailFinanceiro,CONSTANTES.senhaEmailFinanceiro);
////			}
////		}
////	}
////
////	private List<Boleto> getBoletosAtrasados(Long idAluno) {
////		Calendar c = Calendar.getInstance();
////		List<Boleto> boletos = financeiroService.getBoletosAtrasadosAluno(c.get(Calendar.MONTH),idAluno);
////		return boletos;
////	}
////
////	private Boleto getBoletoMesAtual(Long idAluno) {
////		Calendar c = Calendar.getInstance();
////		Boleto boleto = financeiroService.getBoletoMes(c.get(Calendar.MONTH),idAluno);
////		return boleto;
////	}
//
//}
