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
        if (!checkAgree.isSelected()) {
            showAlert("Terms & Conditions", "You must agree to the terms and conditions to register.", Alert.AlertType.WARNING);
            return;
        }

        String userName = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = adminService.registerAdmin(userName, email, password);

        if (success) {
            showAlert("Success", "admin registered successfully!", Alert.AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Error", "Registration failed. Email may already be in use.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void checkAgreeOnAction(ActionEvent event) {
        if (checkAgree.isSelected()) {
            System.out.println("User agreed to terms and conditions.");
        } else {
            System.out.println("User did not agree.");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtPassword.clear();
        checkAgree.setSelected(false);
    }
}
