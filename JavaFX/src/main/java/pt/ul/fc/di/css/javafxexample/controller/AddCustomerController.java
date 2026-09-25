package pt.ul.fc.di.css.javafxexample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.model.Customer;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class AddCustomerController implements ControllerWithModel {

    private DataModel model;

    private String originalVat;

    private Stage stage;

    @FXML
    private Button btnAdd;

    @FXML
    private Menu itemBack;

    @FXML
    private TextField fieldPhone;

    @FXML
    private TextField fieldVAT;

    @FXML
    private TextField fieldNome;

    @FXML
    private Label labelAddSuccess;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelNome;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelVAT;

    @FXML
    void addCustomer(ActionEvent event) {

        String nome = fieldNome.getText().trim();
        String phone = fieldPhone.getText().trim();
        String vat = fieldVAT.getText().trim();

        if (!validInputs(nome, phone, vat)) {
            labelAddSuccess.setVisible(false);
            labelStatus.setText("✘");
            return;
        }

        Customer currentCustomer = model.getCurrentCustomer();

        try {
            if (currentCustomer != null && vat.equals(originalVat)) {
                // VAT inalterado → actualizar cliente (não implementado)
                labelStatus.setText("Customer updated ✔");
            } else {
                ApiClient.createCustomer(vat, nome, phone);
                ApiClient.createSale(vat);
                labelStatus.setText("New customer added ✔");
            }

            Customer newCustomer = new Customer(0, nome, phone, vat);
            model.setCurrentCustomer(newCustomer);

            unbindFields();

            labelNome.textProperty().bindBidirectional(fieldNome.textProperty());
            labelPhone.textProperty().bindBidirectional(fieldPhone.textProperty());
            labelVAT.textProperty().bindBidirectional(fieldVAT.textProperty());

            labelAddSuccess.setVisible(true);
            labelNome.setVisible(true);
            labelPhone.setVisible(true);
            labelVAT.setVisible(true);
            labelStatus.setVisible(true);

            originalVat = vat;
            fieldVAT.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals(originalVat)) {
                    labelStatus.setText("Updating customer 🔄");
                } else {
                    labelStatus.setText("Adding customer 🆕");
                }
            });

        } catch (Exception e) {
            unbindFields();

            labelNome.setText("Error");
            labelPhone.setText("Error");
            labelVAT.setText("Error");
            labelStatus.setText("✘");

            labelNome.setVisible(true);
            labelPhone.setVisible(true);
            labelVAT.setVisible(true);
            labelStatus.setVisible(true);

            labelAddSuccess.setVisible(false);
            e.printStackTrace();
        }
    }

    private void unbindFields() {
        labelVAT.textProperty().unbindBidirectional(fieldVAT.textProperty());
        labelPhone.textProperty().unbindBidirectional(fieldPhone.textProperty());
        labelNome.textProperty().unbindBidirectional(fieldNome.textProperty());
    }

    private boolean validInputs(String nome, String phone, String vat) {
        boolean isValid = true;
        if (nome.isEmpty()) {
            labelNome.textProperty().unbindBidirectional(fieldNome.textProperty());

            labelNome.setText("Name required");
            labelNome.setVisible(true);
            isValid = false;
        }
        if (phone.isEmpty()) {
            labelPhone.textProperty().unbindBidirectional(fieldPhone.textProperty());
            labelPhone.setText("Phone required");
            labelPhone.setVisible(true);
            isValid = false;
        }
        if (vat.isEmpty()) {
            labelVAT.textProperty().unbindBidirectional(fieldVAT.textProperty());
            labelVAT.setText("VAT required");
            labelVAT.setVisible(true);
            isValid = false;
        }
        if (vat.length() != 9) {
            labelVAT.textProperty().unbindBidirectional(fieldVAT.textProperty());
            labelVAT.setText("VAT must be 9 digits");
            labelVAT.setVisible(true);
            isValid = false;
        }

        return isValid;
    }

    @FXML
    public void initialize() {
        labelAddSuccess.setVisible(false);
        labelNome.setVisible(false);
        labelPhone.setVisible(false);
        labelVAT.setVisible(false);
        labelStatus.setVisible(false);
    }

    @FXML
    void back(ActionEvent event) {
        UtilController.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/init.fxml",
                "SaleSys", model);
    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
