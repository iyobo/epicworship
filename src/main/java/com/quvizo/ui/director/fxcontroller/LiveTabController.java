/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import com.quvizo.config.EpicSettings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import com.quvizo.data.PresentationAssetWrapper;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.data.tablemodels.LiveTableItem;
import com.quvizo.projector.DynamicBackground;
import com.quvizo.projector.ImageBackground;
import com.quvizo.projector.pages.PXSongPage;
import com.quvizo.ui.ProjectorView;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.universal.UI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author BuffLogic
 */
public class LiveTabController extends Tab {

    private static LiveTabController instance = null;
    
    public static LiveTabController getInstance() {
        return instance;
    }

    public static LiveTabController initInstance(DirectorViewFX directorView) {
        if (instance == null) {
            instance = new LiveTabController(directorView);
        }

        return instance;
    }

    private LiveTabController(DirectorViewFX view) {
        this.view = view;
        initComponent();
    }
    private DirectorViewFX view;
    //=======
    FileChooser chooser = new FileChooser();
    MediaControl mediaControl;

    public MediaControl getMediaControl() {
        return mediaControl;
    }

    private void initComponent() {
        //TODO: This should be a user setting
    	File initdir = new File("data/backgrounds/");
        chooser.setInitialDirectory(initdir);

        setupLiveTable();
        setupControlPanel();

        refreshLiveTable();
    }

    /**
     * Populate with LiveTableItems, which spawn PXPages.
     */
    public void refreshLiveTable() {
        if (UI.selectedPresentation != null) {
            List<PresentationAssets> liveAssets = EntityOverlord.getInstance().getEm().createNamedQuery("PresentationAssets.findByPid").setParameter("pid", UI.selectedPresentation.getId()).getResultList();
            List<LiveTableItem> liveItems = new ArrayList<LiveTableItem>();

            for (int x = 0; x < liveAssets.size(); x++) {
                new PresentationAssetWrapper(liveAssets.get(x)).generatePages(liveItems);
            }

            view.liveTable.getItems().clear();
            view.liveTable.getItems().addAll(liveItems);
        } else {
            view.liveTable.getItems().clear(); //if no project is selected, I probably don't want to be seeing old stuff.
        }

    }

