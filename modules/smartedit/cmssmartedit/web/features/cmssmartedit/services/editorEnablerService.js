/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name editorEnablerServiceModule
 * @description
 * # The editorEnablerServiceModule
 *
 * The editor enabler service module provides a service that allows enabling the Edit Component contextual menu item,
 * providing a SmartEdit CMS admin the ability to edit various properties of the given component.
 */
angular
    .module('editorEnablerServiceModule', [
        'componentVisibilityAlertServiceModule',
        'slotRestrictionsServiceModule',
        'cmsSmarteditServicesModule'
    ])

    /**
     * @ngdoc service
     * @name editorEnablerServiceModule.service:editorEnablerService
     *
     * @description
     * Convenience service to attach the open editor modal action to the contextual menu of a given component type, or
     * given regex corresponding to a selection of component types.
     *
     * Example: The Edit button is added to the contextual menu of the CMSParagraphComponent, and all types postfixed
     * with BannerComponent.
     *
     * <pre>
     angular.module('app', ['editorEnablerServiceModule'])
     .run(function(editorEnablerService) {
                editorEnablerService.enableForComponents(['CMSParagraphComponent', '*BannerComponent']);
            });
     * </pre>
     */
    .factory('editorEnablerService', function(
        COMPONENT_UPDATED_EVENT,
        systemEventService,
        componentVisibilityAlertService,
        componentHandlerService,
        editorModalService,
        featureService,
        slotRestrictionsService
    ) {
        // Class Definition
        function EditorEnablerService() {
            this._editButtonCallback = this._editButtonCallback.bind(this);
        }

        // Private

        EditorEnablerService.prototype._key = 'se.cms.edit';

        EditorEnablerService.prototype._nameI18nKey = 'se.cms.contextmenu.nameI18nKey.edit';

        EditorEnablerService.prototype._i18nKey = 'se.cms.contextmenu.title.edit';

        EditorEnablerService.prototype._descriptionI18nKey =
            'se.cms.contextmenu.descriptionI18n.edit';

        EditorEnablerService.prototype._editDisplayClass = 'editbutton';

        EditorEnablerService.prototype._editDisplayIconClass = 'sap-icon--edit';

        EditorEnablerService.prototype._editDisplaySmallIconClass = 'sap-icon--edit';

        EditorEnablerService.prototype._editButtonCallback = function(contextualMenuParams) {
            var slotUuid = contextualMenuParams.slotUuid;

            if (!this.inUse) {
                this.inUse = true;
                editorModalService.open(contextualMenuParams.componentAttributes).then(
                    function(item) {
                        this.inUse = false;
                        systemEventService.publish(COMPONENT_UPDATED_EVENT, item);
                        return componentVisibilityAlertService.checkAndAlertOnComponentVisibility({
                            itemId: item.uuid,
                            itemType: item.itemtype,
                            catalogVersion: item.catalogVersion,
                            restricted: item.restricted,
                            slotId: slotUuid,
                            visible: item.visible
                        });
                    }.bind(this),
                    function() {
                        this.inUse = false;
                    }.bind(this)
                );
            }
        };

        EditorEnablerService.prototype._condition = function(contextualMenuParams) {
            var slotId = componentHandlerService.getParentSlotForComponent(
                contextualMenuParams.element
            );

            return slotRestrictionsService.isSlotEditable(slotId).then(function(isSlotEditable) {
                return (
                    isSlotEditable &&
                    !componentHandlerService.isExternalComponent(
                        contextualMenuParams.componentId,
                        contextualMenuParams.componentType
                    )
                );
            });
        };

        // Public
        /**
         * @ngdoc method
         * @name editorEnablerServiceModule.service:editorEnablerService#enableForComponents
         * @methodOf editorEnablerServiceModule.service:editorEnablerService
         *
         * @description
         * Enables the Edit contextual menu item for the given component types.
         *
         * @param {Array} componentTypes The list of component types, as defined in the platform, for which to enable the
         * Edit contextual menu.
         */
        EditorEnablerService.prototype.enableForComponents = function(componentTypes) {
            var contextualMenuConfig = {
                key: this._key, // It's the same key as the i18n, however here we're not doing any i18n work.
                nameI18nKey: this._nameI18nKey,
                descriptionI18nKey: this._descriptionI18nKey,
                priority: 400,
                regexpKeys: componentTypes,
                displayClass: this._editDisplayClass,
                displayIconClass: this._editDisplayIconClass,
                displaySmallIconClass: this._editDisplaySmallIconClass,
                i18nKey: this._i18nKey,
                condition: this._condition,
                permissions: ['se.context.menu.edit.component'],
                action: {
                    callback: this._editButtonCallback
                }
            };

            featureService.addContextualMenuButton(contextualMenuConfig);
        };

        // Factory Definition
        return new EditorEnablerService();
    });
