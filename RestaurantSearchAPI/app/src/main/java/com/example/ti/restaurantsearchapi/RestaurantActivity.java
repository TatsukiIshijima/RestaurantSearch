package com.example.ti.restaurantsearchapi;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // 値を受け取る
        Intent intent = getIntent();
        String shop_name = intent.getStringExtra("shop_name");
        String address = intent.getStringExtra("address");
        String tel = intent.getStringExtra("tel");
        String opentime = intent.getStringExtra("opentime");
        String url = intent.getStringExtra("url");

        TextView shop_name_text = (TextView) findViewById(R.id.shop_name);
        TextView address_text = (TextView) findViewById(R.id.address);
        TextView tel_text = (TextView) findViewById(R.id.tel);
        TextView opentime_text = (TextView) findViewById(R.id.opentime);
        TextView url_text = (TextView) findViewById(R.id.url);

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

        ImageView imageView = (ImageView) findViewById(R.id.shop_image);
        Resources res = getResources();
        Bitmap bitmap1 = BitmapFactory.decodeResource(res, R.drawable.no_image);
        // 画像を縮小する
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 300, 225, false);
        imageView.setImageBitmap(bitmap2);
    }
}
