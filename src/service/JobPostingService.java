package service;

import Data.JobPostingDataManager;
import Nguoi_Dung.JobPosting;
import java.util.List;

public class JobPostingService {
    private JobPostingDataManager dataManager = new JobPostingDataManager();

    public List<JobPosting> getAllJobs() {
        return dataManager.getAllJobs();
    }

    public boolean addJob(JobPosting job) {
        if (dataManager.jobIdExists(job.getJobId())) {
            System.out.println("⚠️ Job ID đã tồn tại! Vui lòng chọn ID khác.");
            return false;
        }
        return dataManager.addJob(job);
    }

    public boolean updateJob(JobPosting job) {
        return dataManager.updateJob(job);
    }

    public boolean deleteJob(int jobId) {
        return dataManager.deleteJob(jobId);
    }
}
