/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { LoginDialogResource } from '@smart/utils';

export const SMARTEDITLOADER_COMPONENT_NAME = 'smarteditloader-component';
export const SMARTEDITCONTAINER_COMPONENT_NAME = 'smarteditcontainer-component';
export const SMARTEDIT_COMPONENT_NAME = 'smartedit-component';

export const ELEMENT_UUID_ATTRIBUTE = 'data-smartedit-element-uuid';
export const ID_ATTRIBUTE = 'data-smartedit-component-id';
export const TYPE_ATTRIBUTE = 'data-smartedit-component-type';
export const NG_ROUTE_PREFIX = 'ng';

export const EXTENDED_VIEW_PORT_MARGIN = 1000;

export const CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
export const CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
export const CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

export const PAGE_CONTEXT_CATALOG = 'CURRENT_PAGE_CONTEXT_CATALOG';
export const PAGE_CONTEXT_CATALOG_VERSION = 'CURRENT_PAGE_CONTEXT_CATALOG_VERSION';
export const PAGE_CONTEXT_SITE_ID = 'CURRENT_PAGE_CONTEXT_SITE_ID';

export const SHOW_SLOT_MENU = '_SHOW_SLOT_MENU';
export const HIDE_SLOT_MENU = 'HIDE_SLOT_MENU';

export const OVERLAY_DISABLED_EVENT = 'OVERLAY_DISABLED';
export const USER_GLOBAL_PERMISSIONS_RESOURCE_URI =
    '/permissionswebservices/v1/permissions/principals/:user/global';

export const DEFAULT_LANGUAGE = 'en_US';
export const CLOSE_CTX_MENU = 'CLOSE_CTX_MENU';

export enum MUTATION_CHILD_TYPES {
    ADD_OPERATION = 'addedNodes',
    REMOVE_OPERATION = 'removedNodes'
}
/*
 * Mutation object (return in a list of mutations in mutation event) can be of different types.
 * We are here only interested in type attributes (used for onPageChanged and onComponentChanged events) and childList (used for onComponentAdded events)
 */
export const MUTATION_TYPES = {
    CHILD_LIST: {
        NAME: 'childList',
        ADD_OPERATION: MUTATION_CHILD_TYPES.ADD_OPERATION,
        REMOVE_OPERATION: MUTATION_CHILD_TYPES.REMOVE_OPERATION
    },
    ATTRIBUTES: {
        NAME: 'attributes'
    }
};

/**
 * @ngdoc object
 * @name smarteditServicesModule.object:CATALOG_VERSION_PERMISSIONS_RESOURCE_URI
 *
 * @description
 * Path to fetch permissions of a given catalog version.
 */
export const CATALOG_VERSION_PERMISSIONS_RESOURCE_URI_CONSTANT =
    '/permissionswebservices/v1/permissions/principals/:principal/catalogs';

export const OPERATION_CONTEXT = {
    BACKGROUND_TASKS: 'Background Tasks',
    INTERACTIVE: 'Interactive',
    NON_INTERACTIVE: 'Non-Interactive',
    BATCH_OPERATIONS: 'Batch Operations',
    TOOLING: 'Tooling',
    CMS: 'CMS'
};

export const I18N_RESOURCE_URI = '/smarteditwebservices/v1/i18n/translations';

/**
 * @ngdoc object
 * @name seConstantsModule.object:WHOAMI_RESOURCE_URI
 *
 * @description
 * Resource URI of the WhoAmI REST service used to retrieve information on the
 * current logged-in user.
 */
export const WHO_AM_I_RESOURCE_URI = '/authorizationserver/oauth/whoami';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_CLIENT_ID
 *
 * @description
 * The default OAuth 2 client id to use during authentication.
 */
export const DEFAULT_AUTHENTICATION_CLIENT_ID = 'smartedit';
export const SSO_AUTHENTICATION_ENTRY_POINT = '/samlsinglesignon/saml';
export const SSO_OAUTH2_AUTHENTICATION_ENTRY_POINT = '/smartedit/authenticate';
export const SSO_LOGOUT_ENTRY_POINT = '/samlsinglesignon/saml/logout';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PREVIEW_RESOURCE_URI
 *
 * @description
 * Path of the preview ticket API
 */
