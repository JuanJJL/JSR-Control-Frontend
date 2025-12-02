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

// Models and services

import models.clients.Client;
import models.clients.ClientCreate;
import models.clients.ClientUpdate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;
import utils.TokenManager;

// Java standard
import java.util.List;

/**
 * Controller for Client CRUD operations
 * Connected to FastAPI backend using Retrofit
 */
public class ClientController {

    // ===== FORM COMPONENTS =====
    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_age;

    @FXML
    private TextArea txt_details;

    @FXML
    private Button btn_create;

    @FXML
    private Button btn_back;

    // ===== TABLE =====
    @FXML
    private TableView<Client> table_clients;

    @FXML
    private TableColumn<Client, Integer> col_id;

    @FXML
    private TableColumn<Client, String> col_name;

    @FXML
    private TableColumn<Client, String> col_email;

    @FXML
    private TableColumn<Client, Integer> col_age;

    @FXML
    private TableColumn<Client, String> col_details;

    @FXML
    private TableColumn<Client, String> col_created_at;

    // ===== BUTTONS =====
    @FXML
    private Button btn_update;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_refresh;

    // Observable list
    private ObservableList<Client> client_list = FXCollections.observableArrayList();

    /**
     * Automatically executed when the view is loaded
     */
    @FXML
    public void initialize() {
        System.out.println("ClientController initialized");

        // Configure table columns
        configureTable();

        // Load clients
        loadClients();
    }

    /**
     * Configure table columns
     */
    private void configureTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_details.setCellValueFactory(new PropertyValueFactory<>("details"));
        col_created_at.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        table_clients.setItems(client_list);
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
                        System.out.println("Clients loaded: " + client_list.size());
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Error", "Could not load clients", Alert.AlertType.ERROR);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Create new client - Connected to POST /clients/create
     */
    @FXML
    private void handleCreateClient() {
        // Get form data
        String name = txt_name.getText().trim();
        String email = txt_email.getText().trim();
        String age_str = txt_age.getText().trim();
        String details = txt_details.getText().trim();

        // Validations
        if (name.isEmpty()) {
            showAlert("Error", "Name is required", Alert.AlertType.ERROR);
            return;
        }

        if (email.isEmpty() || !isValidEmail(email)) {
            showAlert("Error", "Valid email is required", Alert.AlertType.ERROR);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(age_str);
            if (age <= 0 || age > 150) {
                showAlert("Error", "Age must be between 1 and 150", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Age must be a valid number", Alert.AlertType.ERROR);
            return;
        }

        // Create ClientCreate object
        ClientCreate client_data = new ClientCreate(name, email, age, details.isEmpty() ? null : details);

        // Make request to backend
        String token = "Bearer " + TokenManager.getToken();

        Call<Client> call = RetrofitClient.getApiService().createClient(token, client_data);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Success", "Client created successfully", Alert.AlertType.INFORMATION);

                        // Clear form
                        txt_name.clear();
                        txt_email.clear();
                        txt_age.clear();
                        txt_details.clear();

                        // Reload list
                        loadClients();
                    } else {
                        showAlert("Error", "Could not create client. Code: " + response.code(), Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Update selected client
     */
    @FXML
    private void handleUpdate() {
        Client selected_client = table_clients.getSelectionModel().getSelectedItem();

        if (selected_client == null) {
            showAlert("Warning", "Select a client from the table", Alert.AlertType.WARNING);
            return;
        }

        // Create dialog to edit
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Client");
        dialog.setHeaderText("Edit client: " + selected_client.getName());

        // Create fields
        TextField txt_new_name = new TextField(selected_client.getName());
        TextField txt_new_email = new TextField(selected_client.getEmail());
        TextField txt_new_age = new TextField(String.valueOf(selected_client.getAge()));
        TextArea txt_new_details = new TextArea(selected_client.getDetails() != null ? selected_client.getDetails() : "");
        txt_new_details.setPrefRowCount(3);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(txt_new_name, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(txt_new_email, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(txt_new_age, 1, 2);
        grid.add(new Label("Details:"), 0, 3);
        grid.add(txt_new_details, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String new_name = txt_new_name.getText().trim();
                    String new_email = txt_new_email.getText().trim();
                    int new_age = Integer.parseInt(txt_new_age.getText().trim());
                    String new_details = txt_new_details.getText().trim();

                    if (new_name.isEmpty() || new_email.isEmpty()) {
                        showAlert("Error", "Name and email are required", Alert.AlertType.ERROR);
                        return;
                    }

                    if (!isValidEmail(new_email)) {
                        showAlert("Error", "Email format is invalid", Alert.AlertType.ERROR);
                        return;
                    }

                    // Create ClientUpdate object
                    ClientUpdate update_data = new ClientUpdate();
                    update_data.setName(new_name);
                    update_data.setEmail(new_email);
                    update_data.setAge(new_age);
                    update_data.setDetails(new_details.isEmpty() ? null : new_details);

                    // Call backend
                    String token = "Bearer " + TokenManager.getToken();

                    Call<Client> call = RetrofitClient.getApiService().updateClient(
                            token,
                            selected_client.getId(),
                            update_data
                    );

                    call.enqueue(new Callback<Client>() {
                        @Override
                        public void onResponse(Call<Client> call, Response<Client> response) {
                            Platform.runLater(() -> {
                                if (response.isSuccessful()) {
                                    showAlert("Success", "Client updated successfully", Alert.AlertType.INFORMATION);
                                    loadClients();
                                } else {
                                    showAlert("Error", "Could not update. Code: " + response.code(), Alert.AlertType.ERROR);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Client> call, Throwable t) {
                            Platform.runLater(() -> {
                                showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                            });
                        }
                    });

                } catch (NumberFormatException e) {
                    showAlert("Error", "Age must be a valid number", Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Delete selected client
     */
    @FXML
    private void handleDelete() {
        Client selected_client = table_clients.getSelectionModel().getSelectedItem();

        if (selected_client == null) {
            showAlert("Warning", "Select a client from the table", Alert.AlertType.WARNING);
            return;
        }

        // Confirm deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm deletion");
        confirmation.setHeaderText("Are you sure you want to delete this client?");
        confirmation.setContentText("Client: " + selected_client.getName());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String token = "Bearer " + TokenManager.getToken();

                Call<Void> call = RetrofitClient.getApiService().deleteClient(token, selected_client.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                showAlert("Success", "Client deleted successfully", Alert.AlertType.INFORMATION);
                                loadClients();
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
     * Refresh client list
     */
    @FXML
    private void handleRefresh() {
        loadClients();
        showAlert("Info", "List updated", Alert.AlertType.INFORMATION);
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
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        String email_regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(email_regex);
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