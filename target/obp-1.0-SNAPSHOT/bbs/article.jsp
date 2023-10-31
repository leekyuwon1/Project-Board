<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>자 유 게 시 판</title>

    <link rel="stylesheet" type="text/css" href="<%=cp%>/bbs/css/style.css?after"/>
    <link rel="stylesheet" type="text/css" href="<%=cp%>/bbs/css/article.css"/>
    <link rel="stylesheet" type="text/css" href="<%=cp%>/bbs/css/comment.css?after"/>
<%-- 이렇게 링크 뒤에 ?after 쿼리 파라미터를 남겨주면 기존 캐시를 무시하고 새로운 버전을 가져오게 되면서 css변경한게 반영됨. 하지만 남용하게되면 성능 부하가 생길수 있음--%>
    <script type="text/javascript">
        function changeView(value)
        {
            if(value == 0)
                location.href='BoardListAction.bo?page=${pageNum}';
            else if(value == 1)
                location.href='BoardReplyFormAction.bo?num=${dto.num}&page=${pageNum}';
        }

        function doAction(value)
        {
            if(value == 0) // 수정
                location.href="BoardUpdateFormAction.bo?num=${dto.num}&page=${pageNum}";
            else if(value == 1) // 삭제
                location.href="BoardDeleteAction.bo?num=${dto.num}";
        }


        var httpRequest = null;

        // httpRequest 객체 생성
        function getXMLHttpRequest(){ /* 객체 호출 */
            var httpRequest = null;

            if(window.ActiveXObject){ /* 브라우저 ActiveXObject 확인하는 제어문 */
                try{
                    httpRequest = new ActiveXObject("Msxml2.XMLHTTP"); // 해당 루프가 참이라면 객체 생성
                } catch(e) {
                    try{
                        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e2) { httpRequest = null; }
                }
            }
            else if(window.XMLHttpRequest){
                httpRequest = new window.XMLHttpRequest();
            }
            return httpRequest;
        }

        function writeCmt() {
            var form = document.getElementById("writeCommentForm");

            var id = form.comment_id.value;
            var content = form.comment_content.value;

            if (!content) {
                alert("내용을 입력하세요.");
                form.comment_content.focus();
                return false;
            } else {
                var param = "comment_board=" + ${dto.num} + "&comment_id=" + id + "&comment_content=" + content;

                httpRequest = getXMLHttpRequest(); // 동기 / 비동기 처리를 위한 객체
                // 비동기적으로 포스트 방식으로 보낸다 주소는 commentwriteAction.co 로

                httpRequest.open("POST", "commentwriteaction.co", true);
                httpRequest.onreadystatechange = checkFunc; // 서버 응답이 됬을 때 콜백함수( onreadystatechange )가 checkFunc를 사용
                httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
                httpRequest.send(param);
            }
        }
        function checkFunc() {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    // 서버 응답이 성공적으로 도착한 경우
                    var resultText = httpRequest.responseText;
                    if (resultText == 1) {
                        console.log(resultText);
                        document.location.reload(); // 현재 창 새로고침
                    } else {
                        // 서버 응답은 성공적이었지만, 어떤 처리를 해야 할지 정의되지 않은 경우
                        console.log("서버 응답의 결과:" + resultText);
                        console.error('서버 응답은 성공적이었으나 처리할 내용이 정의되지 않았습니다.');
                    }
                } else {
                    // 서버 응답이 실패한 경우
                    console.error('서버 응답이 실패하였습니다. 상태 코드:', httpRequest.status);
                }
            }
        }
    </script>
</head>
<body>

<div id="bbs">

    <div id="bbs_title">
        자 유 게 시 판
    </div>
    <div id="bbsArticle">

        <div id="bbsArticle_header">
            ${dto.subject }
        </div>

        <div class="bbsArticle_bottomLine">
            <dl>
                <dt>작성자</dt>
                <dd>${dto.name }</dd>
                <dt>줄수</dt>
                <dd>${lineSu }</dd>
            </dl>
        </div>

        <div class="bbsArticle_bottomLine">
            <dl>
                <dt>등록일</dt>
                <dd>${dto.created }</dd>
                <dt>조회수</dt>
                <dd>${dto.hitCount }</dd>
            </dl>
        </div>
        <div class="bbsArticle_bottomLine">
            <dl>
