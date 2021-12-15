package com.kakao.insure.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.insure.domain.Agreement;
import com.kakao.insure.domain.AgrmCoverage;
import com.kakao.insure.service.AgreementService;

@RestController
public class AgreementController {
	
	@Autowired
	private AgreementService agreementService;
	
	/**
	 *  API1. 계약 생성 API
	 *  ex) http://localhost:8080/agreement/createContract?prdNm=여행자 보험&startDt=20211203&endDt=20211230&covNmLIst=상해치료비, 항공기 지연도착시 보상금
	 * @param paramPrdNm
	 * @param paramStartDt
	 * @param paramEndDt
	 * @param paramCovNmLIst
	 * @return
	 */
	@RequestMapping("agreement/createContract")
	public ResponseEntity<?> create(@RequestParam(value = "prdNm") String paramPrdNm
			, @RequestParam(value = "startDt") String paramStartDt, @RequestParam(value = "endDt") String paramEndDt, @RequestParam(value = "covNmLIst") String[] paramCovNmLIst){	
		List<String> tmpCovNmList = new ArrayList<String>();
		for(String tmpCovNm : paramCovNmLIst)
		{
			tmpCovNmList.add(tmpCovNm);
			
		}
		Agreement tmpAgreement = agreementService.makeAgreement( "A", paramPrdNm, paramStartDt, paramEndDt, tmpCovNmList);
		String rtnMsg = "";
		
		if( null == tmpAgreement )
		{
			rtnMsg = "Can not make a contract";
		}
		else
		{
			rtnMsg = "Contract making is succed";
			rtnMsg = rtnMsg+"\n";
			rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo() + "], 총 보험료[" + tmpAgreement.getTotPrem() + "원], 보험시작일 : [" + tmpAgreement.getAgrmStartDt() + "], 보험종료일 : [" + tmpAgreement.getAgrmEndDt() + "";
			rtnMsg = rtnMsg + "\n";
			rtnMsg = rtnMsg +"상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 계약상태 : [" + tmpAgreement.getAgrmStat()+"], 계약기간 : [" + tmpAgreement.getContTerm() + "]";
			rtnMsg = rtnMsg + "\n";
			for( AgrmCoverage tmpAgrmCoverage :  tmpAgreement.getAgrmProducts().get(0).getAgrmCoverages())
			{
				rtnMsg = rtnMsg + "담보명 : [ " + tmpAgrmCoverage.getCovNm() + "], 보험료 : [" + tmpAgrmCoverage.getCovPrem()+"원]";
				rtnMsg = rtnMsg + "\n";
			}
		}
		
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
	}
	
	/**
	 * API2-1. 계약 조회( 전체 계약 조회 ) API
	 * http://localhost:8080/agreement/viewAllContract
	 * @return
	 */
	@RequestMapping("agreement/viewAllContract")
	public ResponseEntity<?> viewAllContract(){	
		List<Agreement> tmpAgreements = agreementService.selectAgreementAll();
		String rtnMsg = "";
		for(Agreement tmpAgreement : tmpAgreements)
		{
			rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo()+"], 상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]";
			rtnMsg = rtnMsg + "\n";
		}
		
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
	}
	
	/**
	 * API2-2. 계약 조회( 계약번호로 특정계약 조회 ) API
	 * http://localhost:8080/agreement/findContract?agrmNo=2021121500004 
	 * @param paramAgrmNo
	 * @return
	 */
	@RequestMapping("agreement/findContract")
	public ResponseEntity<?> findContract(@RequestParam(value="agrmNo") String paramAgrmNo ){	
		Agreement tmpAgreement = agreementService.selectAgreement(Long.parseLong(paramAgrmNo));
		String rtnMsg = "";
		
		if( null == tmpAgreement )
		{
			rtnMsg = "There's no agreements";
		}
		else
		{
			rtnMsg = "Search agreements is succed";
			rtnMsg = rtnMsg+"\n";
			rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo() + "], 총 보험료[" + tmpAgreement.getTotPrem() + "원], 보험시작일 : [" + tmpAgreement.getAgrmStartDt() + "], 보험종료일 : [" + tmpAgreement.getAgrmEndDt() + "";
			rtnMsg = rtnMsg + "\n";
			rtnMsg = rtnMsg +"상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 계약상태 : [" + tmpAgreement.getAgrmStat()+"], 계약기간 : [" + tmpAgreement.getContTerm() + "]";
			rtnMsg = rtnMsg + "\n";
			for( AgrmCoverage tmpAgrmCoverage :  tmpAgreement.getAgrmProducts().get(0).getAgrmCoverages())
			{
				rtnMsg = rtnMsg + "담보명 : [ " + tmpAgrmCoverage.getCovNm() + "], 보험료 : [" + tmpAgrmCoverage.getCovPrem()+"원]";
			}
			
		}
	
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
	}
	
