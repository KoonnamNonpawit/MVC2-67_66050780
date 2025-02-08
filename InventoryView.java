import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class InventoryView extends JFrame {
    JTextField productIdField, expirationDateField;
    JComboBox<String> productTypeBox, productConditionBox;
    JTextArea resultArea;
    JLabel foodAcceptedLabel, foodRejectedLabel;
    JLabel electronicsAcceptedLabel, electronicsRejectedLabel;
    JLabel clothingAcceptedLabel, clothingRejectedLabel;
    JButton submitButton;

    public InventoryView() {
        setTitle("Inventory Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        add(new JLabel("Product ID (6 digits, first not 0):"));
        productIdField = new JTextField();
        add(productIdField);

        add(new JLabel("Product Type:"));
        String[] productTypes = {"Food", "Electronics", "Clothing"};
        productTypeBox = new JComboBox<>(productTypes);
        add(productTypeBox);

        add(new JLabel("Expiration Date (yyyy-MM-dd, Food only):"));
        expirationDateField = new JTextField();
        expirationDateField.setEnabled(false);
        add(expirationDateField);

        add(new JLabel("Product Condition:"));
        String[] conditions = {"Normal", "Damaged", "Needs Inspection"};
        productConditionBox = new JComboBox<>(conditions);
        add(productConditionBox);

        submitButton = new JButton("Submit");
        add(submitButton);

        resultArea = new JTextArea();
        add(new JScrollPane(resultArea));

        foodAcceptedLabel = new JLabel("Food Accepted: 0");
        foodRejectedLabel = new JLabel("Food Rejected: 0");
        add(foodAcceptedLabel);
        add(foodRejectedLabel);

        electronicsAcceptedLabel = new JLabel("Electronics Accepted: 0");
        electronicsRejectedLabel = new JLabel("Electronics Rejected: 0");
        add(electronicsAcceptedLabel);
        add(electronicsRejectedLabel);

        clothingAcceptedLabel = new JLabel("Clothing Accepted: 0");
        clothingRejectedLabel = new JLabel("Clothing Rejected: 0");
        add(clothingAcceptedLabel);
        add(clothingRejectedLabel);

        productTypeBox.addActionListener(e -> {
            expirationDateField.setEnabled(productTypeBox.getSelectedItem().equals("Food"));
            if (!productTypeBox.getSelectedItem().equals("Food")) {
                expirationDateField.setText("");
            }
        });
    }

    public void updateCounts(Map<String, Integer> accepted, Map<String, Integer> rejected) {
        foodAcceptedLabel.setText("Food Accepted: " + accepted.get("Food"));
        foodRejectedLabel.setText("Food Rejected: " + rejected.get("Food"));
        electronicsAcceptedLabel.setText("Electronics Accepted: " + accepted.get("Electronics"));
        electronicsRejectedLabel.setText("Electronics Rejected: " + rejected.get("Electronics"));
        clothingAcceptedLabel.setText("Clothing Accepted: " + accepted.get("Clothing"));
        clothingRejectedLabel.setText("Clothing Rejected: " + rejected.get("Clothing"));
    }
}
