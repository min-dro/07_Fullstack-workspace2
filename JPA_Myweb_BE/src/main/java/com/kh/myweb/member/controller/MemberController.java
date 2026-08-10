package com.kh.myweb.member.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myweb.member.model.service.MemberService;
import com.kh.myweb.member.model.vo.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Member API", description="회원 조회/수정/탈퇴 관련 API")
// > 클래스 상단에 작성하여 이 Controller 클래스의 API 메소드들을 그룹으로 묶어서 제목을 달 수 있는 Swagger 어노테이션
//   name 속성에는 제목을 작성하고, description 속성에는 설명을 작성한다!!
// > 생략 시 클래스명이 모두 소문자로 잡히고, - 로 단어 구분이 되서 제목이 잡히게 됨!!

@CrossOrigin
@RestController
public class MemberController {

	@Autowired
	private MemberService memberService /* = new MemberServiceImpl() */;
	// > 내부적으로 실제 객체 생성은 구현체 클래스 기준으로 일어난다!!
	//   (인터페이스로는 애초에 객체 생성이 불가하다)
	// > 자식타입인 구현체 클래스 타입으로 객체가 생성된 뒤 "다형성" 에 의해 부모타입인
	//   인터페이스 타입의 변수에 담기는 꼴임!! (의존성 주입, DI)
	
	// 회원 목록 조회용 컨트롤러
	@Operation(summary="회원 목록 조회", description="전체 회원 목록을 조회합니다.")
	// > API 메소드 상단에 작성하여 summary 로는 제목, description 으로는 설명을 작성할 수 있는 Swagger 어노테이션
	//   @Operation 은 메소드 전반적인 관련된 정보들을 기술
	@ApiResponse(responseCode="200", description="조회 성공")
	// > API 메소드 상단에 작성하여 responseCode 로는 응답 코드 번호, description 으로는 설명을 작성할 수 있는 Swagger 어노테이션
	//	 @ApiResponse 는 응답 정보들을 기술
	// > 생략 시 기본값이 200 에 OK 임!!
	@GetMapping("/members")
	public ResponseEntity<ArrayList<Member>> selectMemberList() {
		
		// System.out.println("잘 호출되나?");
		
		// 서비스 호출
		ArrayList<Member> list = (ArrayList)memberService.selectMemberList();
		
		/*
		for(Member m : list) {
			
			System.out.println(m);
		}
		*/
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(list);
	}

	// 회원 상세 조회용 컨트롤러
	@Operation(summary="회원 상세 조회", description="userId 로 회원 한명의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="조회 성공"),
		@ApiResponse(responseCode="200 (null)", description="해당 아이디의 회원이 없을 경우 body 가 null 로 응답됨")
	})
	// > @ApiResponses 으로 다양한 상황의 응답 정보들을 묶어서 표현도 가능함!!
	@GetMapping("/members/{userId}")
	public ResponseEntity<Member> selectMember(@Parameter(description="조회할 회원의 아이디", example="user01", required=true) 
											   @PathVariable String userId) {
											   // > 요청 시 전달값에는 @Parameter 어노테이션으로 설명과 예시값 등을 적는다.
											   //   특히 example 로 적은 값이 실제 Swagger 문서에서 입력 테스트값으로 잡힘!!
		
		// System.out.println(userId);
		
		// 서비스 호출
		Member m = memberService.selectMember(userId);
		
		// System.out.println(m);
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(m);
	}

	// 회원 정보 수정용 컨트롤러
	@Operation(summary="회원 정보 수정", description="회원 정보를 수정합니다. 수정일 (modifyDate) 은 서버에서 자동 셋팅됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="요청 처리 완료 (body 로 성공/실패 구분)",
				content=@Content(mediaType="text/plain",
								 examples= {
										 @ExampleObject(name="수정 성공", value="success"),
										 @ExampleObject(name="수정 실패", value="fail")
								 }))
	})
	@PutMapping("/members/{userId}")
	public ResponseEntity<String> updateMember(@Parameter(description="수정할 회원의 아이디", example="user01", required=true)
											   @PathVariable String userId,
											   @RequestBody Member m) {
											   // > 요청 시 전달값 중 커맨드 객체 방식으로 JSON 형태를 그대로 넘겨 받을 때
											   //   @RequestBody 어노테이션을 써 주면
											   //   Swagger 가 자동으로 문서에 반영해준다. (따로 뭘 적을 필요가 없음)
											   //   (이 때, READ_ONLY 들은 빠져서 반영됨)
		// System.out.println(userId);
		// System.out.println(m);
		
		 // 회원정보수정일 필드값을 현재시간으로 셋팅하기
		m.setModifyDate(LocalDateTime.now());
		// > LocalDateTime.now() : MySQL 의 CURRENT_TIMESTAMP 역할임
		
		if(m.getGender().equals("")) {
			
			m.setGender(null);
		}
		// > MySQL 에서는 "" 이 자동으로 NULL 로 들어가지 않음!! (오라클에서는 됐었음)
		// > 이 코드를 살려두더라도 오라클레서는 상관 없이 잘 돌아갈 것
		
		// 서비스 호출
		Member updateMemb = memberService.updateMember(m);
		// > JpaRepository 에서 제공되는 save 메소드는 INSERT / UPDATE 성공 시 추가 또는 수정된 행의 정보를
		//   VO로 리턴해주고, 실패 시 null 을 리턴해준다!!
		
		String message = (updateMemb != null) ? "success" : "fail";
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
	}

	// 회원 탈퇴용 컨트롤러
	@Operation(summary="회원 탈퇴", description="회원을 탈퇴하여 정보가 조회되지 않게합니다. 상태값을 N으로 변경합니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="요청 처리 완료 (body 로 성공/실패 구분)",
					 content=@Content(mediaType="text/plain",
							 		  examples= {
							 				  @ExampleObject(name="탈퇴 성공", value="success"),
							 				  @ExampleObject(name="탈퇴 실패", value="fail")
							 		  }))
	})
	@DeleteMapping("/members/{userId}")
	public ResponseEntity<String> deleteMember(@Parameter(description="탈퇴할 회원의 아이디", example="user01", required=true)
											   @PathVariable String userId) {
		
		// 서비스 호출
		int result = memberService.deleteMember(userId);
		
		String message = (result > 0) ? "success" : "fail";
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
		// > "success" 일 경우는 OK 가 아닌 NO_CONTENT (204) 로 응답해도 된다!!
	}

}