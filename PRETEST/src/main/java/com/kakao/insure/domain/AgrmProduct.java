package com.kakao.insure.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class AgrmProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long pkId;
	
	@Column
	String prdNm;						// Product Name
	
	@ManyToOne
	@JoinColumn(name = "agrmNo")
	Agreement agreement;
		
	@Column
	Long prdTotAmt;				// Product Total Amount
	
	@Column
	Long prdTotPrem;				// Product Total Premium
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "agrmProduct", orphanRemoval = true)
	List<AgrmCoverage> agrmCoverages = new ArrayList<AgrmCoverage>();
	
	public void addAgrmCoverage(AgrmCoverage agrmagrmCoverage)
	{
		agrmCoverages.add(agrmagrmCoverage);
		agrmagrmCoverage.setAgrmProduct(this);
	}

}
