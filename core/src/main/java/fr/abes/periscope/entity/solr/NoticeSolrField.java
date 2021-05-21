package fr.abes.periscope.entity.solr;

/**
 * Représente les champs SolR pour une Notice SolR
 * D'après l'xquery qui se trouve dans la table EQUERIES de la base XML
 * select xquery_clob from xqueries where cle3=4;
 *
 * Différence entre le TypeField String et Text de SolR
 * The fields as default defined in the solr schema are vastly different.
 * String stores a word/sentence as an exact string without performing tokenization etc.
 * Commonly useful for storing exact matches, e.g, for facetting.
 * Text typically performs tokenization, and secondary processing (such as lower-casing etc.).
 * Useful for all scenarios when we want to match part of a sentence.
 * If the following sample, "This is a sample sentence", is indexed to both fields we must search for exactly the text
 * This is a sample sentence to get a hit from the string field, while it may suffice to search for sample
 * (or even samples with stemmning enabled) to get a hit from the text field.
 *
 * The name of the field. Field names should consist of alphanumeric or underscore characters only and not start with a digit.
 * This is not currently strictly enforced, but other field names will not have first class support from all components
 * and back compatibility is not guaranteed. Names with both leading and trailing underscores (e.g., _version_) are reserved.
 * Every field must have a name.
 *
 * https://stackoverflow.com/questions/38945039/spring-data-solr-always-create-multivalued-fields
 */
public abstract class NoticeSolrField {

    public static final String ID = "id";
    public static final String TITLE_TYPE = "notice_type";
    public static final String PPN = "zone_001";

    //--------------------------------
    // leader
    public static final String L5 = "L5";
    public static final String L6 = "L6";
    public static final String L7 = "L7";
    public static final String L8 = "L8";
    public static final String L9 = "L9";
    //--------------------------------

    //--------------------------------
    // Zone < 10
    public static final String ZONE_001 = "zone_001";
    public static final String ZONE_002 = "zone_002";
    public static final String ZONE_003 = "zone_003";
    public static final String ZONE_004 = "zone_004";
    public static final String ZONE_005 = "zone_005";
    public static final String ZONE_006 = "zone_006";
    public static final String ZONE_007 = "zone_007";
    public static final String ZONE_008 = "zone_008";
    public static final String ZONE_009 = "zone_009";
    //--------------------------------

    //--------------------------------
    // Zone 011
    public static final String ISSN = "zone_011$a";
    public static final String ZONE_011_F = "zone_011$f";
    //--------------------------------

    //--------------------------------
    // Zone 033
    public static final String EXTERNAL_URLS = "zone_033$a";
    public static final String ZONE_033_2 = "zone_033$2";
    public static final String ZONE_033_D = "zone_033$d";
    //--------------------------------

    //--------------------------------
    // Zone 035
    public static final String ZONE_035_A = "zone_035$a";
    //--------------------------------

    //--------------------------------
    // Zone 100
    public static final String PROCESSING_GLOBAL_DATA = "zone_100$a";
    //--------------------------------
    
    //--------------------------------
    // Zone 101
    public static final String LANGUAGE = "zone_101$a";
    //--------------------------------

    //--------------------------------
    // Zone 102
    public static final String COUNTRY = "zone_102$a";
    //--------------------------------

    //--------------------------------
    // Zone 105
    public static final String ZONE_105_A = "zone_105$a";

    //--------------------------------
    // Zone 106
    public static final String ZONE_106_A = "zone_106$a";
    //--------------------------------

    //--------------------------------
    // Type de document
    public static final String DOCUMENT_TYPE = "document_type";

    //--------------------------------

    //--------------------------------
    //Type de support
    public static final String SUPPORT_TYPE = "support_type";
    //--------------------------------

    //--------------------------------
    // Zone 181
    public static final String ZONE_181_6 = "zone_181$6";
    public static final String ZONE_181_C = "zone_181$c";
    public static final String ZONE_181_2 = "zone_181$2";
    //--------------------------------

    //--------------------------------
    // Zone 182
    public static final String ZONE_182_6 = "zone_182$6";
    public static final String ZONE_182_C = "zone_182$c";
    public static final String ZONE_182_2 = "zone_182$2";
    //--------------------------------

    //--------------------------------
    // Zone 200
    public static final String PROPER_TITLE_FOR_DISPLAY = "zone_200$a_z";
    public static final String PROPER_TITLE = "zone_200$a_t";
    public static final String ZONE_200_B = "zone_200$b";
    public static final String TITLE_FROM_DIFFERENT_AUTHOR_FOR_DISPLAY = "zone_200$c_z";
    public static final String TITLE_FROM_DIFFERENT_AUTHOR = "zone_200$c_t";
    public static final String PARALLEL_TITLE_FOR_DISPLAY = "zone_200$d_z";
    public static final String PARALLEL_TITLE = "zone_200$d_t";
    public static final String TITLE_COMPLEMENT_FOR_DISPLAY = "zone_200$e_z";
    public static final String TITLE_COMPLEMENT = "zone_200$e_t";
    public static final String ZONE_200_F = "zone_200$f";
    public static final String ZONE_200_G = "zone_200$g";
    public static final String SECTION_TITLE_FOR_DISPLAY = "zone_200$i_z";
    public static final String SECTION_TITLE = "zone_200$i_t";

    //--------------------------------

    //--------------------------------
    // Zone 207
    public static final String ZONE_207_A = "zone_207$a";
    //--------------------------------

    //--------------------------------
    // Zone 210
    public static final String ZONE_210_A = "zone_210$a";
    public static final String EDITOR_Z = "zone_210$c_z";
    public static final String EDITOR_T = "zone_210$c_t";
    //--------------------------------

    //--------------------------------
    // Zone 530
    public static final String KEY_TITLE = "zone_530$a";
    public static final String KEY_TITLE_QUALIFIER = "zone_530$b";
    //--------------------------------

    //--------------------------------
    // Zone 531
    public static final String KEY_SHORTED_TITLE_FOR_DISPLAY = "zone_531$a_z";
    public static final String KEY_SHORTED_TITLE = "zone_531$a_t";
    //--------------------------------

    //--------------------------------
    // Zone 930
    @Deprecated
    public static final String PCP_LIST = "zone_930$z";
    @Deprecated
    public static final String RCR_LIST = "zone_930$b";
    //--------------------------------

    //--------------------------------
    // Champs supplémentaire
    @Deprecated
    public static final String DATE_AT = "dateetat_dt";
    @Deprecated
    public static final String DATE_AT_TYPE = "pdate";
    @Deprecated
    public static final String DATE_INDEX = "dateindex_dt";
    @Deprecated
    public static final String DATE_INDEX_TYPE = "pdate";
    public static final String NB_LOC = "NbLocs";
    public static final String PCP_RCR = "pcprcr";

    public static final String START_YEAR = "start_year";
    public static final String START_YEAR_CONFIDENCE_INDEX = "start_year_confidence_index";
    public static final String END_YEAR = "end_year";
    public static final String END_YEAR_CONFIDENCE_INDEX = "end_year_confidence_index";
    //--------------------------------

}
