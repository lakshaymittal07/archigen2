import java.text.NumberFormat;
import java.util.Locale;

public class CostEstimate {
    private final double materialCost;
    private final double labourCost;
    private final double landCost;

    public CostEstimate(double materialCost, double labourCost, double landCost) {
        this.materialCost = materialCost;
        this.labourCost = labourCost;
        this.landCost = landCost;
    }

    public double getMaterialCost() {
        return materialCost;
    }

    public double getLabourCost() {
        return labourCost;
    }

    public double getLandCost() {
        return landCost;
    }

    public double getTotalCost() {
        return materialCost + labourCost + landCost;
    }

    public String getCostCategory() {
        double total = getTotalCost();
        if (total < 500000) {
            return "LOW";
        } else if (total < 2000000) {
            return "MODERATE";
        }
        return "HIGH";
    }

    public static String format(double value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(value).replace("₹", "Rs ");
    }
}