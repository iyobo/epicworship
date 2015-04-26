package com.quvizo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BuffLogic
 */
public class PropUtils
{

    private static final Logger logger = Logger.getLogger(PropUtils.class.getName());
    private Properties settingProps;
    private static PropUtils instance;
    private static final String SETTINGS_PROPFILE = AppUtils.getConfigItemPath("EpicSettings.properties");

    private PropUtils()
    {
        settingProps = new Properties();

        try
        {
            FileInputStream in = new FileInputStream(SETTINGS_PROPFILE);
            settingProps.load(in);
            in.close();
        } catch (IOException ex)
        {
            Logger.getLogger(PropUtils.class.getName()).log(Level.SEVERE, null, ex);
        }



        /*
         * catch (IOException ex) { logger.severe("Failed to load settings file:
         * " + SETTINGS_PROPFILE, ex); System.exit(1); }
         */
        instance = this;
    }

    public static PropUtils getInstance()
    {
        if (instance == null)
        {
            instance = new PropUtils();
        }

        return instance;
    }

    
    public Integer getInteger(String key, Integer def)
    {
        Integer ret = getInteger(key);
        if(ret==null||ret==-1)
            ret = def;
        
        return ret;
    }
    /**
     * Get a number from the settings properties.
     *
     * @param key
     * @return Null if key doesn't exist or if key is not "int-able"
     */
    public Integer getInteger(String key)
    {
        String keyval = settingProps.getProperty(key);
        if (keyval == null)
        {
            logger.warning("Property does not exist: " + key);
            return null;
        }

        Integer i = Integer.parseInt(keyval);
        if (i == null)
        {
            logger.warning("Property could not be turned into an integer: " + key);
            i = -1;
        }

        return i;
    }

    public Properties getSettingProps()
    {
        return settingProps;
    }

    public String getProperty(String key)
    {
        return getProperty(key, null);
    }
    public String getProperty(String key,String def)
    {
        String keyval = settingProps.getProperty(key,def);
        if (keyval == null)
        {
            keyval = "";
        }

        return keyval;
    }

    public void setProperty(String key, String value)
    {
        settingProps.setProperty(key, value);
        //save();
    }
    
    public void setProperty(String key, Integer value)
    {
        settingProps.setProperty(key, value.toString());
        //save();
    }

    public void save()
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(SETTINGS_PROPFILE);
            settingProps.store(out, "---No Comment---");
            out.close();
        } catch (IOException ex)
        {
            Logger.getLogger(PropUtils.class.getName()).log(Level.SEVERE, null, ex);
        }finally
        {
            try
            {
                out.close();
            } catch (IOException ex)
            {
                Logger.getLogger(PropUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
