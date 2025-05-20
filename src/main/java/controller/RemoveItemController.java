package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import model.Item;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RemoveItemController implements Initializable {
    @FXML
    private JFXButton btnRemove;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtSearchItem;

    @FXML
    private JFXTextField txtUnitPrice;



    @FXML
    void btnCancelOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        String itemCode = txtItemCode.getText();
        if(itemCode != null){
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                String sql = "DELETE FROM item WHERE code = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, itemCode);
                int i = preparedStatement.executeUpdate();
                if(i > 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully...");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        ArrayList<Item> itemArrayList = copyDataToArrayList();
        boolean found = false;
        for (Item item : itemArrayList){
            if(item.getCode().equalsIgnoreCase(txtSearchItem.getText()) || item.getName().equalsIgnoreCase(txtSearchItem.getText())){
                txtItemCode.setText(item.getCode());
                txtName.setText(item.getName());
                txtDescription.setText(item.getDescription());
                txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                found = true;
                break;
            }
        };

        if(!found){
            txtSearchItem.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cannot find "+txtSearchItem.getText());
            alert.showAndWait();
        }
    }

    public ArrayList<Item> copyDataToArrayList(){
        ArrayList<Item>itemArrayList = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM item";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()){
                itemArrayList.add(new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                ));
            }
            return itemArrayList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtName.setEditable(false);
        txtItemCode.setEditable(false);
        txtDescription.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setEditable(false);

    }
}
