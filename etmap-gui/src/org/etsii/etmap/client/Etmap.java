package org.etsii.etmap.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
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
//	private Label resultsLabel = new Label();
//	private Label errorMsgLabel = new Label();
	private final DialogBox dialogoError = new DialogBox();
	private HTML errorMsg;

	/**
	 * Convert the string of JSON into JavaScript object.
	 */
//	private final native JsArray<EtpData> asArrayOfEtpData(String json) /*-{ return eval( "(" + json + ")" );	}-*/;
	
	/**
	 * Convert the string of JSON into JavaScript object.
	 */
	// Parenthesis inside surrounding json string inside eval are necessary to parse correctly the string,
	// otherwise throws an JavaScriptException (SyntaxError: invalid label)
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
//			resultsLabel.setText(Integer.toString(response.getStatusCode()));
			if (200 == response.getStatusCode()) {
//				JsArray<EtpData> data = asArrayOfEtpData(responseText);
				EtpData etp = asEtpData(responseText);
//				Window.alert(Integer.toString(data.length()));
				Window.alert(responseText);
//				for (int i = 0; i < data.length(); i++) {
//					EtpData etp = data.get(i);
					if (etp.getStatusMsg().equals("OK")) {
						Window.alert("Dentro!!");
//						resultsLabel.setText(Double.toString(etp.getEtp()));
//						errorMsgLabel.setVisible(false);
					} else {
						displayError("The server produced an error ",
								etp.getStatusMsg());
					}
//				}
			} else {
				displayError("No funciona ", responseText);
			}
		}
	}

	private void showEtpOnMap(LatLng coord) {
		String url = "http://localhost:8080/EtpRestServer/getetp/json?";
		//String url = "http://api.search.yahoo.com\\/ImageSearchService\\/V1/imageSearch?appid=YahooDemo&query=potato&results=2&output=json";
		url += "lat=" + Double.toString(coord.getLatitude());
		url += "&lng=" + Double.toString(coord.getLongitude());
		url += "&days=7";
		url += "&hour=14";
//		String url = "http://www.google.es";

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
		map.addMapClickHandler(new EtMapClickHandler());

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
	 * If can't get JSON, display error message.
	 * 
	 * @param error
	 */
	private void displayError(String error, String info) {
//		errorMsgLabel.setText("Error: " + error + "(" + info + ")");
//		errorMsgLabel.setVisible(true);
		errorMsg.setText(error + " (" + info + ")");
		dialogoError.show();
		dialogoError.center();
	}

	private void displayParseError(String responseText) {
		displayError("Ha fallado el análisis del objeto JSON", responseText);
	}

	private void displayRequestError(String message) {
		displayError("Ha fallado la solicitud ", message);
	}

	private void displaySendError(String message) {
		displayError("Ha fallado el envío ", message);
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
	    errorMsg.setStyleName("texto15");
	    ok.setStyleName("texto13");
		VerticalPanel vertical = new VerticalPanel();
		vertical.setSpacing(10);
		vertical.add(errorMsg);
		vertical.add(ok);
		vertical.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
		dialogoError.setWidget(vertical);
	}

}
