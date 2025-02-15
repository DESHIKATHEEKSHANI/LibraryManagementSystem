package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.custom.impl.AdminServiceImpl;

public class PasswordResetDialogController {

    @FXML
    private TextField txtVerificationCode;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    private String email;
    private String verificationCode;

    private AdminServiceImpl adminService;

    public PasswordResetDialogController() {
        adminService = new AdminServiceImpl(); // Service to interact with the database
    }

    public void initData(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    @FXML
    void btnResetPasswordOnAction() {
        String enteredCode = txtVerificationCode.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (enteredCode.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!enteredCode.equals(verificationCode)) {
            showAlert("Error", "Incorrect verification code.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        // Reset password using the service
        boolean isPasswordReset = adminService.resetPassword(email, newPassword);

        if (isPasswordReset) {
            showAlert("Success", "Your password has been reset successfully.");
            closeStage();
        } else {
            showAlert("Error", "Failed to reset password. Please try again.");
        }
    }

    private void closeStage() {
        Stage stage = (Stage) txtVerificationCode.getScene().getWindow();
        stage.close();  // Close the password reset dialog
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
