package psidev.psi.mi.jami.bridges.fetcher.mockfetcher.cvterm;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.CvTermFetcher;
import psidev.psi.mi.jami.model.CvTerm;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 01/07/13
 */
public class MockCvTermFetcher
        implements CvTermFetcher{


    private Map<String,CvTerm> localCvTerms;

    public MockCvTermFetcher(){
        localCvTerms = new HashMap<String, CvTerm>();
    }

    public void addCvTerm(String identifier , CvTerm cvTerm){
        if(cvTerm == null || identifier == null) return;
        this.localCvTerms.put(identifier , cvTerm);
    }

    private CvTerm getMockTermById(String identifier){
        if(! localCvTerms.containsKey(identifier))  return null;
        else return localCvTerms.get(identifier);
    }



    public CvTerm getCvTermByIdentifier(String termIdentifier, String ontologyDatabaseName) throws BridgeFailedException {
        return getMockTermById(termIdentifier);
    }

    public CvTerm getCvTermByIdentifier(String termIdentifier, CvTerm ontologyDatabase) throws BridgeFailedException {
        return getMockTermById(termIdentifier);
    }

    public CvTerm getCvTermByExactName(String searchName, String ontologyDatabaseName) throws BridgeFailedException {
        return getMockTermById(searchName);
    }

    public CvTerm getCvTermByExactName(String searchName) throws BridgeFailedException {
        return getMockTermById(searchName);
    }

    public Collection<CvTerm> getCvTermByInexactName(String searchName, String databaseName)
            throws BridgeFailedException {
        ArrayList<CvTerm> list = new ArrayList<CvTerm>();
        list.add(getMockTermById(searchName));
        return list;
    }

    public Collection<CvTerm> getCvTermByInexactName(String searchName, CvTerm database) throws BridgeFailedException {
        ArrayList<CvTerm> list = new ArrayList<CvTerm>();
        list.add(getMockTermById(searchName));
        return list;
    }

    public Collection<CvTerm> getCvTermsByIdentifiersWithOntologyNames(HashMap<String, String> identifiers) throws BridgeFailedException {
        return Collections.EMPTY_LIST;
    }

    public Collection<CvTerm> getCvTermsByIdentifiersWithOntologyCvTerms(HashMap<String, CvTerm> identifiers) throws BridgeFailedException {
        return Collections.EMPTY_LIST;
    }

    public Collection<CvTerm> getCvTermsByExactNames(HashMap<String, String> termNames) throws BridgeFailedException {
        return Collections.EMPTY_LIST;
    }

    public Collection<CvTerm> getCvTermsByExactNames(Collection<String> searchNames) throws BridgeFailedException {
        return Collections.EMPTY_LIST;
    }
}