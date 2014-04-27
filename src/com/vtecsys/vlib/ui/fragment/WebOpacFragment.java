package com.vtecsys.vlib.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.ui.screen.BaseScreen;

public class WebOpacFragment extends Fragment {
	
	private WebView webView;
	private ProgressBar progressView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_opac_fragment, null);
		progressView = (ProgressBar) rootView.findViewById(R.id.progress);
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				view.setVisibility(View.INVISIBLE);
				progressView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.setVisibility(View.VISIBLE);
				progressView.setVisibility(View.INVISIBLE);
			}
		});
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!TextUtils.isEmpty(BaseScreen.webOpacUrl)) {
			String url = BaseScreen.webOpacUrl;
			url = url.startsWith("http") ? url : "http://" + url;
			webView.loadUrl(url);
		}
	}

}
