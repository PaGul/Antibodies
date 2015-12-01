/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.antibodieswithmaven;

import java.util.List;
import structures.PepCoordinates;
import structures.ProbSeq;
import structures.Peptide;

/**
 *
 * @author pavelgulaev
 */
public class Methods {

    public static int SimpleHammingDistance(String first, String second) {
        int distance = 0;
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    public static int HammingDistanceWithCoverage(Peptide peptide, ProbSeq ps, int startRegionCoordinate) {
        String pepSeq = peptide.seq;
        String region = ps.sequence.substring(startRegionCoordinate, startRegionCoordinate + pepSeq.length());
        int distance = 0;
        int minCover = Integer.MAX_VALUE;
        for (int i = 0; i < pepSeq.length(); i++) {
            if (ps.cover[i + startRegionCoordinate] < minCover) {
                minCover = ps.cover[i + startRegionCoordinate];
            }
        }
        if (minCover > 10) {
            return pepSeq.length();
        }
        for (int i = 0; i < pepSeq.length(); i++) {
            if (pepSeq.charAt(i) != region.charAt(i)) {
                // лейцин и изолейцин считаются за равных
                if ((pepSeq.charAt(i) == 'I' && region.charAt(i) == 'L') || (pepSeq.charAt(i) == 'L' && region.charAt(i) == 'I')) {
                    continue;
                }
                distance++;
            }
        }
        return distance;
    }

    public static boolean shouldIReplaceThePeptide(String peptideSeq, PepCoordinates coords, ProbSeq ps) {
        if (peptideSeq.equals("DKVSLTCMITDFFPEDITVEWQWNGQPAENYK")) {
            
        }
        if (peptideSeq.equals("GCLVKGYFPEPVTLTW")) {
//            System.out.println("");
        }
        
        String savedPs = ps.sequence.toString();
        List[] savedCoverageByPep = ps.cloneCoverageByPeptides();
        int preCoverSum = 0;
        for (int i = coords.left; i < coords.right; i++) {
            preCoverSum += ps.cover[i];
        }
        if (peptideSeq.equals("EEQFNSTFR")) {
//            System.out.println("");
        }
        ps.sequence.replace(coords.left, coords.right, peptideSeq);
        int newCoverSum = 0;
        int[] newCover = ps.recountCoverage(coords.left, coords.right);
        for (int i = coords.left; i < coords.right; i++) {
            newCoverSum += newCover[i];
        }
        if (preCoverSum < newCoverSum) {
            ps.cover = newCover.clone();
            return true;
        } else {
            ps.sequence = new StringBuilder(savedPs);
            ps.setCoverageByPeptides(savedCoverageByPep);
            return false;
        }
        
    }

    public static boolean PeptideCrossAnotherPeptide(int start, int end, boolean[] boundaries) {
        for (int i = start; i < end; i++) {
            if (boundaries[i]) {
                return true;
            }
        }
        return false;
    }

}
