/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    authorizationEvictionTag,
    rarelyChangingContent,
    Cached,
    IPermissionsRestServicePair,
    IPermissionsRestServiceResult,
    IRestService,
    IRestServiceFactory,
    ISessionService,
    SeInjectable,
    TypedMap
} from 'smarteditcommons';

/**
 * @ngdoc object
 * @name cmsSmarteditServicesModule.object:AttributePermissionNames
 * @description
 * An enum type representing available attribute permission names for a given item
 */
export enum AttributePermissionNames {
    READ = 'read',
    CHANGE = 'change'
}

/**
 * @description
 * Pair to keep track of an attribute and its enclosing type.
 */
interface TypeAttributePair {
    type: string;
    attribute: string;
}

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:AttributePermissionsRestService
 *
 * @description
 * Rest Service to retrieve attribute permissions.
 */
@SeInjectable()
export class AttributePermissionsRestService {
    private readonly ATTRIBUTE_PERMISSIONS_URI =
        '/permissionswebservices/v1/permissions/principals/:user/attributes';
    private attributePermissionsRestService: IRestService<IPermissionsRestServiceResult>;

    constructor(
        restServiceFactory: IRestServiceFactory,
        private sessionService: ISessionService,
        private $q: angular.IQService,
        private $log: angular.ILogService,
        private lodash: lo.LoDashStatic
    ) {
        this.attributePermissionsRestService = restServiceFactory.get<
            IPermissionsRestServiceResult
        >(this.ATTRIBUTE_PERMISSIONS_URI);
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:AttributePermissionsRestService#hasReadPermissionOnAttributesInType
     * @methodOf cmsSmarteditServicesModule.service:AttributePermissionsRestService
     *
     * @description
     * Determines if the current user has READ access to the given attributes in the given type.
     *
     * @param {String} type The type enclosing the attributes for which to evaluate their read permissions.
     * @param {String[]} attributeNames The names of the attributes for which to evaluate their read permissions.
     * @returns {angular.IPromise<TypedMap<boolean>>} A promise that resolves to a TypedMap object with key (the attribute name) and
     * value (true if the user has READ access to the type or false otherwise).
     */
    hasReadPermissionOnAttributesInType(
        type: string,
        attributeNames: string[]
    ): angular.IPromise<TypedMap<boolean>> {
        return this.getPermissionsForAttributesAndNameByType(
            type,
            attributeNames,
            AttributePermissionNames.READ
        );
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:AttributePermissionsRestService#hasChangePermissionOnAttributesInType
     * @methodOf cmsSmarteditServicesModule.service:AttributePermissionsRestService
     *
     * @description
     * Determines if the current user has CHANGE access to the given attributes in the given type.
     *
     * @param {String} type The type enclosing the attributes for which to evaluate their change permissions.
     * @param {String[]} attributeNames The names of the attributes for which to evaluate their change permissions.
     * @returns {angular.IPromise<TypedMap<boolean>>} A promise that resolves to a TypedMap object with key (the attribute name) and
     * value (true if the user has READ access to the type or false otherwise).
     */
    hasChangePermissionOnAttributesInType(
        type: string,
        attributeNames: string[]
    ): angular.IPromise<TypedMap<boolean>> {
        return this.getPermissionsForAttributesAndNameByType(
            type,
            attributeNames,
            AttributePermissionNames.CHANGE
        );
    }

    private getPermissionsForAttributesAndNameByType(
        type: string,
        attributes: string[],
        permissionName: string
    ): angular.IPromise<TypedMap<boolean>> {
        const convertedAttributeNames = attributes.map((attr: string) => type + '.' + attr);
        return this.getPermissionsForAttributesAndName(
            convertedAttributeNames,
            permissionName
        ).then((attributePermissionsByTypeMap: TypedMap<TypedMap<boolean>>) => {
            return attributePermissionsByTypeMap[type];
        });
    }

    private getPermissionsForAttributesAndName(
        attributes: string[],
        permissionName: string
    ): angular.IPromise<TypedMap<TypedMap<boolean>>> {
        return this.getAllPermissionsForAttributes(attributes).then(
            (response: IPermissionsRestServiceResult[]) => {
                const allPermissions = this.concatPermissionsNotFound(attributes, response);
                return allPermissions.reduce(
                    (
                        attributePermissionsByTypeMap: TypedMap<TypedMap<boolean>>,
                        permissionsResult: IPermissionsRestServiceResult
                    ) => {
                        if (permissionsResult.permissions) {
                            const typeAttributePair = this.parsePermissionsResultId(
                                permissionsResult.id
                            );
                            if (!attributePermissionsByTypeMap[typeAttributePair.type]) {
                                attributePermissionsByTypeMap[typeAttributePair.type] = {};
                            }

                            attributePermissionsByTypeMap[typeAttributePair.type][
                                typeAttributePair.attribute
                            ] = this.getPermissionByNameFromResult(
                                permissionsResult,
                                permissionName
                            );
                        }

                        return attributePermissionsByTypeMap;
                    },
                    {}
                );
            }
        );
    }

    private parsePermissionsResultId(id: string): TypeAttributePair {
        const tokens = id.split('.');
        if (tokens.length !== 2) {
            throw new Error(
                'AttributePermissionsRestService - Received invalid attribute permissions'
            );
        }

        return {
            type: tokens[0],
            attribute: tokens[1]
        };
    }

    private getPermissionByNameFromResult(
        permissionsResult: IPermissionsRestServiceResult,
        permissionName: string
    ): boolean {
        return JSON.parse(
            permissionsResult.permissions.find(
                (permission: IPermissionsRestServicePair) => permission.key === permissionName
            ).value
        );
    }

    private concatPermissionsNotFound(
        attributes: string[],
        permissionsFound: IPermissionsRestServiceResult[]
    ): IPermissionsRestServiceResult[] {
        const permissionKeysFound = permissionsFound.map(
            (permission: IPermissionsRestServiceResult) => permission.id
        );
        const permissionKeysNotFound = this.lodash.difference(attributes, permissionKeysFound);

        return permissionsFound.concat(
            permissionKeysNotFound.map((key: string) => {
                return {
                    id: key,
                    permissions: [
                        {
                            key: AttributePermissionNames.READ,
                            value: 'false'
                        },
                        {
                            key: AttributePermissionNames.CHANGE,
                            value: 'false'
                        }
                    ]
                };
            })
        );
    }

    /**
     * @internal
     *
     * This method retrieves ALL the permissions the current user has on the given attributes. Attributes are expected with the following format:
     * - type.attribute name
     * For example, for an attribute called approvalStatus within the type ContentPage, the given attribute must be:
     * - ContentPage.approvalStatus
     *
     * Note: This method is cached.
     *
     * @param attributes The list of attributes for which to retrieve permissions
     * @returns {angular.IPromise<IPermissionsRestServiceResult[]>} A promise that resolves to a list of IPermissionsRestServiceResult, each of which
     * represents the permissions of one of the given attributes.
     */
    @Cached({ actions: [rarelyChangingContent], tags: [authorizationEvictionTag] })
    private getAllPermissionsForAttributes(
        attributes: string[]
    ): angular.IPromise<IPermissionsRestServiceResult[]> {
        if (attributes.length <= 0) {
            return this.$q.when([]);
        }

        return this.$q.resolve(this.sessionService.getCurrentUsername()).then((user: string) => {
            if (!user) {
                return [];
            }

            return this.$q.when(
                this.attributePermissionsRestService
                    .get({
                        user,
                        attributes: attributes.join(','),
                        permissionNames:
                            AttributePermissionNames.CHANGE + ',' + AttributePermissionNames.READ
                    })
                    .then(
                        (response: any) => {
                            return response.permissionsList || [];
                        },
                        (error: any) => {
                            if (error) {
                                this.$log.error(
                                    `AttributePermissionsRestService - couldn't retrieve attribute permissions ${attributes}`
                                );
                            }
                            return this.$q.reject(error);
                        }
                    )
            );
        });
    }
}
