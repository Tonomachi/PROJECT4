package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet; // Cần thiết nếu muốn đóng ResultSet trong try-with-resources (mặc dù không dùng ở đây)
import java.sql.Statement; // Cần thiết nếu muốn đóng Statement trong try-with-resources (mặc dù không dùng ở đây)

public class ConnectSQL {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/CuaHangThucPham";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    // Phương thức chỉ đóng Connection một cách an toàn
    public static void closeQuietly(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) { // Thêm kiểm tra isClosed()
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // KHÔNG cần các phương thức closeQuietly cho PreparedStatement/ResultSet ở đây
    // vì chúng ta sẽ dùng try-with-resources trong các DAO.
}