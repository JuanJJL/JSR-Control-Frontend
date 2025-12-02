package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.LoginRequest;
import models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;
import utils.TokenManager;

public class LoginController {


        @FXML
        private TextField username_field;

        @FXML
        private PasswordField password_field;


        @FXML
        private void handleLogin() {

            String username = username_field.getText();
            String password = password_field.getText();


            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Por favor ingresa usuario y contraseña", Alert.AlertType.ERROR);
                return;
            }


            LoginRequest request = new LoginRequest(username, password);


            Call<TokenResponse> call = RetrofitClient.getApiService().login(request);


            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {


                    if (response.isSuccessful() && response.body() != null) {
                        // Login exitoso
                        TokenResponse token = response.body();

                        // Guardar el token
                        TokenManager.setToken(token.getAccess_token());

                        System.out.println("Login exitoso!");
                        System.out.println("Token: " + token.getAccess_token());

                        // Mostrar mensaje de éxito
                        Platform.runLater(() -> {
                            showAlert("Éxito", "Login exitoso!", Alert.AlertType.INFORMATION);

                            // Cambiar a la pantalla de CRUD
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
                                Parent root = loader.load();

                                Stage stage = (Stage) username_field.getScene().getWindow();
                                stage.setScene(new Scene(root, 900, 600));
                                stage.setTitle("Gestión de Usuarios");

                            } catch (Exception e) {
                                e.printStackTrace();
                                showAlert("Error", "No se pudo cargar la pantalla de usuarios", Alert.AlertType.ERROR);
                            }
                        });

                    } else {
                        // Login fallido (401, 400, etc.)
                        Platform.runLater(() -> {
                            showAlert("Error", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
                        });
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {

                    Platform.runLater(() -> {
                        showAlert("Error", "Error de conexión: " + t.getMessage(), Alert.AlertType.ERROR);
                    });
                    t.printStackTrace();
                }
            });
        }


        private void showAlert(String title, String message, Alert.AlertType type) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }



