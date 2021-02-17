package fr.abes.periscope.core.repository.solr;

/**
 * Représente les champs SolR pour une Notice SolR
 * D'après l'xquery qui se trouve dans la table EQUERIES de la base XML
 * select xquery_clob from xqueries where cle3=4;
 */
public interface NoticeField {

    //--------------------------------
    // @tag="001"
    String PPN_Z = "001_z";
    //--------------------------------

    //--------------------------------
    // date
    String DATE_AT = "dateetat_dt";
    String DATE_INDEX = "dateindex_dt";
    //--------------------------------

    //--------------------------------
    // leader
    String L5 = "L5_s";
    String L6 = "L6_s";
    String L7 = "L7_s";
    String L8 = "L8_s";
    String L9 = "L9_s";
    //--------------------------------

    //--------------------------------
    // @tag < 10
    String ZONE_001_S = "001_s";
    String ZONE_001_Z = "001_z";
    String ZONE_001_T = "001_t";

    String ZONE_002_S = "002_s";
    String ZONE_002_Z = "002_z";
    String ZONE_002_T = "002_t";

    String ZONE_003_S = "003_s";
    String ZONE_003_Z = "003_z";
    String ZONE_003_T = "003_t";

    String ZONE_004_S = "004_s";
    String ZONE_004_Z = "004_z";
    String ZONE_004_T = "004_t";

    String ZONE_005_S = "005_s";
    String ZONE_005_Z = "005_z";
    String ZONE_005_T = "005_t";

    String ZONE_006_S = "006_s";
    String ZONE_006_Z = "006_z";
    String ZONE_006_T = "006_t";

    String ZONE_007_S = "007_s";
    String ZONE_007_Z = "007_z";
    String ZONE_007_T = "007_t";

    String ZONE_008_S = "008_s";
    String ZONE_008_Z = "008_z";
    String ZONE_008_T = "008_t";

    String ZONE_009_S = "009_s";
    String ZONE_009_Z = "009_z";
    String ZONE_009_T = "009_t";
    //--------------------------------

    //--------------------------------
    // @tag=930
    String PCP_RCR_T = "pcprcr_t";
    //--------------------------------

    //--------------------------------
    // @tag > 9 and @tag < 900
    String ISSN_S = "011-a_s";
    String ISSN_T = "011-a_t";

    String EXTERNAL_URLS_S = "033-a_s";
    String EXTERNAL_URLS_T = "033-a_t";

    String PROCESSING_GLOBAL_DATA_S = "100-a_s";
    String PROCESSING_GLOBAL_DATA_T = "100-a_t";

    String LANGUAGE_S = "101-a_s";
    String LANGUAGE_T = "101-a_t";

    String COUNTRY_S = "102-a_s";
    String COUNTRY_T = "102-a_t";

    String CONTINIOUS_TYPE_S = "110-a_s";
    String CONTINIOUS_TYPE_T = "110-a_t";

    String PROPER_TITLE_S = "200-a_s";
    String PROPER_TITLE_T = "200-a_t";

    String TITLE_FROM_DIFFERENT_AUTHOR_S = "200-c_s";
    String TITLE_FROM_DIFFERENT_AUTHOR_T = "200-c_t";

    String PARALLEL_TITLE_S = "200-d_s";
    String PARALLEL_TITLE_T = "200-d_t";

    String TITLE_COMPLEMENT_S = "200-e_s";
    String TITLE_COMPLEMENT_T = "200-e_t";

    String SECTION_TITLE_S = "200-i_s";
    String SECTION_TITLE_T = "200-i_t";

    String EDITOR_S = "210-c_s";
    String EDITOR_T = "210-c_t";

    String KEY_TITLE_S = "530-a_s";
    String KEY_TITLE_T = "530-a_t";

    String KEY_TITLE_QUALIFIER_S = "530-b_s";
    String KEY_TITLE_QUALIFIER_T = "530-b_t";

    String KEY_SHORTED_TITLE_S = "531-a_s";
    String KEY_SHORTED_TITLE_T = "531-a_t";
    //--------------------------------

    //--------------------------------
    // @tag >899
    String PCP_S = "930-z_s";
    String PCP_T = "930-z_t";

    String RCR_S = "930-b_s";
    String RCR_T = "930-b_t";
    //--------------------------------

    //--------------------------------
    // @tag > 9  and @tag <900 not(@tag = preceding-sibling::datafield/@tag)])
    String COUNTRY_Z = "102-a_z";
    String ISSN = "011-a_z";
    String CONTINIOUS_TYPE = "110-a_z";

    String PROPER_TITLE = "200-a_z";
    String TITLE_FROM_DIFFERENT_AUTHOR = "200-c_z";
    String PARALLEL_TITLE = "200-d_z";
    String TITLE_COMPLEMENT = "200-e_z";
    String SECTION_TITLE = "200-i_z";
    String EDITOR = "210-c_z";
    String KEY_TITLE = "530-a_z";
    String KEY_SHORTED_TITLE = "531-a_z";
    String KEY_TITLE_QUALIFIER = "530-b_z";
    //--------------------------------

    String NB_LOC = "NbLocs_i";
}
