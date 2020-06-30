package com.stickers.laks.whatssappforsticker.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stickers.laks.whatssappforsticker.R;


import java.util.ArrayList;
import java.util.List;


public class StickerListAdapter extends BaseAdapter
{

    // Declare Variables
    Context context=null;
    String stickerName[];
    String authorName[];
    String path[][];
    LayoutInflater inflater;
    Picasso  picasso;
    public ArrayList<LauncherActivity.ListItem> infoItems = new ArrayList();
    public StickerListAdapter()
    {

    }
    public StickerListAdapter(Context context,String stickerName[] ,String author[],String[][] path) {
        this.context = context;
        this.stickerName = stickerName;
        this.authorName =author;
        this.path = path;
    }

    @Override
    public int getCount() {
        return stickerName.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView header,updateDate,author;
        ImageView image1,image2,image3,image4,image5,addWp,removeWp;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(R.layout.sticker_list_item, parent, false);
        header = (TextView) itemView.findViewById(R.id.stickerName);
        author = (TextView) itemView.findViewById(R.id.updateDate);
        image1 = (ImageView)itemView.findViewById(R.id.image1);
        image2 = (ImageView)itemView.findViewById(R.id.image2);
        image3 = (ImageView)itemView.findViewById(R.id.image3);
        image4 = (ImageView)itemView.findViewById(R.id.image4);
        image5 = (ImageView)itemView.findViewById(R.id.image5);
        addWp = (ImageView) itemView.findViewById(R.id.addWhatsApp);
        removeWp = (ImageView)itemView.findViewById(R.id.removePath);

        header.setText(stickerName[position].substring(0,1).toUpperCase()+stickerName[position].substring(1));
        author.setText("• "+authorName[position].substring(0,1).toUpperCase()+authorName[position].substring(1));


        if(path[position][0]!= null){
            Picasso.with(context).load(path[position][0]).into(image1);
        }if(path[position][1]!= null){
            Picasso.with(context).load(path[position][1]).into(image2);
        }if(path[position][2]!= null){
            Picasso.with(context).load(path[position][2]).into(image3);
        }if(path[position][3]!= null){
            Picasso.with(context).load(path[position][3]).into(image4);
        }if(path[position][4]!= null){
            Picasso.with(context).load(path[position][4]).into(image5);
        }



        return itemView;
    }



}
