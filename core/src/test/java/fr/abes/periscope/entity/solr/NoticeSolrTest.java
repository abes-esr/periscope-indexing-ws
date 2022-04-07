package fr.abes.periscope.entity.solr;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoticeSolrTest {
    @Test
    @DisplayName("test construction titre notice")
    void testGetTitre() {
        NoticeSolr notice = new NoticeSolr();
        notice.setKeyTitle("titre clé");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre clé");

        notice.setKeyTitleQualifer("titre clé qualifié");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre clé titre clé qualifié");

        notice = new NoticeSolr();
        notice.setKeyShortedTitleForDisplay("titre clé court");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre clé court");

        notice = new NoticeSolr();
        notice.setProperTitleForDisplay("titre propre");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre propre");

        notice = new NoticeSolr();
        notice.setTitleFromDifferentAuthorForDisplay("titre d'un auteur différent");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre d'un auteur différent");

        notice = new NoticeSolr();
        notice.setParallelTitleForDisplay("titre parallèle");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "titre parallèle");

        notice = new NoticeSolr();
        notice.setTitleComplementForDisplay("Complément de titre");
        notice.setTriTitre();
        assertEquals(notice.getTriTitre(), "Complément de titre");
    }
}
