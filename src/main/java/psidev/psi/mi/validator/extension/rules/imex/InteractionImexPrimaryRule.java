package psidev.psi.mi.validator.extension.rules.imex;

import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.validator.extension.Mi25Context;
import psidev.psi.mi.validator.extension.Mi25InteractionRule;
import psidev.psi.mi.validator.extension.rules.PublicationRuleUtils;
import psidev.psi.mi.validator.extension.rules.RuleUtils;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.validator.MessageLevel;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Checks that each interaction has a IMEx ID
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>12-Jan-2011</pre>
 */
public class InteractionImexPrimaryRule extends Mi25InteractionRule{

    public InteractionImexPrimaryRule( OntologyManager ontologyMaganer ) {
        super( ontologyMaganer );

        // describe the rule.
        setName( "Interaction Imex-primary cross reference check" );
        setDescription( "Checks that each interaction has a at least one cross reference 'imex-primary' and that all the imex" +
                "IDs are correct." );
        addTip( "All interactions should have an IMEx ID (IM-xxx-xxx) when there is a cross reference type: imex-primary" );
        addTip( "The PSI-MI identifier for imex-primary is: MI:0662" );
    }

    /**
     * Make sure that an interaction has a valid IMEX id in its xref.
     *
     * @param interaction an interaction to check on.
     * @return a collection of validator messages.
     */
    public Collection<ValidatorMessage> check( InteractionEvidence interaction ) throws ValidatorException {

        // list of messages to return
        List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();

        Mi25Context context = RuleUtils.buildContext(interaction, "interaction");

        // Check xRef
        if (interaction.getImexId() != null){

            PublicationRuleUtils.checkImexInteractionId(interaction.getImexId(), messages, context, this);
        }
        else {
            messages.add( new ValidatorMessage( "The interaction does not have a imex primary cross references. A cross reference with a reference type set" +
                    " to 'imex-primary' (MI:0662) is recommended for IMEx.",
                    MessageLevel.WARN,
                    context,
                    this ) );
        }

        return messages;
    }

    public String getId() {
        return "R35";
    }
}
