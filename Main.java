public class Main {
    public static void main(String[] args) {
        InventoryModel model = new InventoryModel();
        InventoryView view = new InventoryView();
        new InventoryController(model, view);
        view.setVisible(true);
    }
}
