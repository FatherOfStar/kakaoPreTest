# 20211208-ysi
유상인
# 서버사전과제 3 - 계약관리시스템
## 개발환경
- 기본 환경   
    - IDE: Eclipse  
    - OS: Window  
    - GIT  
- Server
    - Java8
    - Spring Boot 
    - JPA
    - H2
    - Gradle
    - Junit  


- 접속 Base URI: `http://localhost:8080`

## 빌드(Eclipse ide 환경)
 1. Git Clone https://github.com/kakao-insurance-quiz/20211208-ysi.git
 2. Gradle Project import
 3. Refresh Gradle proejct
 4. Build
 5. Run Spring Boot
 
 ## 참고사항
 - 계약기간은 월단위로 관리합니다.
 - 계약자, 피보험자에 대한 사항은 별도로 관리하지 않습니다.
 - 보험료의 수납에 대한 사항은 별도로 관리하지 않습니다.
 - 보험료는 소수접 3번째 자리에서 절사(예. 27.5892 -> 27.58) 합니다.
 - 가입시 가입금액은 담보별로 고정되어 있습니다.
 - 기간만료 상태의 계약은 계약 변경 업무가 불가능 합니다.
 
 ## 도메인 (ENTITY)
 - PRODUCT ( 상품 테이블 ) - 상품 정보
   PRD_NM ( 상품명 ) - PK
   MAX_TERM ( 최대보험기간 )
   MIN_TERM ( 최소보험기간 )
 
 - COVERAGE ( 담보 테이블 ) - 담보 정보 - PRODUCT 와 ONT TO MANY 관계
   COV_NM( 담보명 ) - PK
   COV_INS_AMT(보험가입금액)
   COV_STD_AMT(기준금액)
   PRD_NM( 상품명 ) - FK
   
  - AGREEMENT ( 계약기본 테이블 ) - 계약 기본 정보
    AGRM_NO( 계약번호 ) - PK
    AGRM_END_DT ( 계약종료일 )
    AGRM_START_DT ( 계약시작일 )
    AGRM_STAT ( 계약상태 )
    CONT_TERM ( 계약기간(월) )
    TOT_PREM( 총 보험료 )
 
   -  AGRM_PRODUCT - 계약 하위 가입 상품 정보 - AGREEMENT 와 ONE TO MANY 관계
     PK_ID( PK_ID ) - PK
     PRD_NM ( 상품명 )
     PRD_TOT_AMT ( 상품 총 가입금액 )
     PRD_TOT_PREM( 상품 총 보험료)
     AGRM_NO ( 계약번호 ) - FK
     
   - AGRM_COVERAGE - 가입상품 하위 가입 담보 정보 - AGRM_PRODUCT 와 ONE TO MANY 관계
     PK_ID( PK_ID ) - PK
     COV_NM( 담보명 )
     COV_INS_AMT( 담보가입금액 )
     COV_STD_AMT( 담보기준금액 )
     COV_PREM( 담보 보험료 )
     PARENT_ID( AGRM_PRODUCT의 PK_ID ) - FK
     
   - AGREMENT_NO - 계약번호 채번 테이블
     T_DATE ( 일자 )  - PK
     AGRM_NO( 일자별 채번 )
        
 ## Source 목록
### Controller
- Agreementontroller.java
- ProductController.java 

### Domain
- Agreement.java
- AgreementNo.java
- AgrmProduct.java
- AgrmCoverage.java
- product.java
- coverage.java

### Repository
- AgreementRepository.java
- AgreementNoRepository.java
- AgrmProductRepository.java
- AgrmCoverageRepository.java
- productRepository.java
- coverageRepository.java

### Service 
- AgreementNoService.java
- AgreementService.java
- ProductService.java

### DTO
- AgreementDto.java
- AgreementNoDto.java
- AgrmProductDto.java
- AgrmCoverageDto.java
- productDto.java
- coverageDto.java

### UTIL CLASS
- DateUtil.java

