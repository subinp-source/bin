/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.mapping.mappers;

import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;
import ma.glasnost.orika.MappingContext;

public class B2BCostCenterDataMapper extends AbstractCustomMapper<B2BCostCenterData, B2BCostCenterWsDTO> {

    @Override
    public void mapAtoB(final B2BCostCenterData a, final B2BCostCenterWsDTO b, final MappingContext context)
    {
        // other fields are mapped automatically
        b.setActiveFlag(a.isActive());
    }

    @Override
    public void mapBtoA(final B2BCostCenterWsDTO b, final B2BCostCenterData a, final MappingContext context)
    {
        // other fields are mapped automatically
        if (b.getActiveFlag() != null) {
            a.setActive(b.getActiveFlag());
        }
    }
}
