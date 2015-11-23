/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import com.mycompany.antibodieswithmaven.UserConstants;
import helpers.MyReader;
import structures.Peptide;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import structures.ProbSeq;

/**
 *
 * @author pavel
 */
public class Tsv {

    MyReader in = null;

    public Tsv(String filename) {
        runReadOnFile(filename);
    }

    public Tsv(String[] filenames) {
        for (String filename : filenames) {
            runReadOnFile(filename);
        }
    }
    
    public static Tsv createTSV_FromDir(String tsvDirectory) {
        String[] tsvFiles = new String[UserConstants.enzymeNames.length];
        for (int i = 0; i < UserConstants.enzymeNames.length; i++) {
            tsvFiles[i] = tsvDirectory + UserConstants.enzymeNames[i] + ".tsv";
        }
        return new Tsv(tsvFiles);
    }

    private void runReadOnFile(String filename) {
        try {
            in = new MyReader(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.out.println("Input tsv-file not found");
        }
        read();
    }

    private void read() {
        try {
            in.nextString();
            String currentPeptide = "";
            while (true) {
                String fileName = in.nextWord();
                if (fileName == null) {
                    break;
                }
                for (int i = 2; i <= 14; i++) {
                    String field = in.nextWord();
                    switch (i) {
                        case 9:
                            field = field.replaceAll("\\W*\\d*", "");
                            currentPeptide = field;
                            break;
                        case 10:
                            if (field.startsWith("sp|")) {
                                continue;
                            } else {
                                String[] proteinsNames = field.split(";");
                                addPeptideToPeptideMap(currentPeptide, proteinsNames);
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Problems with read tsv-file");
        }
    }
    HashMap<String, Peptide> PeptideNameAndPeptide = new HashMap();

    public int[] makeCoverage(ProbSeq ps) {
        String seqShouldBeCovered = ps.sequence.toString();
        String seqName = ps.probSeqName;
        int[] coverage = new int[seqShouldBeCovered.length()];
        // Going through all peptides and find peptides, that contains in probSeq 
        for (Peptide peptide : PeptideNameAndPeptide.values()) {
            if (peptide.getProteinNamesWhereMayOccurrencePeptide().contains(seqName)) {
                int start = seqShouldBeCovered.indexOf(peptide.seq, 0);
                int end = start + peptide.seq.length();
                int pepCov = peptide.getNumOfRecordsInTSV();;
                for (int i = start; i < end; i++) {
                    try{
                        coverage[i] += pepCov;
                        peptide.setContainsInProbSeq(true);
                        peptide.setPepCoords(new PepCoordinates(start, end));
                    }
                    
                    // очень неприятно: MS/GF почему-то обнаруживает в последовательности пептиды, которых нет
                    catch (Exception e){
//                        System.out.println(peptide.seq);
//                        System.out.println(peptide.getProteinNamesWhereMayOccurrencePeptide().getFirst());
                    }
                }
            }
        }
        return coverage;
    }
    
    public int[] makeCoverage(ProbSeq ps, boolean t) {
        String seqShouldBeCovered = ps.sequence.toString();
        String seqName = ps.probSeqName;
        int[] coverage = new int[seqShouldBeCovered.length()];
        // Going through all peptides and find peptides, that contains in probSeq 
        for (Peptide peptide : PeptideNameAndPeptide.values()) {
            if (peptide.getProteinNamesWhereMayOccurrencePeptide().contains(seqName)) {
                int start = seqShouldBeCovered.indexOf(peptide.seq, 0);
                int end = start + peptide.seq.length();
                int pepCov = peptide.getNumOfRecordsInTSV();
                for (int i = start; i < end; i++) {
                    try{
                        coverage[i] += pepCov;
                    }
                    
                    // очень неприятно: MS/GF почему-то обнаруживает в последовательности пептиды, которых нет
                    catch (Exception e){
//                        System.out.println(peptide.seq);
//                        System.out.println(peptide.getProteinNamesWhereMayOccurrencePeptide().getFirst());
                    }
                }
            }
        }
        return coverage;
    }

    // если пептид найден в протеинах, то для каждой записи он будет содержать один и тот же списко возможных протеинов,
    // поэтому в начале в proteinNamesWhereMayOccurrencePeptide добавляются все возможные значения,
    // а потом после обнаружения записи в TSV каждый раз numOfRecordsInTSV увеличивается на 1.
    private void addPeptideToPeptideMap(String peptide, String[] proteinNames) {
        if (PeptideNameAndPeptide.containsKey(peptide)) {
            Peptide pastRecord = PeptideNameAndPeptide.get(peptide);
            pastRecord.setNumOfRecordsInTSV(pastRecord.getNumOfRecordsInTSV() + 1);
            PeptideNameAndPeptide.put(peptide, pastRecord);
        } else {
            LinkedList<String> proteinNamesWhereMayOccurrencePeptide = new LinkedList<>();
            for (int i = 0; i < proteinNames.length; i++) {
                // pre post используются для указания предыдущий аминокислот, пока можно убрать
                proteinNamesWhereMayOccurrencePeptide.add(proteinNames[i].substring(0, proteinNames[i].indexOf('(')));
            }
            PeptideNameAndPeptide.put(peptide, new Peptide(peptide, 1, proteinNamesWhereMayOccurrencePeptide));
        }
    }

    public LinkedList<Peptide> getPeptides(int minimumNumberOfOccurrences, ProbSeq ps) {
        String probSeqName = ps.probSeqName;
        LinkedList<Peptide> res = new LinkedList<>();
        for (Map.Entry<String, Peptide> entry : PeptideNameAndPeptide.entrySet()) {
            // не учитывать пептиды, уже приложившиеся к предполагаемой последовательности
            if (entry.getValue().getProteinNamesWhereMayOccurrencePeptide().contains(probSeqName)) {
                continue;
            }
            if (entry.getValue().getNumOfRecordsInTSV() >= minimumNumberOfOccurrences) {
                res.add(entry.getValue());
            }
        }
        // сортировка пептидов в порядке убывания их частоты появления в tsv-файлах
        res.sort(new Comparator<Peptide>() {

            @Override
            public int compare(Peptide o1, Peptide o2) {
                return o2.getNumOfRecordsInTSV() - o1.getNumOfRecordsInTSV();
            }
        });
        return res;
    }
    
    
}
