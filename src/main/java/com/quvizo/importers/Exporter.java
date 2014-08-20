/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.importers;

import com.quvizo.config.EpicSettings;
import com.quvizo.data.entity.Asset;
import com.quvizo.ui.importer.ResultDialog;
import com.quvizo.universal.EntityOverlord;
import com.sun.glass.ui.Application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.persistence.EntityManager;
import org.json.simple.JSONObject;

/**
 *
 * @author peki
 */
public class Exporter {

    private static Logger logger = Logger.getLogger(Exporter.class.getName());
    protected static FileChooser chooser = new FileChooser();

    public static void exportEPCFiles() {
        Application.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        chooser.setTitle(EpicSettings.bundle.getString("exporter.title"));
                        chooser.getExtensionFilters().clear();
                        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("*.epc", "*.EPC"));
                        File epcfile = chooser.showSaveDialog(null);
                        int count = 0;

                        if (epcfile != null) {
                            try {
                                
                                if(!epcfile.getAbsolutePath().endsWith(".epc"))
                                {
                                    epcfile = new File(epcfile.getAbsolutePath() + ".epc");
                                }
                                //we have selected stuff. Do an XML parse of XML file
                                //ArrayList<Asset> newsongs = new ArrayList<Asset>();
                                ArrayList<String> trouble = new ArrayList<String>();
                                EntityManager em = EntityOverlord.getInstance().getEm();

                                List<Asset> songList = em.createNamedQuery("Asset.findByType").setParameter("type", EntityOverlord.ASSETTYPESONG).getResultList();
                                FileWriter doc = new FileWriter(epcfile);
                                
                                JSONObject json = new JSONObject();
                                
                                LinkedList<LinkedHashMap> songs = new LinkedList<LinkedHashMap>();
                                for (Asset s : songList) {
                                    try {

                                        /*
                                         * Element song = new Element("song");
                                         * song.addContent(new
                                         * Element("name").setText(s.getName()));
                                         * song.addContent(new
                                         * Element("author").setText(s.getAuthor()));
                                         * song.addContent(new
                                         * Element("content").setText(s.getContent()));
                                         *
                                         * doc.getRootElement().addContent(song);
                                         */


                                        LinkedHashMap song = new LinkedHashMap();
                                        song.put("name", s.getName());
                                        song.put("author", s.getAuthor());
                                        song.put("content", s.getContent());

                                        songs.add(song);

                                        count++;

                                    } catch (Exception ex) {
                                        Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
                                        trouble.add(ex.getMessage());
                                    }
                                }
                                
                                json.put("songs", songs);

                                json.writeJSONString(doc);
                                doc.flush();

                                Logger.getLogger(Exporter.class.getName()).log(Level.INFO, EpicSettings.bundle.getString("exporter.savedall") + epcfile.getAbsolutePath());

                                //Enttrouble?
                                //SongTabController.getInstance().loadSongEditor();
                                //SongTabController.getInstance().refreshSongTable();
                                
                                ResultDialog.showMessage(EpicSettings.bundle.getString("exporter.title2"), java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("exporter.finished"), new Object[] {count}), false,trouble);
                            } catch (IOException ex) {
                                Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
                                ResultDialog.showMessage(EpicSettings.bundle.getString("exporter.titleerror"), EpicSettings.bundle.getString("exporter.errorbody") + ex.getMessage());
                            }
                        }
                    }
                });

    }
}
