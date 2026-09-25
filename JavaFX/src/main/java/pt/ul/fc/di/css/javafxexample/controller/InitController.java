package pt.ul.fc.di.css.javafxexample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class InitController implements ControllerWithModel {

    private DataModel model;

    private Stage stage;

    @FXML
    private Button createButton;

    @FXML
    private Button listButton;

    @FXML
    private MenuItem menuCustomer;

    @FXML
    private MenuItem menuQuit;

    @FXML
    private MenuItem menuProduct;

    @FXML
    private Button productButton;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void createCustomer(ActionEvent event) {
        UtilController.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_customer.fxml",
                "Create Customer", model);
    }

    @FXML
    void createProduct(ActionEvent event) {
        UtilController.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/product.fxml",
                "Create Product", model);
    }

    @FXML
    void listCustomers(ActionEvent event) {
        UtilController.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_customer.fxml",
                "List Customers", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }

}
