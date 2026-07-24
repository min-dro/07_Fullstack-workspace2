package com.kh.jpa.board.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.jpa.board.model.vo.Reply;

// 기존에는 BoardDao 안에 Reply 관련된 CRUD 구문도 함꼐 작성해도 됐었음!!
// 마찬가지로 Board Mapper XML 안에 Reply 관련된 CRUD 쿼리문도 작성해도 됐었음!!
// > 이제는 철저히 객체 위주로 CRUD 될 수 있도록 표현법 상 JpaRepository 상속 시
//	 제네릭 설정으로 각각 Entity타입과 PK타입을 명시해줘야하기 때문에, DAO 코드가 분리될 수 밖에 없음!!
// > 그렇다고 해서 패키지까지 쪼개서 저장할 필요는 없음!! (오히려 Reply 는 Board 의 자식으로써 종속되었으므로 같이 저장해도 됨)
public interface ReplyDao extends JpaRepository<Reply, Integer> {

	// JpaRepository 인터페이스를 상속받는 순간 기본 CRUD 메소드들은 다 먹고 들어가는 것!! (내꺼처럼 쓸 수 있음)
	// > findAll(), findById(PK값), save(Entity), deleteById(PK값), count(), existsById(PK값)
	
	// 그 외의 쿼리문을 실행하고 싶다면?
	// > 쿼리 메소드들을 규칙에 따라 이름을 지어주면 됨!! (메소드명이 길어질 수는 있지만, 쿼리문을 작성할 필요가 없다)
	// 예) findByReplyContentContainingAndStatusOrderByReplyNoDesc
	
	// 1) 댓글 목록 조회용 - 특정 게시글에 딸린것 기준으로 (REF_BNO 로 검사)
	// > SELECT * FROM REPLY WHERE REF_BNO = ? AND STATUS = ? ORDER BY REPLY_NO DESC
	List<Reply> findByBoard_BoardNoAndStatusOrderByReplyNoDesc(int boardNo, String status);
	// > Board_BoardNo : 외래키를 적용한 부모엔티티 Board 의 boardNo 을 참조하여 조건비교를 하겠다.
	
	// -----------------------------------------------------------
	
	/*
	 * * JPQL (Data Persistence Query Language)
	 * 
	 * - 기존의 Query Method (findBy...) 로 표현하기 더 어려운 쿼리를 쓰고자 할 때 사용
	 * - Query Method 로 까지 해결이 안되면, 결국 JPA 를 쓰더라도 쿼리문을 직접 작성해야한다.
	 *   (이 경우, 진짜 SQL 문이 아닌 자바 언어가 알아먹을 수 있는 형태의 쿼리문으로 작성해야함)
	 * - 즉, 자바 언어가 알아먹을 수 있는 형태의 쿼리문
	 * - SQL 문법 + 자바 문법 으로 혼합된 느낌
	 * 
	 * * SQL 문과의 차이점
	 * 
	 * 1. 테이블명 대신에 클래스명
	 * SQL : ... FROM REPLY ...
	 * JPQL : ... FROM Reply r ... (FROM Entity클래스명 객체명)
	 * 
	 * 2. 컬럼명 대신에 필드명
	 * SQL : WHERE REPLY_NO = 1
	 * JPQL : WHERE r.replyNo = 1 (객체명.필드명 = 1) // WHERE 절의 = 은 동등비교 의미
	 * 
	 * 3. * 대신에 별칭
	 * SQL : SELECT *
	 * JPQL : SELECT r (SELECT 객체명)
	 * 
	 * - 나머지 WHERE, SET, AND, COUNT, ORDER BY 같은 건 SQL 이랑 완전 동일!!
	 * 
	 * * JPQL 과 함께 쓰이는 어노테이션들
	 * 
	 * 1. @Query("JPQL 구문")
	 * - 이 안에 JPQL 을 쓴다!!
	 * 
	 * 2. @Modifying
	 * - UPDATE, DELETE (dml문) 에 반드시 붙여야함!
	 *   (안 붙이면 에러남)
	 * - DML 문임을 알려주는 어노테이션
	 * 
	 * 3. @Param("변수명")
	 * - JPQL 도 필요하면 쿼리문 사이에 구멍을 뚫고, 내가 원하는 값을 채워넣을 수 있다!!
	 * - SQL 문의 ? (위치 홀더) 와 같은 역할
	 * 
	 * SQL : WHERE REPLY_NO = ?
	 * JPQL : WHERE r.replyNo = :변수명
	 * 
	 * - @Param("변수명") : 마이바티스의 parameterType 으로 전달받는 전달값
	 * - :변수명 : 마이바티스로 따지면 #{변수명}
	 * 			 PreparedStatement 형식으로 구멍을 메꿔준다!!
	 * 			 (SQL Injection 공격을 애초에 걱정 안해도 된다)
	 * 
	 * * JPQL 이 쓰이는 대표적인 경우
	 * 
	 * - 소프트 삭제 : 삭제를 DELETE 구문으로 하는 것이 아니라, STATUS 처럼 상태 컬럼을 만들어 두고
	 * 				 UPDATE 구문을 통해 삭제 상태로 값만 변경해서 숨김처리 해주는 형태
	 * 				 (우리가 맨날 쓰던 방식)
	 * 
	 * - JOIN 구문 또는 서브쿼리 등의 복잡한 조회 구문
	 * : 특히 RDBMS (관계형 데이터베이스 모델) 를 쓰면
	 *   최대한 데이터의 중복을 막기 위해 (불일치 현상 막으려고) 테이블을 쪼개서 데이터들을 관리함
	 *   그러다 보니 JOIN 은 필수!!
	 *   
	 * * JPQL 사용 시 주의할 점 : 메소드명은 내 맘대로 짓는다!!
	 * 						  JPQL 끝에는 세미콜론을 적지 않는다.
	 */
	
