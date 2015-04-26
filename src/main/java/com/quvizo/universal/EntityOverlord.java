/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.universal;

import com.quvizo.Log;
import com.quvizo.data.SettingName;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.BibleTranslation;
import com.quvizo.data.entity.Setting;
import com.quvizo.data.entity.Version;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;

/**
 *
 * @author peki
 */
public class EntityOverlord {

    private static int DB_VERSION = 1;
    private javax.persistence.EntityManager em;
    private Locale defLoc = Locale.getDefault();

    private EntityOverlord() {
        em = javax.persistence.Persistence.createEntityManagerFactory("EpicWorshipPU").createEntityManager();
        enforceColumnIntegrity();
        prepopulate();
    }
    private static EntityOverlord instance;

    public static EntityOverlord getInstance() {
        if (instance == null) {
            instance = new EntityOverlord();
        }

        return instance;
    }

    public EntityManager getEm() {
        return em;
    }
    public static String ASSETTYPESONG = "song";
    public static String ASSETTYPEMEDIA = "media";
    public static String ASSETTYPEIMAGE = "image";
    public static String ASSETTYPEHTML = "html";
    public static String ASSETTYPEWEBURL = "weburl";

    /**
     * Fixes: "Unable to Save song" bugs.<br/> Hibernate's Derby dialect is
     * silly in that it treats a CLOB as a varchar(255) when creating it in the
     * DB itself. This create's an interesting problem where if you try to save
     * a song that is very large, it would throw an exception (Better this than
     * silent truncation at least so we know there is a problem).<br/><br/>
     *
     * This was never caught as an issue because we mostly changed to hibernate
     * recently from Eclipselink. Eclipselink generates the Tables and columns
     * just fine. We moved from it because it wasn't really updating the table
     * structures on the go according to the definition of the entity classes.
     * Maybe it was just me, but it didn't work and so I moved it to Hibernate
     * which works, save for this particular issue.<br/><br/>
     *
     * We initially had Asset.content as a CLOB in hibernate, so we are changing
     * it to a Varchar of limit 32000. This should be enough. <br/><br/>
     *
     * ***wags finger at Hibernate***
     *
     * - Philip Iyobose Eki
     */
    private void enforceColumnIntegrity() {

        boolean doit = false;
        try {
            Version v = em.find(Version.class, 1);

            if (v != null && v.getNumber() >= DB_VERSION) {
                Log.log(this, "DB Version is " + v.getNumber());
            } else {
                Log.log(this, "DB Versioning Notice: This Database is Old. Upgrading...");
                doit = true;
            }
        } catch (Exception e) {
            Log.log(this, e);
            doit = false;
        }

        //this is an old table structure. Cnvert type the only way derby allows.
        if (doit) {
            upgradeToVersion1();
        }
    }

