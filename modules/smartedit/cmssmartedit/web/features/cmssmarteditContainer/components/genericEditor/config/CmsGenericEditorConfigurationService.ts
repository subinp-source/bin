/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    stringUtils,
    BooleanComponent,
    EditorFieldMappingService,
    GenericEditorField,
    GenericEditorStructure,
    GenericEditorTabService,
    SeInjectable
} from 'smarteditcommons';

@SeInjectable()
export class CmsGenericEditorConfigurationService {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------

    private readonly DEFAULT_PAGE_TAB_ID = 'information';
    private readonly CATEGORIES = {
        PAGE: 'PAGE',
        COMPONENT: 'COMPONENT'
    };
    constructor(
        private editorFieldMappingService: EditorFieldMappingService,
        private genericEditorTabService: GenericEditorTabService
    ) {}

    // --------------------------------------------------------------------------------------
    // Public Methods
    // --------------------------------------------------------------------------------------

    setDefaultEditorFieldMappings(): void {
        this.editorFieldMappingService.addFieldMapping('Media', null, null, {
            template: 'mediaTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('MediaContainer', null, null, {
            template: 'mediaContainerTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('NavigationNodeSelector', null, null, {
            template: 'navigationNodeSelectorWrapperTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('MultiProductSelector', null, null, {
            template: 'multiProductSelectorTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('MultiCategorySelector', null, null, {
            template: 'multiCategorySelectorTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('CMSLinkToSelect', null, null, {
            template: 'cmsLinkToSelectWrapperTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('SingleOnlineProductSelector', null, null, {
            template: 'singleActiveCatalogAwareItemSelectorWrapperTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('SingleOnlineCategorySelector', null, null, {
            template: 'singleActiveCatalogAwareItemSelectorWrapperTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping('CMSItemDropdown', null, null, {
            template: 'cmsItemDropdownWrapperTemplate.html'
        });

        this.editorFieldMappingService.addFieldMapping(
            'CMSComponentRestrictionsEditor',
            null,
            null,
            {
                template: 'componentRestrictionsEditorWrapperTemplate.html'
            }
        );

        this.editorFieldMappingService.addFieldMapping(
            'PageRestrictionsEditor',
            null,
            'restrictions',
            {
                template: 'pageRestrictionsEditorWrapperTemplate.html'
            }
        );

        // for editing modal only, not used for create/clone
        this.editorFieldMappingService.addFieldMapping(
            'DisplayConditionEditor',
            null,
            'displayCondition',
            {
                template: 'pageDisplayConditionWrapperTemplate.html',
                hidePrefixLabel: true
            }
        );

        this.editorFieldMappingService.addFieldMapping(
            'ShortString',
            this._isPagePredicate,
            'typeCode',
            {
                template: 'pageTypeEditorTemplate.html',
                hidePrefixLabel: true
            }
        );

        this.editorFieldMappingService.addFieldMapping(
            'InfoPageName',
            this._isPagePredicate,
            null,
            {
                template: 'infoPageNameWrapperTemplate.html'
            }
        );

        this.editorFieldMappingService.addFieldMapping(
            'PageInfoPageName',
            this._isPagePredicate,
            null,
            {
                template: 'pageInfoPageNameWrapperTemplate.html'
            }
        );

        this.editorFieldMappingService.addFieldMapping('Boolean', null, 'visible', {
            component: BooleanComponent,
            i18nKey: 'type.component.abstractcmscomponent.visible.name'
        });

        this.editorFieldMappingService.addFieldMapping('LinkToggle', null, null, {
            template: 'linkToggleWrapperTemplate.html',
            customSanitize(payload, sanitizeFn) {
                if (sanitizeFn === undefined) {
                    sanitizeFn = stringUtils.sanitize;
                }
                payload.urlLink = sanitizeFn(payload.urlLink);
                return payload;
            }
        });

        this.editorFieldMappingService.addFieldMapping('RestrictionsList', null, null, {
            template: 'restrictionsListWrapperTemplate.html',
            hidePrefixLabel: true
        });

        // Page restore widgets.
        this.editorFieldMappingService.addFieldMapping(
            'DuplicatePrimaryNonContentPageMessage',
            null,
            null,
            {
                template: 'duplicatePrimaryNonContentPageWrapperTemplate.html',
                hidePrefixLabel: true
            }
        );

        this.editorFieldMappingService.addFieldMapping('DuplicatePrimaryContentPage', null, null, {
            template: 'duplicatePrimaryContentPageWrapperTemplate.html',
            hidePrefixLabel: false
        });

        this.editorFieldMappingService.addFieldMapping('MissingPrimaryContentPage', null, null, {
            template: 'missingPrimaryContentPageWrapperTemplate.html',
            hidePrefixLabel: false
        });

        this.editorFieldMappingService.addFieldMapping('WorkflowCreateVersionField', null, null, {
            template: 'workflowCreateVersionFieldWrapperTemplate.html',
            hidePrefixLabel: false
        });
    }

    public setDefaultTabsConfiguration(): void {
        this.genericEditorTabService.configureTab('default', {
            priority: 5
        });
        this.genericEditorTabService.configureTab('information', {
            priority: 5
        });
        this.genericEditorTabService.configureTab('administration', {
            priority: 4
        });
    }

    setDefaultTabFieldMappings(): void {
        // Set default tab.
        this.genericEditorTabService.addComponentTypeDefaultTabPredicate(this._defaultTabPredicate);

        // Set tabs
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'visible',
            'visibility'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'restrictions',
            'visibility'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'onlyOneRestrictionMustApply',
            'visibility'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'uid',
            'basicinfo'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'id',
            'basicinfo'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isComponentPredicate,
            'modifiedtime',
            'basicinfo'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            'DateTime',
            this._isComponentPredicate,
            'creationtime',
            'basicinfo'
        );

        // Page Tabs
        this.editorFieldMappingService.addFieldTabMapping(
            'DisplayConditionEditor',
            this._isPagePredicate,
            'displayCondition',
            'displaycondition'
        );
        this.editorFieldMappingService.addFieldTabMapping(
            null,
            this._isPagePredicate,
            'restrictions',
            'restrictions'
        );
    }

    // Predicates

    private _defaultTabPredicate = (componentTypeStructure: GenericEditorStructure): string => {
        return componentTypeStructure.category === this.CATEGORIES.PAGE
            ? this.DEFAULT_PAGE_TAB_ID
            : null;
    };

    private _isPagePredicate = (
        componentType: string,
        field: GenericEditorField,
        componentTypeStructure: GenericEditorStructure
    ): boolean => {
        return componentTypeStructure.category === this.CATEGORIES.PAGE;
    };

    private _isComponentPredicate = (
        componentType: string,
        field: GenericEditorField,
        componentTypeStructure: GenericEditorStructure
    ): boolean => {
        return componentTypeStructure.category === this.CATEGORIES.COMPONENT;
    };
}
