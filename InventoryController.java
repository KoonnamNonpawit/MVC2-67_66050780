import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class InventoryController {
    private InventoryModel model;
    private InventoryView view;

    public InventoryController(InventoryModel model, InventoryView view) {
        this.model = model;
        this.view = view;

        this.view.submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processProduct();
            }
        });
    }

    private void processProduct() {
        String productId = view.productIdField.getText().trim();
        String productType = (String) view.productTypeBox.getSelectedItem();
        String expirationDate = view.expirationDateField.getText().trim();
        String productCondition = (String) view.productConditionBox.getSelectedItem();

        if (!model.isValidProductId(productId)) {
            view.resultArea.append("Error: Product ID must be 6 digits and not start with 0.\n");
            return;
        }

        if (model.isDuplicateProduct(productId)) {
            view.resultArea.append("Error: Product ID already exists.\n");
            return;
        }

        if (productType.equals("Food")) {
            if (expirationDate.isEmpty()) {
                view.resultArea.append("Error: Expiration date is required for Food.\n");
                return;
            }
            if (!model.isValidDate(expirationDate)) {
                view.resultArea.append("Error: Invalid expiration date format. Use yyyy-MM-dd.\n");
                return;
            }
            if (LocalDate.parse(expirationDate).isBefore(LocalDate.now())) {
                view.resultArea.append("Error: Food expired. Rejected.\n");
                model.saveToCSV(productId, productType, expirationDate, productCondition, false);
                view.updateCounts(model.getAcceptedCount(), model.getRejectedCount());
                return;
            }
        } else {
            if (!expirationDate.isEmpty()) {
                view.resultArea.append("Error: Expiration date should not be provided for non-food items.\n");
                return;
            }
            expirationDate = "N/A";
        }

        if (!model.isValidCondition(productType, productCondition)) {
            view.resultArea.append("Error: Invalid product condition for this category.\n");
            model.saveToCSV(productId, productType, expirationDate, productCondition, false);
            view.updateCounts(model.getAcceptedCount(), model.getRejectedCount());
            return;
        }

        model.saveToCSV(productId, productType, expirationDate, productCondition, true);
        view.resultArea.append("Product accepted.\n");
        view.updateCounts(model.getAcceptedCount(), model.getRejectedCount());
    }
}

