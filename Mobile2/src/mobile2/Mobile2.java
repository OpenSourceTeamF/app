/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile2;

import java.util.ArrayList;
import java.util.Random;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author juang
 */
public class Mobile2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReadFile file = new ReadFile();
        ArrayList<Invention> kk = file.getInventionList();

    }
}
