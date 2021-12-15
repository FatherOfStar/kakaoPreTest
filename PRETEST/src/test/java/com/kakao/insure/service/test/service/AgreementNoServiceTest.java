package com.kakao.insure.service.test.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.kakao.insure.domain.Agreement;
import com.kakao.insure.domain.AgreementNo;
import com.kakao.insure.domain.Product;
import com.kakao.insure.service.AgreementNoService;
import com.kakao.insure.service.AgreementService;
import com.kakao.insure.service.ProductService;


@SpringBootTest
@Transactional
class AgreementNoServiceTest{

	@Autowired
	AgreementNoService agreementNoService;
	
	@Test
	void testCreateAgreementTest() {
		String AgrmNo = agreementNoService.createAgreement();
		
		System.out.println("########## 계약번호 1: [ "+ AgrmNo+ "]");
		
		AgrmNo = agreementNoService.createAgreement();
		
		System.out.println("########## 계약번호 2: [ "+ AgrmNo+ "]");
		
		AgrmNo = agreementNoService.createAgreement();
		
		System.out.println("########## 계약번호 3: [ "+ AgrmNo+ "]");
		
		System.out.println("#####계약	번호 생성 끝");
		System.out.println("#################################################");
	}
	
}
