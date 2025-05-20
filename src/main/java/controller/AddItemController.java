package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextArea txtItemDescription;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    void btnAddItemOnClick(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO item VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, txtItemCode.getText());
            preparedStatement.setString(2, txtItemName.getText());
            preparedStatement.setString(3, txtItemDescription.getText());
            preparedStatement.setDouble(4, Double.parseDouble(txtUnitPrice.getText()));
            preparedStatement.setDouble(5, Double.parseDouble(txtQtyOnHand.getText()));
            int i = preparedStatement.executeUpdate();
            if(i > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Added Successfully...");
                alert.showAndWait();
                clearFields();
                txtItemCode.setText(genereateItemCode());
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Cant add this item...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnClick(ActionEvent event) {
        clearFields();
    }

    public String genereateItemCode(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT COUNT(*) FROM item";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int rowsCount =  resultSet.getInt(1);
                String formattedId = String.format("ITEM%04d", rowsCount+1);
                return formattedId;
            }else{
                return "ITEM0001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtItemCode.setEditable(false);
        txtItemCode.setText(genereateItemCode());
    }

    public void clearFields(){
        txtItemName.clear();
        txtItemDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }
}
