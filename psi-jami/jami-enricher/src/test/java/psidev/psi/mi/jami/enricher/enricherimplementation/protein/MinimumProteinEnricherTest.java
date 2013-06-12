package psidev.psi.mi.jami.enricher.enricherimplementation.protein;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.bridges.exception.BadResultException;
import psidev.psi.mi.jami.bridges.exception.BadSearchTermException;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.enricher.enricherimplementation.protein.listener.ProteinEnricherCounter;
import psidev.psi.mi.jami.enricher.exception.BadToEnrichFormException;
import psidev.psi.mi.jami.enricher.exception.MissingServiceException;
import psidev.psi.mi.jami.enricher.mockfetcher.protein.MockProteinFetcher;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultOrganism;
import psidev.psi.mi.jami.model.impl.DefaultProtein;
import psidev.psi.mi.jami.utils.ChecksumUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import uk.ac.ebi.intact.irefindex.seguid.RogidGenerator;
import uk.ac.ebi.intact.irefindex.seguid.SeguidException;

/**
 * Unit tester for MinimumProteinEnricher
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>23/05/13</pre>
 */

public class MinimumProteinEnricherTest {

    private MinimumProteinEnricher minimumProteinEnricher;
    private MockProteinFetcher fetcher;

    private ProteinEnricherCounter counter;


    private static final String TEST_SHORTNAME = "test shortName";
    private static final String TEST_FULLNAME = "test fullName";
    private static final String TEST_AC_FULL_PROT = "P12345";
    private static final String TEST_AC_HALF_PROT = "P11111";
    private static final String TEST_SEQUENCE = "GATTACA";

    @Before
    public void initialiseFetcherAndEnricher(){
        this.fetcher = new MockProteinFetcher();
        this.minimumProteinEnricher = new MinimumProteinEnricher(fetcher);

        Protein fullProtein = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        fullProtein.setUniprotkb(TEST_AC_FULL_PROT);
        fullProtein.setSequence(TEST_SEQUENCE);
        fetcher.addNewProtein(TEST_AC_FULL_PROT, fullProtein);

        Protein halfProtein = new DefaultProtein(TEST_SHORTNAME);
        halfProtein.setUniprotkb(TEST_AC_HALF_PROT);
        fetcher.addNewProtein(TEST_AC_HALF_PROT, halfProtein);

        counter = new ProteinEnricherCounter();
        minimumProteinEnricher.setProteinEnricherListener(counter);
    }

