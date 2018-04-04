package com.zhr.cat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.zhr.cat.R;

import static com.zhr.cat.tools.Utils.transBar;

public class SettingActivity extends Activity implements OnClickListener {

	private ImageButton ib_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		transBar(this);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;

		default:
			break;
		}
	}
}
