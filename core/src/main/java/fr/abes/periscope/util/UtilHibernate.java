package fr.abes.periscope.util;

import org.hibernate.metamodel.internal.EntityTypeImpl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;
import java.util.Set;

public class UtilHibernate {
    public static org.hibernate.annotations.NamedQuery findNamedQuery(EntityManagerFactory emf, String query) {
        Set<ManagedType<?>> managedTypes = emf.getMetamodel().getManagedTypes();
        for (ManagedType<?> managedType: managedTypes) {
            if (managedType instanceof EntityTypeImpl) {
                org.hibernate.annotations.NamedQuery namedQuery =  managedType.getJavaType().getAnnotation(org.hibernate.annotations.NamedQuery.class);
                if (namedQuery != null && namedQuery.name().equals(query)) {
                    return namedQuery;
                }
            }
        }
        return null;
    }

}