export const PREVIEW_RESOURCE_URI = '/previewwebservices/v1/preview';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CMSWEBSERVICES_PATH
 *
 * @description
 * Regular expression identifying CMS related URIs
 */
export const CMSWEBSERVICES_PATH = /\/cmssmarteditwebservices|\/cmswebservices/;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP
 *
 * @description
 * to calculate platform domain URI, this regular expression will be used
 */
export const SMARTEDIT_RESOURCE_URI_REGEXP = /^(.*)\/smartedit/;

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:SMARTEDIT_ROOT
 *
 * @description
 * the name of the webapp root context
 */
export const SMARTEDIT_ROOT = 'smartedit';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONFIGURATION_URI
 *
 * @description
 * The SmartEdit configuration API root
 */
export const CONFIGURATION_URI = '/smartedit/configuration';

export const SETTINGS_URI = '/smartedit/settings';
export const EVENT_NOTIFICATION_CHANGED = 'EVENT_NOTIFICATION_CHANGED';

export enum SortDirections {
    Ascending = 'asc',
    Descending = 'desc'
}

export const REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT = 'REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT';
export const PREVIOUS_USERNAME_HASH = 'previousUsername';

export const SMARTEDIT_LOGIN_DIALOG_RESOURCES: LoginDialogResource = {
    topLogoURL: 'static-resources/images/SAP_R_grad.svg',
    bottomLogoURL: 'static-resources/images/best-run-sap-logo.svg'
};
export {
    DEFAULT_AUTHENTICATION_ENTRY_POINT,
    EVENTS,
    I18N_ROOT_RESOURCE_URI,
    DEFAULT_AUTH_MAP,
    DEFAULT_CREDENTIALS_MAP,
    DEFAULT_LANGUAGE_ISO,
    LANDING_PAGE_PATH,
    SELECTED_LANGUAGE,
    SWITCH_LANGUAGE_EVENT
} from '@smart/utils';

export const EVENT_PERSPECTIVE_CHANGED = 'EVENT_PERSPECTIVE_CHANGED';
export const EVENT_PERSPECTIVE_UNLOADING = 'EVENT_PERSPECTIVE_UNLOADING';
export const EVENT_PERSPECTIVE_REFRESHED = 'EVENT_PERSPECTIVE_REFRESHED';
export const EVENT_PERSPECTIVE_ADDED = 'EVENT_PERSPECTIVE_ADDED';
export const EVENT_PERSPECTIVE_UPDATED = 'EVENT_PERSPECTIVE_UPDATED';

export const EVENT_STRICT_PREVIEW_MODE_REQUESTED = 'EVENT_STRICT_PREVIEW_MODE_REQUESTED';

export const PERSPECTIVE_SELECTOR_WIDGET_KEY = 'perspectiveToolbar.perspectiveSelectorTemplate';

export const EVENT_SMARTEDIT_COMPONENT_UPDATED = 'EVENT_SMARTEDIT_COMPONENT_UPDATED';

export const OVERLAY_ID = 'smarteditoverlay';

