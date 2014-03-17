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
public class MonthCount {

    private Integer jan; //1
    private Integer fev;//2
    private Integer mar;//3
    private Integer apr;//4
    private Integer may;//5
    private Integer jun;//6
    private Integer jul;//7
    private Integer ago;//8
    private Integer sep;//9
    private Integer oct;//10
    private Integer nov;//11
    private Integer dec;//12

    public MonthCount() {
        this.jan = 0;
        this.fev = 0;
        this.mar = 0;
        this.apr = 0;
        this.may = 0;
        this.jun = 0;
        this.jul = 0;
        this.ago = 0;
        this.sep = 0;
        this.oct = 0;
        this.nov = 0;
        this.dec = 0;
    }

    public void countMonth(Integer dayOfMonthFile1) {
        switch (dayOfMonthFile1) {
            case 1:
                jan++;
                break;
            case 2:
                fev++;
                break;
            case 3:
                mar++;
                break;
            case 4:
                apr++;
                break;
            case 5:
                may++;
                break;
            case 6:
                jun++;
                break;
            case 7:
                jul++;
                break;
            case 8:
                ago++;
                break;
            case 9:
                sep++;
                break;
            case 10:
                oct++;
                break;
            case 11:
                nov++;
                break;
            case 12:
                dec++;
                break;

        }
    }

    public Integer getJan() {
        return jan;
    }

    public Integer getFev() {
        return fev;
    }

    public Integer getMar() {
        return mar;
    }

    public Integer getApr() {
        return apr;
    }

    public Integer getMay() {
        return may;
    }

    public Integer getJun() {
        return jun;
    }

    public Integer getJul() {
        return jul;
    }

    public Integer getAgo() {
        return ago;
    }

    public Integer getSep() {
        return sep;
    }

    public Integer getOct() {
        return oct;
    }

    public Integer getNov() {
        return nov;
    }

    public Integer getDec() {
        return dec;
    }

    @Override
    public String toString() {
        return jan + ";" + fev + ";" + mar + ";" + apr + ";" + may + ";" + jun + ";"
                + jul + ";" + ago + ";" + sep + ";" + oct + ";" + nov + ";" + dec;
    }

    public static String getHeadCSV() {
        return "jan;fev;mar;apr;may;jun;jul;ago;sep;oct;nov;dec;";
    }
}
