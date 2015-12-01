/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.antibodieswithmaven;

import static com.mycompany.antibodieswithmaven.UserConstants.*;
import externalPrograms.MSGF;
import helpers.PrintHelper;
import java.util.HashMap;
import structures.Tsv;
import structures.BaseFile;
import structures.ProbSeq;
import structures.Peptide;
import structures.PepCoordinates;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author pavel
 */
public class Antibodies {
    static Tsv tsv;
    public static void main(String[] args) throws InterruptedException {
//        String fileNameOfProbSeq = "/Users/pavelgulaev/Bioinformatics/Antibodies/HC_genoms.fasta";

        BaseFile db = new BaseFile(dbFile);
        tsv = Tsv.createTSV_FromDir(tsvDirectory);
        // посчитал покрытие и записал его в базу
        db.getPs().cover = tsv.makeCoverage(db.getPs());
        // новая изменяемая последовательность
        ProbSeq probSeq = db.getPs().clone();
        LinkedList<Peptide> peptidesAll = tsv.getAllPeptides();
        
        
        probSeq.coverageByPeptides = db.addCoordinatesToPeptidesObjects(peptidesAll);
        LinkedList<Peptide> peptides = Methods.getOnlyUnCoveredPeptides(peptidesAll, probSeq, 1);
        
        
        PrintHelper.printProbSeqWithCover(probSeq);
        System.out.println("");
        System.out.println("Old and new replaced peptides:");
        // создание новой последовательности, возвращает изменённые пептиды и их координаты
        HashMap<String, Peptide> replacingPeptides = ReadApplication.iterativeApplication(probSeq, peptides, tsv);
//        probSeq.sequence.replace(363, 395, "DKVSLTCMITDFFPEDITVEWQWNGQPAENYK");
        // создаю новую базу с изменённой предполагаемой последовательностью,
        // в будущем будут меняться и бласт данные
        LinkedHashMap<String, String> newDbData = (LinkedHashMap<String, String>) db.getData().clone();
        newDbData.replace(probSeq.probSeqName, probSeq.sequence.toString());
        BaseFile newDb = new BaseFile(new ProbSeq(probSeq.probSeqName, probSeq.sequence.toString()), newDbData);
        newDb.saveDbToFile(newDbFile);

//        запуск MSGF
        MSGF.runMSGF(newDbFile);
        // засунуть в отдельный метод
        Tsv newTsv = Tsv.createTSV_FromDir(mzidsAnsTsvDirectory);
        newDb.getPs().cover = newTsv.makeCoverage(newDb.getPs());
        PrintHelper.printProbSeqWithCover(newDb.getPs());
        System.out.println("");
        for (Map.Entry<String, Peptide> oldAndNewPep : replacingPeptides.entrySet()) {
            Peptide newPeptide = oldAndNewPep.getValue();
            System.out.println(oldAndNewPep.getKey());
            System.out.println(newPeptide.seq);
            System.out.println(newPeptide.getPepCoords().toString());
            for (int i = newPeptide.getPepCoords().left; i < newPeptide.getPepCoords().right; i++) {
                System.out.print(db.getPs().cover[i] + "-" + newDb.getPs().cover[i] + " ");
            }
            System.out.println("");
        }
    }

}

class ReadApplication {

    static LinkedList<Peptide> peptidesForNextIteration;
//    static StringBuffer changableSeq;
    static int[] constantRegions;

    static {
        constantRegions = new int[10];
        int minConstantRegionSize = 120;
        for (int i = 0; i < 10; i++) {
            constantRegions[i] = minConstantRegionSize + i;
        }
    }

    // возвращает список заменивших пептидов
    public static HashMap<String, Peptide> iterativeApplication(ProbSeq ps,
            LinkedList<Peptide> peptidesToCompare, Tsv tsv) {

        HashMap<String, Peptide> oldPepNewPep = new HashMap<>();
//        peptidesForNextIteration = new LinkedList<>();
        // кол-во итераций пока что никак не повлияло на результат
        for (int i = 0; i < 1; i++) {
            for (Peptide peptide : peptidesToCompare) {
                
                replaceSeqByPeptide(ps, peptide, oldPepNewPep, tsv);
            }
            // следующая итерация
//            peptidesToCompare = peptidesForNextIteration;
//            peptidesForNextIteration = new LinkedList<>();
        }
        return oldPepNewPep;
    }

