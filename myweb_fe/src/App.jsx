import './App.css'

import Header from "./common/components/Header";
import Footer from "./common/components/Footer";

import { Routes, Route } from 'react-router-dom';

import Index from "./Index";

import MemberListComponent from "./member/components/MemberListComponent";
import MemberDetailComponent from "./member/components/MemberDetailComponent";

import NoticeListComponent from "./notice/components/NoticeListComponent";
import NoticeEnrollFormComponent from "./notice/components/NoticeEnrollFormComponent";
import NoticeDetailComponent from "./notice/components/NoticeDetailComponent";
import NoticeUpdateFormComponent from "./notice/components/NoticeUpdateFormComponent";

function App() {
  
  // 실행할 구문

  // return 구문
  return (
    <div>
      
      {/* 모든 화면마다 상단에는 메뉴바 (Header) 배치 */}
      <Header />

      {/* 
        실제 화면 내용이 보여질 부분 (content 영역) 배치
        - 화면을 컴포넌트 단위로 만들고, Routes, Route 컴포넌트를 통해
          필요한 화면 컴포넌트의 갯수 만큼 URL 주소와 1:1 로 매핑하면 된다!!
      */}
      <div className="content">
        
        <Routes>
          <Route path="/" element={ <Index /> } />
          
          <Route path="/member/list" element={ <MemberListComponent /> } />
          <Route path="/member/detail/:userId" element={ <MemberDetailComponent /> }/>

          <Route path="/notice/list" element={ <NoticeListComponent /> } />
          <Route path="/notice/enrollForm" element={ <NoticeEnrollFormComponent /> } />
          <Route path="/notice/detail/:noticeNo" element={ <NoticeDetailComponent /> } />
          <Route path="/notice/updateForm" element={ <NoticeUpdateFormComponent /> } />
        </Routes>
        
      </div>

      {/* 모든 화면마다 하단에는 푸터 (Footer) 배치 */}
      <Footer />

    </div>
  )
}

// 내보내기
export default App;