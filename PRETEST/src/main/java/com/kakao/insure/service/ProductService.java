package com.kakao.insure.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.insure.domain.Coverage;
import com.kakao.insure.domain.Product;
import com.kakao.insure.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;

	/**
	 * 상품을 추가하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public Product makeProduct(String paramPrdNm, int paramMinTerm, int paramMaxTerm, List<String> paramCovInforms){
		Product tmpProduct = new Product();
		tmpProduct.setPrdNm(paramPrdNm);
		tmpProduct.setMinTerm(paramMinTerm);
		tmpProduct.setMaxTerm(paramMaxTerm);
		
		for(String tmpCovinform : paramCovInforms)
		{
			String[] tmpSplitStr = tmpCovinform.split(";");
			Coverage tmpCoverage = new Coverage();
			tmpCoverage.setCovNm(tmpSplitStr[0]);
			tmpCoverage.setCovInsAmt(Long.parseLong(tmpSplitStr[1]));
			tmpCoverage.setCovStdAmt(Long.parseLong(tmpSplitStr[2]));
			tmpProduct.addCoverage(tmpCoverage);
		}
						
		return this.createProduct(tmpProduct);
	}
	
	/**
	 * 상품을 저장하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public Product createProduct(Product product){

		productRepository.save(product);	
						
		return product;
	}
	
	/**
	 * 상품 전체 조회 서비스
	 * @return
	 */
	@Transactional
	public List<Product> selectProductAll() {
	 
		 List<Product> productList = productRepository.findAll();
		 
		return productList;
	}
	
	/**
	 * 특정 상품 전체 조회 서비스
	 * @return
	 */
	@Transactional
	public Product selectProduct(String paramPrdNm) {
	 
		 Product product =  productRepository.findById(paramPrdNm).orElse(null);
		 
		return product;
	}
	
	/**
	 * 특정 상품 담보 추가 서비스
	 * @return
	 */
	@Transactional
	public Product addCoverage(String paramPrdNm, List<String> paramCovInforms) {
	 
		 Product tmpProduct =  productRepository.findById(paramPrdNm).orElse(null);
		 for(String tmpCovinform : paramCovInforms)
			{
				String[] tmpSplitStr = tmpCovinform.split(";");
				Coverage tmpCoverage = new Coverage();
				tmpCoverage.setCovNm(tmpSplitStr[0]);
				tmpCoverage.setCovInsAmt(Long.parseLong(tmpSplitStr[1]));
				tmpCoverage.setCovStdAmt(Long.parseLong(tmpSplitStr[2]));
				tmpProduct.addCoverage(tmpCoverage);
			}
		 
		return this.createProduct(tmpProduct);
	}
	
}
