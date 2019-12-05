/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile2;

import java.util.Comparator;

/**
 *
 * @author juang
 */
public class YearSort implements Comparator<Invention> {

    @Override
    public int compare(Invention o1, Invention o2) {
        if (o1.getYear() < o2.getYear()) {
            return 1;
        } else {
            return -1;
        }
    }
}
