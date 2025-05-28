package Giao_dien;

import Nguoi_Dung.JobPosting;
import service.JobPostingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDataFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JobPostingService jobService = new JobPostingService();
    private final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm");

    private JTextField searchField;
    private JButton searchButton;
    private JButton exportTxtButton;
    private JComboBox<String> remoteWorkFilter; 
    private JComboBox<String> noDegreeFilter; 
    private JTextField workScheduleFilterField; 

    private List<JobPosting> allJobs; 

    public ViewDataFrame() {
        setTitle("Xem Dữ Liệu Công Việc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{
                "ID", "Tiêu đề", "Company ID", "Công ty", "Skill ID", "Require Skill",
                "Địa điểm", "Lương", "Ngày đăng", "Remote Work", "Lịch làm việc",
                "Không cần bằng cấp", "Bảo hiểm y tế"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        JPanel topPanel = new JPanel(new BorderLayout());


        JPanel searchFilterControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchFilterControlsPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm & Lọc công việc"));

        searchField = new JTextField(25); 
        searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(255, 152, 0));
        searchButton.setForeground(Color.WHITE);

        remoteWorkFilter = new JComboBox<>(new String[]{"Tất cả Remote", "TRUE", "FALSE"});
        noDegreeFilter = new JComboBox<>(new String[]{"Tất cả Bằng cấp", "TRUE", "FALSE"});
        workScheduleFilterField = new JTextField(10); 

        searchFilterControlsPanel.add(new JLabel("Từ khóa (tất cả trường):"));
        searchFilterControlsPanel.add(searchField);
        searchFilterControlsPanel.add(new JLabel("Remote Work:"));
        searchFilterControlsPanel.add(remoteWorkFilter);
        searchFilterControlsPanel.add(new JLabel("Không cần bằng cấp:"));
        searchFilterControlsPanel.add(noDegreeFilter);
        searchFilterControlsPanel.add(new JLabel("Lịch làm việc:"));
        searchFilterControlsPanel.add(workScheduleFilterField);
        searchFilterControlsPanel.add(searchButton);

        topPanel.add(searchFilterControlsPanel, BorderLayout.CENTER); 

        exportTxtButton = new JButton("Tải về TXT");
        exportTxtButton.setBackground(new Color(33, 150, 243));
        exportTxtButton.setForeground(Color.WHITE);

        JPanel exportButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        exportButtonPanel.add(exportTxtButton);

        topPanel.add(exportButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        allJobs = jobService.getAllJobs();
        displayData(allJobs);

        ActionListener filterActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearchAndFilter();
            }
        };

        searchButton.addActionListener(filterActionListener);
        searchField.addActionListener(filterActionListener); 
        remoteWorkFilter.addActionListener(filterActionListener);
        noDegreeFilter.addActionListener(filterActionListener);
        workScheduleFilterField.addActionListener(filterActionListener); 

        exportTxtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportDataToTxt();
            }
        });

        setVisible(true);
    }

    private void displayData(List<JobPosting> jobsToDisplay) {
        model.setRowCount(0);
        for (JobPosting job : jobsToDisplay) {
            String requireSkill = job.getRequireSkill() != null ? job.getRequireSkill() : "";

            model.addRow(new Object[]{
                    job.getJobId(),
                    job.getJobTitle(),
                    job.getCompanyId(),
                    job.getCompany(),
                    job.getSkillId(),
                    requireSkill,
                    job.getLocation(),
                    job.getSalary(),
                    job.getPostingDate() != null ? sdf.format(job.getPostingDate()) : "",
                    Boolean.toString(job.isRemoteWork()).toUpperCase(),
                    job.getWorkSchedule(),
                    Boolean.toString(job.isNoDegreeRequired()).toUpperCase(),
                    Boolean.toString(job.isHealthInsurance()).toUpperCase()
            });
        }
    }


    private void performSearchAndFilter() {
        String searchText = searchField.getText().trim().toLowerCase();
        String selectedRemoteWork = (String) remoteWorkFilter.getSelectedItem();
        String selectedNoDegree = (String) noDegreeFilter.getSelectedItem();
        String workScheduleText = workScheduleFilterField.getText().trim().toLowerCase();

        List<JobPosting> filteredJobs = allJobs.stream()
            .filter(job -> {
            
                boolean matchesSearchText = true;
                if (!searchText.isEmpty()) {
                    matchesSearchText = String.valueOf(job.getJobId()).toLowerCase().contains(searchText) ||
                                        job.getJobTitle().toLowerCase().contains(searchText) ||
                                        String.valueOf(job.getCompanyId()).toLowerCase().contains(searchText) ||
                                        job.getCompany().toLowerCase().contains(searchText) ||
                                        (job.getSkillId() != null && job.getSkillId().toLowerCase().contains(searchText)) ||
                                        (job.getRequireSkill() != null && job.getRequireSkill().toLowerCase().contains(searchText)) ||
                                        job.getLocation().toLowerCase().contains(searchText) ||
                                        String.valueOf(job.getSalary()).toLowerCase().contains(searchText) ||
                                        (job.getWorkSchedule() != null && job.getWorkSchedule().toLowerCase().contains(searchText));
                }
                return matchesSearchText;
            })
            .filter(job -> {
                boolean matchesRemote = true;
                if (!"Tất cả Remote".equals(selectedRemoteWork)) {
                    matchesRemote = (selectedRemoteWork.equals("TRUE") && job.isRemoteWork()) ||
                                    (selectedRemoteWork.equals("FALSE") && !job.isRemoteWork());
                }
                return matchesRemote;
            })
            .filter(job -> {
                boolean matchesNoDegree = true;
                if (!"Tất cả Bằng cấp".equals(selectedNoDegree)) {
                    matchesNoDegree = (selectedNoDegree.equals("TRUE") && job.isNoDegreeRequired()) ||
                                      (selectedNoDegree.equals("FALSE") && !job.isNoDegreeRequired());
                }
                return matchesNoDegree;
            })
            .filter(job -> {
                boolean matchesWorkSchedule = true;
                if (!workScheduleText.isEmpty()) {
                    matchesWorkSchedule = (job.getWorkSchedule() != null && job.getWorkSchedule().toLowerCase().contains(workScheduleText));
                }
                return matchesWorkSchedule;
            })
            .collect(Collectors.toList());

        displayData(filteredJobs);
    }


    private void exportDataToTxt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu dữ liệu công việc dưới dạng TXT");
        fileChooser.setSelectedFile(new File("job_data.txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) {
                        writer.write("\t");
                    }
                }
                writer.newLine();

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        writer.write(value != null ? value.toString() : "");
                        if (j < model.getColumnCount() - 1) {
                            writer.write("\t");
                        }
                    }
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu thành công tại:\n" + fileToSave.getAbsolutePath(), "Xuất TXT thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu TXT:\n" + ex.getMessage(), "Lỗi xuất TXT", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewDataFrame());
    }
}