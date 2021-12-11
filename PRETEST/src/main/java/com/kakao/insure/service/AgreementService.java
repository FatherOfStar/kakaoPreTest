package com.kakao.insure.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.insure.domain.Agreement;
import com.kakao.insure.domain.AgrmCoverage;
import com.kakao.insure.domain.AgrmProduct;
import com.kakao.insure.domain.Coverage;
import com.kakao.insure.domain.Product;
import com.kakao.insure.repository.AgreementRepository;
import com.kakao.insure.repository.AgrmProductRepository;
import com.kakao.insure.repository.AgrmCoverageRepository;
import com.kakao.insure.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgreementService {
	
	private final AgreementRepository agreementRepository;
	private final AgrmProductRepository agrmProductRepository;
	private final AgrmCoverageRepository agrmCoverageRepository;
	@Autowired
	ProductService productService;
	/**
	 * 계약을 저장하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public Agreement createAgreement(Agreement paramAgreement){

		Agreement tmpAgreement = agreementRepository.save(paramAgreement);	
						
		return tmpAgreement;
	}
	
	/**
	 * 특정 계약을 삭제하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public void deleteAgreement(Long AgreementNo){

		agreementRepository.deleteById(AgreementNo);	
						
	}
	
	/**
	 * 계약 전체 조회 서비스
	 * @return
	 */
	@Transactional
	public List<Agreement> selectAgreementAll() {
	 
		 List<Agreement>tmpAgreementList = agreementRepository.findAll();
		 
		return tmpAgreementList;
	}
	
	/**
	 * 특정 계약 조회 서비스
	 * @return
	 */
	@Transactional
	public Agreement selectAgreement(Long paramAgreementNm) {
	 
		Agreement tmpAgreement =  agreementRepository.findById(paramAgreementNm).orElse(null);
		 
		return tmpAgreement;
	}
	
	/**
	 * 사용자 정보로 계약 생성 및 보험료계산
	 * @param paramPrcs		// A 는 계약생성, S 는 보험료계산
	 * @param paramPrdNm
	 * @param paramStartDt
	 * @param paramEndDt
	 * @param paramCovNmList
	 * @return
	 */
	public Agreement makeAgreement(String paramPrcs, String paramPrdNm, String paramStartDt, String paramEndDt, List<String> paramCovNmList) {
		Agreement tmpAgreement = new Agreement();
		
		// 상품객체 조회
		Product tmpProduct = productService.selectProduct(paramPrdNm);
		if( null == tmpProduct ) return null;
		int minTerm = tmpProduct.getMinTerm();
		int maxTerm = tmpProduct.getMaxTerm();
		List<Coverage> tmpCoverageList = (List<Coverage>) tmpProduct.getCoverages();
		
		// Agreement 세팅
		// 계약종료일이 현재일 이전 일 경우 계약상태는 기간 만료 이다.
		if(DateUtil.isMore(paramEndDt, DateUtil.getCurrentDate()))
			tmpAgreement.setAgrmStat("기간만료");
		else 
			tmpAgreement.setAgrmStat("정상계약");
		tmpAgreement.setAgrmStartDt(paramStartDt);
		tmpAgreement.setAgrmEndDt(paramEndDt);
		int tmpAgrmTerm = DateUtil.diffMonth(paramStartDt, paramEndDt);
		if( tmpAgrmTerm < minTerm  || tmpAgrmTerm > maxTerm ) return null;
		tmpAgreement.setContTerm(tmpAgrmTerm);
		
		// agrmPrd 세팅
		AgrmProduct tmpAgrmProduct = new AgrmProduct();
		
		tmpAgrmProduct.setPrdNm(paramPrdNm);
		Long tmpTotPrem = 0L;
		Long tmpTotInsAmt = 0L;
		//agrmCov 세팅
		List<AgrmCoverage> tmpAgrmCoverageList = new ArrayList<AgrmCoverage>();
		for(String tmpCovNm : paramCovNmList )
		{
			AgrmCoverage tmpAgrmCoverage = new AgrmCoverage();
			for(Coverage tmpCoverage : tmpCoverageList)
			{
				if( null != tmpCoverage )
				{
					if( tmpCovNm.equals(tmpCoverage.getCovNm()) )
					{
						tmpAgrmCoverage.setCovNm(tmpCovNm);
						Long tmpCovInsAmt = tmpCoverage.getCovInsAmt();
						Long tmpStdAmt = tmpCoverage.getCovStdAmt();
						tmpAgrmCoverage.setCovInsAmt(tmpCovInsAmt);
						tmpAgrmCoverage.setCovStdAmt(tmpStdAmt);
						Long tmpCovPrem = this.caculCovPrem(tmpAgrmTerm, tmpCovInsAmt, tmpStdAmt);
						tmpAgrmCoverage.setCovPrem(tmpCovPrem);
						
						tmpTotPrem += tmpCovPrem;
						tmpTotInsAmt += tmpCovInsAmt;
						
						tmpAgrmCoverage.setAgrmProduct(tmpAgrmProduct);
						tmpAgrmProduct.addAgrmCoverage(tmpAgrmCoverage);
					}
				}
			}
		}
		tmpAgrmProduct.setPrdTotAmt(tmpTotInsAmt);
		tmpAgrmProduct.setPrdTotPrem(tmpTotPrem);		
		tmpAgreement.addAgrmProduct(tmpAgrmProduct);
		
		tmpAgreement.setTotPrem(tmpTotPrem);
		if( null != paramPrcs && !"".equals(paramPrcs) && "A".equals(paramPrcs) )
			return this.createAgreement(tmpAgreement);
		else
			return tmpAgreement;
		
	}
	
	public Long caculCovPrem(int paramTerms, Long paramInsAmt, Long paramStdAmt)
	{
		return paramTerms * (paramInsAmt/paramStdAmt);
	}
	
}
