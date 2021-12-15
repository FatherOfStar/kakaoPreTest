package com.kakao.insure.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.insure.domain.Product;
import com.kakao.insure.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
		
	@RequestMapping("/")
	public String sampleRestApiTestHello() {					
		return "Hello";		
	}
	
	/**
	 * 추가 API1 -1. 상품 생성 API
	 * ex) http://localhost:8080/product/createProduct?prdNm=여행자 보험&minTerm=1&maxTerm=3&covInform=상해치료비;1000000;100,항공기 지연도착시 보상금;500000;1000
	 * @param paramPrdNm
	 * @param paramMinTerm
	 * @param paramMaxTerm
	 * @param paramCovInform
	 * @return
	 */
	@RequestMapping("product/createProduct")
	public ResponseEntity<?> create(@RequestParam(value = "prdNm") String paramPrdNm, 
			@RequestParam(value = "minTerm") int paramMinTerm
			, @RequestParam(value = "maxTerm") int paramMaxTerm
			,@RequestParam(value="covInform") List <String> paramCovInform){	
		
		String[] tmpCovInformation = {"상해치료비,1000000,100","항공기 지연도착시 보상금,500000,1000"}; 
		for(String covIn : paramCovInform)
		{
			System.out.println("########[" + covIn + "]############");
		}
		
		Product tmpProduct = productService.makeProduct(paramPrdNm, paramMinTerm, paramMaxTerm, paramCovInform );
		log.debug("Controller_Create_TEST");
		String rtnMessage = null;
		
		if( tmpProduct == null ) rtnMessage = "Can Not Make Product!!";
		else rtnMessage = "Make Success";
				
		return new ResponseEntity<>(rtnMessage , HttpStatus.OK);
	}

	/**
	 * 추가 API1-2. 담보 추가 API
	 * ex) http://localhost:8080/product/addCoverage?prdNm=여행자 보험&covInform=질병치료비;1000000;100
	 * @param paramPrdNm
	 * @param paramCovInform
	 * @return
	 */
	@RequestMapping("product/addCoverage")
	public ResponseEntity<?> addCoverage(@RequestParam(value = "prdNm") String paramPrdNm, 
			@RequestParam(value="covInform") List <String> paramCovInform){	
		
		for(String covIn : paramCovInform)
		{
			System.out.println("########[" + covIn + "]############");
		}
		
		Product tmpProduct = productService.addCoverage(paramPrdNm,paramCovInform);
		String rtnMessage = null;
		
		if( tmpProduct == null ) rtnMessage = "Can Not Add Coverage!!";
		else rtnMessage = "Coverage Add Success";
				
		return new ResponseEntity<>(rtnMessage , HttpStatus.OK);
	}
}
