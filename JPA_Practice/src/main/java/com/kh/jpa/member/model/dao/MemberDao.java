package com.kh.jpa.member.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.jpa.member.model.vo.Member;

/*
 * * JPA 에서의 DAO (Repository)
 * 
 * - 기존 MyBatis 에서느 DAO 클래스를 직접 만들고, SqlSessionTemplate 을 매개변수로 전달받아
 *   sqlSession.selectOne("매퍼.아이디", xxx); 형태로 직접 호출했었음!!
 * - 또한, SQL 구문도 Mapper XML 에 직접 작성했었음!!
 * 
 * - JPA 에서는 DAO 를 클래스가 아니라 "인터페이스" 로 선언한다.
 * - 또한, JPA 에서 제공하는 JpaRepository 인터페이스를 상속 (extends) 받기만 하면 된다.
 * - @Repository 어노테이션도 필요 없음 (내부적으로 자동 등록됨)
 * 
 * * JPA 의 동작 원리
 * 
 * - JpaRepository 라는 인터페이스 기본 CRUD 작업용 메소드가 이미 다 만들어져있음
 * - Mapper XML 파일이 필요 없고 (쿼리문을 직접 작성 X) 메소드 호출만 잘 하면 된다!!
 * 
 * [ 표현법 ]
 * extend JpaRepository<Entity타입, PK타입>
 * - Entity타입 : 이 Repository 가 관리할 Entity 클래스명 (VO 클래스명)
 * - PK타입 : 해당 Entity 의 @Id 필드의 타입
 * 
 * 예) MemberDao 에서는 Member 의 @Id 가 String userId 이므로
 *     extends JpaRepository<Member, String>   
 */

public interface MemberDao extends JpaRepository<Member, String> {

	/*
	 * * JpaRepository 가 기본으로 제공하는 메소드들
	 *   (기본적인 쿼리문은 별도로 선언하지 않더라도 바로 가져다 쓸 수 있다)
	 *   
	 * 1. 전체 조회 : findAll()
	 * 내부적으로
	 * SELECT *
	 *   FROM MEMBER
	 * 전체조회 쿼리문이 실행 (이 때 Entity 타입이 곧 FROM 절의 테이블명이 됨)
	 * - 리턴타입 : List<Member> (이 떄 Entity 타입이 곧 List<Entity타입> 이 됨)
	 * 
	 * 2. PK 로 단일행 조회 : findById(PK값)
	 * 내부적으로
	 * SELECT *
	 *   FROM MEMBER
	 *  WHERE USER_ID = ?
	 * PK 로 단일행 조회 쿼리문이 실행 (이 떄 WHERE 절이 곧 PK 타입에 대한 조건식이 됨)
	 * - 리턴타입 : Optional<Member> (조회 결과가 없을 수도 있으므로 Optional 로 감싸서 반환)
	 * 
	 * 3. 저장 (INSERT / UPDATE) : save(Entity)
	 * - 매개변수로 받아온 Entity 의 @Id 필드값이 기존에 PK 로 테이블에 존재한다면 UPDATE 문이,
	 * 														존재하지 않는다면 INSERT 문이 실행
	 * UPDATE MEMBER
	 * 	  SET 바꿀내용
	 *  WHERE USER_ID = ?
	 * 
	 * INSERT INTO MEMBER (...)
	 * 			   VALUES (...)
	 * 
	 * 4. PK 로 삭제 : deleteByID(PK값)
	 * 내부적으로
	 * DELETE
	 *   FROM MEMBER
	 *  WHERE USER_ID = ?
	 *  
	 * - 리턴 타입이 없음 (void)
	 * 
	 * --- 여기까지가 기본 SELECT, INSERT, UPDATE, DELETE 에 대한 메소드 --
	 * 
	 * 부가적으로
	 *  
	 * 5. 전제 개수 조회 : count()
	 * 내부적으로
	 * SELECT COUNT(*)
	 *   FROM MEMBER
	 * - 리턴타입 : long (정수)
	 * 
	 * 6. PK 존재 여부 확인 : existsById(PK값)
	 * - 리턴타입 : boolean
	 */
	
	//----------------------------------------
	
	// 좀 더 복잡한 쿼리문을 실행시키고 싶다면?
	
	/*
	 * * 쿼리 메소드 (Query Method)
	 * 
	 * -  JPA 에서는 메소드 이름만 규칙에 맞게 작성하명 SQL 문을 자동으로 만들어 준다.
	 * 	  (SQL 문을 직접 쓸 필요가 없음)
	 * - 인터페이스이기 떄문에 "추상메소드" 로만 정의하면 된다.
	 * - 규칙 : findBy + 필드명 + 조건키워드
	 */
	
	// 1) "PK가 아닌" 특정 컬럼값으로 조회 : findBy필드명(값) - List<Entity타입>
	// > SELECT * FROM MEMBER WHERE 필드에대한컬럼명 = 값
	List<Member> findByUserName(String userName);
	
	// 2) LIKE 검색 : findBy필드명Containing(값) - List<Entity타입>
	// > SELECT * FROM MEMBER WHERE 필드에대한컬럼명 LIKE '%값%'
	List<Member> findByUserNameContaining(String keyword);
	
	// 3) 비교 조건 : findBy필드명GreateThan(값) - List<Entity타입>
	// > SELECT * FROM MEMBER WHERE 필드에대한컬럼명 > 값
	List<Member> findByAgeGreaterThan(int age);
	
	// 4) 정렬 : findAllBy필드명Asc / Desc() - List<Entity타입>
	// > SELECT * FROM MEMBER ORDER BY 필드에대한컬럼명 ASC / DESC
	List<Member> findAllByOrderByEnrollDateDesc();
	
	// 5) 특정 조건에 맞는 개수 조회 : countBy필드명(값) - long (정수)
	// > SELECT COUNT (*) FROM MEMBER WHERE 필드에대한컬럼명 = 값
	long countByGender(String gender);
	
}