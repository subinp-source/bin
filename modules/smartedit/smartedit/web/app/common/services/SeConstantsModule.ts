/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import './rest/resourceLocations';

import { SeModule } from 'smarteditcommons/di';
import {
    ALL_PERSPECTIVE,
    ELEMENT_UUID_ATTRIBUTE,
    EVENT_CONTENT_CATALOG_UPDATE,
    EVENT_SMARTEDIT_COMPONENT_UPDATED,
    EVENTS,
    HIDE_TOOLBAR_ITEM_CONTEXT,
    ID_ATTRIBUTE,
    NONE_PERSPECTIVE,
    OPERATION_CONTEXT,
    PERSPECTIVE_SELECTOR_WIDGET_KEY,
    REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
    SCROLL_AREA_CLASS,
    SHOW_TOOLBAR_ITEM_CONTEXT,
    SMARTEDIT_DRAG_AND_DROP_EVENTS,
    SMARTEDIT_ELEMENT_HOVERED,
    SMARTEDIT_IFRAME_DRAG_AREA,
    SMARTEDIT_IFRAME_WRAPPER_ID,
    TYPE_ATTRIBUTE,
    WHO_AM_I_RESOURCE_URI
} from 'smarteditcommons/utils/smarteditconstants';
import {
    moduleUtils,
    CATALOG_VERSION_UUID_ATTRIBUTE,
    CONTAINER_TYPE_ATTRIBUTE,
    CONTENT_SLOT_TYPE,
    CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS,
    CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS,
    DATE_CONSTANTS,
    EVENT_OUTER_FRAME_CLICKED,
    EVENT_PERSPECTIVE_ADDED,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    EVENT_PERSPECTIVE_UNLOADING,
    EVENT_PERSPECTIVE_UPDATED,
    OVERLAY_COMPONENT_CLASS,
    OVERLAY_ID,
    OVERLAY_RERENDERED_EVENT,
    SortDirections,
    UUID_ATTRIBUTE
} from '../utils';

/**
 * @ngdoc overview
 * @name seConstantsModule
 * @description
 * The seConstantsModule module contains Smartedit's global constants.
 */
