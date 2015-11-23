/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package externalPrograms;

import java.io.IOException;
import java.util.HashMap;
/**
 *
 * @author pavelgulaev
 */
import com.mycompany.antibodieswithmaven.UserConstants;
import static com.mycompany.antibodieswithmaven.UserConstants.*;
public class MSGF {
    
    
    public static void runMSGF(String newDbDir) {
//        String[] msgfplusCommands = new String[12];
        String[] msgfplusCommands = new String[]{"java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_Tryp.mzXML -e 1 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "Tryp.mzid",
            "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "Tryp.mzid", 
            "java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_LysC.mzXML -e 3 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "LysC.mzid", "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "LysC.mzid", "java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_GluC.mzXML -e 5 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "GluC.mzid", "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "GluC.mzid", "java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_Chymo.mzXML -e 2 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "Chymo.mzid", "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "Chymo.mzid", "java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_AspN.mzXML -e 7 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "AspN.mzid", "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "AspN.mzid", "java -Xmx3500M -jar " + MSGFPlus + " -s " + spectraAddress + "tk120504_IgG_H_ArgC.mzXML -e 6 -d " + newDbDir + " -o " + mzidsAnsTsvDirectory + "ArgC.mzid", "java -Xmx16G -cp " + MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv -i " + mzidsAnsTsvDirectory + "ArgC.mzid"};
//        for (int i = 0; i < 6; i++) {
//            String enzyme = UserConstants.enzymeNames[i];
//            msgfplusCommands[2 * i] = "java -Xmx3500M -jar " + UserConstants.MSGFPlus + 
//                    " -s " + UserConstants.spectraAddress + "tk120504_IgG_H_" + enzyme + ".mzXML " +
//                    "-e " + UserConstants.EnzymeToNumber.get(enzyme) + 
//                    " -d " + newDbDir + 
//                    " -o " + UserConstants.mzidsAnsTsvDirectory + enzyme + ".mzid";
//            msgfplusCommands[2 * i + 1] = "java -Xmx16G -cp " + UserConstants.MSGFPlus + " edu.ucsd.msjava.ui.MzIDToTsv"
//                    + " -i " + UserConstants.mzidsAnsTsvDirectory + enzyme + ".mzid";
//        }
        Runtime rt = Runtime.getRuntime();

        try {
            for (int i = 0; i < msgfplusCommands.length; i++) {
                rt.exec(msgfplusCommands[i]).waitFor();
                System.out.println("get " + i);
            }
        } catch (IOException iOException) {
            System.out.println("Problems with reading MS/GF");
            iOException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            System.out.println("Problems with running MS/GF");
            interruptedException.printStackTrace();
        }
    }
}