	// 1) 댓글 삭제 - 소프트삭제로
	// > UPDATE REPLY SET STATUS = 'N' WHERE REPLY_NO = ?
	@Modifying
	@Query("UPDATE Reply r SET r.status = 'N' WHERE r.replyNo = :replyNo AND r.status = 'Y'")
	int deleteReply(@Param("replyNo") int replyNo);
	// > @Query("JPQL구문")
	// > @Modifying : UPDATE 문이므로 붙여줘야함, 그 순간 반환형은 int (처리된 행의 갯수)
	//				  성공하면 1, 실패하면 0
	// > @Param("변수명") & :변수명
	//	 : @Param 으로 값에 이름을 붙이고, :변수명 으로 값을 채워주는 것
	
	// 2) 특정 게시글의 활성 댓글 수 조회
	// > SELECT COUNT(*) FROM REPLY WHERE REF_BNO = ? AND STATUS = 'Y'
	 
	// 또는 조인을 이용해서
	/*
	 * SELECT COUNT(*)
	 *   FROM REPLY R
	 *   JOIN BOARD B ON R.REF_BNO = B.BOARD_NO
	 *  WHERE B.BOARD_NO = ?
	 *    AND R.STATUS = 'Y'
	 */
	
	// SELECT 문이기 때문에 @Modifying 어노테이션은 작성하지 않음!!
	@Query("SELECT COUNT(r) FROM Reply r WHERE r.board.boardNo = :boardNo AND r.status = 'Y'")
	long selectReplyCount(@Param("boardNo") int boardNo);
	// > 반환형은 int 가 되도 되고, long 이 되도 됨!!
	// > r.board : Reply Entity 의 부모 Entity 를 나타내는 필드
	// > r.board.boardNo : 참조하려고 하는 부모 Entity 의 @Id 필드
	// > 이렇게 타고 들어가면 필요할 경우 알아서 내부적으로 조인이 됨!!
	
	// 3) 특정 회원이 작성한 댓글들 + 게시글의 제목도 함께 조회
	/*
	 * - SQL
	 * SELECT R.REPLY_CONTENT, B.BOARD_TITLE
	 *   FROM REPLY R
	 *   JOIN BOARD B ON (R.REF_BNO = B.BOARD_NO)
	 *  WHERE R.REPLY_WRITER = ?
	 *  
	 * - JPQL
	 * SELECT r.replyContent, r.board.boardTitle
	 *   FROM Reply r
	 *  WHERE r.member.userId = :변수명
	 *  
	 * => @ManyToOne 으로 부모 Entity 가 필드로 연결되어있으면
	 * 	  JPQL 상에서 JOIN 구문을 직접 쓰지 않더라도 r.board.boardTitle 처럼 . 으로 접근하면
	 *	  JPA 가 알아서 JOIN 쿼리를 만들어 준다!!
	 * => 즉, SQL 에서는 JOIN _ ON 으로 직접 작성해야했다면
	 *    JPQL 에서는 객체명.부모필드명.PK필드명으로 쓰면 끝
	 */
	
	@Query("""
			SELECT r 
			  FROM Reply r
			 WHERE r.member.userId = :userId
			   AND r.status = 'Y'
			 ORDER BY r.replyNo DESC
			""")
	// > JPQL 구문이 너무 길어질 것 같으면 """ (텍스트 블록) 개행을 추가해서 작성해도 된다!!
	List<Reply> selectReplyByUser(@Param("userId") String userId);
	// > JPQL 에서 SELECT 문 조회 시, "해당 객체의 모든 필드를 조회하겠다 (SELECT r)" 가 아닌
	//   SELECT 절에 일부 필드명만 대서 조회 시, Reply 객체로 못만들어줌
	//   (SELECT r.replyContent, r.board.boardTitle)
	// > 이 경우에는 Object[] 로 조회됨 
	
	// 해결방법1) 그냥 SELECT r 쓰기
	// 해결방법2) replyContent, boardTitle 두개의 필드를 가진 VO 클래스를 별도로 또 만들어서 받기

}