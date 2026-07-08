import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

import { BrowserRouter } from "react-router-dom";

/*
 * 1. 프로젝트 생성 (보통 프로젝트명_fe 으로 짓는다)
 * 2. axios, react-router-dom@6 모듈 설치
 * 3. main.jsx 에서 App 컴포넌트를 BrowserRouter 컴포넌트로 감싸기
 * 4. index.css, App.css 의 기본코드 지우기
 *    (둘 중에 파일은 하나만 살리기)
 * 5. App.jsx 에서 기본 화면 코드를 날리고 작업 시작하기
 * 6. src 폴더에 도메인 별로 (회원, 공지사항, 게시글 등) 폴더 생성
 * - 각 도메인 별 폴더 내에 components, srtles 폴더를 각각 또 생성
 *   (components 에는 .jsx, style 애는 .css 파일을 저장한다)
 */

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
)

/*
 * * 리액트와 스프링 부트를 연동해서 "관리자 페이지 (ERP)" 만들기 
 * 
 * 1. 회원 관리 기능
 * 회원 목록 조회 - R (Read) : SELECT 문        GET     /members
 * 회원 상세 조회 - R (Read) : SELECT 문        GET     /members/{userId}
 * 회원 정보 수정 - U (Update) : UPDATE 문      PUT     /members/{userId} + data 전송
 * 회원 탈퇴 - D (Delete) : UPDATE 문           DELETE  /members/{userId}
 * 
 * 2. 공지사항 관리 기능
 * 공지사항 목록 조회 - R (Read) : SELECT 문    GET      /notices
 * 공지사항 상세 조회 - R (Read) : SELECT 문    GET      /notices/{noticeNo}
 * 공지사항 작성 - C (Create) : INSERT 문       POST     /notices + data 전송
 * 공지사항 수정 - U (Update) : UPDATE 문       PUT      /notices/{noticeNo} + data 전송
 * 공지사항 삭제 - D (Delect) : UPDATE 문       DELECT   /notices/{noticeNo}
 * 
 * 3. 일반게시판 관리 기능
 * 일반게시글 목록 조회 + 페이징처리 - R (Read) : SELECT 문       GET     /boards + params 전송
 * 일반게시글 검색 + 페이징처리 - R (Read) : SELECT 문            GET     /boards/{keyword} + params 전송
 * 일반게시글 작성 + 첨부파일1개업로드 - C (Create) : INSERT 문    POST    /boards + data 전송
 * 일반게시글 상세 조회 - R (Read) : SELECT 문                    GET     /boards/{boardNo}
 * 일반게시글 수정 - U (Update) : UPDATE 문                       PUT     /boards/{boardNo} + data 전송
 * 일반게시글 삭제 - D (Delete) : DELETE 문                       DELETE  /boards/{boardNo}
 * 
 * - 일반게시글 검색 부분관 일반게시글 상세 조회 쪽 URL 주소가 겹침!!
 *   추후 실제 구현 시 고민해보고 바꿀 예정!!
 * - 일반게시글 수정 부분도 PUT 으로 요청할 예정이라고 적긴 했으나,
 *   실제 구현에서는 다르게 들어갈 예정!!
 */