package cn.aaaapp.translucentstatusbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.aaaapp.translucentstatus.StatusBarCompat;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {//5.0及以上
//            View decorView = getWindow().getDecorView();
//
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
//
//            //文字
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//           getWindow().setNavigationBarColor(Color.TRANSPARENT);
//        }
//
//        getWindow().getDecorView().setBackgroundResource(R.mipmap.maxiu);
//        StatusBarCompat.setTranslucentStatusbar(this);
        setContentView(R.layout.activity_main);
        StatusBarCompat.setNormalStatusbar(this);
    }
}
