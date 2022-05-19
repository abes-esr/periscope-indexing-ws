package fr.abes.periscope.repository.baseXml;

import fr.abes.periscope.entity.xml.PeriscopeIndex;
import fr.abes.periscope.util.BaseXMLConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@BaseXMLConfiguration
@Repository
public interface PeriscopeIndexRepository extends JpaRepository<PeriscopeIndex, String> {
}
