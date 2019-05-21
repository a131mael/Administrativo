package br.com.service.escola;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;		
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.administrativo.model.Boleto;
import br.com.service.util.Service;

@Stateless
public class FinanceiroEscolaService extends Service {

	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	// TODO pegar da configuracao
	private int anoLetivo = 2018;


	public double getPrevisto(int mes) {
		if (mes >= 0) {
			try {
				Calendar c = Calendar.getInstance();
				c.set(anoLetivo, mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(anoLetivo, mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT sum(bol.valorNominal) from Boleto bol ");
				sql.append("where 1=1 ");
				sql.append(" and bol.vencimento >= '");
				sql.append(c.getTime());
				sql.append("'");
				sql.append(" and bol.vencimento < '");
				sql.append(c2.getTime());
				sql.append("'");
				sql.append(" and bol.pagador.removido = false");
				
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
				c.set(anoLetivo, mes, 1, 0, 0, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(anoLetivo, mes, c.getMaximum(Calendar.MONTH), 23, 59, 59);

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
		sql.append( " and UPPER(trim( REPLACE(REPLACE(REPLACE(cont.nomeresponsavel,'.',''),'Ã' ,'' ),'Ç',''))) =  "  );
		sql.append( "UPPER('" );
		sql.append( nomePagador.trim() );
		sql.append( "')" );
		try{
			em.flush();
			Query query = em.createNativeQuery(sql.toString());
			int at = query.executeUpdate();
			System.out.println("boletosAtualizados = " + at);
			em.flush();
		}catch(Exception e){
			Query query2 = em.createNativeQuery(sql.toString());
			int at = query2.executeUpdate();
			System.out.println("boletosAtualizados = " + at);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		List<Object[]>  boletos = null;
		try{
			Query query = em.createNativeQuery(sql.toString());
			boletos =query.getResultList(); 
		}catch(Exception e){
						
		}

		StringBuilder sqlcontratoAluno = new StringBuilder();
		sqlcontratoAluno.append("select * from ContratoAluno ca ");   
		sqlcontratoAluno.append(" where id = ");
		
		List<Boleto>  bb = new ArrayList<Boleto>();
		for (Object[] a : boletos) {
			BigInteger id = (BigInteger) a[0];
			Date emissao = (Date) a[1];
			BigInteger nossoNumero = (BigInteger) a[2];
			double valorNominal = (Double) a[3];
			Date vencimento = (Date) a[4];
			BigInteger pagador_id = (BigInteger) a[5];
			Date dataPagamento = (Date) a[6];
			BigInteger idContrato = (BigInteger) a[14];
			double valorPago = 0;
			if(a[7] != null){
				valorPago = (Double) a[7];	
			}
						
			
			Object[]  contrato = null;
			try{
				if(idContrato != null){
					StringBuilder sbContrato = new StringBuilder(sqlcontratoAluno.toString());
					sbContrato.append(idContrato);
					Query query2 = em.createNativeQuery(sbContrato.toString());
					contrato =(Object[]) query2.getSingleResult();	
				}
				
			}catch(Exception e){
							
			}
			
			Boleto b = new Boleto();
			b.setId(id.longValue());
			b.setEmissao(emissao);
			b.setNossoNumero(nossoNumero.toString());
			b.setValorNominal(valorNominal);
			b.setVencimento(vencimento);
			b.setDataPagamento(dataPagamento);
			b.setValorPago(valorPago);
			if(contrato != null){
				b.setCep((String)contrato[5]);
				b.setCidade((String)contrato[6]);	
			}
			
			
			bb.add(b);
			
		}
		
		return bb;
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


