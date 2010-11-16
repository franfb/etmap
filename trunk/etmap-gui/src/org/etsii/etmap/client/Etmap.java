package org.etsii.etmap.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Etmap implements EntryPoint {
	private MapWidget map;
	private MarkerOptions etIcon1;
	private MarkerOptions etIcon2;
	private MarkerOptions etIcon3;
	private final DialogBox dialogoError = new DialogBox();
	private HTML errorMsg;
	private String location;

	/**
	 * Convert the string of JSON into JavaScript object.
	 */
	// Parenthesis surrounding json string inside eval are necessary to parse
	// correctly the string, otherwise throws a JavaScriptException
	// (SyntaxError: invalid label)
	private final native EtpData asEtpData(String json) /*-{ return eval( "(" + json + ")" );	}-*/;

	/**
	 * Class for handling the response text associated with a request for an Etp
	 * JSON object.
	 */
	private class EtpJSONResponseTextHandler implements RequestCallback {
		public void onError(Request request, Throwable exception) {
			displayRequestError(exception.toString());
		}

		public void onResponseReceived(Request request, Response response) {
			String responseText = response.getText();
			if (200 == response.getStatusCode()) {
				EtpData etp = asEtpData(responseText);
				if (etp.getStatusMsg().equals("OK")) {
					// Window.alert("Dentro!!");
					Marker m = createMarkerEt(etp);
					map.addOverlay(m);
					// infoWindowEt(m, etp);
				} else {
					displayError("El servidor ha devuelto un error",
							etp.getStatusMsg());
				}
			} else {
				displayError("Código de respuesta inválido", responseText);
			}
		}
	}

	private void showEtpOnMap(LatLng coord) {
		String url = "http://localhost:8080/EtpRestServer/getetp/json?";
		url += "lat=" + Double.toString(coord.getLatitude());
		url += "&lng=" + Double.toString(coord.getLongitude());
		url += "&date=08-01-2009";
		url += "&days=7";
		url += "&hour=14";
		url += "&ftp=false";

		url = URL.encode(url);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new EtpJSONResponseTextHandler());
		} catch (RequestException e) {
			displayRequestError(e.getLocalizedMessage());
		}
	}

	/**
	 * Class for handling the map click event
	 */
	private class EtMapClickHandler implements MapClickHandler {
		public void onClick(MapClickEvent event) {
			if (event.getLatLng() != null) {
				showEtpOnMap(event.getLatLng());
			}
			// else {
			//
			// }
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		buildUi();
		crearDialogoError();
		map.addMapClickHandler(new EtMapClickHandler());

		// Loads icons
		Icon icon = Icon.newInstance();
		icon.setIconAnchor(Point.newInstance(16, 16));
		icon.setInfoWindowAnchor(Point.newInstance(32, 0));
		icon.setShadowURL("http://maps.google.com/mapfiles/ms/micons/pushpin_shadow.png");
		icon.setImageURL("http://maps.google.com/mapfiles/ms/micons/grn-pushpin.png");
		etIcon1 = MarkerOptions.newInstance();
		etIcon1.setIcon(icon);

		icon = Icon.newInstance();
		icon.setIconAnchor(Point.newInstance(16, 16));
		icon.setInfoWindowAnchor(Point.newInstance(32, 0));
		icon.setShadowURL("http://maps.google.com/mapfiles/ms/micons/pushpin_shadow.png");
		icon.setImageURL("http://maps.google.com/mapfiles/ms/micons/blue-pushpin.png");
		etIcon2 = MarkerOptions.newInstance();
		etIcon2.setIcon(icon);

		icon = Icon.newInstance();
		icon.setIconAnchor(Point.newInstance(16, 16));
		icon.setInfoWindowAnchor(Point.newInstance(32, 0));
		icon.setShadowURL("http://maps.google.com/mapfiles/ms/micons/pushpin_shadow.png");
		icon.setImageURL("http://maps.google.com/mapfiles/ms/micons/red-pushpin.png");
		etIcon2 = MarkerOptions.newInstance();
		etIcon2.setIcon(icon);
	}

	private void buildUi() {
		map = new MapWidget();
		LatLng tenerife = LatLng.newInstance(28.5160, -16.3761);
		map.setSize("100%", "100%");
		map.setCenter(tenerife, 13);
		map.setUIToDefault();

		VerticalPanel columna = new VerticalPanel();
		columna.setSize("370px", "100%");
		columna.setStyleName("columna");

		VerticalPanel columna2 = new VerticalPanel();
		columna2.setWidth("293px");

		HTML titulo1 = new HTML("<h1>Evapotranspiración<br><br>Canarias</h1>");
		HTML bienvenida = new HTML(
				"<b>Bienvenido/a al mapa de Evapotranspiración de Canarias</b>");
		HTML texto1 = new HTML(
				"<br>Esta aplicación web utiliza la información "
						+ "de los satélites de la NASA para estimar la Evapotranspiración potencial "
						+ "de cualquier punto del archipiélago de las Islas Canarias. ");

		HTML proyecto = new HTML(
				"<a href=\"http://code.google.com/p/etmap/\" target=\"_blank\">Página web del proyecto ETMAP </a>");

		titulo1.setStylePrimaryName("texto13");
		bienvenida.setStyleName("texto15");
		texto1.setStyleName("texto13");
		proyecto.setStyleName("texto13");

		Image ull = new Image("http://www.ull.es/Public/images/wull/logo.gif");

		columna2.add(titulo1);
		columna2.add(bienvenida);
		columna2.add(texto1);
		columna2.add(new HTML("<br>"));

		columna2.add(new HTML("<br>"));

		columna2.add(proyecto);
		columna2.add(ull);

		columna2.setCellHorizontalAlignment(ull,
				HasHorizontalAlignment.ALIGN_CENTER);
		columna2.setCellHorizontalAlignment(proyecto,
				HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel horizontal = new HorizontalPanel();
		VerticalPanel vertical = new VerticalPanel();

		vertical.setWidth("40px");
		vertical.add(new HTML("<br>"));
		vertical.add(new HTML("<br>"));
		horizontal.add(vertical);
		horizontal.add(columna2);
		columna.add(horizontal);

		RootPanel.get().add(map, 0, 0);
		RootPanel.get().add(columna, 50, 0);
	}

	/**
	 * Creates a marker for potential evapotranspiration
	 */
	private Marker createMarkerEt(final EtpData etp) {
		MarkerOptions markerOpt = MarkerOptions.newInstance();
		markerOpt.setClickable(true);
		MarkerOptions opt = etIcon1;
		if (etp.getEtp() >= 3) {
			opt = etIcon2;
		}
		if (etp.getEtp() >= 7) {
			opt = etIcon3;
		}
		final Marker marker = new Marker(LatLng.newInstance(etp.getLat(),
				etp.getLng()), opt);

		Geocoder geoc = new Geocoder();
		geoc.getLocations(LatLng.newInstance(etp.getLat(), etp.getLng()),
				new LocationCallback() {

					@Override
					public void onSuccess(JsArray<Placemark> locations) {
						// final String location = locations.get(0).getCity() +
						// ", " + locations.get(0).getState() + ", " +
						// locations.get(0).getCounty() + ", " +
						// locations.get(0).getLocality() + ", " +
						// locations.get(0).getAdministrativeArea();
						String location;
						Placemark pm = locations.get(0);
						if (pm.getCity() != null) {
							location = pm.getCity();
						} else {
							location = "Indeterminado";
						}
						if (pm.getCountry().equals("ES")) {
							location += ", España";
						}
						final String loc = location;

						marker.addMarkerClickHandler(new MarkerClickHandler() {
							@Override
							public void onClick(MarkerClickEvent event) {
								infoWindowEt(marker, etp, loc);
							}
						});
					}

					@Override
					public void onFailure(int statusCode) {
						final String location = "Sin información";
						marker.addMarkerClickHandler(new MarkerClickHandler() {
							@Override
							public void onClick(MarkerClickEvent event) {
								infoWindowEt(marker, etp, location);
							}
						});
					}
				});

		return marker;
	}

	/**
	 * Creates marker's Info Window with Geocoder information
	 */
	private void infoWindowEt(final Marker marker, final EtpData etp,
			final String location) {
		InfoWindow info = map.getInfoWindow();

		VerticalPanel vertical = new VerticalPanel();

		HTML titulo = new HTML("<b>Información de esta localización</b><br>");
		titulo.setStyleName("texto15");
		vertical.add(titulo);
		
		HTML text = new HTML(location
				+ "<br>Latitud: " + Double.toString(etp.getLat())
				+ "<br>Longitud: " + Double.toString(etp.getLng())
				+ "<br>Evapotranspiración: " + Double.toString(etp.getEtp()));
		text.setStyleName("texto13");
		vertical.add(text);

		info.open(marker, new InfoWindowContent(vertical));
	}

	/**
	 * If can't get JSON, display error message.
	 * 
	 * @param error
	 */
	private void displayError(String error, String info) {
		errorMsg.setText(error + ": " + info);
		dialogoError.show();
		dialogoError.center();
	}

	private void displayParseError(String responseText) {
		displayError("Ha fallado el análisis del objeto JSON", responseText);
	}

	private void displayRequestError(String message) {
		displayError("Ha fallado la solicitud", message);
	}

	private void displaySendError(String message) {
		displayError("Ha fallado el envío", message);
	}

	private void crearDialogoError() {
		Button ok = new Button("ok");
		dialogoError.setText("ERROR");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogoError.hide();
			}
		});
		dialogoError.setGlassEnabled(true);
		dialogoError.setAnimationEnabled(true);
		errorMsg = new HTML();
		HTML contactMsg = new HTML(
				"Vuelva a intentarlo. Si el problema persiste, por favor, póngase en contacto con nosotros.");
		errorMsg.setStyleName("texto15");
		contactMsg.setStyleName("texto15");
		ok.setStyleName("texto13");
		VerticalPanel vertical = new VerticalPanel();
		vertical.setSpacing(10);
		vertical.add(errorMsg);
		vertical.add(contactMsg);
		vertical.add(ok);
		vertical.setCellHorizontalAlignment(ok,
				HasHorizontalAlignment.ALIGN_CENTER);
		dialogoError.setWidget(vertical);
	}

}
