/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.ui.director.fxcontroller;

import com.quvizo.config.EpicSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

import com.quvizo.data.PresentationAssetWrapper;
import com.quvizo.data.entity.Presentation;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.fxmisc.BuffUIAction;
import com.quvizo.ui.misc.BibleDialog;
import com.quvizo.ui.misc.TextDialog;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.universal.UI;

import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author BuffLogic
 */
public class CreationTabController {

	private static CreationTabController instance = null;

	public static CreationTabController getInstance() {
		return instance;
	}

	public static CreationTabController initInstance(DirectorViewFX directorView) {
		if (instance == null) {
			instance = new CreationTabController(directorView);
		}

		return instance;
	}

	private CreationTabController(DirectorViewFX view) {
		this.view = view;
		initComponent();
	}

	private DirectorViewFX view;

	// ====

	// BibleDialog bibleDialog = new BibleDialog(null, true);
	private void initComponent() {
		// bibleDialog.setAlwaysOnTop(true);
		refreshPresentationList();
		defineBehaviors();
	}

	private void defineBehaviors() {
		view.creationAddPresBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				BuffUIAction action = new BuffUIAction() {
					@Override
					public void perform(TextInputControl[] actionInputs,
							Stage stageOfDialog) {

						if (actionInputs[0].getText().isEmpty()) {
							Qui.showMessageBox("Oops", EpicSettings.bundle
									.getString("creation.tab.message.needname"));
							return;
						}

						EntityManager em = EntityOverlord.getInstance().getEm();

						Presentation newPres = new Presentation();
						newPres.setName(actionInputs[0].getText());
						newPres.setAddedOn(new Date());
						newPres.setUpdatedOn(new Date());

						em.getTransaction().begin();
						em.persist(newPres);
						em.getTransaction().commit();

						System.out.println(newPres.getId());

						UI.selectedPresentation = newPres;
						stageOfDialog.close();
						refreshPresentationList();
					}
				};

				Qui.showTextInput(EpicSettings.bundle
						.getString("creation.tab.message.newpresen"),
						EpicSettings.bundle
								.getString("creation.tab.message.name"), action);

			}
		});

		view.creationDelPresBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (UI.selectedPresentation == null) {
					return;
				}

				EntityOverlord.getInstance().getEm().getTransaction().begin();
				EntityOverlord.getInstance().getEm()
						.remove(UI.selectedPresentation);
				// TODO: delete all presentation assets of PIDs...or rather let
				// it be for easy recovery?

				EntityOverlord.getInstance().getEm().getTransaction().commit();

				UI.selectedPresentation = null;

				refreshPresentationList();

			}
		});
		view.creationDelPresItemBtn
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (view.creationPresAssetList.getSelectionModel()
								.getSelectedItem() == null) {
							return;
						}

						EntityManager em = EntityOverlord.getInstance().getEm();

						em.getTransaction().begin();
						em.remove(view.creationPresAssetList
								.getSelectionModel().getSelectedItem()
								.getPresentationAsset());
						em.getTransaction().commit();

						refreshPresentationAssetList();

					}
				});

		// Delete presentation item using DEL key
		view.creationPresAssetList.addEventHandler(KeyEvent.KEY_PRESSED,
				new EventHandler<KeyEvent>() {
					@Override
					public void handle(final KeyEvent keyEvent) {
						if (keyEvent.getCode().equals(KeyCode.DELETE)) {
							if (view.creationPresAssetList.getSelectionModel()
									.getSelectedItem() == null) {
								return;
							}

							EntityManager em = EntityOverlord.getInstance()
									.getEm();

							em.getTransaction().begin();
							em.remove(view.creationPresAssetList
									.getSelectionModel().getSelectedItem()
									.getPresentationAsset());
							em.getTransaction().commit();

							refreshPresentationAssetList();
						}
					}
				});

		view.creationPresentationList.getSelectionModel()
				.selectedItemProperty()
				.addListener(new ChangeListener<Presentation>() {
					@Override
					public void changed(
							ObservableValue<? extends Presentation> observable,
							Presentation oldValue, Presentation newValue) {
						UI.selectedPresentation = newValue;
						System.out.println("Selected Presentation: "
								+ UI.selectedPresentation);

						view.updateNowPresentingLabel();
						refreshPresentationAssetList();
					}
				});

		view.creationPresAssetList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<PresentationAssetWrapper>() {
					@Override
					public void changed(
							ObservableValue<? extends PresentationAssetWrapper> observable,
							PresentationAssetWrapper oldValue,
							PresentationAssetWrapper newValue) {
						if (newValue == null) {
							return;
						}

						// load the selected song
						SongTabController.getInstance().selectThis(newValue);
					}
				});

		view.creationPresAssetList
				.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent dragEvent) {
						System.out.println("setOnDragEntered");

						view.creationPresAssetList
								.setBlendMode(BlendMode.DIFFERENCE);
					}
				});

		view.creationPresAssetList
				.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent dragEvent) {
						System.out.println("setOnDragExited");

						view.creationPresAssetList.setBlendMode(null);
					}
				});

		view.creationPresAssetList.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				System.out.println("setOnDragOver");

				dragEvent.acceptTransferModes(TransferMode.MOVE);
			}
		});

		view.creationPresAssetList
				.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent dragEvent) {
						System.out.println("setOnDragDropped");

						String player = dragEvent.getDragboard().getString();

						//view.creationPresAssetList.getItems().addAll(
						//		new Player(player));

						//playersList.remove(new Player(player));

						dragEvent.setDropCompleted(true);
						
					}
				});

		// add scripture to this
		view.creationOpenBibleBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doScriptureEntry();
			}
		});
	}

	public void refreshPresentationList() {
		List<Presentation> allPresentations = EntityOverlord.getInstance()
				.getEm().createNamedQuery("Presentation.findAll")
				.getResultList();

		view.creationPresentationList.getItems().clear();
		view.creationPresentationList.getItems().addAll(allPresentations);

		// reselect previous selection
		if (UI.selectedPresentation == null) {
			if (view.creationPresentationList.getItems().size() == 0) {

				refreshPresentationAssetList();
				return;
			}

			view.creationPresentationList.getSelectionModel().select(0); // select
																			// first
																			// one
			UI.selectedPresentation = view.creationPresentationList
					.getSelectionModel().getSelectedItem();
		} else {
			for (Presentation p : view.creationPresentationList.getItems()) {
				if (p.getId() == UI.selectedPresentation.getId()) {
					view.creationPresentationList.getSelectionModel().select(p);
				}
			}
		}
		refreshPresentationAssetList();
	}

	public void refreshPresentationAssetList() {
		if (UI.selectedPresentation != null) {
			List<PresentationAssets> rawPresAssets = EntityOverlord
					.getInstance().getEm()
					.createNamedQuery("PresentationAssets.findByPid")
					.setParameter("pid", UI.selectedPresentation.getId())
					.getResultList();
			List<PresentationAssetWrapper> selectedPresentationAssets = new ArrayList<PresentationAssetWrapper>();

			for (int x = 0; x < rawPresAssets.size(); x++) {
				selectedPresentationAssets.add(new PresentationAssetWrapper(
						rawPresAssets.get(x)));
			}

			view.creationPresAssetList.getItems().clear();
			view.creationPresAssetList.getItems().addAll(
					selectedPresentationAssets);

		} else {
			view.creationPresAssetList.getItems().clear(); // if no project is
															// selected, I
															// probably don't
															// want to be seeing
															// old stuff.
		}

		if (LiveTabController.getInstance() != null) {
			LiveTabController.getInstance().refreshLiveTable();
		}
	}

	public void doScriptureEntry() {

		// new Thread() {
		//
		// @Override
		// public void run() {
		// bibleDialog.launch();
		// }
		// }.start();

		BibleDialog.launch();
		// try{
		// Parent bibledialog =
		// FXMLLoader.load(getClass().getResource("/com/quvizo/ui/bible/BibleDialog.fxml"));
		// Scene scene = new Scene(bibledialog);
		// Stage stageBible = new Stage();
		// stageBible.setTitle("Bible");
		// stageBible.centerOnScreen();
		// stageBible.setResizable(false);
		// stageBible.setScene(scene);
		// stageBible.show();
		// }
		// catch(Exception e){
		//
		// }

	}
}