## API 기능 명세
### 1. 계약생성 API
 - 최초 계약 생성시상태는 정상계약으로 간주합니다.
   - (추가) 현재일보다 계약종료일이 먼저 일 경우 기간만료로 간주 합니다.
 - 총 보험료는 계약 생성시점에 서버에서 계산합니다.
 - (추가) 계약번호는 채번 테이블을 이용하여 현재일자 + 5자리로 생성 합니다.
    예) 20211215 + 00001 = 2021121500001
 - (추가) 계약기간은 계약종료일 - 계약시작일로 결정 합니다.
 - Request ( agreement/createContract )
 - Controller Method 명 : create(@RequestParam(value = "prdNm") String paramPrdNm, @RequestParam(value = "startDt") String paramStartDt, @RequestParam(value = "endDt") String paramEndDt, @RequestParam(value = "covNmLIst") String[] paramCovNmLIst)
 - Service 명 : agreementService.makeAgreement
 - Unit Test : testmakeAgreementTest
 - TestURL : http://localhost:8080/agreement/createContract?prdNm=여행자 보험&startDt=20211203&endDt=20211230&covNmLIst=상해치료비, 항공기 지연도착시 보상금
 - 출력 예시
 Contract making is succed
계약번호 : [2021121500001], 총 보험료[10500원], 보험시작일 : [20211203], 보험종료일 : [20211230
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [1]
담보명 : [ 상해치료비], 보험료 : [10000원]
담보명 : [ 항공기 지연도착시 보상금], 보험료 : [500원]
 
 
### 2. 계약정보조회 API
 - 계약정보를 전달받아서 해당 계약의 상세 내용을 리턴합니다.
 - (추가)계약번호를 전달받아 해당 계약의 상세 내용을 리턴 합니다. 
 - Request ( agreement/findContract )
 - Controller Method 명 : findContract(@RequestParam(value="agrmNo") String paramAgrmNo )
 - Service 명 : agreementService.selectAgreement
 - Unit Test : selectAgreement
 - TestURL : http://localhost:8080/agreement/findContract?agrmNo=2021121500001
 - 출력 예시
 Search agreements is succed
계약번호 : [2021121500001], 총 보험료[10000원], 보험시작일 : [20211203], 보험종료일 : [20211230
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [1]
담보명 : [ 상해치료비], 보험료 : [10000원]담보명 : [ 항공기 지연도착시 보상금], 보험료 : [500원]담보명 : [ 상해치료비], 보험료 : [10000원]

### (추가)2-1. 계약정보전체 List 조회 API
 - (추가) 계약정보전체 List 를 조회 합니다.
 - Request ( agreement/viewAllContract )
 - Controller Method 명 : viewAllContract()
 - Service 명 : agreementService.selectAgreementAll
 - Unit Test : findAllAgreementTest
 - TestURL : http://localhost:8080/agreement/viewAllContract
 - 출력 예시
 계약번호 : [2021121500001], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
계약번호 : [2021121500002], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
계약번호 : [2021121500003], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
계약번호 : [2021121500004], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
계약번호 : [2021121500005], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
계약번호 : [2021121500006], 상품명 : [ 여행자 보험], 총 가입금액 : [1500000], 총 보험료 : [10500]
 
 
### 3. 계약정보 수정 API
 - 해당 계약에 대해서 계약내용 변경 업무를 수행합니다.
 - 변경 가능한 정보는 다음과 같습니다.
 1) 담보 추가/삭제
 2) 계약기간 변경(시작일은 변경 불가, 기간만 변경 가능)
      -(추가) 종료일을 변경할 수 있도록 하여 기간이 변경되도록 한다.
 3) 계약상태 변경(단, 기간만료 상태에서는 변경 불가)
 - Request ( agreement/modifyContract )
 - Controller Method 명 : modifyContract(@RequestParam(value="agrmNo") String paramAgrmNo
			, @RequestParam(value="mdFlag") String paramMdFlag
			, @RequestParam(value="covList") List<String> paramCovList
			, @RequestParam(value="endDt") String paramEndDt
			, @RequestParam(value="status") String paramStatus
			)
 - Service 명 : modifyAgreement
 - Unit Test : 
 - TestURL
   1) 담보삭제 : http://localhost:8080/agreement/modifyContract?agrmNo=2021121500001&mdFlag=2&covList=항공기 지연도착시 보상금, 질병치료비&endDt=null&status=null
   2) 담보 추가 : http://localhost:8080/agreement/modifyContract?agrmNo=2021121500001&mdFlag=1&covList=항공기 지연도착시 보상금, 질병치료비&endDt=null&status=null
   3) 종료일 변경 : http://localhost:8080/agreement/modifyContract?agrmNo=2021121500001&mdFlag=3&covList=null&endDt=20220103&status=null
   4) 상태 변경 : http://localhost:8080/agreement/modifyContract?agrmNo=2021121500001&mdFlag=4&covList=null&endDt=null&status=기간만료
 - 출력 예시
   1) 항공기 지연도착시 보상금 담보 삭제
   Contract modify is succed
