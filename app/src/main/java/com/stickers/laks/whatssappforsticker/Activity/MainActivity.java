package com.stickers.laks.whatssappforsticker.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.stickers.laks.whatssappforsticker.Adapter.StickerListAdapter;
import com.stickers.laks.whatssappforsticker.R;
import java.util.ArrayList;

public class MainActivity extends Activity
{
    ListView stickerList;
    StickerListAdapter stickerListAdapter;
    ImageButton addCreateStickerToolbarButton;
    RelativeLayout createSticker;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater2.inflate(R.layout.main_activity_listheader, null);
        stickerList = (ListView) findViewById(R.id.stickerList);
        addCreateStickerToolbarButton = (ImageButton) findViewById(R.id.addStickerPath);
        createSticker = (RelativeLayout) listHeader.findViewById(R.id.createSticker);

        stickerList.addHeaderView(listHeader);
        stickerList.setItemsCanFocus(true);


        addCreateStickerToolbarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,CreateStickerPacket.class));
            }
        });

        createSticker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                YoYo.with(Techniques.FadeIn)
                        .duration(150)
                        .playOn(createSticker);


                startActivity(new Intent(MainActivity.this,CreateStickerPacket.class));
            }
        });


        String[] name = new String[]{"Cuppy"};
        String[] date = new String[]{"23 Sep. 23:37"};

        stickerListAdapter = new StickerListAdapter(MainActivity.this,name,date);
        stickerList.setAdapter(stickerListAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(new FreeCropView(MainActivity.this));
    }

}


