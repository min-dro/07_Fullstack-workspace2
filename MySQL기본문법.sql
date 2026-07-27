-- 한줄 짜리 주석
/*
	여러줄
	 짜리
     주석
*/

/*
	* MySQL
    
	  - 본래는 오픈소스 RDBMS 였으나, 지금은 오라클사에서 제공하고 관리하는 DB 제품
      - 그래서 마찬가지로 상업용도로 쓸 경우는 비용을 지불해야하고,
        개인적인 용도로 쓸 경우에는 무료로 언제든지 사용 가능하다.
	- 오라클사에 인수되고 난 뒤, 기존 MySQL 개발자가 혹시 몰라서 그대로 복제 뜬 DB 제품도 있음
		(Maria DB : MySQL 과 구조와 개념과 문법이 완전히 똑같음, 여전히 오픈소스)
*/

/*
	1. 테이블 생성 (CREATE)
    
	  [ 표현법 ]
      CREATE TABLE 테이블명 (
      컬럼명 자료형 제약조건,
	    컬럼명 자료형 제약조건,
        ...
	  );
      
      - 오라클과 문법이 완전히 동일하다.
	- 단, 자료형이 다르다!!
    
    * MySQL 자료형
      - 문자
      CHAR(글자수) : 고정 길이 문자열, 최대 255글자 까지 표현 가능
      VARCHAR(글자수) : 가변 길이 문자열, 최대 65,535Byte 만큼의 글자수 표현 가능
      TEXT : VARCHAR 보다 더 긴 문자열을 저장하고 싶을 때, 기존 오라클의 CLOB 과 비슷
      
      - 숫자
        INT : 정수 저장 (자바의 int 와 저장 범위 동일)
        BIGINT : 더 큰 범위의 정수 저장 (자바의 long 과 저장 범위 동일)
        FLOAT : 4Byte 실수 자료형 (자바의 float 와 저장 범위 동일)
        DOUBLE : 8Byte 실수 자료형 (자바의 double 과 저장 범위 동일)
        DECIMAL : 정확한 숫자의 실수값까지 표현하고 싶을 때 사용
        
	  - 날짜
        DATE : 년, 월, 일 까지 저장
        DATETIME : 년, 월, 일, 시, 분, 초 모두 저장
        TIMESTAMP : 년, 월, 일, 시, 분, 초 모두저장,
			CURRENT_TIMESTEMP 로 현재시간을 저장할 수 있다.
            (오라클로 따지면 SYSDATE)
            
	* 제약조건 (오라클과 동일)
      - PRIMARY KEY : 기본키 (주키), 테이블에서 각 행을 식별하는 역할의 컬럼,
			테이블 당 최대 한개까지 지정 가능 (단, 복합키는 가능)
	  - UNIQUE : 중복 값 저장 불가 (NULL 은 저장 가능)
      - NOT NULL : NULL 값 저장 불가
      - CHECK : 지정한 조건식에 맞는 값만 저장 가능
		  원래는 없었으나 8.0.16버전 이상부터 지원함
	- FOREIGN KEY : 다른 테이블의 PRIMARY KEY 값만 들어올 수 있음 (참조한다)
			테이블 간의 관계를 설정, JOIN 또한 가능 (ANSI 구문 이용)
            
	+ 제약조건까지는 아니지만
		- DEFAULT : INSERT 시 값을 제시하지 않으면 기본값 설정
        - AUTO_INCREMENT : 행이 INSERT 될 때 마다 자동으로 숫자를 1씩 증가시켜서 넣어줌
				오라클의 SEQUENCE 를 대체하는 기능
						(주로 PRIMARY KEY 컬럼에 붙여서 사용)
*/

-- 테이블 생성 실습
-- 기존의 오라클의 FULLSTACK 계정의 테이블 구조를 그대로 옮겨보기

-- 본격적인 SQL문 작성 및 실행 전, 스키마를 이용하겠다고 명령문을 한번 실행시키고 나서 들어가야한다!!
-- [ 표현법 ]
-- USE 스키마명;

-- USE PRACTICE;
-- PRACTICE 라는 DB 스키마를 이용하겠다.

USE FULLSTACK;

-- 테이블을 생성하기 전에 이미 존재하는 이름일 수도 있으니 DROP TABLE 먼저하기!!
-- [ 표현법 ]
-- DROP TABLE 테이블명; -- 그냥 테이블을 삭제하겠다.
-- DROP TABLE IF EXISTS 테이블명; -- 만약 해당 테이블이 존재하면 (IF EXISTS) 삭제하겠다.
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS NOTICE;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS REPLY;

