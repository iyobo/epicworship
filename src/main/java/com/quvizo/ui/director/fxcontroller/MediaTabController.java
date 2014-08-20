/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import com.quvizo.Log;
import com.quvizo.config.EpicSettings;
import java.io.File;
import java.util.Date;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.universal.UI;
import java.net.URI;
import javafx.scene.media.Media;

/**
 *
 * @author BuffLogic
 */
public class MediaTabController
{

    private static MediaTabController instance = null;

    public static MediaTabController getInstance()
    {
        return instance;
    }

    public static MediaTabController initInstance(DirectorViewFX directorView)
    {
        if(instance == null)
        {
            instance = new MediaTabController(directorView);
        }

        return instance;
    }

    private MediaTabController(DirectorViewFX view)
    {
        this.view = view;
        initComponent();
    }
    private DirectorViewFX view;

    private void initComponent()
    {
        //Behavior
        defineBehavior();

        //populate
        refreshListView();

    }

    private void defineBehavior()
    {
        view.creationMediaList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        view.creationMediaList.setEditable(false);
        view.creationMediaList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Asset>()
        {
            @Override
            public void changed(ObservableValue<? extends Asset> observable, Asset oldValue, Asset newValue)
            {
                if(newValue == null)
                {
                    return;
                }
                UI.selectedVideo = newValue;
                System.out.println("Selected video: " + UI.selectedVideo);
                refreshListView();
            }
        });
        view.creationMediaList.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {

                if(event.getClickCount() == 2)
                {
                    addAssetToPresentation();
                }
            }
        });

        this.view.creationAddMediaBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                newAsset();
            }
        });

        view.creationDelMediaBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                deleteAsset();
            }
        });

        view.creationPresentMediaBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                addAssetToPresentation();
            }
        });
    }
    FileChooser chooser = new FileChooser();

    public void newAsset()
    {

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.*"));
        File file = chooser.showOpenDialog(null);

        if(file == null)
        {
            return;
        }


        try
        {
            //Can we actually use it?
            new Media(file.toURI().toString());

            //Add it
            EntityManager em = EntityOverlord.getInstance().getEm();

            try
            {
                Asset asset = new Asset();
                asset.setName(file.getName());
                asset.setContent(file.getAbsolutePath());
                asset.setType(EntityOverlord.ASSETTYPEMEDIA);
                asset.setAddedOn(new Date());
                asset.setUpdatedOn(new Date());

                em.getTransaction().begin();
                em.persist(asset);
                em.getTransaction().commit();

                refreshListView();

                view.creationMediaList.getSelectionModel().select(asset);
            }
            catch(Exception ex)
            {
                Log.log(this, ex, "Unable to Add Media File: " + ex.getLocalizedMessage());
                em.getTransaction().rollback();
            }
        }
        catch(Exception e)
        {
            Log.log(this, e, "Invalid Media Format. \n Please make sure it is an H.264 or VP6 encoded MP4 or FLV, with AAC or MP3 audio encoding.");
        }

    }

    public void deleteAsset()
    {
        if(UI.selectedVideo == null)
        {
            return;
        }

        EntityManager em = EntityOverlord.getInstance().getEm();
        try
        {
            em.getTransaction().begin();
            //delete all references to the asset
            em.createQuery("delete from PresentationAssets where assetid = " + UI.selectedVideo.getId()).executeUpdate();
            //delete asset
            em.remove(UI.selectedVideo);
            em.getTransaction().commit();

            UI.selectedVideo = null;

            refreshListView();
            CreationTabController.getInstance().refreshPresentationAssetList();
        }
        catch(Exception e)
        {
            Log.log(this, e, "Error attempting to refresh.");
            em.getTransaction().rollback();
        }
    }

    public void addAssetToPresentation()
    {
        if(UI.selectedPresentation == null)
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    JOptionPane.showMessageDialog(null, EpicSettings.bundle.getString("system.tab.selectpresentation"));
                }
            }.start();

            return;
        }
        if(UI.selectedVideo == null)
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    JOptionPane.showMessageDialog(null, EpicSettings.bundle.getString("media.tab.selectvideo"));
                }
            }.start();

            return;
        }

        EntityManager em = EntityOverlord.getInstance().getEm();
        try
        {
            PresentationAssets pass = new PresentationAssets();
            pass.setPid(UI.selectedPresentation.getId());
            pass.setAssetid(UI.selectedVideo.getId());
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

    private void refreshListView()
    {
        List<Asset> allVideos = EntityOverlord.getInstance().getEm().createNamedQuery("Asset.findByTypeOrType").setParameter("type", EntityOverlord.ASSETTYPEMEDIA).setParameter("typeb", "video").getResultList(); //also find for "video" for legacy support
        view.creationMediaList.getItems().clear();
        view.creationMediaList.getItems().addAll(allVideos);
    }
}
