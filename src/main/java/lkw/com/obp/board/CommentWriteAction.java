package lkw.com.obp.board;

import lkw.com.obp.board.dao.CommentDAO;
import lkw.com.obp.board.dto.BoardDTO;
import lkw.com.obp.board.dto.CommentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

public class CommentWriteAction {
    public void processComment(HttpServletRequest req,
                                 HttpServletResponse resp) throws ServletException, IOException {

        CommentDAO dao = CommentDAO.getInstance();
        CommentDTO comment = new CommentDTO();
        BoardDTO dto = new BoardDTO();

        // 파리미터 값을 가져온다.
        int boardNum = Integer.parseInt(req.getParameter("comment_board"));
        String comment_id = decodeParameter(req.getParameter("comment_id"));
        String comment_content = decodeParameter(req.getParameter("comment_content"));
        System.out.println("Num :" + boardNum);
        System.out.println("comment_content : " + comment_content);
        System.out.println("comment_id :" + comment_id );

//        comment.setCommentNum(dao.getSeq());    // 댓글 번호는 시퀀스값으로

        comment.setCommentBoard(boardNum);
        comment.setCommentId(comment_id);
        comment.setCommentContent(comment_content);

        boolean result = dao.insertComment(dto, comment);

        if(result){
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("1");
            out.close();
        }
    }
    private String decodeParameter(String paramValue) {
        if (paramValue != null) {
            try {
                return URLDecoder.decode(paramValue, "UTF-8");
            } catch (Exception e) {
                System.out.println("디코딩 에러");
            }
        }
        return null;
    }
}
