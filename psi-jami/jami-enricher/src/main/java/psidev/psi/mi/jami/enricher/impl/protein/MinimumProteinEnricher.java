package psidev.psi.mi.jami.enricher.impl.protein;


import psidev.psi.mi.jami.bridges.exception.BadResultException;
import psidev.psi.mi.jami.bridges.exception.BadSearchTermException;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.ProteinFetcher;
import psidev.psi.mi.jami.enricher.OrganismEnricher;
import psidev.psi.mi.jami.enricher.ProteinEnricher;
import psidev.psi.mi.jami.enricher.impl.organism.MinimumOrganismEnricher;
import psidev.psi.mi.jami.enricher.exception.BadToEnrichFormException;
import psidev.psi.mi.jami.enricher.exception.MissingServiceException;
import psidev.psi.mi.jami.enricher.impl.protein.listener.ProteinEnricherListener;
import psidev.psi.mi.jami.model.Protein;
import uk.ac.ebi.intact.irefindex.seguid.SeguidException;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Gabriel Aldam (galdam@ebi.ac.uk)
 * Date: 14/05/13
 * Time: 14:27
 */
public class MinimumProteinEnricher
        extends AbstractProteinEnricher
        implements ProteinEnricher {

    protected ProteinEnricherListener listener = null;
    protected OrganismEnricher organismEnricher;


    public MinimumProteinEnricher(){
        super();
    }

    public MinimumProteinEnricher(ProteinFetcher fetcher){
        super(fetcher);
    }

    public void enrichProtein(Protein proteinToEnrich)
            throws BridgeFailedException,
            MissingServiceException,
            BadToEnrichFormException,
            BadSearchTermException,
            BadResultException,
            SeguidException {

        Collection<Protein> proteinsEnriched = getFullyEnrichedForms(proteinToEnrich);
        Protein proteinEnriched = chooseProteinEnriched(proteinToEnrich, proteinsEnriched);

        if(proteinEnriched != null){
            super.setOrganismEnricher(new MinimumOrganismEnricher());
            runAdditionOnCore(proteinToEnrich, proteinEnriched);
            runAdditionOnChecksum(proteinToEnrich, proteinEnriched);
            this.listener.onProteinEnriched(proteinToEnrich, "Success");
        }
    }

    public OrganismEnricher getOrganismEnricher() {
        return organismEnricher;
    }
}