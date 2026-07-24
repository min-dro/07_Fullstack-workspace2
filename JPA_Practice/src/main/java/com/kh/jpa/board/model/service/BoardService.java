package com.kh.jpa.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jpa.board.model.dao.BoardDao;
import com.kh.jpa.board.model.dao.ReplyDao;
import com.kh.jpa.board.model.vo.Board;
import com.kh.jpa.board.model.vo.Reply;

@Service
public class BoardService {

	@Autowired
	private BoardDao boardDao;

	@Autowired
	private ReplyDao replyDao;
	
	// 1) 게시글 목록 조회
	public List<Board> selectBoardList() {
		
		return boardDao.findAll();
	}

	// 2) 게시글 작성 (추가)
	@Transactional
	public Board insertBoard(Board b) {
		
		return boardDao.save(b);
	}

	// 3) 게시글 상세 조회
	public Board selectBoard(int boardNo) {
		
		return boardDao.findById(boardNo).orElse(null);
	}

	// 4) 게시글 수정
	@Transactional
	public Board updateBoard(Board b) {
		
		return boardDao.save(b);
	}

	// 5) 게시글 삭제
	@Transactional
	public void deleteBoard(int boardNo) {
		
		boardDao.deleteById(boardNo);
	}

	// 6) PK 존재 여부 확인
	public boolean existsById(int boardNo) {
		
		return boardDao.existsById(boardNo);
	}

	// 7) 삭제되지 않은 게시글에 대해서 최신순 정렬을 적용한 전체조회 (1번 메소드 보완)
	public List<Board> selectBoardListLastest() {
		
		return boardDao.findByStatusOrderByBoardNoDesc("Y");
	}

	// 8) 게시글 제목 키워드 검색
	public List<Board> searchBoardList(String keyword) {
		
		return boardDao.findByBoardTitleContainingAndStatusOrderByBoardNoDesc(keyword, "Y");
	}

	// 9) 특정 유저가 작성한 게시글만 보기
	public List<Board> selectBoardByUser(String userId) {
		
		return boardDao.findByMember_UserIdAndStatusOrderByBoardNoDesc(userId, "Y");
	}

	// 10) 삭제되지 않은 게시글의 갯수
	public long selectListCount() {
		
		// return boardDao.count();
		// > 그냥 다 갯수를 세는 것!!
		
		return boardDao.countByStatus("Y");
		// > STATUS 가 'Y' 인 갯수만 세고 싶음!!
	}

	// 11) 댓글 추가
	@Transactional
	public Reply insertReply(Reply r) {

		return replyDao.save(r);
	}

	// 12) 댓글 목록 조회
	public List<Reply> selectReplyList(int boardNo) {
		
		return replyDao.findByBoard_BoardNoAndStatusOrderByReplyNoDesc(boardNo, "Y");
	}

	// 13) 댓글 삭제
	@Transactional
	public int deleteReply(int replyNo) {
		
		return replyDao.deleteReply(replyNo);
	}

	// 14) 특정 게시글의 활성 댓글 갯수 조회
	public long selectReplyCount(int boardNo) {
		
		return replyDao.selectReplyCount(boardNo);
	}

	// 15) 특정 회원이 작성한 댓글들 + 게시글의 제목도 함께 조회
	public List<Reply> selectReplyByUser(String userId) {
		
		return replyDao.selectReplyByUser(userId);
	}
	
	
	
}