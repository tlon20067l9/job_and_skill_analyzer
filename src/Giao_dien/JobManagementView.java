package Giao_dien;

import Nguoi_Dung.JobPosting;
import service.JobPostingService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobManagementView extends JFrame {
    private JobPostingService jobService;

    private JTable tblJobs;
    private DefaultTableModel tableModel;

    private JTextField txtJobId;
    private JTextField txtJobTitle;
    private JTextField txtCompanyId;
    private JTextField txtCompany;
    private JTextField txtSkillId;
    private JTextField txtRequireSkill;
    private JCheckBox[] chkWorkTypes;
    private final String[] workTypeOptions = {
        "Programming", "Databases", "Cloud", "Libraries",
        "Webframeworks", "Os", "Analyst_tools", "Other"
    };
    private JTextField txtLocation;
    private JTextField txtSalary;
    private JTextField txtPostingDate;
    private JComboBox<String> comboRemoteWork;
    private JTextField txtWorkSchedule;
    private JComboBox<String> comboNoDegree;
    private JComboBox<String> comboHealthInsurance;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm");

    public JobManagementView() {
        jobService = new JobPostingService();
        initComponents();
        loadJobData();
    }

    private void initComponents() {
        setTitle("Quản lý Công việc");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {
            "ID", "Job Title", "Company ID", "Company", "Skill ID", "Require Skill", "Work Type",
            "Location", "Salary", "Posting Date", "Remote Work", "Work Schedule",
            "No Degree", "Health Insurance"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblJobs = new JTable(tableModel);
        tblJobs.setFont(new Font("Arial", Font.PLAIN, 14));
        tblJobs.setRowHeight(20); 
        JScrollPane scrollPane = new JScrollPane(tblJobs);

        JPanel formPanel = new JPanel(new GridBagLayout());

        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin công việc",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), new Color(0, 70, 190))); 

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; 

      
        int textFieldColumns = 25; 

        Font inputFont = new Font("Arial", Font.PLAIN, 14); 
        Font labelFont = new Font("Arial", Font.BOLD, 14); 

        txtJobId = new JTextField(textFieldColumns);
        txtJobId.setFont(inputFont);
        txtJobTitle = new JTextField(textFieldColumns);
        txtJobTitle.setFont(inputFont);
        txtCompanyId = new JTextField(textFieldColumns);
        txtCompanyId.setFont(inputFont);
        txtCompany = new JTextField(textFieldColumns);
        txtCompany.setFont(inputFont);
        txtSkillId = new JTextField(textFieldColumns);
        txtSkillId.setFont(inputFont);
        txtRequireSkill = new JTextField(textFieldColumns);
        txtRequireSkill.setFont(inputFont);

      
        JPanel workTypePanel = new JPanel(new GridLayout(4, 2, 3, 2)); 
        chkWorkTypes = new JCheckBox[workTypeOptions.length];
        for (int i = 0; i < workTypeOptions.length; i++) {
            chkWorkTypes[i] = new JCheckBox(workTypeOptions[i]);
            chkWorkTypes[i].setFont(inputFont); 
            workTypePanel.add(chkWorkTypes[i]);
        }

        txtLocation = new JTextField(textFieldColumns);
        txtLocation.setFont(inputFont);
        txtSalary = new JTextField(textFieldColumns);
        txtSalary.setFont(inputFont);
        txtPostingDate = new JTextField(sdf.format(new Date()), textFieldColumns);
        txtPostingDate.setFont(inputFont);
        
        comboRemoteWork = new JComboBox<>(new String[] {"TRUE", "FALSE"});
        comboRemoteWork.setFont(inputFont); 
        txtWorkSchedule = new JTextField(textFieldColumns);
        txtWorkSchedule.setFont(inputFont);
        comboNoDegree = new JComboBox<>(new String[] {"TRUE", "FALSE"});
        comboNoDegree.setFont(inputFont); 
        comboHealthInsurance = new JComboBox<>(new String[] {"TRUE", "FALSE"});
        comboHealthInsurance.setFont(inputFont); 

        int row = 0;
        addToFormPanel(formPanel, gbc, row++, "Job ID:", txtJobId, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Job Title:", txtJobTitle, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Company ID:", txtCompanyId, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Company:", txtCompany, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Skill ID:", txtSkillId, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Require Skill:", txtRequireSkill, labelFont);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel workTypeLabel = new JLabel("Work Type:");
        workTypeLabel.setFont(labelFont);
        formPanel.add(workTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0; 
        formPanel.add(workTypePanel, gbc);

        addToFormPanel(formPanel, gbc, row++, "Location:", txtLocation, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Salary:", txtSalary, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Posting Date:", txtPostingDate, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Remote Work:", comboRemoteWork, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Work Schedule:", txtWorkSchedule, labelFont);
        addToFormPanel(formPanel, gbc, row++, "No Degree:", comboNoDegree, labelFont);
        addToFormPanel(formPanel, gbc, row++, "Health Insurance:", comboHealthInsurance, labelFont);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Dimension buttonDim = new Dimension(100, 35);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        btnAdd = new JButton("Thêm"); btnAdd.setBackground(new Color(76, 175, 80)); btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(buttonDim); btnAdd.setFont(buttonFont);
        
        btnUpdate = new JButton("Sửa"); btnUpdate.setBackground(new Color(33, 150, 243)); btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setPreferredSize(buttonDim); btnUpdate.setFont(buttonFont);
        
        btnDelete = new JButton("Xóa"); btnDelete.setBackground(new Color(244, 67, 54)); btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(buttonDim); btnDelete.setFont(buttonFont);
        
        btnClear = new JButton("Làm mới"); btnClear.setBackground(Color.GRAY); btnClear.setForeground(Color.WHITE);
        btnClear.setPreferredSize(buttonDim); btnClear.setFont(buttonFont);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(10, 10));
        cp.add(scrollPane, BorderLayout.CENTER); 
        cp.add(formPanel, BorderLayout.EAST);   
        cp.add(buttonPanel, BorderLayout.SOUTH);

        tblJobs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loadSelectedJobToForm();
            }
        });

        btnAdd.addActionListener(e -> addJob());
        btnUpdate.addActionListener(e -> updateJob());
        btnDelete.addActionListener(e -> deleteJob());
        btnClear.addActionListener(e -> clearForm());
    }

    private void addToFormPanel(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp, Font labelFont) {
        double originalWeightX = gbc.weightx;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont); 
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);

        gbc.weightx = originalWeightX;
    }

    private void loadJobData() {
        tableModel.setRowCount(0);
        for (JobPosting job : jobService.getAllJobs()) {
            tableModel.addRow(new Object[]{
                job.getJobId(), job.getJobTitle(), job.getCompanyId(), job.getCompany(),
                job.getSkillId(), job.getRequireSkill(), job.getWorkType(),
                job.getLocation(), job.getSalary(),
                job.getPostingDate() != null ? sdf.format(job.getPostingDate()) : "",
                Boolean.toString(job.isRemoteWork()).toUpperCase(), 
                job.getWorkSchedule(),
                Boolean.toString(job.isNoDegreeRequired()).toUpperCase(), 
                Boolean.toString(job.isHealthInsurance()).toUpperCase()
            });
        }
    }

    private void loadSelectedJobToForm() {
        int row = tblJobs.getSelectedRow();
        if (row >= 0) {
            txtJobId.setText(tableModel.getValueAt(row, 0).toString());
            txtJobId.setEditable(false);
            txtJobTitle.setText(tableModel.getValueAt(row, 1).toString());
            txtCompanyId.setText(tableModel.getValueAt(row, 2).toString());
            txtCompany.setText(tableModel.getValueAt(row, 3).toString());
            txtSkillId.setText(tableModel.getValueAt(row, 4).toString());
            txtRequireSkill.setText(tableModel.getValueAt(row, 5).toString());

            for (JCheckBox chk : chkWorkTypes) chk.setSelected(false);
            String workTypeString = tableModel.getValueAt(row, 6).toString();
            if (!workTypeString.isEmpty()) {
                String[] workTypes = workTypeString.split(",");
                for (String wt : workTypes) {
                    for (JCheckBox chk : chkWorkTypes) {
                        if (chk.getText().equalsIgnoreCase(wt.trim())) {
                            chk.setSelected(true);
                        }
                    }
                }
            }

            txtLocation.setText(tableModel.getValueAt(row, 7).toString());
            txtSalary.setText(tableModel.getValueAt(row, 8).toString());
            txtPostingDate.setText(tableModel.getValueAt(row, 9).toString());
            comboRemoteWork.setSelectedItem(tableModel.getValueAt(row, 10).toString().toUpperCase());
            txtWorkSchedule.setText(tableModel.getValueAt(row, 11).toString());
            comboNoDegree.setSelectedItem(tableModel.getValueAt(row, 12).toString().toUpperCase());
            comboHealthInsurance.setSelectedItem(tableModel.getValueAt(row, 13).toString().toUpperCase());
        }
    }

    private void clearForm() {
        txtJobId.setText(""); txtJobId.setEditable(true);
        txtJobTitle.setText(""); txtCompanyId.setText("");
        txtCompany.setText(""); txtSkillId.setText("");
        txtRequireSkill.setText(""); txtLocation.setText("");
        txtSalary.setText(""); txtPostingDate.setText(sdf.format(new Date()));
        comboRemoteWork.setSelectedItem("FALSE"); 
        txtWorkSchedule.setText("");
        comboNoDegree.setSelectedItem("FALSE");
        comboHealthInsurance.setSelectedItem("FALSE");
        for (JCheckBox chk : chkWorkTypes) chk.setSelected(false);
        tblJobs.clearSelection();
    }

    private void addJob() {
        try {
            JobPosting job = getJobFromForm();
            if (jobService.addJob(job)) {
                JOptionPane.showMessageDialog(this, "Thêm công việc thành công!");
                loadJobData(); clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Job ID đã tồn tại!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số. Vui lòng kiểm tra Job ID, Company ID và Salary.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateJob() {
        int row = tblJobs.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc cần sửa!"); return;
        }
        try {
            JobPosting job = getJobFromForm();
            if (jobService.updateJob(job)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadJobData(); clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy công việc để cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số. Vui lòng kiểm tra Job ID, Company ID và Salary.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteJob() {
        int row = tblJobs.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc cần xóa!"); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int jobId = Integer.parseInt(txtJobId.getText().trim());
                if (jobService.deleteJob(jobId)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadJobData(); clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Không tìm thấy công việc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Job ID không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            // End of try-catch block for deleteJob
        }
    }

    private JobPosting getJobFromForm() throws Exception {
        JobPosting job = new JobPosting();
        String jobIdText = txtJobId.getText().trim();
        if (!jobIdText.isEmpty()) {
            job.setJobId(Integer.parseInt(jobIdText));
        } else {
            throw new IllegalArgumentException("Job ID không được để trống.");
        }

        job.setJobTitle(txtJobTitle.getText().trim());
        job.setCompanyId(Integer.parseInt(txtCompanyId.getText().trim()));
        job.setCompany(txtCompany.getText().trim());
        job.setSkillId(txtSkillId.getText().trim());
        job.setRequireSkill(txtRequireSkill.getText().trim());

        List<String> selected = new ArrayList<>();
        for (JCheckBox chk : chkWorkTypes) if (chk.isSelected()) selected.add(chk.getText());
        job.setWorkType(String.join(",", selected));

        job.setLocation(txtLocation.getText().trim());
        job.setSalary(Double.parseDouble(txtSalary.getText().trim()));
        job.setPostingDate(sdf.parse(txtPostingDate.getText().trim()));
        job.setRemoteWork(comboRemoteWork.getSelectedItem().equals("TRUE"));
        job.setWorkSchedule(txtWorkSchedule.getText().trim());
        job.setNoDegreeRequired(comboNoDegree.getSelectedItem().equals("TRUE"));
        job.setHealthInsurance(comboHealthInsurance.getSelectedItem().equals("TRUE"));

        return job;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobManagementView().setVisible(true));
    }
}