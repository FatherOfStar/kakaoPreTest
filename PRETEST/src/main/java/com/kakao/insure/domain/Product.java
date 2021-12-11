package com.kakao.insure.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
public class Product {
	
	@Id
	String prdNm;					// Product name
	
	@Column
	int minTerm;						// min Term
	
	@Column
	int maxTerm;						// max Term

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
	List<Coverage> coverages = new ArrayList<Coverage>();
	
	public void addCoverage(Coverage coverage)
	{
		coverages.add(coverage);
		coverage.setProduct(this);
	}
	
}