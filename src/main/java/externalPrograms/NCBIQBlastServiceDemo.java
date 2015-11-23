/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package externalPrograms;

/**
 *
 * @author pavelgulaev
 */
import java.io.*;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.*;
 
public class NCBIQBlastServiceDemo {
 
	private static final String BLAST_OUTPUT_FILE = "blastOutput.fasta";    // file to save blast results to
	private static final String SEQUENCE = "QVQLQQSAAELARPGASVKMSCKASGYTFTSYTMHWVKQRPGQGLEWIGYINPSSGYTEYNQKFKDKTTLTADKSSSTAYMQLSSLTSEDSAVYYCARCARAPQFYYYGSRYFDVWGAGTTVTVSSAKTTPPSVYPLAPGSAAQTNSMVTLGCLVKGYFPEPVTVTWNSGSLSSGVHTFPAVLQSDLYTLSSSVTVPSSTWPSQTVTCNVAHPASSTKVDKKIVPRDCGCKPCICTVPEVSSVFIFPPKPKDVLTITLTPKVTCVVVDISKDDPEVQFSWFVDDVEVHTAQTKPREEQINSTFRSVSELPIMHQDWLNGKEFKCRVNSAAFPAPIEKTISKTKGRPKAPQVYTIPPPKEQMAKDKVSLTCMITNFFPEDITVEWQWNGQPAENYKNTQPIMDTDGSYFVYSKLNVQKSNWEAGNTFTCSVLHEGLHNHHTEKSLSHSPGK";     // Blast query sequence
 
	public static void main(String[] args) {
		NCBIQBlastService service = new NCBIQBlastService();
 
		// set alignment options
		NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
		props.setBlastProgram(BlastProgramEnum.blastp);
		props.setBlastDatabase("nr");
//		props.setAlignmentOption(ENTREZ_QUERY, "\"serum albumin\"[Protein name] AND mammals[Organism]");
 
		// set output options
		NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
		// in this example we use default values set by constructor (XML format, pairwise alignment, 100 descriptions and alignments) 
//                outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
//                outputProps.setOutputOption(BlastOutputParameterEnum., SEQUENCE);
		// Example of two possible ways of setting output options
//		outputProps.setAlignmentNumber(200);
//		outputProps.setOutputOption(BlastOutputParameterEnum.ALIGNMENTS, "200");
 
		String rid = null;          // blast request ID
		FileWriter writer = null;
		BufferedReader reader = null;
		try {
			// send blast request and save request id
			rid = service.sendAlignmentRequest(SEQUENCE, props);
 
			// wait until results become available. Alternatively, one can do other computations/send other alignment requests
			while (!service.isReady(rid)) {
				System.out.println("Waiting for results. Sleeping for 5 seconds");
				Thread.sleep(5000);
			}
 
			// read results when they are ready
			InputStream in = service.getAlignmentResults(rid, outputProps);
			reader = new BufferedReader(new InputStreamReader(in));
 
			// write blast output to specified file
			File f = new File(BLAST_OUTPUT_FILE);
			System.out.println("Saving query results in file " + f.getAbsolutePath());
			writer = new FileWriter(f);
 
			String line;
			while ((line = reader.readLine()) != null) {
                            // можно сразу записывать всё в класс базы, а не в файл
                            if (line.contains("<Hit_id>") || line.contains("<Hsp_hseq>")) {
                                line = line.trim().replaceAll("<Hit_id|</Hit_id>|<Hsp_hseq>|</Hsp_hseq>", "");
				writer.write(line + System.getProperty("line.separator"));
                            }
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// clean up
			IOUtils.close(writer);
			IOUtils.close(reader);
 
			// delete given alignment results from blast server (optional operation)
			service.sendDeleteRequest(rid);
		}
	}
}
