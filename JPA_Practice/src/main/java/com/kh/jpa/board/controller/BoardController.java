package com.kh.jpa.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jpa.board.model.service.BoardService;
import com.kh.jpa.board.model.vo.Board;
import com.kh.jpa.board.model.vo.Reply;
import com.kh.jpa.member.model.service.MemberService;
import com.kh.jpa.member.model.vo.Member;

@RestController
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private MemberService memberService;
	
	// 1) 게시글 목록 조회
	// 요청 URL : /jpa/board/list
	@GetMapping("list")
	public void selectBoardList() {
		
		// List<Board> list =  boardService.selectBoardList();
		// > 그냥 전체조회 (삭제여부 안따지고, 정렬도 안되어있음)
		
		List<Board> list = boardService.selectBoardListLastest();
		// > 삭제되지 않은 게시글에 대해서 최신순 정렬에 대한 전체조회
		
		for(Board b : list) {
			
			System.out.println(b);
		}
	}
	
	// 2) 게시글 등록
	// 요청 URL : /jpa/board/insert
	@GetMapping("insert")
	public void insertBoard() {
		
		// 요청 시 전달값으로 등록할 게시글 정보를 받아왔다 라는 가정 하에
		Board b = new Board();
		b.setBoardTitle("2번 게시글의 제목입니다");
		b.setBoardContent("2번 게시글의 내용입니다.");
		
		// 작성자의 Member 객체를 얻어내려면
		Member m = memberService.selectMember("user01");
		
		b.setMember(m); // Member 객체를 통으로 셋팅
		
		Board insertB = boardService.insertBoard(b);
		
		System.out.println(insertB);
	}
	
	// 3) 게시글 상세 조회
	// 요청 URL : /jpa/board/detail/x
	@GetMapping("detail/{boardNo}")
	public void selectBoard(@PathVariable int boardNo) {
		
		Board b = boardService.selectBoard(boardNo);
		
		System.out.println(b);
	}
	
	// 4) 게시글 수정
	// 요청 URL : /jpa/board/update
	@GetMapping("update")
	public void updateBoard() {
		
		// Update 느느 모든 필드값을 다 채워서 넘겨야함!!
		// > 그래서 기존 게시글 정보를 하나 불러와서 일부 필드만 수정해서 요청
		Board b = boardService.selectBoard(1);
		
		b.setBoardTitle("변경된 첫번째 제목입니다.");
		b.setBoardContent("변경된 내용이 어쩌고 저쩌고..");
		
		Board updateB = boardService.updateBoard(b);
		
		System.out.println(updateB);
	}
	
	// 5) 게시글 삭제
	// 요청 URL : /jpa/board/delete/x
	@GetMapping("delete/{boardNo}")
	public void deleteBoard(@PathVariable int boardNo) {
		
		boardService.deleteBoard(boardNo);
		
		// 삭제가 제대로 되었는지 검사
		boolean result = boardService.existsById(boardNo);
		
		if(!result) {
			
			System.out.println("게시글 삭제에 성공했습니다.");
			
		} else {
			
			System.out.println("게시글 삭제에 실패했습니다.");
		}
	}
	
	// ---------------------------------------------------------
	
	// 6) 게시글 제목 키워드 검색
	// 요청 URL : /jpa/board/search
	@GetMapping("search")
	public void searchBoardList() {
		
		List<Board> list = boardService.searchBoardList("번째");
		
		for(Board b : list) {
			
			System.out.println(b);
		}
	}
	
	// 7) 특정 유저가 작성한 게시글만 보기
	// 요청 URL : /jpa/board/xxxx
	@GetMapping("{userId}")
	public void selectBoardByUser(@PathVariable String userId) {
	
		List<Board> list = boardService.selectBoardByUser(userId);
	
		for(Board b : list) {
			
			System.out.println(b);
		}
	}
	
	// 8) 삭제되지 않은 게시글으 갯수
	// 요청 URL : /jpa/board/count/status
	@GetMapping("count/status")
	public void selectListCount() {
		
		long listCount = boardService.selectListCount();
		
		System.out.println(listCount);
	}

	// -----------------------------------------------------------
	
	// 9) 댓글 추가용 컨트롤러
	// 요청 URL : /jpa/board/rinsert
	@GetMapping("rinsert")
	public void insertReply() {
		
		// 요청 시 전달값을 받았다라는 가정 하에
		Reply r = new Reply();
		
		r.setReplyContent("3번 댓글 내용입니다.");
		
		// 기존 게시글 정보 하나를 불러오기
		Board b = boardService.selectBoard(1);
		
		r.setBoard(b);
		
		// 기존 회원 정보 하나를 불러오기
		Member m = memberService.selectMember("user01");
		
		r.setMember(m);
		
		Reply insertR = boardService.insertReply(r);
		
		System.out.println(insertR);
		
	}
	
	// 10) 댓글 목록 조회용 컨트롤러
	// 요청 URL : /jpa/board/rlist
	@GetMapping("rlist")
	public void selectReplyList() {
		
		// 특정 게시글에 딸린 댓글들의 목록 조회
		List<Reply> list = boardService.selectReplyList(1);
		
		for(Reply r : list) {
			
			System.out.println(r);
		}
	}
	// > Board 테이블에 List<Reply> 타입의 @OneToMany 필드가 없어도 충분히
	//   자식데이터들에 대한 목록 조회가 가능하다!! (오히려 이게 권장사항임)

	// 11) 댓글 삭제용 컨트롤러
	// 요청 URL : /jpa/board/rdelete
	@GetMapping("rdelete")
	public void deleteReply() {
		
		// 삭제할 댓글의 번호를 받아왔다라는 가정 하에
		int result = boardService.deleteReply(4);
		
		// result > 0 : 성공 처리
	}
	
	// 12) 특정 게시글의 활성 댓글 갯수 조회
	// 요청 URL : /jpa/board/rcount/x
	@GetMapping("rcount/{boardNo}")
	public void selectReplyCount(@PathVariable int boardNo) {
		
		long replyCount = boardService.selectReplyCount(boardNo);
		
		System.out.println("댓글 갯수 : " + replyCount);
	}
	
	// 13) 특정 회원이 작성한 댓글들 + 게시글의 제목도 함께 조회
	// 요청 URL : /jpa/board/reply/{userId}
	@GetMapping("{userId}/rlist")
	public void selectReplyByUser(@PathVariable String userId) {
		
		List<Reply> list = boardService.selectReplyByUser(userId);
		
		for(Reply r : list) {
			
			System.out.println(r);
		}
	}
	
}