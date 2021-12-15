package com.kakao.insure.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.insure.domain.AgreementNo;
import com.kakao.insure.repository.AgreementNoRepository;
import com.kakao.insure.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgreementNoService {
	
	private final AgreementNoRepository agreementNoRepository;
	
	/**
	 * 채번 테이블을 저장하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public AgreementNo saveAgreement(AgreementNo paramAgreementNo){

		AgreementNo tmpAgreementNo = agreementNoRepository.save(paramAgreementNo);	
						
		return tmpAgreementNo;
	}
	
	/**
	 * 특정 계약을 삭제하는 Service
	 * @param product
	 * @return
	 */
	@Transactional
	public void deleteAgreementNo(String Date){

		agreementNoRepository.deleteById(Date);	
						
	}
	
	/**
	 * 채번 테이블 조회 서비스
	 * @param tDate
	 * @return
	 */
	@Transactional
	public AgreementNo selectAgreement(String paramTDate) {
	 
		AgreementNo tmpAgreementNo =  agreementNoRepository.findById(paramTDate).orElse(null);
		 
		return tmpAgreementNo;
	}
	
	/**
	 * 계약번호 채번 서비스
	 * 날짜 + 00001~ 부터 계약번호 생성
	 * @return
	 */
	@Transactional
	public String createAgreement() {
		String tmpTDate = DateUtil.getCurrentDate();
		// return Agreement number
		int tmpAgrNo;
		String tmptDate = "";
		
	
		AgreementNo tmpAgreementNo =  this.selectAgreement(tmpTDate);
		if( null == tmpAgreementNo)
		{
			tmpAgreementNo = new AgreementNo();
			tmpAgreementNo.setTDate(tmpTDate);
			tmpAgreementNo.setAgrmNo(1);
			tmpAgreementNo = this.saveAgreement(tmpAgreementNo);
			tmptDate = tmpAgreementNo.getTDate();
			tmpAgrNo = tmpAgreementNo.getAgrmNo();
			
		}else
		{
			tmpAgrNo = tmpAgreementNo.getAgrmNo()+1;
			tmptDate = tmpAgreementNo.getTDate();
			tmpAgreementNo.setAgrmNo(tmpAgrNo);
		}
		return tmptDate + String.format("%05d", tmpAgrNo);
		
	}
		
}
