package com.ull.etsii.etmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.start);
        
        Button boton = (Button) findViewById(R.id.Button01);
        boton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StartActivity.this, MapsActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }
}