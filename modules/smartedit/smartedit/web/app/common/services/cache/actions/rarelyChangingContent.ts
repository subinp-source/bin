/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CacheAction } from '@smart/utils';

/** @internal */
export const RarelyChangingContentName: string = 'RarelyChangingContent';

export const rarelyChangingContent = new CacheAction(RarelyChangingContentName);
