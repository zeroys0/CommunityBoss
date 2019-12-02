package net.leelink.communityboss.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void show(Context context, String s) {
        if (context == null) {
            return;
        }
        try {
            if (toast == null) {
                toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                oneTime = System.currentTimeMillis();
            } else {
                twoTime = System.currentTimeMillis();
                if (s.equals(oldMsg)) {
                    if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                        toast.show();
                    }
                } else {
                    oldMsg = s;
                    toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            oneTime = twoTime;
        } catch (Exception e) {
        }
    }

    public static void show(Context context, int resId) {
        if(context ==null){
            return;
        }
        show(context, context.getString(resId));
    }


    /**
     * 取消当前页面的所有toast
     * @param context
     */
    public static void cancelAllToast(Context context ) {
        if (context == null)
            return;

        if (toast != null) {
            toast.cancel();
        }else{
            return;
        }
    }
}
