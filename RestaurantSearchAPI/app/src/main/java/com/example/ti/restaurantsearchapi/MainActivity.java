package com.example.ti.restaurantsearchapi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
    private InputMethodManager mInputMethodManager;
    private TextView rangeText;                                                                   // 範囲を示すテキスト
    private SeekBar seekBar;                                                                      // 範囲用のシークバー
    private Button button;                                                                        // 検索ボタン
    private EditText editText;                                                                    // フリーワード用テキストボックス
    private String latitude;                                                                      // 緯度
    private String longitude;                                                                     // 経度
    private List<conditionListItem> mConditionItemList;                                         // 絞り込み条件項目リスト
    private ListView listView;                                                                    // 項目リストのためのリストビュー

    private String freeword;                                                                      // 検索ワード
    private int range_number;                                                                   // 検索範囲
    private boolean location;                                                                   // 現在地ON/OFF
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
        // キーボード表示を制御するためのオブジェクト
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        rangeText = (TextView) findViewById(R.id.rangeText);
        seekBar = (SeekBar) findViewById(R.id.rangeSeekbar);
        button = (Button) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.detail_list);
        editText = (EditText) findViewById(R.id.freeword);

        rangeText.setText("検索範囲 : 周囲 500 m");

        // 絞り込み条件の項目リスト
        mConditionItemList = conditionListItem.contentItem();
        // アダプターの設定(項目リスト、レイアウトなど)
        ConditionAdapter adapter = new ConditionAdapter(getApplicationContext(), mConditionItemList, R.id.condition_switch);
        // アダプターセット
        listView.setAdapter(adapter);

        // 検索ボタンが押されたとき
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストから文字を受け取る
                freeword = editText.getText().toString();

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

        // エディットテキストでキーボードのエンターが押された時
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // キーボードを閉じる
                    mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), mInputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
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

    public void onGPS() {
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
            Toast.makeText(getApplicationContext(), "現在地取得中...", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //    return;
        }
        Toast.makeText(getApplicationContext(), "現在地を取得しました。", Toast.LENGTH_SHORT).show();
        mLocationManager.removeUpdates(this);
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
        Log.i("INFO", "Provider disable...");
    }

    @Override
    public void onPause() {
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
            mLocationManager.removeUpdates(this);
        }
        super.onPause();
    }

    // 絞り込み条件のためのアダプター
    public class ConditionAdapter extends ArrayAdapter<conditionListItem> {
        private int aSwitch;
        private LayoutInflater mLayoutInflater;

        public ConditionAdapter(Context context, List<conditionListItem> data, int mSwitch) {
            super(context, 0, data);
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aSwitch = mSwitch;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            convertView = mLayoutInflater.inflate(R.layout.condition_item, parent, false);
            holder = new ViewHolder();
            holder.condition_name = (TextView) convertView.findViewById(R.id.condition_text);
            holder.condition_imageId = (ImageView) convertView.findViewById(R.id.condtion_img);
            convertView.setTag(position);

            String condition_name = getItem(position).getmTitle();
            int condition_imageId = getItem(position).getmImageId();

            holder.condition_name.setText(condition_name);
            holder.condition_imageId.setImageResource(condition_imageId);

            final Switch sw = (Switch) convertView.findViewById(aSwitch);
            // Tagでpositionの設定
            sw.setTag(position);

            // 一行ずつ読み込まれるため、ボタンの状態を保持しておく
            switch (position) {
                case 0:
                    if (location == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
                case 1:
                    if (lunch == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
                case 2:
                    if (bottomless == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
                case 3:
                    if (buffet == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
                case 4:
                    if (parking == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
                case 5:
                    if (no_smoking == true) {
                        sw.setChecked(true);
                    } else {
                        sw.setChecked(false);
                    }
                    break;
            }

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int i = (Integer) buttonView.getTag();

                    conditionCheck(i, isChecked);
                    switch (i) {
                        case 0:
                            location = isChecked;
                            if (location == true) {
                                // GPS設定の確認
                                boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                if (!gpsEnabled) {
                                    LocationPermissionCheck();
                                    sw.setChecked(false);
                                    location = false;
                                } else {
                                    onGPS();
                                }
                            } else {
                                latitude = null;
                                longitude = null;
                            }
                        }
                    Log.d("SwitchTest:", "position" + i + "true");
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView condition_name;
            ImageView condition_imageId;
        }
    }

    public void conditionCheck(int i, boolean ischecked) {
        switch (i) {
            case 1:
                lunch = ischecked;
                if (lunch == true) {
                    lunch_val = 1;
                } else {
                    lunch_val = 0;
                }
                break;
            case 2:
                bottomless = ischecked;
                if (bottomless == true) {
                    bottom_val = 1;
                } else {
                    bottom_val = 0;
                }
                break;
            case 3:
                buffet = ischecked;
                if (buffet == true) {
                    buffet_val = 1;
                } else {
                    buffet_val = 0;
                }
                break;
            case 4:
                parking = ischecked;
                if (parking == true) {
                    parking_val = 1;
                } else {
                    parking_val = 0;
                }
                break;
            case 5:
                no_smoking = ischecked;
                if (no_smoking == true) {
                    no_smoking_val = 1;
                } else {
                    no_smoking_val = 0;
                }
                break;
        }
    }

    public void LocationPermissionCheck() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("位置情報");
        alertDlg.setMessage("位置情報の取得を許可しますか？");
        alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 位置情報設定画面へ遷移
                Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingIntent);
            }
        });
        alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDlg.create().show();
    }
}
