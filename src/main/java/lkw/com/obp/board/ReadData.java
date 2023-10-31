package lkw.com.obp.board;

import lkw.com.obp.BoardServlet;
import lkw.com.obp.board.dao.BoardDAO;
import lkw.com.obp.board.dto.BoardDTO;
import lkw.com.obp.board.utils.DBConn;
import lkw.com.obp.board.utils.MyUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

public class ReadData {
    public void processSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = DBConn.getConnection();
        BoardDAO dao = new BoardDAO(conn);
        MyUtil myUtil = new MyUtil(); //애는 페이징 처리 클래스이다.
        BoardServlet bsv = new BoardServlet();
        String cp = req.getContextPath(); // cp는 -> /study 이다
        String uri = req.getRequestURI(); // -> /study/sboard/created.do (uri는 전체 주소)
        String url;

        String pageNum = req.getParameter("pageNum");
        int currentPage = 1; // 현재 페이지

        if (pageNum != null) {
            currentPage = Integer.parseInt(pageNum);
        }

        String searchKey = req.getParameter("searchKey");
        String searchValue = req.getParameter("searchValue");

        if (searchValue == null) {
            searchKey = "subject";
            searchValue = "";
        } else {
            try {
                searchKey = URLDecoder.decode(searchKey, "UTF-8");
                searchValue = URLDecoder.decode(searchValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        int dataCount = dao.getDataCount(searchKey, searchValue);

        int numPerPage = 5;
        int totalPage = myUtil.getPageCount(numPerPage, dataCount);

        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * numPerPage;
        int end = start + numPerPage;

        System.out.println("searchKey: " + searchKey);
        System.out.println("searchValue: " + searchValue);
        System.out.println("dataCount: " + dataCount);
        System.out.println("currentPage: " + currentPage);
        System.out.println("numPerPage: " + numPerPage);
        System.out.println("start: " + start);
        System.out.println("end: " + end);

        // 데이터를 가져온다
        List<BoardDTO> lists = dao.getLists(start, end, searchKey, searchValue);
        System.out.println("Query Result: " + lists);

        int virtualNum = start + 1;
        for (BoardDTO dto : lists) {
            dto.setVirtualNum(virtualNum);
            virtualNum++;
        }

        // 페이징 처리 작업
        String param = "";
        if (searchValue != null && !searchValue.equals("")) {
            param = "searchKey=" + searchKey;
            try {
                param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String listUrl = cp + "/sboard/list.do";

        if (!param.equals("")) {
            listUrl += "?" + param;
        }

        String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

        String articleUrl = cp + "/sboard/article.do?pageNum=" + currentPage;

        if (!param.equals("")) {
            articleUrl += "&" + param;
        }

        // 포워딩 페이지에 데이터 넘기기
        req.setAttribute("lists", lists);
        req.setAttribute("articleUrl", articleUrl);
        req.setAttribute("pageIndexList", pageIndexList);
        req.setAttribute("dataCount", dataCount);
//        req.setAttribute("virtualNum", virtualNum);

        url = "/bbs/list.jsp";
        bsv.forward(req, resp, url);
    }

}
