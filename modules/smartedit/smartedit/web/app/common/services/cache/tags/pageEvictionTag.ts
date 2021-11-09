/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { EvictionTag } from '@smart/utils';

export const pageCreationEvictionTag = new EvictionTag({ event: 'PAGE_CREATED_EVENT' });

export const pageDeletionEvictionTag = new EvictionTag({ event: 'PAGE_DELETED_EVENT' });

export const pageUpdateEvictionTag = new EvictionTag({ event: 'PAGE_UPDATED_EVENT' });

export const pageRestoredEvictionTag = new EvictionTag({ event: 'PAGE_RESTORED_EVENT' });

export const pageChangeEvictionTag = new EvictionTag({ event: 'PAGE_CHANGE' });

export const pageEvictionTag = new EvictionTag({
    event: 'pageEvictionTag',
    relatedTags: [pageCreationEvictionTag, pageDeletionEvictionTag, pageUpdateEvictionTag]
});
