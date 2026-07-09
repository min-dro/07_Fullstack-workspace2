function NoticeItemComponent(props) {

    // 실행할 구문
    const item = props.item;

    // return 구문
    return (
        <tr>
            <td>{ item.noticeNo }</td>
            <td>{ item.noticeTitle }</td>
            <td>{ item.noticeWriter }</td>
            <td>{ item.createDate.substring(0, 10) }</td>
        </tr>
    );
}

// 내보내기
export default NoticeItemComponent;