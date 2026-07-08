import { Link } from "react-router-dom";

import "../styles/Header.css"

// 헤더를 나타내는 컴포넌트 - 모든 페이지 상단에 위치
// (기존의 menubar.jsp 에 대응됨)
function Header() {

    // 실행할 구문

    // return 구문
    return (
        <div>
            <h1 align="center">Welcome to React Managet</h1>

            <br/><br/>

            <div className="navi">
                <div>
                    <Link to="/">HOME</Link>
                </div>
                <div>
                    <Link to="/members">회원관리</Link>
                </div>
                <div>
                    <Link to="/boards">공지사항 목록 관리</Link>
                </div>
                <div>
                    <Link to="/boards">일반게시판관리</Link>
                </div>
            </div>
        </div>
    );
}

// 내보내기
export default Header;