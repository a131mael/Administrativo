package br.com.administrativo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Configuracao implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int anoLetivo;
    
    @Column
    private Short anoRematricula;
    
    @Column
    private long sequencialArquivoCNAB;
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public int getAnoLetivo() {
		return anoLetivo;
	}


	public void setAnoLetivo(int anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public long getSequencialArquivoCNAB() {
		return sequencialArquivoCNAB;
	}

	public void setSequencialArquivoCNAB(long sequencialArquivoCNAB) {
		this.sequencialArquivoCNAB = sequencialArquivoCNAB;
	}

	public Short getAnoRematricula() {
		return anoRematricula;
	}

	public void setAnoRematricula(Short anoRematricula) {
		this.anoRematricula = anoRematricula;
	}


}
