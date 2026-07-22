package com.kh.jpa.board.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.jpa.board.model.dao.BoardDao;

@Service
public class BoardService {

	@Autowired
	private BoardDao boardDao;
	
}