package com.stickers.laks.whatssappforsticker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stickers.laks.whatssappforsticker.Activity.CreateStickerPacket;
import com.stickers.laks.whatssappforsticker.Database.Database;
import com.stickers.laks.whatssappforsticker.Model.JsonStickerModel;
import com.stickers.laks.whatssappforsticker.R;
import com.stickers.laks.whatssappforsticker.Util.ConstVariables;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CropStickerAdapter extends BaseAdapter {
    List<String> images;
    Context context;
    String[] point;
    String[] itemId;
    public static  String StickerId;
    LayoutInflater inflater;
    // Constructor
    public CropStickerAdapter(Context c,List<String> image,String[] pos,String id, String[] stickerId) {
        context = c;
        this.images=image;
        point=pos;
        StickerId = id;
        itemId = stickerId;
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

        Fila view = null;

        if(convertView == null)
        {
            view = new Fila();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.girdview_item, parent, false);
            view.setimage = (ImageView) convertView.findViewById(R.id.setImage);
            view.chooseImage = (ImageView) convertView.findViewById(R.id.choose_image);
            view.removeImage = (ImageButton) convertView.findViewById(R.id.removeImage);
            view.removewImageLyt = (RelativeLayout) convertView.findViewById(R.id.deleteImage);
            view.number = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(view);

        }else{
            view = (Fila) convertView.getTag();
        }

        view.number.setText(String.valueOf(position+1));

          if(images.size()>0 && images.size()>position && images.get(position).toString().length()>10)
          {
              //view.setimage.setImageURI(Uri.parse(images.get(position)));
              setImage(images.get(position), view.setimage);
              view.chooseImage.setVisibility(View.INVISIBLE);
              view.removeImage.setVisibility(View.VISIBLE);
          }else{
              view.chooseImage.setVisibility(View.VISIBLE);
              view.removeImage.setVisibility(View.INVISIBLE);
          }

          view.removewImageLyt.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v)
              {

                  File fdelete = new File( Uri.parse(images.get(position).toString()).getPath());

                  if (fdelete.exists()) {

                      if (fdelete.delete()) {

                          Database db = new Database(context);
                          db.removeStickerPath(itemId[position]);
                          db.close();

                          images.remove(position);

                          Intent i = new Intent(context,CreateStickerPacket.class);
                          i.putExtra(ConstVariables.STICKER_ID_STR ,StickerId);
                          context.startActivity(i);

                      } else {
                          Toast.makeText(context,"file not Deleted",Toast.LENGTH_LONG).show();
                      }
                  }else {
                      Toast.makeText(context,"file not Deleted",Toast.LENGTH_LONG).show();
                  }
              }
          });

      return convertView;
    }
    public void setImage(final String url, final ImageView imageview){

        Picasso.with(context).load(url).into(imageview);
    }

    public class Fila {

        ImageView setimage = null,chooseImage = null;
        ImageButton removeImage = null;
        TextView number = null;
        RelativeLayout removewImageLyt = null;

    }

}