package GUI;

import dataFrame.DataFrame;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import value.Value;

import java.util.ArrayList;

public class ScatterChartController {
    @FXML
    private ScatterChart<String, Number> scatterChart;

    @FXML
    private TextField xValues;

    @FXML
    private TextField yValues;

    public DataFrame df;

    public void draw(){

        scatterChart.getData().clear();
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
                scatterChart.getData().add(series);
            }catch(Exception e){

            }
        }






    }
}
