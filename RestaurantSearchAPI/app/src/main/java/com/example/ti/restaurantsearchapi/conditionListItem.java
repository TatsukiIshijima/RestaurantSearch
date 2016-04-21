package com.example.ti.restaurantsearchapi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TI on 2016/04/21.
 * 絞り込み条件リストに入るアイテムクラス
 */
public class conditionListItem {
    private int mImageId;
    private String mTitle;

    public int getmImageId() {
        return mImageId;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public static List<conditionListItem> contentItem() {
        List<conditionListItem> items = new ArrayList<>();

        final String[] condition_name = {"現在地", "ランチ営業", "飲み放題", "食べ放題", "駐車場", "禁煙席"};
        final int[] condition_imageId = {R.drawable.location, R.drawable.lunch, R.drawable.bottomless,
                                          R.drawable.buffet, R.drawable.parking, R.drawable.no_smoking};

        for (int i=0; i<condition_name.length; i++) {
            conditionListItem item = new conditionListItem();
            item.setmTitle(condition_name[i]);
            item.setmImageId(condition_imageId[i]);
            items.add(item);
        }

        return items;
    }
}
