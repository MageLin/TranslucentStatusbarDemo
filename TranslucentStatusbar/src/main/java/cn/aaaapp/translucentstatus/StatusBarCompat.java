package cn.aaaapp.translucentstatus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by LinLin on 2017/11/4.半透明状态栏
 */

public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#ffffffff");

    public static void setNormalStatusbar(Activity activity)
    {
        setNormalStatusbar(activity, INVALID_VAL);
    }


    public static void setNormalStatusbar(Activity activity, int statusColor)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            decorView.setSystemUiVisibility(option);
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            } else
            {
                activity.getWindow().setStatusBarColor(COLOR_DEFAULT);
            }
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (null != contentView.getChildAt(0))
                contentView.getChildAt(0).setFitsSystemWindows(true);
            else
                contentView.setFitsSystemWindows(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (null != contentView.getChildAt(0))
                contentView.getChildAt(0).setFitsSystemWindows(true);
            else
                contentView.setFitsSystemWindows(true);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);

        }
    }


    public static void setTranslucentStatusbar(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {//5.0及以上
            View decorView = activity.getWindow().getDecorView();

            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            //文字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
            contentView.addView(statusBarView, lp);
        }
    }




    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static void setBlackFont(Activity activity)
    {
        if (setMiuiStatusBarDarkMode(activity,true))
        {

        }else if (setMeizuStatusBarDarkIcon(activity,true))
        {

        }else {
            setStatusBarLightMode(activity,true);
        }
    }

    /**
     * 修改小米 MIUI
     */
    private static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode)
    {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try
        {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改Flyme
     */
    private static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean darkmode)
    {
        boolean result = false;
        if (activity != null)
        {
            try
            {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkmode)
                {
                    value |= bit;
                } else
                {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * if version is lager than M
     */
    private static void setStatusBarLightMode(Activity activity, boolean isFontColorDark)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (isFontColorDark)
            {
                //沉浸式
                int flag = activity.getWindow().getDecorView().getSystemUiVisibility();
                activity.getWindow().getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else
            {
                //非沉浸式
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }
}
