package com.ull.etsii.etmap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        
        
        Button boton = (Button) findViewById(R.id.Button01);
        boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
		        progressDialog.setMessage("Buscando. Espere...");
		        progressDialog.show();
				
		        new Thread() { 
		            public void run() { 
		            	try{ 
		            		EditText edittext = (EditText) findViewById(R.id.EditText01);
		    				
		    				Intent intent = new Intent();
		    				Bundle bundle = new Bundle();
		    				bundle.putString("cadena", edittext.getText().toString());
		    				
		    				Geocoder geocod = new Geocoder(getBaseContext(), Locale.getDefault());
		    				try {
		    					List<Address> addresses = geocod.getFromLocationName(edittext.getText().toString(), 1);

		    					if (addresses.isEmpty()) {
		    						System.out.println("no hay resultados");
		    						bundle.putInt("estado", 0);
		    					} else {
		    						bundle.putInt("estado", 1);
		    						bundle.putString("lugar", addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
		    						bundle.putDouble("latitud", addresses.get(0).getLatitude());
		    						bundle.putDouble("longitud", addresses.get(0).getLongitude());
		    					}	
		    				} catch (IOException e) {
		    					bundle.putInt("estado", 0);
		    				}
		    				intent.putExtras(bundle);
		    				setResult(RESULT_OK, intent);
		            		sleep(500); 
		            	} catch (Exception e) {  } 
		            	progressDialog.dismiss();
		            	finish();
		            } 
		        }.start();
			}
		});
    }
}