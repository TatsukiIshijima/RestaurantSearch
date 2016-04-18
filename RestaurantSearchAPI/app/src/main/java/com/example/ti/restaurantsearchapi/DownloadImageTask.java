package com.example.ti.restaurantsearchapi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by TI on 2016/04/18.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    // コンストラクタ
    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            String str_url = params[0];
            URL image_url = new URL(str_url);
            InputStream imageInputStream;
            imageInputStream = image_url.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream);
            Log.d("DownLoadImage:", "画像読み込み完了");

            return bitmap;
        } catch (Exception e) {
            Log.d("DownLoadImage:", "画像読み込みで例外発生:" + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            // 画像サイズ変更
            Bitmap bitmap2 = Bitmap.createScaledBitmap(result, 300, 225, false);
            imageView.setImageBitmap(bitmap2);
            Log.d("DownLoadImage:", "画像セット");
        }
    }
}
