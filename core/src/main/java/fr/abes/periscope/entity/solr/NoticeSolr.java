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

    @Field(NoticeSolrField.EDITOR_T)
    @Indexed(name = NoticeSolrField.EDITOR_T)
    protected List<String> editorForSearch = new ArrayList<>();

    @Field(NoticeSolrField.EDITOR_Z)
    @Indexed(name = NoticeSolrField.EDITOR_Z)
    protected String editorForDisplay;

    @Field(NoticeSolrField.PROCESSING_GLOBAL_DATA)
    @Indexed(name = NoticeSolrField.PROCESSING_GLOBAL_DATA)
    protected String processingGlobalData;

    @Field(NoticeSolrField.PROPER_TITLE_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.PROPER_TITLE_FOR_DISPLAY)
    protected String properTitleForDisplay;

    @Field(NoticeSolrField.PROPER_TITLE)
    @Indexed(name = NoticeSolrField.PROPER_TITLE)
    protected List<String> properTitle = new ArrayList<>();

    @Field(NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR_FOR_DISPLAY)
    protected String titleFromDifferentAuthorForDisplay;

    @Field(NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR)
    @Indexed(name = NoticeSolrField.TITLE_FROM_DIFFERENT_AUTHOR)
    protected List<String> titleFromDifferentAuthor = new ArrayList<>();

    @Field(NoticeSolrField.PARALLEL_TITLE_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.PARALLEL_TITLE_FOR_DISPLAY)
    protected String parallelTitleForDisplay;

    @Field(NoticeSolrField.PARALLEL_TITLE)
    @Indexed(name = NoticeSolrField.PARALLEL_TITLE)
    protected List<String> parallelTitle = new ArrayList<>();

    @Field(NoticeSolrField.TITLE_COMPLEMENT_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.TITLE_COMPLEMENT_FOR_DISPLAY)
    protected String titleComplementForDisplay;

    @Field(NoticeSolrField.TITLE_COMPLEMENT)
    @Indexed(name = NoticeSolrField.TITLE_COMPLEMENT)
    protected List<String> titleComplement = new ArrayList<>();

    @Field(NoticeSolrField.SECTION_TITLE_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.SECTION_TITLE_FOR_DISPLAY)
    protected String sectionTitleForDisplay;

    @Field(NoticeSolrField.SECTION_TITLE)
    @Indexed(name = NoticeSolrField.SECTION_TITLE)
    protected List<String> sectionTitle = new ArrayList<>();

    @Field(NoticeSolrField.KEY_TITLE)
    @Indexed(name = NoticeSolrField.KEY_TITLE)
    protected String keyTitle;

    @Field(NoticeSolrField.KEY_TITLE_QUALIFIER)
    @Indexed(name = NoticeSolrField.KEY_TITLE_QUALIFIER)
    protected String keyTitleQualifer;

    @Field(NoticeSolrField.KEY_SHORTED_TITLE_FOR_DISPLAY)
    @Indexed(name = NoticeSolrField.KEY_SHORTED_TITLE_FOR_DISPLAY)
    protected String keyShortedTitleForDisplay;

    @Field(NoticeSolrField.KEY_SHORTED_TITLE)
    @Indexed(name = NoticeSolrField.KEY_SHORTED_TITLE)
    protected List<String> keyShortedTitle = new ArrayList<>();

    @Field(NoticeSolrField.DOCUMENT_TYPE)
    @Indexed(name = NoticeSolrField.DOCUMENT_TYPE)
    protected String typeDocument;

    @Field(NoticeSolrField.SUPPORT_TYPE)
    @Indexed(name = NoticeSolrField.SUPPORT_TYPE)
    protected String typeSupport;

    @Field(NoticeSolrField.EXTERNAL_URLS)
    @Indexed(name = NoticeSolrField.EXTERNAL_URLS)
    protected List<String> externalURLs = new ArrayList<>();

    @Field(NoticeSolrField.NB_LOC)
    @Indexed(name = NoticeSolrField.NB_LOC)
    protected Integer nbLocation;

    @Field(NoticeSolrField.NB_PCP)
    @Indexed(name = NoticeSolrField.NB_PCP)
    protected Integer nbPcp;

    @Field(NoticeSolrField.LANGUAGE)
    @Indexed(name = NoticeSolrField.LANGUAGE)
    protected String language;

    @Field(NoticeSolrField.COUNTRY)
    @Indexed(name = NoticeSolrField.COUNTRY)
    protected String country;

    @Field(NoticeSolrField.START_YEAR)
    @Indexed(name = NoticeSolrField.START_YEAR)
    protected String startYear;

    @Field(NoticeSolrField.START_YEAR_CONFIDENCE_INDEX)
    @Indexed(name = NoticeSolrField.START_YEAR_CONFIDENCE_INDEX)
    protected Integer startYearConfidenceIndex;

    @Field(NoticeSolrField.END_YEAR)
    @Indexed(name = NoticeSolrField.END_YEAR)
    protected String endYear;

    @Field(NoticeSolrField.END_YEAR_CONFIDENCE_INDEX)
    @Indexed(name = NoticeSolrField.END_YEAR_CONFIDENCE_INDEX)
    protected Integer endYearConfidenceIndex;

    @Field(NoticeSolrField.RCR_LIST)
    @Indexed(name = NoticeSolrField.RCR_LIST)
    protected Set<String> rcrList = new HashSet<>();

    @Field(NoticeSolrField.PCP_LIST)
    @Indexed(name = NoticeSolrField.PCP_LIST)
    protected Set<String> pcpList = new HashSet<>();

    @ChildDocument
    protected Set<ItemSolr> itemSolrs = new HashSet<>();

    protected boolean toDelete;

    public void addItem(ItemSolr specimen) {
        this.itemSolrs.add(specimen);
        this.rcrList.add(specimen.getRcr());
    }

    public void addExternalUrl(String url) {
        this.externalURLs.add(url);
    }

    public void addEditor(String editor) {
        this.editorForSearch.add(editor);
    }

    public void addKeyShortedTitle(String keyShortedTitle) {
        this.keyShortedTitle.add(keyShortedTitle);
    }

    public void addProperTitle(String properTitle) {
        this.properTitle.add(properTitle);
    }

    public void addTitleFromDifferentAuthor(String titleFromDifferentAuthor) {
        this.titleFromDifferentAuthor.add(titleFromDifferentAuthor);
    }

    public void addParallelTitle(String parallelTitle) {
        this.parallelTitle.add(parallelTitle);
    }

    public void addTitleComplement(String titleComplement) {
        this.titleComplement.add(titleComplement);
    }

    public void addSectionTitle(String sectionTitle) {
        this.sectionTitle.add(sectionTitle);
    }

    public void addRcr(String rcr) { this.rcrList.add(rcr);}

    public void addPcp(String pcp) { this.pcpList.add(pcp);}

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
