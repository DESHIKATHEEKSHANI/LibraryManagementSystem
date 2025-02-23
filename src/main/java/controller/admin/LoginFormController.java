package controller.admin;

import controller.dashboard.DashboardFormController;
import controller.dashboard.HomeFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.custom.impl.AdminServiceImpl;
import util.EmailService; // Assuming you have an EmailService for sending emails

import java.io.IOException;
import java.util.prefs.Preferences;

public class LoginFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox checkRememberMe;

    private AdminServiceImpl adminService;
    private Preferences preferences;

    public LoginFormController() {
        adminService = new AdminServiceImpl();
        preferences = Preferences.userNodeForPackage(LoginFormController.class);
    }

    @FXML
    public void initialize() {
        String savedEmail = preferences.get("email", "");
        String savedPassword = preferences.get("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            txtEmail.setText(savedEmail);
            txtPassword.setText(savedPassword);
            checkRememberMe.setSelected(true);
        }
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both email and password are required.");
        } else {
            boolean isAuthenticated = adminService.authenticate(email, password);

            if (isAuthenticated) {
                showAlert("Success", "Login successful!");

                if (checkRememberMe.isSelected()) {
                    preferences.put("email", email);
                    preferences.put("password", password);
                } else {
                    preferences.remove("email");
                    preferences.remove("password");
                }

                String adminName = adminService.getAdminName(email);

                navigateToDashboard(event, adminName);
            } else {
                showAlert("Error", "Invalid credentials, please try again.");
            }
        }
    }

    private void navigateToDashboard(ActionEvent event, String adminName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardForm.fxml"));
            Parent root = loader.load();

            DashboardFormController dashboardController = loader.getController();

            if (dashboardController != null) {
                HomeFormController homeFormController = dashboardController.getHomeFormController();

                if (homeFormController != null) {
                    homeFormController.setAdminName(adminName);
                } else {
                    showAlert("Error", "Failed to load the HomeFormController.");
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert("Error", "Failed to load the Dashboard form.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the dashboard.");
        }
    }





    @FXML
    void checkRememberMeOnAction(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (checkRememberMe.isSelected()) {
            preferences.put("email", email);
            preferences.put("password", password);
        } else {
            preferences.remove("email");
            preferences.remove("password");
        }
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/RegisterForm.fxml"))));
        stage.show();
    }

    @FXML
    void ForgotPasswordOnAction(ActionEvent event) {
        String email = txtEmail.getText();

        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email to reset your password.");
            return;
        }

        boolean emailExists = adminService.checkEmailExists(email);
        if (emailExists) {
            String verificationCode = generateVerificationCode();
            boolean emailSent = EmailService.sendVerificationCode(email, verificationCode);

            if (emailSent) {
                showAlert("Success", "A verification code has been sent to your email.");
                showPasswordResetDialog(email, verificationCode);
            } else {
                showAlert("Error", "Failed to send verification email. Please try again later.");
            }
        } else {
            showAlert("Error", "Email not found. Please check and try again.");
        }
    }

    private void showPasswordResetDialog(String email, String verificationCode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PasswordResetDialog.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            PasswordResetDialogController controller = loader.getController();
            controller.initData(email, verificationCode);

            stage.setTitle("Reset Password");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the password reset dialog.");
        }
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void navigateToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
