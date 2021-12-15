package com.kakao.insure.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dom4j.dom.DOMNodeHelper.EmptyNodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.insure.domain.Agreement;
import com.kakao.insure.domain.AgrmCoverage;
import com.kakao.insure.domain.AgrmProduct;
import com.kakao.insure.domain.Coverage;
import com.kakao.insure.domain.Product;
import com.kakao.insure.repository.AgreementRepository;
import com.kakao.insure.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgreementService {
	
	private final AgreementRepository agreementRepository;
	@Autowired
	ProductService productService;
	@Autowired
	AgreementNoService agreementNoService;
	/**
	 * 계약을 저장하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public Agreement createAgreement(Agreement paramAgreement){

		if( null ==  paramAgreement.getAgrmNo() )
		{
			paramAgreement.setAgrmNo(Long.parseLong(agreementNoService.createAgreement()));
		}
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
	@Transactional
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
	
	/**
	 * 
	 * @param paramMdFlag (1 : 담보 추가, 2 : 담보 삭제, 3 : EndDate 변경, 4 : 보험상태 변경 )
	 * @param paramAgreementNo
	 * @param paramCovList
	 * @param paramEndDt
	 * @param paramStatus
	 * @return
	 */
	@Transactional
	public Agreement modifyAgreement(String paramAgreementNo, String paramMdFlag, List<String> paramCovNmList, String paramEndDt, String paramStatus) {
		if( null ==  paramAgreementNo || "".equals(paramAgreementNo) ) return null;
		if( null ==  paramMdFlag || "".equals(paramMdFlag)  || "1234".indexOf(paramMdFlag) < 0) return null;
		
		
		Agreement tmpAgreement = this.selectAgreement(Long.parseLong(paramAgreementNo));
		if( null ==  tmpAgreement ) return null;
		
		if( "기간만료".equals(tmpAgreement.getAgrmStat())) return null;
		
		AgrmProduct tmpAgrmProduct = tmpAgreement.getAgrmProducts().get(0);
		String tmpProductNm = tmpAgreement.getAgrmProducts().get(0).getPrdNm();
		Product tmpProduct = productService.selectProduct(tmpProductNm);
		List<Coverage> tmpCoverageList = tmpProduct.getCoverages();
		int tmpAgrmTerm = tmpAgreement.getContTerm();
		int tmpMinTerm = tmpProduct.getMinTerm();
		int tmpMaxTerm = tmpProduct.getMaxTerm();
		if ( "1".equals(paramMdFlag))
		{// Add Coverage
			//agrmCov 세팅
			Long addCovPrem = 0L;
			Long addCovInsAmt = 0L;
			
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
							addCovPrem = addCovPrem + tmpCovPrem;
							addCovInsAmt = addCovInsAmt + tmpCovInsAmt;
							
							tmpAgrmProduct.addAgrmCoverage(tmpAgrmCoverage);
						}
					}
				}
			}
			tmpAgrmProduct.setPrdTotAmt(tmpAgrmProduct.getPrdTotAmt() + addCovInsAmt);
			tmpAgrmProduct.setPrdTotPrem(tmpAgrmProduct.getPrdTotPrem() + addCovPrem);
			
			tmpAgreement.setTotPrem(tmpAgreement.getTotPrem()+addCovPrem);
			
			return tmpAgreement;
		}
		else if( "2".equals(paramMdFlag) )
		{// delete Coverage
			List<AgrmCoverage> tmpAgrmCoverageList = tmpAgrmProduct.getAgrmCoverages();
			Long addCovPrem = 0L;
			Long addCovInsAmt = 0L;
			List<String> addAgrmCov = new ArrayList<String>();
			for(AgrmCoverage tmpAgrmCoverage : tmpAgrmCoverageList)
			{
				boolean isAddTarget = false;
				for( String tmpCovNm : paramCovNmList)
				{
					if( tmpCovNm.equals(tmpAgrmCoverage.getCovNm()) )
					{
						isAddTarget = false;
						break;
					}else
					{
						isAddTarget = true;
					}
				}
				if(isAddTarget) addAgrmCov.add(tmpAgrmCoverage.getCovNm());
				
			}
			// 담보 초기화
			tmpAgrmProduct.getAgrmCoverages().clear();
			
			// 담보 새로 담기
			for(String tmpCovNm : addAgrmCov )
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
							addCovPrem = addCovPrem + tmpCovPrem;
							addCovInsAmt = addCovInsAmt + tmpCovInsAmt;
							
							tmpAgrmProduct.addAgrmCoverage(tmpAgrmCoverage);
						}
					}
				}
		}
		tmpAgrmProduct.setPrdTotAmt( addCovInsAmt);
		tmpAgrmProduct.setPrdTotPrem( addCovPrem);
		
		tmpAgreement.setTotPrem(addCovPrem);
		
		return tmpAgreement;
		}
		else if( "3".equals(paramMdFlag) )
		{// Modify EndDt
			if(DateUtil.isMore(paramEndDt, DateUtil.getCurrentDate()))
				tmpAgreement.setAgrmStat("기간만료");
			else 
				tmpAgreement.setAgrmStat("정상계약");
			String tmpStartDt = tmpAgreement.getAgrmStartDt();
			tmpAgreement.setAgrmEndDt(paramEndDt);
			int newTmpAgrmTerm = DateUtil.diffMonth(tmpStartDt, paramEndDt);
			if( newTmpAgrmTerm < tmpMinTerm  || newTmpAgrmTerm > tmpMaxTerm ) return null;
			tmpAgreement.setContTerm(newTmpAgrmTerm);
			
			// Coverage Name List Up
			List<String> tmpCoverageNmList = new ArrayList<String>();
			List<AgrmCoverage> coverageList = tmpAgrmProduct.getAgrmCoverages();
			for( AgrmCoverage tmpAgrmCoverage : coverageList )
			{
				tmpCoverageNmList.add(tmpAgrmCoverage.getCovNm());
			}
			
			// 상품 초기화
			tmpAgreement.getAgrmProducts().clear();
			 
			// 상품 재 새팅
			// agrmPrd 세팅
			AgrmProduct tmpNewAgrmProduct = new AgrmProduct();
			
			tmpNewAgrmProduct.setPrdNm(tmpProduct.getPrdNm());
			
			Long tmpTotPrem = 0L;
			Long tmpTotInsAmt = 0L;
			
			//agrmCov 세팅
			for(String tmpCovNm : tmpCoverageNmList )
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
							Long tmpCovPrem = this.caculCovPrem(newTmpAgrmTerm, tmpCovInsAmt, tmpStdAmt);
							tmpAgrmCoverage.setCovPrem(tmpCovPrem);
							
							tmpTotPrem += tmpCovPrem;
							tmpTotInsAmt += tmpCovInsAmt;
							
							tmpNewAgrmProduct.addAgrmCoverage(tmpAgrmCoverage);
						}
					}
				}
			}
			tmpNewAgrmProduct.setPrdTotAmt(tmpTotInsAmt);
			tmpNewAgrmProduct.setPrdTotPrem(tmpTotPrem);		
			tmpAgreement.addAgrmProduct(tmpNewAgrmProduct);
			
			tmpAgreement.setTotPrem(tmpTotPrem);
			return tmpAgreement;
			
		}
		else if( "4".equals(paramMdFlag) )
		{// Modify Status
			tmpAgreement.setAgrmStat(paramStatus);
			return tmpAgreement;
		}
		
		return null;
	}
	
	/**
	 * 보험료 계산
	 * @param paramTerms
	 * @param paramInsAmt
	 * @param paramStdAmt
	 * @return
	 */
	public Long caculCovPrem(int paramTerms, Long paramInsAmt, Long paramStdAmt)
	{
		return paramTerms * (paramInsAmt/paramStdAmt);
	}
	
	/**
	 * (현재 일 기준 ) 만기일이 15일 남은 계약 목록 Return
	 * @return
	 */
	public List<Agreement> getExpirationNotice()
	{
		String tmpTargetDate = DateUtil.addYearMonthDay(DateUtil.getCurrentDate(),0,0,+15);
		List<Agreement> tmpAgreementList = agreementRepository.getExpireexpirationAgreement(tmpTargetDate);
		return tmpAgreementList;
	}
		
}
