/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import helpers.*;
import java.io.File;
import structures.Peptide;
import structures.PepCoordinates;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pavelgulaev
 */
public class BaseFile {

    LinkedHashMap<String, String> data;
//    String probSeqName;
//    String probSeq;

    ProbSeq ps;

    public BaseFile(String fileNameOfBase) {
        MyReader fileReader = createFileReaderFromFile(fileNameOfBase);
        data = readProteins(fileReader, true);
    }

    public BaseFile(ProbSeq ps, LinkedHashMap<String, String> db) {
        this.ps = ps;
        this.data = db;
    }

    public void saveDbToFile(String filename) {
        File file = new File(filename);
//        MyWriter out = null;
        try (MyWriter out = new MyWriter(new FileOutputStream(filename))) {
            file.createNewFile();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                out.println(">" + entry.getKey());
                out.println(entry.getValue());
            }
        } catch (IOException ex) {
            Logger.getLogger(BaseFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Problem with writing to output file");
        }
    }

    private LinkedHashMap<String, String> readProteins(MyReader in, boolean hasProbSeq) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        try {
            String seqName = in.nextString();
            StringBuilder seq = new StringBuilder();
            while (true) {
                String curString = in.nextString();
                if (curString == null) {
                    break;
                }
                if (curString.startsWith(">")) {
                    // проверка есть ли в первом поле предполагаемая строка, если нет, то сразу считывать в базу
                    if (!hasProbSeq) {
                        result.put(seqName.substring(1), seq.toString());
                    } else {
                        result.put(seqName.substring(1).split(" ")[0], seq.toString());
                        ps = new ProbSeq(seqName.substring(1).split(" ")[0], seq.toString());
                        hasProbSeq = false;
                    }
                    seqName = curString;
                    seq = new StringBuilder();
                } else {
                    seq.append(curString);
                }

            }
        } catch (IOException e) {
            System.out.println("Error with read database");
        }
        return result;
    }

    private LinkedHashMap<String, String> readProteins(MyReader in) {
        return readProteins(in, false);
    }

//    public void updateBlastAndProbSeq(String probSeq, String filenameOfBlastData) {
//        MyReader fileReader = createFileReaderFromFile(filenameOfBlastData);
//        this.probSeq = probSeq;
//        data = readProteins(fileReader);
//        data.put(probSeqName, probSeq);
//    }
    private MyReader createFileReaderFromFile(String filename) {
        MyReader fr = null;
        try {
            fr = new MyReader(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.out.println("Can't find file with base");
        }
        return fr;
    }

    private LinkedList<PepCoordinates> findPeptideInProteins(Peptide peptide, HashMap<String, String> proteins) {
        LinkedList<PepCoordinates> result = new LinkedList<>();
        for (String proteinWithPeptideName : peptide.getProteinNamesWhereMayOccurrencePeptide()) {
            String proteinSeq = "";
            for (String proteinNameInDB : proteins.keySet()) {
                if (proteinNameInDB.contains(proteinWithPeptideName)) {
                    proteinSeq = proteins.get(proteinNameInDB);
                    int startCoordinate = proteinSeq.indexOf(peptide.seq) + 1;
                    // сомнительное решение
                    boolean isConstantRegion = (proteinSeq.length() > 150) ? true : false;
                    PepCoordinates newCoord = new PepCoordinates(
                            startCoordinate, startCoordinate + peptide.seq.length(), isConstantRegion);
                    result.add(newCoord);
                    peptide.getOccurrencesInBigSeq().add(newCoord);
//                    System.out.println("Peptide, protein in the data and start coord in protein \n"+peptide.seq+" "+proteinSeq+" "+(proteinSeq.indexOf(peptide.seq)+1));
                }
            }
        }
        return result;
    }

    public List[] addCoordinatesToPeptidesObjects(LinkedList<Peptide> peptides) {
        List[] coords = new List[ps.sequence.length()];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = new LinkedList<Peptide>();
        }
        for (Peptide peptide : peptides) {
            findPeptideInProteins(peptide, data);
            addPeptidesToMap(peptide, coords);
        }
        return coords;
    }

    private void addPeptidesToMap(Peptide peptide, List[] coords) {
        if (peptide.isContainsInProbSeq()) {
            PepCoordinates coordsInPS = peptide.getPepCoords();
            for (int i = coordsInPS.left; i < coordsInPS.right; i++) {
                coords[i].add(peptide);
            }
            return;
        }
        for (PepCoordinates pepCoordinates : peptide.getOccurrencesInBigSeq()) {
            if (pepCoordinates.isConstantRegion) {
                for (int i = 120; i < 129; i++) {
                    Peptide notCoveredPeptide = new Peptide(peptide.seq, 
                            peptide.numOfRecordsInTSV, 
                            new PepCoordinates(pepCoordinates.left+i, pepCoordinates.right+i));
                    for (int j = notCoveredPeptide.pepCoords.left; j < notCoveredPeptide.pepCoords.left; j++) {
                        coords[j].add(notCoveredPeptide);
                    }
                }
            } else {
                for (int i = pepCoordinates.left; i < pepCoordinates.right; i++) {
                    Peptide notCoveredPeptide = new Peptide(peptide.seq, 
                            peptide.numOfRecordsInTSV, 
                            new PepCoordinates(pepCoordinates.left, pepCoordinates.right));
                    coords[i].add(notCoveredPeptide);
                }
            }
        }
    }

    public ProbSeq getPs() {
        return ps;
    }

    public void setPs(ProbSeq ps) {
        this.ps = ps;
    }

    public LinkedHashMap<String, String> getData() {
        return data;
    }

}
