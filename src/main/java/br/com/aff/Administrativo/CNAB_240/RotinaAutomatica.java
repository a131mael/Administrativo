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

import br.com.service.util.Util;


@Singleton
@Startup
public class RotinaAutomatica {

	
	@Inject
	private CNAB240 cnab240;
		
	@Schedule( minute = "*/4", hour = "*", persistent = false)
	public void automaticTimeout() {
		System.out.println("Importando pagamentos do banco......");
		cnab240.importarPagamentosCNAB240();
	}
	
	@Schedule( minute = "*/8", hour = "*", persistent = false)
	public void geradorDeCnabDeEnvio() {
		/*System.out.println("Gerando Arquivo CNAB de envio......");
		int mes = Util.getMesInt(new Date());
		mes ++;
		cnab240.gerarCNAB240DeEnvio(mes);*/
	}
	


}
