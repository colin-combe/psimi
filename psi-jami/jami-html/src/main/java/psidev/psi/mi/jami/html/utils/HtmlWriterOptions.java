package psidev.psi.mi.jami.html.utils;

import psidev.psi.mi.jami.factory.options.InteractionWriterOptions;

/**
 * Class that lists all possible options for MI HTML writer.
 * All the options in InteractionWriterOptions are also valid for MI HTML writer.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>06/12/13</pre>
 */

public class HtmlWriterOptions extends InteractionWriterOptions {

    /**
     * This option is for writing a header and body.
     * It has to be a boolean value.
     * If this option is not provided, it will write a header and body by default.
     */
    public static final String WRITE_HTML_HEADER_BODY_OPTION = "write_html_header_option_key";

    /**
     * The html format value to use with HtmlWriterOptions.OUTPUT_FORMAT_OPTION_KEY
     */
    public static final String MI_HTML_FORMAT = "mi_html";
}
