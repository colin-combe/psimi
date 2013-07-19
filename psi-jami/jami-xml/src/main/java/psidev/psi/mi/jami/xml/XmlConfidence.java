package psidev.psi.mi.jami.xml;

import psidev.psi.mi.jami.model.Confidence;
import psidev.psi.mi.jami.model.ModelledConfidence;
import psidev.psi.mi.jami.model.Publication;
import psidev.psi.mi.jami.utils.comparator.confidence.UnambiguousConfidenceComparator;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Xml implementation of confidence
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19/07/13</pre>
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "confidenceType", propOrder = {
        "type",
        "value"
})
public class XmlConfidence implements Confidence, ModelledConfidence, Serializable{

    private XmlOpenCvTerm type;
    private String value;
    private Collection<Integer> experimentRefList;
    private Collection<Publication> publications;

    public XmlConfidence() {
    }

    public XmlConfidence(XmlOpenCvTerm type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link XmlOpenCvTerm }
     *
     */
    @XmlElement(name = "unit", required = true)
    public XmlOpenCvTerm getType() {
        if (this.type == null){
            this.type = new XmlOpenCvTerm();
        }
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlOpenCvTerm }
     *
     */
    public void setType(XmlOpenCvTerm value) {
        this.type = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @XmlElement(required = true)
    public String getValue() {
        if (value == null){
            value = PsiXmlUtils.UNSPECIFIED;
        }
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the experimentRefList property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    @XmlElementWrapper(name="experimentRefList")
    @XmlElement(name="experimentRef")
    public Collection<Integer> getExperimentRefList() {
        return experimentRefList;
    }

    /**
     * Sets the value of the experimentRefList property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setExperimentRefList(Collection<Integer> value) {
        this.experimentRefList = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (!(o instanceof Confidence)){
            return false;
        }

        return UnambiguousConfidenceComparator.areEquals(this, (Confidence) o);
    }

    @Override
    public String toString() {
        return getType().toString() + ": " + getValue();
    }

    @Override
    public int hashCode() {
        return UnambiguousConfidenceComparator.hashCode(this);
    }

    @XmlTransient
    public Collection<Publication> getPublications() {
        if (publications == null){
           publications = new ArrayList<Publication>();
        }
        return publications;
    }
}
