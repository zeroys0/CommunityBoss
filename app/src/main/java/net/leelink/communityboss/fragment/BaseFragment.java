package net.leelink.communityboss.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
