package psidev.psi.mi.jami.xml.model.xml25;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.Complex;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.xml.model.AbstractEntry;
import psidev.psi.mi.jami.xml.model.extension.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Complex Entry implementation for JAXB read only.
 *
 * It does not take into account all experimental details
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>07/11/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ComplexEntry extends AbstractEntry<Complex> {
    @XmlLocation
    @XmlTransient
    private Locator locator;
    private JAXBExperimentsWrapper experimentsWrapper;

    public List<Experiment> getExperiments(){
        return this.experimentsWrapper != null ? this.experimentsWrapper.experiments : Collections.EMPTY_LIST;
    }

    @XmlElement(name = "source", type = XmlSource.class)
    public void setJAXBSource(ExtendedPsiXmlSource source) {
        super.setSource(source);
    }

    @XmlElement(name = "experimentList")
    public void setJAXBExperimentWrapper(JAXBExperimentsWrapper wrapper){
        this.experimentsWrapper = wrapper;
    }

    @XmlElement(name = "interactorList")
    public void setJAXBInteractorsWrapper(JAXBInteractorsWrapper wrapper){
        super.setInteractorsWrapper(wrapper);
    }

    @XmlElement(name = "interactionList", required = true)
    public void setJAXBInteractionsWrapper(JAXBInteractionsWrapper wrapper){
        super.setInteractionsWrapper(wrapper);
    }

    @XmlElement(name = "annotationList")
    public void setJAXBAnnotationWrapper(JAXBAnnotationsWrapper wrapper) {
        super.setAnnotationsWrapper(wrapper);
    }

    @Override
    public FileSourceLocator getSourceLocator() {
        if (super.getSourceLocator() == null && locator != null){
            super.setSourceLocator(new PsiXmlLocator(locator.getLineNumber(), locator.getColumnNumber(), null));
        }
        return super.getSourceLocator();
    }

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            super.setSourceLocator(null);
        }
        else{
            super.setSourceLocator(new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null));
        }
    }

    //////////////////////////////// class wrapper
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="complexExperimentsWrapper")
    public static class JAXBExperimentsWrapper implements Locatable, FileSourceContext {
        private List<Experiment> experiments;
        private PsiXmlLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;

        public JAXBExperimentsWrapper(){
            initialiseExperiments();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmlLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        protected void initialiseExperiments(){
            experiments = new ArrayList<Experiment>();
        }

        @XmlElement(type=XmlExperiment.class, name="experimentDescription", required = true)
        public List<Experiment> getJAXBExperiments() {
            return experiments;
        }

        @Override
        public String toString() {
            return "Entry experiment List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="complexInteractionsWrapper")
    public static class JAXBInteractionsWrapper extends AbstractEntry.JAXBInteractionsWrapper<Complex>{

        public JAXBInteractionsWrapper(){
            super();
        }

        @XmlElement(type=XmlComplex.class, name="interaction", required = true)
        public List<Complex> getJAXBInteractions() {
            return super.getJAXBInteractions();
        }
    }
}