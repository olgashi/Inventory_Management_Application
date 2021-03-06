package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class modifyPartViewController implements Initializable {

    @FXML
    private RadioButton modifyPartInHouseRadioButton;
    @FXML
    private RadioButton modifyPartOutSourcedRadioButton;
    @FXML
    private Button modifyPartViewSaveButton;
    @FXML
    private Button modifyPartViewCancelButton;
    @FXML
    private TextField modifyPartIdTextField;
    @FXML
    private TextField modifyPartNameTextField;
    @FXML
    private TextField modifyPartPriceCostTextField;
    @FXML
    private TextField modifyPartMaxTextField;
    @FXML
    private TextField modifyPartMinTextField;
    @FXML
    private TextField modifyPartInventoryTextField;
    @FXML
    private TextField modifyPartCompanyNameMachineIDTextField;
    @FXML
    private Label modifyPartIdLabel;
    @FXML
    private Label modifyPartNameLabel;
    @FXML
    private Label modifyPartPriceCostLabel;
    @FXML
    private Label modifyPartMaxLabel;
    @FXML
    private Label modifyPartMinLabel;
    @FXML
    private Label modifyPartInventoryLabel;
    @FXML
    private Label modifyPartCompanyNameMachineIDLabel;

    private ToggleGroup modifyPartTypeToggleGroup;

    public static Part selectedPart;

    // prepopulate text fields with data for selected part
    public void initModifyPartData(Part part) {
        selectedPart = part;
        modifyPartIdTextField.setText(Integer.toString(selectedPart.getId()));
        modifyPartNameTextField.setText(selectedPart.getName());
        modifyPartInventoryTextField.setText(Integer.toString(selectedPart.getStock()));
        modifyPartPriceCostTextField.setText(Double.toString(selectedPart.getPrice()));
        modifyPartMaxTextField.setText(Integer.toString(selectedPart.getMax()));
        modifyPartMinTextField.setText(Integer.toString(selectedPart.getMin()));

        if (part instanceof InHouse) {
            modifyPartInHouseRadioButton.setSelected(true);
            inHouseRadioButtonSelected();
            modifyPartCompanyNameMachineIDTextField.setText(Integer.toString(((InHouse) part).getMachineId()));
        } else {
            modifyPartOutSourcedRadioButton.setSelected(true);
            outsourcedRadioButtonSelected();
            modifyPartCompanyNameMachineIDTextField.setText(((Outsourced) part).getCompanyName());
        }
    }

    // exit to main window
    public void changeSceneMainWindowView(ActionEvent event) throws IOException {
        Parent mainWindowViewParent = FXMLLoader.load(getClass().getResource("mainWindowView.fxml"));
        Scene modifyPartViewScene = new Scene(mainWindowViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(modifyPartViewScene);
        window.show();
    }

    // sets partCompanyNameMachineIDLabel label to Machine ID if InHouse radiobutton is selected
    public void inHouseRadioButtonSelected() {
        modifyPartCompanyNameMachineIDLabel.setText("Machine ID");
    }

    // sets partCompanyNameMachineIDLabel label to Company Name if Outsourced radiobutton is selected
    public void outsourcedRadioButtonSelected() {
        modifyPartCompanyNameMachineIDLabel.setText("Company Name");
    }

    // save modified part and return to main window
    public void modifyPartSaveButtonClicked(ActionEvent event) throws IOException {
        if (modifyPartOutSourcedRadioButton.isSelected() && selectedPart instanceof InHouse) {
            outsourcedRadioButtonSelected();
            Inventory.addPart(new Outsourced(
                selectedPart.getId(),
                modifyPartNameTextField.getText(),
                Double.parseDouble(modifyPartPriceCostTextField.getText()),
                Integer.parseInt(modifyPartInventoryTextField.getText()),
                Integer.parseInt(modifyPartMinTextField.getText()),
                Integer.parseInt(modifyPartMaxTextField.getText()),
                modifyPartCompanyNameMachineIDTextField.getText()));

            Inventory.deletePart(selectedPart);
        }

        if (modifyPartInHouseRadioButton.isSelected() && selectedPart instanceof Outsourced) {
            inHouseRadioButtonSelected();
            Inventory.addPart(new InHouse(
                selectedPart.getId(),
                modifyPartNameTextField.getText(),
                Double.parseDouble(modifyPartPriceCostTextField.getText()),
                Integer.parseInt(modifyPartInventoryTextField.getText()),
                Integer.parseInt(modifyPartMinTextField.getText()),
                Integer.parseInt(modifyPartMaxTextField.getText()),
                Integer.parseInt(modifyPartCompanyNameMachineIDTextField.getText())));

            Inventory.deletePart(selectedPart);

        } else {
            if (modifyPartInHouseRadioButton.isSelected()) {
                InHouse updatedPart = new InHouse(selectedPart.getId(),
                    modifyPartNameTextField.getText(),
                    Double.parseDouble(modifyPartPriceCostTextField.getText()),
                    Integer.parseInt(modifyPartInventoryTextField.getText()),
                    Integer.parseInt(modifyPartMinTextField.getText()),
                    Integer.parseInt(modifyPartMaxTextField.getText()),
                    Integer.parseInt(modifyPartCompanyNameMachineIDTextField.getText()));
                Inventory.updatePart(selectedPart.getId(), updatedPart);
            }
            if (modifyPartOutSourcedRadioButton.isSelected()) {
                Outsourced updatedPart = new Outsourced(selectedPart.getId(),
                    modifyPartNameTextField.getText(),
                    Double.parseDouble(modifyPartPriceCostTextField.getText()),
                    Integer.parseInt(modifyPartInventoryTextField.getText()),
                    Integer.parseInt(modifyPartMinTextField.getText()),
                    Integer.parseInt(modifyPartMaxTextField.getText()),
                    modifyPartCompanyNameMachineIDTextField.getText());
                Inventory.updatePart(selectedPart.getId(), updatedPart);
            }
        }
        changeSceneMainWindowView(event);
    }

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        modifyPartTypeToggleGroup = new ToggleGroup();
        this.modifyPartInHouseRadioButton.setToggleGroup(modifyPartTypeToggleGroup);
        this.modifyPartOutSourcedRadioButton.setToggleGroup(modifyPartTypeToggleGroup);
        this.modifyPartIdTextField.isDisable();
    }
}
