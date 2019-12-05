/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile2;

import java.util.Comparator;
import java.awt.Image;
/**
 *
 * @author juang
 */
public class Invention {

    private int year, century, id;
    private String invention, person, country, hint;
    public Invention() {
        year = 0;
        century = 0;
        id=0;
        invention = "";
        person = "";
        country = "";
        hint = "";
    }



    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        this.century = (year / 100) + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCentury() {
        return century;
    }

    public void setCentury(int century) {
        this.century = century;
    }

    public String getInvention() {
        return invention;
    }

    public void setInvention(String invent) {
        this.invention = invent;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

}
