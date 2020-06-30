package com.stickers.laks.whatssappforsticker.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.stickers.laks.whatssappforsticker.Adapter.StickerListAdapter;
import com.stickers.laks.whatssappforsticker.Database.Database;

import com.stickers.laks.whatssappforsticker.R;
import com.stickers.laks.whatssappforsticker.Util.ConstVariables;
import com.stickers.laks.whatssappforsticker.Util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity
{
    ListView stickerList;
    StickerListAdapter stickerListAdapter;
    ImageButton addCreateStickerToolbarButton;
    RelativeLayout createSticker;
    ArrayList<HashMap<String, String>> createStickerExtList;
    ArrayList<HashMap<String, String>> createStickerList;
    String[] stickerID;
    public static int REQUEST_CODE = 100;
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

        isStoragePermissionGranted();


        addCreateStickerToolbarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = String.valueOf(getCreateId(1));
                Intent i = new Intent(MainActivity.this,CreateStickerPacket.class);
                i.putExtra(ConstVariables.STICKER_ID_STR ,id);
                startActivity(i);
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
                String id = String.valueOf(getCreateId(1));
                Intent i = new Intent(MainActivity.this,CreateStickerPacket.class);
                i.putExtra(ConstVariables.STICKER_ID_STR ,id);
                startActivity(i);
            }
        });


       stickerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
       {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id)
           {
               Intent i = new Intent(MainActivity.this,CreateStickerPacket.class);
               i.putExtra(ConstVariables.STICKER_ID_STR , stickerID[position-1]);
               i.putExtra(ConstVariables.EXTRA_SHOW_UP_BUTTON, false);

               startActivity(i);
           }
       });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getStickersPacketInfo();
    }




    public int getCreateId(int id){

        Database db = new Database(MainActivity.this);
        if(db.isHasStickerPacket(String.valueOf(id))){
            id = getCreateId(Utility.getNewUniqueId(id));
        }
        return id;
    }

    public void getStickersPacketInfo(){

        Database db = new Database(MainActivity.this);

        createStickerList = db.getStickerPacket();
        int size = createStickerList.size();

        String name[] = new String[size];
        String author[] = new String[size];
        String path[][] = new String[size][5];
        stickerID = new String[size];


        for (int j = 0; j < createStickerList.size(); j++)
        {
            stickerID[j] = createStickerList.get(size - j - 1).get("pid").toString();
            name[j] = createStickerList.get(size - j - 1).get("name");
            author[j] = createStickerList.get(size - j - 1).get("author");

            createStickerExtList = db.getStickersPath(stickerID[j]);
            int extSize = createStickerExtList.size();

            if(createStickerExtList.size()>5){
                extSize = 5;
            }

            for (int i = 0; i < extSize; i++)
            {
                path[j][i] = createStickerExtList.get(extSize - i - 1).get("path");
            }
        }
        stickerListAdapter = new StickerListAdapter(MainActivity.this,name,author,path);
        stickerList.setAdapter(stickerListAdapter);

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

}


