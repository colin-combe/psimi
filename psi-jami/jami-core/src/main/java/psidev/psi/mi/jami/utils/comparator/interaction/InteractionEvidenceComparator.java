package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.CollectionComparator;
import psidev.psi.mi.jami.utils.comparator.experiment.ExperimentComparator;
import psidev.psi.mi.jami.utils.comparator.experiment.VariableParameterValueSetCollectionComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.ParameterCollectionComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.ParameterComparator;
import psidev.psi.mi.jami.utils.comparator.participant.ParticipantCollectionComparator;

import java.util.Collection;
import java.util.Comparator;

/**
 * Basic InteractionEvidenceComparator.
 *
 * It will first compare the basic interaction properties using Comparator<Interaction>.
 * It will then compares the IMEx identifiers if both IMEx ids are set. If at least one IMEx id is not set, it will compare the negative properties.
 * A negative interaction will come after a positive interaction. it will compare
 * the experiment using ExperimentComparator. If the experiments are the same, it will compare the participants using ParticipantEvidenceComparator. Then it will compare the parameters using ParameterComparator.
 * If the parameters are the same, it will first compare the experimental variableParameters using VariableParameterValueSetComparator and then it will compare the inferred boolean value (Inferred interactions will always come after).
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/01/13</pre>
 */

public class InteractionEvidenceComparator implements Comparator<InteractionEvidence> {

    private Comparator<Interaction> interactionComparator;
    private ExperimentComparator experimentComparator;
    private CollectionComparator<Parameter> parameterCollectionComparator;
    private CollectionComparator<ParticipantEvidence> participantCollectionComparator;
    private CollectionComparator<VariableParameterValueSet> variableParameterValueSetCollectionComparator;

    /**
     * Creates a new InteractionEvidenceComparator.
     * @param interactionComparator : required to compare basic interaction properties
     * @param experimentComparator : required to compare experiments
     * @param parameterComparator : required to compare parameters
     */
    public InteractionEvidenceComparator(Comparator<ParticipantEvidence> participantComparator, Comparator<Interaction> interactionComparator, ExperimentComparator experimentComparator,
                                         ParameterComparator parameterComparator){
        if (interactionComparator == null){
            throw new IllegalArgumentException("The Interaction comparator is required to compare basic interaction properties. It cannot be null");
        }
        this.interactionComparator = interactionComparator;

        if (experimentComparator == null){
            throw new IllegalArgumentException("The Experiment comparator is required to compare experiment where the interaction has been determined. It cannot be null");
        }
        this.experimentComparator = experimentComparator;

        if (parameterComparator == null){
            throw new IllegalArgumentException("The Parameter comparator is required to compare parameters of the interaction. It cannot be null");
        }
        this.parameterCollectionComparator = new ParameterCollectionComparator(parameterComparator);
        if (participantComparator == null){
            throw new IllegalArgumentException("The participant comparator is required to compare participants of an interaction. It cannot be null");
        }
        this.participantCollectionComparator = new ParticipantCollectionComparator<ParticipantEvidence>(participantComparator);
        this.variableParameterValueSetCollectionComparator = new VariableParameterValueSetCollectionComparator();
    }

    public InteractionEvidenceComparator(CollectionComparator<ParticipantEvidence> participantComparator, Comparator<Interaction> interactionComparator,
                                         ExperimentComparator experimentComparator,
                                         CollectionComparator<Parameter> parameterComparator,
                                         CollectionComparator<VariableParameterValueSet> variableParameterValuesSetComparator){
        if (interactionComparator == null){
            throw new IllegalArgumentException("The Interaction comparator is required to compare basic interaction properties. It cannot be null");
        }
        this.interactionComparator = interactionComparator;

        if (experimentComparator == null){
            throw new IllegalArgumentException("The Experiment comparator is required to compare experiment where the interaction has been determined. It cannot be null");
        }
        this.experimentComparator = experimentComparator;

        if (parameterComparator == null){
            throw new IllegalArgumentException("The Parameter comparator is required to compare parameters of the interaction. It cannot be null");
        }
        this.parameterCollectionComparator = parameterComparator;
        if (participantComparator == null){
            throw new IllegalArgumentException("The participant comparator is required to compare participants of an interaction. It cannot be null");
        }
        this.participantCollectionComparator = participantComparator;
        if (variableParameterValuesSetComparator == null){
            throw new IllegalArgumentException("The variable values set comparator is required to compare variable parameters values of an interaction. It cannot be null");
        }
        this.variableParameterValueSetCollectionComparator = variableParameterValuesSetComparator;
    }

