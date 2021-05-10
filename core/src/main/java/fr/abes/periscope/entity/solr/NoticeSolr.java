package fr.abes.periscope.entity.solr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Représente une notice au format d'indexation SolR V2
 */
@NoArgsConstructor
@Getter @Setter
@SolrDocument(collection = "periscope-v2")
public class NoticeSolr {

    @Id
    @Field(NoticeSolrField.ID)
    @Indexed(name = NoticeSolrField.ID)
    protected String id;

    @Field(NoticeSolrField.TITLE_TYPE)
    @Indexed(name = NoticeSolrField.TITLE_TYPE)
    private String type = "notice";

    @Field(NoticeSolrField.PPN)
    @Indexed(name = NoticeSolrField.PPN)
    protected String ppn;

    @Field(NoticeSolrField.ISSN)
    @Indexed(name = NoticeSolrField.ISSN)
    protected String issn;

    @Field(NoticeSolrField.EDITOR)
    @Indexed(name = NoticeSolrField.EDITOR)
    protected String editor;

    @Field(NoticeSolrField.PROCESSING_GLOBAL_DATA)
    @Indexed(name = NoticeSolrField.PROCESSING_GLOBAL_DATA)
    protected String processingGlobalData;

    @Field(NoticeSolrField.KEY_TITLE)
    @Indexed(name = NoticeSolrField.KEY_TITLE)
    protected String keyTitle;

    @Field(NoticeSolrField.KEY_SHORTED_TITLE)
    @Indexed(name = NoticeSolrField.KEY_SHORTED_TITLE)
    protected String keyShortedTitle;

    @Field(NoticeSolrField.PROPER_TITLE)
    @Indexed(name = NoticeSolrField.PROPER_TITLE)
    protected String properTitle;

    @Field(NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR)
    @Indexed(name = NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR)
    protected String titleFromDifferentAuthor;

    @Field(NoticeSolrField.PARALLEL_TITLE)
    @Indexed(name = NoticeSolrField.PARALLEL_TITLE)
    protected String parallelTitle;

    @Field(NoticeSolrField.TITLE_COMPLEMENT)
    @Indexed(name = NoticeSolrField.TITLE_COMPLEMENT)
    protected String titleComplement;

    @Field(NoticeSolrField.SECTION_TITLE)
    @Indexed(name = NoticeSolrField.SECTION_TITLE)
    protected String sectionTitle;

    @Field(NoticeSolrField.KEY_TITLE_QUALIFIER)
    @Indexed(name = NoticeSolrField.KEY_TITLE_QUALIFIER)
    protected String keyTitleQualifer;

    @Field(NoticeSolrField.DOCUMENT_TYPE)
    @Indexed(name = NoticeSolrField.DOCUMENT_TYPE)
    protected String typeDocument;

    @Field(NoticeSolrField.EXTERNAL_URLS)
    @Indexed(name = NoticeSolrField.EXTERNAL_URLS)
    protected List<String> externalURLs = new ArrayList<>();

    @Field(NoticeSolrField.NB_LOC)
    @Indexed(name = NoticeSolrField.NB_LOC)
    protected Integer nbLocation;

    @Field(NoticeSolrField.LANGUAGE)
    @Indexed(name = NoticeSolrField.LANGUAGE)
    protected String language;

    @Field(NoticeSolrField.COUNTRY)
    @Indexed(name = NoticeSolrField.COUNTRY)
    protected String country;

    @Field(NoticeSolrField.START_YEAR)
    @Indexed(name = NoticeSolrField.START_YEAR)
    protected Integer startYear;

    @Field(NoticeSolrField.START_YEAR_CONFIDENCE_INDEX)
    @Indexed(name = NoticeSolrField.START_YEAR_CONFIDENCE_INDEX)
    protected Integer startYearConfidenceIndex;

    @Field(NoticeSolrField.END_YEAR)
    @Indexed(name = NoticeSolrField.END_YEAR)
    protected Integer endYear;

    @Field(NoticeSolrField.END_YEAR_CONFIDENCE_INDEX)
    @Indexed(name = NoticeSolrField.END_YEAR_CONFIDENCE_INDEX)
    protected Integer endYearConfidenceIndex;

    @ChildDocument
    protected Set<ItemSolr> itemSolrs = new HashSet<>();

    protected boolean toDelete;
    protected Set<String> rcrList = new HashSet<>();

    public void addItem(ItemSolr specimen) {
        this.itemSolrs.add(specimen);
        this.rcrList.add(specimen.getRcr());
    }

    public void addExternalUrl(String url) {
        this.externalURLs.add(url);
    }

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

        return this.ppn != null && ppn.equals(((NoticeSolr) obj).ppn);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "NoticeSolrExtended {"+ "ppn="+ ppn+", issn="+issn+" , items="+ itemSolrs +"}";
    }
}
