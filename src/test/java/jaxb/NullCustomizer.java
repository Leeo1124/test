package jaxb;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.mappings.XMLDirectMapping;
import org.eclipse.persistence.oxm.mappings.nullpolicy.XMLNullRepresentationType;

public class NullCustomizer implements DescriptorCustomizer {

    @Override
    public void customize(ClassDescriptor descriptor) {
        for(DatabaseMapping mapping : descriptor.getMappings()) {
            if(mapping.isAbstractDirectMapping()) {
                XMLDirectMapping xmlDirectMapping = (XMLDirectMapping) mapping;
                xmlDirectMapping.getNullPolicy().setMarshalNullRepresentation(XMLNullRepresentationType.EMPTY_NODE);
                xmlDirectMapping.getNullPolicy().setNullRepresentedByEmptyNode(true);
            }
        }
    }

}