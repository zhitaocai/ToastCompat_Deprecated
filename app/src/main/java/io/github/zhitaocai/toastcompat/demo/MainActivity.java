package io.github.zhitaocai.toastcompat.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.zhitaocai.toastcompat.IToast;
import io.github.zhitaocai.toastcompat.ToastCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_show_one_toast).setOnClickListener(this);
		findViewById(R.id.btn_show_multi_toast).setOnClickListener(this);
		findViewById(R.id.btn_show_toast_test).setOnClickListener(this);
		findViewById(R.id.btn_cancel_toast_test).setOnClickListener(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "BugReports via czt.saisam@gmail.com", Snackbar.LENGTH_LONG).setAction("Action", null)
						.show();
			}
		});
	}

	private IToast mToast;

	@Override
	public void onClick(View v) {
		if (v instanceof Button) {
			switch (v.getId()) {

			case R.id.btn_show_one_toast:
				ToastCompat.makeText(this, "show One Toast", Toast.LENGTH_SHORT).show();
				break;

			case R.id.btn_show_multi_toast:
				ToastCompat.makeText(this, "show Multi Toast : one", Toast.LENGTH_SHORT).show();
				ToastCompat.makeText(this, "show Multi Toast : two", Toast.LENGTH_SHORT).show();
				ToastCompat.makeText(this, "show Multi Toast : three", Toast.LENGTH_SHORT).show();
				//				Toast.makeText(this, "show Multi Toast : one", Toast.LENGTH_SHORT).show();
				//				Toast.makeText(this, "show Multi Toast : two", Toast.LENGTH_SHORT).show();
				//				Toast.makeText(this, "show Multi Toast : three", Toast.LENGTH_SHORT).show();
				break;

			case R.id.btn_show_toast_test:
				mToast = new ToastCompat(this).setText("test").setDuration(Toast.LENGTH_SHORT);
				mToast.show();
				break;

			case R.id.btn_cancel_toast_test:
				if (mToast != null) {
					mToast.cancel();
				}
				break;

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
