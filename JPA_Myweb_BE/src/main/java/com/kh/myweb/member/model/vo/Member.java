package com.kh.myweb.member.model.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Alias("member")
// > 지금부터 이 클래스의 별칭은 "member" 임을 알려주는 MyBatis 어노테이션

@Schema(description="회원 정보 엔티티")
// > 클래스 선언부 상단에 적어서 그 VO 클래스의 설명을 기술할 수 있는 Swagger 에서 제공해주는 어노테이션
//   desctiption 속성에 해당 VO 에 대한 설명을 적는다.

@Entity
@Table(name="MEMBER")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Member {

	// 필드부
	@Schema(description="회원의 아이디", example="user01", requiredMode=Schema.RequiredMode.REQUIRED)
	// > 필드마다 적어서 그 필드에 대한 설명을 기술할 수 있는 어노테아션
	//	 description 속성에 그 필드에 대한 설명을 작성한다.
	//   example 속성에는 그 필드에 담을 수 있는 예시값을 쓸 수 있다. (API 응답 데이터 샘플로 보여짐)
	//   requiredMode 속성에는 필수값인지 (REQUIRED), 선택값인지 (NOT_REQUIRED) 표시할 수 있다.
	@Id
	@Column(name="USER_ID", length=30)
	private String userId;			//	USER_ID	VARCHAR2(30 BYTE)
	
	@Schema(description="회원의 비밀번호", example="pass01", requiredMode=Schema.RequiredMode.REQUIRED,
			accessMode=Schema.AccessMode.WRITE_ONLY)
	// > accessMode 속성에 WRITE_ONLY 라고 적으면 READ 가 불가능해진다!!
	//   예) 회원 정보를 응답해서 화면에 출력할 떄 비밀번호 보여지면 (READ) 안되기 때문!!
	//	 즉, 비밀번호처럼 "요청으로 받기만 하고, 응답 JSON 에서는 노출되면 안되는" 필드에 사용한다.
	// > Swagger 문서 상 요청화면에서는 보이고, 응답예시에서는 숨겨진다!!
	@Column(name="USER_PWD", nullable=false, length=100)
	private String userPwd;			//	USER_PWD	VARCHAR2(100 BYTE)
	
	@Schema(description="회원의 이름", example="김말똥", requiredMode=Schema.RequiredMode.REQUIRED)
	@Column(name="USER_NAME", nullable=false, length=5)
	private String userName;		//	USER_NAME	VARCHAR2(15 BYTE)
	
	@Schema(description="이메일", example="user01@naver.com")
	@Column(name="EMAIL", length=100)
	private String email;			//	EMAIL	VARCHAR2(100 BYTE)
	
	@Schema(description="성별 (M : 남자 / F : 여자)", example="M", allowableValues= {"M", "F"})
	// > allowableValues 속성에 허용되는 값 목록을 문서에 표기할 수 있다.
	@Column(name="GENDER", columnDefinition="CHAR(1)")
	private String gender;			//	GENDER	VARCHAR2(1 BYTE)
	
	@Schema(description="나이", example="20")
	@Column(name="AGE")
	private int age;				//	AGE	NUMBER
	
	@Schema(description="전화번호", example="010-1234-5678")
	@Column(name="PHONE", length=13)
	private String phone;			//	PHONE	VARCHAR2(13 BYTE)
	
	@Schema(description="주소", example="서울시 강남구")
	@Column(name="ADDRESS", length=35)
	private String address;			//	ADDRESS	VARCHAR2(100 BYTE)
	
	@Schema(description="회원가입일 (DB 자동 입력)", example="2026-08-04T10:00:00",
			accessMode=Schema.AccessMode.READ_ONLY)
	// > accessMode 속성에 READ_ONLY 라고 적으면 작성 (WRITE) 가 불가해진다!!
	//   Swagger 문서 상 응답에서는 보이고, 반대로 요청에서는 숨겨진다!! (굳이 요청 시 입력할 필요가 없는 값이라서)
	@Column(name="ENROLL_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime enrollDate;		//	ENROLL_DATE	DATE
	
	@Schema(description="회원 정보 수정일 (DB 자동 입력)", example="2026-08-04T10:00:00",
			accessMode=Schema.AccessMode.READ_ONLY)
	@Column(name="MODIFY_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime modifyDate;		//	MODIFY_DATE	DATE
	
	@Schema(description="회원 상태 (Y : 활성 / N : 탈퇴)", example="Y", allowableValues= {"Y", "N"},
			defaultValue="Y")
	// > defaultValue 속성으로 기본값 또한 문서에 표시할 수 있다.
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;			//	STATUS	VARCHAR2(1 BYTE)
	
	@Schema(description="회원의 권한 (USER : 일반회원 / ADMIN : 관리자)", example="USER",
			allowableValues={"USER", "ADMIN"}, defaultValue="USER")
	@Column(name="ROLE", columnDefinition="VARCHAR(6) DEFAULT 'USER'")
	private String role;
}