-- 본격적으로 테이블 생성해보기
-- MEMBER 테이블
CREATE TABLE MEMBER (
  USER_ID		VARCHAR(30) PRIMARY KEY									   	COMMENT '회원아이디',
  USER_PWD		VARCHAR(100) NOT NULL									   	COMMENT '회원비밀번호',
  USER_NAME		VARCHAR(5) NOT NULL										   	COMMENT '회원이름',
  EMAIL			VARCHAR(100)											   	COMMENT '회원이메일',
  GENDER		CHAR(1) CHECK(GENDER IN ('M', 'F'))						   	COMMENT '회원성별',
  AGE			INT														   	COMMENT '회원나이',
  PHONE			VARCHAR(13)												   	COMMENT '회원전화번호',
  ADDRESS		VARCHAR(35)												   	COMMENT '회원주소',
  ENROLL_DATE	TIMESTAMP DEFAULT CURRENT_TIMESTAMP						   	COMMENT '회원가입날짜',
  MODIFY_DATE	TIMESTAMP DEFAULT CURRENT_TIMESTAMP						   	COMMENT '회원수정날짜',
  STATUS		CHAR(1) DEFAULT 'Y' CHECK(STATUS IN ('Y', 'N'))			   	COMMENT '회원상태값',
  ROLE			VARCHAR(6) DEFAULT 'USER' CHECK(ROLE IN ('ADMIN', 'USER')) 	COMMENT '회원권한'
);
-- 컬럼마다 COMMENT 를 달고싶다면 컬럼 옆에 COMMENT '주석내용' 으로 같이 작성한다.

-- 만들어진 MEMBER 테이블에 회원정보 담아보기
-- INSERT 문 사용 : 오라클과 문법이 아예 똑같음
INSERT INTO MEMBER(USER_ID
				 , USER_PWD
				 , USER_NAME
                 , EMAIL
                 , GENDER
                 , AGE
                 , PHONE
                 , ADDRESS
                 , ROLE)
		    VALUES('admin'
				 , '$2a$10$BD.4Ik86tpfIc8sdj70sGOkdaZXTjTaAXurZqCW0E/xCHF0nmcYBa'
				 , '관리자'
                 , 'admin@kh.or.kr'
                 , 'M'
                 , 30
                 , '010-1234-5678'
                 , '서울시 강남구'
                 , 'ADMIN');
                 
INSERT INTO MEMBER(USER_ID
				 , USER_PWD
                 , USER_NAME
                 , EMAIL
                 , GENDER
                 , AGE
                 , PHONE
                 , ADDRESS)
			VALUES('user01'
				 , '$2a$10$j0Mp0DR9m2pi7Hf8wCArsu/HEzdlYBeLbsg03cllOKKuPYmLJvKee'
                 , '홍길동'
                 , 'user01@kh.or.kr'
                 , 'F'
                 , 23
                 , '010-9876-5432'
                 , '경기도 수원시');
                 
INSERT INTO MEMBER(USER_ID
				 , USER_PWD
                 , USER_NAME
                 , EMAIL
                 , GENDER
				 , AGE
                 , PHONE
                 , ADDRESS)
			VALUES('user02'
				 , '$2a$10$nhKEdpo.1lu/mRUnxQGygeJ7kqsbSe74dtK58NMA50AvEDFaQRe0m'
                 , '김말똥'
                 , 'user02@kh.or.kr'
                 , 'M'
                 , 47
                 , '010-5555-6666'
                 , '서울시 영등포구');
                 
-- 잘 INSERT 되었는지 직접 확인하고 싶다면?
-- SELECT 문을 이용 : 오라클과 완전히 동일
SELECT *
  FROM MEMBER;
  
SELECT *
  FROM MEMBER
 WHERE USER_ID = 'admin';
 
-- INSERT 된 데이터가 DB 에 반영되려면 COMMIT 까지 해줘야 한다!!
-- 그런데, MySQL 에서는 기본 설정이 자동 커밋임!! (안해줘도 바로 반영이 된 상태임)
-- 그렇다고 해서 TRANSACTION 처리 개념이 없는것음 아님!!

