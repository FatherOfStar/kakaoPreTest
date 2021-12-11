package com.kakao.insure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class AgrmCoverage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long pkId;
	
	@Column
	String covNm;						// coverage Name
	
	@ManyToOne
	@JoinColumn(name = "ParentId")
	AgrmProduct agrmProduct;
	
	
	@Column
	Long covInsAmt;					// Coverage insure amount
	
	@Column
	Long covStdAmt;				//Coverage standard amount
	
	@Column
	Long covPrem;					// coverage premium
	

}
