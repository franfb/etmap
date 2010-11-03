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
	private static final ModeloEtLineal modelo = new ModeloEtLineal(new ParamConfig(), 0.5, -20);
	private static final Calendar refDay = Calendar.getInstance();
	public static final int DEFAULT_DAYS_IN_MEMORY = 7;
	
	public GetEtp() {
//		ParamConfig param = new ParamConfig();
		modelo.setDiasUtilizados(7);
		modelo.setHoraDeseada(HorasSat.AQUA_14H);
		modelo.setUsarFtp(false);
//		modelo = new ModeloEtLineal(param, 0.5, -20);
	}

	@GET
	@Path("{update}")
	@Produces(MediaType.TEXT_HTML)
	public String updateToCurrentDay() {

		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		refDay.clear();
//		refDay.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
		refDay.set(2009, 0, 8);
		// Yesterday is the reference day
		refDay.add(Calendar.DAY_OF_MONTH, -1);

		try {
			modelo.setUsarFtp(true);
			modelo.setDiasUtilizados(DEFAULT_DAYS_IN_MEMORY);
//			int numDays = modelo.buscarHdfs(7, 1, 2009);
			int numDays = modelo.buscarHdfs(refDay.get(Calendar.DAY_OF_MONTH), refDay.get(Calendar.MONTH) + 1, refDay.get(Calendar.YEAR));
			modelo.setUsarFtp(false);
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

	
	private boolean checkHour(ModeloEtLineal workingModel, Integer hour, LatLngEtp result) {
		switch (hour) {
		case 2:
			workingModel.setHoraDeseada(HorasSat.AQUA_02H);
			break;
		case 11:
			workingModel.setHoraDeseada(HorasSat.TERRA_11H);
			break;
		case 14:
			workingModel.setHoraDeseada(HorasSat.AQUA_14H);
			break;
		case 23:
			workingModel.setHoraDeseada(HorasSat.TERRA_23H);
			break;
		default:
			result.setStatusMsg("Hora incorrecta solicitada incorrecta. Elija entre {2, 11, 14, 23}");
			return false;
		}
		return true;
	}
	
	
	private boolean checkDays(ModeloEtLineal workingModel, Integer days, Integer hour, LatLngEtp result) {
		double despY = 0;
		double pendiente = 0;

		switch (days) {
		case 1:
			if (hour == 2) { despY = 0.7396776; pendiente = 0.2192183; }
			else if (hour == 11) { despY = -0.5657797; pendiente = 0.1549733; }
			else if (hour == 14) { despY = -0.5771249; pendiente = 0.1489726; }
			else if (hour == 23) { despY = 0.2437498; pendiente = 0.2388307; }
			break;
		case 3:
			if (hour == 2) { despY = 0.5158017; pendiente = 0.2331120; }
			else if (hour == 11) { despY = -1.1121347; pendiente = 0.1684281; }
			else if (hour == 14) { despY = -1.2150370; pendiente = 0.1627427; }
			else if (hour == 23) { despY = -0.01475418; pendiente = 0.25327133; }
			break;
		case 5:
			if (hour == 2) { despY = 0.4231908; pendiente = 0.2355496; }
			else if (hour == 11) { despY = -1.2747426; pendiente = 0.1714598; }
			else if (hour == 14) { despY = -1.4111010; pendiente = 0.1661040; }
			else if (hour == 23) { despY = -0.09524708; pendiente = 0.25638125; }
			break;
		case 7:
			if (hour == 2) { despY = 0.4078631; pendiente = 0.2336523; }
			else if (hour == 11) { despY = -1.3384307; pendiente = 0.1714753; }
			else if (hour == 14) { despY = -1.4581690; pendiente = 0.1654386; }
			else if (hour == 23) { despY = -0.1437212; pendiente = 0.2579475; }
			break;
		default:
			result.setStatusMsg("Número incorrecto de días utilizados. Elija entre {1, 3, 5, 7}");
			return false;
		}
		workingModel.setDespY(despY);
		workingModel.setPendiente(pendiente);
		return true;
	}
	
	
	@GET
	@Path("json")
	@Produces(MediaType.APPLICATION_JSON)
	public LatLngEtp getEtpAsJson(@QueryParam("lat") Double lat,
			@QueryParam("lng") Double lng,
			@QueryParam("date") String date,
			@QueryParam("days") Integer diasUsados,
			@QueryParam("hour") Integer hora,
			@QueryParam("ftp") Boolean usarFtp) {

		ModeloEtLineal workingModel;
		LatLngEtp lle = new LatLngEtp();
		lle.setLat(lat);
		lle.setLng(lng);
		lle.setDate(date);
		lle.setHour(hora);
		lle.setDays(diasUsados);
		
		// Check if requested days are less than max days allowed
		if (diasUsados > DEFAULT_DAYS_IN_MEMORY) {
			lle.setStatusMsg("No se pueden solicitar más de " + Integer.toString(DEFAULT_DAYS_IN_MEMORY) + " días.");
			return lle;
		}
		
		// Check requested date
		String[] splitDate = date.split("-");
		String dayStr = splitDate[0];
		String monthStr = splitDate[1];
		String yearStr = splitDate[2];
		Calendar dayReq = Calendar.getInstance();
		dayReq.clear();
		dayReq.set(Integer.parseInt(yearStr), Integer.parseInt(monthStr) - 1, Integer.parseInt(dayStr));
		long diffDays = (refDay.getTimeInMillis() - dayReq.getTimeInMillis()) / (24 * 60 * 60 * 1000);
		if (diffDays < 0) {
			lle.setStatusMsg("La fecha máxima disponible es la de ayer.");
			return lle;
		}
		// Data not in hard disk and don't use FTP
		else if (((diffDays + diasUsados) > DEFAULT_DAYS_IN_MEMORY) && (!usarFtp)) {
			lle.setStatusMsg("Datos no disponibles localmente, ¿desea descargarlos desde el FTP?");
			return lle;
		}
		// Data not in hard disk and use FTP
		else if (((diffDays + diasUsados) > DEFAULT_DAYS_IN_MEMORY) && (usarFtp)) {
			workingModel = new ModeloEtLineal(new ParamConfig(), 0.5, -20);
			try {
//				int numDays = modelo.buscarHdfs(7, 1, 2009);
				workingModel.setUsarFtp(true);
				workingModel.setDiasUtilizados(diasUsados);
				workingModel.buscarHdfs(dayReq.get(Calendar.DAY_OF_MONTH), dayReq.get(Calendar.MONTH) + 1, dayReq.get(Calendar.YEAR));
			}
			catch (ModelException e) {
				lle.setStatusMsg("Error al buscar los datos en la fecha seleccionada (" + e.getMessage() + ")");
				return lle;
			}
		}
		// Data in hard disk
		else if (diffDays > 0) {
			workingModel = new ModeloEtLineal(new ParamConfig(), 0.5, -20);
			try {
//				int numDays = modelo.buscarHdfs(7, 1, 2009);
				workingModel.setUsarFtp(false);
				workingModel.setDiasUtilizados(diasUsados);
				workingModel.buscarHdfs(dayReq.get(Calendar.DAY_OF_MONTH), dayReq.get(Calendar.MONTH) + 1, dayReq.get(Calendar.YEAR));
			}
			catch (ModelException e) {
				lle.setStatusMsg("Error al buscar los datos en la fecha seleccionada (" + e.getMessage() + ")");
				return lle;
			}
		}
		else if (diffDays == 0) {
			workingModel = modelo;
		}
		else {
			lle.setStatusMsg("Condición inesperada.");
			return lle;
		}

		
		// Check requested hour
		if (!checkHour(workingModel, hora, lle))
			return lle;

		// Check requested days and set linear model parameters
		workingModel.setDiasUtilizados(diasUsados);
		if (!checkDays(workingModel, diasUsados, hora, lle))
			return lle;

		// Get potential evapotranspiration with requested parameters
		try {
			Double et = workingModel.getEtByLatLon(lat, lng);

			if (et != ModeloEtLineal.NO_EVAPOTRANSP) {
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
}
