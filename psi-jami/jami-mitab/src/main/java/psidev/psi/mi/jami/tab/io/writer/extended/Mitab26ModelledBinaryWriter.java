package psidev.psi.mi.jami.tab.io.writer.extended;

import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.model.ModelledParticipant;
import psidev.psi.mi.jami.tab.io.writer.AbstractMitab26BinaryWriter;
import psidev.psi.mi.jami.tab.io.writer.feeder.extended.ExtendedMitabModelledInteractionFeeder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Mitab 2.6 writer for modelled binary interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>20/06/13</pre>
 */

public class Mitab26ModelledBinaryWriter extends AbstractMitab26BinaryWriter<ModelledBinaryInteraction, ModelledParticipant> {

    public Mitab26ModelledBinaryWriter() {
        super();
    }

    public Mitab26ModelledBinaryWriter(File file) throws IOException {
        super(file);
    }

    public Mitab26ModelledBinaryWriter(OutputStream output) {
        super(output);
    }

    public Mitab26ModelledBinaryWriter(Writer writer) {
        super(writer);
    }

    @Override
    protected void initialiseColumnFeeder() {
        setColumnFeeder(new ExtendedMitabModelledInteractionFeeder(getWriter()));
    }
}
