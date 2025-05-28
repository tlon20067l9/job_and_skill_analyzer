package Data;

import Nguoi_Dung.User;
import Chuc_nang.DBConnection;
import java.sql.*;

public class UserDATA {
    private Connection conn;

    public UserDATA() {
        this.conn = DBConnection.getConnection();
    }

    public User getUserByEmailAndPassword(String email, String rawPassword) {
        User user = null;

        if (conn == null) {
            System.out.println("Không thể kết nối đến cơ sở dữ liệu.");
            return null;
        }

        String sql = "SELECT user_id, name, email, role FROM [User] WHERE email = ? AND password = HASHBYTES('SHA2_256', CONVERT(VARCHAR(100), ?))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, rawPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
