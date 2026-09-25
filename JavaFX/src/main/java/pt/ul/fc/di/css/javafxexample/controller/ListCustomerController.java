package pt.ul.fc.di.css.javafxexample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.grpc.CustomerResponse;
import pt.ul.fc.di.css.javafxexample.grpc.SaleProductItem;
import pt.ul.fc.di.css.javafxexample.model.Customer;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.util.List;

public class ListCustomerController implements ControllerWithModel {

    private Stage stage;

    private DataModel model;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Button btnRemoveProduct;

    @FXML
    private MenuItem itemBack;

    @FXML
    private Label labelNome;

    @FXML
    private Label labelPhone;

    @FXML
    private ListView<SaleProductItem> labelProducts;

    @FXML
    private Label labelVAT;

    @FXML
    private ListView<Customer> listCustomers;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void back(ActionEvent event) {
        UtilController.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/init.fxml",
                "SaleSys", model);
    }

    private void setCurrentCustomer(Customer customer) {
        labelNome.setText(customer.getNome());
        labelPhone.setText(customer.getPhone());
        labelVAT.setText(customer.getVat());

        try {
            List<SaleProductItem> saleProducts = ApiClient.getProductByVat(customer.getVat());
            labelProducts.getItems().clear();
            labelProducts.getItems().addAll(saleProducts);
            // Formata cada item como "product=XXXX, quantity=Y"
            labelProducts.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(SaleProductItem item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null
                            ? null
                            : "product=" + item.getProductCode() + ", quantity=" + item.getQuantity());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        

        labelPhone.setVisible(true);
        labelNome.setVisible(true);
        labelVAT.setVisible(true);
    }

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        checkAndSetCustomer(model);
        setDoubleClickListener();

        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        setCustomerList(model);
    }

    private void setCustomerList(DataModel model) {
        try {
            List<CustomerResponse> customers = ApiClient.getAllCustomers();

            List<Customer> customerList = customers.stream()
                    .map(c -> new Customer(c.getId(), c.getDesignation(), c.getPhone(), c.getVatNumber()))
                    .toList();

            model.setCustomerList(customerList);
            listCustomers.setItems(model.getCustomerList());
            listCustomers.getSelectionModel().selectedItemProperty()
                    .addListener((obs, oldSelection, newSelection) -> model.setCurrentCustomer(newSelection));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndSetCustomer(DataModel model) {
        if (model.getCurrentCustomer() != null) {
            setCurrentCustomer(model.getCurrentCustomer());
        } else {
            labelNome.setVisible(false);
            labelPhone.setVisible(false);
            labelVAT.setVisible(false);
        }
    }

    private void setDoubleClickListener() {
        listCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Customer selectedCustomer = listCustomers.getSelectionModel().getSelectedItem();
                if (selectedCustomer != null) {
                    setCurrentCustomer(selectedCustomer);
                }
            }
        });
    }
}
