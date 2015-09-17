/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

/**
 *
 * @author geazzy
 */
public class PeriodOfDayCount {

    private Integer manhaCount;
    private Integer tardeCount;
    private Integer noiteCount;
    private Integer madrugadaCount;

    public PeriodOfDayCount() {
        this.manhaCount = 0;
        this.tardeCount = 0;
        this.noiteCount = 0;
        this.madrugadaCount = 0;
    }

    public void countPeriodOfDay(PeriodOfDay pedriodOfDayFile) {
        switch (pedriodOfDayFile) {
            case MANHA:
                this.manhaCount++;
                break;
            case TARDE:
                this.tardeCount++;
                break;
            case NOITE:
                this.noiteCount++;
                break;
            case MADRUGADA:
                this.madrugadaCount++;
                break;

        }

    }

    public Integer getManhaCount() {
        return manhaCount;
    }

    public Integer getTardeCount() {
        return tardeCount;
    }

    public Integer getNoiteCount() {
        return noiteCount;
    }

    public Integer getMadrugadaCount() {
        return madrugadaCount;
    }

    @Override
    public String toString() {
        return manhaCount + ";" + tardeCount + ";" + noiteCount + ";" + madrugadaCount;
    }

    public static String getHeadCSV() {
        return "manha;tarde;noite;madrugada;";
    }
}
