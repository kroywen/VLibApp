package com.vtecsys.vlib.ui.screen;

import zxing.library.DecodeCallback;
import zxing.library.ZXingFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.zxing.Result;
import com.vtecsys.vlib.R;

public class ISBNScannerScreen extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.isbn_scanner_screen);
		
		ZXingFragment xf = (ZXingFragment) 
			getSupportFragmentManager().findFragmentById(R.id.scanner);
		xf.setDecodeCallback(new DecodeCallback() {
			@Override
			public void handleBarcode(Result result, Bitmap barcode, float scaleFactor) {
				String isbn = result.getText();
				Intent data = new Intent();
				data.putExtra("isbn", isbn);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

}
