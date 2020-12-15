/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Activities;

import java.util.LinkedList;

/**
 *
 * @author saby_
 */
public class Procedure {
    private String name;
    private String urlPdf;
    private LinkedList<String> competenciesList;
    
    public Procedure(String name,String urlPdf,LinkedList<String> competenciesList) { 
    this.name=name;
    this.urlPdf=urlPdf;
    this.competenciesList=competenciesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public LinkedList<String> getCompetenciesList() {
        return competenciesList;
    }

    public void setCompetenciesList(LinkedList<String> competenciesList) {
        this.competenciesList = competenciesList;
    }
    
}
