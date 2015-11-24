/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author pavelgulaev
 */

public class Peptide {
    public String seq;
    int numOfRecordsInTSV;
    LinkedList<String> proteinNamesWhereMayOccurrencePeptide;
    // окончательно высчитанные координаты
    PepCoordinates pepCoords;
    private boolean containsInProbSeq = false;
    
    public Peptide(String seq, int numOfRecordsInTSV, LinkedList<String> proteinNamesWhereMayOccurrencePeptid) {
        this.seq = seq;
        this.numOfRecordsInTSV = numOfRecordsInTSV;
        this.proteinNamesWhereMayOccurrencePeptide = proteinNamesWhereMayOccurrencePeptid;
    }

    public Peptide(String seq, int numOfRecordsInTSV, PepCoordinates pepCoords) {
        this.seq = seq;
        this.numOfRecordsInTSV = numOfRecordsInTSV;
        this.pepCoords = pepCoords;
    }

    private Peptide(String seq, int numOfRecordsInTSV, LinkedList<String> proteinNamesWhereMayOccurrencePeptide, PepCoordinates pepCoords, boolean containsInProbSeq) {
        this.seq = seq;
        this.numOfRecordsInTSV = numOfRecordsInTSV;
        this.proteinNamesWhereMayOccurrencePeptide = proteinNamesWhereMayOccurrencePeptide;
        this.pepCoords = pepCoords;
        this.containsInProbSeq = containsInProbSeq;
    }
    
    
    
    Collection<PepCoordinates> occurrencesInBigSeq = new TreeSet<PepCoordinates>(new Comparator<PepCoordinates>(){

        @Override
        public int compare(PepCoordinates o1, PepCoordinates o2) {
            return o1.left - o2.left;
        }
        
    });
//    LinkedList<PepCoordinates> occurrencesInBigSeq = new LinkedList<>();

    public LinkedList<String> getProteinNamesWhereMayOccurrencePeptide() {
        return proteinNamesWhereMayOccurrencePeptide;
    }

    public int getNumOfRecordsInTSV() {
        return numOfRecordsInTSV;
    }

    public void setNumOfRecordsInTSV(int numOfRecordsInTSV) {
        this.numOfRecordsInTSV = numOfRecordsInTSV;
    }

    public boolean isContainsInProbSeq() {
        return containsInProbSeq;
    }

    public void setContainsInProbSeq(boolean containsInProbSeq) {
        this.containsInProbSeq = containsInProbSeq;
    }
    
    

    public Collection<PepCoordinates> getOccurrencesInBigSeq() {
        return occurrencesInBigSeq;
    }
//    public PepCoordinates getFirstCoordinate() {
//        return occurrencesInBigSeq.getFirst();
//    }

    public PepCoordinates getPepCoords() {
        return pepCoords;
    }

    public void setPepCoords(PepCoordinates pepCoords) {
        this.pepCoords = pepCoords;
    }
    
    public Peptide clone() {
        return new Peptide(seq, numOfRecordsInTSV, proteinNamesWhereMayOccurrencePeptide, pepCoords, containsInProbSeq);
    }
    
}