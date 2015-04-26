/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo;

import com.quvizo.universal.EpicOverlord;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 * for error logging and UX
 * @author bufflogic
 */
public class Log
{
    public static void log(Object reporter, Exception e)
    {
        Logger.getLogger(reporter.getClass().getName()).log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    
    public static void log(Object reporter, String message)
    {
        Logger.getLogger(reporter.getClass().getName()).log(Level.INFO, message);
        
    }
    
    public static void log(Object reporter, Exception e, final String message)
    {
        Logger.getLogger(reporter.getClass().getName()).log(Level.SEVERE, message, e);
        new Thread(){

            @Override
            public void run()
            {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                JOptionPane.showMessageDialog(null, message);
            }
            
        }.start();
        
    }
}
