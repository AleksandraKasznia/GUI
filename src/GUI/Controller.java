package GUI;

import dataFrame.DataFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;

public class Controller {


    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TextArea alertsArea;

    @FXML
    ScrollPane spane;



    private DataFrame df;
    private Pane pane=new Pane();

    public DataFrame getData(){
        return df;
    }


    public void showLoadDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("readFileWindow.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
                String error = e.getMessage();
                addAlert(error);
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            FileWindowController fileWindowController = fxmlLoader.getController();
            try {
                FileInformation fileInformation = fileWindowController.process();
                df = new DataFrame(fileInformation.filePath, fileInformation.columnsTypes, fileInformation.isFirstLineWithNames);

                printDataFrame(df);

            } catch (Exception e) {
                try {
                    String error = e.getCause().getMessage();
                    addAlert(error);
                }catch (Exception b){
                    String error = e.getMessage();
                    addAlert(error);
                }

            }

        }
    }

    public void printDataFrame(DataFrame toPrint){
        for(int columnIterator=0; columnIterator<toPrint.numberOfColumns; columnIterator++){
            TextField value;
            value = new TextField(toPrint.getName(columnIterator));
            value.setMaxWidth(98);
            value.setLayoutX(98 * columnIterator);
            value.setLayoutY(1);
            pane.getChildren().add(value);

            for(int rowIterator=0; rowIterator<toPrint.size(); rowIterator++){
                if(toPrint.getRecord(columnIterator,rowIterator)!=null) {
                    value = new TextField(toPrint.getRecord(columnIterator, rowIterator).toString());
                }
                else{
                    value = new TextField("Unavailable");
                }
                value.setMaxWidth(98);
                value.setLayoutX(98 * columnIterator);
                value.setLayoutY(27 * (rowIterator + 1));
                pane.getChildren().add(value);
            }
        }
        spane.setContent(pane);
    }

    public void max(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().max());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void min(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().min());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void mean(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().mean());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void var(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().var());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void std(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().std());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void sum(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().sum());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void median(){
        try {
            pane.getChildren().clear();
            printDataFrame(df.groupby().median());
        }catch(Exception e){
            String error = e.getMessage();
            addAlert(error);
        }
    }

    public void data(){
        pane.getChildren().clear();
        printDataFrame(df);
    }

    private void addAlert(String toAdd)
    {
        alertsArea.setText(toAdd);
    }

    public void getLineChartWindow(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("lineChartWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            String error = e.getMessage();
            addAlert(error);
        }


        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        LineChartWindowController controller = fxmlLoader.getController();
        controller.df = new DataFrame(this.df);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){

        }
    }

    public void getScatterChartWindow(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("scatterChartWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            String error = e.getMessage();
            addAlert(error);
        }


        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        ScatterChartController controller = fxmlLoader.getController();
        controller.df = new DataFrame(this.df);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){

        }
    }

    public void getBarChartWindow(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("barChartWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            String error = e.getMessage();
            addAlert(error);
        }


        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        BarChartWindowController controller = fxmlLoader.getController();
        controller.df = new DataFrame(this.df);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){

        }
    }

}
