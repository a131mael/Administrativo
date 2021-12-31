/*

d * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.aff.Administrativo.CNAB_240;


import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import br.com.service.administrativo.escola.FinanceiroEscolaService;
import br.com.service.administrativo.escola.FinanceiroEscolarService;
import br.com.service.administrativo.util.Util;

@Singleton
@Startup
public class RotinaAutomatica {

	
	@Inject
	private CNAB240 cnab240;
	
	@Inject
	private FinanceiroEscolarService financeiroEscolarService;
	
	@Inject
	private FinanceiroEscolaService financeiroEscolaService;
		
	@Schedule(minute = "*/2", persistent = false)
	public void importarPagamento() {
		System.out.println("Importando pagamentos do banco......");
		cnab240.importarPagamentosCNAB240();
	}
	
	/*@Schedule(hour = "23", persistent = false)
	public void importarPagamento2() {
		System.out.println("Importando pagamentos do banco......");
		cnab240.importarPagamentosCNAB240();
	}*/
	
	/*@Schedule( minute = "50", hour = "9", persistent = false)
	public void atualizarBoletoProtestado() {
		System.out.println("Setando boleto como protestado.....");
		try{
			financeiroEscolarService.updateBoletoProtesto();
		}catch(Exception e){
		}
		try{
			financeiroEscolaService.updateBoletoProtesto();
		}catch(Exception e){
			
		}
	}
	*/
/*	@Schedule( minute = "50", hour = "22", persistent = false)
	public void atualizarBoletoProtestado2() {
		System.out.println("Setando boleto como protestado.....");
		try{
			financeiroEscolarService.updateBoletoProtesto();
		}catch(Exception e){
		}
		try{
			financeiroEscolaService.updateBoletoProtesto();
		}catch(Exception e){
			
		}
	}*/
	
	@Schedule( minute = "*/2", hour = "*", persistent = false)
	public void geradorDeCnabDeEnvio() {
		System.out.println("Gerando Arquivo CNAB de envio......");

		int mes = 0;
		Date d = new Date();
		Util u = new Util();
		mes = u.getMesInt(d);
		
		mes ++;
		cnab240.gerarCNAB240DeEnvio(mes);
	}

}
