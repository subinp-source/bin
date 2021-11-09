/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.oauth2.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.smarteditwebservices.data.SmarteditAuthenticatedUserData;

/**
 * Populates the {@link SmarteditAuthenticatedUserData} uid field using the {@link UserModel} uid field
 */
public class UserUidPopulator implements Populator<UserModel, SmarteditAuthenticatedUserData>
{
  @Override
  public void populate(final UserModel source, final SmarteditAuthenticatedUserData target) throws ConversionException
  {
    target.setUid(source.getUid());
  }
}
