package com.example.ti.restaurantsearchapi;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private LocationManager mLocationManager;
    private List<Map<String, String>> mList;
    private final String[] item = new String[] {
            "ランチ営業",
            "飲み放題",
            "食べ放題",
            "駐車場",
            "禁煙席"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;

        // GPSで現在地取得
        //mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //boolean gpsFlg = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Log.d("GPS Enabled", gpsFlg? "OK":"NG");

        final TextView rangeText = (TextView) findViewById(R.id.rangeText);
        SeekBar seekBar = (SeekBar) findViewById(R.id.rangeSeekbar);
        Button button = (Button) findViewById(R.id.search_button);
        ListView listView = (ListView) findViewById(R.id.detail_list);

        // アダプタ生成
        mList = new ArrayList<>();
        final ConditionAdapter adapter = new ConditionAdapter(this, mList, R.layout.condition_item,
                new String[]{"title"},
                new int[]{R.id.condition_text},
                R.id.condition_switch);

        // クリックイベント処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("itemClick", "position=" + String.valueOf(position));
            }
        });

        // アダプターセット
        listView.setAdapter(adapter);

        // 表示するデータ設定
        for (int i=0; i<5; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", item[i]);
            mList.add(map);
        }

        rangeText.setText("検索範囲 : 周囲 300 m");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.freeword);
                String freeword = editText.getText().toString();

                // レストラン一覧画面の起動
                Intent intent = new Intent(activity, RestlistActivity.class);

                // 検索ワードを次の画面へ渡す
                intent.putExtra("FreeWord", freeword);
                activity.startActivity(intent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Seekbarを動かした時
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String range = new String();

                if (seekBar.getProgress() == 1) {
                    range = "500";
                } else if (seekBar.getProgress() == 2) {
                    range = "1000";
                } else if (seekBar.getProgress() == 3) {
                    range = "2000";
                } else if (seekBar.getProgress() == 4) {
                    range = "3000";
                } else {
                    range = "300";
                }
                rangeText.setText("検索範囲 : 周囲 " + range + " m");
            }

            // Seekbarに触れたとき
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // Seekbarから離れたとき
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
