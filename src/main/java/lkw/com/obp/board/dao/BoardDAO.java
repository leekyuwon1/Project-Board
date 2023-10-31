package lkw.com.obp.board.dao;

import lkw.com.obp.board.dto.BoardDTO;
import lkw.com.obp.board.utils.DBConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private Connection conn;

    public BoardDAO(Connection conn) {
        this.conn = conn;
    }

    public int getMaxNum() {
        int maxNum = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        try {
            sql = "SELECT IFNULL(max(num),0) FROM `online_board`.`board`";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                maxNum = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            System.out.println("번호 지정 실패");
        }
        return maxNum;
    }

    public int insertData(BoardDTO dto) {
        int result = 0;

        PreparedStatement pstmt = null;
        String sql;

        try{
            sql = "INSERT INTO `online_board`.`board`(`num`, `name`, `pwd`, `email`, `subject`, \n" +
                    "`content`, `ip_addr`, `hit_count`, `created`) \n" +
                    "VALUES(?,?,?,?,?,?,?,0,sysdate())";

            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, dto.getNum());
            if (dto.getName() == null) {
                throw new IllegalArgumentException("Name cannot be null.");
            }
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getPwd());
            pstmt.setString(4, dto.getEmail());
            pstmt.setString(5, dto.getSubject());
            pstmt.setString(6, dto.getContent());
            pstmt.setString(7, dto.getIpAddr());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            // 예외 처리: 예외 메시지를 출력하고 로그를 남긴다.
            System.out.println("데이터베이스 인서트 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.out.println("리소스 해제");
            }
        }

        return result;
    }

    public int getDataCount(String searchKey, String searchValue) {
        int totalCount = 0;

        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        String sql;

        try {
            searchValue = "%" + searchValue + "%";

            sql = "SELECT IFNULL(count(*), 0) FROM `online_board`.`board`\n" +
                    "WHERE " + searchKey + " LIKE ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, searchValue);

            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
            resultSet.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("데이터 카운트 실패");
        }
        return totalCount;
    }

    public List<BoardDTO> getLists(int start, int end, String searchKey, String searchValue) {
        List<BoardDTO> lists = new ArrayList<BoardDTO>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            //concat = 문자열 합치기
            sql = "SELECT `num`, `name`, `subject`, `hit_count`, DATE_FORMAT(`created`, '%Y-%m-%d') AS created \n" +
                    "FROM `online_board`.`board` \n" +
                    "WHERE " + searchKey + " LIKE CONCAT('%', ?, '%') \n" +
                    "ORDER BY `created` ASC \n" +
                    "LIMIT ?, ?";

            conn = DBConn.getConnection();
            if (!conn.isClosed()) {
                System.out.println("데이터 베이스 열렸습니다.");
            }
            pstmt = conn.prepareStatement(sql);

//            pstmt.setString(1, searchKey); // 변경된 부분
            pstmt.setString(1, searchValue.trim());
            pstmt.setInt(2, start); // 0
            pstmt.setInt(3, end); // 4
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BoardDTO dto = new BoardDTO();

                dto.setNum(rs.getInt("num"));
                dto.setName(rs.getString("name"));
                dto.setSubject(rs.getString("subject"));
                dto.setHitCount(rs.getInt("hit_count"));
                dto.setCreated(rs.getString("created"));

                lists.add(dto);
            }
            rs.close();
            pstmt.close();
        }catch (Exception e){
            System.out.println("getList 부분의 예외 : " + e.getMessage());
        }
        return lists;
    }

    public BoardDTO getReadData(int num) {
        BoardDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT num, name, pwd, email, subject, content,\n" +
                    "ip_addr, hit_count, created\n" +
                    "FROM `online_board`.`board` WHERE num=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, num);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new BoardDTO();

                dto.setNum(rs.getInt("num"));
                dto.setVirtualNum(num);
                dto.setName(rs.getString("name"));
                dto.setPwd(rs.getString("pwd"));
                dto.setEmail(rs.getString("email"));
                dto.setSubject(rs.getString("subject"));
                dto.setContent(rs.getString("content"));
                dto.setIpAddr(rs.getString("ip_addr"));
                dto.setHitCount(rs.getInt("hit_count"));
                dto.setCreated(rs.getString("created"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("getReadData 부분의 예외");
        }
        return dto;
    }

    public int updateHitCount(int num) {
        int result = 0;
        PreparedStatement pstmt = null;;
        String sql;

        try {
            sql = "update `online_board`.`board` set hit_count = hit_count + 1 WHERE num=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            result = pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            System.out.println("UpDate 부분의 예외1");
        }
        return result;
    }

    public int updateData(BoardDTO dto) {
        int result = 0;
        PreparedStatement pstmt = null;
        String sql;

        try {
            sql = "update `online_board`.`board` set name=?, pwd=?, \n" +
                    "email=?, subject=?, content=? WHERE num=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getPwd());
            pstmt.setString(3, dto.getEmail());
            pstmt.setString(4, dto.getSubject());
            pstmt.setString(5, dto.getContent());
            pstmt.setInt(6, dto.getNum());

            result = pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            System.out.println("2");
        }
        return result;
    }

    public int deleteData(int num) {
        int result = 0;

        PreparedStatement pstmt = null;
        String sql;

        try {
            sql = "DELETE FROM `online_board`.`board` WHERE num = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, num);

            result = pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            System.out.println("딜리트 예외");
        }
        return result;
    }
}
