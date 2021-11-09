/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface InternalFeature {
    /**
     * @ngdoc property
     * @name key
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The feature (could be IDecorator | IToolbarItem | IContextualMenuButton) key defined in the API
     */
    key: string;

    /**
     * @ngdoc property
     * @name nameI18nKey
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The i18n key that stores the feature name to be translated.
     */
    nameI18nKey?: string;

    /**
     * @ngdoc property
     * @name descriptionI18nKey
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The i18n key that stores the feature description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
     */
    descriptionI18nKey?: string;

    /**
     * @ngdoc property
     * @name permissions
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The list of permissions required to enable the feature.
     */
    permissions?: string[];
}
/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IFeature
 *
 * @description
 * IFeature - Interface for feature configuration DTO in FeatureService
 */

export interface IFeature extends InternalFeature {
    /**
     * @ngdoc property
     * @name enablingCallback
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The callback to be called when feature is enabled.
     */
    enablingCallback?: () => void;
    /**
     * @ngdoc property
     * @name disablingCallback
     * @propertyOf smarteditServicesModule.interface:IFeature
     * @description The callback to be called when feature is disbled.
     */
    disablingCallback?: () => void;
}
