package com.kakao.insure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.AgrmCoverage;

@Repository
public interface AgrmCoverageRepository extends JpaRepository<AgrmCoverage, Long> {

	
}
