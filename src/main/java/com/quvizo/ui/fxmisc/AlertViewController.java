package com.quvizo.ui.fxmisc;

import com.quvizo.ui.ProjectorView;
import com.quvizo.universal.ProjectorConstants;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Ítalo C. Zaina
 */
public class AlertViewController implements Initializable {

    public ChoiceBox<String> boxNumTimes;
    public Button btCancel;
    public Button btProject;
    public ColorPicker colorPckBG;
    public ColorPicker colorPckFont;
    public TextArea txtAreaAlert;
    public Timeline textMove;
    public Timeline retOUT;
    public Timeline retIN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colorPckFont.setValue(Color.WHITE);
        colorPckBG.setValue(Color.BLACK);
        boxNumTimes.setValue("3");

        btProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //Start test to alert message
                ProjectorView.getInstance().getTextAlert().setTranslateX(ProjectorConstants.SCREENWIDTH);
                ProjectorView.getInstance().getTextAlert().setFill(colorPckFont.getValue());
                ProjectorView.getInstance().getTextAlert().setVisible(true);
                ProjectorView.getInstance().getBgAlert().setFill(colorPckBG.getValue());
                ProjectorView.getInstance().getBgAlert().setVisible(true);
                ProjectorView.getInstance().getTextAlert().setText(txtAreaAlert.getText());
                int speedTime = 20 * (int) ProjectorView.getInstance().getTextAlert().getLayoutBounds().getWidth();
                if (speedTime < 9000) {
                    speedTime = 9000;
                }
                retIN = new Timeline();
                retIN.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, // Here move retangle to down
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateXProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateYProperty(), - (ProjectorConstants.SCREENHEIGHT/10)),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().opacityProperty(), 0.5)),
                        new KeyFrame(new Duration(500), // Final position (High top)
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateXProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateYProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().opacityProperty(), 1)));
                retIN.play();

                textMove = new Timeline();
                textMove.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO,
                        new KeyValue(ProjectorView.getInstance().getTextAlert().opacityProperty(), 1),
                        new KeyValue(ProjectorView.getInstance().getTextAlert().translateXProperty(), ProjectorConstants.SCREENWIDTH)),
                        new KeyFrame(new Duration(speedTime), // Move text on speed (9000 > or size*20)                        
                        new KeyValue(ProjectorView.getInstance().getTextAlert().translateXProperty(), 0 - ProjectorView.getInstance().getTextAlert().getLayoutBounds().getWidth())));

                retIN.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        textMove.setCycleCount(Integer.parseInt(boxNumTimes.getValue().toString()));
                        textMove.play();
                    }
                });


                retOUT = new Timeline();
                retOUT.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, // Here move retangle to up
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateXProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateYProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().opacityProperty(), 1)),
                        new KeyFrame(new Duration(500), // Vanish objects and finish the Alert
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateXProperty(), 0),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().translateYProperty(), - (ProjectorConstants.SCREENHEIGHT/10)),
                        new KeyValue(ProjectorView.getInstance().getBgAlert().opacityProperty(), 0.5)));

                textMove.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        retOUT.play();
                    }
                });
                //End test
            }
        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //TODO
                retIN.stop();
                retOUT.stop();
                textMove.stop();
                ProjectorView.getInstance().getTextAlert().setVisible(false);
                ProjectorView.getInstance().getBgAlert().setVisible(false);
            }
        });
    }
}
