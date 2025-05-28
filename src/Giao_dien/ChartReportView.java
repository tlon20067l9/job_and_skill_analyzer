package Giao_dien;

import Data.ChartDB;
import Nguoi_Dung.JobPosting;
import service.JobPostingService;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Date;
import java.util.Collections;
public class ChartReportView extends JFrame {

    private JComboBox<String> chartTypeCombo;
    private JComboBox<String> dataScopeCombo;
    private JPanel chartPanel;

    private JobPostingService service = new JobPostingService();

    public ChartReportView() {
        setTitle("\uD83D\uDCCA Job Chart Report");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(250, 250, 250));

        chartTypeCombo = new JComboBox<>(new String[]{
                "Bar Chart - Average Salary by Job Title/Salary",
                "Pie Chart - Skill Frequency by Required Skill",
                "Line Chart - Skill Trend Over Time"
        });

        dataScopeCombo = new JComboBox<>();

        JButton generateButton = new JButton("\uD83C\uDFA8 Generate Chart");
        generateButton.setBackground(new Color(100, 149, 237));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.addActionListener(e -> generateChart());

        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(new JLabel("Chart Type: "));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(chartTypeCombo);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Data Scope: "));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(dataScopeCombo);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(generateButton);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

        chartTypeCombo.addActionListener(e -> updateScopeOptions());
        updateScopeOptions();

        setVisible(true);
    }

    private void updateScopeOptions() {
        dataScopeCombo.removeAllItems();
        String selectedChart = (String) chartTypeCombo.getSelectedItem();

        if (selectedChart == null) return;

        if (selectedChart.startsWith("Bar Chart")) {
            dataScopeCombo.addItem("job_title");
            dataScopeCombo.addItem("salary");

        } else if (selectedChart.startsWith("Pie Chart")) {
            dataScopeCombo.addItem("required_skill");

        } else if (selectedChart.startsWith("Line Chart")) {
            List<JobPosting> jobs = service.getAllJobs();
            Set<String> skills = new TreeSet<>();
            for (JobPosting job : jobs) {
                if (job.getRequireSkill() != null) {
                    for (String s : job.getRequireSkill().split(",")) {
                        String skill = s.trim();
                        if (!skill.isEmpty()) {
                            skills.add(skill);
                        }
                    }
                }
            }
            for (String skill : skills) {
                dataScopeCombo.addItem(skill);
            }
        }
    }

    private void generateChart() {
        chartPanel.removeAll();
        String selectedChart = (String) chartTypeCombo.getSelectedItem();
        String scope = (String) dataScopeCombo.getSelectedItem();
        List<JobPosting> jobs = service.getAllJobs();

        if (selectedChart != null && scope != null) {
            if (selectedChart.startsWith("Bar Chart")) {
                Map<String, Double> data = ChartDB.getAverageSalaryByScope(jobs, scope);
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for (Map.Entry<String, Double> entry : data.entrySet()) {
                    dataset.addValue(entry.getValue(), "Average Salary", entry.getKey());
                }

                JFreeChart chart = ChartFactory.createBarChart(
                        "Average Salary by " + scope,
                        scope,
                        "Average Salary (USD)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        false, true, false
                );

                chart.setBackgroundPaint(Color.WHITE);
                chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));

                CategoryPlot plot = chart.getCategoryPlot();
                plot.setBackgroundPaint(new Color(245, 245, 245));
                plot.setDomainGridlinePaint(Color.GRAY);
                plot.setRangeGridlinePaint(Color.GRAY);

                BarRenderer renderer = (BarRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, new Color(100, 149, 237));
                renderer.setShadowVisible(false);

                plot.getDomainAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
                plot.getRangeAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));

                chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);

            } else if (selectedChart.startsWith("Pie Chart")) {
                Map<String, Integer> data = ChartDB.getSkillFrequencyByScope(jobs, scope);
                DefaultPieDataset dataset = new DefaultPieDataset();
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    dataset.setValue(entry.getKey(), entry.getValue());
                }

                JFreeChart chart = ChartFactory.createPieChart(
                        "Skill Frequency by " + scope,
                        dataset,
                        true, true, false
                );

                chart.setBackgroundPaint(Color.WHITE);
                chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
                plot.setBackgroundPaint(Color.WHITE);
                plot.setOutlineVisible(false);

                chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);

            } else if (selectedChart.startsWith("Line Chart")) {
                Map<String, Integer> trend = ChartDB.getSkillTrendOverTime(jobs, scope);
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for (Map.Entry<String, Integer> entry : trend.entrySet()) {
                    dataset.addValue(entry.getValue(), scope, entry.getKey());
                }

                JFreeChart chart = ChartFactory.createLineChart(
                        "Trend of '" + scope + "' over time",
                        "Period",
                        "Postings",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true, true, false
                );

                chart.setBackgroundPaint(Color.WHITE);
                chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
                chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
            }
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }
}
