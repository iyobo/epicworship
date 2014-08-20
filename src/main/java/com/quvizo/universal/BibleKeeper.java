/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.universal;


import com.quvizo.data.NamedString;
import com.quvizo.data.entity.BibleTranslation;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import com.quvizo.wrappers.BibleCodex;
import com.quvizo.wrappers.BibleCodexChapters;
import com.quvizo.wrappers.BibleCodexVerses;
import com.quvizo.wrappers.BibleScripture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jdom.JDOMException;

/**
 *
 * @author BuffLogic
 */
public class BibleKeeper
{

    private static BibleKeeper instance;
    
    public static BibleKeeper getInstance()
    {
	if (instance == null)
	{
	    instance = new BibleKeeper();
	}

	return instance;
    }
    Document doc = null;

    private BibleKeeper()
    {
	try
	{
	    if(defLoc.equals(Locale.forLanguageTag("pt-BR"))){
            loadBible("ACF");    
            } else {
            loadBible("KJV");
            }
	}
	catch (Exception ex)
	{
	    Logger.getLogger(BibleKeeper.class.getName()).log(Level.SEVERE, null, ex);
	    JOptionPane.showMessageDialog(null, "EpicWorship can't seem to load the bible. You WILL BE UNABLE TO USE THE BIBLE. It is highly recommended that you restart.");
	}
    }

    public String getScripture(BibleScripture scripture)
    {
	if (doc == null)
	{
	    return "";
	}

	StringBuffer result = new StringBuffer();

	try
	{
	    BibleCodexVerses verses = codex.get(scripture.getBook()).get(scripture.getChapter());

	    if (scripture.getFromverse() == 0 && scripture.getToverse() == 0)
	    {
		//show all verses
		for(int x=0; x<verses.size();x++)
		{
		    result.append(x+1).append(" ").append(verses.get(x)).append("\n\n");
		}
	    }
	    else
	    {
		for (int x = scripture.getFromverse() - 1; x < scripture.getToverse(); x++)
		{
		    result.append(x+1).append(" ").append(verses.get(x)).append("\n\n");
		}
	    }

	}
	catch (Exception e)
	{
	}

	return result.toString();
    }

    public int getChapterCount(String bookname)
    {
	int result = 0;

	if (doc == null)
	{
	    return result;
	}

	//how many chapters in this book?
	try
	{
	    result = codex.get(bookname).size();
	}
	catch (Exception e)
	{
	};

	return result;
    }

    public int getVerseCount(String bookname, int chapter)
    {
	int result = 0;

	if (doc == null)
	{
	    return result;
	}

	//how many verses in this chapter?
	try
	{
	    result = codex.get(bookname).get(chapter).size();
	}
	catch (Exception e)
	{
	}

	return result;
    }
    BibleCodex codex;
    Logger log = Logger.getLogger(BibleKeeper.class.getName());
    /**
     * best done at start-up.
     */
    public void loadBible(String translation) throws IOException, JDOMException
    {
        log.info("Loading bible Version: "+translation);
        // Create the document
        URL rs = getClass().getResource("/media/bibles/"+translation+".xmm");
	doc = new SAXBuilder().build(rs);
	codex = new BibleCodex(translation);
        
        refresh();
    }
    
    public void loadBible(File file) throws IOException, JDOMException
    {
        log.info("Loading bible file: "+file.getAbsolutePath());
        // Create the document
	doc = new SAXBuilder().build(file);
        codex = new BibleCodex(file.getName());
        
        refresh();
	
    }
    
    public void loadBible(BibleTranslation translation) throws IOException, JDOMException
    {
        log.info("Loading bible Version: "+translation);        
        if(translation.getPathtype().equals("internal"))
        {
            doc = new SAXBuilder().build(getClass().getResource(translation.getPath()));
            codex = new BibleCodex(translation.getName());
        }
	else
        {
            loadBible(new File(translation.getPath()));
        }
        
        refresh();
    }
    

    private void refresh() {
        Element bible = doc.getRootElement();
        codex.clear();

	for (Object b : bible.getChildren("b"))
	{
	    Element book = (Element) b;
	    BibleCodexChapters chapters = new BibleCodexChapters();
	    
	    //String strbook = book.getAttributeValue("n");
	   // System.out.println("BOOK: "+strbook);

	    for (Object c : book.getChildren("c")) //run thru chapters
	    {
		Element chapter = (Element) c;
		BibleCodexVerses verses = new BibleCodexVerses();

		//String strchap = chapter.getAttributeValue("n");
		//System.out.println("Chapter=>"+strchap);
		
		for (Object v : chapter.getChildren("v")) //run thru chapters
		{
		    Element verse = (Element) v;
		    
		   // String strverse = verse.getAttributeValue("n");
		    //String strverseContent = verse.getText();
		    
		   // System.out.print(strverse+" ");
		   // System.out.println(strverseContent);
		    
		    verses.add(verse.getText());
		}

		chapters.put(Integer.parseInt(chapter.getAttributeValue("n")),verses);
	    }

	    codex.put(book.getAttributeValue("n"), chapters);
	}

    }
    private Locale defLoc = Locale.getDefault();
    //return defaults
    public List<NamedString> getOldTestamentBooks()
    {
        List<NamedString> list = new ArrayList<NamedString>();
        InputStream in;
        if(defLoc.equals(Locale.ENGLISH)){
        in = this.getClass().getResourceAsStream("/media/bibles/oldtestament.txt");
        } else if(defLoc.equals(Locale.forLanguageTag("pt-BR"))){
        in = this.getClass().getResourceAsStream("/media/bibles/velhotestamento.txt");
        } else if(defLoc.equals(Locale.forLanguageTag("cs-CZ"))){
        in = this.getClass().getResourceAsStream("/media/bibles/staryzakon.txt"); 
        } else {
        in = this.getClass().getResourceAsStream("/media/bibles/oldtestament.txt");    
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        try {
            while ((line = br.readLine()) != null)
            {
                    list.add(new NamedString(line));
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(BibleKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return list;
    }
    
    public List<NamedString> getNewTestamentBooks()
    {
        List<NamedString> list = new ArrayList<NamedString>();
        InputStream in;
        if(defLoc.equals(Locale.ENGLISH)){
        in  = this.getClass().getResourceAsStream("/media/bibles/newtestament.txt");
        } else if(defLoc.equals(Locale.forLanguageTag("pt-BR"))){
        in = this.getClass().getResourceAsStream("/media/bibles/novotestamento.txt"); 
        } else if(defLoc.equals(Locale.forLanguageTag("cs-CZ"))){
        in = this.getClass().getResourceAsStream("/media/bibles/novyzakon.txt"); 
        } else {
        in = this.getClass().getResourceAsStream("/media/bibles/newtestament.txt");    
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        try {
            while ((line = br.readLine()) != null) 
            {
                    list.add(new NamedString(line));
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(BibleKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return list;
    }
    
    public List<BibleTranslation> getTranslations()
    {
        return BibleTranslation.getAll();
    }
    
}
