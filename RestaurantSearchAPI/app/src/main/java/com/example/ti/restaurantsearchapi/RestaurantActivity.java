package com.example.ti.restaurantsearchapi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

/* レストラン詳細画面 */

public class RestaurantActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private String shop_name;                                                                    // 店舗名
    private String address;                                                                      // 住所
    private String tel;                                                                           // 電話番号
    private String opentime;                                                                     // 営業時間
    private String url;                                                                           // 店舗URL
    private String image_url;                                                                    // 店舗画像URL

    private TextView shop_name_text;
    private TextView address_text;
    private TextView tel_text;
    private TextView opentime_text;
    private TextView url_text;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        // 値を受け取る
        final Intent intent = getIntent();
        shop_name = intent.getStringExtra("shop_name");
        address = intent.getStringExtra("address");                                 // 住所
        tel = intent.getStringExtra("tel");                                                 // 電話番号
        opentime = intent.getStringExtra("opentime");                                      // 営業時間
        url = intent.getStringExtra("url");                                                 // URL
        image_url = intent.getStringExtra("image_url");                                    // 画像URL

        shop_name_text = (TextView) findViewById(R.id.shop_name);
        address_text = (TextView) findViewById(R.id.address);
        tel_text = (TextView) findViewById(R.id.tel);
        opentime_text = (TextView) findViewById(R.id.opentime);
        url_text = (TextView) findViewById(R.id.url);

        // リンク設定(電話、URL)
        //address_text.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        tel_text.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        url_text.setAutoLinkMask(Linkify.WEB_URLS);

        // リンク設定(住所)
        SparseArray<String> maplinks = new SparseArray<String>();
        // リンクにする文字列の設定
        maplinks.append(0, address);
        // リンク文字列がクリックされた時
        TextLinker.OnLinkClickListener listener = new TextLinker.OnLinkClickListener() {
            @Override
            public void onLinkClick(int textId) {
                try {
                    // 地図アプリ連携
                    Uri uri = Uri.parse("geo:0,0?q=" + URLEncoder.encode(address,"UTF-8"));
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "地図アプリがありません", Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), "検索できません", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 値をセット
        shop_name_text.setText(shop_name);
        //address_text.setText("住所 : " + address);
        address_text.setText(TextLinker.getLinkableText(address, maplinks, listener));
        address_text.setMovementMethod(LinkMovementMethod.getInstance());
        tel_text.setText(tel);
        opentime_text.setText(opentime);
        url_text.setText(url);
        //image_url_text.setText("画像 : " + image_url);

        imageView = (ImageView) findViewById(R.id.shop_image);

        // 画像URLが存在するとき
        if (!image_url.equals("")) {
            // 画像のダウンロード
            try {
                DownloadImageTask task = new DownloadImageTask(imageView, progressBar);
                task.execute(image_url);
            } catch (Exception e) {
                Log.d("Error:", "画像ダウンロードで例外発生");
            }

        // 画像URLが存在しないとき
        // NoImage画像表示
        } else {
            progressBar.setVisibility(View.GONE);
            Resources res = getResources();
            Bitmap bitmap1 = BitmapFactory.decodeResource(res, R.drawable.no_image);
            // 画像のサイズ変更する
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 300, 225, false);
            imageView.setImageBitmap(bitmap2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.line_cooperation) {
            // LINE連携
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("line://msg/text/" + shop_name + " " + address + " " + url));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "LINEがありません", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
