package br.com.service.escola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import br.com.administrativo.model.Configuracao;
import br.com.service.util.Service;

@Stateless
public class ConfiguracaoService extends Service {

	
	@PersistenceContext(unitName = "EscolaDS")
	private EntityManager em;

	public Configuracao findById(EntityManager em, Long id) {
		return em.find(Configuracao.class, id);
	}

	public Configuracao findById(Long id) {
		return em.find(Configuracao.class, id);
	}
	
	public Configuracao findByCodigo(Long id) {
		return em.find(Configuracao.class, id);
	}
	
	public String remover(Long idEvento){
		em.remove(findById(idEvento));
		return "index";
	}

	public Configuracao getConfiguracao(){
		return findAll().get(0);
	}
	
	public List<Configuracao> findAll() {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Configuracao> criteria = cb.createQuery(Configuracao.class);
			Root<Configuracao> member = criteria.from(Configuracao.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
			criteria.select(member).orderBy(cb.asc(member.get("id")));
			return em.createQuery(criteria).getResultList();
	
		}catch(NoResultException nre){
			return new ArrayList();
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public Configuracao save(Configuracao configuracao) {
		Configuracao user = null;
		try {

			if (configuracao.getId() != null && configuracao.getId() != 0L) {
				user = findById(configuracao.getId());
			} else {
				user = new Configuracao();
			}
			
			user.setAnoLetivo(configuracao.getAnoLetivo());
			user.setAnoRematricula(configuracao.getAnoRematricula());
			
			em.persist(user);
			
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap();
			responseObj.put("email", "Email taken");

		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap();
			responseObj.put("error", e.getMessage());

			e.printStackTrace();
		}

		return user;
	}
	
	public long getSequencialArquivo(){
		return getConfiguracao().getSequencialArquivoCNAB();
	}
	
	public void incrementaSequencialArquivoCNAB(){
		long sequecial = getSequencialArquivo();
		sequecial++;
		Configuracao conf = getConfiguracao();
		conf.setSequencialArquivoCNAB(sequecial);
		save(conf);
	}

	
}

