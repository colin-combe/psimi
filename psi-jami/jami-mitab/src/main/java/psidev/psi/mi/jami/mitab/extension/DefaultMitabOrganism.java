package psidev.psi.mi.jami.mitab.extension;

import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.impl.DefaultOrganism;

/**
 * Mitab extension of Organism.
 * It contains a FileSourceLocator
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/06/13</pre>
 */

public class DefaultMitabOrganism extends DefaultOrganism implements FileSourceContext{

    private MitabSourceLocator sourceLocator;

    public DefaultMitabOrganism(int taxId) {
        super(taxId);
    }

    public DefaultMitabOrganism(int taxId, String commonName) {
        super(taxId, commonName);
    }

    public DefaultMitabOrganism(int taxId, String commonName, String scientificName) {
        super(taxId, commonName, scientificName);
    }

    public DefaultMitabOrganism(int taxId, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        super(taxId, cellType, tissue, compartment);
    }

    public DefaultMitabOrganism(int taxId, String commonName, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        super(taxId, commonName, cellType, tissue, compartment);
    }

    public DefaultMitabOrganism(int taxId, String commonName, String scientificName, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        super(taxId, commonName, scientificName, cellType, tissue, compartment);
    }

    public MitabSourceLocator getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(MitabSourceLocator sourceLocator) {
        this.sourceLocator = sourceLocator;
    }
}