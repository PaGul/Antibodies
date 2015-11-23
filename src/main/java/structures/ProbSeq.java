/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavelgulaev
 */
public class ProbSeq {
    public String probSeqName;
    public StringBuffer sequence;
    public int[] cover;
    public boolean[] bounds;
    
    public List[] coverageByPeptides;
    public ProbSeq(String probSeqName, String probSeq) {
        this.probSeqName = probSeqName;
        this.sequence = new StringBuffer(probSeq);
        bounds = new boolean[probSeq.length()];
        cover = new int[probSeq.length()];
    }

    public ProbSeq(String probSeqName, String probSeq, int[] probSeqCover) {
        this(probSeqName, probSeq);
        this.cover = probSeqCover;
    }
    
    
    public void recountCoverage() {
        int[] coverage = new int[sequence.length()];
        String currSeq = sequence.toString();
        for (int i = 0; i < sequence.length(); i++) {
            for (Object obj : coverageByPeptides[i]) {
                Peptide peptide = (Peptide) obj;
                boolean coverPs = true;
                for (int j = peptide.pepCoords.left; j < peptide.pepCoords.right; j++) {
                    if (peptide.seq.charAt(j) != currSeq.charAt(j)) {
                        coverPs = false;
                        break;
                    }
                }
                if (coverPs) {
                    
                }
                
            }
        }
    }
    
    public ProbSeq clone() {
        ProbSeq res = new ProbSeq(probSeqName, sequence.toString(), cover.clone());
        return res;
    }
}
