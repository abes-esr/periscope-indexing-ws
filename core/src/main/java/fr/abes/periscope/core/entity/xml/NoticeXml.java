package fr.abes.periscope.core.entity.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.SortedSet;

/**
 * Représente une notice au format d'export UnimarcXML
 */
@NoArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement(localName = "record")
public class NoticeXml {

    @JacksonXmlProperty(localName = "leader")
    private String leader;

    @JacksonXmlProperty(localName = "controlfield")
    private List<ControlField> controlFields;

    @JacksonXmlProperty(localName = "datafield")
    private List<DataField> dataFields;

    @Override
    public String toString() {
        return "Notice {"+ "leader="+ leader+"}";
    }

}
