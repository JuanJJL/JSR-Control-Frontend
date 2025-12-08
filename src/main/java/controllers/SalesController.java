package controllers;

// JavaFX imports
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

// Models and services
import models.clients.*;
import models.products.*;
import models.sales.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;
import utils.TokenManager;

// Java standard
import java.util.List;

public class SalesController {

    // ===== FORM COMPONENTS =====
    @FXML
    private ComboBox<Client> cmb_client;

    @FXML
    private ComboBox<Product> cmb_product;

    @FXML
    private TextField txt_quantity;

    @FXML
    private ComboBox<PaymentMethod> cmb_payment_method;

    @FXML
    private Label lbl_product_info;

    @FXML
    private Label lbl_total;

    @FXML
    private Button btn_register;

    @FXML
    private Button btn_back;

    // ===== TABLE =====
    @FXML
    private TableView<SalesRecord> table_sales;

    @FXML
    private TableColumn<SalesRecord, Integer> col_id;

    @FXML
    private TableColumn<SalesRecord, String> col_client;

    @FXML
    private TableColumn<SalesRecord, String> col_product;

    @FXML
    private TableColumn<SalesRecord, Integer> col_quantity;

    @FXML
    private TableColumn<SalesRecord, Integer> col_unit_price;

    @FXML
    private TableColumn<SalesRecord, Integer> col_total;

    @FXML
    private TableColumn<SalesRecord, String> col_payment;

    @FXML
    private TableColumn<SalesRecord, String> col_created_at;

    // ===== BUTTONS =====
    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_refresh;

    // Observable lists
    private ObservableList<SalesRecord> sales_list = FXCollections.observableArrayList();
    private ObservableList<Client> client_list = FXCollections.observableArrayList();
    private ObservableList<Product> product_list = FXCollections.observableArrayList();
    private ObservableList<PaymentMethod> payment_method_list = FXCollections.observableArrayList();

    /**
     * Automatically executed when the view is loaded
     */
    @FXML
    public void initialize() {
        System.out.println("SalesController initialized");

        // Configure table columns
        configureTable();

        // Load data
        loadClients();
        loadProducts();
        loadPaymentMethods();

        loadSales();

        // Configure ComboBox converters for proper display
        configureComboBoxes();
    }

    /**
     * Configure ComboBox converters to show names instead of object toString()
     */
    private void configureComboBoxes() {
        // Client Converter
        cmb_client.setConverter(new javafx.util.StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client != null ? client.getName() : "";
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });

        // Product Converter
        cmb_product.setConverter(new javafx.util.StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product != null ? product.getName() : "";
            }

            @Override
            public Product fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Configure table columns
     */
    private void configureTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_unit_price.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        col_created_at.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        // Show client name instead of ID
        col_client.setCellValueFactory(cellData -> {
            int client_id = cellData.getValue().getId_client();
            return new javafx.beans.property.SimpleStringProperty(getClientName(client_id));
        });

        // Show product name instead of ID
        col_product.setCellValueFactory(cellData -> {
            int product_id = cellData.getValue().getId_product();
            return new javafx.beans.property.SimpleStringProperty(getProductName(product_id));
        });

        // Show payment method name instead of ID
        col_payment.setCellValueFactory(cellData -> {
            int payment_id = cellData.getValue().getId_payment_method();
            return new javafx.beans.property.SimpleStringProperty(getPaymentMethodName(payment_id));
        });

