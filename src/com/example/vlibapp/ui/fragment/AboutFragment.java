package com.example.vlibapp.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.vlibapp.R;

public class AboutFragment extends Fragment {
	
	public static final String ABOUT_URL = "http://www.vtecsys.com/";
	
	private WebView webView;
	private ProgressBar progressView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.about_fragment, null);
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
		webView.loadUrl(ABOUT_URL);
	}

}
