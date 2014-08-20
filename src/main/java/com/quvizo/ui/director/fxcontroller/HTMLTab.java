/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import java.util.Date;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.fxmisc.BuffUIAction;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.universal.EpicOverlord;
import com.quvizo.universal.ProjectorConstants;
import com.quvizo.universal.UI;
import javafx.stage.Stage;

/**
 *
 * @author BuffLogic
 */
public class HTMLTab extends Tab
{

    private final CreationTabController creationTab;

    public HTMLTab(CreationTabController creationTab, String title)
    {
	super(title);
	this.creationTab = creationTab;
	initComponent();
    }

    private void initComponent()
    {
	this.setClosable(false);

	VBox mainBox = new VBox(8);
	mainBox.setPadding(UI.defaultPadding);

	//button panel
	HBox buttonPanel = new HBox(8);
	buttonPanel.getChildren().addAll(createBtn, deleteBtn, presentBtn);

	//list View
	listView.setPrefHeight(480);

        //help text
        Label helpLabel = new Label("Only use this if you understand HTML. Use this to present HTML content to viewers as you would a webpage. \nTypical use is to enter in embed codes from other websites for video or power-pointish web presentations.\nBe mindful of width and height.");
        helpLabel.setWrapText(true);

	mainBox.getChildren().addAll(buttonPanel, listView, helpLabel);

	//final piece
	this.setContent(mainBox);

	//Behavior
	defineBehavior();

	//populate
	refreshListView();
    }
    /**
     * Assemble the song Asset tab.
     */
    Button createBtn = new Button(" Add ", new ImageView(new Image(getClass().getResource("/com/quvizo/resource/add.png").toString())));
    Button deleteBtn = new Button(" Delete ", new ImageView(new Image(getClass().getResource("/com/quvizo/resource/delete.png").toString())));
    Button presentBtn = new Button(" Add To Set ", new ImageView(new Image(getClass().getResource("/com/quvizo/resource/addtolist.png").toString())));
    ListView<Asset> listView = new ListView<Asset>();

    private void defineBehavior()
    {
	listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	listView.setEditable(false);
	listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Asset>()
	{

	    @Override
	    public void changed(ObservableValue<? extends Asset> observable, Asset oldValue, Asset newValue)
	    {
		if (newValue == null)
		{
		    return;
		}
		UI.selectedVideo = newValue;
		System.out.println("Selected video: " + UI.selectedVideo);
		refreshListView();
	    }
	});
	listView.setOnMouseClicked(new EventHandler<MouseEvent>()
	{

	    @Override
	    public void handle(MouseEvent event)
	    {

		if (event.getClickCount() == 2)
		{
		    addAssetToPresentation();
		}
	    }
	});

	this.createBtn.setOnAction(new EventHandler<ActionEvent>()
	{

	    @Override
	    public void handle(ActionEvent event)
	    {
		newAsset();
	    }
	});
        
	deleteBtn.setOnAction(new EventHandler<ActionEvent>()
	{

	    @Override
	    public void handle(ActionEvent event)
	    {
		deleteAsset();
	    }
	});

	presentBtn.setOnAction(new EventHandler<ActionEvent>()
	{

	    @Override
	    public void handle(ActionEvent event)
	    {
		addAssetToPresentation();
	    }
	});
    }
   

    public void newAsset()
    {
        final int inputcount = 2;
        BuffUIAction action = new BuffUIAction() {

            @Override
            public void perform(TextInputControl[] input, Stage stageOfDialog) {

                //now add it
                EntityManager em = EntityOverlord.getInstance().getEm();

		Asset asset = new Asset();
		asset.setName("HTML_"+input[0].getText());
		asset.setContent(input[1].getText());
		asset.setType(EntityOverlord.ASSETTYPEHTML);
		asset.setAddedOn(new Date());
		asset.setUpdatedOn(new Date());

		em.getTransaction().begin();
		em.persist(asset);
		em.getTransaction().commit();

		refreshListView();

		listView.getSelectionModel().select(asset);
                stageOfDialog.close();
            }
        };
        
        Qui.showTextInput(inputcount, "Creating New HTML Script...", new String[]{"Name:","Advanced HTML content. (If you need it, Use this width and height for optimal full screen embeds ['"+(ProjectorConstants.getSCREENWIDTH()-30)+"','"+(ProjectorConstants.getSCREENHEIGHT()-30)+"']):"}, new int[]{Qui.INPUTTYPESINGLELINE,Qui.INPUTTYPEMULTILINE}, action);

    }

    public void deleteAsset()
    {
	if (UI.selectedVideo == null)
	{
	    return;
	}

	EntityManager em = EntityOverlord.getInstance().getEm();

	em.getTransaction().begin();
	//delete all references to the asset
	em.createQuery("delete from PresentationAssets where assetid = " + UI.selectedVideo.getId()).executeUpdate();
	//delete asset
	em.remove(UI.selectedVideo);
	em.getTransaction().commit();

	UI.selectedVideo = null;

	refreshListView();
        creationTab.refreshPresentationAssetList();
    }

    public void addAssetToPresentation()
    {
	if (UI.selectedPresentation == null)
	{
	    new Thread()
	    {

		@Override
		public void run()
		{
		    JOptionPane.showMessageDialog(null, "Please select a PResentation first before adding to PResentation List");
		}
	    }.start();


	    return;
	}
	if (UI.selectedVideo == null)
	{
	    new Thread()
	    {

		@Override
		public void run()
		{
		    JOptionPane.showMessageDialog(null, "Please select a Video first before adding to PResentation List");
		}
	    }.start();


	    return;
	}

	EntityManager em = EntityOverlord.getInstance().getEm();

	PresentationAssets pass = new PresentationAssets();
	pass.setPid(UI.selectedPresentation.getId());
	pass.setAssetid(UI.selectedVideo.getId());
	pass.setAddedOn(new Date());

	em.getTransaction().begin();
	em.persist(pass);
	em.getTransaction().commit();

	creationTab.refreshPresentationAssetList();
    }

    private void refreshListView()
    {
	List<Asset> allVideos = EntityOverlord.getInstance().getEm().createNamedQuery("Asset.findByType").setParameter("type", EntityOverlord.ASSETTYPEHTML).getResultList();
	listView.getItems().clear();
	listView.getItems().addAll(allVideos);
    }
}
