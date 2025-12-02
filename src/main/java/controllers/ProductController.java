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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

// Models and services
import models.products.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;
import utils.TokenManager;

// Java standard
import java.util.List;
import java.util.Optional;

/**
 * Controller for Product CRUD operations
 * Connected to FastAPI backend using Retrofit
 */
public class ProductController {

    // ===== FORM COMPONENTS =====
    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_price;

    @FXML
    private TextField txt_cost;

    @FXML
    private TextField txt_stock;

    @FXML
    private ComboBox<ProductCategory> cmb_category;

    @FXML
    private Button btn_create;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_manage_categories;

    // ===== TABLE =====
    @FXML
    private TableView<Product> table_products;

    @FXML
    private TableColumn<Product, Integer> col_id;

    @FXML
    private TableColumn<Product, String> col_name;

    @FXML
    private TableColumn<Product, Integer> col_price;

    @FXML
    private TableColumn<Product, Integer> col_cost;

    @FXML
    private TableColumn<Product, Integer> col_stock;

    @FXML
    private TableColumn<Product, String> col_category;

    @FXML
    private TableColumn<Product, String> col_created_at;

    // ===== BUTTONS =====
    @FXML
    private Button btn_update;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_refresh;

    // Observable lists
    private ObservableList<Product> product_list = FXCollections.observableArrayList();
    private ObservableList<ProductCategory> category_list = FXCollections.observableArrayList();

    /**
     * Automatically executed when the view is loaded
     */
    @FXML
    public void initialize() {
        System.out.println("ProductController initialized");

        // Configure table columns
        configureTable();

        // Load categories and products
        loadCategories();
        loadProducts();
    }

    /**
     * Configure table columns
     */
    private void configureTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        col_created_at.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        // Show category name instead of ID
        col_category.setCellValueFactory(cellData -> {
            int category_id = cellData.getValue().getCategory_id();
            return new javafx.beans.property.SimpleStringProperty(getCategoryName(category_id));
        });

