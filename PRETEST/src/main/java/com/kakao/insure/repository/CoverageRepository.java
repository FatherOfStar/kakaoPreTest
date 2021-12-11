package com.kakao.insure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.Coverage;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, String> {

	
}
