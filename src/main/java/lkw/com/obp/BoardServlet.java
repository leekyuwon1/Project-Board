package lkw.com.obp;

import lkw.com.obp.board.CommentWriteAction;
import lkw.com.obp.board.dao.BoardDAO;
import lkw.com.obp.board.dao.CommentDAO;
import lkw.com.obp.board.dto.BoardDTO;
import lkw.com.obp.board.ReadData;
import lkw.com.obp.board.dto.CommentDTO;
import lkw.com.obp.board.utils.DBConn;
import lkw.com.obp.board.utils.MyUtil;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class BoardServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    //포워드 메서드 만들어서 호출해서 쓰기
    public void forward(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {

        //포워딩될 페이지 지정 - 주소대신 String url을 써주면 우리는 forward를 호출할때 url을 써주면 된다.
        RequestDispatcher rd =
                req.getRequestDispatcher(url);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Connection conn = DBConn.getConnection();
        BoardDAO boardDAO = new BoardDAO(conn);
        CommentDAO commentDAO = new CommentDAO();

        //애를 제외한 모든 객체는 공통으로 하는하는 객체이다
        MyUtil myUtil = new MyUtil(); //애는 페이징 처리 클래스이다.
        ReadData readData;

        String cp = req.getContextPath(); // cp는 -> /study 이다
        String uri = req.getRequestURI(); // -> /study/sboard/created.do (uri는 전체 주소)
        String url;


//        //uri에 created.do가 있으면 if문을 실행해라
        if(uri.indexOf("create.do")!=-1) {
            url = "/bbs/created.jsp";//이 주소를 직접 찾아간다.
            System.out.println("created.do 가 있다.");
            forward(req, resp, url);

        } else if(uri.contains("create_ok.do")) {
            System.out.println("created_ok.do 가 있다.");

            BoardDTO dto = new BoardDTO();

            int maxNum = boardDAO.getMaxNum();

            dto.setNum(maxNum + 1);
            //넘어오는건 post방식으로 넘어오니 getParameter로 받는다.
            // why? -> 서블릿에서는 action을 사용 못하니까
            dto.setSubject(req.getParameter("subject"));
            dto.setName(req.getParameter("name"));
            dto.setEmail(req.getParameter("email"));
            dto.setPwd(req.getParameter("pwd"));
            dto.setContent(req.getParameter("content"));
            dto.setIpAddr(req.getRemoteAddr());

            boardDAO.insertData(dto);

            url = cp + "/sboard/list.do";
            resp.sendRedirect(url);

            //만약에 created_ok.do가 있으면
        }else if(uri.contains("list.do")) {
            readData = new ReadData();
            readData.processSearch(req, resp);

        } else if (uri.contains("commentwriteaction.co")) {
            CommentWriteAction cwa = new CommentWriteAction();
            cwa.processComment(req, resp);

        } else if (uri.indexOf("article.do") != -1) {
            int num = Integer.parseInt(req.getParameter("num"));
            String pageNum = req.getParameter("pageNum");
            String searchKey = req.getParameter("searchKey");
            String searchValue = req.getParameter("searchValue");

            if(searchValue != null) {
                searchValue = URLDecoder.decode(searchValue, "UTF-8");
            }

            boardDAO.updateHitCount(num);

            BoardDTO dto = boardDAO.getReadData(num);
            List<CommentDTO> comment = commentDAO.getCommentList(num);

            if(dto==null) {
                url = cp + "/sboard/list.do";
                resp.sendRedirect(url);
            }
            int commentCount = commentDAO.getCommentCount(num);

            int lineSu = dto.getContent().split("\n").length;

            dto.setContent(dto.getContent().replace("\n", "<br/>"));

            String param = "pageNum=" + pageNum;
            if(searchValue != null && !searchValue.equals("")) {

                param += "&searchKey=" + searchKey;
                param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");

            }

            System.out.println("댓글 갯수 : " + commentCount);
            req.setAttribute("commentCount", commentCount);
            req.setAttribute("comment", comment);
            req.setAttribute("dto", dto);
            req.setAttribute("params", param);
            req.setAttribute("lineSu", lineSu);
            req.setAttribute("pageNum", pageNum);

            url = "/bbs/article.jsp";
            forward(req, resp, url);


        }else if(uri.indexOf("update.do")!=-1) {

            int num = Integer.parseInt(req.getParameter("num"));
            String pageNum = req.getParameter("pageNum");

            String searchKey = req.getParameter("searchKey");
            String searchValue = req.getParameter("searchValue");

            if(searchValue != null) {
                searchValue = URLDecoder.decode(searchValue, "UTF-8");
            }

            BoardDTO dto = boardDAO.getReadData(num);

            if(dto==null) {
                url = cp + "sboard/list.do";
                resp.sendRedirect(url);
            }

            String param = "pageNum=" + pageNum;

            if(searchValue != null&& !searchValue.equals("")) {
                param += "&searchKey=" + searchKey;
                param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
            }

            //이제 데이터 넘김
            req.setAttribute("dto", dto);
            req.setAttribute("pageNum", pageNum);
            req.setAttribute("params", param);
            req.setAttribute("searchKey", searchKey);
            req.setAttribute("searchValue", searchValue);

            url = "/bbs/update.jsp";
            forward(req, resp, url);

        }else if(uri.indexOf("update_ok.do")!=-1) {

            String pageNum = req.getParameter("pageNum");

            String searchKey = req.getParameter("searchKey");
            String searchValue = req.getParameter("searchValue");

            BoardDTO dto = new BoardDTO();

            dto.setNum(Integer.parseInt(req.getParameter("num")));
            dto.setSubject(req.getParameter("subject"));
            dto.setName(req.getParameter("name"));
            dto.setEmail(req.getParameter("email"));
            dto.setPwd(req.getParameter("pwd"));
            dto.setContent(req.getParameter("content"));

            //수정된 데이터를 보낸다
            boardDAO.updateData(dto);

            //되돌아올때
            String param = "pageNum=" + pageNum;

            if(searchValue != null&& !searchValue.equals("")) {
                param += "&searchKey=" + searchKey;
                param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
            }

            //업데이트 시켰으니 리다이렉트 해야함
            url = cp + "/sboard/list.do?" + param;
            resp.sendRedirect(url);

        }else if(uri.indexOf("deleted_ok.do")!=-1) {

            int num = Integer.parseInt(req.getParameter("num"));
            String pageNum = req.getParameter("pageNum");

            String searchKey = req.getParameter("searchKey");
            String searchValue = req.getParameter("searchValue");

            boardDAO.deleteData(num);

            String param = "pageNum=" + pageNum;

            if(searchValue != null&& !searchValue.equals("")) {
                param += "&searchKey=" + searchKey;
                param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
            }

            url = cp + "/sboard/list.do?" + param;
            resp.sendRedirect(url);
        }
    }
}
