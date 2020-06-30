package com.stickers.laks.whatssappforsticker.GoogleKeyboard.Util;


import android.content.Context;
import com.stickers.laks.whatssappforsticker.Activity.CreateStickerPacket;
import com.stickers.laks.whatssappforsticker.Database.Database;
import com.stickers.laks.whatssappforsticker.Util.ConstVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StickersDataFactory {

    public static ArrayList<HashMap<String, String>> stickerPathList;

    public static List<Sticker> getAllStickerReference(Context context,String id) {

        Database db = new Database(context);

        stickerPathList = db.getStickersPath(id);
        int size = stickerPathList.size();

        String[] stickerURLRef = new String[size];
        for (int i = 0; i < size; i++)
        {
            stickerURLRef[i]=(stickerPathList.get(size - i - 1).get("path"));

        }

        List<Sticker> stickerList = new ArrayList<>();
        for (int i = 0; i < stickerURLRef.length; i++) {
            stickerList.add(new Sticker(stickerURLRef[i]));
        }
        return stickerList;
    }
}