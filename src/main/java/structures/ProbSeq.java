/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pavelgulaev
 */
public class ProbSeq {

    public String probSeqName;
    public StringBuilder sequence;
    public int[] cover;
    public boolean[] bounds;

    public List[] coverageByPeptides;

    public ProbSeq(String probSeqName, String probSeq) {
        this.probSeqName = probSeqName;
        this.sequence = new StringBuilder(probSeq);
        bounds = new boolean[probSeq.length()];
        cover = new int[probSeq.length()];
    }

    public ProbSeq(String probSeqName, String probSeq, int[] probSeqCover) {
        this(probSeqName, probSeq);
        this.cover = probSeqCover;
    }

    public int[] recountCoverage(int start, int end) {
        int[] coverage = cover.clone();
        String currSeq = sequence.toString();
        for (int i = 0; i < coverageByPeptides.length; i++) {
            for (int k = 0; k < coverageByPeptides[i].size(); k++) {
                Peptide peptide = (Peptide) coverageByPeptides[i].get(k);
                if (peptide.seq.equals("EEQFNSTFR")) {
//                    System.out.println("");
                }
                boolean coverPs = currSeq.substring(peptide.getPepCoords().left, peptide.getPepCoords().right).equals(peptide.seq);
                boolean wasCoverPs = peptide.isContainsInProbSeq();

                // если покрытие поменялось
                if (coverPs ^ wasCoverPs) {
                    // стало покрыто
                    if (coverPs) {
                        Peptide updatedPeptide = peptide.clone();
                        updatedPeptide.setContainsInProbSeq(true);

                        for (int j = peptide.pepCoords.left; j < peptide.pepCoords.right; j++) {
//                            if (j==202) {
//                                System.out.println(peptide.seq+" " + peptide.pepCoords.left+" "+peptide.pepCoords.right);
//                                System.out.println(coverage[j]);
//                            }
                            int temp = 0;
                            temp = coverageByPeptides[j].indexOf(peptide);
                            coverageByPeptides[j].set(temp, updatedPeptide);

//                            Peptide cur = (Peptide)(coverageByPeptides[j].get(coverageByPeptides[j].indexOf(peptide)));
//                            cur.setContainsInProbSeq(true);
                            coverage[j] += peptide.numOfRecordsInTSV;

                        }
                    } // было покрыто
                    else {
                        Peptide updatedPeptide = peptide.clone();
                        updatedPeptide.setContainsInProbSeq(false);
                        coverageByPeptides[i].set(k, peptide);
                        for (int j = peptide.pepCoords.left; j < peptide.pepCoords.right; j++) {
//                            if (j==202) {
//                                System.out.println(peptide.seq+" " + peptide.pepCoords.left+" "+peptide.pepCoords.right);
//                                System.out.println(coverage[j]);
//                            }
//                            Peptide cur = (Peptide)(coverageByPeptides[j].get(coverageByPeptides[j].indexOf(peptide)));
//                            cur.setContainsInProbSeq(true);
                            coverageByPeptides[j].set(coverageByPeptides[j].indexOf(peptide), updatedPeptide);
                            coverage[j] -= peptide.numOfRecordsInTSV;
                        }
                    }
                }
            }
        }
        return coverage;
    }

    public void setCoverageByPeptides(List[] coverageByPeptides) {
        this.coverageByPeptides = coverageByPeptides;
    }

    public ProbSeq clone() {
        ProbSeq res = new ProbSeq(probSeqName, sequence.toString(), cover.clone());
        return res;
    }

    public List[] cloneCoverageByPeptides() {
        List[] res = new List[coverageByPeptides.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = new LinkedList<Peptide>();
            for (Object obj : coverageByPeptides[i]) {
                Peptide peptide = (Peptide) obj;
                res[i].add(peptide.clone());
            }
        }
        return res;
    }
}
