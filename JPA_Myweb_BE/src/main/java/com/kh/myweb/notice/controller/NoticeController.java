package com.kh.myweb.notice.controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myweb.auth.controller.AuthController;
import com.kh.myweb.common.template.XssDefencePolicy;
import com.kh.myweb.member.model.service.MemberService;
import com.kh.myweb.member.model.vo.Member;
import com.kh.myweb.notice.model.service.NoticeService;
import com.kh.myweb.notice.model.vo.Notice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="Notice API", description="공지사항 조회/작성/수정/삭제 관련 API")
@CrossOrigin
@RestController
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private MemberService memberService;
	
	// 공지사항 목록 조회용 컨트롤러
	@Operation(summary="공지사항 목록 조회", description="공지사항의 목록을 조회합니다.")
	@ApiResponse(responseCode="200", description="조회 성공")
	@GetMapping("/notices")
	public ResponseEntity<ArrayList<Notice>> selectNoticeList() {
		
		// 서비스 호출
		ArrayList<Notice> list = (ArrayList)noticeService.selectNoticeList();
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(list);
	}
	
	// 공지사항 작성용 컨트롤러
	@Operation(summary="공지사항 작성", description="공지사항을 작성합니다. JWT 토큰에서 작성자 정보를 추출하므로 로그인이 필요합니다.")
	@ApiResponse(responseCode="200", description="body 로 success/fail 응답")
	@SecurityRequirement(name="JWT")
	// > 이 API 메소드는 JWT 토큰 (로그인) 이 필요하다라는 것을 문서에 표시애주는 어노테이션 (자물쇠 모양이 뜸)
	@PostMapping("/notices")
	public ResponseEntity<String> insertNotice(@RequestBody Notice n,
											   HttpServletRequest request) {
											   // > HttpServletRequest 객체는 Swagger 가 알아서 문서에서 제외해준다!!
											   //   (표시 안됨)
		
		// System.out.println(n);
		// > 작성자의 아이디가 안들어가있음!!
		
		// request 객체를 매개변수로 추가했고, 여기서부터 현재 로그인한 회원의 정보를 알아내기!!
		// URL 요청의 Header 에 Authorization 으로 "Bearer xxxxx.xxxxxxx.xxxx" 를 넣어뒀음
		// > 기존 인터셉터의 검증용 코드와 동일
		String authHeader = request.getHeader("Authorization");
		
		// > 인터셉터에서 이미 검증을 거쳐왔기 떄문에 중첩 if문 사용 X
		//   그냥 바로 뽑아서 쓰면 된다!!
		String jwtTokenString = authHeader.substring(7);
		// > "xxxxx.xxxxxxx.xxxx"
		
		// 위의 jwtTokenString 을 파싱하기
		Key key = Keys.hmacShaKeyFor(
						AuthController.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
							.setSigningKey(key)
							.build()
							.parseClaimsJws(jwtTokenString)
							.getBody();
		
		String userId = claims.getSubject();
		
		// System.out.println(userId);
		
		// 이 아이디값을 가지고 MemberService 를 호출해서 해당 회원의 정보를 Member 객체로 받아줘야함
		Member m = memberService.selectMember(userId);
		
		// System.out.println(m);
		
		// n 의 member 필드에 m 을 셋팅 (setter)
		n.setMember(m);
		
		// 현재시간이 담겨있는 컬럼에 not null 제약조건이 걸려있다면?
		n.setCreateDate(LocalDateTime.now());
		
		// XSS 공격 방지 처리 - 공통 코드 작업 참고
		n.setNoticeTitle(XssDefencePolicy.defence(n.getNoticeTitle()));
		n.setNoticeContent(XssDefencePolicy.defence(n.getNoticeContent()));
		Notice insertNo = noticeService.insertNotice(n);
		String message = (insertNo != null) ? "success" : "fail";
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
	}
	
	// 공지사항 상세조회용 컨트롤러
	@Operation(summary="공지사항 상세 조회", description="noticeNo로 한개의 공지사항의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="조회 성공"),
		@ApiResponse(responseCode="200 (null)", description="해당 글번호의 공지사항이 없을 경우 body 가 null 로 응답됨")
	})
	// > @ApiResponses 으로 다양한 상황의 응답 정보들을 묶어서 표현도 가능함!!
	@GetMapping("/notices/{noticeNo}")
	public ResponseEntity<Notice> selectNotice(@Parameter(description="조회할 공지사항의 글번호", example="1", required=true)
											   @PathVariable int noticeNo) {
		
		// System.out.println(noticeNo);
		
		Notice n = noticeService.selectNotice(noticeNo);
		
		// System.out.println(n);
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(n);
	}
	
	// 공지사항 수정용 컨트롤러
	@Operation(summary="공지사항 정보 수정", description="공지사항 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="요청 처리 완료 (body 로 성공/실패 구분)",
				content=@Content(mediaType="text/plain",
							     examples= {
							    		 @ExampleObject(name="수정 성공", value="success"),
							    		 @ExampleObject(name="수정 실패", value="fail")
							     }))
	})
	@PutMapping("/notices/{noticeNo}")
	public ResponseEntity<String> updateNotice(@Parameter(description="수정할 공지사항의 글번호", example="1", required=true)
											   @PathVariable int noticeNo,
											   @RequestBody Notice n) {
		
		// System.out.println(noticeNo);
		// System.out.println(n);
		n.setNoticeTitle(XssDefencePolicy.defence(n.getNoticeTitle()));
		n.setNoticeContent(XssDefencePolicy.defence(n.getNoticeContent()));
		Notice updateNo = noticeService.updateNotice(n);
		System.out.println(updateNo);
		
		String message = (updateNo != null) ? "success" : "fail";
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
	}
	
	// 공지사항 삭제용 컨트롤러
	@Operation(summary="공지사항 삭제", description="공지사항을 삭제하여 정보가 조회되지 않게합니다. 상태값을 N으로 변경합니다.")
	@ApiResponses({
		@ApiResponse(responseCode="200", description="요청 처리 완료 (body 로 성공/실패 구분)",
					 content=@Content(mediaType="text/plain",
							 		  examples= {
							 				  @ExampleObject(name="삭제 성공", value="success"),
							 				  @ExampleObject(name="삭제 실패", value="fail")
							 		  }))
	})
	@DeleteMapping("/notices/{noticeNo}")
	public ResponseEntity<String> deleteNotice(@Parameter(description="삭제할 공지사항의 글번호", example="1", required=true)
											   @PathVariable int noticeNo) {
		
		// 서비스 호출
		int result = noticeService.deleteNotice(noticeNo);
		
		String message = (result > 0) ? "success" : "fail";
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
	}
	
}