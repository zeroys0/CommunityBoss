package net.leelink.communityboss.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.app.CommunityBossApplication;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static net.leelink.communityboss.app.CommunityBossApplication.preferences;



public class JPushReceiver extends BroadcastReceiver {

    private String mStrPname;
    private String mStrCname;
    private int type;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        System.out.println("[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            System.out.println("1[MyReceiver] 接收Registration Id : " + regId);
            MediaPlayer mediaPlayer;
            mediaPlayer = MediaPlayer.create(CommunityBossApplication.getInstance(), R.raw.alex);
            mediaPlayer.start();
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            System.out.println("2[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            checkLoginOut(intent, context);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            System.out.println("3[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            System.out.println("接收到的消息:" + bundle.getString(JPushInterface.EXTRA_ALERT));

            String json = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
            JSONObject jo = null;
            try {
                jo = new JSONObject(json);
                String mobile = jo.getString("mobile");
                int status = jo.getInt("status");
//                if (status == 1) {
//                    EventBus.getDefault().postSticky(new MsgEvent(1));
//                    EventBus.getDefault().postSticky(new SystemMsgEvent(1));
//                } else if (status == 2) {
//                    EventBus.getDefault().postSticky(new MsgEvent(1));
//                    EventBus.getDefault().postSticky(new ServiceMsgEvent(1));
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            String json = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jo = new JSONObject(json);
                String mobile = jo.getString("mobile");
                int status = jo.getInt("status");
                Log.e( "点开通知: ", jo.toString());
//                if (status == 1) {
//                    Intent it_msg = new Intent(context, SystemNewsActivity.class);
//                    it_msg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(it_msg);
//                } else if (status == 2) {
//                    Intent it_msg = new Intent(context, WordsActivity.class);
//                    it_msg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(it_msg);
//                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            System.out.println("5[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                    + bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            System.out.println("[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            System.out.println("[MyReceiver] Unhandled intent - "
                    + intent.getAction());
        }
    }
//
//    SharedPreferences preferences = PartyBuildingApplication.getInstance().getSharedPreferences();

    private void checkLoginOut(Intent intent, Context context) {
        /*
         * 收到通知,处于登录状态 UID相同 则 踢下线 到GUIDE界面 并且提示 有人在其他设备登录
		 */
        Bundle bundle = intent.getExtras();
        String json = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject jo = new JSONObject(json);
//			JSONObject object = jo.getJSONObject("extras");
            String mobile = jo.getString("mobile");
            String randcode = jo.getString("randcode");
            int isread = jo.getInt("isread");
            int type = jo.getInt("type");

            String time = preferences.getString("time", "");
            String phone = preferences.getString("mobile", "");

            switch (type) {
                case 1:
                    Toast.makeText(context, bundle.getString(JPushInterface.EXTRA_MESSAGE), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            //        PartyBuildingApplication.getInstance().cancleLogin(context);
                    break;
                case 2:
                    if (phone.equals(mobile)) {
                        if (!randcode.equals("")) {
                            if (!time.equals(randcode)) {
                                Toast.makeText(context, bundle.getString(JPushInterface.EXTRA_MESSAGE), Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
          //                      PartyBuildingApplication.getInstance().cancleLogin(context);
                            }
                        }
                    }
                    break;
            }

//            Logger.i("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",jo.toString());

//            if (num > 0) {
//                EventBus.getDefault().postSticky(new MsgEvent(1));
//            } else {
//                EventBus.getDefault().postSticky(new MsgEvent(0));
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}

