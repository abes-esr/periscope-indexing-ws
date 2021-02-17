package fr.abes.periscope.core.entity.solr;

import fr.abes.periscope.core.repository.solr.NoticeField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Représente une notice au format d'indexation SolR V1
 */
@NoArgsConstructor
@Getter @Setter
@SolrDocument(collection = "periscope")
public class NoticeSolr implements Serializable {

    @Id
    @Indexed(name = NoticeField.PPN_Z, type = "string")
    protected String ppn;

    @Indexed(name = NoticeField.ISSN, type = "string")
    protected String issn;

    @Indexed(name = NoticeField.PCP_T, type = "string")
    protected HashSet<String> pcpList;

    @Indexed(name = NoticeField.RCR_T, type = "string")
    protected HashSet<String> rcrList;

    @Indexed(name = NoticeField.EDITOR, type = "string")
    protected String editor;

    @Indexed(name = NoticeField.PROCESSING_GLOBAL_DATA_T, type = "string")
    protected String processingGlobalData;

    @Indexed(name = NoticeField.KEY_TITLE, type = "string")
    protected String keyTitle;

    @Indexed(name = NoticeField.KEY_SHORTED_TITLE, type = "string")
    protected String keyShortedTitle;

    @Indexed(name = NoticeField.PROPER_TITLE, type = "string")
    protected String properTitle;

    @Indexed(name = NoticeField.TITLE_FROM_DIFFERENT_AUTHOR, type = "string")
    protected String titleFromDifferentAuthor;

    @Indexed(name = NoticeField.PARALLEL_TITLE, type = "string")
    protected String parallelTitle;

    @Indexed(name = NoticeField.TITLE_COMPLEMENT, type = "string")
    protected String titleComplement;

    @Indexed(name = NoticeField.SECTION_TITLE, type = "string")
    protected String sectionTitle;

    @Indexed(name = NoticeField.KEY_TITLE_QUALIFIER, type = "string")
    protected String keyTitleQualifer;

    @Indexed(name = NoticeField.CONTINIOUS_TYPE, type = "string")
    protected String continiousType;

    @Indexed(name = NoticeField.EXTERNAL_URLS_S, type = "string")
    protected List<String> externalURLs;

    @Indexed(name = NoticeField.NB_LOC, type = "integer")
    protected Integer nbLocation;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return ppn != null && ppn.equals(((NoticeSolr) obj).ppn);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "NoticeSolr {"+ "ppn="+ ppn+", issn="+issn+"}";
    }
}
