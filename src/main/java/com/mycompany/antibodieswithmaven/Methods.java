/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.antibodieswithmaven;

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
        String peptideSeq = peptide.seq;
        String region = ps.sequence.substring(startRegionCoordinate, startRegionCoordinate + peptideSeq.length());
        int distance = 0;
        int minCover = Integer.MAX_VALUE;
        for (int i = 0; i < peptideSeq.length(); i++) {
            if (ps.cover[i + startRegionCoordinate] < minCover) {
                minCover = ps.cover[i + startRegionCoordinate];
            }
        }
        if (minCover > 10) {
            return peptideSeq.length();
        }
        for (int i = 0; i < peptideSeq.length(); i++) {
            if (peptideSeq.charAt(i) != region.charAt(i)) {
                // лейцин и изолейцин считаются за равных
                if ((peptideSeq.charAt(i) == 'I' && region.charAt(i) == 'L') || (peptideSeq.charAt(i) == 'L' && region.charAt(i) == 'I')) {
                    continue;
                }
                distance++;
            }
        }
        return distance;
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