    public CollectionComparator<Parameter> getParameterCollectionComparator() {
        return parameterCollectionComparator;
    }

    public Comparator<Interaction> getInteractionBaseComparator() {
        return interactionComparator;
    }

    public ExperimentComparator getExperimentComparator() {
        return experimentComparator;
    }

    public CollectionComparator<ParticipantEvidence> getParticipantCollectionComparator() {
        return participantCollectionComparator;
    }

    public CollectionComparator<VariableParameterValueSet> getVariableParameterValueSetCollectionComparator() {
        return variableParameterValueSetCollectionComparator;
    }

    /**
     * It will first compare the basic interaction properties using Comparator<Interaction>.
     * It will then compares the IMEx identifiers if both IMEx ids are set. If at least one IMEx id is not set, it will compare the negative properties.
     * A negative interaction will come after a positive interaction. it will compare
     * the experiment using ExperimentComparator. If the experiments are the same, it will compare the participants using ParticipantEvidenceComparator. Then, it will compare the parameters using ParameterComparator.
     * If the parameters are the same, it will first compare the experimental variableParameters using VariableParameterValueSetComparator and then it will compare the inferred boolean value (Inferred interactions will always come after).
     * @param experimentalInteraction1
     * @param experimentalInteraction2
     * @return
     */
    public int compare(InteractionEvidence experimentalInteraction1, InteractionEvidence experimentalInteraction2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (experimentalInteraction1 == experimentalInteraction2){
            return 0;
        }
        else if (experimentalInteraction1 == null){
            return AFTER;
        }
        else if (experimentalInteraction2 == null){
            return BEFORE;
        }
        else {
            int comp = interactionComparator.compare(experimentalInteraction1, experimentalInteraction2);
            if (comp != 0){
                return comp;
            }

            // first compares the IMEx id
            String imex1 = experimentalInteraction1.getImexId();
            String imex2 = experimentalInteraction2.getImexId();

            if (imex1 != null && imex2 != null){
                return imex1.compareTo(imex2);
            }

            // then compares negative
            boolean isNegative1 = experimentalInteraction1.isNegative();
            boolean  isNegative2 = experimentalInteraction2.isNegative();

            if (isNegative1 && !isNegative2){
                return AFTER;
            }
            else if (isNegative2 && !isNegative1){
                return BEFORE;
            }

            // If at least one IMEx id is not set, will compare the experiment
            Experiment exp1 = experimentalInteraction1.getExperiment();
            Experiment exp2 = experimentalInteraction2.getExperiment();

            comp = experimentComparator.compare(exp1, exp2);
            if (comp != 0){
                return comp;
            }

            // first compares participants of an interaction
            Collection<ParticipantEvidence> participants1 = experimentalInteraction1.getParticipants();
            Collection<ParticipantEvidence> participants2 = experimentalInteraction2.getParticipants();

            comp = participantCollectionComparator.compare(participants1, participants2);
            if (comp != 0){
                return comp;
            }

            // If experiments are the same, compare parameters
            Collection<Parameter> parameters1 = experimentalInteraction1.getParameters();
            Collection<Parameter> parameters2 = experimentalInteraction2.getParameters();

            comp = parameterCollectionComparator.compare(parameters1, parameters2);
            if (comp != 0){
                return comp;
            }

            // if parameters are the same, check experimental parameters
            Collection<VariableParameterValueSet> parameterSet1 = experimentalInteraction1.getVariableParameterValues();
            Collection<VariableParameterValueSet> parameterSet2 = experimentalInteraction2.getVariableParameterValues();

            comp = variableParameterValueSetCollectionComparator.compare(parameterSet1, parameterSet2);
            if (comp != 0){
                return comp;
            }

            // check if inferred
            boolean inferred1 = experimentalInteraction1.isInferred();
            boolean inferred2 = experimentalInteraction2.isInferred();

            if (inferred1 == inferred2){
                return EQUAL;
            }
            else if (inferred1){
                return AFTER;
            }
            else if (inferred2){
                return BEFORE;
            }
            else {
                return EQUAL;
            }
        }
    }
}
