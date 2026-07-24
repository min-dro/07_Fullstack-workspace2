package com.kh.jpa.board.model.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kh.jpa.member.model.vo.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="REPLY")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reply {

	@Id
	@Column(name="REPLY_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int replyNo;
	
	@Column(name="REPLY_CONTENT", nullable=false, length=400)
	private String replyContent;
	
	// 단순 숫자로 참조게시글 번호 저장 - 외래키가 안걸린다!!
	// @Column(name="REF_BNO", nullable=false)
	// private int refBno;
	
	// 외래키 적용
	@ManyToOne(fetch=FetchType.LAZY) // 기본값이 EAGER 라 직접 LAZY 명시 필요!!
	// > 이 Reply 를 기준으로 게시글 하나 당 (1) 댓글이 여러개 (N) 일 수 있다.
	@JoinColumn(name="REF_BNO")
	@ToString.Exclude
	// reply 기준으로 toString 이 호출 될 때 board 필드의 toString 이 실행됨
	// > 이 필드는 toString 으로부터 제외해달라 라는 뜻
	@JsonIgnore
	// > 우리는 Board 객체가 통으로 필요한게 아니라 사실 boardNo 필드만 필요한 것
	// > @ToString.Exclude, @JsonIgnote 는 연산량이 알게 모르게 계속 쌓이는 것을 방지해줌!! (부가적 원인)
	private Board board;
	
	// 단순 문자열로 작성자 아이디 저장 - 외래키가 안걸린다!!
	// @Column(name="REPLY_WRITER", nullable=false, length=30)
	// private String replyWriter;
	
	// 외래키 적용
	@ManyToOne
	// > 회원 1명이 댓글을 여러개 (N) 달 수 있다!!
	@JoinColumn(name="REPLY_WRITER")
	private Member member;
	
	@Column(name="CREATE_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
	private LocalDateTime cteateDate;
	
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;
	
	/*
	 * * JPA 의 연관관계 (Entity 간의 관계 설정, 외래키를 걸겠다)
	 * 
	 * - SQL 문에서 FOREIGN 로 테이블 간의 관계를 설정하듯이,
	 *   JPA 에서는 어노테이션으로 Entity 간의 관계를 정의한다.
	 *   
	 * * JPA 연관관계 종류
	 * 
	 * 1. @ManyToOne (N : 1) - 가장 기본적인 관계, 가장 많이 쓰임
	 * 여러 개의 자식 데이터가 하나의 부모데이터를 참조할 때
	 * 예) 1, 2, 3 번 댓글이 모두 5번 게시글에 딸려있을 때
	 *    5, 2, 6 번 게시글이 모두 3번 회원에 의해 작성되었을 때
	 * 
	 * [ 표현법 ]
	 * 자식 Entity 에서 작성하되, 외래키를 걸고자 하는 필드 (컬럼) 정보에
	 * 부모타입의 필드로 정의하고, @JoinColumn 어노테이션으로 실제 컬럼명을 지정한 뒤, @ManyToOne 붙이기
	 * 
	 * @ManyToOne
	 * @JoinColumn(name="실제컬럼명")
	 * private 부모Entity타입 필드명;
	 * 
	 * 실제 참조할 필드에 접근하려고 하면 자식Entity명.get부모Entity객체명().getPK필드명();
	 * 
	 * 2. @OneToMany (1 : N) - 외래키를 걸 때 필수로 써야 하는 어노테이션은 아님
	 * 부모 Entity 에서 자식 목록을 조회하고 싶을 떄 사용 (안적어도 자식 목록 조회 가능)
	 * 외래키를 걸 떄, 자식 Entity 쪽에 @ManyToOne 만 걸어도 충분하되,
	 * 부모 Entity 쪽에 @OneToMany 까지 걸면 "양방향 매핑" 이라고 부른다.
	 * 
	 * [ 표현법 ]
	 * 부모 Entity 에서 작성하되, 자식 목록 조회를 위한 어노테이션이기 때문에
	 * 자식타입만 담을 수 있는 List 로 필드를 정의하고, 실제 객체 생성은 ArrayList 로 해준다.
	 * 
	 * @OneToMany(mappedBy="자식Entity에서부모를 참조하던필드명")
	 * private List<자식Entity타입> 필드명 = new ArrayList<>();
	 * 
	 * @OneToMany 어노테이션이 걸린 필드는 실제 컬럼으로 만들어지지 않는다!!
	 * (@Column 이나 @JoinColumn 어노테이션이 붙지는 않았기 떄문)
	 * 자식 목록 조회를 위한 보조해주는 필드에 괜히 붙여주는 어노테이션이다!! (필수는 아님)
	 * 
	 * 3. @OneToOne (1 : 1)
	 * 하나의 Entity 가 다른 하나의 Entity 와 1 : 1로 매핑될 때 사용
	 * 예) 회원 테이블에 회원을 나타내야하는 컬럼이 30개 정도 된다고 치자..
	 * 회원의 인적정보들, 회원 프로필과 관련된 정보들,... 이런 경우에는 테이블을 1 : 1 로 쪼갠다.
	 * 
	 * 회원 Entity 1 <--> 프로필 Entity 1
	 * 
	 * [ 표현법 ]
	 * @OneToOne
	 * @JoinColumn(name="컬럼명")
	 * private Profile profile;
	 * 
	 * 4. @ManyToMany (N : M) - 거의 안씀
	 * 관계형 데이터베이스에서는 다 대 다 관계가 성립할 수 없다!!
	 * 중간다리 테이블을 하나 만들고 1 : N / M : 1 이렇게 쪼개줬었음!!
	 * 
	 * 중간 테이블이 자동으로 생성되긴 하나, 잘 안쓰임
	 * 실무에서는 중간 Entity 도 처음부터 같이 만들고, 모두 @ManyToOne 으로 풀어서 사용한다.
	 */
	
	
}