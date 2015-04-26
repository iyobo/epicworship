/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.fxmisc;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.quvizo.EpicWorship;
import com.quvizo.universal.EpicOverlord;
import com.quvizo.universal.ProjectorConstants;
import com.quvizo.universal.UI;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A goody bag of uis. full of static methods.
 *
 * @author BuffLogic
 */
public class Qui {

    private static Logger logger = Logger.getLogger(Qui.class.getName());

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
    public static int INPUTTYPESINGLELINE = 1;
    public static int INPUTTYPEMULTILINE = 2;

    public static void showTextInput(String title, String instructions, BuffUIAction onOkAction) {
        showTextInput(1, title, new String[]{instructions}, new int[]{Qui.INPUTTYPESINGLELINE}, onOkAction);
    }

    /**
     * Pure freaking Genius.
     *
     * @param inputcount
     * @param title
     * @param instructions
     * @param inputTypes
     * @param onOkAction
     */
    public static void showTextInput(int inputcount, String title, String[] instructions, int[] inputTypes, final BuffUIAction onOkAction) {
        if (instructions == null || inputTypes == null) {
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
            if (inputTypes[x] == INPUTTYPEMULTILINE) {
                input = new TextArea();

            } else {
                input = new TextField();

            }
            input.setMinWidth(500);
            inputs[x] = input;          
            box.getChildren().add(input);

        }

        final TextInputControl[] actionInputs = inputs;

        Button okButton = new Button("Ok");
        box.getChildren().add(okButton);

        //Enter to confirm dialog message
        box.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    onOkAction.perform(actionInputs, stage);
                }
            }
        });
        
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onOkAction.perform(actionInputs, stage);
            }
        });

        stage.sizeToScene();
        stage.show();
    }

    /**
     * Launches an FXML with an associated controller (defined in FXML). The
     * controller class MUST extend FXMLController.
     *
     * @param title
     * @param path
     * @param stagestyle
     * @param modality
     * @throws IOException
     */
    public static void launchFXMLWindow(String title, String path, StageStyle stagestyle, Modality modality) throws IOException {
        final Stage stage = new Stage(stagestyle);
        if (title != null) {
            stage.setTitle(title);
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EpicWorship.class.getResource(path));
        
//        FXMLLoader loader = new FXMLLoader(stage.getClass().getResource(path));
        Scene scene = new Scene((Parent) loader.load());

        stage.initModality(modality);
        stage.setScene(scene);
        stage.getIcons().add(EpicWorship.icon);
        stage.centerOnScreen();

        ((FXMLController) loader.getController()).stage = stage;
        ((FXMLController) loader.getController()).postInit();


        if (modality == Modality.APPLICATION_MODAL) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    //show screen picker
    public static void showScreenPicker() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ObservableList<Screen> gs = Screen.getScreens();

        if (gs.size() == 1) {
            //No need to show monitor picker if only one monitor
            Rectangle2D devicebounds = gs.get(0).getBounds();
            ProjectorConstants.SCREENX = devicebounds.getMinX();
            ProjectorConstants.SCREENY = devicebounds.getMinY();
            ProjectorConstants.SCREENWIDTH = devicebounds.getWidth();
            ProjectorConstants.SCREENHEIGHT = devicebounds.getHeight();
            return;
        }

        //okay lets have them choose a screen seeing as they have more than 1
        try {
            Qui.launchFXMLWindow("EpicWorship: Choose a Screen to Project to", "/fxml/ScreenPicker.fxml", StageStyle.UNDECORATED, Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static Rectangle scale(Rectangle2D me, Rectangle max, float tow, float toh) {
        float horifact = tow / (float) max.width;
        float vertifact = toh / (float) max.height;

        float minx = max.x;
        float maxx = max.x + max.width;
        float mintox = 0;
        float maxtox = tow;

        float miny = max.y;
        float maxy = max.y + max.height;
        float mintoy = 0;
        float maxtoy = toh;


        Rectangle result = new Rectangle();
        result.x = (int) scaleNumber((float) me.getMinX(), minx, maxx, mintox, maxtox);
        //result.x = (int) (horifact * me.x)+(min.x);
        result.width = (int) (me.getWidth() * horifact);
        result.y = (int) scaleNumber((float) me.getMinY(), miny, maxy, mintoy, maxtoy);
        result.height = (int) (me.getHeight() * vertifact);

        return result;
    }

    /**
     *
     * @param number -900
     * @param from_min -1000
     * @param from_max 1500
     * @param to_max 800
     * @param to_min 0
     * @return
     */
    private static float scaleNumber(float number, float from_min, float from_max, float to_min, float to_max) {
        // return ((to_max - to_min) * (number - from_min)) / (from_max - from_min) + to_min;
        //return (number - from_min) * (to_max - to_min) / (from_max - from_min) + to_min;
        float ret = 0;

        ret = Math.abs((from_min - number) / (from_max - from_min)); //percentage distance of value from min/start

        ret = ret * (to_max - to_min);
        return ret;
    }

    public static Rectangle Ract2DtoRect(Rectangle2D rect2d) {
        Rectangle r = new Rectangle();
        r.width = (int) rect2d.getWidth();
        r.height = (int) rect2d.getHeight();
        r.x = (int) rect2d.getMinX();
        r.y = (int) rect2d.getMinY();

        return r;
    }

    static Rectangle getMinimumBounds(List<Screen> devices) {
        Rectangle result = new Rectangle();

        for (Screen d : devices) {
            Rectangle bounds = Ract2DtoRect(d.getBounds());
            result = result.union(bounds);
        }

        return result;
    }

    /**
     * scale image
     *
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     *
     * @return scaled image
     */
    public static BufferedImage scaleImage(BufferedImage sbi, int imageType, int dWidth, int dHeight) {
        return scaleImage(sbi, imageType, dWidth, dHeight, 1, 1);
    }

    /**
     * scale image
     *
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @param fWidth x-factor for transformation / scaling
     * @param fHeight y-factor for transformation / scaling
     * @return scaled image
     */
    public static BufferedImage scaleImage(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    public static ImageView createImageView(String resourcepath, float width, float height, EventHandler<MouseEvent> clickevent) {
        ImageView imageView = new ImageView(new Image(resourcepath));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);

        if (clickevent != null) {
            imageView.setOnMouseClicked(clickevent);
            imageView.setCursor(Cursor.OPEN_HAND);
        }
        return imageView;
    }
}