    private void setupLiveTable() {
        TableColumn titlecol = new TableColumn(EpicSettings.bundle.getString("live.table.title"));
        titlecol.setMinWidth(200);
        titlecol.setMaxWidth(250);
        titlecol.setSortable(false);
        TableColumn typecol = new TableColumn(EpicSettings.bundle.getString("live.table.type"));
        typecol.setMinWidth(40);
        typecol.setMaxWidth(70);
        typecol.setSortable(false);
        TableColumn bodycol = new TableColumn(EpicSettings.bundle.getString("live.table.body"));
        bodycol.setPrefWidth(350);
        bodycol.setSortable(false);

        titlecol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveTableItem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveTableItem, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getNameProperty();
            }
        });
        typecol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveTableItem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveTableItem, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getTypeProperty();
            }
        });
        bodycol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiveTableItem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LiveTableItem, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getContentProperty();
            }
        });


        view.liveTable.getColumns().addAll(titlecol, typecol, bodycol);
        view.liveTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //BEHAVIOR
        view.liveTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LiveTableItem>() {
            @Override
            public void changed(ObservableValue<? extends LiveTableItem> observable, LiveTableItem oldValue, LiveTableItem newValue) {
                if (newValue == null) {
                    return;
                }

                newValue.pushToProjector();

            }
        });

        //Starting the code for pass verses over the selected iten!!
        if (EpicSettings.getLiveHotkeys().equalsIgnoreCase("no")) {
        } else {
            view.liveTable.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(final KeyEvent keyEvent) {
                    String type = view.liveTable.getSelectionModel().getSelectedItem().getTypeProperty().get();
                    if (type.equals("Scripture")) {
                        // This part work only if is a scripture verse selected
                        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
                            //This part take the actual verse and project the next, when RIGHT Arrow was pushed - INCOMPLETE                       
                            ProjectorView.getInstance().pushScene(new PXSongPage("Next verse", "Rigth arrow pressed, next verse"));
                        }
                        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
                            //This part take the actual verse and project the prev, when LEFT Arrow was pushed - INCOMPLETE                       
                            ProjectorView.getInstance().pushScene(new PXSongPage("Prev verse", "Left arrow pressed, prev verse"));
                        }
                    }
                    //This code is a test for make a Hotkey to change background;
                    if (keyEvent.getCode().equals(KeyCode.F1)) {
                        if(!EpicSettings.getLiveF1Video().equalsIgnoreCase("")) DynamicBackground.instance.setPath(EpicSettings.getLiveF1Video());                        
                    }
                    if (keyEvent.getCode().equals(KeyCode.F2)) {
                        if(!EpicSettings.getLiveF2Video().equalsIgnoreCase("")) DynamicBackground.instance.setPath(EpicSettings.getLiveF2Video());                        
                    }
                    if (keyEvent.getCode().equals(KeyCode.F3)) {
                        if(!EpicSettings.getLiveF3Video().equalsIgnoreCase("")) DynamicBackground.instance.setPath(EpicSettings.getLiveF3Video());                        
                    }
                    if (keyEvent.getCode().equals(KeyCode.F4)) {
                        if(!EpicSettings.getLiveF4Video().equalsIgnoreCase("")) DynamicBackground.instance.setPath(EpicSettings.getLiveF4Video());                                                
                    }
                    if (keyEvent.getCode().equals(KeyCode.F5)) {
                        if(!EpicSettings.getLiveF5Image().equalsIgnoreCase("")) ImageBackground.instance.setPath(EpicSettings.getLiveF5Image());
                    }
                    if (keyEvent.getCode().equals(KeyCode.F6)) {
                        if(!EpicSettings.getLiveF6Image().equalsIgnoreCase("")) ImageBackground.instance.setPath(EpicSettings.getLiveF6Image());
                    }
                    if (keyEvent.getCode().equals(KeyCode.F7)) {
                        if(!EpicSettings.getLiveF7Image().equalsIgnoreCase("")) ImageBackground.instance.setPath(EpicSettings.getLiveF7Image());
                    }
                    if (keyEvent.getCode().equals(KeyCode.F8)) {
                        if(!EpicSettings.getLiveF8Image().equalsIgnoreCase("")) ImageBackground.instance.setPath(EpicSettings.getLiveF8Image());
                    }
                    //This code is a test for make Hotkey of Toggle hide motion
                    if (keyEvent.getCode().equals(KeyCode.F9)) {
                        DynamicBackground.instance.showVideo();
                        view.liveMotionBackToggler.setSelected(true);
                        view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle"));
                    }
                    if (keyEvent.getCode().equals(KeyCode.F10)) {
                    	if(view.liveMotionBackToggler.isSelected()) DynamicBackground.instance.hideVideo();
                        view.liveMotionBackToggler.setSelected(false);
                        view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
                    }
                    //This code is a test for make Hotkey of Clear Screen
                    if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                            ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
                    }
                    //Turn default screen image(Logo)
                    if (keyEvent.getCode().equals(KeyCode.F11)) {
                        ImageBackground.instance.setPath(EpicSettings.getScreenImagePath());
                        ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
                        if(view.liveMotionBackToggler.isSelected()) DynamicBackground.instance.hideVideo();
                        view.liveMotionBackToggler.setSelected(false);
                        view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
                    }
                    //Turn off projector - Black Screen
                    if (keyEvent.getCode().equals(KeyCode.F12)) {
                        ImageBackground.instance.setPath(getClass().getResource("/media/blackscr.jpg").toString());
                        ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
                        if(view.liveMotionBackToggler.isSelected()) DynamicBackground.instance.hideVideo();
                        view.liveMotionBackToggler.setSelected(false);
                        view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
                    }
                }
            });
        }
        //setup media controller
        mediaControl = new MediaControl();
        //mediaControl.setLayoutX(0);
        //mediaControl.setLayoutY(0);

        view.liveMediaControlPane.getChildren().add(mediaControl);
    }

    private void setupControlPanel() {
        view.liveOpenBibleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CreationTabController.getInstance().doScriptureEntry();
            }
        });

        view.liveMotionBackToggler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (view.liveMotionBackToggler.isSelected()) {
                    DynamicBackground.instance.showVideo();
                    view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle"));
                } else {
                    DynamicBackground.instance.hideVideo();
                    view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
                }
            }
        });

        view.liveChangeMotionBackBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chooser.getExtensionFilters().clear();
                chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Video File", "*.mp4", "*.MP4", "*.avi", "*.AVI", "*.flv", "*.FLV"));
                File file = chooser.showOpenDialog(null);

                if (file != null) {
                    DynamicBackground.instance.setPath(file.toURI().toString());
                    view.liveMotionBackToggler.setSelected(true);
                    view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle"));

                }
            }
        });

        view.liveChangePicBackBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chooser.getExtensionFilters().clear();
                chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));

                File file = chooser.showOpenDialog(null);

                if (file != null) {
                    ImageBackground.instance.setPath(file.toURI().toString());
                }
            }
        });



        //Color pickers
        //body

        //populate color
        ArrayList<Rectangle> colorboxes = new ArrayList<Rectangle>();
        colorboxes.add(new Rectangle(20, 20, Color.WHITE));
        colorboxes.add(new Rectangle(20, 20, Color.BLACK));
        colorboxes.add(new Rectangle(20, 20, Color.RED));
        colorboxes.add(new Rectangle(20, 20, Color.GREEN));
        colorboxes.add(new Rectangle(20, 20, Color.BLUE));


        view.liveTextColor.getItems().addAll(colorboxes);

        //shadow

        view.liveTextShadowColor.setPrefHeight(30);
        view.liveTextShadowColor.setOrientation(Orientation.HORIZONTAL);
        ArrayList<Rectangle> shadcolorboxes = new ArrayList<Rectangle>();
        shadcolorboxes.add(new Rectangle(20, 20, Color.BLACK));
        shadcolorboxes.add(new Rectangle(20, 20, Color.WHITE));
        shadcolorboxes.add(new Rectangle(20, 20, Color.RED));
        shadcolorboxes.add(new Rectangle(20, 20, Color.GREEN));
        shadcolorboxes.add(new Rectangle(20, 20, Color.BLUE));
        view.liveTextShadowColor.getItems().addAll(shadcolorboxes);

        //dimmer
        view.liveTextShadowDimmer.setOrientation(Orientation.HORIZONTAL);

        ArrayList<Float> shaddimmerboxes = new ArrayList<Float>();
        shaddimmerboxes.add(0.75f);
        shaddimmerboxes.add(0.775f);
        shaddimmerboxes.add(0.8f);
        shaddimmerboxes.add(0.825f);
        shaddimmerboxes.add(0.85f);
        shaddimmerboxes.add(0.9f);
        view.liveTextShadowDimmer.getItems().addAll(shaddimmerboxes);

        //Font size
        view.liveTextSize.setOrientation(Orientation.HORIZONTAL);

        ArrayList<Integer> textszeboxes = new ArrayList<Integer>();
        textszeboxes.add(120);
        textszeboxes.add(100);
        textszeboxes.add(90);
        textszeboxes.add(80);
        textszeboxes.add(70);
        textszeboxes.add(60);

        view.liveTextSize.getItems().addAll(textszeboxes);

        //add to live control panel


        //define behaviors
        view.liveTextColor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rectangle>() {
            @Override
            public void changed(ObservableValue<? extends Rectangle> observable, Rectangle oldValue, Rectangle newValue) {
                if (newValue == null) {
                    return;
                }

                UI.TEXTCOLOR = (Color) newValue.getFill();
            }
        });

        view.liveTextShadowColor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rectangle>() {
            @Override
            public void changed(ObservableValue<? extends Rectangle> observable, Rectangle oldValue, Rectangle newValue) {
                if (newValue == null) {
                    return;
                }

                UI.TEXTSHADOWCOLOR = (Color) newValue.getFill();
            }
        });

        view.liveTextShadowDimmer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Float>() {
            @Override
            public void changed(ObservableValue<? extends Float> observable, Float oldValue, Float newValue) {
                if (newValue == null) {
                    return;
                }

                UI.TEXTSHADOWDIMNESS = newValue;
            }
        });


        view.liveTextSize.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue == null) {
                    return;
                }

                UI.TEXTSIZE = newValue;
            }
        });

        view.liveTextColor.getSelectionModel().select(0);
        view.liveTextShadowColor.getSelectionModel().select(0);
        view.liveTextShadowDimmer.getSelectionModel().select(4);
        view.liveTextSize.getSelectionModel().select(0);

        /*
         //FONT SIZE PICKER
         ListView fontSizePicker = new ListView();
         fontSizePicker.setPrefHeight(30);
         fontSizePicker.getItems().addAll(80d,70d,60d);
         fontSizePicker.setOrientation(Orientation.HORIZONTAL);
         rightbox.getChildren().addAll(new Label("Song Font Size"), fontSizePicker,new Separator());
         fontSizePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Double>()
         {

         @Override
         public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue)
         {
         if(newValue==null)
         return;
		
         UI.songFontSizeMax = newValue.doubleValue();
         }
         });
         fontSizePicker.getSelectionModel().select(0);
         */

        //QuikText
        view.liveQuikTextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProjectorView.getInstance().pushScene(new PXSongPage("", view.liveQuikTextField.getText()));
            }
        });

        //clear button in quiktext
        view.liveClearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProjectorView.getInstance().pushScene(new PXSongPage("", ""));

            }
        });


        //ClearButton and CheckBox
        view.BTClearScreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    view.liveTable.getSelectionModel().clearSelection();
                    ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
            }
        });
        
        view.btLogoScreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!EpicSettings.getScreenImagePath().equalsIgnoreCase("")) {
                    ImageBackground.instance.setPath(EpicSettings.getScreenImagePath());
                } else {
                    ImageBackground.instance.setPath(getClass().getResource("/media/bkg.jpg").toString());
                }
                ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
                if(view.liveMotionBackToggler.isSelected()) DynamicBackground.instance.hideVideo();
                view.liveMotionBackToggler.setSelected(false);
                view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
            }
        });

        view.btBlackScreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ImageBackground.instance.setPath(getClass().getResource("/media/blackscr.jpg").toString());
                ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
                if(view.liveMotionBackToggler.isSelected()) DynamicBackground.instance.hideVideo();
                view.liveMotionBackToggler.setSelected(false);
                view.liveMotionBackToggler.setText(EpicSettings.bundle.getString("tab.live.backgrounds.motiontoggle.off"));
            }
        });
        
         view.btAlert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    Parent settings = FXMLLoader.load(getClass().getResource("/fxml/AlertView.fxml"),EpicSettings.bundle);
                    Scene scene = new Scene(settings);
                    Stage stageSettings = new Stage();
                    stageSettings.setTitle(EpicSettings.bundle.getString("alertview.title"));
                    stageSettings.getIcons().add(new Image(getClass().getResourceAsStream("/media/warning.png")));
                    stageSettings.centerOnScreen();
                    stageSettings.setResizable(false);
                    stageSettings.setScene(scene);
                    stageSettings.show();
                } catch (Exception e) {
                }
            }
        });
    }
}
