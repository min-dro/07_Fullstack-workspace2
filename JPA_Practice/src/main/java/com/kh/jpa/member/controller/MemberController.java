package com.kh.jpa.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jpa.member.model.service.MemberService;
import com.kh.jpa.member.model.vo.Member;

/*
 * - 브라우저 주소창으로 대놓고 URL 주소를 입력해서 Controller 메소드가 호출되게끔 유도할것
 * - 모든 메소드를 @GetMapping 으로 작성
 * - 어쩔 수 없이 url 주소 상에 동사를 작성할 예정
 */

@RestController
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	// CRUD 기능 별로 Controller 메소드를 작성
	
	// 1) 회원 목록조회
	// 요청 URL : /jpa/member/list
	@GetMapping("list")
	public void selectMemberList() {
		
		List<Member> list = memberService.selectMemberList();
		
		for(Member m : list) {
			
			System.out.println(m);
		}
	}
	
	// 2) 회원 추가
	// 요청 URL : /jpa/member/insert
	@GetMapping("insert")
	public void insertMember() {
		
		// insert 할 회원의 정보를 요청 시 전달값으로 받았다는 가정 하에
		Member m = new Member();
		m.setUserId("user04");
		m.setUserPwd("pass044");
		m.setUserName("강개똥");
		m.setGender("F");
		m.setAge(42);
		// > DEFAULT 설정이 걸려있는 필드 (컬럼값) 은 뺐음
		
		Member insertMem = memberService.insertMember(m);

		System.out.println(insertMem);
	}
	
	// 3) 회원 상세조회
	// 요청 URL : /jpa/member/detail/xxx
	@GetMapping("detail/{userId}")
	public void selectMember(@PathVariable String userId) {
		
		Member m = memberService.selectMember(userId);
		
		System.out.println(m);
	}
	
	// 4) 회원 정보 수정
	// 요청 URL : /jpa/member/update
	@GetMapping("update")
	public void updateMember() {
		
		// 요청 시 전달값들을 받았다라는 가정 하에
		// 위에서 만든 상세조회로 기존 회원의 정보를 받아오고,
		// 바꿀내용만 살짝 수정해보기
		Member m = memberService.selectMember("user01");
		
		// 이름만 수정
		m.setUserName("홍길동");
		m.setUserPwd("Password01!");
		
		Member updateMem = memberService.updateMember(m);
		
		System.out.println(updateMem);
	}
	
	// 5) 회원 삭제
	// 요청 URL : /jpa/member/delete
	@GetMapping("delete")
	public void deleteMember() {
		
		memberService.deleteMember("user01");
		// > deleteById 은 리턴값이 없기 때문에 삭제 여부를 한번에 알기 어려움
		
		// 따라서 삭제 후, 진짜 삭제가 되었는지 상세조회 등으로 한번 더 확인 후 넘어가야함!!
		// 방법1)
		Member deleteMem = memberService.selectMember("user01");
		// > 만약 deleteMem 이 null 이라면 삭제가 잘 된거!!
		
		// 방법2)
		boolean isDeleted = memberService.existById("user01");
		// > 만약 isDeleted 가 false 라면 삭제가 잘 된거!!
		
		System.out.println(deleteMem);
		System.out.println(isDeleted);
	}
	
	// --------------------------------------------------------------------
	
	// 쿼리 메소드 테스트
	// 6) 이름으로 조회
	// 요청 URL : /jpa/member/name/xxx
	@GetMapping("name/{userName}")
	public void selectByUserName(@PathVariable String userName) {
		
		List<Member> list = memberService.selectByUserName(userName);
		
		for(Member m : list) {
			
			System.out.println(m);
		}
	}
	
	// 7) 이름 키워드 검색 (LIKE)
	// 요청 URL : /jpa/member/search
	@GetMapping("search")
	public void searchMemberList() {
		
		// 검색어가 넘어왔다 라는 가정 하에
		List<Member> list = memberService.searchMemberList("김");
		
		for(Member m : list) {
			
			System.out.println(m);
		}
	}
	
	// 8) 나이 조건 조회
	// 요청 URL : /jpa/member/age
	@GetMapping("age")
	public void selectByAge() {
		
		// 나이값이 넘어왔다라는 가정 하에
		List<Member> list = memberService.selectByAge(19);
		
		for(Member m : list) {
			
			System.out.println(m);
		}
	}
	
	// 9) 전체 조회 (최신 가입 순)
	// 요청 URL : /jpa/member/lastest
	@GetMapping("lastest")
	public void selectMemberListLatest() {
		
		List<Member> list = memberService.selectMemberListLastest();
		
		for(Member m : list) {
			
			System.out.println(m);
		}
	}

	// 10) 성별 별 회원 수
	// 요청 URL : /jpa/member/count/gender
	@GetMapping("count/gender")
	public void countByGender() {
		
		long m = memberService.countByGender("M");
		
		long f = memberService.countByGender("F");
		
		System.out.printf("남자 : %d명, 여자 : %d명 \n", m, f);
	}
	
}