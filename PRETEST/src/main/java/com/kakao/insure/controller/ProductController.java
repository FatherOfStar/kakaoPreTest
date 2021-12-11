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
	
	
	@RequestMapping("/createProduct")
	public ResponseEntity<?> create(@RequestParam(value = "prdList") List<Product> paramProductList ){	
		log.debug("Controller_Create_TEST");
		List<Product> tmpProductList = new ArrayList<Product>();
		for( Product tmpProduct : paramProductList) {		
			tmpProductList.add(productService.createProduct(tmpProduct));
		}
				
		return new ResponseEntity<>(tmpProductList , HttpStatus.OK);
	}

}