        table_sales.setItems(sales_list);
    }

    /**
     * Get client name from ID
     */
    private String getClientName(int client_id) {
        for (Client client : client_list) {
            if (client.getId() == client_id) {
                return client.getName();
            }
        }
        return "Unknown";
    }

    /**
     * Get product name from ID
     */
    private String getProductName(int product_id) {
        for (Product product : product_list) {
            if (product.getId() == product_id) {
                return product.getName();
            }
        }
        return "Unknown";
    }

    /**
     * Get payment method name from ID
     */
    private String getPaymentMethodName(int payment_id) {
        for (PaymentMethod pm : payment_method_list) {
            if (pm.getId() == payment_id) {
                return pm.getPayment_method();
            }
        }
        return "Unknown";
    }

    /**
     * Load clients from backend
     */
    private void loadClients() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<Client>> call = RetrofitClient.getApiService().getAllClients(token);

        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        client_list.clear();
                        client_list.addAll(response.body());
                        cmb_client.setItems(client_list);
                        System.out.println("Clients loaded: " + client_list.size());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Load products from backend
     */
    private void loadProducts() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<Product>> call = RetrofitClient.getApiService().getAllProducts(token);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        product_list.clear();
                        product_list.addAll(response.body());
                        cmb_product.setItems(product_list);
                        System.out.println("Products loaded: " + product_list.size());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Load payment methods from backend
     */
    private void loadPaymentMethods() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<PaymentMethod>> call = RetrofitClient.getApiService().getAllPaymentMethods(token);

        call.enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, Response<List<PaymentMethod>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        payment_method_list.clear();
                        payment_method_list.addAll(response.body());
                        cmb_payment_method.setItems(payment_method_list);
                        System.out.println("Payment methods loaded: " + payment_method_list.size());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Load sales from backend
     */
    private void loadSales() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<SalesRecord>> call = RetrofitClient.getApiService().getAllSales(token);

        call.enqueue(new Callback<List<SalesRecord>>() {
            @Override
            public void onResponse(Call<List<SalesRecord>> call, Response<List<SalesRecord>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        sales_list.clear();
                        sales_list.addAll(response.body());
                        System.out.println("Sales loaded: " + sales_list.size());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<SalesRecord>> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Handle product selection - show price and stock
     */
    @FXML
    private void handleProductSelection() {
        Product selected_product = cmb_product.getValue();

        if (selected_product != null) {
            String info = String.format("Price: $%d | Stock: %d units",
                    selected_product.getPrice(),
                    selected_product.getStock());
            lbl_product_info.setText(info);

            // Update total if quantity is already entered
            updateTotal();
        } else {
            lbl_product_info.setText("");
            lbl_total.setText("$0");
        }
    }

    /**
     * Handle quantity change - update total
     */
    @FXML
    private void handleQuantityChange() {
        updateTotal();
    }

    /**
     * Update total amount preview
     */
    private void updateTotal() {
        Product selected_product = cmb_product.getValue();
        String quantity_str = txt_quantity.getText().trim();

        if (selected_product != null && !quantity_str.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantity_str);
                int total = selected_product.getPrice() * quantity;
                lbl_total.setText("$" + total);
            } catch (NumberFormatException e) {
                lbl_total.setText("$0");
            }
        } else {
            lbl_total.setText("$0");
        }
    }

    /**
     * Register sale - Connected to POST /sales/create
     */
    @FXML
    private void handleRegisterSale() {
        // Get form data
        Client selected_client = cmb_client.getValue();
        Product selected_product = cmb_product.getValue();
        String quantity_str = txt_quantity.getText().trim();
        PaymentMethod selected_payment = cmb_payment_method.getValue();

        // Validations
        if (selected_client == null) {
            showAlert("Error", "Please select a client", Alert.AlertType.ERROR);
            return;
        }

        if (selected_product == null) {
            showAlert("Error", "Please select a product", Alert.AlertType.ERROR);
            return;
        }

        if (selected_payment == null) {
            showAlert("Error", "Please select a payment method", Alert.AlertType.ERROR);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantity_str);
            if (quantity <= 0) {
                showAlert("Error", "Quantity must be greater than 0", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Quantity must be a valid number", Alert.AlertType.ERROR);
            return;
        }

        // Create SalesRecordCreate object
        SalesRecordCreate sale_data = new SalesRecordCreate(
                selected_client.getId(),
                selected_product.getId(),
                quantity,
                selected_payment.getId());

        // Make request to backend
        String token = "Bearer " + TokenManager.getToken();

        Call<SalesRecord> call = RetrofitClient.getApiService().createSale(token, sale_data);

        call.enqueue(new Callback<SalesRecord>() {
            @Override
            public void onResponse(Call<SalesRecord> call, Response<SalesRecord> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Success", "Sale registered successfully!", Alert.AlertType.INFORMATION);

                        // Clear form
                        cmb_client.setValue(null);
                        cmb_product.setValue(null);
                        txt_quantity.clear();
                        cmb_payment_method.setValue(null);
                        lbl_product_info.setText("");
                        lbl_total.setText("$0");

                        // Reload data
                        loadSales();
                        loadProducts(); // Refresh to see updated stock
                    } else {
                        showAlert("Error", "Could not register sale. Code: " + response.code(), Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            public void onFailure(Call<SalesRecord> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Delete selected sale
     */
    @FXML
    private void handleDelete() {
        SalesRecord selected_sale = table_sales.getSelectionModel().getSelectedItem();

        if (selected_sale == null) {
            showAlert("Warning", "Select a sale from the table", Alert.AlertType.WARNING);
            return;
        }

        // Confirm deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm deletion");
        confirmation.setHeaderText("Are you sure you want to delete this sale?");
        confirmation.setContentText("Note: This will NOT restore the product stock.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String token = "Bearer " + TokenManager.getToken();

                Call<Void> call = RetrofitClient.getApiService().deleteSale(token, selected_sale.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                showAlert("Success", "Sale deleted successfully", Alert.AlertType.INFORMATION);
                                loadSales(); // Refresh table
                            } else {
                                showAlert("Error", "Could not delete sale. Code: " + response.code(),
                                        Alert.AlertType.ERROR);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Platform.runLater(() -> {
                            showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                        });
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * Refresh sales table
     */
    @FXML
    private void handleRefresh() {
        loadSales();
        loadProducts(); // Also refresh products to see updated stock
        showAlert("Info", "Data refreshed", Alert.AlertType.INFORMATION);
    }

    /**
     * Go back to main menu
     */
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btn_back.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load main menu", Alert.AlertType.ERROR);
        }
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}