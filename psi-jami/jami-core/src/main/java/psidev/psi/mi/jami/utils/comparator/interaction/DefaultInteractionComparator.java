package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.Interaction;

/**
 * Default Generic interaction comparator.
 * Experimental interactions come first, then allosteric interactions, then cooperative interactions, then modelled interactions.
 * - It uses DefaultInteractionEvidenceComparator to compare experimental interactions
 * - It uses DefaultModelledInteractionComparator to compare modelled interactions
 * - It uses DefaultCooperativeInteractionComparator to compare cooperative interactions
 * - It uses DefaultAllostericInteractionComparator to compare allosteric interactions
 * - It uses DefaultInteractionBaseComparator to compare basic interaction properties
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>05/02/13</pre>
 */

public class DefaultInteractionComparator extends InteractionComparator {

    private static DefaultInteractionComparator defaultInteractionComparator;

    /**
     * Creates a new DefaultInteractionComparator.
     */
    public DefaultInteractionComparator() {
        super(new DefaultInteractionBaseComparator(), new DefaultModelledInteractionComparator(), new DefaultInteractionEvidenceComparator());
    }

    @Override
    public DefaultInteractionBaseComparator getInteractionBaseComparator() {
        return (DefaultInteractionBaseComparator) this.interactionBaseComparator;
    }

    @Override
    public DefaultInteractionEvidenceComparator getExperimentalInteractionComparator() {
        return (DefaultInteractionEvidenceComparator) this.experimentalInteractionComparator;
    }

    @Override
    /**
     * Experimental interactions come first, then allosteric interactions, then cooperative interactions, then modelled interactions.
     * - It uses DefaultInteractionEvidenceComparator to compare experimental interactions
     * - It uses DefaultModelledInteractionComparator to compare modelled interactions
     * - It uses DefaultCooperativeInteractionComparator to compare cooperative interactions
     * - It uses DefaultAllostericInteractionComparator to compare allosteric interactions
     * - It uses DefaultInteractionBaseComparator to compare basic interaction properties
     */
    public int compare(Interaction interaction1, Interaction interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use DefaultInteractionComparator to know if two interactions are equals.
     * @param interaction1
     * @param interaction2
     * @return true if the two interactions are equal
     */
    public static boolean areEquals(Interaction interaction1, Interaction interaction2){
        if (defaultInteractionComparator == null){
            defaultInteractionComparator = new DefaultInteractionComparator();
        }

        return defaultInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}