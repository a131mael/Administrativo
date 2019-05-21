package br.com.service.escola;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.administrativo.model.Boleto;
import br.com.service.util.Service;

@Stateless
public class FinanceiroEscolarService extends Service {

	@PersistenceContext(unitName = "EscolarDS")
	private EntityManager em;

	@Inject
	private ConfiguracaoService configuracaoService;


	public double getPrevisto(int mes) {
		if (mes >= 0) {
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT sum(bol.valorNominal) from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.vencimento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" and (bol.cancelado = false");
				sql.append(" or  bol.cancelado is null)");
				
				Query query = em.createQuery(sql.toString());
				Double boleto = (Double) query.getSingleResult();
				return boleto;
			} catch (NoResultException nre) {
				return 0D;
			}
		}
		return 0D;

	}

	public Double getPago(int mes) {
		if (mes >= 0) {
			try {
				Calendar c = Calendar.getInstance();
				c.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(configuracaoService.getConfiguracao().getAnoLetivo(), mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT sum(bol.valorPago) from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.dataPagamento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.dataPagamento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" and bol.pagador.removido = false");
				
				Query query = em.createQuery(sql.toString());
				Object retorno = query.getSingleResult();
				Double boleto = null;
				if (retorno != null) {
					boleto = (Double) retorno;
				} else {
					boleto = 0D;
				}

				return boleto;
			} catch (NoResultException nre) {
				return 0D;
			}
		}
		return 0D;

	}


	public void updateBoleto(Long numeroBoleto, String nomePagador, Double valor, Date dataPagamento, Boolean extrato) {
		em.flush();
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE boleto as bol ");
		sql.append("SET");
		sql.append(" valorpago = ");
		sql.append(valor);
		sql.append(", datapagamento = '");
		sql.append(dataPagamento);
		sql.append("'");
		sql.append(", conciliacaoporextrato = ");
		sql.append(extrato);
		
		sql.append(" from ContratoAluno as cont ");
		sql.append(" WHERE ");
		
		sql.append("bol.id = ");
		sql.append(numeroBoleto);
		
		sql.append(" and bol.contrato_id = cont.id ");
		sql.append( " and UPPER(trim( REPLACE(REPLACE(REPLACE(cont.nomeresponsavel,'.',''),'Ã' ,'' ),'Ç',''))) = "  );
		sql.append( "UPPER('" );
		sql.append( nomePagador.trim() );
		sql.append( "')" );
		try{
			em.flush();
			Query query = em.createNativeQuery(sql.toString());
			int at = query.executeUpdate();
			if(at == 0){
				System.out.println("nao encontrou boleto com id e nome do reponsavel  = " + nomePagador.trim() + " = "+ numeroBoleto);
			}
			System.out.println("boletosAtualizados = " + at);
		}catch(Exception e){
			Query query2 = em.createNativeQuery(sql.toString());
			int at = query2.executeUpdate();
			if(at == 0){
				System.out.println("nao encontrou boleto com id e nome do reponsavel  = " + nomePagador.trim() + " = "+ numeroBoleto);
			}
			System.out.println("boletosAtualizados = " + at);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		em.flush();
	}
	
	public List<Boleto> findBoletos(boolean cancelado, boolean arquivoGerado) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from boleto b ");   
		sql.append(" where (b.manteraposremovido is null or b.manteraposremovido = false) ");
		sql.append(" and b.cancelado = ");
		sql.append(cancelado);
		sql.append(" and (b.enviadoparabanco is null or b.enviadoparabanco = ");
		sql.append(arquivoGerado);
		sql.append(")");
		
		Query query = em.createNativeQuery(sql.toString());
		List<Boleto> boletos = query.getResultList();
		if(boletos == null){
			boletos = new ArrayList<Boleto>();
		}
		boletos.size();
		return boletos;
	}

	public void updateBoletoProtesto(Long numeroBoleto, String nomePagador, Boolean extrato) {
		em.flush();
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE boleto as bol ");
		sql.append("SET");
		sql.append(" valorpago = ");
		sql.append(0);
		sql.append(", datapagamento = null");
		sql.append(", conciliacaoporextrato = ");
		sql.append(extrato);
		
		sql.append(" from ContratoAluno as cont ");
		sql.append(" WHERE ");
		
		sql.append("bol.id = ");
		sql.append(numeroBoleto);
		
		sql.append(" and bol.contrato_id = cont.id ");
		sql.append( " and UPPER(trim(cont.nomeresponsavel)) = "  );
		sql.append( "UPPER('" );
		sql.append( nomePagador.trim() );
		sql.append( "')" );
		try{
			em.flush();
			Query query = em.createNativeQuery(sql.toString());
			int at = query.executeUpdate();
			if(at == 0){
				System.out.println("nao encontrou boleto com id e nome do reponsavel  = " + nomePagador.trim() + " = "+ numeroBoleto);
			}
			System.out.println("boletosAtualizados = " + at);
		}catch(Exception e){
			Query query2 = em.createNativeQuery(sql.toString());
			int at = query2.executeUpdate();
			if(at == 0){
				System.out.println("nao encontrou boleto com id e nome do reponsavel  = " + nomePagador.trim() + " = "+ numeroBoleto);
			}
			System.out.println("boletosAtualizados = " + at);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		try {
			StringBuilder sqlUpdateContrato = new StringBuilder();
			sqlUpdateContrato.append("UPDATE ContratoAluno as ca ");
			sqlUpdateContrato.append("SET protestado = true");
			sqlUpdateContrato.append(" from ContratoAluno_boleto as cab ");
			sqlUpdateContrato.append(" WHERE ");
			sqlUpdateContrato.append(" cab.contratoaluno_id = ca.id ");
			sqlUpdateContrato.append(" and cab.boletos_id = ");
			sqlUpdateContrato.append(numeroBoleto);
			sqlUpdateContrato.append( " and UPPER(trim(ca.nomeresponsavel)) = "  );
			sqlUpdateContrato.append( "UPPER('" );
			sqlUpdateContrato.append( nomePagador.trim() );
			sqlUpdateContrato.append( "')" );
			
			Query query = em.createNativeQuery(sqlUpdateContrato.toString());
			int at2 = query.executeUpdate();
			System.out.println(at2 + " : updates");
			
		} catch (Exception exp) {
		}
		
		em.flush();
	}

}


