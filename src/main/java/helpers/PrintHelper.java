/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;
import structures.ProbSeq;
/**
 *
 * @author pavelgulaev
 */
public class PrintHelper {
    public static void printProbSeqWithCover(ProbSeq ps) {
        int stringLength = 30;
        for (int j = 0; j < ps.sequence.length(); j += 20) {
            for (int i = j; i < Math.min(j + stringLength, ps.sequence.length()); i++) {
                System.out.printf("   %s", ps.sequence.charAt(i));
            }
            System.out.println("");
            for (int i = j; i < Math.min(j + stringLength, ps.sequence.length()-1); i++) {
                System.out.printf("%4d", ps.cover[i]);
            }
            System.out.println("");
            for (int i = j; i < Math.min(j + stringLength, ps.sequence.length()-1); i++) {
                System.out.printf("%4d", i + 1);
            }
            System.out.println("");
            System.out.println("");
        }
    }
}
