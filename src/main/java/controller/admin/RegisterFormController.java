package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.custom.impl.AdminServiceImpl;

import java.io.IOException;

public class RegisterFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox checkAgree;

    // Initialize the service here
    private final AdminServiceImpl adminService = new AdminServiceImpl();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
        stage.show();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        // Step 1: Validate agreement to terms
        if (!checkAgree.isSelected()) {
            showAlert("Terms & Conditions", "You must agree to the terms and conditions to register.", Alert.AlertType.WARNING);
            return;
        }

        // Step 2: Get user input
        String userName = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        // Step 3: Validate user input
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        // Step 4: Call service layer to register
        boolean success = adminService.registerAdmin(userName, email, password);

        // Step 5: Provide feedback based on success
        if (success) {
            showAlert("Success", "admin registered successfully!", Alert.AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Error", "Registration failed. Email may already be in use.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void checkAgreeOnAction(ActionEvent event) {
        // Optionally print or log for debug purposes
        if (checkAgree.isSelected()) {
            System.out.println("User agreed to terms and conditions.");
        } else {
            System.out.println("User did not agree.");
        }
    }

    // Helper method to show alerts to the user
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to clear the form after a successful registration
    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtPassword.clear();
        checkAgree.setSelected(false);
    }
}
