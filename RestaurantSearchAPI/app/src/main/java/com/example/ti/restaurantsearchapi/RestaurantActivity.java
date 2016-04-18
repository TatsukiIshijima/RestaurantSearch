package com.example.ti.restaurantsearchapi;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/* レストラン詳細画面 */

public class RestaurantActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        // 値を受け取る
        Intent intent = getIntent();
        String shop_name = intent.getStringExtra("shop_name");                                    // 店舗名
        String address = intent.getStringExtra("address");                                        // 住所
        String tel = intent.getStringExtra("tel");                                                 // 電話番号
        String opentime = intent.getStringExtra("opentime");                                      // 営業時間
        String url = intent.getStringExtra("url");                                                 // URL
        String image_url = intent.getStringExtra("image_url");                                    // 画像URL

        TextView shop_name_text = (TextView) findViewById(R.id.shop_name);
        TextView address_text = (TextView) findViewById(R.id.address);
        TextView tel_text = (TextView) findViewById(R.id.tel);
        TextView opentime_text = (TextView) findViewById(R.id.opentime);
        TextView url_text = (TextView) findViewById(R.id.url);
        // 画像URLの存在を確認するため
        //TextView image_url_text = (TextView) findViewById(R.id.image_url);

        // リンク設定(住所、電話番号)
        address_text.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        tel_text.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        url_text.setAutoLinkMask(Linkify.WEB_URLS);

        // 値をセット
        shop_name_text.setText(shop_name);
        address_text.setText("住所 : " + address);
        tel_text.setText("TEL : " + tel);
        opentime_text.setText("営業時間 : " + opentime);
        url_text.setText("URL : " + url);
        //image_url_text.setText("画像 : " + image_url);

        ImageView imageView = (ImageView) findViewById(R.id.shop_image);

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
}