    /**
     * *
     * This version
     */
    private void upgradeToVersion1() {
        try {
            em.getTransaction().begin();
            em.createNativeQuery("ALTER TABLE Asset ADD COLUMN content_new VARCHAR(32000)").executeUpdate();
            em.createNativeQuery("UPDATE Asset SET content_new = content").executeUpdate();
            em.createNativeQuery("RENAME COLUMN Asset.content TO content_old").executeUpdate();
            em.createNativeQuery("RENAME COLUMN Asset.content_new TO content").executeUpdate();
            em.createNativeQuery("ALTER TABLE Asset DROP COLUMN content_old").executeUpdate();

            //Set this as DB Version 1
            Version v = new Version(1);
            v.setNumber(1);
            em.merge(v);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Log.log(this, ex, "EpicWorship encountered an error in Data initialization.\n If you have issues saving songs, try reducing the size of it to be less than 255 characters.\nSorry for the inconvenience");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    /**
     * *
     * Put anything in here you wish to ensure is in the database at all times,
     * Like defaults.
     */
    private void prepopulate() {
        //if there are no default bible translations available, create them
        List<BibleTranslation> versions = em.createNamedQuery("BibleTranslation.findAll").getResultList();
        if (versions.size() < 4) {
            Logger.logMsg(Logger.INFO, "populating Bible translation index");
            if (defLoc.equals(Locale.forLanguageTag("pt-BR"))) {
                em.persist(new BibleTranslation("ACF", "/media/bibles/ACF.xmm", "internal"));
                em.persist(new BibleTranslation("RA", "/media/bibles/RA.xmm", "internal"));
                em.persist(new BibleTranslation("NVI", "/media/bibles/NVI.xmm", "internal"));
                em.persist(new BibleTranslation("NTLH", "/media/bibles/NTLH.xmm", "internal"));
            } else {
                em.persist(new BibleTranslation("KJV", "/media/bibles/KJV.xmm", "internal"));
                em.persist(new BibleTranslation("ESV", "/media/bibles/ESV.xmm", "internal"));
                em.persist(new BibleTranslation("AMP", "/media/bibles/AMP.xmm", "internal"));
                em.persist(new BibleTranslation("MSG", "/media/bibles/MSG.xmm", "internal"));
            }
        }

        //Test if translation already exist on database
        List<String> listvers = new ArrayList<String>();
        for (BibleTranslation t : versions) {
            listvers.add(t.getName());            
        }

        //Unity test of external bibles add One more file(teste)
        File bibles = new File("bibles");
        if (bibles.exists() && bibles.isDirectory()) {
            List<File> xmms = searchRecursive(bibles, ".xmm");
            for (File xmm : xmms) {
                if (xmm.exists()) {
                    String name = xmm.getName().replaceFirst(".xmm", "");
                    //test if translation already exist on database
                    if (!listvers.contains(name)) {
                        String abspath = xmm.getAbsolutePath();
                        System.out.println("Novo Teste: " + xmm.getAbsolutePath());
                        em.persist(new BibleTranslation(name, abspath, "external"));
                    }
                }
            }
        }
    }

    // Testing new bible populate
    public void repopulate() {
        //if there are new bible translations available on bible(folder), create them
        List<BibleTranslation> versions = em.createNamedQuery("BibleTranslation.findAll").getResultList();
        if (versions.size() < 4) {
            Logger.logMsg(Logger.INFO, "populating Bible translation index");
            if (defLoc.equals(Locale.forLanguageTag("pt-BR"))) {
                em.persist(new BibleTranslation("ACF", "/media/bibles/ACF.xmm", "internal"));
                em.persist(new BibleTranslation("RA", "/media/bibles/RA.xmm", "internal"));
                em.persist(new BibleTranslation("NVI", "/media/bibles/NVI.xmm", "internal"));
                em.persist(new BibleTranslation("NTLH", "/media/bibles/NTLH.xmm", "internal"));
            } else {
                em.persist(new BibleTranslation("KJV", "/media/bibles/KJV.xmm", "internal"));
                em.persist(new BibleTranslation("ESV", "/media/bibles/ESV.xmm", "internal"));
                em.persist(new BibleTranslation("AMP", "/media/bibles/AMP.xmm", "internal"));
                em.persist(new BibleTranslation("MSG", "/media/bibles/MSG.xmm", "internal"));
            }
        }
        //Test if translation already exist on database
        List<String> listvers = new ArrayList<String>();
        for (BibleTranslation t : versions) {
            listvers.add(t.getName());            
        }

        //Unity test of external bibles add One more file(teste)
        File bibles = new File("bibles");
        if (bibles.exists() && bibles.isDirectory()) {
            List<File> xmms = searchRecursive(bibles, ".xmm");
            for (File xmm : xmms) {
                if (xmm.exists()) {
                    String name = xmm.getName().replaceFirst(".xmm", "");
                    //test if translation already exist on database
                    if (!listvers.contains(name)) {
                        String abspath = xmm.getAbsolutePath();
                        System.out.println("Novo Teste: " + xmm.getAbsolutePath());
                        em.persist(new BibleTranslation(name, abspath, "external"));
                    }
                }
            }
        }
    }

    //Method to search and list all files in one directory
    public List<File> searchRecursive(File folder, String ext) {
        List<File> result = new ArrayList<File>();
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                result.addAll(searchRecursive(f, ext));
            } else if (f.getName().endsWith(ext)) {
                result.add(f);
            }
        }
        return result;
    }
}
