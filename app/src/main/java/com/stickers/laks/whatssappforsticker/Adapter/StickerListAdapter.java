package com.stickers.laks.whatssappforsticker.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.stickers.laks.whatssappforsticker.R;

import java.util.ArrayList;
import java.util.List;


public class StickerListAdapter extends BaseAdapter
{

    // Declare Variables
    Context context=null;
    List<String> path;
    String stickerName[];
    String createDate[];
    LayoutInflater inflater;
    Picasso  picasso;
    public ArrayList<LauncherActivity.ListItem> infoItems = new ArrayList();

    public StickerListAdapter()
    {

    }
    public StickerListAdapter(Context context,String stickerName[] ,String createDate[]) {
        this.context = context;
        this.stickerName = stickerName;
        this.createDate = createDate;
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
        TextView header,updateDate;
        ImageView image1,image2,image3,image4,image5;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(R.layout.sticker_list_item, parent, false);
        header = (TextView) itemView.findViewById(R.id.stickerName);
        updateDate = (TextView) itemView.findViewById(R.id.updateDate);
        image1 = (ImageView)itemView.findViewById(R.id.image1);
        image2 = (ImageView)itemView.findViewById(R.id.image2);
        image3 = (ImageView)itemView.findViewById(R.id.image3);
        image4 = (ImageView)itemView.findViewById(R.id.image4);
        image5 = (ImageView)itemView.findViewById(R.id.image5);

        header.setText(stickerName[position]);
        updateDate.setText(createDate[position]);

        Picasso.with(context).load(R.drawable.cuppy_lol).into(image1);
        Picasso.with(context).load(R.drawable.cuppy_rofl).into(image2);
        Picasso.with(context).load(R.drawable.cuppy_sad).into(image3);
        Picasso.with(context).load(R.drawable.cuppy_smile).into(image4);
        Picasso.with(context).load(R.drawable.cuppy_lol).into(image5);



        return itemView;
    }


}
