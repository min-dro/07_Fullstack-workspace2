function BoardItemComponent(props) {

    // 실행할 구문
    // props = {item : {여기}};
    const item = props.item;

    // return 구문
    return (
        <tr>
            <td>{ item.boardNo }</td>
            <td>{ item.boardTitle }</td>
            <td>{ item.boardWriter }</td>
            <td>{ item.count }</td>
            <td>{ item.createDate.substring(0, 10) }</td>
            <td>{ (item.originName != null) ? "★" : "" }</td>
        </tr>
    );
}

// 내보내기
export default BoardItemComponent;