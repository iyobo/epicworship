/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import com.quvizo.config.EpicSettings;
import com.quvizo.data.PresentationAssetWrapper;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.Presentation;
import com.quvizo.data.tablemodels.LiveTableItem;
import com.quvizo.data.tablemodels.SongTableItem;
import com.quvizo.importers.Exporter;
import com.quvizo.importers.Importer;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.misc.InstructionsDialog;
import com.quvizo.ui.misc.OptionsDialog;
import com.quvizo.universal.UI;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Must contain variables for every UI element that matters in the
 * DirectorTemplate.fxml file. Each variable must have the same name as the
 * corresponding UI element's fx:id.
 *
 * @author bufflogic
 */
public class DirectorViewFX implements Initializable {

    //Main
    public Label livePresentationLabel;
    public MenuItem countdownMenuItem;
    public MenuItem exitMenuItem;
    public MenuItem instructionsMenuItem;
    public MenuItem aboutnMenuItem;
    public MenuItem defaultImportMenuItem;
    public MenuItem epcImportMenuItem;
    public MenuItem epcExportMenuItem;
    public MenuItem MIPowerPoint;
    public MenuItem settingsMenuItem;
    public MenuItem mIEnglish;
    public MenuItem mIPortuguese;
    public MenuItem mICzech;
    //Creation
    public Button creationAddPresBtn;
    public Button creationDelPresBtn;
    public Button creationDelPresItemBtn;
    public ListView<PresentationAssetWrapper> creationPresAssetList;
    public ListView<Presentation> creationPresentationList;
    //Creation.Song
    public TableView<SongTableItem> creationSongTable;
    public TextField creationSongTitleField;
    public TextField creationSongAuthorField;
    public TextArea creationSongBodyField;
    public Button creationSaveSongBtn;
    public Button creationSaveSongPresBtn;
    public Button creationAddSongBtn;
    public Button creationDelSongBtn;
    public Button creationPresentSongBtn;
    public TextField creationSearchSongField;
    //Creation.Media
    public Button creationAddMediaBtn;
    public Button creationDelMediaBtn;
    public Button creationPresentMediaBtn;
    public ListView<Asset> creationMediaList;
    //Creation.Scripture
    public Button creationOpenBibleBtn;
    //Live
    public TableView<LiveTableItem> liveTable;
    public VBox liveMediaControlPane;
    //Live.Background
    public ToggleButton liveMotionBackToggler;
    public Button liveChangeMotionBackBtn;
    public Button liveChangePicBackBtn;
    //Live.Text
    public Button liveOpenBibleBtn;
    public ListView liveTextColor;
    public ListView liveTextShadowColor;
    public TextArea liveQuikTextField;
    public Button liveQuikTextBtn;
    public Button liveClearBtn;
    public ListView liveTextShadowDimmer;
    public ListView liveTextSize;
    public Button BTClearScreen;    
    public Button btLogoScreen;
    public Button btBlackScreen;
    public Button btAlert;
    //forChangeflags
    private Locale defLoc = Locale.getDefault();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CreationTabController.initInstance(this);
        SongTabController.initInstance(this);
        MediaTabController.initInstance(this);
        LiveTabController.initInstance(this);

        updateNowPresentingLabel();

        setupMainMenu();


    }

    void updateNowPresentingLabel() {
        if (livePresentationLabel != null && UI.selectedPresentation != null) {
            livePresentationLabel.setText(EpicSettings.bundle.getString("now.live.init") + UI.selectedPresentation.toString());
        } else {
            livePresentationLabel.setText(EpicSettings.bundle.getString("now.live"));
        }
    }

    private void setupMainMenu() {

        countdownMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                OptionsDialog.open();
            }
        });

        mIEnglish.setGraphic(new ImageView(new Image("/media/Canada.png")));
        mIPortuguese.setGraphic(new ImageView(new Image("/media/Brazil.png")));
        mICzech.setGraphic(new ImageView(new Image("/media/Czech-Republic.png")));

        mIEnglish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {     
               try{
            	   changeLanguage("en","CA");
            	   
               } catch(Exception e){
                   Qui.showMessageBox("OOps", "EpicWorship could not switch language");
                   e.printStackTrace();
               }
            }
        });

        mIPortuguese.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {               
                try{
                	changeLanguage("pt","BR");
               } catch(Exception e){
            	   Qui.showMessageBox("OOps", "EpicWorship could not switch language");
            	   e.printStackTrace();
               }
            }
        });
        
        mICzech.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {               
                try{
                	changeLanguage("cs","CZ");
               } catch(Exception e){
            	   Qui.showMessageBox("OOps", "EpicWorship could not switch language");
            	   e.printStackTrace();
               }
            }
        });

        settingsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Image flag;// = new Image(getClass().getResourceAsStream("/media/Canada.png"));
                try {
                    Parent settings = FXMLLoader.load(getClass().getResource("/fxml/SettingsView.fxml"),EpicSettings.bundle);
                    Scene scene = new Scene(settings);
                    Stage stageSettings = new Stage();
                    stageSettings.setTitle(EpicSettings.bundle.getString("menu.application.settings"));
                    if (defLoc.equals(Locale.forLanguageTag("en-CA"))) {
                        flag = new Image(getClass().getResourceAsStream("/media/Canada.png"));
                    } else if (defLoc.equals(Locale.forLanguageTag("pt-BR"))) {
                        flag = new Image(getClass().getResourceAsStream("/media/Brazil.png"));
                    } else if (defLoc.equals(Locale.forLanguageTag("cs-CZ"))) {
                        flag = new Image(getClass().getResourceAsStream("/media/Czech-Republic.png"));                        
                    } else {
                        flag = new Image(getClass().getResourceAsStream("/media/United States of America.png"));
                    }
                    stageSettings.getIcons().add(flag);
                    stageSettings.centerOnScreen();
                    stageSettings.setResizable(false);
                    stageSettings.setScene(scene);
                    stageSettings.show();
                } catch (Exception e) {
                }
            }
        });

        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int response = JOptionPane.showConfirmDialog(null, EpicSettings.bundle.getString("director.exit.message"), EpicSettings.bundle.getString("director.exit.title"), JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
            }
        });

        instructionsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                InstructionsDialog.launch();
            }
        });

        aboutnMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                InstructionsDialog.launch();
            }
        });

        defaultImportMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Importer.importDefaults();
            }
        });

        epcImportMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Importer.importEPCFiles();
            }
        });

        MIPowerPoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Importer.importPowerPointFiles();
            }
        });

        epcExportMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Exporter.exportEPCFiles();
            }
        });     
    }

	protected void changeLanguage(String language, String country) throws IOException
	{
		File f = new File("./"); 
 	   File[] matchingFiles = f.listFiles(new FilenameFilter() {
 	       public boolean accept(File dir, String name) {
 	           return name.startsWith("epicworship") && name.endsWith("jar");
 	       }
 	   });
        Runtime.getRuntime().exec("java -Duser.language="+language+" -Djavafx.autoproxy.disable=true -Duser.country="+country+" -jar "+matchingFiles[0].getName());
        System.exit(0);
		
	}
}
