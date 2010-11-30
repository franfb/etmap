package com.ull.etsii.etmap;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MapsActivity extends MapActivity {
	private MapController mapcontroller;
	private MapView mapView;
	// OLD - private GeoPoint esquina1GeoPoint = new GeoPoint(28617076,-16088791);
	// OLD - private GeoPoint esquina2GeoPoint = new GeoPoint(27958017,-16962891);
	private static final int zoomDefLevel = 10;
	private static final GeoPoint esquina1GeoPoint = new GeoPoint(28721065,-16088791);	
	private static final GeoPoint esquina2GeoPoint = new GeoPoint(27990764,-16962891);
    private static final GeoPoint initGeoPoint = new GeoPoint(esquina2GeoPoint.getLatitudeE6() + ((esquina1GeoPoint.getLatitudeE6() - esquina2GeoPoint.getLatitudeE6()) / 2),
    											 esquina2GeoPoint.getLongitudeE6() + ((esquina1GeoPoint.getLongitudeE6() - esquina2GeoPoint.getLongitudeE6()) / 2));
	private int diasRef;
	private int horaTemp;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private OverlayClass itemizedOverlay;
	private int moviendose;
	private static final int MAXMOVES = 2;
	private int lastLon;
	private int lastLat;
	
	static final int START_DIALOG_ID = 0;
    
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
    		if (data.getExtras().getInt("estado") == 1) {
    			GeoPoint p = new GeoPoint(
    					(int) (data.getExtras().getDouble("latitud") * 1E6), 
                        (int) (data.getExtras().getDouble("longitud") * 1E6));
    			OverlayItem overlayitem = new OverlayItem(p, "", "");
	            itemizedOverlay = new OverlayClass(drawable);  
	            itemizedOverlay.addOverlay(overlayitem);
	            mapOverlays.clear();
	            mapOverlays.add(itemizedOverlay);
	            
	            lastLon = p.getLongitudeE6();
	            lastLat = p.getLatitudeE6();
	            centrarMapaEnTenerifeSlow();
	            mapcontroller.animateTo(p);
	            
	            System.out.println(data.getExtras().getString("lugar").substring(0,5));
	            
	            if (data.getExtras().getString("lugar").substring(0,5).compareTo("null,") == 0) {
	            	Toast.makeText(getBaseContext(), "No se encontraron coincidencias para \"" + data.getExtras().getString("cadena") + "\".", Toast.LENGTH_SHORT).show();
	            } else { 
	            	Toast.makeText(getBaseContext(), "Mostrando localización de \"" + data.getExtras().getString("cadena") + "\"", Toast.LENGTH_SHORT).show();
	            	TextView textview = (TextView) findViewById(R.id.TextView01);
	            	textview.setText(data.getExtras().getString("lugar"));
	            	Button botonET = (Button) findViewById(R.id.Button01);
	            	botonET.setEnabled(true);
	            }
    		}
    		if (data.getExtras().getInt("estado") == 0) {
    			Toast.makeText(getBaseContext(), "No se encontraron coincidencias para \"" + data.getExtras().getString("cadena") + "\".", Toast.LENGTH_SHORT).show();
    		}
    	}
    }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
               
        centrarMapaEnTenerifeFast();
        // OLD - zoomDefLevel = mapView.getZoomLevel();
        mapView.setTraffic(true);
        mapView.setSatellite(false);
    	mapView.setKeepScreenOn(true);
        diasRef = 7;
        horaTemp = 14;
        
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.marker);
        
        mapView.setOnTouchListener(new OnTouchListener() {        	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	moviendose = 0;
		        }
				
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					moviendose++;
				}
				
				if ((moviendose < MAXMOVES) && (event.getAction() == MotionEvent.ACTION_UP)) {                
		            GeoPoint p = mapView.getProjection().fromPixels(
		                (int) event.getX(),
		                (int) event.getY());
		            OverlayItem overlayitem = new OverlayItem(p, "", "");
		            itemizedOverlay = new OverlayClass(drawable);  
		            itemizedOverlay.addOverlay(overlayitem);
		            mapOverlays.clear();
		            mapOverlays.add(itemizedOverlay);
		            
		            lastLon = p.getLongitudeE6();
		            lastLat = p.getLatitudeE6();
		            
		            final ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
			        progressDialog.setMessage("Estudiando localización. Espere...");
			        progressDialog.show();
			        			        
			        new Thread() { 
			            public void run() {
			            	final String result = geocodificar(lastLat, lastLon);

			            	progressDialog.dismiss();

					        MapsActivity.this.runOnUiThread(
					        	new Runnable() {
									public void run() {
										// Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
						            	TextView textview = (TextView) findViewById(R.id.TextView01);
						            	textview.setText(result);
						            	Button botonET = (Button) findViewById(R.id.Button01);
						            	if (!botonET.isEnabled()) {
						            		botonET.setEnabled(true);
						            	}
									}
								}
					        );	
			            }
			        }.start();
				}
		        return false;
			}
		});
        
        Button botonET = (Button) findViewById(R.id.Button01);
        botonET.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		final ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
		        progressDialog.setMessage("Comunicando con el servidor. Espere...");
		        progressDialog.show();
		        
		        final AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
		        dialog.setTitle("Información de esta localización");
		        
		        new Thread() { 
		            public void run() { 
		            	try {
		            		String evapStr = "";
		        
		            		// *************************************
		            		// double srv_lat = 28.500527177728866;
		            		// double srv_lon = -16.328773498535156;
		            		// String srv_date = "08-01-2009";
		            		// int srv_days = 7;
		            		// int srv_hour = 14;
		        
		            		// String resJSON = llamadaWS(srv_lat, srv_lon, srv_date, srv_days, srv_hour);
		            		
		            		Calendar cal = new GregorianCalendar();
		            		int dia = cal.get(Calendar.DAY_OF_MONTH);
		            		int mes = cal.get(Calendar.MONTH) + 1;
		            		int ano = cal.get(Calendar.YEAR);
		            		
		            		String Today = dia + "-" + mes + "-" + ano;
		            		System.out.println("FECHA DE HOY: " + Today);
		        
		            		String resJSON = llamadaWS(lastLat / 1E6, lastLon / 1E6, /*srv_date*/Today, diasRef, horaTemp);
		        
		            		if (resJSON != "Error") {
		            			try {
		            				JSONObject jsonObject = new JSONObject(resJSON);
		            				String estado = jsonObject.getString("statusMsg");
		            				if (estado.compareTo("OK") == 0) {
		            					evapStr = jsonObject.getString("etp");
		            				} else {
		            					evapStr = "no existe información";
		            				}
		            			} catch (JSONException e) {
		            				evapStr = "el servidor está offline";
		            			}
		            		} else {
		            			evapStr = "el servidor está offline";
		            		}
		            		// *************************************
	    			
		            		dialog.setMessage(geocodificar(lastLat, lastLon) +
		        			"\n\nLatitud: " + lastLat / 1E6 +
		        			"\nLongitud: " + lastLon / 1E6 + 
		        			"\n\nEvapotranspiración: " + evapStr);
		            		
		            		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						    	public void onClick(DialogInterface dialog, int id) {
						    		dialog.dismiss();
						    	}
						    });
		            	} catch (Exception e) {  } 	
		            	progressDialog.dismiss();
		            	
				        MapsActivity.this.runOnUiThread(
				        	new Runnable() {
								public void run() {
						        	dialog.show();
								}
							}	
				        );       
		            } 
		        }.start();
        	}
        });
        
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
        dialog.setMessage("<< ADVERTENCIA >>\n\n" +
        				  "Esta aplicación ha sido diseñada para mostrar\n" +
        				  "la utilidad real de la API para el cálculo de\n" +
        				  "la evapotranspiración potencial en Tenerife.\n\n" +
        				  "La respuesta de esta API está sujeta a la\n" +
        				  "disponibilidad del servidor en donde se ha\n" +
        				  "instalado el servicio que accede a dicha API,\n" +
        				  "por lo que puede que no esté disponible en\n" +
        				  "determinados momentos.\n\n" +
        				  "Los datos expuestos en esta aplicación son\n" +
        				  "meramente informativos y en ningún caso\n" +
        				  "pretenden reflejar una información exacta\n" +
        				  "sobre la evapotranspiración en la isla.\n\n\n" +
        				  "<< INSTRUCCIONES DE USO >>\n\n" +
        				  "1- Toque el mapa para señalar una ubicación.\n\n" +
        				  "2- Pulse sobre 'Solicitar ET' para ver la\n" +
        				  "     información de la ubicación actual.\n\n" +
        				  "3- Use el Multitouch (efecto pinza) para\n" +
        				  "     acercar o alejar el mapa.\n\n" +
        				  "4- Despliegue el menú para configurar las\n" +
        				  "     opciones de la llamada al Web Service\n" +
        				  "     de la API.\n\n" +
        				  "5- Busque ubicaciones por nombre a través del\n" +
        				  "     buscador situado en el menú.\n\n" +
        				  "6- Modifique el tipo de mapa a satélite para\n" +
        				  "     poder orientarse con mayor precisión.\n");
        dialog.setIcon(R.drawable.icono);
        dialog.setInverseBackgroundForced(true);
        dialog.setTitle("Bienvenido a ETMAP App");
        dialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
			    dialog.dismiss();
			}
		});		
        dialog.show();
    }
	
	protected String llamadaWS(double srvCall_lat, double srvCall_lon, String srvCall_date,
			int srvCall_days, int srvCall_hour) {
		final String srvCall_server = "http://192.168.1.23:8080/EtpRestServer/getetp/json?"; 
		final String srvCall_ftp = "false";
		
		System.out.println("ENTRADA WS: " + srvCall_server +
				  		   "lat=" + srvCall_lat +
				  		   "&lng=" + srvCall_lon +
				  		   "&date=" + srvCall_date +
				  		   "&days=" + srvCall_days +
				  		   "&hour=" + srvCall_hour +
				  		   "&ftp=" + srvCall_ftp);
		
		HttpClient httpclient = new DefaultHttpClient();  
        HttpGet request = new HttpGet(srvCall_server +
        							  "lat=" + srvCall_lat +
        							  "&lng=" + srvCall_lon +
        							  "&date=" + srvCall_date +
        							  "&days=" + srvCall_days +
        							  "&hour=" + srvCall_hour +
        							  "&ftp=" + srvCall_ftp);  
        
        request.addHeader("deviceId", "NexusOne");  
        ResponseHandler<String> handler = new BasicResponseHandler();
        
        String srvRes_json = "Error";
        
        try {  
            srvRes_json = httpclient.execute(request, handler);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        httpclient.getConnectionManager().shutdown();
        
        System.out.println("SALIDA WS: " + srvRes_json);
		
        return srvRes_json;
	}
	
	protected String geocodificar(int lat, int lon) {		
		String lugar = "";
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
        	List<Address> addresses = geoCoder.getFromLocation(
                    lat  / 1E6, 
                    lon / 1E6, 1);
        	
        	if (!addresses.isEmpty()) {
        		if (addresses.get(0).getMaxAddressLineIndex() > 1) {
        			lugar = addresses.get(0).getLocality();
        			System.out.println(lugar);
        			if (lugar.compareTo("null") == 0) {
        				lugar = "Indeterminado";
        			}
        		} else {
        			lugar = "Indeterminado";
        		}		            	
        		if (addresses.get(0).getMaxAddressLineIndex() > 0) {
        			lugar += ", " + addresses.get(0).getCountryName();
        		} else {
        			lugar = "Sin información";
        		}
        	} else {
        		lugar = "Sin información";
        	}		            		
        } catch (IOException e) {                
        	lugar = "Sin información";
        }			
		return lugar;
	}
    
    protected void centrarMapaEnTenerifeFast() {
    	mapView = (MapView) findViewById(R.id.mapview);
        // OLD - mapView.setBuiltInZoomControls(true);
        mapcontroller = mapView.getController();      
        // OLD - mapcontroller.zoomToSpan( (int) (esquina1GeoPoint.getLatitudeE6() - esquina2GeoPoint.getLatitudeE6()), (int) (esquina1GeoPoint.getLongitudeE6() - esquina2GeoPoint.getLongitudeE6()));
        mapcontroller.setZoom(zoomDefLevel);
        mapcontroller.animateTo(initGeoPoint);
    }
    
    protected void centrarMapaEnTenerifeSlow() {
    	mapcontroller.animateTo(initGeoPoint);
    	while (mapView.getZoomLevel() != zoomDefLevel) {
    		if (mapView.getZoomLevel() < zoomDefLevel) {
    			mapcontroller.zoomIn();
    		} else {
    			mapcontroller.zoomOut();
    		}	
    	}
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opciones, menu);
        
    	return true;
    }
    
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
        	// opciones del menu principal
            case R.id.buscarlugar : {
            	Intent intent = new Intent(MapsActivity.this, SearchActivity.class);
            	startActivityForResult(intent,0);
            }
            break;
            case R.id.centrarmapa : {
		        centrarMapaEnTenerifeSlow();
            }
            break;
            // opciones de "cambiar tipo mapa"
            case R.id.tipoTrafico : {
            	mapView.setTraffic(true);
            	mapView.setSatellite(false);
            	item.setChecked(true);
            }
            break;
            case R.id.tipoSatelite : {
            	mapView.setSatellite(true);
            	mapView.setTraffic(false);
            	item.setChecked(true);
            }
            break;
            // opciones de "cambiar dias referencia"
            case R.id.dias1 : {
            	diasRef = 1;
            	item.setChecked(true);
            }
            break;
            case R.id.dias3 : {
            	diasRef = 3;
            	item.setChecked(true);
            }
            break;
            case R.id.dias5 : {
            	diasRef = 5;
            	item.setChecked(true);
            }
            break;
            case R.id.dias7 : {
            	diasRef = 7;
            	item.setChecked(true);
            }
            break;
            // opciones de "cambiar datos temperatura"
            case R.id.hora02 : {
            	horaTemp = 2;
            	item.setChecked(true);
            }
            break;
            case R.id.hora11 : {
            	horaTemp = 11;
            	item.setChecked(true);
            }
            break;
            case R.id.hora14 : {
            	horaTemp = 14;
            	item.setChecked(true);
            }
            break;
            case R.id.hora23 : {
            	horaTemp = 23;
            	item.setChecked(true);
            }
            break;
        }
		return true;
    }
}