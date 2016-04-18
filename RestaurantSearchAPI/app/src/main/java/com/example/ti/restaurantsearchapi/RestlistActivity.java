package com.example.ti.restaurantsearchapi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* レストラン一覧画面 */

public class RestlistActivity extends AppCompatActivity {

    private TextView total_hit_count;
    private ListView  restlistview;
    private ProgressBar progress;

    // 店リスト
    ArrayList<Map<String, Object>> restlist = new ArrayList<Map<String, Object>>();
    //SimpleAdapter adapter;

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
                    map.put("name", rest.name);                                                                // 店舗名
                    map.put("access", rest.access.line + rest.access.station +
                            "\n" + rest.access.station_exit + "から" + rest.access.walk + "分");          // アクセス
                    map.put("address", rest.address);                                                         // 住所
                    map.put("tel", rest.tel);                                                                  // 電話番号
                    map.put("opentime", rest.opentime);                                                      // 営業時間
                    map.put("url", rest.url);                                                                  // URL
                    map.put("image_url", rest.image_url.shop_image1);                                      // 店舗画像URL

                    // リストに追加
                    restlist.add(map);
                }
            } else if (exception != null) {
                total_hit_count.setText("検索ヒット数 : " + 0);
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
        // 値を受け取る
        Intent intent = getIntent();
        String freeword = intent.getStringExtra("FreeWord");                                      // 検索ワード 範囲を受け取る
        int range_number = intent.getIntExtra("range", 1);                                        // 検索範囲
        int lunch = intent.getIntExtra("lunch", 0);                                               // ランチ営業
        int bottom = intent.getIntExtra("bottom", 0);                                             // 飲み放題
        int buffet = intent.getIntExtra("buffet", 0);                                             // 食べ放題
        int parking = intent.getIntExtra("parking", 0);                                           // 飲み放題
        int smoking = intent.getIntExtra("smoking", 0);                                           // 禁煙席

        Log.d("ConditionLunch:", String.valueOf(lunch));
        Log.d("ConditionBottom:" , String.valueOf(bottom));
        Log.d("ConditionBuffet:" , String.valueOf(buffet));
        Log.d("ConditionParking:" , String.valueOf(parking));
        Log.d("ConditionSmoking:" , String.valueOf(smoking));

        // 検索ワードの全角コンマを半角コンマへ変換
        //freeword.replaceAll("，", ",");
        //freeword.replaceAll("、", " ");
        //Toast.makeText(getApplication(), "FreeWord'" + freeword + "'", Toast.LENGTH_SHORT).show();

        // アダプター生成
        SimpleAdapter adapter = new SimpleAdapter(this, restlist, R.layout.restlist_item,
                new String[] {"name", "access"},
                new int[] {R.id.restName, R.id.restAccess});
        // 画像つきアダプター生成
        ImageListAdapter Imgadapter = new ImageListAdapter(this, restlist, R.layout.restlist_item,
                new String[] {"name", "access"},
                new int[] {R.id.restName, R.id.restAccess});
        // アダプターセット
        restlistview.setAdapter(Imgadapter);

        progress = (ProgressBar) findViewById(R.id.progress);
        // URLをたたく
        new ApiTask().execute("35.670082", "139.763267", String.valueOf(range_number), freeword,
                String.valueOf(lunch), String.valueOf(bottom), String.valueOf(buffet), String.valueOf(parking), String.valueOf(smoking));

        // リスト項目がクリックされた時
        restlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // リストからクリックされたデータ呼び出し
                Map<String, Object> data_row = restlist.get(position);

                String shop_name = data_row.get("name").toString();                                // 店舗名(name : Mapのキー)
                String address = data_row.get("address").toString();                              // 住所
                String tel = data_row.get("tel").toString();                                       // 電話番号
                String opentime = data_row.get("opentime").toString();                            // 営業時間
                String url = data_row.get("url").toString();                                       // URL
                String image_url = data_row.get("image_url").toString();                          // 店舗画像URL
                //Toast.makeText(getApplication(), "selected'" + shop_name + "'", Toast.LENGTH_SHORT).show();

                // 押された項目を次の画面に渡す
                Intent intent = new Intent(RestlistActivity.this, RestaurantActivity.class);

                intent.putExtra("shop_name", shop_name);                                          // 店舗名(shop_name  = キー設定名)
                intent.putExtra("address", address);                                              // 住所
                intent.putExtra("tel", tel);                                                       // 電話番号
                intent.putExtra("opentime", opentime);                                            // 営業時間
                intent.putExtra("url", url);                                                       // URL
                intent.putExtra("image_url", image_url);                                          // 店舗画像URL
                startActivity(intent);
            }
        });
    }

    // 画像つきリストアダプタ
    public class ImageListAdapter extends SimpleAdapter {

        private List<? extends  Map<String, Object>> list_data;
        private Context context;
        private LayoutInflater mInflater;

        public ImageListAdapter(Context context, List<? extends Map<String, Object>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.list_data = data;
            this.context = context;

            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.restlist_item, null);
            }

            // 行にあるデータ読み込み
            Map<String, Object> data_row = list_data.get(position);
            String rest_name = data_row.get("name").toString();                                    // 店舗名
            String access = data_row.get("access").toString();                                    // アクセス
            String img_url = data_row.get("image_url").toString();                               // 店舗画像

            TextView rest_name_text = (TextView) view.findViewById(R.id.restName);
            TextView access_text = (TextView) view.findViewById(R.id.restAccess);
            TextView img_url_text = (TextView) view.findViewById(R.id.rest_img_url);
            ImageView imageView = (ImageView) view.findViewById(R.id.rest_image);

            // 画像URLが存在するとき
            if (!img_url.equals("")) {
                // 画像のダウンロード
                try {
                    ListImageDownloadTask task = new ListImageDownloadTask(imageView);
                    task.execute(img_url);
                } catch (Exception e) {
                    Log.d("Error:", "画像ダウンロードで例外発生");
                }

                // 画像URLが存在しないとき
                // NoImage画像表示
            } else {
                Resources res = getResources();
                Bitmap bitmap1 = BitmapFactory.decodeResource(res, R.drawable.no_image);
                // 画像のサイズ変更する
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 80, 60, false);
                imageView.setImageBitmap(bitmap2);
            }

            rest_name_text.setText(rest_name);
            access_text.setText(access);
            img_url_text.setText(img_url);
            return view;
        }
    }
}
