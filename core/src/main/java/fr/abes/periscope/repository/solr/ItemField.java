package fr.abes.periscope.repository.solr;

/**
 * Représente les champs SolR pour un Exemplaire SolR
 */
public interface ItemField {

    String ID = "id";
    String ID_TYPE = "string";

    String EPN = "930-5";
    String EPN_TYPE = "string";

    //--------------------------------
    // Zone 930
    String PCP = "930-z";
    String PCP_TYPE = "strings";

    String RCR = "930-b";
    String RCR_TYPE = "strings";
    //--------------------------------

}
