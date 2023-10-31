package lkw.com.obp.board.dao;

import lkw.com.obp.board.dto.BoardDTO;
import lkw.com.obp.board.dto.CommentDTO;
import lkw.com.obp.board.utils.DBConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lkw.com.obp.board.utils.DBConn.close;

public class CommentDAO {
    private Connection conn;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private static CommentDAO instance;

    public CommentDAO() {
    }

    public static CommentDAO getInstance() {
        if (instance == null) {
            instance = new CommentDAO();
        }
        return instance;
    }

    private int getLastAnonymousNumber(int commentBoardNum) {
        int anonNum = 0;
        String sql;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        System.out.println("commentBoard : " + commentBoardNum);

        try {
            sql = "SELECT IFNULL(max(`comment_num`), 0) FROM `online_board`.`board_comment`\n" +
                    "WHERE `comment_board`= ?";
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, commentBoardNum);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                anonNum = rs.getInt(1);
            }
            rs.next();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("getLastAnonymousNumber 실패");
        }
        return anonNum;
    }

    public boolean insertComment(BoardDTO dto, CommentDTO comment) {
        boolean result = false;
        String sql;
        try {
            // 이전 익명 사용자의 순번 조회
            int lastAnonymousNumber = getLastAnonymousNumber(comment.getCommentBoard());
            int newAnonymousNumber = lastAnonymousNumber + 1;

            sql = "INSERT INTO `online_board`.`board_comment` (`comment_board`, `comment_id`, `comment_date`, `comment_parent`, `comment_content`)\n" +
                    "VALUES(?,?,sysdate(),?,?)";

            conn = DBConn.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, comment.getCommentBoard());
            pstmt.setString(2, !Objects.equals(comment.getCommentId(), "") ? comment.getCommentId() : "익명" + newAnonymousNumber);
            pstmt.setInt(3, comment.getCommentParent());
            pstmt.setString(4, comment.getCommentContent());

            int flag = pstmt.executeUpdate();
            if (flag > 0) {
                result = true;
                conn.commit();
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        close();
        return result;
    }

    public int getCommentCount(int num) {
        int totalCount = 0;

        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        String sql;

        try {
            sql = "SELECT IFNULL(count(*), 0) FROM `online_board`.`board_comment`\n" +
                    "WHERE `comment_board` LIKE ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, num);

            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
            resultSet.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("댓글 카운트 실패");
        }
        return totalCount;
    }

    public List<CommentDTO> getCommentList(int num) {
        List<CommentDTO> comment = new ArrayList<CommentDTO>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT `comment_num`, `comment_board`, `comment_id`,\n" +
                    "`comment_date`, `comment_parent`,`comment_content`\n" +
                    "FROM `online_board`.`board_comment`\n" +
                    "WHERE comment_board = ? \n" +
                    "ORDER BY `comment_date` ASC \n";
            conn = DBConn.getConnection();

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, num);

            rs = pstmt.executeQuery();
            while(rs.next())
            {
                CommentDTO commentDTO = new CommentDTO();

                commentDTO.setCommentNum(rs.getInt("comment_num"));
                commentDTO.setCommentBoard(rs.getInt("comment_board"));
                commentDTO.setCommentId(rs.getString("comment_id"));
                commentDTO.setCommentDate(rs.getDate("comment_date"));
                commentDTO.setCommentParent(rs.getInt("comment_parent"));
                commentDTO.setCommentContent(rs.getString("comment_content"));

                comment.add(commentDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("댓글 목록 불러오기 실패");
        }
        finally {
            // 리소스 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                System.out.println("getCommentList 부분의 예외");
            }
        }
        return comment;
    }
}
