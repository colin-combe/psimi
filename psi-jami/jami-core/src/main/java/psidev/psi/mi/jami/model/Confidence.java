package psidev.psi.mi.jami.model;

/**
 * A Confidence gives information about how reliable an object is.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>22/11/12</pre>
 */

public interface Confidence {

    public static final String AUTHOR_BASED_CONFIDENCE = "author-based confidence";
    public static final String AUTHOR_BASED_CONFIDENCE_MI = "MI:1221";
    /**
     * Method used to compute the confidence value.
     * The confidence type is a controlled vocabulary term and it cannot be null.
     * Ex: author-based confidence, statistical-based confidence, ...
     * @return the confidence type
     */
    public CvTerm getType();

    /**
     * The confidence value cannot be null. It can be a numerical or literal value
     * Ex: 'high', 'low', 0.4, ...
     * @return the confidence value
     */
    public String getValue();
}
