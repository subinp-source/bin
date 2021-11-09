/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { diNameUtils, SeModule } from 'smarteditcommons';
import {
    CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI,
    CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID,
    CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI,
    GET_PAGE_SYNCHRONIZATION_RESOURCE_URI,
    ITEMS_RESOURCE_URI,
    NAVIGATION_MANAGEMENT_ENTRIES_RESOURCE_URI,
    NAVIGATION_MANAGEMENT_ENTRY_TYPES_RESOURCE_URI,
    NAVIGATION_MANAGEMENT_PAGE_PATH,
    NAVIGATION_MANAGEMENT_RESOURCE_URI,
    PAGE_CONTEXT_CATALOG,
    PAGE_CONTEXT_CATALOG_VERSION,
    PAGE_CONTEXT_SITE_ID,
    PAGE_LIST_PATH,
    PAGE_TEMPLATES_URI,
    PAGE_TYPES_RESTRICTION_TYPES_URI,
    PAGE_TYPES_URI,
    PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI,
    PAGES_CONTENT_SLOT_RESOURCE_URI,
    PAGES_LIST_RESOURCE_URI,
    PAGES_RESTRICTIONS_RESOURCE_URI,
    POST_PAGE_SYNCHRONIZATION_RESOURCE_URI,
    RESTRICTION_TYPES_URI,
    TRASHED_PAGE_LIST_PATH,
    TYPES_RESOURCE_URI
} from './resourceLocationsConstants';

@SeModule({
    providers: [
        diNameUtils.makeValueProvider({ CONTEXT_SITE_ID }),
        diNameUtils.makeValueProvider({ CONTEXT_CATALOG }),
        diNameUtils.makeValueProvider({ CONTEXT_CATALOG_VERSION }),
        diNameUtils.makeValueProvider({ PAGE_CONTEXT_SITE_ID }),
        diNameUtils.makeValueProvider({ PAGE_CONTEXT_CATALOG }),
        diNameUtils.makeValueProvider({ PAGE_CONTEXT_CATALOG_VERSION }),
        diNameUtils.makeValueProvider({ TYPES_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ ITEMS_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ PAGES_LIST_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ PAGE_LIST_PATH }),
        diNameUtils.makeValueProvider({ TRASHED_PAGE_LIST_PATH }),
        diNameUtils.makeValueProvider({ PAGES_CONTENT_SLOT_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ PAGE_TEMPLATES_URI }),
        diNameUtils.makeValueProvider({ NAVIGATION_MANAGEMENT_PAGE_PATH }),
        diNameUtils.makeValueProvider({ NAVIGATION_MANAGEMENT_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ NAVIGATION_MANAGEMENT_ENTRIES_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ NAVIGATION_MANAGEMENT_ENTRY_TYPES_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ PAGES_RESTRICTIONS_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ RESTRICTION_TYPES_URI }),
        diNameUtils.makeValueProvider({ PAGE_TYPES_RESTRICTION_TYPES_URI }),
        diNameUtils.makeValueProvider({ PAGE_TYPES_URI }),
        diNameUtils.makeValueProvider({ GET_PAGE_SYNCHRONIZATION_RESOURCE_URI }),
        diNameUtils.makeValueProvider({ POST_PAGE_SYNCHRONIZATION_RESOURCE_URI })
    ]
})
export class CmsResourceLocationsModule {}
