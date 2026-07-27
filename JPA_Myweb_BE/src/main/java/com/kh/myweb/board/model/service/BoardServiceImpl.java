package com.kh.myweb.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.myweb.board.model.dao.BoardDao;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession; 
	
	@Override
	public int selectListCount() {
		
		return boardDao.selectListCount(sqlSession);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi) {
		
		return boardDao.selectBoardList(sqlSession, pi);
	}

	@Override
	public int selectSearchCount(String keyword) {
		
		return boardDao.selectSearchCount(sqlSession, keyword);
	}

	@Override
	public ArrayList<Board> selectSearchList(PageInfo pi, String keyword) {
		
		return boardDao.selectSearchList(sqlSession, pi, keyword);
	}

	@Transactional
	@Override
	public int insertBoard(Board b) {
		
		return boardDao.insertBoard(sqlSession, b);
	}

	@Transactional
	@Override
	public int increaseCount(int boardNo) {
		
		return boardDao.increaseCount(sqlSession, boardNo);
	}

	@Override
	public Board selectBoard(int boardNo) {
		
		return boardDao.selectBoard(sqlSession, boardNo);
	}

	@Transactional
	@Override
	public int updateBoard(Board b) {
		
		return boardDao.updateBoard(sqlSession, b);
	}
	
	@Transactional
	@Override
	public int deleteBoard(int boardNo) {

		return boardDao.deleteBoard(sqlSession, boardNo);
	}

}