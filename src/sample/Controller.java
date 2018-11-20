package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Controller {


    @FXML
    private BorderPane mainBorderPane;


    public void showLoadDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("readFileWindow.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            FileWindowController fileWindowController = fxmlLoader.getController();
            try {
                FileInformation fileInformation = fileWindowController.process();
                DataFrame df = new DataFrame(fileInformation.filePath, fileInformation.columnsTypes, fileInformation.isFirstLineWithNames);
                int k = 0;

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
