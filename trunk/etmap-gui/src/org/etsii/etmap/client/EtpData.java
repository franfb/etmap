package org.etsii.etmap.client;

import com.google.gwt.core.client.JavaScriptObject;

class EtpData extends JavaScriptObject {
	// Overlay types always have protected, zero argument constructors.
	protected EtpData() {
	}

	// JSNI methods to get ETP data.
	public final native double getLat() /*-{
		return this.lat;
	}-*/;

	public final native double getLng() /*-{
		return this.lng;
	}-*/;

	public final native double getEtp() /*-{
		return this.etp;
	}-*/;

	public final native String getDate() /*-{
		return this.date;
	}-*/;

	public final native int getHour() /*-{
		return this.hour;
	}-*/;

	public final native int getDays() /*-{
		return this.days;
	}-*/;

	public final native String getStatusMsg() /*-{
		return this.statusMsg;
	}-*/;
}
