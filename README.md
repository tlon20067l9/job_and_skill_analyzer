# DATA JOB & SKILL ANALYZER

## Giới thiệu

Viết JavaApplication để phân tích trực quan hóa bộ dữ liệu DATA_Job_and_Skill.  
Đây là ứng dụng Java desktop quản lý tuyển dụng và kỹ năng. Ứng dụng sử dụng SQL Server để lưu dữ liệu, Java Swing để thiết kế giao diện, JFreeChart để hiển thị biểu đồ thống kê.

## Tính năng

- Đăng nhập / Đăng xuất (Quay trở lại giao diện đăng nhập)
- Quản lý công việc và kỹ năng (CRUD Job và Skill gộp chung)
- Thống kê biểu đồ bằng JFreeChart:
  - Lương trung bình theo job_title
  - Tần suất xuất hiện của require_skill
- Kết nối CSDL SQL Server

## Công cụ sử dụng

| Công cụ/Công nghệ | Vai trò chính |
|-------------------|--------------|
| Java              | Ngôn ngữ lập trình chính để phát triển ứng dụng desktop |
| Java Swing        | Xây dựng giao diện đồ họa người dùng (UI) |
| SQL Server        | Lưu trữ và truy vấn dữ liệu |
| JDBC              | Giao tiếp giữa Java và cơ sở dữ liệu SQL Server |
| JFreeChart        | Trực quan hóa dữ liệu và hiển thị biểu đồ |
| SHA-256           | Mã hóa mật khẩu người dùng trước khi lưu trữ |
| Eclipse IDE       | Viết mã, biên dịch và chạy ứng dụng Java |

## Cài đặt

### Yêu cầu hệ thống

- JDK 8 trở lên
- Eclipse IDE hoặc IntelliJ IDEA
- SQL Server
- JDBC Driver cho SQL Server (`mssql-jdbc`)
- JFreeChart (để hiển thị biểu đồ)

### Cài đặt cơ sở dữ liệu

Chạy toàn bộ script SQL sau trong SQL Server:

```sql
CREATE DATABASE [Job_Skill];
GO

USE [Job_Skill];
GO

CREATE TABLE Company (
    company_id INT PRIMARY KEY,
    name NVARCHAR(255),
    location NVARCHAR(100)
);
GO

CREATE TABLE Skill (
    skill_id INT PRIMARY KEY,
    skill_name NVARCHAR(100)
);
GO

CREATE TABLE Job_Posting (
    job_id INT PRIMARY KEY,
    job_title NVARCHAR(255),
    company_id INT,
    skill_id NVARCHAR(255),
    location NVARCHAR(100),
    salary FLOAT,
    posting_date DATETIME,
    remote_work BIT,
    work_schedule NVARCHAR(50),
    no_degree_required BIT,
    health_insurance BIT,
    work_type NVARCHAR(50),
    company NVARCHAR(255),
    requireSkill NVARCHAR(255)
);
GO

CREATE TABLE Job_Skill (
    job_id INT,
    skill_id INT,
    PRIMARY KEY (job_id, skill_id)
);
GO

CREATE TABLE [User] (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    email NVARCHAR(100),
    password VARBINARY(64),
    role NVARCHAR(50),
    created_at DATETIME
);
GO
```

## Cấu trúc dự án

```
DO_AN/
├── src/
│   ├── Chuc_nang/                            # Kết nối cơ sở dữ liệu
│   │   └── DBConnection.java                 # Cấu hình kết nối đến CSDL (JDBC)
│   ├── Data/                                 # Quản lý truy vấn dữ liệu
│   │   ├── ChartDB.java                      # Xử lý dữ liệu cho biểu đồ JFreeChart
│   │   ├── CompanyDataManager.java           # Truy xuất dữ liệu công ty
│   │   ├── JobPostingDataManager.java        # Truy xuất & cập nhật dữ liệu việc làm
│   │   └── UserDATA.java                     # Lấy thông tin người dùng
│   ├── Giao_dien/                            # Giao diện người dùng (Swing)
│   │   ├── DashboardView.java                # Giao diện chính (Dashboard)
│   │   ├── JobManagementView.java            # Quản lý công việc và kỹ năng (CRUD)
│   │   ├── ChartReportView.java              # Hiển thị biểu đồ phân tích (JFreeChart)
│   │   └── LoginView.java                    # Giao diện đăng nhập
│   ├── Main/
│   │   └── Main.java                         # Điểm khởi động chương trình
│   ├── Nguoi_Dung/                           # Lớp mô hình (Model - OOP)
│   │   ├── User.java                         # Lớp đại diện người dùng
│   │   ├── JobPosting.java                   # Lớp đại diện bài đăng tuyển dụng
│   │   └── Skill.java                        # Lớp đại diện kỹ năng
│   ├── service/                              # Xử lý nghiệp vụ (Business logic)
│   │   ├── AuthService.java                  # Xác thực người dùng (login, session)
│   │   ├── JobPostingService.java            # Dịch vụ CRUD cho JobPosting và Skill
│   │   └── HashUtil.java                     # Mã hóa mật khẩu SHA-256
│   └── session/
│       └── UserSession.java                  # Lưu thông tin phiên đăng nhập
├── PIC/                                      # Hình ảnh giao diện
├── Thu_vien/                                 # Thư viện Java ngoài
│   ├── jcommon-1.0.23.jar
│   ├── jfreechart-1.0.19.jar
│   └── mssql-jdbc-12.10.0.jre11.jar
└── README.md                                 # Mô tả dự án
```

## Xác thực đăng nhập

- Dùng `email` + `password` (SHA-256 hash)
- Vai trò: `admin`, `viewer`
- Mã hóa password trước khi lưu và so sánh

## Chức năng chính

### Quản lý Job và Skill (gộp chung)

- CRUD công việc và kỹ năng
- Tìm kiếm theo tên công việc, lương, remote, location
- Gán kỹ năng cho công việc

### Biểu đồ JFreeChart

- Lương trung bình theo job_title, salary
- Tần suất xuất hiện của require_skill

## Hướng dẫn chạy ứng dụng

### Cấu hình kết nối Database

Mở file `DBConnection.java`, chỉnh sửa cấu hình:

```java
private static final String URL = "jdbc:sqlserver://LAPTOP-448VUAA7\SQLEXPRESS;databaseName=Job_Skill;encrypt=true;trustServerCertificate=true";
private static final String USER = "sa";
private static final String PASSWORD = "123456";
```

Điều chỉnh theo thông tin máy của bạn.

### Chạy project

- Mở `Main.java` trong package `Main`
- Nhấn Run để khởi động ứng dụng

## Bảo mật

- Password được lưu dưới dạng hash SHA-256 (kiểu VARBINARY)
- Tuyệt đối không lưu mật khẩu dạng plain text
- Mã hóa/giải mã trong Java dùng `MessageDigest`

##  Kết luận

Ứng dụng DATA JOB & SKILL ANALYZER là giải pháp trực quan, dễ sử dụng cho việc quản lý tuyển dụng và kỹ năng. Ứng dụng này phù hợp triển khai tại các doanh nghiệp vừa và nhỏ để tối ưu quy trình tuyển dụng và phân tích kỹ năng theo yêu cầu thị trường.

https://github.com/tlon20067l9/job_and_skill_analyzer
https://www.youtube.com/watch?v=YyS54lvaF_4
