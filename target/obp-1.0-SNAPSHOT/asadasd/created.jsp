<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String cp = request.getContextPath();

    String pageNum = request.getParameter("pageNum");

    String searchKey = request.getParameter("searchKey");
    String searchValue = request.getParameter("searchValue");

    if(searchValue != null) {

        if(request.getMethod().equalsIgnoreCase("GET")) {
            searchValue = URLDecoder.decode(searchValue, "UTF-8");
        }

    }else {
        searchKey = "subject";
        searchValue = "";
    }

    String param = "";
    //null이 아니면 검색을 한 것이다.
    if(!searchValue.equals("")) {

        //이때 주소를 만들어준다
        param = "&searchKey=" + searchKey;
        param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");

    }


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>게 시 판</title>

    <link rel="stylesheet" type="text/css" href="<%=cp%>/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="<%=cp%>/css/created.css"/>

    <script type="text/javascript" src="<%=cp%>/utils/util.js"></script>
    <script type="text/javascript">
        function sendIt() {

            var f = document.myForm;
            var subject = f.subject.value.trim();
            var name = f.name.value.trim();
            var email = f.email.value.trim();
            var content = f.content.value.trim();
            var pwd = f.pwd.value.trim();

            if (!subject) {
                alert("\n제목을 입력하세요.");
                f.subject.focus();
                return;
            }

            if (!name) {
                alert("\n이름을 입력하세요.");
                f.name.focus();
                return;
            }

            if(!isValidEmail(email)){
                alert("\n정상적인 E-Mail을 입력하세요.");
                f.email.focus();
                return;
            }

            if(!content){
                alert("\n내용을 입력하세요.");
                f.content.focus();
                return;
            }
            if(!pwd){
                alert("\n패스워드를 입력하세요.");
                f.pwd.focus();
                return;
            }
            // 이하 생략: 이메일, 내용, 패스워드에 대한 유효성 검사 추가
            f.action = "<%=cp%>/created_ok.jsp";
            f.submit();
        }
    </script>


</head>
<body>

<div id="bbs">
    <div id="bbs_title">
        게 시 판
    </div>

    <form action="" method="post" name="myForm">
        <div id="bbsCreated">

            <div class="bbsCreated_bottomLine">
                <dl>
                    <dt>제&nbsp;&nbsp;&nbsp;&nbsp;목</dt>
                    <dd>
                        <input type="text" name="subject" size="60"
                               maxlength="100" class="boxTF"/>
                    </dd>
                </dl>
            </div>

            <div class="bbsCreated_bottomLine">
                <dl>
                    <dt>작성자</dt>
                    <dd>
                        <input type="text" name="name" size="35"
                               maxlength="20" class="boxTF" />
                    </dd>
                </dl>
            </div>

            <div class="bbsCreated_bottomLine">
                <dl>
                    <dt>E-Mail</dt>
                    <dd>
                        <input type="text" name="email" size="35"
                               maxlength="50" class="boxTF"/>
                    </dd>
                </dl>
            </div>

            <div id="bbsCreated_content">
                <dl>
                    <dt>내&nbsp;&nbsp;&nbsp;&nbsp;용</dt>
                    <dd>
				<textarea rows="12" cols="63" name="content"
                          class="boxTA"></textarea>
                    </dd>
                </dl>
            </div>

            <div class="bbsCreated_noLine">
                <dl>
                    <dt>패스워드</dt>
                    <dd>
                        <input type="password" name="pwd" size="35"
                               maxlength="7" class="boxTF"/>
                        &nbsp;(게시물 수정 및 삭제시 필요!!)
                    </dd>
                </dl>
            </div>

        </div>

        <div id="bbsCreated_footer">
            <input type="button" value=" 등록하기 " class="btn2" onclick="sendIt();"/>
            <input type="reset" value=" 다시입력 " class="btn2"
                   onclick="document.myForm.subject.focus();"/>
            <input type="button" value=" 작성취소 " class="btn2"
                   onclick="location.href='list.jsp';"/>
        </div>

    </form>

</div>


</body>
</html>