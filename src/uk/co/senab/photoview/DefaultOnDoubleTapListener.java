package uk.co.senab.photoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * Provided default implementation of GestureDetector.OnDoubleTapListener, to be overriden with custom behavior, if needed
 * <p>&nbsp;</p>
 * To be used via {@link uk.co.senab.photoview.PhotoViewAttacher#setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener)}
 */
public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    private static final int MATCH_PARENT = -1;
	private PhotoViewAttacher photoViewAttacher;

    /**
     * Default constructor
     *
     * @param photoViewAttacher PhotoViewAttacher to bind to
     */
    public DefaultOnDoubleTapListener(PhotoViewAttacher photoViewAttacher) {
        setPhotoViewAttacher(photoViewAttacher);
     
    }

    /**
     * Allows to change PhotoViewAttacher within range of single instance
     *
     * @param newPhotoViewAttacher PhotoViewAttacher to bind to
     */
    public void setPhotoViewAttacher(PhotoViewAttacher newPhotoViewAttacher) {
        this.photoViewAttacher = newPhotoViewAttacher;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.photoViewAttacher == null) {
        	
        	return false;
        }
        
        ImageView imageView = photoViewAttacher.getImageView();
     
       
        if (null != photoViewAttacher.getOnPhotoTapListener()) {
            final RectF displayRect = photoViewAttacher.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();

                // Check to see if the user tapped on the photo
                if (displayRect.contains(x, y)) {

                    float xResult = (x - displayRect.left)
                            / displayRect.width();
                    float yResult = (y - displayRect.top)
                            / displayRect.height();

                    photoViewAttacher.getOnPhotoTapListener().onPhotoTap(imageView, xResult, yResult);
                    return true;
                }
            }
        }
        if (null != photoViewAttacher.getOnViewTapListener()) {
            photoViewAttacher.getOnViewTapListener().onViewTap(imageView, e.getX(), e.getY());
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        
    	if (photoViewAttacher == null)
            return false;
    	// Rui: when double taps happen, we need to change the image view layout 
        //back to fill parent, so that the first double taps give a good view. :)
    	
    	LayoutParams params = photoViewAttacher.getImageView().getLayoutParams();
    	if (params.height != MATCH_PARENT 
    			&& params.width != MATCH_PARENT) {
    		
    		params.height = MATCH_PARENT;
    		params.width = MATCH_PARENT;
    		photoViewAttacher.getImageView().setLayoutParams(params);
    		return true;
    	}
	
        try {
            float scale = photoViewAttacher.getScale();
            float x = ev.getX();
            float y = ev.getY();
            
            // Rui: first double taps make the view fill the screen.
            //photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), x, y, true);

            if (scale >= photoViewAttacher.getMinimumScale() && scale < photoViewAttacher.getMediumScale()) {
            	sop("scale = " + scale);
                photoViewAttacher.setScale(photoViewAttacher.getMediumScale(), x, y, true);
            } else if (scale >= photoViewAttacher.getMediumScale() && scale < photoViewAttacher.getMaximumScale()) {
            	sop("scale = " + scale);
                photoViewAttacher.setScale(photoViewAttacher.getMaximumScale(), x, y, true);
            }
            else 
            	photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), x, y, true);
            
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }
        
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Wait for the confirmed onDoubleTap() instead
        return false;
    }
    
    public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}

}
