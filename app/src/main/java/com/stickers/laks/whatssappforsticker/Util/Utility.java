package com.stickers.laks.whatssappforsticker.Util;

import java.util.concurrent.atomic.AtomicInteger;


public class Utility
{

    public static int getNewUniqueId(int start){

        AtomicInteger at = new AtomicInteger(start);
        return at.incrementAndGet();
    }

}
