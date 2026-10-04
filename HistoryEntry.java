class HistoryEntry {
    private final String name;
    private final double total;
    private final String category;

    HistoryEntry(String name, double total, String category) {
        this.name = name;
        this.total = total;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }

    public String getCategory() {
        return category;
    }
}