package com.kakao.insure.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

	/**
	 * EndDate 기준 만기 15일 전 안내장 발송 
	 * @param appldate
	 * @return
	 */
	@Query(value = "SELECT * FROM agreement  WHERE agrm_end_dt = :tDate"
			  , nativeQuery = true)
	ArrayList<Agreement> getExpireexpirationAgreement (@Param("tDate" ) String paramTDate);
}
