package fr.abes.periscope.core.entity.solr;

import fr.abes.periscope.core.repository.solr.NoticeField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Représente une notice au format d'indexation SolR V1
 */
@NoArgsConstructor
@Getter @Setter
@SolrDocument(collection = "periscope-v2")
public class NoticeSolr implements Serializable {

    @Id
    @Field(NoticeField.ID)
    @Indexed(name = NoticeField.ID, type = NoticeField.ID_TYPE)
    protected String id;

    @Field(NoticeField.TYPE)
    @Indexed(name = NoticeField.TYPE, type = NoticeField.TYPE_TYPE)
    private String type = "notice";

    @Field(NoticeField.PPN)
    @Indexed(name = NoticeField.PPN, type = NoticeField.PPN_TYPE)
    protected String ppn;

    @Field(NoticeField.ISSN)
    @Indexed(name = NoticeField.ISSN, type = NoticeField.ISSN_TYPE)
    protected String issn;

    @Field(NoticeField.PCP_LIST)
    @Indexed(name = NoticeField.PCP_LIST, type = NoticeField.PCP_LIST_TYPE)
    @Deprecated
    protected Set<String> pcpList;

    @Field(NoticeField.RCR_LIST)
    @Indexed(name = NoticeField.RCR_LIST, type = NoticeField.RCR_LIST_TYPE)
    @Deprecated
    protected Set<String> rcrList;

    @Field(NoticeField.EDITOR)
    @Indexed(name = NoticeField.EDITOR, type = NoticeField.EDITOR_TYPE)
    protected String editor;

    @Field(NoticeField.PROCESSING_GLOBAL_DATA)
    @Indexed(name = NoticeField.PROCESSING_GLOBAL_DATA, type = NoticeField.PROCESSING_GLOBAL_DATA_TYPE)
    protected String processingGlobalData;

    @Field(NoticeField.KEY_TITLE)
    @Indexed(name = NoticeField.KEY_TITLE, type = NoticeField.KEY_TITLE_TYPE)
    protected String keyTitle;

    @Field(NoticeField.KEY_SHORTED_TITLE)
    @Indexed(name = NoticeField.KEY_SHORTED_TITLE, type = NoticeField.KEY_SHORTED_TITLE_TYPE)
    protected String keyShortedTitle;

    @Field(NoticeField.PROPER_TITLE)
    @Indexed(name = NoticeField.PROPER_TITLE, type = NoticeField.PROPER_TITLE_TYPE)
    protected String properTitle;

    @Field(NoticeField.TITLE_FROM_DIFFERENT_AUTHOR)
    @Indexed(name = NoticeField.TITLE_FROM_DIFFERENT_AUTHOR, type = NoticeField.TITLE_FROM_DIFFERENT_AUTHOR_TYPE)
    protected String titleFromDifferentAuthor;

    @Field(NoticeField.PARALLEL_TITLE)
    @Indexed(name = NoticeField.PARALLEL_TITLE, type = NoticeField.PARALLEL_TITLE_TYPE)
    protected String parallelTitle;

    @Field(NoticeField.TITLE_COMPLEMENT)
    @Indexed(name = NoticeField.TITLE_COMPLEMENT, type = NoticeField.TITLE_COMPLEMENT_TYPE)
    protected String titleComplement;

    @Field(NoticeField.SECTION_TITLE)
    @Indexed(name = NoticeField.SECTION_TITLE, type = NoticeField.SECTION_TITLE_TYPE)
    protected String sectionTitle;

    @Field(NoticeField.KEY_TITLE_QUALIFIER)
    @Indexed(name = NoticeField.KEY_TITLE_QUALIFIER, type = NoticeField.KEY_TITLE_QUALIFIER_TYPE)
    protected String keyTitleQualifer;

    @Field(NoticeField.CONTINIOUS_TYPE)
    @Indexed(name = NoticeField.CONTINIOUS_TYPE, type = NoticeField.CONTINIOUS_TYPE_TYPE)
    protected String continiousType;

    @Field(NoticeField.EXTERNAL_URLS)
    @Indexed(name = NoticeField.EXTERNAL_URLS, type = NoticeField.EXTERNAL_URLS_TYPE)
    protected List<String> externalURLs;

    @Field(NoticeField.NB_LOC)
    @Indexed(name = NoticeField.NB_LOC, type = NoticeField.NB_LOC_TYPE)
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
        return "NoticeSolr {"+ "id="+id+", ppn="+ ppn+", issn="+issn+"}";
    }
}
