import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class CostPanel extends JPanel {
    private final JLabel lblStatus = new JLabel("Ready to estimate");
    private final JLabel lblBadge = new JLabel("IDLE");
    private final JLabel lblTotal = new JLabel("Rs 0");
    private final JLabel lblPerSqft = new JLabel("Rs 0 / sqft");
    private final JLabel lblMaterial = new JLabel("Rs 0");
    private final JLabel lblLabour = new JLabel("Rs 0");
    private final JLabel lblLand = new JLabel("Rs 0");
    private final JProgressBar progBar = new JProgressBar(0, 100);
    private final JTextField txtName = new JTextField("Residential Villa");
    private final JTextField txtArea = new JTextField("1200");
    private final JTextField txtFloors = new JTextField("2");
    private final JButton btnEstimate = new JButton("Estimate Cost");
    private final List<HistoryEntry> history = new ArrayList<>();
    private final ComparisonChart chart = new ComparisonChart(history);

    public CostPanel() {
        setBackground(AppColors.PANEL);
        setLayout(new BorderLayout(0, 0));
        buildUI();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(AppColors.PANEL);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        JLabel title = makeLabel("Archigen Cost Estimator", AppColors.TEXT, 18);
        title.setFont(title.getFont().deriveFont(18f));
        header.add(title, BorderLayout.NORTH);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topRow.setOpaque(false);
        topRow.add(makeBadge("READY", AppColors.BLUE));
        topRow.add(lblStatus);
        header.add(topRow, BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(makeCard("Project Details", buildInputPanel()));
        left.add(Box.createVerticalStrut(10));
        left.add(makeCard("Cost Breakdown", buildSummaryPanel()));

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setOpaque(false);
        right.add(makeCard("Estimate Summary", buildSummaryHeader()), BorderLayout.NORTH);
        right.add(makeCard("Recent History", chart), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setOpaque(false);
        body.add(left, BorderLayout.CENTER);
        body.add(right, BorderLayout.EAST);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        add(header, BorderLayout.NORTH);
        add(scrollWrapper, BorderLayout.CENTER);
        add(scroll, BorderLayout.CENTER);
        showWaiting();
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setOpaque(false);
        panel.add(makeLabel("Project name", AppColors.TEXT2, 12));
        panel.add(txtName);
        panel.add(makeLabel("Land area (sqft)", AppColors.TEXT2, 12));
        panel.add(txtArea);
        panel.add(makeLabel("Floors", AppColors.TEXT2, 12));
        panel.add(txtFloors);
        panel.add(new JLabel());
        btnEstimate.addActionListener(e -> calculateEstimate());
        panel.add(btnEstimate);
        return panel;
    }

    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(breakdownRow("Material", lblMaterial));
        panel.add(breakdownRow("Labour", lblLabour));
        panel.add(breakdownRow("Land", lblLand));
        panel.add(Box.createVerticalStrut(8));
        panel.add(makeLabel("Progress", AppColors.TEXT2, 12));
        progBar.setStringPainted(true);
        progBar.setForeground(AppColors.GREEN);
        panel.add(progBar);
        return panel;
    }

    private JPanel buildSummaryHeader() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(makeLabel("Estimated total", AppColors.TEXT2, 12));
        panel.add(lblTotal);
        panel.add(Box.createVerticalStrut(6));
        panel.add(makeLabel("Per sqft", AppColors.TEXT2, 12));
        panel.add(lblPerSqft);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return panel;
    }

    private void calculateEstimate() {
        showLoading();
        try {
            String name = txtName.getText().trim();
            double area = Double.parseDouble(txtArea.getText().trim());
            int floors = Integer.parseInt(txtFloors.getText().trim());

            if (name.isEmpty() || area <= 0 || floors <= 0) {
                throw new IllegalArgumentException("Please enter valid project details.");
            }

            Building building = new Building(name, area, floors);
            CostEstimate estimate = estimateFor(building);
            showResult(estimate);
        } catch (NumberFormatException ex) {
            showError("Area and floors must be numeric values.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private CostEstimate estimateFor(Building building) {
        double material = building.getLandArea() * 4200 * (1 + (building.getFloors() - 1) * 0.15);
        double labour = building.getLandArea() * 2400 * (1 + (building.getFloors() - 1) * 0.08);
        double land = building.getLandArea() * 1800;
        return new CostEstimate(material, labour, land);
    }

    public void showLoading() {
        lblStatus.setText("Calculating estimate...");
        lblBadge.setText("WORKING");
        lblBadge.setForeground(AppColors.AMBER);
        btnEstimate.setEnabled(false);
    }

    public void showResult(CostEstimate estimate) {
        lblStatus.setText("Estimate ready for " + txtName.getText().trim());
        lblBadge.setText(estimate.getCostCategory());
        lblBadge.setForeground(getStatusColor(estimate.getCostCategory()));
        lblTotal.setText(CostEstimate.format(estimate.getTotalCost()));
        lblPerSqft.setText(CostEstimate.format(estimate.getTotalCost() / Math.max(Double.parseDouble(txtArea.getText().trim()), 1)) + " / sqft");
        lblMaterial.setText(CostEstimate.format(estimate.getMaterialCost()));
        lblLabour.setText(CostEstimate.format(estimate.getLabourCost()));
        lblLand.setText(CostEstimate.format(estimate.getLandCost()));
        progBar.setValue(Math.min(100, (int) (estimate.getTotalCost() / 25000)));
        history.add(0, new HistoryEntry(txtName.getText().trim(), estimate.getTotalCost(), estimate.getCostCategory()));
        if (history.size() > 6) {
            history.remove(history.size() - 1);
        }
        chart.setHistory(history);
        btnEstimate.setEnabled(true);
    }

    public void showError(String message) {
        lblStatus.setText(message);
        lblBadge.setText("ERROR");
        lblBadge.setForeground(AppColors.RED);
        btnEstimate.setEnabled(true);
    }

    public void showWaiting() {
        lblStatus.setText("Enter your building details to begin.");
        lblBadge.setText("READY");
        lblBadge.setForeground(AppColors.BLUE);
        btnEstimate.setEnabled(true);
    }

    private JPanel makeCard(String title, JComponent content) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(AppColors.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(260, 220));
        card.add(makeLabel(title, AppColors.TEXT, 13), BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel breakdownRow(String label, JLabel value) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.add(makeLabel(label, AppColors.TEXT2, 12), BorderLayout.WEST);
        value.setForeground(AppColors.TEXT);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private JLabel makeLabel(String text, Color color, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, size));
        return label;
    }

    private JLabel makeBadge(String text, Color color) {
        JLabel badge = new JLabel(text);
        badge.setForeground(color);
        badge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        return badge;
    }

    private Color getStatusColor(String category) {
        if ("LOW".equalsIgnoreCase(category)) {
            return AppColors.GREEN;
        }
        if ("MODERATE".equalsIgnoreCase(category)) {
            return AppColors.AMBER;
        }
        return AppColors.RED;
    }
}