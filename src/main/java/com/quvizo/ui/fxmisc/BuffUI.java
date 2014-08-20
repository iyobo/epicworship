/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.fxmisc;

import com.quvizo.ui.misc.TextDialog;
import com.quvizo.universal.EpicOverlord;
import java.lang.String;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A goody bag of uis. full of static methods.
 * @author BuffLogic
 */
public class BuffUI {

    private static Logger logger = Logger.getLogger(BuffUI.class.getName());

    public static void showMessageBox(String title, String body) {
        final Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(title);
        stage.initOwner(EpicOverlord.primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        Group uiGroup = new Group();
        Scene scene = new Scene(uiGroup);//, 500, 250);



        stage.setScene(scene);

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        uiGroup.getChildren().add(box);

        Label bodyLabel = new Label();
        bodyLabel.setMaxWidth(500);
        bodyLabel.setWrapText(true);
        bodyLabel.setText(body);
        box.getChildren().add(bodyLabel);

        Button okButton = new Button("Ok");
        box.getChildren().add(okButton);

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        stage.sizeToScene();



        stage.show();
    }

    public static String showSwingTextInput(String title, String body) {
        TextDialog td = new TextDialog(null);
        String res = td.showTextInput(title, body, "");

        td.dispose();

        return res;
    }

    public static int INPUTTYPESINGLELINE = 1;
    public static int INPUTTYPEMULTILINE = 2;
    
    public static void showTextInput(String title, String instructions, BuffUIAction onOkAction)
    {
        showTextInput(1, title, new String[]{instructions}, new int[]{BuffUI.INPUTTYPESINGLELINE}, onOkAction);
    }
    
    /**
     * Pure freaking Genius.
     * @param inputcount
     * @param title
     * @param instructions
     * @param inputTypes
     * @param onOkAction 
     */
    public static void showTextInput(int inputcount, String title, String[] instructions, int[] inputTypes, final BuffUIAction onOkAction) {
        if (instructions == null || inputTypes==null) {
            logger.severe("Programmer Error: Coder needs to be sure that neither instructions nor inputtypes are null");
            return;
        }

        if (inputcount != instructions.length || inputcount != inputTypes.length) {
            logger.severe("Programmer Error: Coder needs to be sure there are as much instructions and inputtype definitions as there are input boxes.");
            return;
        }

        final Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(title);
        stage.initOwner(EpicOverlord.primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        Group uiGroup = new Group();
        Scene scene = new Scene(uiGroup);//, 500, 250);

        

        stage.setScene(scene);

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        uiGroup.getChildren().add(box);

        //for each input count
        TextInputControl[] inputs = new TextInputControl[inputcount];
        for (int x = 0; x < inputcount; x++) {
            //info text
            Label bodyLabel = new Label();
            bodyLabel.setMaxWidth(500);
            bodyLabel.setWrapText(true);
            bodyLabel.setText(instructions[x]);
            box.getChildren().add(bodyLabel);

            //input box
            TextInputControl input = null;
            if(inputTypes[x]==INPUTTYPEMULTILINE)
            {
                input = new TextArea();
                
            }
            else
            {
                input = new TextField();

            }
            input.setMinWidth(500);
            inputs[x] = input;
            box.getChildren().add(input);

        }
        
        final TextInputControl[] actionInputs = inputs;

        Button okButton = new Button("Ok");
        box.getChildren().add(okButton);

        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                onOkAction.perform(actionInputs,stage);
            }
        });

        stage.sizeToScene();
        stage.show();
    }
}
