package com.example.ti.restaurantsearchapi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by TI on 2016/04/17.
 */
public class ConditionAdapter extends SimpleAdapter {
    private int mSwitch;

    public ConditionAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int button) {
        super(context, data, resource, from, to);
        mSwitch = button;
    }

    public View getView(final int position, View converView, final ViewGroup parent) {
        View view = super.getView(position, converView, parent);

        Button btn = (Button) view.findViewById(mSwitch);
        btn.setTag(position);

        final ListView list = (ListView) parent;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterView.OnItemClickListener listener = list.getOnItemClickListener();
                long id = getItemId(position);
                listener.onItemClick((AdapterView<?>) parent, v, position, id);
            }
        });
        return view;
    }
}
