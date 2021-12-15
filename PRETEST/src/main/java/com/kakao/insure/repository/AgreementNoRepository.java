package com.kakao.insure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.AgreementNo;

@Repository
public interface AgreementNoRepository extends JpaRepository<AgreementNo, String> {

	
}
