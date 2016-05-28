package com.lifayu.teatcamera2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button button;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Intent intent = new Intent("MediaStore.ACTION_IMAGE_CAPTURE");
                    Intent intent = new Intent();
                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    String imagePath = Environment.getExternalStorageDirectory() + "/采集照片/";
                    File file = new File(imagePath);
                    if(!file.exists())
                    {
                        file.mkdirs();
                    }
                    imageUri = Uri.fromFile(new File(imagePath + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                    Log.i("TestFile", "正在拍照...");

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TestFile", "拍完了，回传结果");

        if(resultCode == Activity.RESULT_OK)
        {
            ((ImageView)findViewById(R.id.imageView)).setImageURI(imageUri);
            Toast.makeText(this, "照片已保存:" + imageUri.getPath(), Toast.LENGTH_LONG).show();
        }


        /*
        if(resultCode == Activity.RESULT_OK)
        {
            String sdStatus = Environment.getExternalStorageState();
            Log.i("sd卡状态", sdStatus);
            if(!sdStatus.equals(Environment.MEDIA_MOUNTED))
            {
                Log.i("TestFile", "SD card is not avaiable/writeable right now");
                return;
            }
            String name = new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Log.i("TestFile", "文件名为:" + name);
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap)bundle.get("data");
            Log.i("TestFile", "开始输出文件");
            FileOutputStream b = null;
            //File file = new File("/sdcard/myImage/" + name);
            File folder = new File(Environment.getExternalStorageDirectory() + "/采集照片/");
            folder.mkdirs();
            File file = new File(Environment.getExternalStorageDirectory() + "/采集照片/" + name);
            //file.mkdirs();
            String fileName = Environment.getExternalStorageDirectory() + "/采集照片/" + name;
//            String fileName = "/sdcard/myImage/" + name;
            try
            {
                Log.i("TestFile", "输出文件中...");
                b = new FileOutputStream(fileName);
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
            }
            catch (Exception e)
            {
                Log.i("TestFile", "遇到错误了:" + e);
                e.printStackTrace();
            }
            finally
            {
                Log.i("TestFile", "完成了！");
                try
                {
                    b.flush();
                    b.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            */
            //((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
        }


    public void testpic(View v)
    {
        try {
            ImageView iv = (ImageView)findViewById(R.id.imageView);
            if(getDrawable().size() != 0)
            {
                iv.setImageDrawable(getDrawable().get(0));
            }

            MainActivity.this.setContentView(iv);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataBase", e.toString());
        }
    }

    private ArrayList<Drawable> getDrawable()
    {
        PictureDatabase pd = new PictureDatabase(this);
        SQLiteDatabase sd = pd.getWritableDatabase();

        ArrayList<Drawable> drawables = new ArrayList<Drawable>();
        //查询数据库
        Cursor c = sd.query("picture", null, null, null, null, null, null);
        //遍历数据
        if(c != null && c.getCount() != 0)
        {
            while(c.moveToNext())
            {
                //获取数据
                byte[] b = c.getBlob(c.getColumnIndexOrThrow(PictureDatabase.PictureColumns.PICTURE));
                //将获取的数据转换成drawable
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;
                drawables.add(drawable);
            }
        }
        return drawables;
    }


}
