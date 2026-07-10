import { useState, useEffect } from "react";

import { selectNoticeListApi } from "../api/NoticeApi";

import NoticeItemComponent from "./NoticeItemComponent";

import { useNavigate } from "react-router-dom";

function NoticeListComponent() {

    // 실행할 구문
    // 화면 깜빡임 없이 URL 주소를 전환해줄 navigate 함수 셋팅
    let navigate = useNavigate();

    // 조회된 데이터를 tr 요소로 감싼 뒤 차곡차곡 담을 배열 형태의 State 변수 셋팅
    const [dataList, setDataList] = useState([]);

    // 이 컴포넌트가 다 로딩된 후 딱 한번만 실행할 구문
    useEffect(() => {

        const setNoticeList = async () => {

            try {

                const response = await selectNoticeListApi();

                // console.log(response.data);
                
                // 응답데이터를 별도의 변수로 옮겨담기
                const items = response.data;

                const trArr = items.map((item, index) => {
                    
                    return(
                        <NoticeItemComponent key={ index } item={ item } />
                    );
                });

                setDataList(trArr);

            } catch(error) {

                console.log("공지사항 목록 조회용 ajax 통신 실패!" + error);
            }
        };

        setNoticeList();

    }, []);

    // return 구문
    return (
        <div>
            <h2 align="center">공지사항 목록 조회</h2>

            <br /><br />

            <div align="right" style={ {width : "950px"} }>
                <button className="btn btn-outline-secondary btn-sm"
                        onClick={ () => { navigate("/notice/enrollForm"); } }>
                    글작성
                </button>
            </div>

            <br />

            <table className="list-area table table-hover">
                <thead>
                    <tr>
                        <th width="150">글번호</th>
                        <th width="500">제목</th>
                        <th width="200">작성자</th>
                        <th width="200">작성일</th>
                    </tr>
                </thead>
                <tbody>{ dataList }</tbody>
            </table>

            <br /><br />
        </div>
    );
}

// 내보내기
export default NoticeListComponent;