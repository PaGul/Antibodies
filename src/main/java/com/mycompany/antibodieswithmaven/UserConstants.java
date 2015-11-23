/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.antibodieswithmaven;

import java.util.HashMap;

/**
 *
 * @author pavelgulaev
 */
public class UserConstants {
    public final static String myProjectDir = "./workingData/";
    public static String dbFile = myProjectDir + "db/DB.fasta";
    public static String tsvDirectory = myProjectDir + "oldTSV/";
    
    public static String newDbDir = myProjectDir + "db/";
    public static String newDbFile = newDbDir + "newDB.fasta";
    
    
    public static String MSGFPlus = myProjectDir + "MSGFPlus.20140630/MSGFPlus.jar";
    public static String spectraAddress = myProjectDir + "HC-spectra/";
    public static String mzidsAnsTsvDirectory = myProjectDir + "newmzidAndTsv/";
    public static String[] enzymeNames = {"ArgC", "AspN", "Chymo", "GluC", "LysC", "Tryp"};
    public static HashMap<String, Integer> EnzymeToNumber = new HashMap<String, Integer>();
}
