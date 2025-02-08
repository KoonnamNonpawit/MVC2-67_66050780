import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class InventoryModel {
    private static final String CSV_FILE = "inventory.csv";
    private static final String COUNT_FILE = "count.txt";
    private Map<String, Integer> acceptedCount = new HashMap<>();
    private Map<String, Integer> rejectedCount = new HashMap<>();

    public InventoryModel() {
        for (String type : new String[]{"Food", "Electronics", "Clothing"}) {
            acceptedCount.put(type, 0);
            rejectedCount.put(type, 0);
        }
        loadCounts();
    }

    private void loadCounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if (acceptedCount.containsKey(parts[0])) {
                        acceptedCount.put(parts[0], Integer.parseInt(parts[1]));
                        rejectedCount.put(parts[0], Integer.parseInt(parts[2]));
                    }
                }
            }
        } catch (IOException e) {
            // File does not exist, initialize with zeros
        }
    }

    private void saveCounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNT_FILE))) {
            for (String type : acceptedCount.keySet()) {
                writer.write(type + "," + acceptedCount.get(type) + "," + rejectedCount.get(type) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidProductId(String productId) {
        return productId.matches("^[1-9][0-9]{5}$");
    }

    public boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDuplicateProduct(String productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(productId + ",")) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean isValidCondition(String productType, String productCondition) {
        if (productType.equals("Electronics") && (productCondition.equals("Damaged") || productCondition.equals("Needs Inspection"))) {
            return false;
        }
        if (productType.equals("Clothing") && productCondition.equals("Damaged")) {
            return false;
        }
        return true;
    }

    public void saveToCSV(String productId, String productType, String expirationDate, String productCondition, boolean accepted) {
        if (accepted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
                writer.write(productId + "," + productType + "," + expirationDate + "," + productCondition + "\n");
                acceptedCount.put(productType, acceptedCount.get(productType) + 1);
                saveCounts();
            } catch (IOException e) {
                rejectedCount.put(productType, rejectedCount.get(productType) + 1);
                saveCounts();
            }
        } else {
            rejectedCount.put(productType, rejectedCount.get(productType) + 1);
            saveCounts();
        }
    }

    public Map<String, Integer> getAcceptedCount() {
        return acceptedCount;
    }

    public Map<String, Integer> getRejectedCount() {
        return rejectedCount;
    }
}
