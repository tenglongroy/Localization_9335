package sensor.project.roy.localization_9335;
//http://www.cnblogs.com/plokmju/p/android_Canvas.html

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.widget.ImageView;

/*
http://blog.csdn.net/huim_lin/article/details/17019751
http://www.eoeandroid.com/thread-73018-1-1.html?_dsign=31139581
http://fecbob.pixnet.net/blog/post/38335253
http://gundumw100.iteye.com/blog/1336145
*/
class CanvasLocation{
    Context mContext;
    ImageView mImageView;
    private Bitmap baseBitmap;
    private Canvas canvas = null;
    private Paint paint;

    /*int width = 901;
    int height = 469;*/

    CanvasLocation(Context context, View view)
    {
        this.mContext = context;
        this.mImageView = (ImageView)view;
        paint = new Paint();
        paint.setStrokeWidth(7);
        paint.setColor(Color.RED);
        //resumeCanvas();   in OnCreate() the width and height are 0, until OnMeasure()
        }

    public void resumeCanvas() {
        baseBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(), Bitmap.Config.ARGB_8888);
        if(canvas == null) {
            canvas = new Canvas(baseBitmap);
            canvas.drawColor(Color.TRANSPARENT);
        }
        else
            canvas.setBitmap(baseBitmap);
    }

    public void drawLocation(float x, float y){
        resumeCanvas();
        float[] coors = calCoor(x,y);
        //canvas.drawPoint(50, 50, paint);
        canvas.drawCircle(coors[0], coors[1], 20, paint);
        mImageView.setImageBitmap(baseBitmap);
    }
    public void clearSCR(){
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }
    private float[] calCoor(float x, float y){
        float width = mImageView.getWidth();
        float height = mImageView.getHeight();
        float wid_block = width/12;
        float hei_block = height/6;
        float[] result = new float[2];
        result[0] = (2*x-1)*wid_block;      //x's real coordinate
        result[1] = (7-2*y)*hei_block;      //y's ...
        return result;
    }
}