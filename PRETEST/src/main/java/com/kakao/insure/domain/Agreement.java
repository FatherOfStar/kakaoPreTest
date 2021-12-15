package com.kakao.insure.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Agreement {
	
	@Id
	Long agrmNo;						// agreement No
	
	@Column
	int contTerm;						// Terms of Contract
	
	@Column
	String agrmStat;					// agreement status
	
	@Column
	Long totPrem;						// total premium
	
	@Column
	String agrmStartDt;			// agreement start date
	
	@Column
	String agrmEndDt;				// agreement end date
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "agreement")
	List<AgrmProduct> agrmProducts = new ArrayList<AgrmProduct>();
	
	public void addAgrmProduct(AgrmProduct agrmProduct)
	{
		agrmProducts.add(agrmProduct);
		agrmProduct.setAgreement(this);
	}

}
