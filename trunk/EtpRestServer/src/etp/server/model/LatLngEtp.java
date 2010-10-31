package etp.server.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LatLngEtp {
	private Double lat;
	private Double lng;
	private Double etp;
	private String date;
	private Integer hour;
	private Integer days;
	private String statusMsg;

	/**
	 * @return the latitude
	 */
	public Double getLat() {
		return lat;
	}
	/**
	 * @param lat the latitude to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}
	/**
	 * @return the longitud
	 */
	public Double getLng() {
		return lng;
	}
	/**
	 * @param lng the longitud to set
	 */
	public void setLng(Double lng) {
		this.lng = lng;
	}
	/**
	 * @return the potential evapotranspiration
	 */
	public Double getEtp() {
		return etp;
	}
	/**
	 * @param etp the potential evapotranspiration to set
	 */
	public void setEtp(Double etp) {
		this.etp = etp;
	}
	/**
	 * @return the statusMsg
	 */
	public String getStatusMsg() {
		return statusMsg;
	}
	/**
	 * @param statusMsg the statusMsg to set
	 */
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	/**
	 * @return the initial date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the initial date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the hour the satellite passes
	 */
	public Integer getHour() {
		return hour;
	}
	/**
	 * @param hour the hour the satellite passes to set
	 */
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	/**
	 * @return the days backward from the initial date
	 */
	public Integer getDays() {
		return days;
	}
	/**
	 * @param days the days backward from the initial date to set
	 */
	public void setDays(Integer days) {
		this.days = days;
	}
	
}
