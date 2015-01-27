/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.nodes;


import com.quvizo.config.EpicSettings;
import java.util.logging.Logger;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import com.quvizo.universal.UI;

/**
 *
 * @author peki
 */
public class PXtextNode extends PXNode
{
    private static Logger logger = Logger.getLogger(PXtextNode.class.getName());
    
    public PXtextNode(double x,double y,double maxwidth, double maxheight,Font font,String text,TextAlignment align, VPos vpos)
    {
        node = new Text();
        
        Text textnode = (Text)node;
	textnode.setLayoutX(x);
	textnode.setLayoutY(y);
	
	textnode.setText(text);
	textnode.setTextAlignment(align);
        
	textnode.setFont(font);
	textnode.setWrappingWidth(maxwidth);
	//node.autosize();
        textnode.setTextOrigin(vpos);
	
	//just like ol' jworship. make it an image so we're not drawing from scratch all the time. But increases memory usage. I saw whatever.
	textnode.setCache(true);
	
        //auto resize
	while( textnode.boundsInLocalProperty().getValue().getHeight()> maxheight && textnode.getFont().getSize()>5)
	{
	    logger.config(textnode.boundsInLocalProperty().getValue().getHeight()+" height is bigger than "+ maxheight+". Reducing font size.");
            textnode.setFont(new Font(EpicSettings.getFontType(),textnode.getFont().getSize()-4)); /* Modificado por Ítalo */
	}

	//Effects
	DropShadow ds = new DropShadow();
	ds.setRadius(UI.TEXTSHADOWRADIUS);
	ds.setColor(UI.TEXTSHADOWCOLOR);
	ds.setSpread(UI.TEXTSHADOWDIMNESS);
	
        //Include or not the shadow effect 
	if(EpicSettings.getHaveShadow().equalsIgnoreCase("yes")){
        textnode.setEffect(ds);
        }

        //Include or not the stroke effect
        if(EpicSettings.getHaveStroke().equalsIgnoreCase("yes")){
        textnode.setStyle("-fx-stroke: black;-fx-stroke-width: 3;");
        }
        
	textnode.setFill(UI.TEXTCOLOR);

        
        logger.config("using "+textnode.getFont().getSize());
    }

    public Text getTextNode()
    {
        return ((Text)node);
    }
    
}
