package fr.abes.periscope.core.entity.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Getter
@Setter
public class DataField {

    @JacksonXmlProperty(isAttribute = true)
    private String tag;

    @JacksonXmlProperty(isAttribute = true)
    private String ind1;

    @JacksonXmlProperty(isAttribute = true)
    private String ind2;

    @JacksonXmlProperty(localName = "subfield")
    private List<SubField> subFields;
}
