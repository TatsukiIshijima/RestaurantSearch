package com.example.ti.restaurantsearchapi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* レストラン検索画面 */

public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager mLocationManager;
    private String latitude;
    private String longitude;
    private List<Map<String, String>> mList;                                                      // 絞り込み条件リスト
    private final String[] item = new String[]{
            "ランチ営業",
            "飲み放題",
            "食べ放題",
            "駐車場",
            "禁煙席"
    };
    private int range_number;                                                                   // 検索範囲
    private boolean lunch;                                                                      // 絞り込み条件(ランチ営業)
    private int lunch_val;
    private boolean bottomless;                                                                // 絞り込み条件(飲み放題)
    private int bottom_val;
    private boolean buffet;                                                                     // 絞り込み条件(食べ放題)
    private int buffet_val;
    private boolean parking;                                                                    // 絞り込み条件(駐車場)
    private int parking_val;
    private boolean no_smoking;                                                                 // 絞り込み条件(禁煙席)
    private int no_smoking_val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;

        // GPSで現在地取得
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // GPSがONになっているか確認
        //String providers = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //if (!mLocationManager.isProviderEnabled(LOCATION_SERVICE)) {
        //    Toast.makeText(getApplicationContext(), "GPSがOFFになっています。", Toast.LENGTH_LONG).show();
        //} else {

        //}
        //Toast.makeText(getApplicationContext(), "現在地取得中・・・", Toast.LENGTH_SHORT).show();
        checkGPS();
        //Toast.makeText(getApplicationContext(), "現在地を取得しました。", Toast.LENGTH_SHORT).show();

        final TextView rangeText = (TextView) findViewById(R.id.rangeText);
        SeekBar seekBar = (SeekBar) findViewById(R.id.rangeSeekbar);
        final Button button = (Button) findViewById(R.id.search_button);
        final ListView listView = (ListView) findViewById(R.id.detail_list);

        // アダプタ生成
        mList = new ArrayList<>();
        final ConditionAdapter2 adapter = new ConditionAdapter2(this, mList, R.layout.condition_item,
                new String[]{"title"},
                new int[]{R.id.condition_text},
                R.id.condition_switch);

        /*
        // クリックイベント処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("itemClick", "position=" + String.valueOf(position));
            }
        });
        */

        // アダプターセット
        listView.setAdapter(adapter);

        // 表示するデータ設定
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", item[i]);
            mList.add(map);
        }

        rangeText.setText("検索範囲 : 周囲 300 m");

        // 検索ボタンが押されたとき
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.freeword);
                String freeword = editText.getText().toString();

                // レストラン一覧画面の起動
                Intent intent = new Intent(activity, RestlistActivity.class);

                Log.d("ConditionLunch:", String.valueOf(lunch_val));
                Log.d("ConditionBottom:", String.valueOf(bottom_val));
                Log.d("ConditionBuffet:", String.valueOf(buffet_val));
                Log.d("ConditionParking:", String.valueOf(parking_val));
                Log.d("ConditionSmoking:", String.valueOf(no_smoking_val));

                // 検索ワードを次の画面へ渡す
                intent.putExtra("FreeWord", freeword);
                intent.putExtra("range", range_number);
                intent.putExtra("lunch", lunch_val);
                intent.putExtra("bottom", bottom_val);
                intent.putExtra("buffet", buffet_val);
                intent.putExtra("parking", parking_val);
                intent.putExtra("smoking", no_smoking_val);

                // 位置情報を次の画面へ渡す
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                activity.startActivity(intent);
            }
        });

        // Seekbarを動かした時
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                range_number = seekBar.getProgress() + 1;
            }
        });
    }

    public void checkGPS() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            // プロバイダ名, 最短更新時間(ms), 更新移動距離(m)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
        }
    }

    // 位置が変更された時のイベントで呼び出される
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();                                                       // 緯度
        double lon = location.getLongitude();                                                      // 経度
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lon);
        Log.e("Location lat", String.valueOf(lat));
        Log.e("Location lon", String.valueOf(lon));
        //Toast.makeText(getApplicationContext(), "lat:" + latitude + " lon:" + longitude, Toast.LENGTH_SHORT).show();
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        //    return;
        //}
        //mLocationManager.removeUpdates(this);
    }

    // GPSを取得するのに利用しているプロバイダのステータス変更時のイベントで呼び出される
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("INFO", "Status changed...");
    }

    // プロバイダが利用可能な状態になったとき呼び出される
    @Override
    public void onProviderEnabled(String provider) {
        Log.i("INFO", "Provider enable...");
    }

    // プロバイダが利用不可な状態になったときに呼び出される
    @Override
    public void onProviderDisabled(String provider) {
        Log.i("INFO","Provider disable...");
    }

    // 絞り込み条件のためのアダプター
    public class ConditionAdapter2 extends SimpleAdapter {
        private int aSwitch;

        public ConditionAdapter2(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int mSwitch) {
            super(context, data, resource, from, to);
            aSwitch = mSwitch;
        }

        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            Switch sw = (Switch) view.findViewById(aSwitch);
            // Tagでpositionの設定
            sw.setTag(position);

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int i = (Integer) buttonView.getTag();

                    if (isChecked) {
                        conditionCheck(i, isChecked);
                        Log.d("SwitchTest:", "position" + i + "true");
                    } else {
                        conditionCheck(i, isChecked);
                        Log.d("SwitchTest:", "position" + i + "false");
                    }
                }
            });
            return view;
        }
    }

    public void conditionCheck(int i, boolean ischecked) {
        switch (i) {
            case 0:
                lunch = ischecked;
                if (lunch == true) {
                    lunch_val = 1;
                } else {
                    lunch_val = 0;
                }
                break;
            case 1:
                bottomless = ischecked;
                if (bottomless == true) {
                    bottom_val = 1;
                } else {
                    bottom_val = 0;
                }
                break;
            case 2:
                buffet = ischecked;
                if (buffet == true) {
                    buffet_val = 1;
                } else {
                    buffet_val = 0;
                }
                break;
            case 3:
                parking = ischecked;
                if (parking == true) {
                    parking_val = 1;
                } else {
                    parking_val = 0;
                }
                break;
            case 4:
                no_smoking = ischecked;
                if (no_smoking == true) {
                    no_smoking_val = 1;
                } else {
                    no_smoking_val = 0;
                }
                break;
        }
    }
}
