// axios 를 직접적으로 호출하는 구문들만 별도로 뺀 파일
// > 컴포넌트 쪽에는 진짜 화면을 그리는 로직들만, Api 파일에는 axios 호출 구문만 둬서 역할을 분리한다!!
// > 단, import, export 를 잘 신경 써서 실수하지 않도록 하자!!
// > 또한, 코드를 분리했다고 해서 async 와 await 도 누락시키면 안된다!!

import axios from "axios";

// 이 파일 내의 모든 함수에서 공통적을
// "http://localhost:8006/myweb/members" 까지가 공통 호출 URL 주소임!!
// > 전역변수로 빼기
const BASE_URL = "http://localhost:8006/myweb/members";
// > 추후 운영 또는 배포상의 이유로 ip주소 또는 포트번호 또는 context path 등이 변경되면
//   이 변수값만 수정하면 모두 반영됨!!

// 회원 목록 조회용
const selectMemberListApi = () => {

    const response = axios({
        url : `${ BASE_URL }`,
        method : "get" 
    });
    
    return response;
};

// 회원 상세 조회용
const selectMemberApi = userId => {

    const response = axios({
        url : `${ BASE_URL }/${ userId }`,
        method : "get"
    });

    return response;
};

// 회원 정보 수정용
const updateMemberApi = (userId, member) => {

    const response = axios({
        url : `${ BASE_URL }/${ userId }`,
        method : "put",
        data : member
    });

    return response;
};

// 회원 탈퇴용
const deleteMemberApi = userId => {

    const response = axios({
        url : `${ BASE_URL }/${ userId }`,
        method : "delete" 
    });

    return response;
};

// 내보내기
export { selectMemberListApi, selectMemberApi, updateMemberApi, deleteMemberApi };