package com.kh.jpa.board.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.jpa.board.model.vo.Board;

public interface BoardDao extends JpaRepository<Board, Integer>{

	// 눈에 보이지는 않지만..
	// JpaRepository 라는 부모 인터페이스로부터 물려받은 (상속받은, extends) 메소드들이 있다!!
	
	// 전체 조회용 메소드
	// List<Board> findAll();
	
	// PK로 단일 조회
	// Optional<Board> findById(int boardNo);
	
	// 저장 or 수정
	// Member save(Board b);
	
	// PK로 삭제
	// void deleteById(int boardNo);
	
	// 전체 개수 조회
	// long count();
	
	// PK 존재 여부 확인
	// boolean existById(int boardNo)
	
	// -----------------------------------------------------
	
	// 좀 더 복잡한 형식의 쿼리문을 실행하고 싶다면?
	// > Query Method 이용 (각종 키워드 규칙에 맞게 조합해서 자동으로 SQL문이 실행되도록 유도)
	
	// 1) 게시글 목록 조회 (STATUS 가 'Y'인 애들만) + 최신순 정렬
	// > SELECT * FROM BOARD WHERE STATUS = 'Y' ORDER BY BOARD_NO DESC
	List<Board> findByStatusOrderByBoardNoDesc(String status);
	// > findByStatus : status 필드값을 기준으로 조회하겠다.
	//	 OrderByBoardNoDesc : boardNo 필드값을 기준으로 내림차순 정렬하겠다.
	
	// 2) 제목 키워드 (LIKE) 검색 (STATUS 가 'Y'인 애들만)
	// > SELECT * FROM BOARD WHERE BOARD_TITLE LIKE '%?%' AND STATUS = ?
	List<Board> findByBoardTitleContainingAndStatusOrderByBoardNoDesc(String keyword, String status);
	// > findByBoardTitleContaining : boardTitle 필드값을 기준으로 포함여부를 검사하겠다.
	//   And : AND 연산자를 이용해서 조건을 하나 더붙이겠다.
	//   Status : status 필드값을 기준으로 일치함을 검사하겠다.
	// > 여기서 검색 결과를 최신순으로 보고싶다면? 메소드명 뒤에 OrderByBoardNoDesc 를 더 붙여주면 됨!!
	
	// 3) 작성자로 조회 (작성자가 일치한 경우에만 조회)
	// - 조건식을 "작성자" 를 기준으로 고민해봐야함!! 기존에는 String boardWriter; 로 필드를 구성했기 때문에
	//   findByBoardWriter 이런식으로 메소드명을 잡아주면 되지만,
	//	 우리는 @ManyToOne 관계에 의해 작성자를 나타내는 필드를 Member member; 로 변경했었음!!
	//   이 상황에 맞는 메소드 네이밍 규칙을 적용해야함!!
	// > SELECT * FROM BOARD WHERE BOARD_WRITER = ? AND STATUS = ?
	List<Board> findByMember_UserIdAndStatusOrderByBoardNoDesc(String userId, String status);
	// > findBy : 조회하겠다. (SELECT 문을 실행하겠다)
	//	 Member_UserId : Member Entity (VO 클래스) 의 userId 필드
	//   member.userId (X) -> member.getUserId() (O) -> 부모Entity명_Id필드명 (즉, 참조할 컬럼, 필드명)
	// > 여기서 삭제되지 않은 게시글만 보고싶다면? 메소드명 뒤에 AndStatus 를 더 붙여주면 됨!!
	// > 여기서 이왕이면 최신순으로 결과를 보고싶다면? 메소드명 뒤에 OrderByBoardNoDesc 를 더 붙여주면 됨!!
	
	// 4) 삭제되지 않은 게시글의 갯수
	// > SELECT COUNT(*) BOARD WHERE STATUS = ?
	long countByStatus(String status);
	// > countBy : COUNT 함수를 통해 조회하겠다.
	
}