package com.example.ti.restaurantsearchapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestlistActivity extends AppCompatActivity {

    private TextView total_hit_count;
    private ListView  restlistview;
    private ProgressBar progress;

    // 店リスト
    ArrayList<Map<String, Object>> restlist = new ArrayList<Map<String, Object>>();
    //SimpleAdapter adapter;
    ArrayList<String> namelist = new ArrayList<String>();

    public class ApiTask extends GetRestaurantTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(RestSearch data) {
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);

            if (data != null) {
                total_hit_count.setText("検索ヒット数 : " + data.total_hit_count);

                for (RestSearch.Rest rest : data.restList) {
                    // Mapに各項目追加
                    Map map = new HashMap();
                    map.put("name", rest.name);
                    map.put("access", rest.access.line + rest.access.station +
                            "\n" + rest.access.station_exit + "から" + rest.access.walk + "分");
                    map.put("address", rest.address);
                    map.put("tel", rest.tel);
                    map.put("opentime", rest.opentime);
                    map.put("url", rest.url);

                    // リストに追加
                    restlist.add(map);
                    //namelist.add(rest.name);
                    //total_hit_count.append("\n");
                    //total_hit_count.append(rest.name);
                }
            } else if (exception != null) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "検索結果なし", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restlist);

        total_hit_count = (TextView) findViewById(R.id.total_count);
        restlistview = (ListView) this.findViewById(R.id.listView);
        // 検索ワード 範囲を受け取る
        Intent intent = getIntent();
        String freeword = intent.getStringExtra("FreeWord");
        int range_number = intent.getIntExtra("range", 1);
        // 4/17 ここが全部Nullのため結果が出てこない
        int lunch = intent.getIntExtra("lunch", 0);
        int bottom = intent.getIntExtra("bottom", 0);
        int buffet = intent.getIntExtra("buffet", 0);
        int parking = intent.getIntExtra("parking", 0);
        int smoking = intent.getIntExtra("smoking", 0);

        Log.d("ConditionLunch:", String.valueOf(lunch));
        Log.d("ConditionBottom:" , String.valueOf(bottom));
        Log.d("ConditionBuffet:" , String.valueOf(buffet));
        Log.d("ConditionParking:" , String.valueOf(parking));
        Log.d("ConditionSmoking:" , String.valueOf(smoking));

        // 検索ワードの全角コンマを半角コンマへ変換
        //freeword.replaceAll("，", ",");
        //freeword.replaceAll("、", " ");
        //Toast.makeText(getApplication(), "FreeWord'" + freeword + "'", Toast.LENGTH_SHORT).show();

        // ListViewの設定
        // サンプル
        //String[] sample = {"Windows", "Mac", "Unix", "Linux", "Android", "iOS"};
        // 第2引数はレイアウト 第3引数は項目
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namelist);
        // アダプター生成
        SimpleAdapter adapter = new SimpleAdapter(this, restlist, R.layout.restlist_item,
                new String[] {"name", "access"},
                new int[] {R.id.restName, R.id.restAccess});
        // アダプターセット
        restlistview.setAdapter(adapter);

        progress = (ProgressBar) findViewById(R.id.progress);
        new ApiTask().execute("35.670082", "139.763267", String.valueOf(range_number), freeword,
                String.valueOf(lunch), String.valueOf(bottom), String.valueOf(buffet), String.valueOf(parking), String.valueOf(smoking));

        // リスト項目がクリックされた時
        restlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // クリックされたデータ呼び出し
                Map<String, Object> data_row = restlist.get(position);
                // 店舗名(name : Mapのキー)
                String shop_name = data_row.get("name").toString();
                String address = data_row.get("address").toString();
                String tel = data_row.get("tel").toString();
                String opentime = data_row.get("opentime").toString();
                String url = data_row.get("url").toString();
                //String str = (String) ((TextView) view).getText();
                Toast.makeText(getApplication(), "selected'" + shop_name + "'", Toast.LENGTH_SHORT).show();

                // 押された項目を次の画面に渡す
                Intent intent = new Intent(RestlistActivity.this, RestaurantActivity.class);
                // 店舗名(shop_name : キー設定名)
                intent.putExtra("shop_name", shop_name);
                intent.putExtra("address", address);
                intent.putExtra("tel", tel);
                intent.putExtra("opentime", opentime);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}
