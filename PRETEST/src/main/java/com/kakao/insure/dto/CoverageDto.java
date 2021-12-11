package com.kakao.insure.dto;

import com.kakao.insure.domain.Coverage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverageDto {
	
	private Coverage coverage;

}