        table_products.setItems(product_list);
    }

    /**
     * Get category name from ID
     */
    private String getCategoryName(int category_id) {
        for (ProductCategory category : category_list) {
            if (category.getId() == category_id) {
                return category.getCategory();
            }
        }
        return "Unknown";
    }

    /**
     * Load categories from backend
     */
    private void loadCategories() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<ProductCategory>> call = RetrofitClient.getApiService().getAllCategories(token);

        call.enqueue(new Callback<List<ProductCategory>>() {
            @Override
            public void onResponse(Call<List<ProductCategory>> call, Response<List<ProductCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        category_list.clear();
                        category_list.addAll(response.body());
                        cmb_category.setItems(category_list);
                        System.out.println("Categories loaded: " + category_list.size());
                        table_products.refresh();
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Error", "Could not load categories", Alert.AlertType.ERROR);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ProductCategory>> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
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
                        System.out.println("Products loaded: " + product_list.size());
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Error", "Could not load products", Alert.AlertType.ERROR);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Create new product - Connected to POST /products/Create
     */
    @FXML
    private void handleCreateProduct() {
        // Get form data
        String name = txt_name.getText().trim();
        String price_str = txt_price.getText().trim();
        String cost_str = txt_cost.getText().trim();
        String stock_str = txt_stock.getText().trim();
        ProductCategory selected_category = cmb_category.getValue();

        // Validations
        if (name.isEmpty()) {
            showAlert("Error", "Product name is required", Alert.AlertType.ERROR);
            return;
        }

        if (selected_category == null) {
            showAlert("Error", "You must select a category", Alert.AlertType.ERROR);
            return;
        }

        int price, cost, stock;
        try {
            price = Integer.parseInt(price_str);
            cost = Integer.parseInt(cost_str);
            stock = Integer.parseInt(stock_str);

            if (price <= 0 || cost <= 0 || stock < 0) {
                showAlert("Error", "Price and cost must be positive, stock cannot be negative", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Price, cost, and stock must be valid numbers", Alert.AlertType.ERROR);
            return;
        }

        // Create ProductCreate object
        ProductCreate product_data = new ProductCreate(name, price, cost, stock, selected_category.getId());

        // Make request to backend
        String token = "Bearer " + TokenManager.getToken();

        Call<Product> call = RetrofitClient.getApiService().createProduct(token, product_data);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Success", "Product created successfully", Alert.AlertType.INFORMATION);

                        // Clear form
                        txt_name.clear();
                        txt_price.clear();
                        txt_cost.clear();
                        txt_stock.clear();
                        cmb_category.setValue(null);

                        // Reload list
                        loadProducts();
                    } else {
                        showAlert("Error", "Could not create product. Code: " + response.code(), Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Update selected product
     */
    @FXML
    private void handleUpdate() {
        Product selected_product = table_products.getSelectionModel().getSelectedItem();

        if (selected_product == null) {
            showAlert("Warning", "Select a product from the table", Alert.AlertType.WARNING);
            return;
        }

        // Create dialog to edit
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Product");
        dialog.setHeaderText("Edit product: " + selected_product.getName());

        // Create fields
        TextField txt_new_name = new TextField(selected_product.getName());
        TextField txt_new_price = new TextField(String.valueOf(selected_product.getPrice()));
        TextField txt_new_cost = new TextField(String.valueOf(selected_product.getCost()));
        TextField txt_new_stock = new TextField(String.valueOf(selected_product.getStock()));

        ComboBox<ProductCategory> cmb_new_category = new ComboBox<>();
        cmb_new_category.setItems(category_list);

        // Select current category
        for (ProductCategory category : category_list) {
            if (category.getId() == selected_product.getCategory_id()) {
                cmb_new_category.setValue(category);
                break;
            }
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(txt_new_name, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(txt_new_price, 1, 1);
        grid.add(new Label("Cost:"), 0, 2);
        grid.add(txt_new_cost, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(txt_new_stock, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(cmb_new_category, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String new_name = txt_new_name.getText().trim();
                    int new_price = Integer.parseInt(txt_new_price.getText().trim());
                    int new_cost = Integer.parseInt(txt_new_cost.getText().trim());
                    int new_stock = Integer.parseInt(txt_new_stock.getText().trim());
                    ProductCategory new_category = cmb_new_category.getValue();

                    if (new_name.isEmpty() || new_category == null) {
                        showAlert("Error", "All fields are required", Alert.AlertType.ERROR);
                        return;
                    }

                    // Create ProductUpdate object
                    ProductUpdate update_data = new ProductUpdate();
                    update_data.setName(new_name);
                    update_data.setPrice(new_price);
                    update_data.setCost(new_cost);
                    update_data.setStock(new_stock);
                    update_data.setCategory_id(new_category.getId());

                    // Call backend
                    String token = "Bearer " + TokenManager.getToken();

                    Call<Product> call = RetrofitClient.getApiService().updateProduct(
                            token,
                            selected_product.getId(),
                            update_data);

                    call.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            Platform.runLater(() -> {
                                if (response.isSuccessful()) {
                                    showAlert("Success", "Product updated successfully", Alert.AlertType.INFORMATION);
                                    loadProducts();
                                } else {
                                    showAlert("Error", "Could not update. Code: " + response.code(),
                                            Alert.AlertType.ERROR);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Platform.runLater(() -> {
                                showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                            });
                        }
                    });

                } catch (NumberFormatException e) {
                    showAlert("Error", "Price, cost, and stock must be valid numbers", Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Delete selected product
     */
    @FXML
    private void handleDelete() {
        Product selected_product = table_products.getSelectionModel().getSelectedItem();

        if (selected_product == null) {
            showAlert("Warning", "Select a product from the table", Alert.AlertType.WARNING);
            return;
        }

        // Confirm deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm deletion");
        confirmation.setHeaderText("Are you sure you want to delete this product?");
        confirmation.setContentText("Product: " + selected_product.getName());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String token = "Bearer " + TokenManager.getToken();

                Call<Void> call = RetrofitClient.getApiService().deleteProduct(token, selected_product.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                showAlert("Success", "Product deleted successfully", Alert.AlertType.INFORMATION);
                                loadProducts();
                            } else {
                                showAlert("Error", "Could not delete. Code: " + response.code(), Alert.AlertType.ERROR);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Platform.runLater(() -> {
                            showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                        });
                    }
                });
            }
        });
    }

    /**
     * Refresh product list
     */
    @FXML
    private void handleRefresh() {
        loadProducts();
        showAlert("Info", "List updated", Alert.AlertType.INFORMATION);
    }

    /**
     * Manage categories (create, update, delete)
     */
    @FXML
    private void handleManageCategories() {
        // Create dialog for category management
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Manage Categories");
        dialog.setHeaderText("Category Management");

        // Create UI elements
        ListView<ProductCategory> category_listview = new ListView<>();
        category_listview.setItems(category_list);
        category_listview.setPrefHeight(200);

        TextField txt_category_name = new TextField();
        txt_category_name.setPromptText("New category name");

        Button btn_add = new Button("Add Category");
        Button btn_delete_cat = new Button("Delete Selected");

        // Add category action
        btn_add.setOnAction(e -> {
            String category_name = txt_category_name.getText().trim();
            if (category_name.isEmpty()) {
                showAlert("Error", "Category name cannot be empty", Alert.AlertType.ERROR);
                return;
            }

            ProductCategoryCreate category_data = new ProductCategoryCreate(category_name);
            String token = "Bearer " + TokenManager.getToken();

            Call<ProductCategory> call = RetrofitClient.getApiService().createCategory(token, category_data);
            call.enqueue(new Callback<ProductCategory>() {
                @Override
                public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            showAlert("Success", "Category created", Alert.AlertType.INFORMATION);
                            txt_category_name.clear();
                            loadCategories();
                        } else {
                            showAlert("Error", "Could not create category", Alert.AlertType.ERROR);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ProductCategory> call, Throwable t) {
                    Platform.runLater(() -> {
                        showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                    });
                }
            });
        });

        // Delete category action
        btn_delete_cat.setOnAction(e -> {
            ProductCategory selected = category_listview.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Warning", "Select a category to delete", Alert.AlertType.WARNING);
                return;
            }

            String token = "Bearer " + TokenManager.getToken();
            Call<Void> call = RetrofitClient.getApiService().deleteCategory(token, selected.getId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            showAlert("Success", "Category deleted", Alert.AlertType.INFORMATION);
                            loadCategories();
                        } else {
                            showAlert("Error", "Could not delete. May have products associated", Alert.AlertType.ERROR);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Platform.runLater(() -> {
                        showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                    });
                }
            });
        });

        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Existing Categories:"),
                category_listview,
                new Label("Add New Category:"),
                txt_category_name,
                new HBox(10, btn_add, btn_delete_cat));

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    /**
     * Go back to main menu
     */
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btn_back.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.setTitle("Main Menu");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to show alerts
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}