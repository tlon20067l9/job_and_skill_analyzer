package Giao_dien;

import session.UserSession;
import Nguoi_Dung.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class DashboardView extends JFrame {
    private JPanel mainPanel;
    private JPanel centerPanel;

    public DashboardView() {
        User user = UserSession.getCurrentUser();

        setTitle("JobSkill Dashboard - " + user.getRole().toUpperCase());
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // ==== TOP BAR ====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 245, 245));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel roleLabel = new JLabel("Vai trò: " + user.getRole().toUpperCase());
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        roleLabel.setForeground(new Color(33, 33, 33));
        topBar.add(roleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        logoutButton.setBackground(new Color(255, 77, 77));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setToolTipText("Nhấn để đăng xuất khỏi hệ thống");
        logoutButton.setIcon(loadAndResizeIcon("PIC/2849796_lock_security_multimedia_close_protection_icon.png", 20, 20));

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                UserSession.clearSession();
                this.dispose();
                LoginView login = new LoginView();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            }
        });

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRight.setBackground(new Color(245, 245, 245));
        topRight.add(logoutButton);
        topBar.add(topRight, BorderLayout.EAST);

        // ==== CENTER ====
        centerPanel = new JPanel(new GridLayout(1, 3, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        centerPanel.setBackground(new Color(245, 245, 245));

        ImageIcon manageIcon = loadAndResizeIcon("PIC/2620529_checklist_employee_job_seeker_unemployee_icon.png", 120, 120);
        ImageIcon viewDataIcon = loadAndResizeIcon("PIC/document.png", 120, 120);
        ImageIcon reportIcon = loadAndResizeIcon("PIC/4288598_analysis_analytics_chart_data_pie_icon.png", 120, 120);

        if (user.getRole().equalsIgnoreCase("admin")) {
            JPanel manageCard = createFeatureCard(manageIcon, "Quản lý Công việc", "Thêm, sửa, xóa công việc",
                    new Color(70, 130, 180), new Color(225, 240, 255));
            manageCard.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JobManagementView jmView = new JobManagementView();
                    jmView.setLocationRelativeTo(null);
                    jmView.setVisible(true);
                }
            });
            centerPanel.add(manageCard);
        }

        JPanel viewCard = createFeatureCard(viewDataIcon, "Xem Dữ liệu", "Xem danh sách tất cả công việc",
                new Color(255, 140, 0), new Color(255, 235, 205));
        viewCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ViewDataFrame viewFrame = new ViewDataFrame();
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setVisible(true);
            }
        });
        centerPanel.add(viewCard);

        JPanel reportCard = createFeatureCard(reportIcon, "Xem Báo cáo", "Trực quan hóa dữ liệu công việc",
                new Color(148, 0, 211), new Color(240, 230, 250));
        reportCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ChartReportView reportView = new ChartReportView();
                reportView.setLocationRelativeTo(null);
                reportView.setVisible(true);
            }
        });
        centerPanel.add(reportCard);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createFeatureCard(ImageIcon icon, String title, String description, Color borderColor, Color hoverColor) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel iconLabel = new JLabel(icon);
        gbc.gridy = 0;
        card.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridy = 1;
        card.add(titleLabel, gbc);

        JLabel descLabel = new JLabel("<html><div style='text-align:center; width:220px;'>" + description + "</div></html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = 2;
        card.add(descLabel, gbc);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverColor);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor.darker(), 3, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                card.setToolTipText("Nhấp để truy cập " + title.toLowerCase());
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 3, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }

            public void mousePressed(MouseEvent e) {
                card.setBackground(borderColor.darker());
            }

            public void mouseReleased(MouseEvent e) {
                card.setBackground(hoverColor);
            }
        });

        return card;
    }

    private ImageIcon loadAndResizeIcon(String path, int width, int height) {
        File f = new File(path);
        if (!f.exists()) {
            return (ImageIcon) UIManager.getIcon("OptionPane.warningIcon");
        }
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}
