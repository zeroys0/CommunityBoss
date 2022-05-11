package net.leelink.communityboss.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.leelink.communityboss.R;


public class LoadDialog extends Dialog {

    private static LoadDialog dialog;
    private static ProgressBar p;


    /**
     * 开始加载动画
     *
     * @param context
     */
    public static void start(Context context) {
        stop();
        dialog = new LoadDialog(context, R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 开始加载动画
     * 并使用自己图片
     *
     * @param context
     */
    public static void start(Context context, int drawableres) {
        stop();
        dialog = new LoadDialog(context, R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        p.setIndeterminateDrawable(ContextCompat.getDrawable(context, drawableres));
    }

    /**
     * 开始加载动画
     * 并使用自己图片
     *
     * @param context
     */
    public static void start(Context context, Drawable d) {
        stop();
        dialog = new LoadDialog(context, R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        p.setIndeterminateDrawable(d);
    }
    /**
     * 开始加载动画
     * 并使用自己图片
     *
     * @param context
     */
    public static void start(Context context, int drawableres, int width, int height) {
        stop();
        dialog = new LoadDialog(context, R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ViewGroup.LayoutParams params=p.getLayoutParams();
        params.width=width;
        params.height=height;
        p.setLayoutParams(params);
        p.setIndeterminateDrawable(ContextCompat.getDrawable(context, drawableres));
    }
    /**
     * 开始加载动画
     * 并使用自己图片
     *
     * @param context
     */
    public static void start(Context context, Drawable d, int width, int height) {
        stop();
        dialog = new LoadDialog(context, R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        p.setIndeterminateDrawable(d);
    }

    /**
     * 结束加载动画
     */
    public static void stop() {
        if (null != dialog && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public LoadDialog(Context context) {
        super(context);
        setContentView(R.layout.view_dialog_load);
    }

    public LoadDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.view_dialog_load);
    }

    public LoadDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.view_dialog_load);
        p = (android.widget.ProgressBar) findViewById(R.id.progressBar1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_load);
        p = (android.widget.ProgressBar) findViewById(R.id.progressBar1);
    }

}