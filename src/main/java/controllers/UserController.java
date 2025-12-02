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
import models.user.User;
import models.user.UserCreate;
import models.user.UserUpdate;

import models.Rol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;
import utils.TokenManager;

// Java standard
import java.util.List;

/**
 * Controller for User CRUD operations
 * Connected to FastAPI backend using Retrofit
 */
public class UserController {

    // ===== FORM COMPONENTS =====
    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private ComboBox<Rol> cmb_role_id;

    @FXML
    private Button btn_create;

    @FXML
    private Button btn_back;

    // ===== TABLE =====
    @FXML
    private TableView<User> table_users;

    @FXML
    private TableColumn<User, Integer> col_id;

    @FXML
    private TableColumn<User, String> col_username;

    @FXML
    private TableColumn<User, String> col_role_id;

    @FXML
    private TableColumn<User, Integer> col_status;

    @FXML
    private TableColumn<User, String> col_created_at;

    // ===== BUTTONS =====
    @FXML
    private Button btn_update;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_refresh;

    // Observable list for the table
    private ObservableList<User> user_list = FXCollections.observableArrayList();

    /**
     * Automatically executed when the view is loaded
     */
    @FXML
    public void initialize() {
        System.out.println("UserController initialized");

        // Configure role ComboBox with names
        ObservableList<Rol> roles = FXCollections.observableArrayList(
                new Rol(1, "Employee"),
                new Rol(2, "Supervisor"),
                new Rol(3, "Admin")
        );
        cmb_role_id.setItems(roles);

        // Configure table columns
        configureTable();

        // Load users from backend
        loadUsers();
    }

    /**
     * Configure table columns
     */
    private void configureTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_created_at.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        // Show role name instead of ID
        col_role_id.setCellValueFactory(cellData -> {
            int role_id = cellData.getValue().getRole_id();
            return new javafx.beans.property.SimpleStringProperty(getRoleName(role_id));
        });

        table_users.setItems(user_list);
    }

    /**
     * Get role name from ID
     */
    private String getRoleName(int role_id) {
        switch (role_id) {
            case 1: return "Employee";
            case 2: return "Supervisor";
            case 3: return "Admin";
            default: return "Unknown";
        }
    }

    /**
     * Load users from backend
     */
    private void loadUsers() {
        String token = "Bearer " + TokenManager.getToken();

        Call<List<User>> call = RetrofitClient.getApiService().getUsers(token);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Platform.runLater(() -> {
                        user_list.clear();
                        user_list.addAll(response.body());
                        System.out.println("Users loaded: " + user_list.size());
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Error", "Could not load users", Alert.AlertType.ERROR);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Platform.runLater(() -> {
                    showAlert("Error", "Connection error: " + t.getMessage(), Alert.AlertType.ERROR);
                });
                t.printStackTrace();
            }
        });
    }

    /**
     * Create new user - Connected to POST /user/create
     */
    @FXML
    private void handleCreateUser() {
        // Get form data
        String username = txt_username.getText();
        String password = txt_password.getText();
        Rol selected_role = cmb_role_id.getValue();

        // Validations
        if (username.isEmpty() || username.length() < 3) {
            showAlert("Error", "Username must be at least 3 characters", Alert.AlertType.ERROR);
            return;
        }

        if (password.isEmpty() || password.length() < 8) {
            showAlert("Error", "Password must be at least 8 characters", Alert.AlertType.ERROR);
            return;
        }

        if (selected_role == null) {
            showAlert("Error", "You must select a role", Alert.AlertType.ERROR);
            return;
        }

        // Get role ID
        int role_id = selected_role.getId();

        // Make request to backend
        String token = "Bearer " + TokenManager.getToken();

        UserCreate newUser = new UserCreate(username, password, role_id);


        Call<Void> call = RetrofitClient.getApiService().createUser(token, newUser);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Success", "User created successfully", Alert.AlertType.INFORMATION);

                        // Clear form
                        txt_username.clear();
                        txt_password.clear();
                        cmb_role_id.setValue(null);

                        // Reload list
                        loadUsers();
                    } else {
                        showAlert("Error", "Could not create user. Code: " + response.code(), Alert.AlertType.ERROR);
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

    /**
     * Update selected user
     */
    @FXML
    private void handleUpdate() {
        User selected_user = table_users.getSelectionModel().getSelectedItem();


        if (selected_user == null) {
            showAlert("Warning", "Select a user from the table", Alert.AlertType.WARNING);
            return;
        }

        // Create dialog to edit
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update User");
        dialog.setHeaderText("Edit user data: " + selected_user.getUsername());

        // Create fields
        TextField txt_new_username = new TextField(selected_user.getUsername());
        txt_new_username.setPromptText("New username");

        ComboBox<Rol> cmb_new_role_id = new ComboBox<>();
        ObservableList<Rol> roles = FXCollections.observableArrayList(
                new Rol(1, "Employee"),
                new Rol(2, "Supervisor"),
                new Rol(3, "Admin")
        );
        cmb_new_role_id.setItems(roles);

        // Select current user role
        for (Rol role : roles) {
            if (role.getId() == selected_user.getRole_id()) {
                cmb_new_role_id.setValue(role);
                break;
            }
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(txt_new_username, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(cmb_new_role_id, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String new_username = txt_new_username.getText().trim();
                Rol new_role = cmb_new_role_id.getValue();

                if (new_username.isEmpty() || new_role == null) {
                    showAlert("Error", "All fields are required", Alert.AlertType.ERROR);
                    return;
                }

                int new_role_id = new_role.getId();

                // Call backend
                String token = "Bearer " + TokenManager.getToken();

                UserUpdate updatedUser = new UserUpdate(new_username, new_role_id);

                Call<Void> call = RetrofitClient.getApiService().updateUser(
                        selected_user.getId(),
                        updatedUser
                );

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                showAlert("Success", "User updated successfully", Alert.AlertType.INFORMATION);
                                loadUsers();
                            } else {
                                showAlert("Error", "Could not update. Code: " + response.code(), Alert.AlertType.ERROR);
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
     * Delete selected user
     */
    @FXML
    private void handleDelete() {
        User selected_user = table_users.getSelectionModel().getSelectedItem();

        if (selected_user == null) {
            showAlert("Warning", "Select a user from the table", Alert.AlertType.WARNING);
            return;
        }

        // Confirm deletion
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm deletion");
        confirmation.setHeaderText("Are you sure you want to delete this user?");
        confirmation.setContentText("Username: " + selected_user.getUsername());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String token = "Bearer " + TokenManager.getToken();

                Call<Void> call = RetrofitClient.getApiService().deleteUser(token, selected_user.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                showAlert("Success", "User deleted successfully", Alert.AlertType.INFORMATION);
                                loadUsers();
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
     * Refresh user list
     */
    @FXML
    private void handleRefresh() {
        loadUsers();
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
            stage.setScene(new Scene(root, 800, 600));
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