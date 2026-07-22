package com.kh.jpa.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jpa.board.model.service.BoardService;

@RestController
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
}