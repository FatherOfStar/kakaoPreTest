package com.kakao.insure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.insure.domain.AgrmProduct;

@Repository
public interface AgrmProductRepository extends JpaRepository<AgrmProduct, Long> {

	
}
