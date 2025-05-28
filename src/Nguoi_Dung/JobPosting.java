package Nguoi_Dung;

import java.util.Date;

public class JobPosting {
    private int jobId;
    private String jobTitle;
    private int companyId;
    private String company;
    private String skillId;
    private String requireSkill; 
    private String location;
    private Double salary;
    private Date postingDate;
    private boolean remoteWork;
    private String workSchedule;
    private boolean noDegreeRequired;
    private boolean healthInsurance;
    private String workType;



    public int getJobId() {
        return jobId;
    }
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getSkillId() {
        return skillId;
    }
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getRequireSkill() {
        return requireSkill;
    }
    public void setRequireSkill(String requireSkill) {
        this.requireSkill = requireSkill;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getPostingDate() {
        return postingDate;
    }
    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public boolean isRemoteWork() {
        return remoteWork;
    }
    public void setRemoteWork(boolean remoteWork) {
        this.remoteWork = remoteWork;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }
    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public boolean isNoDegreeRequired() {
        return noDegreeRequired;
    }
    public void setNoDegreeRequired(boolean noDegreeRequired) {
        this.noDegreeRequired = noDegreeRequired;
    }

    public boolean isHealthInsurance() {
        return healthInsurance;
    }
    public void setHealthInsurance(boolean healthInsurance) {
        this.healthInsurance = healthInsurance;
    }
    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }
}
