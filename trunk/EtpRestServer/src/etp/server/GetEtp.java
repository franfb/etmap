package etp.server;

import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import etp.configuracion.ParamConfig;
import etp.modelo.HorasSat;
import etp.modelo.ModeloEtLineal;
import etp.modelo.exceptions.DataNotLoadedException;
import etp.modelo.exceptions.ModelException;

import etp.server.model.LatLngEtp;

@Path("/getetp")
public class GetEtp {
//	public static final ModeloEtLineal modelo = new ModeloEtLineal(7,
//			HorasSat.AQUA_14H, directorio_local_de_hdfs, ftp_de_hdfs, 0.5, -20);
	public static final ModeloEtLineal modelo = new ModeloEtLineal(new ParamConfig(), 0.5, -20);
	
	public GetEtp() {
//		ParamConfig param = new ParamConfig();
		modelo.setDiasUtilizados(7);
		modelo.setHoraDeseada(HorasSat.AQUA_14H);
//		modelo = new ModeloEtLineal(param, 0.5, -20);
	}

	@GET
	@Path("{update}")
	@Produces(MediaType.TEXT_HTML)
	public String updateToCurrentDay() {
		Calendar hoy = Calendar.getInstance();
		hoy.setTimeInMillis(System.currentTimeMillis());

		try {
			int numDays = modelo.buscarHdfs(7, 1, 2009);
			return "<html> " + "<title>" + "Current Day Updater" + "</title>"
					+ "<body><h1>" + "Current day successfully updated (" + Integer.toString(numDays) + " days available in memory)."
					+ "</body></h1>" + "</html> ";
			
		}
		catch (ModelException e) {
			return "<html> " + "<title>" + "Current Day Updater" + "</title>"
					+ "<body><h1>" + "Current day update FAILED: " + e.getMessage()
					+ "</body></h1>" + "</html> ";
		}
	}

	@GET
	@Path("json")
	@Produces(MediaType.APPLICATION_JSON)
	public LatLngEtp getEtpAsJson(@QueryParam("lat") Double lat,
			@QueryParam("lng") Double lng,
			@QueryParam("days") Integer diasUsados,
			@QueryParam("hour") Integer hora) {

		LatLngEtp lle = new LatLngEtp();

		switch (hora) {
		case 2:
			modelo.setHoraDeseada(HorasSat.AQUA_02H);
			break;
		case 11:
			modelo.setHoraDeseada(HorasSat.TERRA_11H);
			break;
		case 14:
			modelo.setHoraDeseada(HorasSat.AQUA_14H);
			break;
		case 23:
			modelo.setHoraDeseada(HorasSat.TERRA_23H);
			break;
		default:
			lle.setStatusMsg("Hora solicitada incorrecta. Elija entre {2, 11, 14, 23}");
			return lle;
		}

		modelo.setDiasUtilizados(diasUsados);
		switch (diasUsados) {
		case 1:
			modelo.setDespY(8);
			modelo.setPendiente(0.5);
			break;
		case 7:
			modelo.setDespY(-20);
			modelo.setPendiente(0.5);
			break;
		default:
			modelo.setDespY(8);
			modelo.setPendiente(0.5);
			modelo.setDiasUtilizados(1);
			lle.setStatusMsg("Número de días hacia atrás incorrecto. Elija entre {1, 3, 5, 7}");
			return lle;
		}

		try {
			Double et = modelo.getEtByLatLon(lat, lng);

			if (et != ModeloEtLineal.NO_EVAPOTRANSP) {
				lle.setLat(lat);
				lle.setLng(lng);
				lle.setEtp(et);
				lle.setStatusMsg("OK");
				return lle;
			}
			else {
				lle.setStatusMsg("No se ha podido calcular la evapotranspiración. Es posible que no haya datos de temperatura para los días solicitados. Pruebe con otro intervalo de fechas.");
			}
		} catch (DataNotLoadedException e) {
			lle.setStatusMsg("No hay datos cargados en memoria. ¿Ha olvidado llamar al servicio 'update'?");
		}
		return lle;
	}
	// @GET
	// @Path("{lat}/{lng}")
	// @Produces(MediaType.TEXT_XML)
	// public LatLngEtp getEtpAsXml(@PathParam("lat") Double lat,
	// @PathParam("lng") Double lng) {
	// LatLngEtp lle = new LatLngEtp();
	// lle.setLat(lat + 1);
	// lle.setLng(lng + 1);
	// lle.setEtp(lat + lng);
	// return lle;
	// }

	// // This method is called if HTML is request
	// @GET
	// @Produces(MediaType.TEXT_HTML)
	// public String sayHtmlHello() {
	// return "<html> " + "<title>" + "Hello ETPSERVER" + "</title>"
	// + "<body><h1>" + "Hello ETPSERVER" + "</body></h1>" + "</html> ";
	// }
}
