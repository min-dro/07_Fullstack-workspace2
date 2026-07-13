package com.kh.myweb.board.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.model.vo.PageInfo;
import com.kh.myweb.common.template.Pagination;

@CrossOrigin
@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 일반게시글 목록 조회용 컨트롤러
	@GetMapping("/boards")
	public ResponseEntity<HashMap<String, Object>> selectBoardList(
				@RequestParam(value="cpage", defaultValue="1") int currentPage) {
		
		// System.out.println(currentPage);
		
		// 페이징 처리를 위한 7개의 변수 셋팅
		// listCount : 총 게시글 갯수 (실제 DB 에서 COUNT 로 세오기)
		// currentPage : 현재 요청한 페이지 (이미 요청 시 전달값으로 받음)
		// pageLimit : 한 페이지당 보여질 페이징 바의 페이지 수
		// boardLimit : 한 페이지당 보여질 게시글의 갯수
		
		// maxPage : 마지막 페이지수 (총 몇개의 페이지가 나오는지)
		// startPage : 하단에 보여질 페이징바의 시작수
		// endPage : 하단에 보여질 페이징바의 끝수
		
		int listCount = boardService.selectListCount();
		
		int pageLimit = 5;
		int boardLimit = 5;
		
		PageInfo pi =  Pagination.getPageInfo(listCount, currentPage,
											  pageLimit, boardLimit);
		
		// 서비스 호출
		ArrayList<Board> list = boardService.selectBoardList(pi);
		
		/*
		System.out.println(pi);
		
		for(Board b : list) {
			
			System.out.println(b);
		}
		*/
		
		// pi 와 list 를 둘 다 응답데이터로 넘길 것
		// > pi 와 list 를 하나로 묶어서 넘길 것 (HashMap 으로  -> JSONObject 형식)
		HashMap<String, Object> hm = new HashMap<>();
		
		hm.put("pi", pi);
		hm.put("list", list);
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(hm);
	}
	
}