    private static void replaceSeqByPeptide(ProbSeq ps, Peptide peptide, HashMap<String, Peptide> oldPepNewPep,
            Tsv tsv) {
        PepCoordinates[] coord = peptide.getOccurrencesInBigSeq().toArray(new PepCoordinates[0]);
//        PepCoordinates coord = peptide.getFirstCoordinate();
        int[] offsets;
        int minHamming = Integer.MAX_VALUE;
        int minHammingIndex = -1;
        // minHammingIndexForCoors
        int mHIFC = -1;

        // обход константного региона
        for (int i = 0; i < coord.length; i++) {
            if (coord[i].isConstantRegion) {
                offsets = constantRegions;
            } else {
                offsets = new int[]{0};
                continue;
            }
            for (int j = 0; j < offsets.length; j++) {
                if (offsets[j] + coord[i].right - 1 >= ps.sequence.length()) {
                    break;
                }
                int offset = offsets[j];
                // вычитаю один, потому что отсчёт в последовательности идёт с единицы
                int leftCoord = coord[i].left - 1 + offset;
                int rightCoord = coord[i].right - 1 + offset;

                // проверка на пересечение пептидов
                if (Methods.PeptideCrossAnotherPeptide(leftCoord, rightCoord, ps.bounds)) {
                    continue;
                }
                String regionInPosSeq = regionInPosSeq = ps.sequence.substring(leftCoord, rightCoord);

                int HammingDistance = Methods.HammingDistanceWithCoverage(peptide, ps, leftCoord);
//            int HammingDistance = Methods.SimpleHammingDistance(peptide.seq, regionInPosSeq);
                if ((HammingDistance < minHamming) && (HammingDistance < peptide.seq.length() * 0.3)) {
                    minHamming = HammingDistance;
                    minHammingIndex = offset;
                    mHIFC = i;
                }
            }

        }

        //добавление нового пептида к последовательности
        if (minHammingIndex != -1) {
            String oldSubSeq = ps.sequence.subSequence(coord[mHIFC].left - 1 + minHammingIndex, coord[mHIFC].right - 1 + minHammingIndex).toString();

            if (Methods.shouldIReplaceThePeptide(
                    peptide.seq,
                    new PepCoordinates(coord[mHIFC].left - 1 + minHammingIndex,
                            coord[mHIFC].right - 1 + minHammingIndex),
                    ps, tsv)) {
                System.out.println((coord[mHIFC].left - 1 + minHammingIndex + 1) + " " + (coord[mHIFC].right - 1 + minHammingIndex + 1));
                System.out.println(oldSubSeq);
                System.out.println(peptide.seq);
                for (int i = coord[mHIFC].left - 1 + minHammingIndex; i < coord[mHIFC].right - 1 + minHammingIndex; i++) {
                    ps.bounds[i] = true;
                }
                int startRegionCoordinate = coord[mHIFC].left - 1 + minHammingIndex;
                peptide.setPepCoords(
                        new PepCoordinates(startRegionCoordinate,
                                startRegionCoordinate + peptide.seq.length()));
                oldPepNewPep.put(oldSubSeq, peptide);
            }
//            ps.sequence.replace(coord[mHIFC].left - 1 + minHammingIndex, coord[mHIFC].right - 1 + minHammingIndex, peptide.seq);

//            String curPS = ps.sequence.toString();
//            for (int i = 0; i < peptide.seq.length(); i++) {
//                if (curPS.charAt(i) != peptide.seq.charAt(i)) {
//                    ps.cover[i + startRegionCoordinate] = peptide.getNumOfRecordsInTSV();
//                } else {
//                    ps.cover[i + startRegionCoordinate] += peptide.getNumOfRecordsInTSV();
//                }
//            }
            // В данном случае записываю координаты напрямую в последовательности,
            // не упоминая про константный регион, т.к. эти пептиды связаны 
            // с предполагаемой последовательностью, а не с белками в базе. 
        } else {
//            peptidesForNextIteration.add(peptide);
        }
    }

}
