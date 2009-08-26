/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.johnstonshome.jenatool.ui.Activator;

public class ImageCache {

//	private static Map<String, ImageDescriptor> descriptorCache = new HashMap<String, ImageDescriptor>();
	private static Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();

	public static Image getImage(ImageDescriptor descriptor) {
		Image image = (Image)imageCache.get(descriptor);
	    if (image == null) {
	        image = descriptor.createImage();
	        imageCache.put(descriptor, image);
	    }
	    return image;
	}

	public static Image getImage(String imagePath) {
		ImageDescriptor descriptor = Activator.getImageDescriptor(imagePath);
		return getImage(descriptor);
	}
}
