import { useEffect, useState } from "react";

import { useSearchParams } from "react-router-dom";

import { selectBoardListApi } from "../api/boardApi";

import BoardItemComponent from "./BoardItemComponent";

function BoardListComponent() {

    // 실행할 구문
    // 입력된 검색어를 담아둘 State 형 변수 셋팅
    const [keyword, setKeyword] = useState("");

    // 리액트의 브라우저 URL 주소에 QueryString 방식도 적용할 수 있다!!
    // > useSearchParams 라는 훅을 이용하면 된다!! (useState 훅과 사용법은 비슷)
    const [searchParams, setSearchParams] = useSearchParams();
    // > searchParams : 현재 URL 의 QueryString 을 담는 객체
    // > setSearchParams : URL 의 QueryString 을 변경하는 함수 (setter 함수)
    // > QueryString 을 조작할 경우에는 Link 컴포넌트와 함께 쓰면 안됨!! (충돌남)

    // > cpage 값이 새로고침되는것을 막기 위해 QueryString 을 활용한다!!
    const cpage = parseInt(searchParams.get("cpage")) || 1;
    // > or 연산자를 삼항연산자처럼 쓰면서 QueryString 상에 cpage 라는 키 + 밸류 세트가 없다면
    //   cpage 라는 변수에는 지금 1 로 초기화 될 것!!

    // 현재 내가 보고있는 페이지 수 (currentPage) 를 담아낼 State 형 변수 셋팅
    // const [cpage, setCpage] = useState(1);
    // > 목록페이지 첫 진입 시 무조건 1번 페이지가 나와야 하므로 1로 초기화
    // > State 형 변수는 브라우저에서 새로고침 시 값이 "초기화" 된다!!

    // 조회된 데이터를 tr 요소로 감싸서 배열에 차곡차곡 담아둘 State 형 변수 셋팅
    const [dataList, setDataList] = useState([]);

    // 상황에 맞는 페이징바를 나타내는 Link 컴포넌트를 배열에 차곡차곡 담아둘 State 형 변수 셋팅
    const [pageList, setPageList] = useState([]);

    // 이 컴포넌트 함수가 최초 로딩시 딱 한번만 실행할 구문
    useEffect(() => {

        const selectBoardList = async () => {

            try {

                const response = await selectBoardListApi(cpage);

                // console.log(response.data);
                // console.log(response.data.list); // [{}, {}, ...]
                // console.log(response.data.pi); // {}

                // 실제 tbody 에 들어갈
                // 우선 response.data.list 를 별도의 변수를 옮겨담기
                const items = response.data.list; 

                const trArr = items.map((item, index) => {
                    
                    return( 
                        <BoardItemComponent key={ index } item={ item } />
                    );
                });

                setDataList(trArr);

                // paging-area 에 들어갈 데이터 후처리
                // 우선 response.data.pi 를 별도의 변수로 옮겨담기
                const pageInfo = response.data.pi;

                // console.log(pageInfo);
                // > pageInfo.startPage ~ pageInfo.endPage 까지 1씩 증가시키면서 페이징바 버튼 생성
                const btnArr = [];

                if(cpage == 1) {

                    btnArr.push(
                        <button key="prev" className="btn btn-info btn-sm">
                            &lt;
                        </button>
                    );

                } else {

                    btnArr.push(
                        <button key="prev" className="btn btn-outline-info btn-sm"
                              onClick={ () => {
                                /* setCpage(cpage - 1); */
                                setSearchParams({ cpage : cpage - 1 });
                              } }>
                            &lt;
                        </button>
                    );
                }

                for(let p = pageInfo.startPage; p <= pageInfo.endPage; p++) {

                    if(cpage == p) {
                        
                        btnArr.push(
                            <button key={ p } className="btn btn-info btn-sm">
                                { p }
                            </button>
                        );

                    } else {
                      
                        btnArr.push(
                            <button key={ p } className="btn btn-outline-info btn-sm"
                                  onClick={ () => { 
                                    /* setCpage(p); */
                                    setSearchParams({ cpage : p })
                                } }>
                                { p }
                            </button>
                        );
                    }
                }

                if(cpage == pageInfo.maxPage) {

                    btnArr.push(
                        <button key="next" className="btn btn-info btn-sm">
                           &gt;
                        </button>
                    );

                } else {

                    btnArr.push(
                        <button key="next" className="btn btn-outline-info btn-sm"
                                onClick={ () => { 
                                    /* setCpage(cpage + 1); */
                                    setSearchParams({ cpage : cpage + 1 });
                                } }>
                           &gt;
                        </button>
                    );
                }

                setPageList(btnArr);

            } catch(error) {

                console.log("일반게시글 목록 조회용 ajax 통신 실패!" + error);
            }
        };

        selectBoardList();

    }, [cpage]);

    // 검색어 입력 내용이 변경될 때 마다 실행할 이벤트 핸들러 함수
    const handleChange = e => {

        setKeyword(e.target.value);
    };

    // return 구문
    return (
        <div>
            <h2 align="center">일반게시글 목록 조회</h2>

            <br /><br />

            {/* 검색창 영역 */}
            <div align="center" className="search-area">
                <form>
                    <input type="text" name="keyword" placeholder="제목을 입력하세요" 
                           value={ keyword } onChange={ handleChange } />
                    <button type="submit">검색</button>
                </form>
            </div>

            <br /><br />

            <div align="right" style={ {width : "950px"} }>
                <button className="btn btn-outline-secondary btn-sm">
                    글작성
                </button>
            </div>

            <br/>

            {/* 게시글 목록을 보여주는 리스트 영역 */}
            <table className="list-area table table-hover">
                <thead>
                    <tr>
                        <th width="150">글번호</th>
                        <th width="500">제목</th>
                        <th width="200">작성자</th>
                        <th width="150">조회수</th>
                        <th width="200">작성일</th>
                        <th width="150">첨부파일</th>
                    </tr>
                </thead>
                <tbody>{ dataList }</tbody>
            </table>

            <br></br>

            {/* 페이징바 영역 */}
            <div align="center" className="paging-area">{ pageList }</div>

            <br /><br />

        </div>
    );

}

// 내보내기
export default BoardListComponent;