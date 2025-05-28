package Giao_dien;

import javax.swing.*;
import java.awt.*;
import Nguoi_Dung.User;
import service.AuthService;
import session.UserSession;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginView() {
        setTitle("Job Skill Analyzer - Đăng nhập");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(true); 

        Color buttonBlue = new Color(0, 123, 255);

        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            Image bgImage = new ImageIcon("PIC/tải-xuống.png").getImage(); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        add(backgroundPanel);

        JPanel cardPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 30;
                int shadowOffset = 5;
                g2.setColor(new Color(0, 0, 0, 60)); 
                g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset * 2, getHeight() - shadowOffset * 2, arc, arc);
                g2.setColor(new Color(255, 255, 255, 230)); 
                g2.fillRoundRect(0, 0, getWidth() - shadowOffset * 2, getHeight() - shadowOffset * 2, arc, arc);
                super.paintComponent(g);
            }
        };
        cardPanel.setOpaque(false); 
        cardPanel.setPreferredSize(new Dimension(500, 480)); 
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 6, 18); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.BOTH; 
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        ImageIcon logoIcon = new ImageIcon("C:/Users/PC/Documents/CODE/DO_AN/PIC/ChatGPT_Image_17_44_09_26_thg_5__2025-removebg-preview.png"); 
        
        Image scaledImage = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); 
        iconLabel.setIcon(new ImageIcon(scaledImage));
        cardPanel.add(iconLabel, gbc);

        gbc.gridy++;
        JLabel titleLabel = new JLabel("Job Skill Analyzer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        titleLabel.setForeground(new Color(30, 144, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1; 
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; 
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        cardPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 
        gbc.insets = new Insets(12, 8, 6, 18); 
        emailField = new JTextField() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 45;  
                return d;
            }
        };
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        cardPanel.add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(12, 18, 6, 18); 
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        cardPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(12, 8, 6, 18); 
        passwordField = new JPasswordField() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 45;  
                return d;
            }
        };
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        cardPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(35, 10, 10, 10); 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER; 
        JButton loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setBackground(buttonBlue);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(220, 60)); 
        loginButton.addActionListener(e -> handleLogin());
        cardPanel.add(loginButton, gbc);

        backgroundPanel.add(cardPanel);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ email và mật khẩu!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthService authService = new AuthService();
        User user = authService.login(email, password);

        if (user != null) {
            UserSession.setCurrentUser(user);
            new DashboardView().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai email hoặc mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
