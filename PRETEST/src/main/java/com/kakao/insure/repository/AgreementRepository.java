package com.kakao.insure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

	
}