    @Test(expected = BadToEnrichFormException.class)
    public void test_no_fetching_on_null_protein()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein null_protein = null;
        this.minimumProteinEnricher.enrichProtein(null_protein);
    }

    @Test(expected = BadToEnrichFormException.class)
    public void test_no_fetching_on_null_protein_identifier()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein null_identifier_protein = new DefaultProtein("Identifier free protein");
        this.minimumProteinEnricher.enrichProtein(null_identifier_protein);
    }


    @Test
    public void test_ignore_if_interactorType_is_already_protein()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_interactor_type = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_interactor_type.setUniprotkb(TEST_AC_HALF_PROT);

        CvTerm value = CvTermUtils.createProteinInteractorType();

        protein_with_interactor_type.setInteractorType(value);

        minimumProteinEnricher.enrichProtein(protein_with_interactor_type);

        assertEquals(0, counter.getAdded());
        assertTrue(protein_with_interactor_type.getInteractorType() == value); //Show they are the same instance
    }



    @Test
    public void test_interactorType_updated_if_unknown()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_no_interactor_type = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_no_interactor_type.setUniprotkb(TEST_AC_HALF_PROT);
        protein_with_no_interactor_type.setInteractorType(CvTermUtils.createUnknownInteractorType());

        assertEquals(Protein.UNKNOWN_INTERACTOR_MI,
                protein_with_no_interactor_type.getInteractorType().getMIIdentifier());
        assertEquals(Protein.UNKNOWN_INTERACTOR,
                protein_with_no_interactor_type.getInteractorType().getShortName());

        minimumProteinEnricher.enrichProtein(protein_with_no_interactor_type);


        assertEquals(Protein.PROTEIN,
                protein_with_no_interactor_type.getInteractorType().getShortName());
        assertEquals(Protein.PROTEIN_MI,
                protein_with_no_interactor_type.getInteractorType().getMIIdentifier());

        // 1 addition (the protein interactor type)
        assertEquals(1, counter.getAdded());
    }

    //Todo - currently has no test
    @Test
    public void test_interactorType_conflict_if_unexpected_type()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_bad_interactor_type = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_bad_interactor_type.setUniprotkb(TEST_AC_HALF_PROT);
        protein_with_bad_interactor_type.setInteractorType(CvTermUtils.createGeneInteractorType());

        minimumProteinEnricher.enrichProtein(protein_with_bad_interactor_type);

        assertEquals(0, counter.getAdded());
        assertEquals(0, counter.getRemoved());
        assertEquals(0, counter.getUpdated());
        assertTrue(counter.getStatus().contains("Failed"));
    }

    /**
     * Enrich a protein that has no full name.
     * Check the full name has been added
     */
    @Test
    public void test_set_fullName_if_null()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_without_fullName = new DefaultProtein("test2 shortName");
        protein_without_fullName.setUniprotkb(TEST_AC_FULL_PROT);

        this.minimumProteinEnricher.enrichProtein(protein_without_fullName);

        assertNotNull(protein_without_fullName.getFullName());
        assertEquals(TEST_FULLNAME, protein_without_fullName.getFullName());
    }

    //TEST CAN NOT BE IMPLEMENTED WITH CURRENT MOCK.
    //@Test
    //public void test_set_primaryAC_if_null() throws EnrichmentException {
    //
    //    Protein protein_without_AC = new DefaultProtein("test2 shortName");
    //
    //    this.minimumProteinEnricher.enrichProtein(protein_without_AC);
    //
    //    Assert.assertNotNull(protein_without_fullName.getFullName());
    //    Assert.assertEquals("test1 fullName", protein_without_fullName.getFullName());
    //    Assert.assertEquals("test2 shortName", protein_without_fullName.getShortName());
    //}

    /**
     * Enrich a protein with no sequence.
     * Check that the sequence has been added
     */
    @Test
    public void test_set_sequence_if_null()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_without_sequence = new DefaultProtein("test2 shortName");
        protein_without_sequence.setUniprotkb(TEST_AC_FULL_PROT);

        this.minimumProteinEnricher.enrichProtein(protein_without_sequence);

        assertNotNull(protein_without_sequence.getSequence());
        assertEquals(TEST_SEQUENCE, protein_without_sequence.getSequence());
    }

    @Test
    public void test_set_organism_if_null()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_without_organism = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_without_organism.setUniprotkb(TEST_AC_FULL_PROT);
        protein_without_organism.setSequence(TEST_SEQUENCE);


            for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
                protein.setOrganism(new DefaultOrganism(11111,"Common","Scientific"));
            }

        assertNull(protein_without_organism.getOrganism());

        this.minimumProteinEnricher.enrichProtein(protein_without_organism);

        assertNotNull(protein_without_organism.getOrganism());
        assertEquals(11111 , protein_without_organism.getOrganism().getTaxId());
        assertEquals("Common" , protein_without_organism.getOrganism().getCommonName());
        assertEquals("Scientific" , protein_without_organism.getOrganism().getScientificName());
    }

    @Test
    public void test_organism_conflict()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_wrong_organism = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_wrong_organism.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_wrong_organism.setSequence(TEST_SEQUENCE);
        protein_with_wrong_organism.setOrganism(new DefaultOrganism(55555));


            for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
                protein.setOrganism(new DefaultOrganism(11111,"Common","Scientific"));
            }

        assertNotNull(protein_with_wrong_organism.getOrganism());

        this.minimumProteinEnricher.enrichProtein(protein_with_wrong_organism);
        assertEquals(0, counter.getAdded());
        assertEquals(0, counter.getRemoved());
        assertEquals(0, counter.getUpdated());
        assertTrue(counter.getStatus().contains("Failed"));
    }

    /**
     * Enrich an already complete protein with a different but complete protein.
     * This should not change any fields of the original protein.
     */
    @Test
    public void test_no_overwrite_on_not_null_fields()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_all_fields = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_with_all_fields.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_all_fields.setSequence("TAGTAG");

        assertNotNull(protein_with_all_fields.getShortName());
        assertNotNull(protein_with_all_fields.getFullName());
        assertNotNull(protein_with_all_fields.getUniprotkb());
        assertNotNull(protein_with_all_fields.getSequence());

        this.minimumProteinEnricher.enrichProtein(protein_with_all_fields);

        assertEquals( protein_with_all_fields.getShortName(), "test2 shortName");
        assertEquals( protein_with_all_fields.getFullName(), "test2 fullName");
        assertEquals( protein_with_all_fields.getSequence(), "TAGTAG");
    }

    /**
     * Enrich an already complete protein with one which is only half complete.
     * This should not have any additions, nor throw any exceptions.
     */
    @Test
    public void test_mismatch_does_not_happen_on_a_null_enrichedProtein()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_all_fields = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_with_all_fields.setUniprotkb(TEST_AC_HALF_PROT);
        protein_with_all_fields.setSequence("TAGTAG");

        //If this is failing, you may wish to use a logging listener to read the log.
        //this.minimumProteinEnricher.addEnricherListener(new LoggingEnricherListener());

        this.minimumProteinEnricher.enrichProtein(protein_with_all_fields);

        //assertTrue(event.getMismatches().size() == 1);
    }

    //TODO Consider further tests for the passing of an organism to the enricher

    /**
     * If there is no Rogid, add it.
     * @throws SeguidException
     */
    @Test
    public void test_set_rogid_if_null()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_no_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_no_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_no_rogid.setSequence(TEST_SEQUENCE);
        protein_with_no_rogid.setOrganism(new DefaultOrganism(55555));

            for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
                protein.setOrganism(new DefaultOrganism(55555,"Common","Scientific"));
            }


        assertTrue(protein_with_no_rogid.getRogid() == null);
        minimumProteinEnricher.enrichProtein(protein_with_no_rogid);
        assertTrue(protein_with_no_rogid.getRogid() != null);

        RogidGenerator rogidGenerator = new RogidGenerator();
        String rogid = rogidGenerator.calculateRogid(TEST_SEQUENCE,"55555");

        assertEquals(rogid, protein_with_no_rogid.getRogid());
    }

    /**
     * If the supplied rogid matches, ignore it.
     * @throws SeguidException
     */
    @Test
    public void test_ignore_rogid_if_the_same()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_a_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_a_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_a_rogid.setSequence(TEST_SEQUENCE);
        protein_with_a_rogid.setOrganism(new DefaultOrganism(55555));
        RogidGenerator rogidGenerator = new RogidGenerator();
        String rogid = rogidGenerator.calculateRogid(TEST_SEQUENCE,"55555");
        protein_with_a_rogid.setRogid(rogid);

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.setOrganism(new DefaultOrganism(55555,"Common","Scientific"));
        }


        assertEquals(rogid, protein_with_a_rogid.getRogid());

        minimumProteinEnricher.enrichProtein(protein_with_a_rogid);

        assertEquals(rogid, protein_with_a_rogid.getRogid());
    }


    //TODO
    /**
     * If the supplied rogid mismatches, throw a conflict.
     * @throws SeguidException
     */
    @Test
    public void test_conflict_if_rogid_is_different()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_a_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_a_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_a_rogid.setSequence(TEST_SEQUENCE);
        protein_with_a_rogid.setOrganism(new DefaultOrganism(55555));
        protein_with_a_rogid.setRogid("This is a bad ROGID");

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
                protein.setOrganism(new DefaultOrganism(55555,"Common","Scientific"));
        }


        minimumProteinEnricher.enrichProtein(protein_with_a_rogid);
    }


    //TODO
    /**
     * If there is no taxId but a rogid is present, it can not be confirmed so throw a conflict.
     * @throws SeguidException
     */
    @Test
    public void test_conflict_if_rogid_can_not_be_confirmed_due_to_missing_organism()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_a_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_a_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_a_rogid.setSequence(TEST_SEQUENCE);
        protein_with_a_rogid.setRogid("This is a bad ROGID");

        minimumProteinEnricher.enrichProtein(protein_with_a_rogid);
    }

    //TODO
    /**
     * If there is no taxId but a rogid is present, it can not be confirmed so throw a conflict.
     * @throws SeguidException
     */
    @Test
    public void test_conflict_if_rogid_can_not_be_confirmed_due_to_missing_taxid()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_a_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_a_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_a_rogid.setSequence(TEST_SEQUENCE);
        protein_with_a_rogid.setOrganism(new DefaultOrganism(-3));
        protein_with_a_rogid.setRogid("This is a bad ROGID");

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.setOrganism(new DefaultOrganism(-3));
        }

        minimumProteinEnricher.enrichProtein(protein_with_a_rogid);
    }


    //TODO
    /**
     * If there is not sequence but a rogid is present, it cannot be confirmed so throw a conflict.
     * @throws SeguidException
     */
    @Test
    public void test_conflict_if_rogid_can_not_be_confirmed_due_to_missing_sequence()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_with_a_rogid = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        protein_with_a_rogid.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_a_rogid.setOrganism(new DefaultOrganism(55555));
        protein_with_a_rogid.setRogid("This is a bad ROGID");


        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.setOrganism(new DefaultOrganism(55555,"Common","Scientific"));
            protein.setSequence("");
        }

        minimumProteinEnricher.enrichProtein(protein_with_a_rogid);
    }


    /**
     * Tests that when the proteinToEnrich contains no CRC64 checksum,
     * it is added.

     */
    @Test
    public void test_set_CRC64_if_none_in_proteinToEnrich()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.getChecksums().add(
                    ChecksumUtils.createChecksum("CRC64", null, "AbCdEfG"));
        }

        Protein protein_with_no_checksum = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME);
        protein_with_no_checksum.setUniprotkb(TEST_AC_FULL_PROT);

        assertTrue(protein_with_no_checksum.getChecksums().size() == 0);
        minimumProteinEnricher.enrichProtein(protein_with_no_checksum);
        assertTrue(protein_with_no_checksum.getChecksums().size() == 1);
    }

    /**
     * Tests that when the proteinToEnrich contains an identical CRC64 checksum,
     * it is not added.
     */
    @Test
    public void test_identical_CRC64_is_not_added()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.getChecksums().add(
                    ChecksumUtils.createChecksum("CRC64", null, "AbCdEfG"));
        }

        Protein protein_with_matching_checksum = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME);
        protein_with_matching_checksum.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_matching_checksum.setSequence(TEST_SEQUENCE);
        protein_with_matching_checksum.getChecksums().add(
                ChecksumUtils.createChecksum("CRC64", null, "AbCdEfG"));

        assertTrue(protein_with_matching_checksum.getChecksums().size() == 1);

        minimumProteinEnricher.enrichProtein(protein_with_matching_checksum);

        assertTrue(protein_with_matching_checksum.getChecksums().size() == 1);
        assertTrue(counter.getAdded() == 0);
    }

    //TODO
    /**
     * Tests that when a checksum that does not match is found, a conflict error is thrown.
     * The checksum comparison should also be case sensitive.
     */
    @Test
    public void test_case_sensitive_conflict_on_CRC64()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {


        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.getChecksums().add(
                ChecksumUtils.createChecksum("CRC64", null, "AbCdEfG"));
        }

        Protein protein_with_mismatched_checksum = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME);
        protein_with_mismatched_checksum.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_mismatched_checksum.getChecksums().add(
                ChecksumUtils.createChecksum("CRC64", null, "abcdefg"));

        minimumProteinEnricher.enrichProtein(protein_with_mismatched_checksum);
    }



    /**
     * Tests that when the fetched protein contains multiple checksums, a fetchingException is thrown.
     * This shows that the fetched protein contains errors.
     */
    @Test
    public void test_no_multiple_CRC64_checksums_in_fetched_proteins()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        for(Protein protein: fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            protein.getChecksums().add(
                    ChecksumUtils.createChecksum("CRC64", null, "AbCdEfG"));
            protein.getChecksums().add(
                ChecksumUtils.createChecksum("CRC64", null, "HiJkLmN"));
        }

        Protein protein_with_mismatched_checksum = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME);
        protein_with_mismatched_checksum.setUniprotkb(TEST_AC_FULL_PROT);
        for(Protein protein : fetcher.getProteinsByIdentifier(TEST_AC_FULL_PROT)){
            assertTrue(protein.getChecksums().size() == 2);
        }

        minimumProteinEnricher.enrichProtein(protein_with_mismatched_checksum);
    }


    /**
     * Enrich an already completed protein with a less complete protein
     * Count additions, should be 0
     * Count mismatches, should be at least 1 (shortname)
     * Check the id, should be the same as the enriched protein.
     * Run enrichment on another protein of different id
     * Check the id, should be the same as new protein
     * Check the mismatches.
     *
     */
    /*@Test
    public void test_enricher_event_is_cleared()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {

        Protein protein_test_one = new DefaultProtein("testpart1 shortName", "testpart1 fullName");
        protein_test_one.setUniprotkb(TEST_AC_FULL_PROT);
        protein_test_one.setSequence("TAGTAG");

        Protein protein_test_two = new DefaultProtein("testpart2 shortName", "testpart2 fullName");
        protein_test_two.setUniprotkb(TEST_AC_HALF_PROT);
        protein_test_two.setSequence("TAGTAG");

        //If this is failing, you may wish to use a logging listener to read the log.
        //this.minimumProteinEnricher.addEnricherListener(new LoggingEnricherListener());

        minimumProteinEnricher.enrichProtein(protein_test_one);

        assertEquals(event.getQueryID(), TEST_AC_FULL_PROT);

        assertTrue(event.getAdditions().size() == 0);
        assertTrue(event.getMismatches().size() > 0);
        assertTrue(event.getOverwrites().size() == 0);

        minimumProteinEnricher.enrichProtein(protein_test_two);

        assertEquals(event.getQueryID(), TEST_AC_HALF_PROT);
        assertTrue(event.getAdditions().size() == 0);
        assertTrue(event.getMismatches().size() == 1);
        assertTrue(event.getOverwrites().size() == 0);

    } */

    /**
     * Enrich a half completed protein.
     * Check that this fires an enrichmentEvent.
     * Check this event has recorded some additions
     * check this event has recorded some mismatches
     * check this event has not recorded some overwrites
     */
    @Test
    public void test_enricher_event_is_fired_and_has_correct_content()
            throws MissingServiceException,
            BadResultException,
            SeguidException,
            BadToEnrichFormException,
            BadSearchTermException,
            BridgeFailedException {


        Protein protein_to_enrich = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_to_enrich.setUniprotkb(TEST_AC_FULL_PROT);

        minimumProteinEnricher.enrichProtein(protein_to_enrich);

        //assertNotNull(event);
        //assertEquals(event.getObjectType(), "Protein");
        //assertEquals(event.getQueryID(), TEST_AC_FULL_PROT);
        //assertEquals(event.getQueryIDType(), "UniprotKB AC");
        assertTrue(counter.getAdded() > 0);
        assertTrue(counter.getRemoved() == 0);
    }
}
