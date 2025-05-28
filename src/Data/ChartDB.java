package Data;

import Nguoi_Dung.JobPosting;

import java.text.SimpleDateFormat;
import java.util.*;

public class ChartDB {

    // Bar Chart - Lương trung bình theo job title hoặc mức lương cụ thể
    public static Map<String, Double> getAverageSalaryByScope(List<JobPosting> jobs, String scope) {
        Map<String, List<Double>> map = new HashMap<>();

        switch (scope) {
            case "job_title":
                for (JobPosting job : jobs) {
                    if (job.getJobTitle() != null && job.getSalary() != null) {
                        map.computeIfAbsent(job.getJobTitle(), k -> new ArrayList<>()).add(job.getSalary());
                    }
                }
                break;

            case "salary":
                for (JobPosting job : jobs) {
                    if (job.getSalary() != null) {
                        String salaryKey = String.valueOf(job.getSalary());
                        map.computeIfAbsent(salaryKey, k -> new ArrayList<>()).add(job.getSalary());
                    }
                }
                break;

            default:
                return Collections.emptyMap();
        }

        Map<String, Double> avgMap = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            avgMap.put(entry.getKey(), avg);
        }

        return avgMap;
    }

    // Pie Chart - Thống kê tần suất kỹ năng
    public static Map<String, Integer> getSkillFrequencyByScope(List<JobPosting> jobs, String scope) {
        if (!"required_skill".equals(scope)) {
            return Collections.emptyMap();
        }

        Map<String, Integer> skillMap = new HashMap<>();
        for (JobPosting job : jobs) {
            if (job.getRequireSkill() != null) {
                String[] skills = job.getRequireSkill().split(",");
                for (String skill : skills) {
                    skill = skill.trim();
                    if (!skill.isEmpty()) {
                        skillMap.put(skill, skillMap.getOrDefault(skill, 0) + 1);
                    }
                }
            }
        }
        return skillMap;
    }

    // Line Chart - Xu hướng kỹ năng theo thời gian
    public static Map<String, Integer> getSkillTrendOverTime(List<JobPosting> jobs, String skill) {
        Map<String, Integer> trendMap = new TreeMap<>(); // TreeMap để giữ thứ tự thời gian

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); // Nhóm theo tháng/năm

        for (JobPosting job : jobs) {
            if (job.getRequireSkill() != null && job.getPostingDate() != null) {
                String[] skills = job.getRequireSkill().split(",");
                for (String s : skills) {
                    if (s.trim().equalsIgnoreCase(skill.trim())) {
                        String period = sdf.format(job.getPostingDate());
                        trendMap.put(period, trendMap.getOrDefault(period, 0) + 1);
                        break;
                    }
                }
            }
        }

        return trendMap;
    }
}
