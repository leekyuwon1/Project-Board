package lkw.com.obp.board.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null) {
                Class.forName("org.mariadb.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306",
                        "board",
                        "1234");
                System.out.println("데이터베이스 연결 O");
            }
        } catch (Exception e) {
            System.out.println("데이터베이스 연결 x");
        }
        return conn;
    }

    private DBConn() {
        super();
    }

//    public static void close() {
//        if (conn == null) {
//            return;
//        }
//        try {
//            if (!conn.isClosed()) {
//                conn.close();
//            }
//        } catch (Exception e) {
//            System.out.println("데이터베이스 종료");
//        }
//        conn = null;
//    }
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("데이터베이스 연결을 닫았습니다.");
            }
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결을 닫는 중 오류 발생: " + e.getMessage());
        }
    }

}
