package com.kh.jpa.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jpa.board.model.dao.ReplyDao;
import com.kh.jpa.board.model.vo.Reply;

@Service
public class ReplyService {
	
	@Autowired
	private ReplyDao replyDao;

	// 1) 댓글 목록 조회
	public List<Reply> selectReplyListLastest() {
		
		return replyDao.findByStatusOrderByReplyNoDesc("Y");
	}

	// 2) 댓글 작성 (추가)
	@Transactional
	public Reply insertReply(Reply r) {
		
		return replyDao.save(r);
	}

	// 3) 댓글 상세 조회
	public Reply selectReply(int replyNo) {
		
		return replyDao.findById(replyNo).orElse(null);
	}

	// 4) 댓글 수정
	@Transactional
	public Reply updateReply(Reply r) {
		
		return replyDao.save(r);
	}

	// 5) 댓글 삭제
	@Transactional
	public void deleteReply(int replyNo) {
	
		replyDao.deleteById(replyNo);
	}

	// 6) PK 존재 여부
	public boolean existsReply(int replyNo) {
		
		return replyDao.existsById(replyNo);
	}
	
	

}