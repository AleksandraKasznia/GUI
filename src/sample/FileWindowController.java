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

public class FileWindowController {
    @FXML
    private String filePath;

    @FXML
    private TextField chosenFile;

    @FXML
    private CheckBox isFirstLineWithNames;

    @FXML
    private TextField columnsTypes;

    @FXML
    private TextField columnsNames;

    public void handleKeyReleased()
    {
        String textColumNames = columnsNames.getText();
        columnsNames.setDisable(isFirstLineWithNames.isSelected());
        if(isFirstLineWithNames.isSelected()) columnsNames.setText(null);
    }

    public void setPath()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        filePath = fileChooser.showOpenDialog(new Stage()).getPath();
        chosenFile.setText(filePath);
    }

    public FileInformation process() throws CustomException{
        FileInformation fileInformation = new FileInformation();
        fileInformation.filePath = chosenFile.getText();
        if(fileInformation.filePath==null || fileInformation.filePath.equals("")){
            throw new CustomException("You need to choose a file");
        }
        fileInformation.isFirstLineWithNames = isFirstLineWithNames.isSelected();
            if(columnsNames.getText() != null)
            {
                fileInformation.columnsNames = columnsNames.getText().split(",");
                for( String names : fileInformation.columnsNames)
                    names = names.trim();
            }
            if(!columnsTypes.getText().trim().isEmpty())
            {
                String[] types = columnsTypes.getText().split(",");

                for(int i=0; i<types.length ; i++) {
                    types[i] = types[i].trim();
                    if(types[i].equals("int")) {
                        fileInformation.columnsTypes.add(IntHolder.class);
                    }
                    else{
                        if (types[i].equals("double")){
                                fileInformation.columnsTypes.add(DoubleHolder.class);
                        }
                        else{
                            if (types[i].equals("date")) {
                                fileInformation.columnsTypes.add(DateTimeHolder.class);
                            }
                            else{
                                if (types[i].equals("float")) {
                                    fileInformation.columnsTypes.add(FloatHolder.class);
                                }
                                else{
                                    if (types[i].equals("string")) {
                                        fileInformation.columnsTypes.add(StringHolder.class);
                                    }
                                    else throw new CustomException("You misspelled type");
                                }
                            }
                        }
                    }
                }
            }
            else throw new CustomException("You forgot to type the types");
        return fileInformation;
    }

}
