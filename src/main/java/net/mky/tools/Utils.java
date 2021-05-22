/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

/**
 * http://javaonlineguide.net/2018/03/copy-text-and-image-into-clipboard-using-javafx-example.html
 * @author mkfs
 */
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class Utils {


//To copy 

	public static void copyToClipboardText(String s) {

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();

		content.putString(s);
		clipboard.setContent(content);

	}

	public static void copyToClipboardImage(Label lbl) {

		WritableImage snapshot = lbl.snapshot(new SnapshotParameters(), null);
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();

		content.putImage(snapshot);
		clipboard.setContent(content);

	}
        
        public static void copyToClipboardImage(Image lbl) {
 		final Clipboard clipboard = Clipboard.getSystemClipboard();
                clipboard.clear();
		final ClipboardContent content = new ClipboardContent();
                content.clear();//Clear content
		content.putImage(lbl);
		clipboard.setContent(content);

	}

	public static void copyToClipboardImageFromFile(String path) {

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();

		content.putImage(Utils.getImage(path));
		clipboard.setContent(content);

	}

	public static Image getImage(String path) {

		InputStream is = Utils.class.getResourceAsStream(path);
		return new Image(is);
	}
	
	
	public static ImageView setIcon(String path) {

		InputStream is = Utils.class.getResourceAsStream(path);
		ImageView iv = new ImageView(new Image(is));

		iv.setFitWidth(100);
		iv.setFitHeight(100);
		return iv;
	}

	/**
 * COPY TO CLIPBOARD
 * ----------------------------------------------------------------------------------
 * Steps to take when the user requests the current image is copied to the clipboard.
 */
//public void doCopyToClipboardAction()
//{
//  // figure out which frame is in the foreground
//  MetaFrame activeMetaFrame = null;
//  for (MetaFrame mf : frames)
//  {
//    if (mf.isActive()) activeMetaFrame = mf;
//  }
//  // get the image from the current jframe
//  Image image = activeMetaFrame.getCurrentImage();
//  // place that image on the clipboard
//  setClipboard(image);
//}


// code below from exampledepot.com
//This method writes a image to the system clipboard.
//otherwise it returns null.
public static void setClipboard(java.awt.Image image)
{
   ImageSelection imgSel = new ImageSelection(image);
   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
}


// This class is used to hold an image while on the clipboard.
static class ImageSelection implements Transferable
{
  private java.awt.Image image;

  public ImageSelection(java.awt.Image image)
  {
    this.image = image;
  }

  // Returns supported flavors
  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[] { DataFlavor.imageFlavor };
  }

  // Returns true if flavor is supported
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return DataFlavor.imageFlavor.equals(flavor);
  }

  // Returns image
  public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException, IOException
  {
    if (!DataFlavor.imageFlavor.equals(flavor))
    {
      throw new UnsupportedFlavorException(flavor);
    }
    return image;
  }
}
	
}