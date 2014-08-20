/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.importers;

import com.quvizo.data.entity.Asset;
import com.quvizo.ui.director.fxcontroller.SongTabController;
import com.quvizo.ui.importer.ResultDialog;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.util.AppUtils;
import com.sun.glass.ui.Application;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.persistence.EntityManager;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author peki
 */
public class Importer {

    protected static FileChooser chooser = new FileChooser();

        public static void importPowerPointFiles() {
        Application.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        chooser.setTitle(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titlepp"));
                        chooser.getExtensionFilters().clear();
                        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PowerPoint","*.ppt", "*.PPT"));
                        List<File> files = chooser.showOpenMultipleDialog(null);

                        int count = 0;
                        int dupcount = 0;
                        if (files != null && files.size() > 0) {
                            //we have selected stuff. Do an XML parse of XML file
                            //ArrayList<Asset> newsongs = new ArrayList<Asset>();
                            ArrayList<String> trouble = new ArrayList<String>();
                            EntityManager em = EntityOverlord.getInstance().getEm();

                            for (File file : files) {
                                try {
                                    // Metodo para importar PowerPoint
                                    // by: Italo C. Zaina
                                    // Funcionando apenas ppt
                                    //
                                    PowerPointExtractor ppt = new PowerPointExtractor(file.getAbsolutePath());

                                    Asset song = new Asset();

                                    song.setType(EntityOverlord.ASSETTYPESONG);

                                    song.setName(file.getName().replaceFirst(".ppt", ""));
                                    song.setAuthor("PowerPoint");
                                    song.setContent(ppt.getText());

                                    song.setAddedOn(new Date());
                                    song.setUpdatedOn(new Date());
                                    List dupl = em.createNativeQuery("SELECT a.id FROM Asset a WHERE a.name = ? AND CAST(a.content AS VARCHAR(30000)) = ?").setParameter(1, song.getName()).setParameter(2, song.getContent()).getResultList();

                                    if (dupl == null || (dupl != null && dupl.isEmpty())) {
                                        em.getTransaction().begin();
                                        em.merge(song);
                                        em.getTransaction().commit();
                                        count++;
                                    } else {
                                        dupcount++;
                                    }

                                } catch (Exception ex) {
                                    Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                                    trouble.add(file.getName() + ": \n\t--> " + ex.getMessage());
                                }
                            }

                            //Enttrouble?
                            SongTabController.getInstance().loadSongEditor();
                            SongTabController.getInstance().refreshSongTable();
                            
                            
                           // new ResultDialog(null, false, trouble, "Finished importing " + count + " songs. Ignored " + dupcount + " as they already exists.").setVisible(true);
                            ResultDialog.showMessage(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titleimporter"),java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.finished"), new Object[] {count, dupcount}), false,trouble);
                        }
                    }
                });

    } 
    
    public static void importSNGFiles() {
        Application.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        chooser.setTitle(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titlesng"));
                        chooser.getExtensionFilters().clear();
                        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("*.sng", "*.SNG"));
                        List<File> files = chooser.showOpenMultipleDialog(null);

                        int count = 0;
                        int dupcount = 0;
                        if (files != null && files.size() > 0) {
                            //we have selected stuff. Do an XML parse of XML file
                            //ArrayList<Asset> newsongs = new ArrayList<Asset>();
                            ArrayList<String> trouble = new ArrayList<String>();
                            EntityManager em = EntityOverlord.getInstance().getEm();

                            for (File file : files) {
                                try {
                                    //CLEANUP data first (trim and replace ampersands with &amp;
                                    String str = AppUtils.readFileAsString(file.getAbsolutePath());
                                    str = str.trim(); //trim 
                                    str = str.replaceAll("& ", "&amp; "); //replace ampersand

                                    //System.out.println(str);
                                    //now import
                                    Asset song = new Asset();
                                    Document doc = new SAXBuilder().build(new StringReader(str));

                                    Element root = doc.getRootElement();
                                    Namespace ns = root.getNamespace();

                                    Element attributes = root.getChild("attributes", ns);


                                    song.setType(EntityOverlord.ASSETTYPESONG);

                                    if (attributes.getChild("title", ns) != null) {
                                        song.setName(attributes.getChildText("title", ns));
                                    }
                                    if (attributes.getChild("author", ns) != null) {
                                        song.setAuthor(attributes.getChildText("author", ns));
                                    }

                                    StringBuilder sb = new StringBuilder();
                                    for (Object v : root.getChild("verses", ns).getChildren("verse", ns)) {
                                        Element verse = (Element) v;

                                        //soung verses
                                        if (verse != null) {
                                            sb.append(verse.getTextTrim()).append("\n\n");
                                        }
                                    }

                                    song.setContent(sb.toString());

                                    song.setAddedOn(new Date());
                                    song.setUpdatedOn(new Date());
                                    List dupl = em.createNativeQuery("SELECT a.id FROM Asset a WHERE a.name = ? AND CAST(a.content AS VARCHAR(30000)) = ?").setParameter(1, song.getName()).setParameter(2, song.getContent()).getResultList();

                                    if (dupl == null || (dupl != null && dupl.isEmpty())) {
                                        em.getTransaction().begin();
                                        em.merge(song);
                                        em.getTransaction().commit();
                                        count++;
                                    } else {
                                        dupcount++;
                                    }

                                } catch (Exception ex) {
                                    Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                                    trouble.add(file.getName() + ": \n\t--> " + ex.getMessage());
                                }
                            }

                            //Enttrouble?
                            SongTabController.getInstance().loadSongEditor();
                            SongTabController.getInstance().refreshSongTable();
                            //new ResultDialog(null, false, trouble, "Finished importing " + count + " songs. Ignored " + dupcount + " as they already exists.").setVisible(true);
                            ResultDialog.showMessage(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titleimporter"),java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.finished"), new Object[] {count, dupcount}), false,trouble);
                        }
                    }
                });

    }
    public static Logger log = Logger.getLogger(Importer.class.getName());

    private static void performEPCImportFromString(String path) throws FileNotFoundException {
        if (path != null && !path.isEmpty()) {
            performEPCImportFromFile(new File(path));
        }
    }

    private static void performEPCImportFromFile(File epcfile){
        if (epcfile == null) {
            chooser.setTitle(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titleewpck"));
            chooser.getExtensionFilters().clear();
            chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("EpicWorship(.epc)","*.epc", "*.EPC"));
            epcfile = chooser.showOpenDialog(null);
        }
        if (epcfile != null && epcfile.exists()) {
            try {
                performEPCImportFromStream(new FileInputStream(epcfile));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void performEPCImportFromStream(InputStream in) {
        log.info(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.importing"));

        int count = 0;
        int dupcount = 0;
        if (in != null) {
            //we have selected stuff. Do an XML parse of XML file
            //ArrayList<Asset> newsongs = new ArrayList<Asset>();
            ArrayList<String> trouble = new ArrayList<String>();
            EntityManager em = EntityOverlord.getInstance().getEm();
            JSONParser parser = new JSONParser();


            try {
                JSONArray array = (JSONArray) (((JSONObject) parser.parse(new InputStreamReader(in))).get("songs"));

                for (int i = 0; i < array.size(); i++) {

                    try {

                        JSONObject node = (JSONObject) array.get(i);

                        Asset song = new Asset();
                        song.setType(EntityOverlord.ASSETTYPESONG);
                        song.setName(node.get("name").toString());
                        if (node.get("author") != null) {
                            song.setAuthor(node.get("author").toString());
                        }
                        song.setContent(node.get("content").toString());

                        List dupl = em.createNativeQuery("SELECT a.id FROM Asset a WHERE a.name = ? AND CAST(a.content AS VARCHAR(30000)) = ?").setParameter(1, song.getName()).setParameter(2, song.getContent()).getResultList();

                        if (dupl == null || (dupl != null && dupl.isEmpty())) {
                            em.getTransaction().begin();
                            em.merge(song);
                            em.getTransaction().commit();
                            count++;
                        } else {
                            dupcount++;
                        }

                    } catch (Exception e) {
                        Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, e);
                        trouble.add("When importing song #" + i + 1 + ": \n\t--> " + e.getMessage());
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                    }
                }




            } catch (ParseException pe) {
                log.severe("position: " + pe.getPosition());
                log.log(Level.SEVERE, null, pe);

            } catch (Exception ex) {
                Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                trouble.add("\n\t--> " + ex.getMessage());
            }

            SongTabController.getInstance().loadSongEditor();
            SongTabController.getInstance().refreshSongTable();
            
            ResultDialog.showMessage(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titleimporter"),java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.finished"), new Object[] {count, dupcount}), false,trouble);
        }




    }

    public static void importEPCFiles() {
        Application.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {

                            performEPCImportFromFile(null);
                    }
                });

    }
    
    public static void importDefaults() {
        Application.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Locale defLoc = Locale.getDefault();
                            if(defLoc.equals(Locale.forLanguageTag("pt-BR"))) {
                            Importer.performEPCImportFromStream(getClass().getResourceAsStream("/media/brl.epc"));
                            } else {
                            Importer.performEPCImportFromStream(getClass().getResourceAsStream("/media/naija.epc"));
                            }
                            
                        } catch (Exception ex) {
                            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                            ResultDialog.showMessage(java.util.ResourceBundle.getBundle("i18n/Bundle").getString("importer.text.titlecouldnot"), "Could Not import Default Songs: " + ex.getMessage());
                        }
                    }
                });

    }

    public static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while (c != -1) {
                c = r.read();
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
