/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author peki
 */
public class ImageUtil
{

    /**
     * Creates a diagonal gradient image.
     * @param upleft
     * @param downright
     * @param size  Be sure this is a power of 2.
     * @return
     */
    public static BufferedImage makeDiagonalGradient(Color upleft, Color downright, int sqsize)
    {
	BufferedImage baseImage = new BufferedImage(sqsize, sqsize, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	Graphics2D g = baseImage.createGraphics();
	g.setPaint(new GradientPaint(0, 0, upleft,
		baseImage.getWidth(), baseImage.getHeight(), downright));
	g.fillRect(0, 0, baseImage.getWidth(), baseImage.getHeight());
	g.dispose();

	return baseImage;
    }

    public static BufferedImage resizeImage(BufferedImage orig, int thumbW)
    {
	double origRatio = (double) orig.getWidth() / (double) orig.getHeight();
	int thumbH = (int) (thumbW * origRatio);
	Image scaled = orig.getScaledInstance(thumbW, thumbH, Image.SCALE_SMOOTH);
	BufferedImage ret = new BufferedImage(thumbW, thumbH, BufferedImage.TYPE_INT_RGB);
	ret.getGraphics().drawImage(scaled, 0, 0, null);
	return ret;
    }
}
