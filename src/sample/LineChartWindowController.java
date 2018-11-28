package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class LineChartWindowController {

    @FXML
    private LineChart <String, Number> lineChart;

    @FXML
    private TextField xValues;

    @FXML
    private TextField yValues;

    public DataFrame df;

    public void draw(){

        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String x = xValues.getText().trim();
        String y = yValues.getText().trim();

        if(!x.isEmpty() && !y.isEmpty()){
            series.setName(x);
            try {
                ArrayList columnx = df.get(x);
                ArrayList columny = df.get(y);

                for (int i = 0; i < columnx.size(); i++){
                    series.getData().add(new XYChart.Data<>(columnx.get(i).toString(),(Number)((Value)columny.get(i)).getValue()));
                }
                lineChart.getData().add(series);
            }catch(Exception e){

            }
        }






    }
}
