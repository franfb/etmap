/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.configuracion;

import etp.modelo.HorasSat;

/**
 *
 */
public class ParamConfig {
    private Integer diasUtilizados = 1;
    private HorasSat horaDeseada = HorasSat.AQUA_14H;
    private String dirHdfs = "d:\\etsii\\pfc\\hdfs\\";
    private String dirFtp = "e4ftl01u.ecs.nasa.gov";
    private Boolean usarFtp = false;

    public void valoresPorDefecto() {
        diasUtilizados = 1;
        horaDeseada = HorasSat.AQUA_14H;
        dirHdfs = "d:\\etsii\\pfc\\hdfs\\";
        dirFtp = "e4ftl01u.ecs.nasa.gov";
        usarFtp = false;
    }

    public Integer getDiasUtilizados() {
        return diasUtilizados;
    }

    public void setDiasUtilizados(Integer diasUtilizados) {
        this.diasUtilizados = diasUtilizados;
    }

    public String getDirFtp() {
        return dirFtp;
    }

    public void setDirFtp(String dirFtp) {
        this.dirFtp = dirFtp;
    }

    public String getDirHdfs() {
        return dirHdfs;
    }

    public void setDirHdfs(String dirHdfs) {
        this.dirHdfs = dirHdfs;
    }

    public HorasSat getHoraDeseada() {
        return horaDeseada;
    }

    public void setHoraDeseada(HorasSat horaDeseada) {
        this.horaDeseada = horaDeseada;
    }

    public Boolean getUsarFtp() {
        return usarFtp;
    }

    public void setUsarFtp(Boolean usarFtp) {
        this.usarFtp = usarFtp;
    }
}