<%--                <dt>IpAddr</dt>--%>
<%--                <dd>${dto.ipAddr}</dd>--%>
                <dt>댓글 수</dt>
                <dd>${commentCount}</dd>
            </dl>
        </div>

        <div id="bbsArticle_content">
            <table width="600" border="0">
                <tr>
                    <td style="padding: 20px 80px 20px 62px;"
                        valign="top" height="200">
                        ${dto.content }
                    </td>
                </tr>
            </table>
        </div>

    </div>

<%--    <div class="bbsArticle_noLine" style="text-align: right">--%>
<%--        From : ${dto.ipAddr }--%>
<%--    </div>--%>

    <div id="bbsArticle_footer">
        <div id="leftFooter">
            <button type="button" class="list_menu" onclick="javascript:location.href='<%=cp%>/sboard/update.do?num=${dto.num }&${params }'">수정</button>
            <button type="button" class="list_menu" onclick="javascript:location.href='<%=cp%>/sboard/deleted_ok.do?num=${dto.num }&${params }'">삭제</button>

        <%-- <input type="button" value=" 수정 " class="btn2"
                   onclick="javascript:location.href='<%=cp%>/sboard/update.do?num=${dto.num }&${params }'"/>
            <input type="button" value=" 삭제 " class="btn2"
                   onclick="javascript:location.href='<%=cp%>/sboard/deleted_ok.do?num=${dto.num }&${params }'"/>--%>
        </div>
        <div id="rightFooter">
            <button type="button" class="list_menu" onclick="javascript:location.href='<%=cp%>/sboard/list.do?${params }';">목록</button>
            <%--<input type="button" value=" 목록 " class="btn2"
                   onclick="javascript:location.href='<%=cp%>/sboard/list.do?${params }';"/>--%>
        </div>
    </div>

   <%-- <c:if test="${requestScope.commentList != null}">--%>
        <c:forEach var="comment" items="${comment }">
            <tr>
                <!-- 아이디, 작성날짜-->
                <td width="150">
                    <div>
                        ${comment.commentId}<br>
                        <font size="2" color="lightgray">${comment.commentDate}</font>
                    </div>
                </td>
                <%--본문내용--%>
                <td width="550">
                    <div class="text_wrapper">
                        ${comment.commentContent}
                    </div>
                </td>
                <%--버튼--%>
                <td width="100">
                    <div id="btn" style="text-align: center">
                        <a href="#">[답변]</a>
                        <%--댓글 작성만 수정, 삭제 가능하도록--%>
                    <c:if test="${comment.commentId == sessionScope.sesstionID}">
                        <a href="#">[수정]</a><br>
                        <a href="#">[삭제]</a>
                    </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
    <%--</c:if>--%>
    <%--로그인 했을 경우에 한해서 댓글을 작성 가능--%>
<%--
    <c:if test="${sessionScope.sesstionID != null}">
--%>
        <tr bgcolor="#F5F5F5">
            <form id="writeCommentForm">
                <input type="hidden" name="comment_board" value="${dto.num }">
                <input type="hidden" name="comment_id" value="${sessionScope.sesstionID}">
        <%--아이디--%>
                <td width="150">
                    <div>
                        ${sessionScope.sesstionID}
                    </div>
                 </td>
            <%--본문 작성--%>
                <td width="550">
                    <div style="display: flex;">
                         <textarea name="comment_content" rows="3" cols="70"></textarea>
                    <div id="btn1" >
                        <p style="margin: 0;">
                            <button type="button" class="comment" onclick="writeCmt()">댓글 등록</button>
        <%--                    <a href="#" onclick="writeCmt()" class="comment">댓글 등록</a>--%>
                        </p>
                    </div>
                    </div>
                </td>
            </form>
        </tr>
        <%--댓글 등록 버튼--%>
        <%--<td width="100">
            <div id="btn1" style="text-align: center">
                <p><a href="#" onclick="writeCmt()">댓글 등록</a></p>
            </div>
        </td>--%>
    <%--</c:if>--%>

</div>


</body>
</html>