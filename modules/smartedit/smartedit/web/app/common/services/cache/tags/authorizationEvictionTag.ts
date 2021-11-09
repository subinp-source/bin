/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { EvictionTag } from '@smart/utils';

// TODO : merge the EVENT strings and the tag ones
export const authorizationEvictionTag = new EvictionTag({ event: 'AUTHORIZATION_SUCCESS' });