-- NOTICE 테이블
CREATE TABLE NOTICE (
	NOTICE_NO		INT AUTO_INCREMENT PRIMARY KEY					COMMENT '공지사항번호',
    NOTICE_TITLE	VARCHAR(35) NOT NULL							COMMENT '공지사항제목',
    NOTICE_WRITER	VARCHAR(30)										COMMENT '공지사항작성자아이디',
    NOTICE_CONTENT	VARCHAR(200) NOT NULL							COMMENT '공지사항내용',
    CREATE_DATE		TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL	COMMENT '공지사항작성날짜',
	STATUS			CHAR(1) DEFAULT 'Y'								COMMENT '공지사항상태값',
    FOREIGN KEY(NOTICE_WRITER) REFERENCES MEMBER(USER_ID) ON DELETE SET NULL -- 테이블레벨 방식
);
-- 오라클과 제역조건 부여 방식, 문법, 외래키 삭제 옵션 등 다 동일하다!!

-- 만들어진 NOTICE 테이블에 INSERT
INSERT INTO NOTICE(NOTICE_TITLE
				 , NOTICE_WRITER
                 , NOTICE_CONTENT)
			VALUES('관지자 공지'
				 , 'admin'
                 , '공지서비스를 시작합니다. 많이 이용해 주세요.');
                 
INSERT INTO NOTICE(NOTICE_TITLE
				 , NOTICE_WRITER
                 , NOTICE_CONTENT)
			VALUES('공지서비스 오픈 환영'
				 , 'admin'
                 , '드디어 오픈되었습니다. 많은 관심 부탁드려요!');
                 
INSERT INTO NOTICE(NOTICE_TITLE
				 , NOTICE_WRITER
                 , NOTICE_CONTENT)
			VALUES('공지서비스 이용 안내'
                 , 'admin'
                 , '게시판 서비스는 회원만 이용 가능합니다. 회원가입을 먼저 해주세요.');
                 
SELECT *
  FROM NOTICE;
  
-- BOARD 테이블
CREATE TABLE BOARD (
	BOARD_NO		INT AUTO_INCREMENT PRIMARY KEY		COMMENT '게시글번호',
    BOARD_TITLE		VARCHAR(35) NOT NULL				COMMENT '게시글제목',
    BOARD_WRITER	VARCHAR(30)							COMMENT '게시글작성자아이디',
    BOARD_CONTENT	VARCHAR(200) NOT NULL				COMMENT '게시글내용',
    ORIGIN_NAME		VARCHAR(50)							COMMENT '첨부파일의원래이름',
    CHANGE_NAME		VARCHAR(50)							COMMENT '첨부파일의변경된이름',
    COUNT			INT DEFAULT 0						COMMENT '게시글조회수',
    CREATE_DATE		TIMESTAMP DEFAULT CURRENT_TIMESTAMP	COMMENT '게시글작성날짜',
    STATUS 			CHAR(1) DEFAULT 'Y'					COMMENT '게시글상태값',
    FOREIGN KEY(BOARD_WRITER) REFERENCES MEMBER(USER_ID) ON DELETE CASCADE
);

INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('관리자 게시글', 'admin', '저희 사이트를 이용해 주셔서 감사합니다.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('첫번째 게시글', 'user01', '자바는 대표적인 객체지향프로그래밍 언어입니다.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('두번째 게시글', 'user02', '오라클은 데이터베이스');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('세번째 게시글', 'admin', 'JDBC는 자바와 오라클을 연동시켜주는 기술입니다.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('네번째 게시글', 'admin', '웹의 3요소는 html, css, javascript!');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('다섯번째 게시글', 'user01', 'html 은 웹의 뼈대를 구성하는 언어');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('여섯번째 게시글', 'user02', 'css 는 웹을 예쁘게 꾸며주는 역할');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('일곱번째 게시글', 'user02', '자바스크립트는 웹의 동적인 효과를 구현할 수 있게 도와줌');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('여덟번째 게시글', 'user01', 'jQuery 는 자바스크립트의 복잡한 문법을 대체');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('아홉번째 게시글', 'admin', '서블릿은 자바 코드로 웹 서버를 구동할 수 있게 도와주는 일종의 컨트롤러 역할을 하는 자바 클래스입니다.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열번째 게시글', 'admin', 'JSP 는 동적인 페이지를 구현 가능한 개념!');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열한번째 게시글', 'user01', 'jstl 쓰면 JSP 에서 자바 구문을 없앨 수 있어요.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열두번째 게시글', 'user01', '프레임워크의 종류로는 영속성, 자바 구문을 없앨 수 있어요.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열세번째 게시글', 'user02', '마이바티스는 DB 의 영속성 작업, 즉 CRUD 작업을 편리하게 도와주는 프레임워크입니다.');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열네번째 게시글', 'user01', '스프링은 자바로 웹사이트 구현을 더 수월하게 할 수 있도록 도와주는 프레임워크에요!');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열다섯번째 게시글', 'user02', '리액트는 SPA (Single Page Application) 구현을 도와주는 자바스크립트 라이브러리임둥');
INSERT INTO BOARD (BOARD_TITLE, BOARD_WRITER, BOARD_CONTENT) VALUES ('열여섯번째 게시글', 'admin', '열심히 공부하세요. 화이팅.');

