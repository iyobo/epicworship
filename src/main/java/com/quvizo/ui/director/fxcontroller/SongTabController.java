/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import com.quvizo.Log;
import com.quvizo.config.EpicSettings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import com.quvizo.data.PresentationAssetWrapper;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.data.tablemodels.SongTableItem;
import com.quvizo.importers.Importer;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.misc.InitImportDialog;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.universal.UI;

/**
 *
 * @author BuffLogic
 */
public class SongTabController
{    
    private static SongTabController instance = null;

    public static SongTabController getInstance()
    {
        return instance;
    }

    public static SongTabController initInstance(DirectorViewFX directorView)
    {
        if(instance == null)
        {
            instance = new SongTabController(directorView);
        }

        return instance;
    }

    private SongTabController(DirectorViewFX view)
    {
        this.view = view;
        initComponent();
    }
    private DirectorViewFX view;

    private void initComponent()
    {
        setupSongTable();

        //Behavior
        defineBehavior();

        //populate
        refreshSongTable();

        //mass import?
        int songcount = view.creationSongTable.getItems().size();
        if(songcount == 0)
        {
            Importer.importDefaults();
        }
        else if(view.creationSongTable.getItems().size() < 10)
        {
            InitImportDialog.launch();
        }
    }

    private void setupSongTable()
    {       
        TableColumn titlecol = new TableColumn(EpicSettings.bundle.getString("tab.creation.song.table.title"));
        //titlecol.setPrefWidth(200);

        TableColumn bodycol = new TableColumn(EpicSettings.bundle.getString("tab.creation.song.table.body"));
        //bodycol.setPrefWidth(150);

        titlecol.setCellValueFactory(new Callback<CellDataFeatures<SongTableItem, String>, ObservableValue<String>>()
        {
            public ObservableValue<String> call(CellDataFeatures<SongTableItem, String> p)
            {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getTitle();
            }
        });
        bodycol.setCellValueFactory(new Callback<CellDataFeatures<SongTableItem, String>, ObservableValue<String>>()
        {
            public ObservableValue<String> call(CellDataFeatures<SongTableItem, String> p)
            {
                // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getBody();
            }
        });

        view.creationSongTable.getColumns().addAll(titlecol, bodycol);
        view.creationSongTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void refreshSongTable()
    {
        try
        {
            List<Asset> allSongs = EntityOverlord.getInstance().getEm().createNamedQuery("Asset.findByTypeSearch").setParameter("type", EntityOverlord.ASSETTYPESONG).setParameter("search", "%" + view.creationSearchSongField.getText() + "%").getResultList();
            ArrayList<SongTableItem> rows = new ArrayList<SongTableItem>();

            for (Asset a : allSongs)
            {
                rows.add(new SongTableItem(a));
            }

            view.creationSongTable.getItems().clear();
            view.creationSongTable.getItems().addAll(rows);
        }
        catch(Exception e)
        {
            Log.log(this, e, "Error attempting to refresh.");
        }
    }

    public void disableSongEditor()
    {
        view.creationSongTitleField.setText("");
        view.creationSongBodyField.setText("");
        view.creationSongTitleField.setEditable(false);
        view.creationSongBodyField.setEditable(false);
    }

    public void enableSongEditor()
    {
        view.creationSongTitleField.setEditable(true);
        view.creationSongBodyField.setEditable(true);
    }

    private void defineBehavior()
    {
        view.creationSongTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        view.creationSongTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SongTableItem>()
        {
            @Override
            public void changed(ObservableValue<? extends SongTableItem> observable, SongTableItem oldValue, SongTableItem newValue)
            {
                if(newValue == null)
                {
                    return;
                }

                UI.selectedSong = newValue.getSongAsset();
                loadSongEditor();
            }
        });

        view.creationSongTable.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {

                if(event.getClickCount() == 2)
                {
                    presentSelectedSong();
                }
            }
        });

        view.creationAddSongBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                newSong();
            }
        });

        view.creationDelSongBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                deleteSong();
            }
        });

        view.creationSaveSongBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                saveSong();
            }
        });

        view.creationSaveSongPresBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                saveSong();
                presentSelectedSong();
            }
        });

        view.creationPresentSongBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                presentSelectedSong();
            }
        });

        //search field
        view.creationSearchSongField.setOnKeyTyped(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {

                refreshSongTable();

            }
        });
    }

    public void loadSongEditor()
    {
        if(UI.selectedSong != null)
        {
            view.creationSongTitleField.setText(UI.selectedSong.getName());
            view.creationSongAuthorField.setText(UI.selectedSong.getAuthor());
            view.creationSongBodyField.setText(UI.selectedSong.getContent());
        }
    }

    public void saveSong()
    {

        if(UI.selectedSong != null)
        {
            UI.selectedSong.setName(view.creationSongTitleField.getText());
            UI.selectedSong.setContent(view.creationSongBodyField.getText());
            EntityManager em = EntityOverlord.getInstance().getEm();
            try
            {
                em.getTransaction().begin();
                em.merge(UI.selectedSong);
                em.getTransaction().commit();

                refreshSongTable();
                CreationTabController.getInstance().refreshPresentationAssetList();


            }
            catch(Exception e)
            {
                Log.log(this, e, EpicSettings.bundle.getString("song.tab.errorsave")+e.getLocalizedMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public void newSong()
    {
        EntityManager em = EntityOverlord.getInstance().getEm();

        Asset song = new Asset();
        song.setName(EpicSettings.bundle.getString("tab.creation.song.new"));
        song.setContent("");
        song.setType(EntityOverlord.ASSETTYPESONG);
        song.setAddedOn(new Date());
        song.setUpdatedOn(new Date());
        try
        {
            em.getTransaction().begin();
            em.persist(song);
            em.getTransaction().commit();


            //refreshSongTable();
            UI.selectedSong = song;

            loadSongEditor();
            refreshSongTable();
        }
        catch(Exception e)
        {
            Log.log(this, e, EpicSettings.bundle.getString("song.tab.errorsave")+e.getLocalizedMessage());
            em.getTransaction().rollback();
        }

    }

    public void deleteSong()
    {
        if(UI.selectedSong == null)
        {
            return;
        }

        EntityManager em = EntityOverlord.getInstance().getEm();
        try
        {
            em.getTransaction().begin();
            //delete all references to the asset
            em.createQuery("delete from PresentationAssets where assetid = " + UI.selectedSong.getId()).executeUpdate();

            //delete asset
            em.remove(UI.selectedSong);


            em.getTransaction().commit();

            UI.selectedSong = null;




            refreshSongTable();
            CreationTabController.getInstance().refreshPresentationAssetList();
        }
        catch(Exception e)
        {
            Log.log(this, e, "Error attempting to refresh.");
            em.getTransaction().rollback();
        }
    }

    public void presentSelectedSong()
    {
        if(UI.selectedPresentation == null)
        {

            Qui.showMessageBox("Oops", EpicSettings.bundle.getString("system.tab.selectpresentation"));
            return;
        }
        if(UI.selectedSong == null)
        {

            Qui.showMessageBox("Oops", EpicSettings.bundle.getString("song.tab.selectsong"));

            return;
        }

        EntityManager em = EntityOverlord.getInstance().getEm();
        try
        {
            PresentationAssets pass = new PresentationAssets();
            pass.setPid(UI.selectedPresentation.getId());
            pass.setAssetid(UI.selectedSong.getId());
            pass.setAddedOn(new Date());

            em.getTransaction().begin();
            em.persist(pass);
            em.getTransaction().commit();

            CreationTabController.getInstance().refreshPresentationAssetList();
        }
        catch(Exception e)
        {
            Log.log(this, e, "Error attempting to refresh.");
            em.getTransaction().rollback();
        }
    }

    public TableView<SongTableItem> getSongTable()
    {
        return view.creationSongTable;
    }

    void selectThis(PresentationAssetWrapper newValue)
    {
        for (SongTableItem item : view.creationSongTable.getItems())
        {
            if(item.getSongAsset().equals(newValue.getItem()))
            {
                view.creationSongTable.getSelectionModel().select(item);
                break;
            }
        }

    }
}
