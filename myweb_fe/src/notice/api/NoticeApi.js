import axios from "axios";

// 공지사항 목록 조회용
const selectNoticeListApi = () => {

    const response = axios({
        url : "http://localhost:8006/myweb/notices",
        method : "get"
    });

    return response;
};

export { selectNoticeListApi }