	/**
	 * API3. 계약정보 수정 API
	 * ex.) 
	 * 담보삭제
	 * http://localhost:8080/agreement/modifyContract?agrmNo=2021121500002&mdFlag=2&covList=항공기 지연도착시 보상금&endDt=null&status=null
	 * 담보 추가
	 * http://localhost:8080/agreement/modifyContract?agrmNo=2021121500002&mdFlag=1&covList=항공기 지연도착시 보상금&endDt=null&status=null
	 * 종료일 변경
	 * http://localhost:8080/agreement/modifyContract?agrmNo=2021121500002&mdFlag=3&covList=null&endDt=20220103&status=null
	 * 상태 변경
	 * http://localhost:8080/agreement/modifyContract?agrmNo=2021121500002&mdFlag=4&covList=null&endDt=null&status=기간만료
	 * @param paramAgrmNo
	 * @param paramMdFlag
	 * @param paramCovList
	 * @param paramEndDt
	 * @param paramStatus
	 * @return
	 */
	@RequestMapping("agreement/modifyContract")
	public ResponseEntity<?> modifyContract(@RequestParam(value="agrmNo") String paramAgrmNo
			, @RequestParam(value="mdFlag") String paramMdFlag
			, @RequestParam(value="covList") List<String> paramCovList
			, @RequestParam(value="endDt") String paramEndDt
			, @RequestParam(value="status") String paramStatus
			){
		String rtnMsg = "";
		Agreement tmpAgreement = tmpAgreement = agreementService.modifyAgreement(paramAgrmNo, paramMdFlag, paramCovList, paramEndDt, paramStatus);
		if( null == tmpAgreement )
		{
			rtnMsg = "Can not modify a contract";
		}
		else
		{
			rtnMsg = "Contract modify is succed";
			rtnMsg = rtnMsg+"\n";
			rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo() + "], 총 보험료[" + tmpAgreement.getTotPrem() + "원], 보험시작일 : [" + tmpAgreement.getAgrmStartDt() + "], 보험종료일 : [" + tmpAgreement.getAgrmEndDt() + "";
			rtnMsg = rtnMsg + "\n";
			rtnMsg = rtnMsg +"상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 계약상태 : [" + tmpAgreement.getAgrmStat()+"], 계약기간 : [" + tmpAgreement.getContTerm() + "]";
			rtnMsg = rtnMsg + "\n";
			for( AgrmCoverage tmpAgrmCoverage :  tmpAgreement.getAgrmProducts().get(0).getAgrmCoverages())
			{
				rtnMsg = rtnMsg + "담보명 : [ " + tmpAgrmCoverage.getCovNm() + "], 보험료 : [" + tmpAgrmCoverage.getCovPrem()+"원]";
				rtnMsg = rtnMsg + "\n";
			}
			
		}
	
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
			
	}
	
	/**
	 *  API3. 단순보험료계산
	 *  ex) http://localhost:8080/agreement/simpleCal?prdNm=여행자 보험&startDt=20211203&endDt=20211230&covNmLIst=상해치료비, 항공기 지연도착시 보상금, 질병치료비
	 * @param paramPrdNm
	 * @param paramStartDt
	 * @param paramEndDt
	 * @param paramCovNmLIst
	 * @return
	 */
	@RequestMapping("agreement/simpleCal")
	public ResponseEntity<?> simpleCal(@RequestParam(value = "prdNm") String paramPrdNm
			, @RequestParam(value = "startDt") String paramStartDt, @RequestParam(value = "endDt") String paramEndDt, @RequestParam(value = "covNmLIst") String[] paramCovNmLIst){	
		List<String> tmpCovNmList = new ArrayList<String>();
		for(String tmpCovNm : paramCovNmLIst)
		{
			tmpCovNmList.add(tmpCovNm);
			
		}
		Agreement tmpAgreement = agreementService.makeAgreement( "S", paramPrdNm, paramStartDt, paramEndDt, tmpCovNmList);
		String rtnMsg = "";
		if( null == tmpAgreement )
		{
			rtnMsg = "Calculating error!!";
		}
		else
		{
			rtnMsg = "Calculating is succed";
			rtnMsg = rtnMsg+"\n";
			rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo() + "], 총 보험료[" + tmpAgreement.getTotPrem() + "원], 보험시작일 : [" + tmpAgreement.getAgrmStartDt() + "], 보험종료일 : [" + tmpAgreement.getAgrmEndDt() + "";
			rtnMsg = rtnMsg + "\n";
			rtnMsg = rtnMsg +"상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 계약상태 : [" + tmpAgreement.getAgrmStat()+"], 계약기간 : [" + tmpAgreement.getContTerm() + "]";
			rtnMsg = rtnMsg + "\n";
			for( AgrmCoverage tmpAgrmCoverage :  tmpAgreement.getAgrmProducts().get(0).getAgrmCoverages())
			{
				rtnMsg = rtnMsg + "담보명 : [ " + tmpAgrmCoverage.getCovNm() + "], 보험료 : [" + tmpAgrmCoverage.getCovPrem()+"원]";
				rtnMsg = rtnMsg + "\n";
			}
			
		}
		
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
	}
	
	/**
	 * 추가API2. 안내장 발송 기능
	 *   - 현재일 기준 15일 이후 종료일인 계약 List 를 조회 후 안내 한다.
	 * @return
	 */
	@RequestMapping("agreement/expireAgrm")
	public ResponseEntity<?> getExpirationAgreement(){
		List<Agreement> tmpAgreementList = agreementService.getExpirationNotice();
		String rtnMsg = "";
		if( null == tmpAgreementList && tmpAgreementList.isEmpty() ) 
		{
			rtnMsg = "There no Expiration Agreement";
		}
		else {
			rtnMsg = "There are Expiration Agreement after 15Days.";
			for(Agreement tmpAgreement : tmpAgreementList)
			{
				rtnMsg = rtnMsg + "계약번호 : [" + tmpAgreement.getAgrmNo()+"], 상품명 : [ "+ tmpAgreement.getAgrmProducts().get(0).getPrdNm()+ "], 총 가입금액 : [" + tmpAgreement.getAgrmProducts().get(0).getPrdTotAmt() + "], 총 보험료 : [" + tmpAgreement.getTotPrem()+"]";
				rtnMsg = rtnMsg + "\n";
			}
		}
		
		return new ResponseEntity<>(rtnMsg , HttpStatus.OK);
	}	
}