SELECT *
  FROM BOARD
 ORDER BY BOARD_NO DESC;
 
 -- REPLY 테이블
 CREATE TABLE REPLY (
	REPLY_NO		INT AUTO_INCREMENT PRIMARY KEY		COMMENT '댓글번호',
    REPLY_CONTENT	VARCHAR(150) NOT NULL				COMMENT '댓글내용',
    REF_BNO			INT NOT NULL						COMMENT '참조게시글번호',
    REPLY_WRITER 	VARCHAR(30) NOT NULL				COMMENT '댓글작성자아이디',
    CREATE_DATE		TIMESTAMP DEFAULT CURRENT_TIMESTAMP	COMMENT '댓글작성날짜',
    STATUS			CHAR(1) DEFAULT 'Y' NOT NULL		COMMENT '댓글상태값',
    FOREIGN KEY(REF_BNO) REFERENCES BOARD(BOARD_NO),
    FOREIGN KEY(REPLY_WRITER) REFERENCES MEMBER(USER_ID)
 );
 
 -- INSERT
 INSERT INTO REPLY(REPLY_CONTENT
			     , REF_BNO
                 , REPLY_WRITER)
			VALUES('첫번째 댓글입니다'
				 , 1
                 , 'user02');
                 
INSERT INTO REPLY(REPLY_CONTENT
				, REF_BNO
                , REPLY_WRITER)
		   VALUES('두번째 댓글입니다.'
				, 1
                , 'user01');
                
INSERT INTO REPLY(REPLY_CONTENT
				, REF_BNO
                , REPLY_WRITER)
		   VALUES('세번째 댓글입니다.'
				, 1
                , 'admin');
			
INSERT INTO REPLY(REPLY_CONTENT
				, REF_BNO
                , REPLY_WRITER)
		   VALUES('첫번째 댓글입니다.'
				, 5
                , 'user01');
                
SELECT *
  FROM REPLY
 WHERE REF_BNO = 1
 ORDER BY REPLY_NO DESC;
 
 /*
	* Oracle 과의 차이점 종합정리
    
      - CREATE TABLE
      1. VARCHAR2(바이트수) -> VARCHAR(글자수)
         CHAR(바이트수)	  -> CHAR(글자수)
	  2. NUMBER -> INT 또는 BIG 또는 FLOAT 또는 DOUBLE 또는 DECIMAL
      3. SYSDATE -> CURRENT_TIMESTAMP
      4. COMENT ON COLUMN 테이블명.컬럼명 IS '주석' -> 컬럼 옆에 COMMENT '주석'
      5. 시퀀스 -> AUTO_INCREMENT
      6. 커밋 불필요 (자동 커밋)
      7. 만약 날짜를 직접 INSERT 하고 싶다면 'YYYY-MM-DD' 이런식으로 포맷을 맞춰서 써야함
      
      - DROP TABLE
      1. IF EXISTS 옵션 존재
      
      - SELECT, INSERT, UPDATE, DELETE
      오라클과 완전 동일함!!
 */
 
 /*
	* MySQL 의 ALTER 구문 (DDL)
    
    [ 표현법 ]
    ALTER TABLE 테이블명 바꿀내용;
    
    1) 컬럼 추가
    ADD 컬럼명 자료형;
    
    2) 컬럼 수정
    - 자료형 수정
    MODIFY 컬럼명 바꿀자료형 제약조건;
    
    3) 컬럼명 수정
    CHANGE 기존컬럼명 바꿀컬럼명 자료형;
    (기존 오라클에서는 RENAME 이였음)
    
    4) 컬럼 삭제
    DROP COLUMN 삭제할컬럼명;
    
    5) 제약조건 추가
    ADD CONSTRAINT 제약조건명 제약조건종류(컬럼명);
    
    6) 제약조건 삭제
    DROP CONSTRAINT 제약조건명;
    
    7) 테이블명 변경
    RENAME TO 새테이블명;
 */