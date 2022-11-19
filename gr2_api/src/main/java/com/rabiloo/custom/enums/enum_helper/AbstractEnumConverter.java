package com.rabiloo.custom.enums.enum_helper;

import javax.persistence.AttributeConverter;


public abstract class AbstractEnumConverter<EnumClazz extends Enum<EnumClazz> & ValueEnumInterface<TypeData>, TypeData>
        implements AttributeConverter<EnumClazz, TypeData> {

    private final Class<EnumClazz> enumCLass;

    public AbstractEnumConverter(Class<EnumClazz> enumCLass) {
        this.enumCLass = enumCLass;
    }

    @Override
    public TypeData convertToDatabaseColumn(EnumClazz attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public EnumClazz convertToEntityAttribute(TypeData value) {
        EnumClazz[] enums = enumCLass.getEnumConstants();
        for (EnumClazz type : enums) {
            if (type.getValue() == null) {
                continue;
            }
            if (type.getValue() == value || type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
