package lkw.com.obp.board;

import lkw.com.obp.BoardServlet;
import lkw.com.obp.board.utils.DBConn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ReadComment {
    public void processComment(HttpServletRequest req, HttpServletResponse response) {
        Connection conn = DBConn.getConnection();
        BoardServlet bsv = new BoardServlet();

        String uri = req.getContextPath();
        String url;

        String pageNum = req.getParameter("pageNum");
        int currentPage = 1;

        if (pageNum != null) {
            currentPage = Integer.parseInt(pageNum);
        }


    }
}
