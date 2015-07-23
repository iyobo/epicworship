/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.ui.bible;

import com.quvizo.config.EpicSettings;
import com.quvizo.data.NamedString;
import com.quvizo.data.PresentationAssetWrapper;
import com.quvizo.data.SettingName;
import com.quvizo.data.entity.BibleTranslation;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.data.entity.Setting;
import com.quvizo.projector.pages.PXSongPage;
import com.quvizo.ui.ProjectorView;
import com.quvizo.ui.director.fxcontroller.CreationTabController;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.universal.BibleKeeper;
import com.quvizo.universal.EntityOverlord;

import static com.quvizo.universal.EpicOverlord.primaryStage;

import com.quvizo.universal.UI;
import com.quvizo.wrappers.BibleScripture;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.EntityManager;

import org.jdom.JDOMException;

/**
 * FXML Controller class
 *
 * @author bufflogic
 */
public class BibleDialogController implements Initializable {

    public static BibleDialogController instance;
    public static Stage biblestage;
    //public Label livePresentationLabel;
    public TextArea previewfield;
    public ListView<NamedString> oldbooklist;
    public ListView<NamedString> newbooklist;
    public ListView<Integer> chapterlist;
    public ListView<Integer> verselist;
    public ListView<Integer> toverselist;
    public ComboBox<BibleTranslation> versionactivefield;
    //Projection
    /**
     * IF this is zero, we cannot "Next" it. We must start first, which makes it
     * >0.
     */
    Integer nowprojecting = 0;