@SeModule({
    providers: [
        ...moduleUtils.provideValues({
            CTX_MENU_DROPDOWN_IS_OPEN: 'CTX_MENU_DROPDOWN_IS_OPEN',
            CLOSE_CTX_MENU: 'CLOSE_CTX_MENU',
            SHOW_SLOT_MENU: '_SHOW_SLOT_MENU',
            HIDE_SLOT_MENU: 'HIDE_SLOT_MENU'
        }),
        /**
         * @ngdoc object
         * @name seConstantsModule.OVERLAY_ID
         * @description
         * the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.
         */
        {
            provide: 'OVERLAY_ID',
            useValue: OVERLAY_ID
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.COMPONENT_CLASS
         * @description
         * the css class of the smartEdit components as per contract with the storefront
         */
        {
            provide: 'COMPONENT_CLASS',
            useValue: 'smartEditComponent'
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.OVERLAY_COMPONENT_CLASS
         * @description
         * the css class of the smartEdit component clones copied to the storefront overlay
         */
        {
            provide: 'OVERLAY_COMPONENT_CLASS',
            useValue: OVERLAY_COMPONENT_CLASS
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.SMARTEDIT_ATTRIBUTE_PREFIX
         * @description
         * If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value
         */
        {
            provide: 'SMARTEDIT_ATTRIBUTE_PREFIX',
            useValue: 'data-smartedit-'
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.ID_ATTRIBUTE
         * @description
         * the id attribute of the smartEdit components as per contract with the storefront
         */
        {
            provide: 'ID_ATTRIBUTE',
            useValue: ID_ATTRIBUTE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.UUID_ATTRIBUTE
         * @description
         * the uuid attribute of the smartEdit components as per contract with the storefront
         */
        {
            provide: 'UUID_ATTRIBUTE',
            useValue: UUID_ATTRIBUTE
        },
        /**
         * @description
         * the front-end randomly generated uuid of the smartEdit components and their equivalent in the overlay
         */
        {
            provide: 'ELEMENT_UUID_ATTRIBUTE',
            useValue: ELEMENT_UUID_ATTRIBUTE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.UUID_ATTRIBUTE
         * @description
         * the uuid attribute of the smartEdit components as per contract with the storefront
         */
        {
            provide: 'CATALOG_VERSION_UUID_ATTRIBUTE',
            useValue: CATALOG_VERSION_UUID_ATTRIBUTE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.TYPE_ATTRIBUTE
         * @description
         * the type attribute of the smartEdit components as per contract with the storefront
         */
        {
            provide: 'TYPE_ATTRIBUTE',
            useValue: TYPE_ATTRIBUTE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.CONTAINER_ID_ATTRIBUTE
         * @description
         * the id attribute of the smartEdit container, when applicable, as per contract with the storefront
         */
        {
            provide: 'CONTAINER_ID_ATTRIBUTE',
            useValue: 'data-smartedit-container-id'
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.CONTAINER_TYPE_ATTRIBUTE
         * @description
         * the type attribute of the smartEdit container, when applicable, as per contract with the storefront
         */
        {
            provide: 'CONTAINER_TYPE_ATTRIBUTE',
            useValue: CONTAINER_TYPE_ATTRIBUTE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.CONTENT_SLOT_TYPE
         * @description
         * the type value of the smartEdit slots as per contract with the storefront
         */
        {
            provide: 'CONTENT_SLOT_TYPE',
            useValue: CONTENT_SLOT_TYPE
        },
        {
            provide: 'SMARTEDIT_IFRAME_WRAPPER_ID',
            useValue: SMARTEDIT_IFRAME_WRAPPER_ID
        },
        {
            provide: 'SMARTEDIT_IFRAME_DRAG_AREA',
            useValue: SMARTEDIT_IFRAME_DRAG_AREA
        },
        {
            provide: 'EVENT_TOGGLE_SMARTEDIT_IFRAME_DRAG_AREA',
            useValue: 'EVENT_TOGGLE_SMARTEDIT_IFRAME_DRAG_AREA'
        },
        {
            provide: 'SCROLL_AREA_CLASS',
            useValue: SCROLL_AREA_CLASS
        },
        {
            provide: 'SMARTEDIT_ELEMENT_HOVERED',
            useValue: SMARTEDIT_ELEMENT_HOVERED
        },
        {
            provide: 'SMARTEDIT_DRAG_AND_DROP_EVENTS',
            useValue: SMARTEDIT_DRAG_AND_DROP_EVENTS
        },
        {
            provide: 'DATE_CONSTANTS',
            useValue: DATE_CONSTANTS
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_CONTENT_CATALOG_UPDATE
         * @description
         * The ID of the event that is triggered when the content of a catalog is
         * updated (by page edit or page deletion).
         */
        {
            provide: EVENT_CONTENT_CATALOG_UPDATE,
            useValue: EVENT_CONTENT_CATALOG_UPDATE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_PERSPECTIVE_CHANGED
         * @description
         * The ID of the event that is triggered when the perspective (known as mode for users) is changed.
         */
        {
            provide: EVENT_PERSPECTIVE_CHANGED,
            useValue: EVENT_PERSPECTIVE_CHANGED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_PERSPECTIVE_ADDED
         * @description
         * The ID of the event that is triggered when a new perspective (known as mode for users) is registered.
         */
        {
            provide: EVENT_PERSPECTIVE_ADDED,
            useValue: EVENT_PERSPECTIVE_ADDED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_PERSPECTIVE_UPDATED
         * @description
         * The ID of the event that is triggered when a perspective (known as mode for users) is updated.
         * The perspective is updated when features, permissions or perspectives attributes are changed.
         */
        {
            provide: EVENT_PERSPECTIVE_UPDATED,
            useValue: EVENT_PERSPECTIVE_UPDATED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_PERSPECTIVE_UNLOADING
         * @description
         * The ID of the event that is triggered when a perspective is about to be unloaded.
         * This event is triggered immediately before the features are disabled.
         */
        {
            provide: EVENT_PERSPECTIVE_UNLOADING,
            useValue: EVENT_PERSPECTIVE_UNLOADING
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_PERSPECTIVE_REFRESHED
         * @description
         * The ID of the event that is triggered when the perspective (known as mode for users) is refreshed.
         */
        {
            provide: EVENT_PERSPECTIVE_REFRESHED,
            useValue: EVENT_PERSPECTIVE_REFRESHED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:ALL_PERSPECTIVE
         * @description
         * The key of the default All Perspective.
         */
        {
            provide: 'ALL_PERSPECTIVE',
            useValue: ALL_PERSPECTIVE
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:NONE_PERSPECTIVE
         * @description
         * The key of the default None Perspective.
         */
        {
            provide: 'NONE_PERSPECTIVE',
            useValue: NONE_PERSPECTIVE
        },
        {
            provide: 'PERSPECTIVE_SELECTOR_WIDGET_KEY',
            useValue: PERSPECTIVE_SELECTOR_WIDGET_KEY
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES
         * @description
         * Validation message types
         */
        {
            provide: 'VALIDATION_MESSAGE_TYPES',
            useValue: {
                /**
                 * @ngdoc property
                 * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES#VALIDATION_ERROR
                 * @propertyOf seConstantsModule.object:VALIDATION_MESSAGE_TYPES
                 * @description
                 * Validation error type.
                 */
                VALIDATION_ERROR: 'ValidationError',
                /**
                 * @ngdoc property
                 * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES#WARNING
                 * @propertyOf seConstantsModule.object:VALIDATION_MESSAGE_TYPES
                 * @description
                 * Validation warning type.
                 */
                WARNING: 'Warning'
            }
        },
        {
            provide: 'CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS',
            useValue: CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS
        },
        {
            provide: 'SMARTEDIT_COMPONENT_PROCESS_STATUS',
            useValue: 'smartEditComponentProcessStatus'
        },
        {
            provide: 'CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS',
            useValue: CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:SORT_DIRECTIONS
         * @description
         * Sort directions
         */
        {
            provide: 'SORT_DIRECTIONS',
            useValue: {
                /**
                 * @ngdoc property
                 * @name seConstantsModule.object:SORT_DIRECTIONS#ASC
                 * @propertyOf seConstantsModule.object:SORT_DIRECTIONS
                 * @description
                 * Sort direction - Ascending
                 */
                ASC: SortDirections.Ascending,
                /**
                 * @ngdoc property
                 * @name seConstantsModule.object:SORT_DIRECTIONS#DESC
                 * @propertyOf seConstantsModule.object:SORT_DIRECTIONS
                 * @description
                 * Sort direction - Descending
                 */
                DESC: SortDirections.Descending
            }
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_OUTER_FRAME_CLICKED
         * @description
         * The event that triggeres when user clicks on the outer frame.
         */
        {
            provide: EVENT_OUTER_FRAME_CLICKED,
            useValue: EVENT_OUTER_FRAME_CLICKED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT
         * @description
         * Name of the event triggered whenever SmartEdit decides to update items in contextual menus.
         */
        {
            provide: REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
            useValue: REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:SHOW_TOOLBAR_ITEM_CONTEXT
         * @description
         * The event that is used to show the toolbar item context.
         */
        {
            provide: 'SHOW_TOOLBAR_ITEM_CONTEXT',
            useValue: SHOW_TOOLBAR_ITEM_CONTEXT
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:HIDE_TOOLBAR_ITEM_CONTEXT
         * @description
         * The event that is used to hide the toolbar item context.
         */
        {
            provide: 'HIDE_TOOLBAR_ITEM_CONTEXT',
            useValue: HIDE_TOOLBAR_ITEM_CONTEXT
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_NOTIFICATION_CHANGED
         *
         * @description
         * The ID of the event that is triggered when a notification is pushed or removed.
         */
        {
            provide: 'EVENT_NOTIFICATION_CHANGED',
            useValue: 'EVENT_NOTIFICATION_CHANGED'
        },
        {
            provide: 'OVERLAY_RERENDERED_EVENT',
            useValue: OVERLAY_RERENDERED_EVENT
        },
        {
            provide: 'WHO_AM_I_RESOURCE_URI',
            useValue: WHO_AM_I_RESOURCE_URI
        },
        /**
         * @ngdoc object
         * @deprecated import EVENTS from smarteditconstants.ts
         * @name seConstantsModule.object:EVENTS
         *
         * @description
         * Events that are fired/handled in the SmartEdit application
         */
        {
            provide: 'EVENTS',
            useValue: EVENTS
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:SELECTED_LANGUAGE
         *
         * @description
         * A constant that is used as key to store the selected language in the storageService
         */
        {
            provide: 'SELECTED_LANGUAGE',
            useValue: 'SELECTED_LANGUAGE'
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:SWITCH_LANGUAGE_EVENT
         *
         * @description
         * A constant that is used as key to publish and receive events when a language is changed.
         */
        {
            provide: 'SWITCH_LANGUAGE_EVENT',
            useValue: 'SWITCH_LANGUAGE_EVENT'
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:EVENT_SMARTEDIT_COMPONENT_UPDATED
         *
         * @description
         * A constant that is used as key to publish and receive events when a component is updated.
         */
        {
            provide: 'EVENT_SMARTEDIT_COMPONENT_UPDATED',
            useValue: EVENT_SMARTEDIT_COMPONENT_UPDATED
        },
        /**
         * @ngdoc object
         * @name seConstantsModule.object:OPERATION_CONTEXT
         *
         * @description
         * Injectable angular constant<br/>
         * This object provides an enumeration of operation context for the application.
         */
        {
            provide: 'OPERATION_CONTEXT',
            useValue: OPERATION_CONTEXT
        }
    ]
})
export class SeConstantsModule {}
