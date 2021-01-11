package net.leelink.communityboss.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.MyInfoBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


public abstract class BaseFragment extends Fragment {
	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			handleCallBack(msg);
		}

	};

	public abstract void handleCallBack(Message msg);
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);

	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}

	public void quicklogin(){
		OkGo.<String>get(Urls.getInstance().STOREHOME)
				.tag(this)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(Response<String> response) {
						try {
							String body = response.body();
							JSONObject json = new JSONObject(body);
							Log.d("获取个人信息",json.toString());
							if (json.getInt("status") == 200) {

								json = json.getJSONObject("data");
								Gson gson = new Gson();
							} else if(json.getInt("status") == 505) {
								final SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
								if (!sp.getString("secretKey", "").equals("")) {
									OkGo.<String>post(Urls.getInstance().QUICKLOGIN)
											.params("telephone", sp.getString("telephone", ""))
											.params("secretKey", sp.getString("secretKey", ""))
											.params("deviceToken", JPushInterface.getRegistrationID(getContext()))
											.tag(this)
											.execute(new StringCallback() {
												@Override
												public void onSuccess(Response<String> response) {
													try {
														String body = response.body();
														JSONObject json = new JSONObject(body);
														Log.d("快速登录", json.toString());
														if (json.getInt("status") == 200) {
															JSONObject jsonObject = json.getJSONObject("data");
															Gson gson = new Gson();
															Acache.get(getContext()).put("storeInfo",jsonObject);
															StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
															CommunityBossApplication.storeInfo = storeInfo;

														} else {
															Toast.makeText(getContext(), "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
															Intent intent4 = new Intent(getContext(), LoginActivity.class);
															SharedPreferences sp = getActivity().getSharedPreferences("sp",0);
															SharedPreferences.Editor editor = sp.edit();
															editor.remove("secretKey");
															editor.remove("telephone");
															editor.apply();
															intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
															startActivity(intent4);
															getActivity().finish();

														}
													} catch (JSONException e) {
														e.printStackTrace();
													}
												}
											});
								}
							}else {
								Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
//	public void notch(View view) {
//		if (hasNotchInScreen(getContext()) ) {
//			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
//			params.setMargins(0, NotchUtils.getNotchSize(getContext())[1]+5, 0, 30);//左上右下
//			view.setLayoutParams(params);
//		}
//	}

}
