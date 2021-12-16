package com.kakao.insure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class AgreementNo {
	
	@Id
	String tDate;							// pk
	
	@Column(unique = true)
	@NotNull
	int agrmNo;						// AgremNOPerDay
	
}