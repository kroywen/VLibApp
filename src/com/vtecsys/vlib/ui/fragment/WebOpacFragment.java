package com.vtecsys.vlib.ui.fragment;

import android.annotation.SuppressLint;
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
import com.vtecsys.vlib.storage.Settings;

public class WebOpacFragment extends BaseFragment {
	
	private WebView webView;
	private ProgressBar progressView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_opac_fragment, null);
		progressView = (ProgressBar) rootView.findViewById(R.id.progress);
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
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
		if (!TextUtils.isEmpty(settings.getString(Settings.WEB_OPAC_URL))) {
			String url = settings.getString(Settings.WEB_OPAC_URL);
			url = url.startsWith("http") ? url : "http://" + url;
			webView.loadUrl(url);
		}
	}

}
