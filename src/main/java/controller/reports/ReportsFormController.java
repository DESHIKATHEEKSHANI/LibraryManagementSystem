package controller.reports;

import db.DBConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.custom.BookService;
import service.custom.BorrowingTransactionService;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.BorrowingTransactionServiceImpl;

import java.util.Map;

public class ReportsFormController {

    @FXML
    private PieChart pieChartBookCategories;
    @FXML
    private BarChart<String, Number> barChartBorrowingStats;

    private final BookService bookService = new BookServiceImpl();
    private final BorrowingTransactionService borrowingTransactionService = new BorrowingTransactionServiceImpl();

    @FXML
    public void initialize() {
        loadPieChartData();
        loadBarChart();
    }

    private void loadBarChart() {
        barChartBorrowingStats.getData().clear();

        Map<String, Integer> stats = borrowingTransactionService.getBorrowingStatistics();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Transactions");

        XYChart.Data<String, Number> borrowedData = new XYChart.Data<>("Borrowed", stats.get("Borrowed"));
        XYChart.Data<String, Number> returnedData = new XYChart.Data<>("Returned", stats.get("Returned"));
        XYChart.Data<String, Number> overdueData = new XYChart.Data<>("Overdue", stats.get("Overdue"));

        series.getData().addAll(borrowedData, returnedData, overdueData);

        barChartBorrowingStats.getData().add(series);

        barChartBorrowingStats.setCategoryGap(10);
        barChartBorrowingStats.setBarGap(0);

        barChartBorrowingStats.setStyle("-fx-bar-width: 60;");

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node barNode = data.getNode();
                if (barNode != null) {
                    String category = data.getXValue();

                    switch (category) {
                        case "Borrowed":
                            barNode.setStyle("-fx-bar-fill: #b2f2bb;");
                            break;
                        case "Returned":
                            barNode.setStyle("-fx-bar-fill: #d8b4fe;");
                            break;
                        case "Overdue":
                            barNode.setStyle("-fx-bar-fill: #fc4747;");
                            break;
                    }
                }
            }
        });
    }



    private void loadPieChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> categoryData = bookService.getBookCategoryData();

        for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChartBookCategories.setData(pieChartData);
    }

    public void btnallbooksReportsOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/AllBookList.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }

    private void showReportWithFloatingCircle(String reportPath, ActionEvent event) {
        Stage loadingStage = new Stage(StageStyle.TRANSPARENT);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        StackPane root = new StackPane(progressIndicator);
        root.setStyle("-fx-padding: 10; -fx-background-color: transparent;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        loadingStage.setScene(scene);

        Window currentWindow = ((Node) event.getSource()).getScene().getWindow();
        loadingStage.initOwner(currentWindow);

        loadingStage.show();

        Thread loadThread = new Thread(() -> {
            try {
                JasperDesign design = JRXmlLoader.load(reportPath);
                JasperReport jasperReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        null,
                        DBConnection.getInstance().getConnection()
                );

                Platform.runLater(() -> {
                    loadingStage.close();
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);

                    int width = jasperPrint.getPageWidth() + 400;
                    int height = jasperPrint.getPageHeight() + 400;

                    viewer.setSize(width, height);

                    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

                    int x = (screenSize.width - width) / 2;
                    int y = (screenSize.height - height) / 2;

                    viewer.setLocation(x, y);
                    viewer.setVisible(true);
                });



            } catch (Exception e) {
                Platform.runLater(() -> {
                    loadingStage.close();
                    showErrorDialog("Failed to load report", e.getMessage());
                });
            }
        });
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void btnallmembersReportsOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/AllMemberList.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }

    public void btnBorrowedBooksReportsOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/BorrowedBooks.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }

    public void btnFinePayementsReportOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/FinePaymentReport.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }

    public void btnOverDueBooksOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/OverDueBooksReport.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }

    public void btnReturnBooksOnAction(ActionEvent actionEvent) {
        String reportPath = "D:/ICD113/JavaFX/LibraryManagement/src/main/resources/reports/ReturnedBooksReport.jrxml";
        showReportWithFloatingCircle(reportPath, actionEvent);
    }
}