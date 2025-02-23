package controller.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {

    @FXML
    private AnchorPane paneLoadContents;

    @FXML
    private Label txtDate;

    @Getter
    @FXML
    private HomeFormController homeFormController;

    private String adminName = "Admin";

    @FXML
    private void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtDate.setText(currentDate.format(formatter));

        loadContent("/view/WelcomeHome.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("/view/WelcomeHome.fxml")) {
                homeFormController = loader.getController();

                if (homeFormController != null) {
                    homeFormController.setAdminName(adminName);
                } else {
                    System.out.println("HomeFormController is not initialized.");
                }
            }

            paneLoadContents.getChildren().clear();
            paneLoadContents.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnBookManagementOnAction(ActionEvent event) {
        loadContent("/view/BookManagementForm.fxml");
    }

    @FXML
    void btnBorrowTransactionOnAction(ActionEvent event) {
        loadContent("/view/TransactionsForm.fxml");
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadContent("/view/WelcomeHome.fxml");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) paneLoadContents.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnMemberMangementOnAction(ActionEvent event) {
        loadContent("/view/MemberManagementForm.fxml");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        loadContent("/view/ReportsForm.fxml");
    }

    @FXML
    void btnReturnBookOnAction(ActionEvent event) {
        loadContent("/view/ReturnBookForm.fxml");
    }
}
