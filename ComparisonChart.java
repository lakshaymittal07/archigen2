import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

class ComparisonChart extends JPanel {
    private List<HistoryEntry> history;

    ComparisonChart(List<HistoryEntry> history) {
        this.history = history;
        setBackground(AppColors.SURFACE);
        setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
    }

    void setHistory(List<HistoryEntry> history) {
        this.history = history;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (history == null || history.isEmpty()) {
            g2.setColor(AppColors.MUTED);
            g2.drawString("No estimates yet", 20, getHeight() / 2);
            g2.dispose();
            return;
        }

        int margin = 20;
        int chartWidth = getWidth() - (margin * 2);
        int chartHeight = getHeight() - (margin * 2);
        double maxValue = history.stream().mapToDouble(HistoryEntry::getTotal).max().orElse(1.0);

        g2.setColor(AppColors.BORDER);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(margin, margin, chartWidth, chartHeight);

        int barCount = history.size();
        int gap = 12;
        int barWidth = Math.max(16, (chartWidth - gap * (barCount + 1)) / barCount);

        for (int i = 0; i < barCount; i++) {
            HistoryEntry entry = history.get(i);
            int barHeight = (int) ((entry.getTotal() / maxValue) * (chartHeight - 10));
            int x = margin + gap * (i + 1) + barWidth * i;
            int y = margin + chartHeight - barHeight;

            g2.setColor(i % 2 == 0 ? AppColors.BLUE : AppColors.GREEN);
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(AppColors.TEXT);
            g2.drawString(entry.getName(), x, y - 6);
        }

        g2.dispose();
    }
}