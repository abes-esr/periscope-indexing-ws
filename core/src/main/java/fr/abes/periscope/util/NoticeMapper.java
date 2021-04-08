package fr.abes.periscope.util;

import fr.abes.periscope.entity.solr.NoticeSolr;
import fr.abes.periscope.entity.solr.ItemSolr;
import fr.abes.periscope.entity.xml.DataField;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.entity.xml.SubField;
import fr.abes.periscope.exception.MissingFieldException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NoticeMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Fonction de mapping générique pour un objet
     *
     * @param source      Objet source
     * @param targetClass Classe de l'objet cible
     * @return Objet cible
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    /**
     * Fonction de mapping générique pour des listes
     *
     * @param source      Liste source
     * @param targetClass Classe des objets cibles
     * @return Liste des objets cibles
     */
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Convertisseur pour les notices XML vers les notices SolR avec des exemplaires
     */
    @Bean
    public void converterNoticeXML() {

        Converter<NoticeXml, NoticeSolr> myConverter = new Converter<NoticeXml, NoticeSolr>() {

            public NoticeSolr convert(MappingContext<NoticeXml, NoticeSolr> context) {
                NoticeXml source = context.getSource();
                NoticeSolr target = new NoticeSolr();
                try {
                    boolean deleteFlag = source.getLeader().substring(5,6).equalsIgnoreCase("d")?true:false;
                    target.setToDelete(deleteFlag);
                    // ID
                    target.setId(source.getControlFields().stream().filter(elm -> elm.getTag().equalsIgnoreCase("001")).findFirst().orElseThrow().getValue());

                    // Champs PPN
                    target.setPpn(source.getControlFields().stream().filter(elm -> elm.getTag().equalsIgnoreCase("001")).findFirst().orElseThrow().getValue());

                    // Champs data fields
                    Iterator<DataField> iterator = source.getDataFields().iterator();
                    while (iterator.hasNext()) {
                        DataField dataField = iterator.next();

                        // Zone 035
                        if (dataField.getTag().equalsIgnoreCase("035")) {

                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                // zone 035-a
                                if (subField.getCode().equalsIgnoreCase("a") && subField.getValue().contains("issn")) {
                                    target.setIssn(subField.getValue().substring(4));
                                }
                            }
                        }

                        // Zone 200
                        if (dataField.getTag().equalsIgnoreCase("200")) {

                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                // zone 200-a
                                if (subField.getCode().equalsIgnoreCase("a")) {
                                    target.setProperTitle(subField.getValue());
                                }

                                // zone 200-c
                                if (subField.getCode().equalsIgnoreCase("c")) {
                                    target.setTitleFromDifferentAuthor(subField.getValue());
                                }

                                // zone 200-d
                                if (subField.getCode().equalsIgnoreCase("d")) {
                                    target.setParallelTitle(subField.getValue());
                                }

                                // zone 200-e
                                if (subField.getCode().equalsIgnoreCase("e")) {
                                    target.setTitleComplement(subField.getValue());
                                }

                                // zone 200-i
                                if (subField.getCode().equalsIgnoreCase("i")) {
                                    target.setSectionTitle(subField.getValue());
                                }
                            }

                        }

                        if (dataField.getTag().equalsIgnoreCase("110")) {
                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();
                                if (subField.getCode().equalsIgnoreCase("a")) {
                                    target.setTypeDocument(subField.getValue().substring(0, 1));
                                }
                            }
                        }

                        // Zone 9XX
                        if (dataField.getTag().startsWith("9")) {

                            // On cherche la sous-zone 5 qui contient le EPN
                            SubField specimenIdField = dataField.getSubFields().stream().filter(elm -> elm.getCode().equalsIgnoreCase("5"))
                                    .findAny().orElse(null);

                            if (specimenIdField == null) {
                                throw new MissingFieldException("Zone "+dataField.getTag()+" doesn't have a subfield code=\"5\"");
                            }

                            String epn = specimenIdField.getValue().split(":")[1];

                            // On récupère l'exemplaire ou on le crée s'il n'existe pas
                            ItemSolr itemSolr = target.getItemSolrs().stream().filter(elm -> elm.getId().equalsIgnoreCase(epn))
                                    .findAny().orElse(null);

                            if (itemSolr == null) {
                                itemSolr = new ItemSolr(target.getPpn(),epn);
                            }

                            // On itère sur les autres sous-zone
                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                if (dataField.getTag().equalsIgnoreCase("930")) {
                                    if (subField.getCode().equalsIgnoreCase("b")) {
                                        itemSolr.setRcr(subField.getValue());
                                    }
                                    if (subField.getCode().equalsIgnoreCase("z")) {
                                        itemSolr.getPcp().add(subField.getValue());
                                    }
                                }
                            }
                            target.addItem(itemSolr);
                        }
                    }

                    return target;

                } catch (NullPointerException ex) {
                    throw new MappingException(Arrays.asList(new ErrorMessage("NoticeSolr has null field")));
                } catch (Exception ex) {
                    throw new MappingException(Arrays.asList(new ErrorMessage(ex.getMessage())));
                }

            }
        };
        modelMapper.addConverter(myConverter);
    }

}
