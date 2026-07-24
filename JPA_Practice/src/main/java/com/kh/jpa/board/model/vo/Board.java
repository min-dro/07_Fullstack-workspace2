package com.kh.jpa.board.model.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kh.jpa.member.model.vo.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="BOARD")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Board {

	@Id
	@Column(name="BOARD_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	// > PK 가 숫자일 경우 AUTO_INCREMENT 를 사용하기 위해 @GeneratedValue 어노테이션을 작성
	//	 strategy=GenerationType.IDENTITY : MySQL 의 AUTO_INCREMENT 전략을 사용하겠다.
	//	 (INSERT 시 PK 값을 직접 넣지 않더라도 자동으로 1, 2, 3, 4, ... 증가)
	private int boardNo;
	
	@Column(name="BOARD_TITLE", nullable=false, length=35)
	private String boardTitle;
	
	// * 기존의 방식 - 외래키로 지정 X
	// @Column(name="BOARD_WRITER", nullable=false)
	// private String boardWriter;
	// > 단순히 String borderWriter; 필드로 작성자의 아이디를 컬럼으로 생성하면
	//   실제 만들어진 테이블들의 ERD 를 살펴봤을 때 (DDL 구문을 뜯어봤을 때) 외래키 설정이 안되어있음!!
	// > 누군가가 잘못된 경로 등으로 게시글을 작성했을 때, 실제 회원 정보에 없는 아이디로 작성자 정보가 들어갈 수 있음
	//	 (데이터의 무결성이 깨짐, 데이터의 정합성이 맞지 않음)
	
	// * 외래키를 지정하려면? - @JoinColumn, 카디널리티를 나타내는 어노테이션 둘 다 작성
	@ManyToOne
	// > 지금 내가 코드 작업중인 이 곳은 Board Entity (N), 여기를 기준으로 시작해야함
	//   (게시글 N개는 1명의 회원에 의해 씌어질 수 있음)
	@JoinColumn(name="BOARD_WRITER")
	// > @JoinColumn 어노테이션은 외래키가 걸린 컬럼임을 의미한다.
	//   기존의 @Column 어노테이션과 동일하게 name 속성에 실제로 쓸 컬럼명을 정의한다.
	private Member member;
	// > 참조하고자 하는 부모 Entity를 통째로 필드로 정의한다!!
	// > 이 때, 이 컬럼의 자료형은 Member의 @Id 컬럼에 대한 자료형으로 잡히게 된다!!
	
	@Column(name="BOARD_CONTENT", nullable=false, length=200)
	private String boardContent;
	
	@Column(name="COUNT", columnDefinition="INT DEFAULT 0")
	private int count;
	
	@Column(name="CREATE_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	@Column(name="STATUS", length=1, columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;
	
	// ---------------------------------
	
	// Board Entity 에 
	// 게시글 1개을 조회할 때, 그 게시글에 딸린 댓글들도 같이 조회하겠다.
	@OneToMany(mappedBy="board" /* , fetch=FetchType.LAZY */) // 안써도 기본값임
	private List<Reply> replyList;
	// > 양방향 Mapping : 자식인 Reply 의 board 필드랑 연결한 것
	// > @Column, @JoinColumn 은 쓰지 않는다. (컬럼으로 안만들겠다)
	// > 이 필드는 읽기 전용임 (조회해온 댓글 목록들을 담고 수정 불가)
	
	/*
	 * * @OneToMany 로 List<Reply> 필드를 추가했을 경우
	 *   Board 객체 1개 조회 시 (단일행 조회 시) 그 게시글에 딸린 Reply 객체 여러개가 함께 조회된다.
	 * - 그 같이 조회된 Reply 객체들은 @OneToMany List<Reply> 필드에 차곡차곡 같이 담겨오는 구조!!
	 * 
	 * * 이 상황에서 Board 상세조회를 했더니
	 * - 뭔가 조회되는것 같기는 함 (콘솔에 select 문이 출력됐으니까)
	 * - 그런데 오류 페이지가 뜨면서 StackOverflowError 가 발생하고 있음!! (계속 반복이 돌다가 멈춤)
	 * 
	 * 왜?
	 * Board 1개 조회 시
	 * Board 조회 1번 + 그 Board 에 딸린 Reply 를 1개씩 3번 조회 = 총 4번 쿼리 실행
	 * 
	 * - 기본적으로 @OneToMany 조회 시, 자식 목록을 조회할 경우 하나씩 조회해서 List 에 차곡차곡 담아주는 구조
	 * - 의도치 않게 반복이 돌 수 밖에 없고, 그에 따하 쿼리문 실행 횟수가 계속 늘어날 수 밖에 없음!!
	 * 
	 * => 이 상황을 "N+1문제" 라고 부른다!!
	 * 	  (JPA 뿐만 아니라 MyBatis 에서도 충분히 발생할 수 있는 문제임)
	 * 
	 * - 그래서 지금은 우리가 Board 1개만 조회했지만, Board 목록 조회를 한다면?
	 *   Board 마다 딸린 Reply 들이 일일이 조회되면서 SELECT 문의 실행 횟수가 기하급수적으로 늘어나게될것!!
	 *   
	 * * N+1 해결 방법
	 * 1. @OneToMany 를 안쓰고, Board 1번 조회, Reply 목록 1번 조회 따로 하기 (권장사항, 실무에서도 이 방법 선호)
	 * => 프론트 (React) 쪽에서 axios 요청을 동기식으로 두번 보내야 함
	 * => 애초에 N+1 문제가 발생하지 않음!!
	 * 
	 * 2. @OneToMany 와 @ManyToOne 어노테이션에 관련된 설정 값을 붙여서 해결하기
	 * => Board 1번 조회 요청만 보내면 Board 데이터 + Board 에 딸린 Reply 목록도 같이 조회됨
	 * => N+1 문제가 발생, 이를 해결하기 위해서 잔잔바리 설정들을 잘 작성해줘야함!!
	 * 
	 * * fetch (조회 전략을 설장할 수 있는 속성)
	 * - FetchType.LAZY : 실제로 접근할 때 조회 (시간차를 좀 두고 조회) -> 성능상 권장사항!!
	 * - FetchType.EAGER : 즉시 조회 (즉시 로딩, 한번에 다 조회해오겠다)
	 * 
	 * @OneToMany 는 기본값이 LAZY 임!!
	 * @ManyToOne 은 기본값이 EAGER 임!! -> 여기 때문에 StackOverflowError 가 발생
	 * 
	 * * 자바 Entity 클래스들을 통해 테이블을 만들고 외래키까지 걸어서 관계를 이어줄 때,
	 *   마찬가지로 삭제 제한 옵션이 걸린다!!
	 *   
	 * - @OneToMany 어노테이션에 삭제 옵션을 작성하지 않으면, 부모 데이터가 삭제되지 않음!!
	 *   (외래키 기본 삭제 옵션이 ON DELETE RESTRICTED)
	 * - 그럼에도 불구하고 부모데이터를 삭제하고 싶다면?
	 *   @OneToMany 어노테이션에 cascade=CascadeType.REMOVE 속성을 추가하면 된다!!
	 *   (부모데이터가 삭제 가능해지고, 부모데이터 삭제 시 자식데이터도 다 같이 삭제된다)
	 */  
	
}