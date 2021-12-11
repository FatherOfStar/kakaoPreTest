package com.kakao.insure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Coverage {
	
	@Id
	String covNm;						// CoverageName
	
	@ManyToOne
	@JoinColumn(name = "prdNm")
	Product product;
	
	@Column
	Long covInsAmt;					// Coverage Insured Amount
	
	@Column
	Long covStdAmt;				// Coverage Standard Amount

}
