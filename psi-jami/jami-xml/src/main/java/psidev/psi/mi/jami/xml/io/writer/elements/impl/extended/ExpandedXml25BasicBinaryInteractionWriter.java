package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import org.codehaus.stax2.XMLStreamWriter2;
import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.PsiXml25ObjectCache;
import psidev.psi.mi.jami.xml.extension.InferredInteraction;
import psidev.psi.mi.jami.xml.io.writer.elements.CompactPsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.ExpandedPsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25XrefWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.expanded.ExpandedXml25ParticipantWriter;

import javax.xml.stream.XMLStreamException;

/**
 * Expanded XML 2.5 writer for a basic binary interaction (ignore experimental details).
 * Interactions are named interactions with a fullname and aliases in addition to a shortname
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15/11/13</pre>
 */

public class ExpandedXml25BasicBinaryInteractionWriter extends AbstractXml25InteractionWriter<BinaryInteraction,Participant> implements CompactPsiXml25ElementWriter<BinaryInteraction> {

    public ExpandedXml25BasicBinaryInteractionWriter(XMLStreamWriter2 writer, PsiXml25ObjectCache objectIndex) {
        super(writer, objectIndex, new ExpandedXml25ParticipantWriter(writer, objectIndex));
    }

    public ExpandedXml25BasicBinaryInteractionWriter(XMLStreamWriter2 writer, PsiXml25ObjectCache objectIndex,
                                                     PsiXml25XrefWriter primaryRefWriter, PsiXml25XrefWriter secondaryRefWriter,
                                                     ExpandedPsiXml25ElementWriter<Participant> participantWriter, PsiXml25ElementWriter<CvTerm> interactionTypeWriter,
                                                     PsiXml25ElementWriter<Annotation> attributeWriter, PsiXml25ElementWriter<Experiment> experimentWriter, PsiXml25ElementWriter<Alias> aliasWriter,
                                                     PsiXml25ElementWriter<InferredInteraction> inferredInteractionWriter) {
        super(writer, objectIndex, primaryRefWriter, secondaryRefWriter,
                participantWriter != null ? participantWriter : new ExpandedXml25ParticipantWriter(writer, objectIndex), interactionTypeWriter, attributeWriter, experimentWriter, aliasWriter,
                inferredInteractionWriter);
    }

    @Override
    protected void writeAvailability(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeExperiments(BinaryInteraction object) throws XMLStreamException {
        writeExperimentDescription();
    }

    @Override
    protected void writeOtherAttributes(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeModelled(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeParameters(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeConfidences(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeNegative(BinaryInteraction object) {
        // nothing to do
    }
}
