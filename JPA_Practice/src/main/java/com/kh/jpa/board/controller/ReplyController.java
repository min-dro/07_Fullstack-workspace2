package com.kh.jpa.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jpa.board.model.service.BoardService;
import com.kh.jpa.board.model.service.ReplyService;
import com.kh.jpa.board.model.vo.Board;
import com.kh.jpa.board.model.vo.Reply;
import com.kh.jpa.member.model.service.MemberService;
import com.kh.jpa.member.model.vo.Member;

@RestController
@RequestMapping("reply")
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BoardService boardService;
	
	// 1) 댓글 목록 조회
	@GetMapping("list")
	public void selectReplyList() {
		
		List<Reply> list = replyService.selectReplyListLastest();
		
		for(Reply r : list) {
			
			System.out.println("댓글 번호 : " + r.getReplyNo());
		    System.out.println("댓글 내용 : " + r.getReplyContent());
		}
	}
	
	// 2) 댓글 등록
	@GetMapping("insert")
	public void insertReply() {
		
		Reply r = new Reply();
		r.setReplyContent("첫번째 댓글입니다.");
		
		Member m = memberService.selectMember("user04");
		Board b = boardService.selectBoard(1);
		
		r.setMember(m);
		r.setBoard(b);
		
		Reply insertR = replyService.insertReply(r);
		
		System.out.println("댓글 번호 : " + insertR.getReplyNo());
	    System.out.println("댓글 내용 : " + insertR.getReplyContent());
	}
	
	// 3) 댓글 상세 조회
	@GetMapping("detail/{replyNo}")
	public void selectReply(@PathVariable int replyNo) {
		
		Reply r = replyService.selectReply(replyNo);
		
		System.out.println("댓글 번호 : " + r.getReplyNo());
	    System.out.println("댓글 내용 : " + r.getReplyContent());
	}
	
	
	// 4) 댓글 수정
	@GetMapping("update")
	public void updateReply() {
		
		Reply r = replyService.selectReply(1);
		
		r.setReplyContent("변경된 댓글입니다.");
		
		Reply updateR = replyService.updateReply(r);
		
		System.out.println("댓글 번호 : " + updateR.getReplyNo());
	    System.out.println("댓글 내용 : " + updateR.getReplyContent());
	}
	
	// 5) 댓글 삭제
	@GetMapping("delete/{replyNo}") 
	public void deleteReply(@PathVariable int replyNo) {
		
		replyService.deleteReply(replyNo);
		
		boolean result = replyService.existsReply(replyNo);
		
		if(!result) {
			
			System.out.println("댓글 삭제에 성공했습니다.");
		
		} else {
			
			System.out.println("댓글 삭제에 실패했습니다.");
		}
		
	}
}