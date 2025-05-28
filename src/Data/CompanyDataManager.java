package Data;

import java.sql.*;

public class CompanyDataManager {
    private static final String URL = "jdbc:sqlserver://LAPTOP-448VUAA7\\SQLEXPRESS;databaseName=Job_Skill;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    public boolean companyExists(int companyId) {
        String sql = "SELECT COUNT(*) FROM Company WHERE company_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {  // Đóng ResultSet tự động
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra company tồn tại!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean addCompany(int companyId, String companyName) {
        String sql = "INSERT INTO Company(company_id, name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setString(2, companyName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm company!");
            e.printStackTrace();
        }
        return false;
    }
}
