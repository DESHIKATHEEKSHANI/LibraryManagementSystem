package controller.member;

import com.jfoenix.controls.JFXTextField;
import dto.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.custom.MemberService;
import service.custom.impl.MemberServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MemberFormController implements Initializable {

    private final MemberService memberService = new MemberServiceImpl();

    @FXML
    private TableView tblMembers;
    @FXML
    private TableColumn colMemberID;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colDate;
    @FXML
    private TableColumn colStatus;

    @FXML
    private JFXTextField txtMemberID;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtContactNo;
    @FXML
    private DatePicker txtDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTable();
        txtDate.setValue(LocalDate.now());
        txtMemberID.setText(generateNewMemberId());

//        statusComboBox.getItems().setAll("ACTIVE", "INACTIVE");
//        statusComboBox.setValue("ACTIVE");

        tblMembers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Member) newValue);
            }
        });
    }

    private void setTextToValues(Member member) {
        txtMemberID.setText(member.getMemberID());
        txtName.setText(member.getName());
        txtContactNo.setText(member.getContactInfo());
        txtDate.setValue(member.getMembershipDate());
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateInput()) {
            String memberId = generateNewMemberId();
            txtMemberID.setText(memberId);

            Member member = new Member(
                    memberId,
                    txtName.getText(),
                    txtContactNo.getText(),
                    txtDate.getValue(),
                    "ACTIVE"
            );

            boolean result = memberService.addMember(member);
            if (result) {
                showAlert("Success", "Member Added Successfully!", Alert.AlertType.INFORMATION);
                loadTable();
                clearFields();
            } else {
                showAlert("Error", "Failed to add member", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        boolean result = memberService.deleteMember(txtMemberID.getText());
        if (result) {
            showAlert("Success", "Member Deleted Successfully!", Alert.AlertType.INFORMATION);
            loadTable();
            clearFields();
        } else {
            showAlert("Error", "Failed to delete member", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Member member = memberService.searchMember(txtMemberID.getText());
        if (member != null) {
            setTextToValues(member);
        } else {
            showAlert("Error", "Member not found", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (validateInput()) {
            Member member = new Member(
                    txtMemberID.getText(),
                    txtName.getText(),
                    txtContactNo.getText(),
                    txtDate.getValue(),
                    "ACTIVE"
            );

            boolean result = memberService.updateMember(member);
            if (result) {
                showAlert("Success", "Member Updated Successfully!", Alert.AlertType.INFORMATION);
                loadTable();
                clearFields();
            } else {
                showAlert("Error", "Failed to update member", Alert.AlertType.ERROR);
            }
        }
    }

    private void loadTable() {
        List<Member> members = memberService.getAllMembers();

        if (members != null && !members.isEmpty()) {
            ObservableList<Member> observableMembers = FXCollections.observableList(members);
            tblMembers.setItems(observableMembers);
        } else {
            tblMembers.getItems().clear();
        }
    }

    private void clearFields() {
        txtMemberID.setText(generateNewMemberId());
        txtName.clear();
        txtContactNo.clear();
        txtDate.setValue(LocalDate.now());
    }

    private String generateNewMemberId() {
        return memberService.generateNewMemberId();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty() || txtContactNo.getText().trim().isEmpty() || txtDate.getValue() == null) {
            showAlert("Validation Error", "Please fill in all fields!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}
