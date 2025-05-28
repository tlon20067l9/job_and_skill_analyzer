package Data;

import Nguoi_Dung.JobPosting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobPostingDataManager {
    private static final String URL = "jdbc:sqlserver://LAPTOP-448VUAA7\\SQLEXPRESS;databaseName=Job_Skill;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    private CompanyDataManager companyManager = new CompanyDataManager();

    public boolean jobIdExists(int jobId) {
        String sql = "SELECT COUNT(*) FROM Job_Posting WHERE job_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra job_id!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean addJob(JobPosting job) {
        if (jobIdExists(job.getJobId())) {
            System.out.println("⚠️ Job ID đã tồn tại! Không thể thêm.");
            return false;
        }

        if (!companyManager.companyExists(job.getCompanyId())) {
            boolean companyAdded = companyManager.addCompany(job.getCompanyId(), job.getCompany());
            if (!companyAdded) {
                System.out.println("❌ Thêm company thất bại, không thể thêm job.");
                return false;
            }
        }

        String sql = "INSERT INTO Job_Posting(job_id, job_title, company, location, salary, posting_date, remote_work, work_schedule, no_degree_required, health_insurance, skill_id, company_id, requireSkill, work_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, job.getJobId());
            ps.setString(2, job.getJobTitle());
            ps.setString(3, job.getCompany());
            ps.setString(4, job.getLocation());
            ps.setDouble(5, job.getSalary());

            if (job.getPostingDate() != null) {
                ps.setTimestamp(6, new Timestamp(job.getPostingDate().getTime()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }

            ps.setBoolean(7, job.isRemoteWork());
            ps.setString(8, job.getWorkSchedule());
            ps.setBoolean(9, job.isNoDegreeRequired());
            ps.setBoolean(10, job.isHealthInsurance());
            ps.setString(11, job.getSkillId());
            ps.setInt(12, job.getCompanyId());
            ps.setString(13, job.getRequireSkill());
            ps.setString(14, job.getWorkType());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm công việc!");
            e.printStackTrace();
            return false;
        }
    }

    public List<JobPosting> getAllJobs() {
        List<JobPosting> jobs = new ArrayList<>();
        String sql = "SELECT * FROM Job_Posting";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int jobId = rs.getInt("job_id");
                String jobTitle = rs.getString("job_title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                double salary = rs.getDouble("salary");

                Timestamp postingTimestamp = rs.getTimestamp("posting_date");
                java.util.Date postingDate = null;
                if (postingTimestamp != null) {
                    postingDate = new java.util.Date(postingTimestamp.getTime());
                }

                boolean remoteWork = rs.getBoolean("remote_work");
                String workSchedule = rs.getString("work_schedule");
                boolean noDegree = rs.getBoolean("no_degree_required");
                boolean healthInsurance = rs.getBoolean("health_insurance");
                String skillId = rs.getString("skill_id");
                int companyId = rs.getInt("company_id");
                String requireSkill = rs.getString("requireSkill");
                String workType = rs.getString("work_type");

                JobPosting job = new JobPosting();
                job.setJobId(jobId);
                job.setJobTitle(jobTitle);
                job.setCompany(company);
                job.setLocation(location);
                job.setSalary(salary);
                job.setPostingDate(postingDate);
                job.setRemoteWork(remoteWork);
                job.setWorkSchedule(workSchedule);
                job.setNoDegreeRequired(noDegree);
                job.setHealthInsurance(healthInsurance);
                job.setSkillId(skillId);
                job.setCompanyId(companyId);
                job.setRequireSkill(requireSkill);
                job.setWorkType(workType);

                jobs.add(job);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách công việc!");
            e.printStackTrace();
        }
        return jobs;
    }

    public boolean updateJob(JobPosting job) {
        String sql = "UPDATE Job_Posting SET job_title=?, company=?, location=?, salary=?, posting_date=?, remote_work=?, work_schedule=?, no_degree_required=?, health_insurance=?, skill_id=?, company_id=?, requireSkill=?, work_type=? WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, job.getJobTitle());
            ps.setString(2, job.getCompany());
            ps.setString(3, job.getLocation());
            ps.setDouble(4, job.getSalary());

            if (job.getPostingDate() != null) {
                ps.setTimestamp(5, new Timestamp(job.getPostingDate().getTime()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.setBoolean(6, job.isRemoteWork());
            ps.setString(7, job.getWorkSchedule());
            ps.setBoolean(8, job.isNoDegreeRequired());
            ps.setBoolean(9, job.isHealthInsurance());
            ps.setString(10, job.getSkillId());
            ps.setInt(11, job.getCompanyId());
            ps.setString(12, job.getRequireSkill());
            ps.setString(13, job.getWorkType());
            ps.setInt(14, job.getJobId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật công việc!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteJob(int jobId) {
        String sql = "DELETE FROM Job_Posting WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi xóa công việc!");
            e.printStackTrace();
            return false;
        }
    }
}
