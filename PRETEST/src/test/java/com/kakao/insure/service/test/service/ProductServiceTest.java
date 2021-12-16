package com.kakao.insure.service.test.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakao.insure.domain.Product;
import com.kakao.insure.service.ProductService;

@SpringBootTest
@Transactional
class ProductServiceTest {

	@Autowired
	ProductService productService;

	@BeforeEach
	void setUp() throws Exception{
		List<String> tmpCovInformation = new ArrayList<String>();
		tmpCovInformation.add("상해치료비;1000000;100");
		tmpCovInformation.add("항공기 지연도착시 보상금;500000;1000");
		productService.makeProduct("여행자 보험", 1, 3, tmpCovInformation );
	}
		
	/**
	 * 상품 등록 테스트
	 */
	@Test
	void testMakeProduct() {
		System.out.println("###########################################################");
		System.out.println("###### 상품등록");
		List<String> tmpCovInformation = new ArrayList<String>();
		tmpCovInformation.add("부분손실;750000;38");
		tmpCovInformation.add("전체손실;1570000;40");
		Product tmpProduct = productService.makeProduct("휴대폰 보험", 1, 12, tmpCovInformation );
		System.out.println(tmpProduct.getPrdNm() + " 담보갯수 = " + tmpProduct.getCoverages().size());
		System.out.println("###### 상품등록");
		System.out.println("###########################################################");
		
	}
	
	/**
	 * 모든 상품 조회
	 */
	@Test
	void testSelectAllProduct() {
		System.out.println("###########################################################");
		System.out.println("###### 모든상품조회");
		List<Product> tmpProducts = productService.selectProductAll();
		for(Product tmpProduct : tmpProducts)
		{
			System.out.println(tmpProduct.getPrdNm() + " 담보갯수 = " + tmpProduct.getCoverages().size());
		}
		System.out.println("###### 모든상품조회끝");
		System.out.println("###########################################################");
		
		
	}
	
	/**
	 * 특정 상품 조회
	 */
	@Test
	void testSelectProduct() {
		System.out.println("###########################################################");
		System.out.println("###### 특정상품조회");
		Product tmpProduct = productService.selectProduct("여행자 보험");
		System.out.println(tmpProduct.getPrdNm() + " 담보갯수 = " + tmpProduct.getCoverages().size());
		System.out.println("###### 특정상품조회끝");
		System.out.println("###########################################################");
		
	}

	/**
	 * 담보 추가 테스트
	 */
	@Test
	void testAddCoverageTest() {
		System.out.println("###########################################################");
		System.out.println("###### 담보추가");
		List<String> tmpCovInformation = new ArrayList<String>();
		tmpCovInformation.add("부분손실;750000;38");
		tmpCovInformation.add("전체손실;1570000;40");
		Product tmpProduct = productService.addCoverage("여행자 보험", tmpCovInformation );
		System.out.println(tmpProduct.getPrdNm() + " 담보갯수 = " + tmpProduct.getCoverages().size());
		System.out.println("###### 담보추가");
		System.out.println("###########################################################");
		
		List<Product> tmpProducts = productService.selectProductAll();
		for(Product product : tmpProducts)
		{
			System.out.println(product.getPrdNm() + " 담보갯수 = " + tmpProduct.getCoverages().size());
		}
	}
}
