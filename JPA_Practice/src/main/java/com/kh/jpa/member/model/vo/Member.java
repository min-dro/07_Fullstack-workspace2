package com.kh.jpa.member.model.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * * JPA 에서의 VO (DTO)
 * 
 * - 기존 MyBatis 에서는 VO 클래스에 @Alias 어노테이션을 붙이고 (별칭을 부여하고)
 *   Mapper XML 파일에서 resultMap 으로 컬럼과 필드를 1:1로 매핑했었음!!
 * - JPA 에서는 VO 클래스 자체가 곧 테이블과 1:1로 매핑되는 "Entity" 가 된다!!
 * - @Entity, @Table, @Column 등의 어노테이션으로 매핑 정보를 작성하면 내부적으로 테이블과 컬럼들을 생성해준다.
 *   (Mapper XML, resultMap 이 필요 없다)
 * - 프로젝트 구동 시 Entity 정보에 의해 테이블이 생성된다
 */

@Entity
// > 이 클래스가 JPA 의 Entity (DB 테이블과 매핑되는 클래스) 임을 선언
//   @Entity 가 붙은 클래스는 JPA 가 관리하며
//	 application.properties. 파일의 ddl-auto 설정에 따라 테이블을 자동 생성 / 수정해줌

@Table(name="MEMBER")
// > 이 Entity 가 매칭될 DB 테이블명을 지정 (생략 가능)
//   생략 시, 클래스명과 동일한 이름의 테이블에 매핑됨 (클래스명이 Member 면 테이블명이 MEMBER)

@DynamicInsert
// > 이 어노테이션이 없다면 INSERT 시 모든 컬럼에 대해 다 insert 를 수행함
//	 어노테이션을 작성하면 null 로 셋팅된 컬럼에 대해서는 insert 를 수행하지 않음
@DynamicUpdate
// > 마찬가지로 UPDATE 시 null 이 아닌 컬럼에 대해서만 수정하겠다.

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Member {

	@Id	
	// > 이 필드가 이 테이블의 PRIMARY KEY (기본키) 임을 선언
	//	 @Entity 클래스에는 반드시 @Id 가 하나 있어야함!!
	@Column(name="USER_ID")
	// > 이 필드가 매핑될 DB 컬럼명을 지정하는 어노테이션 (생략 가능)
	//	 생략 시 필드명을 그대로 컬럼명으로 사용함 (필드명이 userId 면 컬럼명도 USER_ID)
	//   단, 카멜케이스가 스네이크케이스로 변환
	private String userId;
	// > 필드의 자료형이 바자의 String 이면, 컬럼명의 자료형으로 VARCHAR 로 변환된다!! (가변문자열)
	
	@Column(name="USER_PWD", nullable=false, length=15)
	// > nullable=false : NOT NULL 제약조건을 추가하겠다.
	//   letgth=15 : VARCHAR(15) 를 나타낸다.
	private String userPwd;
	
	@Column(name="USER_NAME", nullable=false, length=5)
	private String userName; // VARCHAR(5)
	
	@Column(name="GENDER", columnDefinition="CHAR(1)")
	// > 같은 String 이지만 CHAR 타입으로 설정하고자 한다면 columnDefinition 속성을 사용한다.
	private String gender; // CHAR(1)
	
	@Column(name="AGE")
	private int age;
	// > 필드의 자료형이 자바의 int 라면, 컬럼의 자료형으로 INT 로 변환된다!!
	
	@Column(name="ENROLL_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	// > columnDefinition 으로 자료형 지정 시, DEFAULT 설정도 같이 지정할 수 있다!!
	private LocalDateTime enrollDate; // TIMESTAMP
	// > 필드의 자료형이 자바의 LocalDateTime 이라면, 컬럼의 자료형으로 DATETIME 으로 변환된다!!
	
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status; // CHAR(1)
}