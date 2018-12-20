package com.stickers.laks.whatssappforsticker.ImageCropper;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.stickers.laks.whatssappforsticker.Activity.Choose_image_crop;
import com.stickers.laks.whatssappforsticker.Activity.CreateStickerPacket;
import com.stickers.laks.whatssappforsticker.Database.Database;
import com.stickers.laks.whatssappforsticker.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CropActivity extends Activity
{
    ImageView compositeImageView;
    boolean crop;
    String uri="";
    Button tryAgain,saveSticker;
    Bitmap resultingImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crop = extras.getBoolean("crop");
            uri = extras.getString("path");
        }

        byte[] byteArray = Choose_image_crop.byteArray;
        resultingImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        compositeImageView = (ImageView) findViewById(R.id.our_imageview);
        tryAgain = (Button) findViewById(R.id.noTryAgain);
        saveSticker = (Button) findViewById(R.id.saveSticker);

        compositeImageView.setImageBitmap(resultingImage);

      /*  Bitmap bitmap2 = loadBitmap(uri);

        WindowManager wm = (WindowManager) CropActivity.this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        bitmap2 = getResizedBitmap(bitmap2,width);
        resultingImage= Bitmap.createBitmap(bitmap2.getWidth(),
               bitmap2.getHeight(), bitmap2.getConfig());

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < FreeCropView.points.size(); i++) {
            path.lineTo(FreeCropView.points.get(i).x, FreeCropView.points.get(i).y);
        }
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }

       // bitmap2 = getResizedBitmap(bitmap2,width);
        canvas.drawBitmap(bitmap2,0,0, paint);
        compositeImageView.setImageBitmap(resultingImage);*/

        tryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                YoYo.with(Techniques.FlipInX)
                        .duration(800)
                        .playOn(tryAgain);

                Intent i = new Intent(CropActivity.this,Choose_image_crop.class);
                Bundle bundle = new Bundle();
                bundle.putString("path",uri);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        saveSticker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(saveSticker);

                    saveBitmapFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            url = "File://"+url;
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 1024);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1)
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }
        else
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CropActivity.this,Choose_image_crop.class);
        Bundle bundle = new Bundle();
        bundle.putString("path",uri);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void saveBitmapFile ( ) throws IOException
    {

        Bitmap bitmap = ((BitmapDrawable)compositeImageView.getDrawable()).getBitmap();
        ContextWrapper cw = new ContextWrapper(CropActivity.this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("mySticker", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"sticker.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();

               /* String path = "File://"+directory.getAbsolutePath();

                Database db = new Database(CropActivity.this);
                db.addStickerPacket("12","MySticker","Umut",path,"20181204","1");
                db.close();*/

               startActivity(new Intent(CropActivity.this, CreateStickerPacket.class));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String path = directory.getAbsolutePath();
        //return directory.getAbsolutePath();
        int a =3;
    }

}