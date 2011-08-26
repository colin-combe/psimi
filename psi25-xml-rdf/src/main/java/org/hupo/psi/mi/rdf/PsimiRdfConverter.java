/**
 * Copyright 2011 The European Bioinformatics Institute, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hupo.psi.mi.rdf;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.arp.JenaReader;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.mskcc.psibiopax.converter.PSIMIBioPAXConverter;
import psidev.psi.mi.xml.PsimiXmlReader;
import psidev.psi.mi.xml.PsimiXmlReaderException;
import psidev.psi.mi.xml.model.EntrySet;

import java.io.*;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class PsimiRdfConverter {

    public PsimiRdfConverter() {

    }

    public void convert(EntrySet entrySet, RdfFormat format, Writer writer) throws IOException {
        convert(entrySet, format.getName(), writer);
    }

    public void convert(Reader reader, RdfFormat format, Writer writer) throws IOException, PsimiXmlReaderException {
        convert(reader, format.getName(), writer);
    }

    public void convert(EntrySet entrySet, RdfFormat format, OutputStream os) throws IOException {
        convert(entrySet, format.getName(), os);
    }

    public void convert(InputStream is, RdfFormat format, OutputStream os) throws IOException, PsimiXmlReaderException {
        convert(new InputStreamReader(is), format.getName(), new OutputStreamWriter(os));
    }

    public void convert(EntrySet entrySet, String format, OutputStream os) throws IOException {
        convert(entrySet, format, new OutputStreamWriter(os));
    }

    public void convert(InputStream is, String format, Writer writer) throws IOException, PsimiXmlReaderException {
        convert(new InputStreamReader(is), format, writer);
    }

    public void convert(Reader reader, String format, Writer writer) throws IOException, PsimiXmlReaderException {
        PsimiXmlReader xmlReader = new PsimiXmlReader();
        EntrySet entrySet = xmlReader.read(reader);
        convert(entrySet, format, writer);
    }

    public void convert(EntrySet entrySet, String format, Writer writer) throws IOException {

        if (format.startsWith("BioPAX")) {
            BioPAXLevel biopaxLevel;

            if (format.equals(RdfFormat.BIOPAX_L2.getName())) {
                biopaxLevel = BioPAXLevel.L2;
            } else {
                biopaxLevel = BioPAXLevel.L3;
            }

            final String biopaxOutput = convertToBioPAXAndFixURIs(entrySet, biopaxLevel);
            writer.write(biopaxOutput);
        } else {
            final String baseUri = "http://org.hupo.psi.mi";

            OntModel jenaModel = convertToJena(entrySet);

            final RDFWriter rdfWriter = jenaModel.getWriter(format);
            rdfWriter.setProperty("xmlbase", baseUri);
            jenaModel.setNsPrefix("", baseUri);

            rdfWriter.write(jenaModel, writer, baseUri);
        }
    }

    public OntModel convertToJena(EntrySet entrySet) {
        String fixedBiopax = convertToBioPAXAndFixURIs(entrySet, BioPAXLevel.L3);

        return createJenaModel(new StringReader(fixedBiopax), "http://org.hupo.psi.mi");
    }

    public Model convertToBioPAX(EntrySet entrySet, BioPAXLevel biopaxLevel) {
        String fixedBiopax = convertToBioPAXAndFixURIs(entrySet, biopaxLevel);

        return new SimpleIOHandler().convertFromOWL(new ByteArrayInputStream(fixedBiopax.getBytes()));
    }

    private String convertToBioPAXAndFixURIs(EntrySet entrySet, BioPAXLevel biopaxLevel) {
        OutputStream os = new ByteArrayOutputStream();

        PSIMIBioPAXConverter biopaxConverter = new PSIMIBioPAXConverter(biopaxLevel);
        biopaxConverter.convert(entrySet, os);

        String biopaxOutput = os.toString();

        String output = "";

        if (!biopaxOutput.isEmpty()) {
            // fix the biopax non-dereferenciable URIs
            Model model = new SimpleIOHandler().convertFromOWL(new ByteArrayInputStream(biopaxOutput.getBytes()));

            BioPaxUriFixer fixer = new BioPaxUriFixer();
            output = fixer.fixBioPaxUris(model);
        }

        return output;
    }

    private OntModel createJenaModel(Reader reader, String baseUri) {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        final RDFReader rdfReader = new JenaReader();
        rdfReader.read(model, reader, baseUri);
        return model;
    }

}
