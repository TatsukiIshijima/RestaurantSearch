package com.example.ti.restaurantsearchapi;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import java.util.List;
import java.util.Map;

/**
 * Created by TI on 2016/04/17.
 * 絞り込み条件リストのためのアダプター
 */
public class ConditionAdapter extends SimpleAdapter {
    private int aSwitch;

    public ConditionAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int mSwitch) {
        super(context, data, resource, from, to);
        aSwitch = mSwitch;
    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Switch sw = (Switch) view.findViewById(aSwitch);
        // Tagでpositionの設定
        sw.setTag(position);

        //final ListView list = (ListView) parent;

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int i = (Integer) buttonView.getTag();

                if (isChecked) {
                    Log.d("SwitchTest:", "position" + i + "true");
                } else {
                    Log.d("SwitchTest:", "position" + i + "false");
                }
            }
        });

        return view;
    }
}
