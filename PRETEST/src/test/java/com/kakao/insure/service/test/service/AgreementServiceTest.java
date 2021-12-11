package com.kakao.insure.service.test.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.kakao.insure.domain.Agreement;
import com.kakao.insure.domain.Product;
import com.kakao.insure.service.AgreementService;
import com.kakao.insure.service.ProductService;


@SpringBootTest
@Transactional
class AgreementServiceTest{

	@Autowired
	ProductService productService;
	@Autowired
	AgreementService agreementService;
	
//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
//	
	@BeforeEach
	void setUp() throws Exception{
		
		// 상품 등록
		
		String[] tmpCovInformation = {"부분손실,750000,38","전체손실,1570000,40"};
		Product tmpProduct = productService.makeProduct("휴대폰 보험", 1, 12, tmpCovInformation );
		
		String[] tmpCovInformation2 = {"상해치료비,1000000,100","항공기 지연도착시 보상금,500000,1000"};
		tmpProduct = productService.makeProduct("여행자 보험", 1, 3, tmpCovInformation2 );
		
		// 계약 1개 생성
		List<String> covNmList = new ArrayList<String>();
		covNmList.add("상해치료비");
		covNmList.add("항공기 지연도착시 보상금");
		Agreement tmpAgreement = agreementService.makeAgreement("A","여행자 보험", "20211230", "20220130", covNmList);
		
	}
	
	@Test
	void testmakeAgreementTest() {
		List<String> covNmList = new ArrayList<String>();
		covNmList.add("부분손실");
		covNmList.add("전체손실");
		Agreement tmpAgreement = agreementService.makeAgreement("A","휴대폰 보험", "20211230", "20221229", covNmList);
		System.out.println("상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		System.out.println("#####계약 생성 끝");
		System.out.println("#################################################");
	}
	
	/**
	 * 전체 계약 조회
	 * 계약 1개 더 생성 후 전체 계약을 조회한다.
	 */
	@Test
	void findAllAgreementTest() {
		List<String> covNmList = new ArrayList<String>();
		covNmList.add("부분손실");
		covNmList.add("전체손실");
		Agreement tmpRtnAgreement = agreementService.makeAgreement("A","휴대폰 보험", "20211230", "20221229", covNmList);
		List<Agreement> tmpAgreements = agreementService.selectAgreementAll();
		for(Agreement tmpAgreement : tmpAgreements)
		{
			System.out.println("상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		}
		System.out.println("#####계약 전체 조회 끝");
		System.out.println("#################################################");
	}
	
	/**
	 * 계약번호로 계약 조회
	 * 계약 추가 후 추가된 계약까지 조회
	 */
	@Test
	void findAgreementTest() {
		List<String> covNmList = new ArrayList<String>();
		covNmList.add("부분손실");
		covNmList.add("전체손실");
		Agreement tmpRtnAgreement = agreementService.makeAgreement("A","휴대폰 보험", "20211230", "20221229", covNmList);
		
		List<Agreement> tmpAgreements = agreementService.selectAgreementAll();
		for(Agreement tmpAgreement : tmpAgreements)
		{
			System.out.println("계약번호 : [" + tmpAgreement.getAgrmNo() + "], 상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		}
		
		Agreement tmpAgreement = agreementService.selectAgreement(6L);
		System.out.println("상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		
		tmpAgreement = agreementService.selectAgreement(7L);
		System.out.println("상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		
		System.out.println("#####계약 조회 끝");
		System.out.println("#################################################");
	}
	
	/**
	 * 전체 계약 조회
	 * 계약 1개 보험료 계산 후  생성 후 전체 계약을 조회한다.
	 * 계약이 하나만 나와야 정상.
	 */
	@Test
	void onlyCalAgreementTest() {
		List<String> covNmList = new ArrayList<String>();
		covNmList.add("부분손실");
		covNmList.add("전체손실");
		Agreement tmpRtnAgreement = agreementService.makeAgreement("S","휴대폰 보험", "20211230", "20221229", covNmList);
		System.out.println("보험료계산 상품명 : [ "+ tmpRtnAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpRtnAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpRtnAgreement.getTotPrem()+"]");
		List<Agreement> tmpAgreements = agreementService.selectAgreementAll();
		for(Agreement tmpAgreement : tmpAgreements)
		{
			System.out.println("상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]");
		}
		System.out.println("#####계약 전체 조회 끝");
		System.out.println("#################################################");
	}
}
