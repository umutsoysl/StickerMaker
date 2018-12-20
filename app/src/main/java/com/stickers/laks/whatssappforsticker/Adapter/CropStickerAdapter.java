package com.stickers.laks.whatssappforsticker.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.stickers.laks.whatssappforsticker.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CropStickerAdapter extends BaseAdapter {
    ArrayList<String> images;
    Context context;
    String[] point;
    LayoutInflater inflater;
    // Constructor
    public CropStickerAdapter(Context c,ArrayList<String> image,String[] pos) {
        context = c;
        this.images=image;
        point=pos;
    }

    public int getCount() {
        return point.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

          ImageView setimage,chooseImage;
          ImageButton removeImage;

        if(convertView == null)
        {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.girdview_item, parent, false);
        }
          setimage = (ImageView) convertView.findViewById(R.id.setImage);
          chooseImage = (ImageView) convertView.findViewById(R.id.choose_image);
          removeImage = (ImageButton) convertView.findViewById(R.id.removeImage);


        /*  if(images!=null||images.size()!=0||(images.get(position)!=null&&images.get(position).length()>5)){
              setimage.setImageBitmap(convertBitmap(images.get(position).toString()));
          }
*/
      return convertView;
    }

    public Bitmap convertBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
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
}