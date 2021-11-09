/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useValue:false */
/* forbiddenNameSpaces useFactory:false */
/* forbiddenNameSpaces useClass:false */
import * as angular from 'angular';
import { TypedMap } from '@smart/utils';

export interface SeDirectiveDefinition {
    selector?: string;
    inputs?: string[];
    // outputs?: string[]
    // host?: {...}
    providers?: SeProvider[];
    // exportAs?: string
    // queries?: {...},
    require?: string | string[] | TypedMap<string>;
    transclude?: boolean | TypedMap<string>;
    replace?: boolean;
    controllerAs?: string;
    template?: string;
    templateUrl?: string;
    scope?: boolean | { [boundProperty: string]: string };
}

export interface SeComponentDefinition extends SeDirectiveDefinition {
    custom?: boolean;
    // changeDetection?: ChangeDetectionStrategy
    // viewProviders?: Provider[]
    // moduleId?: string
    templateUrl?: string;
    template?: string;
    // styleUrls?: string[]
    // styles?: string[]
    // animations?: any[]
    // encapsulation?: ViewEncapsulation
    // interpolation?: [string, string]
    entryComponents?: SeComponentConstructor[];
    // preserveWhitespaces?: boolean
    // inherited from core/Directive
}

export interface SeModuleDefinition {
    declarations?: (SeDirectiveConstructor | SeComponentConstructor | SeFilterConstructor)[];
    entryComponents?: (SeDirectiveConstructor | SeComponentConstructor)[];
    imports?: (string | SeModuleConstructor | SeModuleWithProviders)[];
    providers?: SeProvider[];
    config?: (...args: any[]) => void;
    initialize?: (...args: any[]) => void;
}

export type SeFactory = (...arg: any[]) => any;

export type SeConstructor<T = any> = new (...arg: any[]) => T;

export interface SeModuleConstructor extends SeConstructor {
    // used by Smartedit DI
    moduleName?: string;
}

export interface SeDirectiveConstructor extends SeConstructor {
    directiveName?: string;
    definition?: angular.IDirective;
    // will be browsed by owning @SeModule
    providers?: SeProvider[];
}

export interface SeFilterConstructor extends SeConstructor {
    filterName?: string;
    transform: (...deps: any[]) => (...args: any[]) => any;
}

export interface SeComponentConstructor extends SeConstructor {
    componentName?: string;
    selector?: string;
    definition?: angular.IComponentOptions;
    // will be browsed by owning @SeModule
    entryComponents?: SeComponentConstructor[];
    providers?: SeProvider[];
}

export interface SeBaseProvider {
    provide: string;
    multi?: boolean;
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:SeValueProvider
 * @description
 * Configures an injectable value provider in a {@link smarteditServicesModule.object:@SeModule module}, component or directive.
 */
export interface SeValueProvider extends SeBaseProvider {
    /**
     * @ngdoc property
     * @name provide
     * @propertyOf smarteditServicesModule.interface:SeValueProvider
     * @description
     * The provider name.
     */
    /**
     * @ngdoc property
     * @name useValue
     * @propertyOf smarteditServicesModule.interface:SeValueProvider
     * @description
     * An instance value of the provider.
     */
    useValue: any;
    /**
     * @ngdoc property
     * @name multi (optional)
     * @propertyOf smarteditServicesModule.interface:SeValueProvider
     * @description
     * If set to true, an array of instances will be provided for the same provider name. Useful for
     * configuring a module by many modules.
     */
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:SeClassProvider
 * @description
 * Configures an injectable class provider in a {@link smarteditServicesModule.object:@SeModule module}, component or directive.
 */
export interface SeClassProvider extends SeBaseProvider {
    /**
     * @ngdoc property
     * @name provide
     * @propertyOf smarteditServicesModule.interface:SeClassProvider
     * @description
     * The provider name.
     */
    /**
     * @ngdoc property
     * @name useClass
     * @propertyOf smarteditServicesModule.interface:SeClassProvider
     * @description
     * A class to invoke of the provider.
     */
    useClass: SeConstructor;
    /**
     * @ngdoc property
     * @name multi (optional)
     * @propertyOf smarteditServicesModule.interface:SeClassProvider
     * @description
     * If set to true, an array of instances will be provided for the same provider name. Useful for
     * configuring a module by many modules.
     */
}

/**
 * @ngdoc object
 * @name smarteditServicesModule.interface:SeFactoryProvider
 * @description
 * Configures an injectable factory provider in a {@link smarteditServicesModule.object:@SeModule module}, component or directive.
 */
export interface SeFactoryProvider extends SeBaseProvider {
    /**
     * @ngdoc property
     * @name provide
     * @propertyOf smarteditServicesModule.interface:SeFactoryProvider
     * @description
     * The provider name.
     */
    /**
     * @ngdoc property
     * @name useFactory
     * @propertyOf smarteditServicesModule.interface:SeFactoryProvider
     * @description
     * A function to invoke the construction of the provider.
     */
    useFactory: SeFactory;
    /**
     * @ngdoc property
     * @name deps (optional)
     * @propertyOf smarteditServicesModule.interface:SeFactoryProvider
     * @description
     * A list of strings or referenced dependencies to be injected into the factory. The 'ngInject;' hint may be used
     * in replacement of this property.
     */
    deps?: (SeConstructor | SeFactory | string)[];
    /**
     * @ngdoc property
     * @name multi (optional)
     * @propertyOf smarteditServicesModule.interface:SeFactoryProvider
     * @description
     * If set to true, an array of instances will be provided for the same provider name. Useful for
     * configuring a module by many modules.
     */
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:SeModuleWithProviders
 * @description
 * The returning type of a configurable module.
 */
export interface SeModuleWithProviders {
    seModule: SeModuleConstructor;
    providers: SeProvider[];
}

export type SeProvider =
    | SeValueProvider
    | SeClassProvider
    | SeFactoryProvider
    | SeConstructor
    | SeFactory;