계약번호 : [2021121500001], 총 보험료[10000원], 보험시작일 : [20211203], 보험종료일 : [20211230
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [1]
담보명 : [ 상해치료비], 보험료 : [10000원]
   
   2) 항공기 지연도착시 보상금 담보 추가
   Contract modify is succed
계약번호 : [2021121500001], 총 보험료[20500원], 보험시작일 : [20211203], 보험종료일 : [20211230
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [1]
담보명 : [ 상해치료비], 보험료 : [10000원]
담보명 : [ 항공기 지연도착시 보상금], 보험료 : [500원]
담보명 : [ 질병치료비], 보험료 : [10000원]

  3) 계약종료일 변경
  Contract modify is succed
계약번호 : [2021121500001], 총 보험료[21000원], 보험시작일 : [20211203], 보험종료일 : [20220104
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [2]
담보명 : [ 상해치료비], 보험료 : [20000원]
담보명 : [ 항공기 지연도착시 보상금], 보험료 : [1000원]

4) 계약상태 변경
Contract modify is succed
계약번호 : [2021121500001], 총 보험료[21000원], 보험시작일 : [20211203], 보험종료일 : [20220104
상품명 : [ 여행자 보험], 계약상태 : [기간만료], 계약기간 : [2]
담보명 : [ 상해치료비], 보험료 : [20000원]
담보명 : [ 항공기 지연도착시 보상금], 보험료 : [1000원]

 
### 4. 예상 총 보험료 계산 API
 - 보험 가입 전 보험료를 미리 산출해 보기 위한 기능입니다. 상품/담보 정보와 계약기간을 통해서 예상되는 보험료를 리턴합니다.
 - Request ( agreement/simpleCal )
 - Controller Method 명 : simpleCal(@RequestParam(value = "prdNm") String paramPrdNm
			, @RequestParam(value = "startDt") String paramStartDt, @RequestParam(value = "endDt") String paramEndDt, @RequestParam(value = "covNmLIst") String[] paramCovNmLIst)
 - Service 명 : agreementService.makeAgreement
 - Unit Test : onlyCalAgreementTest
 - TestURL : http://localhost:8080/agreement/simpleCal?prdNm=여행자 보험&startDt=20211203&endDt=20211230&covNmLIst=상해치료비, 항공기 지연도착시 보상금
 - 출력 예시
 Calculating is succed
계약번호 : [null], 총 보험료[10500원], 보험시작일 : [20211203], 보험종료일 : [20211230
상품명 : [ 여행자 보험], 계약상태 : [정상계약], 계약기간 : [1]
담보명 : [ 상해치료비], 보험료 : [10000원]
담보명 : [ 항공기 지연도착시 보상금], 보험료 : [500원]
 
 
### 선택 1. 상품 생성 API
 - 기존 상품에 담보를 추가하는것도 가능합니다.
 - Request ( product/createProduct )
 - Controller Method 명 : create(@RequestParam(value = "prdNm") String paramPrdNm, 
			@RequestParam(value = "minTerm") int paramMinTerm
			, @RequestParam(value = "maxTerm") int paramMaxTerm
			,@RequestParam(value="covInform") List <String> paramCovInform)
 - Service 명 : productService.makeProduct
 - Unit Test : testMakeProduct
 - TestURL : http://localhost:8080/product/createProduct?prdNm=여행자 보험&minTerm=1&maxTerm=3&covInform=상해치료비;1000000;100,항공기 지연도착시 보상금;500000;1000, 질병치료비;1000000;100
 - 출력 예시 
  Make Success

### 선택 1-1. 담보 추가 API
 - 기존 상품에 담보를 추가하는것도 가능합니다.
 - Request ( product/addCoverage )
 - Controller Method 명 : addCoverage(@RequestParam(value = "prdNm") String paramPrdNm, 
			@RequestParam(value="covInform") List <String> paramCovInform)
 - Service 명 : productService.addCoverage
 - Unit Test : testSelectProduct
 - TestURL : http://localhost:8080/product/addCoverage?prdNm=여행자 보험&covInform=질병치료비;1000000;100
 - 출력 예시
  Coverage Add Success
 
### 선택 2. 안내장 발송 기능
 - 만기가 도래한 계약에 대해 일주일 전 안내장을 발송하는 기능입니다.
 - 단, 발송은 실제로 이루어질 필요 없이 시스템 출력이나 로그 메세지로 대체하셔도 됩니다.
 - Request ( agreement/expireAgrm )
 - Controller Method 명 :getExpirationAgreement()
 - Service 명 : agreementService.getExpirationNotice
 - Unit Test : expirationNoticeTest
 - TestURL : 
 - 출력 예시