    private void updateProjectorIndicator() {
    }
    BibleKeeper keeper = BibleKeeper.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        setup();
        translate();
    }

    private void setup() {
        //populate books
        oldbooklist.getItems().addAll(BibleKeeper.getInstance().getOldTestamentBooks());
        newbooklist.getItems().addAll(BibleKeeper.getInstance().getNewTestamentBooks());

        //populate versions
        versionactivefield.getItems().clear();
        versionactivefield.getItems().addAll(BibleKeeper.getInstance().getTranslations());

        //select previously selected version, KJV if none selected previously
        Setting activeversion = Setting.getSetting(SettingName.BIBLE_TRANSLATION.toString());
        if (activeversion == null) {
            versionactivefield.getSelectionModel().select(0);
        } else {
            versionactivefield.getSelectionModel().select(BibleTranslation.getBibleTranslation(activeversion.getName()));
        }
    }

    public static void launch() {
        try {
            if (biblestage == null) {
                biblestage = new Stage(StageStyle.DECORATED);
                biblestage.initOwner(primaryStage);
                final Parent fxmlRoot = FXMLLoader.load(Stage.class.getClass().getResource("/fxml/BibleDialog.fxml"),EpicSettings.bundle);
                biblestage.setScene(new Scene(fxmlRoot));
                biblestage.initModality(Modality.NONE);
            }

            biblestage.show();
        } catch (IOException ex) {
            Logger.getLogger(BibleDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onVersionChange(ActionEvent event) {
        try {
            BibleKeeper.getInstance().loadBible(versionactivefield.getSelectionModel().getSelectedItem());
            Setting.saveSetting(SettingName.BIBLE_TRANSLATION.toString(), versionactivefield.getSelectionModel().getSelectedItem().getName());
        } catch (IOException ex) {
            Logger.getLogger(BibleDialogController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(BibleDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onImportVersion(ActionEvent event) {
        LoadVersionFile();
    }
    
     private void LoadVersionFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Bible File");
        File chosen = chooser.showOpenDialog(primaryStage);
        if ( chosen== null) {
            return;
        }

        try {
            EntityManager em = EntityOverlord.getInstance().getEm();
            BibleKeeper.getInstance().loadBible(chosen);
            
            //TODO: save custom bible translations
            //em.getTransaction().begin();
            BibleTranslation tr = new BibleTranslation(chosen.getName(), chosen.getAbsolutePath(), "file");
            versionactivefield.getItems().add(tr);
            versionactivefield.getSelectionModel().select(tr);
            
            //em.getTransaction().commit();
            
            submitScripture();
        } catch (IOException ex) {
            Logger.getLogger(BibleDialogController.class.getName()).log(Level.SEVERE, null, ex);
            Qui.showMessageBox("Hmm", "Can't open Bible File");

        } catch (JDOMException ex) {
            Logger.getLogger(BibleDialogController.class.getName()).log(Level.SEVERE, null, ex);
            Qui.showMessageBox("Hmm", "Can't open Bible File");
        }

    }

    @FXML
    private void onPrev(ActionEvent event) {
        if (scripture == null || scripture.getChapter() == 0 || nowprojecting == 0) {
            return;
        }

        BibleScripture scripverse = null;

        nowprojecting--;
        if (nowprojecting < 1) {
            nowprojecting = 0;
            return;
        }


        scripverse = new BibleScripture(scripture.getBook(), scripture.getChapter(), nowprojecting, nowprojecting);

        final String pname = scripverse.getName();
        final String pverse = BibleKeeper.getInstance().getScripture(scripverse);

        ProjectorView.getInstance().pushScene(new PXSongPage(pname, pverse));
    }

    @FXML
    private void onNext(ActionEvent event) {
        if (scripture == null || scripture.getChapter() == 0) {
            return;
        }

        BibleScripture scripverse = null;

        //Starting
        if (nowprojecting == 0) {
            //start from fromverse
            if (scripture.getFromverse() != 0) {

                scripverse = new BibleScripture(scripture.getBook(), scripture.getChapter(), scripture.getFromverse(), scripture.getFromverse());

                nowprojecting = scripture.getFromverse();
            } else {
                //assume to start from 1st verse
                scripverse = new BibleScripture(scripture.getBook(), scripture.getChapter(), 1, 1);
                nowprojecting = 1;
            }


        } else//continueing
        {
            nowprojecting++;
            if (nowprojecting > keeper.getVerseCount(scripture.getBook(), scripture.getChapter())) {
                nowprojecting = 0;
                return;
            }

            scripverse = new BibleScripture(scripture.getBook(), scripture.getChapter(), nowprojecting, nowprojecting);
        }

        final String pname = scripverse.getName();
        final String pverse = BibleKeeper.getInstance().getScripture(scripverse).trim();

        ProjectorView.getInstance().pushScene(new PXSongPage(pname, pverse));

    }

    @FXML
    private void onClearScreen(ActionEvent event) {
        clearScreen();
    }

    public void translate() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FXML
    private void onOldBookSelected(MouseEvent event) {
        newScripture();
        newbooklist.getSelectionModel().clearSelection();
        selectBook(event);
    }

    @FXML
    private void onNewBookSelected(MouseEvent event) {
        newScripture();
        oldbooklist.getSelectionModel().clearSelection();
        selectBook(event);

    }

    @FXML
    private void onChapterSelected(MouseEvent event) {
        selectChapter(event);
    }

    @FXML
    private void onVerseSelected(MouseEvent event) {
        selectFromVerse(event);
    }

    @FXML
    private void onToVerseSelected(MouseEvent event) {
        selectToVerse(event);
    }

    private void selectBook(MouseEvent evt) {
        newScripture();
        ListView list = (ListView) evt.getSource();

        if (list.getSelectionModel().getSelectedItem() != null) {

            selectedBook = list.getSelectionModel().getSelectedItem().toString();

            System.out.println(selectedBook);

            //clear chapter and verse list
            chapterlist.getItems().clear();
            verselist.getItems().clear();
            toverselist.getItems().clear();

            //populate chapter list, leaving verse lists empty
            for (int x = 1; x <= keeper.getChapterCount(selectedBook); x++) {
                chapterlist.getItems().add(x);
            }

            //set scripture

            scripture.set(selectedBook, 0, 0, 0);
            //submitScripture();
            nowprojecting = 0;
            updateProjectorIndicator();
        }

    }

    private void newScripture() {
        if (scripture == null) {
            scripture = new BibleScripture();
        }
    }
    public static BibleScripture scripture = new BibleScripture();
    String selectedBook = "";
    int selectedChapter = 0;

    private void selectChapter(MouseEvent evt) {
        ListView list = (ListView) evt.getSource();

        if (list.getSelectionModel().getSelectedItem() != null) {

            int chapter = (Integer) list.getSelectionModel().getSelectedItem();

            //clear verse lists
            verselist.getItems().clear();
            toverselist.getItems().clear();

            //populate verse lists
            for (int x = 1; x <= keeper.getVerseCount(selectedBook, chapter); x++) {
                verselist.getItems().add(x);
                toverselist.getItems().add(x);
            }


            //set scripture
            scripture.setChapter(chapter);
            scripture.setFromverse(0);
            scripture.setToverse(0);
            submitScripture();
            nowprojecting = 0;
            updateProjectorIndicator();
        }

    }

    private void selectFromVerse(MouseEvent evt) {
        if (verselist.getSelectionModel().getSelectedItem() != null) {

            int fverse = (Integer)verselist.getSelectionModel().getSelectedItem();
            
            if (scripture.getToverse() == 0 || scripture.getToverse() < fverse) {
                scripture.setToverse(fverse);
                toverselist.getSelectionModel().select(fverse - 1);
            }

            //set scripture
            scripture.setFromverse(fverse);
            submitScripture();

            nowprojecting = 0;
            updateProjectorIndicator();
        }
    }

    private void selectToVerse(MouseEvent evt) {

        ListView list = (ListView) evt.getSource();

        int fverse = (Integer) verselist.getSelectionModel().getSelectedItem();
        int tverse = (Integer) toverselist.getSelectionModel().getSelectedItem();

        if (scripture.getToverse() == 0 || scripture.getToverse() < fverse) {
            tverse = fverse;
            toverselist.getSelectionModel().select(fverse - 1);
        }

        //set scripture
        scripture.setToverse(tverse);
        submitScripture();

    }

    /**
     * Query the bible keeper with this scripture and show result in text area.
     */
    private void submitScripture() {
        String verses = BibleKeeper.getInstance().getScripture(scripture);

        previewfield.setText(verses);
    }
    String result = "";
    
    private void clearScreen() {

        ProjectorView.getInstance().pushScene(new PXSongPage("", ""));
    }

    @FXML
    private void onAdd(ActionEvent event) {
        addScripturetoPresentation();
    }
    
    private void addScripturetoPresentation() {

        if (UI.selectedPresentation == null) {
            Qui.showMessageBox("Hey!", "Please select a Presentation first before adding to Presentation List");
            return;
        }

        if (!scripture.isUsable()) {
             Qui.showMessageBox("Hey!", "Be sure to select at least a book and a chapter.");
            return;
        }

        EntityManager em = EntityOverlord.getInstance().getEm();

        PresentationAssets pass = new PresentationAssets();
        pass.setPid(UI.selectedPresentation.getId());
        pass.setAssetid(0);
        pass.setAddedOn(new Date());
        pass.setBook(scripture.getBook());
        pass.setChapter(scripture.getChapter());
        pass.setFromverse(scripture.getFromverse());
        pass.setToverse(scripture.getToverse());

        em.getTransaction().begin();
        em.persist(pass);
        em.getTransaction().commit();

        CreationTabController.getInstance().refreshPresentationAssetList();
       
    }

    @FXML
    private void onClose(ActionEvent event) {
        //wasOk = false;
        biblestage.hide();
    }
}
