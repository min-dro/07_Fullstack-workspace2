import axios from "axios";

const BASE_URL = "http://localhost:8006/myweb/boards";

const selectBoardListApi = cpage => {

    const response = axios({
        url : `${ BASE_URL }`,
        method : "get",
        params : {
            cpage : cpage
        }
    });

    return response;
};

export { selectBoardListApi };