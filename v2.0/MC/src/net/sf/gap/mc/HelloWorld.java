/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.gap.mc;
/**
 *
 * @author giovanni
 */
public class HelloWorld {

    public static void main(String[] arg) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("touch results.tar.bz2");
        } catch (Exception e) {}
    }
}
