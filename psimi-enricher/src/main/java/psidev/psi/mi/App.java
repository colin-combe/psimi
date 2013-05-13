package psidev.psi.mi;

import psidev.psi.mi.DEPRECATEDquery.uniprotbridge.UniprotEnricher;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args){
        UniprotEnricher u = new UniprotEnricher();
        UniProtEntry e = u.getEntryByID("P11163");
        System.out.println(e.getUniProtId());
        System.out.println(e.getNcbiTaxonomyIds());
        System.out.println(e.getProteinDescription());
         /*
        tester t = new tester();

        t.testCVTerm(CvTermFactory.createMODCvTerm("bob","GO:0071840"));

        t.testCVTerm(CvTermFactory.createMODCvTerm("dehydromethionine","MOD:01906"));
        t.testCVTerm(CvTermFactory.createMODCvTerm("dehydromethionine",null));
        t.testCVTerm(CvTermFactory.createMODCvTerm("dehydromethion",null));

        t.testCVTerm(CvTermFactory.createMODCvTerm("stuff","MOD:00698"));


        t.testCVTerm(CvTermFactory.createMICvTerm("allosteric change in dynamics","MOD:01906"));
        t.testCVTerm(CvTermFactory.createMODCvTerm("allosteric change in dynamics","MI:1166"));

        t.testCVTerm(CvTermFactory.createMICvTerm("allosteric change in dynamics","MI:1166"));
        t.testCVTerm(CvTermFactory.createMICvTerm("allosteric change in dynamics",null));
        t.testCVTerm(CvTermFactory.createMICvTerm("allosteric change in dynam",null));
        t.testCVTerm(CvTermFactory.createMICvTerm("osteric change in dynam",null));
        t.testCVTerm(CvTermFactory.createMICvTerm("allosteric",null));
        t.testCVTerm(CvTermFactory.createMICvTerm("0915", "MI:0915"));
        t.testCVTerm(CvTermFactory.createMICvTerm("0915", null));
           */


    }


}