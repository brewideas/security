package com.infius.proximitysecurity.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.images.Size;
import com.infius.proximitysecurity.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author kshemendra.jangir@paytm.com (Kshemendra Jangir)
 */
public final class ViewFinder extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 80L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private static final int frameOffset = 0;
    private static final String TAG = ViewFinder.class.getSimpleName();

    private final Paint paint;
    private Bitmap resultBitmap;
//    public final int maskColor;
//    private final int resultColor;
//    private final int laserColor;
//    private final int resultPointColor;
    private int scannerAlpha;
//    private List<ResultPoint> possibleResultPoints;
//    private List<ResultPoint> lastPossibleResultPoints;
    private boolean isScanOnly;
    public static final int MIN_FRAME_WIDTH = 240;
    public static final int MIN_FRAME_HEIGHT = 240;
    public static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
    public static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080
    private CameraSource mCameraSource;
    private boolean swipingOn;

    // This constructor is used when the class is built from an XML resource.
    public ViewFinder(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
//        maskColor = resources.getColor(R.color.viewfinder_mask);
//        resultColor = resources.getColor(R.color.result_view);
//        laserColor = resources.getColor(R.color.viewfinder_laser);
//        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
//        possibleResultPoints = new ArrayList<>(5);
//        lastPossibleResultPoints = null;
    }

    public void setScanOnly(boolean isScanOnly) {
        this.isScanOnly = isScanOnly;
    }

    public Rect getScanOnlyFramingRect() {
        Point screenResolution = getScreenResolution();
        if (screenResolution == null) {
            // Called early, before init even finished
            return null;
        }

        int width = findScanOnlyDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
        int height = findScanOnlyDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

        int leftOffset = (screenResolution.x - width) / 2;
//        int topOffset = (screenResolution.y - height) / 3 ;
        int topOffset = (getHeight() - width) / 2;
        //Done to make square frame
        Rect framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + width);

//      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        return framingRect;
    }

    private static int findScanOnlyDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
        int dim = (int) (5.8 * resolution / 8); // Target 5/8 of each dimension
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }

//    public synchronized Rect getFramingRect() {
//        if (isScanOnly) {
//            return getScanOnlyFramingRect();
//        }
//        Point screenResolution = getScreenResolution();
//        if (screenResolution == null) {
//            // Called early, before init even finished
//            return null;
//        }
//
//        int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
//        int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);
//
//        int leftOffset = (screenResolution.x - width) / 2;
////      int topOffset = (screenResolution.y - height) / 2;
//
//        //Done to make square frame
//        int topOffset = (screenResolution.y - width) / 4;
//        Rect framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + width);
//
////      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
//        return framingRect;
//    }


    public synchronized Rect getFramingRect() {
        if (isScanOnly) {
            return getScanOnlyFramingRect();
        }
        Point screenResolution = getScreenResolution();
        if (screenResolution == null) {
            // Called early, before init even finished
            return null;
        }

        int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
        int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

        int leftOffset = (screenResolution.x - width) / 2;
//      int topOffset = (screenResolution.y - height) / 2;

        //Done to make square frame
        int topOffset = (getHeight() - width) / 2;
        Rect framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + width);

//      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        return framingRect;

    }

    private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
        int dim = (int) (5.0 * resolution / 9); // Target 5/8 of each dimension
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
//        if (cameraManager == null) {
//            return; // not ready yet, early draw before done configuring
//        }
        Rect frame = getFramingRect();
        Rect previewFrame = getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.colorPrimary));
        if (swipingOn) {
            canvas.drawRect(0, 0, width, height, paint);
        } else {
            canvas.drawRect(0, 0, width, frame.top, paint);
            canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
            canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
            canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        }

        // draw the blue color frame around the viewfinder
        NinePatchDrawable drawable = (NinePatchDrawable) getResources().getDrawable(R.drawable.frame);
        drawable.setBounds(frame.left - frameOffset, frame.top - frameOffset, frame.right + frameOffset, frame.bottom + frameOffset);
        drawable.draw(canvas);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);

        } else {

            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + frame.top;
//      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

//            List<ResultPoint> currentPossible = possibleResultPoints;
//            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
//            if (currentPossible.isEmpty()) {
//                lastPossibleResultPoints = null;
//            } else {
//                possibleResultPoints = new ArrayList<>(5);
//                lastPossibleResultPoints = currentPossible;
//                paint.setAlpha(CURRENT_POINT_OPACITY);
//                paint.setColor(resultPointColor);
//                synchronized (currentPossible) {
////                    for (ResultPoint point : currentPossible) {
////                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
////                                frameTop + (int) (point.getY() * scaleY),
////                                POINT_SIZE, paint);
////                    }
//                }
//            }
//            if (currentLast != null) {
//                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
//                paint.setColor(resultPointColor);
//                synchronized (currentLast) {
//                    float radius = POINT_SIZE / 2.0f;
////                    for (ResultPoint point : currentLast) {
////                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
////                                frameTop + (int) (point.getY() * scaleY),
////                                radius, paint);
////                    }
//                }
//            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

//    public void addPossibleResultPoint(ResultPoint point) {
//        List<ResultPoint> points = possibleResultPoints;
//        synchronized (points) {
//            points.add(point);
//            int size = points.size();
//            if (size > MAX_RESULT_POINTS) {
//                // trim it
//                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
//            }
//        }
//    }

    public Point getScreenResolution() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point();
        display.getSize(theScreenResolution);
        return theScreenResolution;
    }

    public Rect getFramingRectInPreview() {
        Rect framingRect = getFramingRect();
        if (framingRect == null) {
            return null;
        }
        Rect rect = new Rect(framingRect);
        Point cameraResolution = getCameraResolution();
        Point screenResolution = getScreenResolution();
        if (cameraResolution == null || screenResolution == null) {
            // Called early, before init even finished
            return null;
        }

        rect.left = rect.left * cameraResolution.y / screenResolution.x;
        rect.right = rect.right * cameraResolution.y / screenResolution.x;
        rect.top = rect.top * (cameraResolution.x + rect.top) / screenResolution.y;
        rect.bottom = rect.bottom * (cameraResolution.x + rect.top) / screenResolution.y;

//      rect.left = rect.left * cameraResolution.x / screenResolution.x;
//      rect.right = rect.right * cameraResolution.x / screenResolution.x;
//      rect.top = rect.top * cameraResolution.y / screenResolution.y;
//      rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
        return rect;
    }

    public void setCameraSource(CameraSource mCameraSource) {
        this.mCameraSource = mCameraSource;
    }

    public Point getCameraResolution() {
        if (mCameraSource != null) {
            Size size = mCameraSource.getPreviewSize();
            if (size == null) {
                return getScreenResolution();
            }
            return new Point(size.getWidth(), size.getHeight());
        } else {
            return null;
        }
    }

    public void setSwipingOn(boolean swipingOn) {
        this.swipingOn = swipingOn;
    }
}
