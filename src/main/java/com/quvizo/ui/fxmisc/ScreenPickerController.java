/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.ui.fxmisc;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;

import com.quvizo.universal.ProjectorConstants;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author bufflogic
 */
public class ScreenPickerController extends FXMLController implements Initializable {

    public Pane canvas;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    }
    
    @FXML
    private void quit(ActionEvent event) {
    	System.exit(0);
    
    }

	@Override
	public void postInit()
	{
		 //Scale max bounds
        ObservableList<Screen> gs = Screen.getScreens();
        Rectangle maxbounds = Qui.getMinimumBounds(gs);
        
        Font font = Font.font("System", 18);
        

        //Check how many monitors we have and put representative buttons on screen. 
        for (Screen s: gs) {
        	final Rectangle2D devicebounds = s.getBounds();
        	
        	StackPane screenitem = new StackPane();
        	screenitem.setCursor(Cursor.OPEN_HAND);
        	
        	Label label = new Label();
        	label.setTextFill(Color.WHITE);
        	label.setFont(font);
        	label.setText("Screen");                     
            if (s.equals(Screen.getPrimary())) {
            	label.setText("Main Screen");
            }
            
            //scale
            Rectangle b = Qui.scale(devicebounds, maxbounds, (float)600,(float)310);
            
            final javafx.scene.shape.Rectangle back = new javafx.scene.shape.Rectangle();
            screenitem.setLayoutX(b.getMinX());
            screenitem.setLayoutY(b.getMinY());
            back.setWidth(b.getWidth());
            back.setHeight(b.getHeight());
            
            back.setFill(Color.web("66a3ff"));
            back.setStroke(Color.WHITE);
            back.setStrokeWidth(6);
            back.setStrokeType(StrokeType.INSIDE);
            back.setEffect(new DropShadow());

            screenitem.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ProjectorConstants.SCREENX = devicebounds.getMinX();
                    ProjectorConstants.SCREENY = devicebounds.getMinY();
                    ProjectorConstants.SCREENWIDTH = devicebounds.getWidth();
                    ProjectorConstants.SCREENHEIGHT = devicebounds.getHeight();
                    stage.hide();
                }
            });
            
            screenitem.setOnMouseEntered(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        back.setFill(Color.BLUEVIOLET);
                    }
                });
            screenitem.setOnMouseExited(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                    	back.setFill(Color.web("66a3ff"));
                    }
                });
            
            screenitem.getChildren().add(back);
            screenitem.getChildren().add(label);
            canvas.getChildren().add(screenitem);
        }    
		
	}
}
