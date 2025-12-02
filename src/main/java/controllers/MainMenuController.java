package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.TokenManager;

public class MainMenuController {

    @FXML
    private Label username_label;

    @FXML
    private Button logout_button;

    @FXML
    private Button sales_button;

    @FXML
    private Button expenses_button;

    @FXML
    private Button users_button;

    @FXML
    private Button products_button;

    @FXML
    private Button reports_button;

    @FXML
    public void initialize() {
        // Show logged in user
        if (TokenManager.isLoggedIn()) {
            username_label.setText("Welcome, " + TokenManager.getUsername());
        }
    }

    @FXML
    private void openSales() {
        changeView("/views/client_crud.fxml", "Client Management", 1000, 650);
    }

    @FXML
    private void openExpenses() {
        System.out.println("Opening Expenses...");
        // TODO: changeView("/views/expenses.fxml", "Expenses", 900, 600);
    }



    @FXML
    private void openUserCrud() {
        changeView("/views/user_crud.fxml", "User Management", 950, 650);
    }

    @FXML
    private void openProducts() {
        changeView("/views/product_crud.fxml", "Product Management", 1000, 650);
    }


    @FXML
    private void openReports() {
        System.out.println("Opening Reports...");
        // TODO: changeView("/views/reports.fxml", "Reports", 900, 600);
    }

    @FXML
    private void handleLogout() {
        TokenManager.clearSession();
        changeView("/views/login.fxml", "Login", 400, 300);
    }

    private void changeView(String fxml_path, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_path));
            Parent root = loader.load();

            Stage stage = (Stage) logout_button.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading: " + fxml_path);
        }
    }
}