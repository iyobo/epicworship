/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.ui.fxmisc;

import com.quvizo.config.EpicSettings;
import com.quvizo.util.FontComboBoxCellFactory;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Ítalo C. Zaina
 */
public class SettingsViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public CheckBox cBSettings;
    public CheckBox cBHotkeys;
    public CheckBox cbSongName;
    public TextField tFVideoBGSettings;
    public TextField tFImageBGSettings;
    public TextField tFF1videoPath;
    public TextField tFF2videoPath;
    public TextField tFF3videoPath;
    public TextField tFF4videoPath;
    public TextField tFF5imagePath;
    public TextField tFF6imagePath;
    public TextField tFF7imagePath;
    public TextField tFF8imagePath;
    public Button bTVideoBGSettings;
    public Button bTImageBGSettings;
    public Button bTF1ChangeVideo;
    public Button bTF2ChangeVideo;
    public Button bTF3ChangeVideo;
    public Button bTF4ChangeVideo;
    public Button bTF5ChangeImage;
    public Button bTF6ChangeImage;
    public Button bTF7ChangeImage;
    public Button bTF8ChangeImage;
    public Button bTSaveSettings;
    public ComboBox fontComboBox;
    FileChooser chooser = new FileChooser();
    public String fonttype = "System";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cBSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                CheckBoxDefault();
            }
        });
        cBHotkeys.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                CheckBoxHotkey();
            }
        });
        bTImageBGSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeImage();
            }
        });
        bTVideoBGSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeVideo();
            }
        });
        bTF1ChangeVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeVideoF1();
            }
        });
        bTF2ChangeVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeVideoF2();
            }
        });
        bTF3ChangeVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeVideoF3();
            }
        });
        bTF4ChangeVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeVideoF4();
            }
        });
        bTF5ChangeImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeImageF5();
            }
        });
        bTF6ChangeImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeImageF6();
            }
        });
        bTF7ChangeImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeImageF7();
            }
        });
        bTF8ChangeImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChangeImageF8();
            }
        });
        bTSaveSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveSettings();
            }
        });
        tFVideoBGSettings.setText(EpicSettings.getScreenVideoPath());
        tFImageBGSettings.setText(EpicSettings.getScreenImagePath());
        if (EpicSettings.getLiveTitleSong().equalsIgnoreCase("no")) cbSongName.setSelected(true);
        if (EpicSettings.getSystemDefault().equalsIgnoreCase("yes")) {
            cBSettings.setSelected(true);
        } else {
            cBSettings.setSelected(false);
            bTVideoBGSettings.setDisable(false);
            bTImageBGSettings.setDisable(false);
            cBHotkeys.setDisable(false);
            fonttype = EpicSettings.getFontType();            
            if (EpicSettings.getLiveHotkeys().equalsIgnoreCase("yes")) {
                cBHotkeys.setSelected(true);
                bTF1ChangeVideo.setDisable(false);
                bTF2ChangeVideo.setDisable(false);
                bTF3ChangeVideo.setDisable(false);
                bTF4ChangeVideo.setDisable(false);
                bTF5ChangeImage.setDisable(false);
                bTF6ChangeImage.setDisable(false);
                bTF7ChangeImage.setDisable(false);
                bTF8ChangeImage.setDisable(false);
                tFF1videoPath.setText(EpicSettings.getLiveF1Video());
                tFF2videoPath.setText(EpicSettings.getLiveF2Video());
                tFF3videoPath.setText(EpicSettings.getLiveF3Video());
                tFF4videoPath.setText(EpicSettings.getLiveF4Video());
                tFF5imagePath.setText(EpicSettings.getLiveF5Image());
                tFF6imagePath.setText(EpicSettings.getLiveF6Image());
                tFF7imagePath.setText(EpicSettings.getLiveF7Image());
                tFF8imagePath.setText(EpicSettings.getLiveF8Image());
            }            
        }
        fontComboBox.setPromptText(fonttype);
        fontsToComboBox(fontComboBox);
        fontComboBox.setCellFactory(new FontComboBoxCellFactory());
        fontComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                fonttype = t1;                
            }    
        });
    }

    public void CheckBoxDefault() {
        if (cBSettings.isSelected()) {
            bTVideoBGSettings.setDisable(true);
            bTImageBGSettings.setDisable(true);
            cBHotkeys.setDisable(true);
            bTF1ChangeVideo.setDisable(true);
            bTF2ChangeVideo.setDisable(true);
            bTF3ChangeVideo.setDisable(true);
            bTF4ChangeVideo.setDisable(true);
            bTF5ChangeImage.setDisable(true);
            bTF6ChangeImage.setDisable(true);
            bTF7ChangeImage.setDisable(true);
            bTF8ChangeImage.setDisable(true);
            fontComboBox.setDisable(true);
        } else {
            bTVideoBGSettings.setDisable(false);
            bTImageBGSettings.setDisable(false);
            cBHotkeys.setDisable(false);
            fontComboBox.setDisable(false);
            if (cBHotkeys.isSelected()) {
                bTF1ChangeVideo.setDisable(false);
                bTF2ChangeVideo.setDisable(false);
                bTF3ChangeVideo.setDisable(false);
                bTF4ChangeVideo.setDisable(false);
                bTF5ChangeImage.setDisable(false);
                bTF6ChangeImage.setDisable(false);
                bTF7ChangeImage.setDisable(false);
                bTF8ChangeImage.setDisable(false);
            } else {
                bTF1ChangeVideo.setDisable(true);
                bTF2ChangeVideo.setDisable(true);
                bTF3ChangeVideo.setDisable(true);
                bTF4ChangeVideo.setDisable(true);
                bTF5ChangeImage.setDisable(true);
                bTF6ChangeImage.setDisable(true);
                bTF7ChangeImage.setDisable(true);
                bTF8ChangeImage.setDisable(true);
            }
        }
    }

    public void CheckBoxHotkey() {
        if (cBHotkeys.isSelected()) {
            bTF1ChangeVideo.setDisable(false);
            bTF2ChangeVideo.setDisable(false);
            bTF3ChangeVideo.setDisable(false);
            bTF4ChangeVideo.setDisable(false);
            bTF5ChangeImage.setDisable(false);
            bTF6ChangeImage.setDisable(false);
            bTF7ChangeImage.setDisable(false);
            bTF8ChangeImage.setDisable(false);
        } else {
            bTF1ChangeVideo.setDisable(true);
            bTF2ChangeVideo.setDisable(true);
            bTF3ChangeVideo.setDisable(true);
            bTF4ChangeVideo.setDisable(true);
            bTF5ChangeImage.setDisable(true);
            bTF6ChangeImage.setDisable(true);
            bTF7ChangeImage.setDisable(true);
            bTF8ChangeImage.setDisable(true);
        }
    }

    public void ChangeVideo() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            //##### test of fullpath of video to put on default .properties file  
            tFVideoBGSettings.setText(file.toURI().toString());
        }
    }

    public void ChangeImage() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            tFImageBGSettings.setText(file.toURI().toString());
        }
    }

    public void ChangeVideoF1() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            //##### test of fullpath of video to put on default .properties file  
            tFF1videoPath.setText(file.toURI().toString());
        }
    }

    public void ChangeVideoF2() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            //##### test of fullpath of video to put on default .properties file  
            tFF2videoPath.setText(file.toURI().toString());
        }
    }

    public void ChangeVideoF3() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            //##### test of fullpath of video to put on default .properties file  
            tFF3videoPath.setText(file.toURI().toString());
        }
    }

    public void ChangeVideoF4() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            //##### test of fullpath of video to put on default .properties file  
            tFF4videoPath.setText(file.toURI().toString());
        }
    }

    public void ChangeImageF5() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            tFF5imagePath.setText(file.toURI().toString());
        }
    }

    public void ChangeImageF6() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            tFF6imagePath.setText(file.toURI().toString());
        }
    }

    public void ChangeImageF7() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            tFF7imagePath.setText(file.toURI().toString());
        }
    }

    public void ChangeImageF8() {
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            tFF8imagePath.setText(file.toURI().toString());
        }
    }

        private void fontsToComboBox(ComboBox<String> fontComboBox) {
        List<String> fontNames = Font.getFontNames();
        ObservableList<String> items = fontComboBox.getItems();
        for(String fontName: fontNames){            
            items.add(fontName);
        }
    }
    
    public void SaveSettings() {
        String title = "yes";
        String hotkeys = "no";
        String f1 = "";
        String f2 = "";
        String f3 = "";
        String f4 = "";
        String f5 = "";
        String f6 = "";
        String f7 = "";
        String f8 = "";
        if(cbSongName.isSelected()){
            title = "no";
        }
        if (cBSettings.isSelected()) {
            EpicSettings.gravaPropriedade("yes", "", "", hotkeys, f1, title, f2, f3, f4, f5, f6, f7, f8, "Arial");
            bTSaveSettings.setText(EpicSettings.bundle.getString("settingsview.saved"));
        } else {
            if (cBHotkeys.isSelected()) {
                hotkeys = "yes";
                f1 = tFF1videoPath.getText();
                f2 = tFF2videoPath.getText();
                f3 = tFF3videoPath.getText();
                f4 = tFF4videoPath.getText();
                f5 = tFF5imagePath.getText();
                f6 = tFF6imagePath.getText();
                f7 = tFF7imagePath.getText();
                f8 = tFF8imagePath.getText();
            }
            if (tFVideoBGSettings.getText().equalsIgnoreCase("")) {
                bTSaveSettings.setText(EpicSettings.bundle.getString("settingsview.notsaved.video"));
            } else if (tFImageBGSettings.getText().equalsIgnoreCase("")) {
                bTSaveSettings.setText(EpicSettings.bundle.getString("settingsview.notsaved.image"));
            } else {
                EpicSettings.gravaPropriedade("no", tFVideoBGSettings.getText(), tFImageBGSettings.getText(), hotkeys, f1, title, f2, f3, f4, f5, f6, f7, f8, fonttype);
                bTSaveSettings.setText(EpicSettings.bundle.getString("settingsview.saved"));
            }
        }
    }
}