export const EVENT_OUTER_FRAME_CLICKED = 'EVENT_OUTER_FRAME_CLICKED';
export const CATALOG_VERSION_UUID_ATTRIBUTE = 'data-smartedit-catalog-version-uuid';
export const COMPONENT_CLASS = 'smartEditComponent';
export const CONTAINER_ID_ATTRIBUTE = 'data-smartedit-container-id';
export const CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS = {
    PROCESS: 'processComponent',
    REMOVE: 'removeComponent',
    KEEP_VISIBLE: 'keepComponentVisible'
};
export const CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS = {
    PROCESS_COMPONENTS: 'contractChangeListenerProcessComponents',
    RESTART_PROCESS: 'contractChangeListenerRestartProcess'
};
export const OVERLAY_RERENDERED_EVENT = 'overlayRerendered';
export const SMARTEDIT_ATTRIBUTE_PREFIX = 'data-smartedit-';
export const SMARTEDIT_COMPONENT_PROCESS_STATUS = 'smartEditComponentProcessStatus';
export const UUID_ATTRIBUTE = 'data-smartedit-component-uuid';
export const OVERLAY_COMPONENT_CLASS = 'smartEditComponentX';
export const CONTENT_SLOT_TYPE = 'ContentSlot';
export const CONTAINER_TYPE_ATTRIBUTE = 'data-smartedit-container-type';
export const LANGUAGE_RESOURCE_URI = '/cmswebservices/v1/sites/:siteUID/languages';
export const I18N_LANGUAGES_RESOURCE_URI = '/smarteditwebservices/v1/i18n/languages';

// Generic Editor

export const GENERIC_EDITOR_LOADED_EVENT = 'genericEditorLoadedEvent';
export const GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT =
    'UnrelatedValidationMessagesEvent';
export const VALIDATION_MESSAGE_TYPES = {
    VALIDATION_ERROR: 'ValidationError',
    WARNING: 'Warning'
};

export const ENUM_RESOURCE_URI = '/cmswebservices/v1/enums';

// Dropdown

export const LINKED_DROPDOWN = 'LinkedDropdown';
export const SITES_RESOURCE_URI = '/cmswebservices/v1/sites';

export const DATE_CONSTANTS = {
    ANGULAR_FORMAT: 'short',
    MOMENT_FORMAT: 'M/D/YY h:mm A',
    MOMENT_ISO: 'YYYY-MM-DDTHH:mm:00ZZ',
    ISO: 'yyyy-MM-ddTHH:mm:00Z',
    ANGULAR_SHORT: 'M/d/yy h:mm a'
};

export const CATALOG_DETAILS_COLUMNS = {
    LEFT: 'left',
    RIGHT: 'right'
};

export const TYPES_RESOURCE_URI = '/cmswebservices/v1/types';
export const STORE_FRONT_CONTEXT = '/storefront';
export const PRODUCT_RESOURCE_API =
    '/cmssmarteditwebservices/v1/sites/:siteUID/products/:productUID';
export const PRODUCT_LIST_RESOURCE_API =
    '/cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:catalogVersion/products';

export const HIDE_TOOLBAR_ITEM_CONTEXT = 'HIDE_TOOLBAR_ITEM_CONTEXT';
export const SHOW_TOOLBAR_ITEM_CONTEXT = 'SHOW_TOOLBAR_ITEM_CONTEXT';

export const SMARTEDIT_DRAG_AND_DROP_EVENTS = {
    DRAG_DROP_CROSS_ORIGIN_START: 'DRAG_DROP_CROSS_ORIGIN_START',
    DRAG_DROP_START: 'EVENT_DRAG_DROP_START',
    DRAG_DROP_END: 'EVENT_DRAG_DROP_END',
    TRACK_MOUSE_POSITION: 'EVENT_TRACK_MOUSE_POSITION',
    DROP_ELEMENT: 'EVENT_DROP_ELEMENT'
};

export const NONE_PERSPECTIVE = 'se.none';
export const ALL_PERSPECTIVE = 'se.all';

export const SEND_MOUSE_POSITION_THROTTLE = 100;
export const THROTTLE_SCROLLING_DELAY = 70;

export const SMARTEDIT_ELEMENT_HOVERED = 'smartedit-element-hovered';
export const SCROLL_AREA_CLASS = 'ySECmsScrollArea';
export const SMARTEDIT_IFRAME_DRAG_AREA = 'ySmartEditFrameDragArea';
export const DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME = {
    START: 'START',
    END: 'END'
};

export const SMARTEDIT_IFRAME_WRAPPER_ID = '#js_iFrameWrapper';
export const HEART_BEAT_TIMEOUT_THRESHOLD_MS = 10000;

export const EVENT_CONTENT_CATALOG_UPDATE = 'EVENT_CONTENT_CATALOG_UPDATE';
