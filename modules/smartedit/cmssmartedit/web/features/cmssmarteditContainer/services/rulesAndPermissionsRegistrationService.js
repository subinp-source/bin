/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('rulesAndPermissionsRegistrationModule', [
        'catalogVersionRestServiceModule',
        'pageServiceModule',
        'pageVersionsModule',
        'cmsSmarteditServicesModule',
        'smarteditServicesModule'
    ])
    .run(function(
        catalogService,
        catalogVersionPermissionService,
        catalogVersionRestService,
        pageService,
        permissionService,
        sharedDataService,
        cMSModesService,
        attributePermissionsRestService,
        typePermissionsRestService,
        experienceService,
        workflowService,
        $q,
        windowUtils
    ) {
        var onSuccess = function(result) {
            return (
                result.reduce(function(acc, val) {
                    return acc && val;
                }, true) === true
            );
        };
        var onError = function() {
            return false;
        };
        var getCurrentPageActiveWorkflow = function() {
            if (!windowUtils.getGatewayTargetFrame()) {
                return $q.resolve(null);
            }
            return pageService
                .getCurrentPageInfo()
                .then(function(pageInfo) {
                    return workflowService.getActiveWorkflowForPageUuid(pageInfo.uuid);
                })
                .then(function(workflow) {
                    return workflow;
                });
        };

        function registerRules() {
            permissionService.registerRule({
                names: [
                    'se.write.page',
                    'se.write.slot',
                    'se.write.component',
                    'se.write.to.current.catalog.version'
                ],
                verify: function(permissionNameObjs) {
                    var promises = permissionNameObjs.map(function(permissionNameObject) {
                        if (permissionNameObject.context) {
                            return catalogVersionPermissionService.hasWritePermission(
                                permissionNameObject.context.catalogId,
                                permissionNameObject.context.catalogVersion
                            );
                        } else {
                            return catalogVersionPermissionService.hasWritePermissionOnCurrent();
                        }
                    });
                    return $q.all(promises).then(onSuccess, onError);
                }
            });

            /**
             * This rule returns true if the page is in a workflow and current user is participant of this workflow
             * or there is no workflow.
             * Otherwise, it returns false;
             */
            permissionService.registerRule({
                names: ['se.current.user.can.act.on.page.in.workflow'],
                verify: function(permissionNameObjs) {
                    function isAvailableForCurrentPrincipal(workflow) {
                        return workflow === null ? true : workflow.isAvailableForCurrentPrincipal;
                    }

                    var promises = permissionNameObjs.map(function(permissionNameObject) {
                        if (permissionNameObject.context) {
                            return workflowService
                                .getActiveWorkflowForPageUuid(
                                    permissionNameObject.context.pageInfo.uuid
                                )
                                .then(function(workflow) {
                                    return isAvailableForCurrentPrincipal(workflow);
                                });
                        } else {
                            return getCurrentPageActiveWorkflow().then(
                                function(workflow) {
                                    return isAvailableForCurrentPrincipal(workflow);
                                },
                                function() {
                                    return true;
                                }
                            );
                        }
                    });
                    return $q.all(promises).then(onSuccess, onError);
                }
            });

            /**
             * This rule returns true if the current user is a participant of currently active step of a workflow
             * or there is no workflow.
             * Otherwise, it returns false;
             */
            permissionService.registerRule({
                names: ['se.current.user.can.act.on.workflow.current.action'],
                verify: function(permissionNameObjs) {
                    function isUserParticipant(workflow) {
                        return workflow === null
                            ? $q.when(true)
                            : workflowService.isUserParticipanInActiveAction(workflow.workflowCode);
                    }

                    var promises = permissionNameObjs.map(function(permissionNameObject) {
                        if (permissionNameObject.context) {
                            return workflowService
                                .getActiveWorkflowForPageUuid(
                                    permissionNameObject.context.pageInfo.uuid
                                )
                                .then(function(workflow) {
                                    return isUserParticipant(workflow);
                                });
                        } else {
                            return getCurrentPageActiveWorkflow().then(
                                function(workflow) {
                                    return isUserParticipant(workflow);
                                },
                                function() {
                                    return true;
                                }
                            );
                        }
                    });
                    return $q.all(promises).then(onSuccess, onError);
                }
            });

            permissionService.registerRule({
                names: ['se.sync.catalog'],
                verify: function(permissionNameObjs) {
                    var promises = permissionNameObjs.map(function(permissionNameObject) {
                        if (permissionNameObject.context) {
                            return catalogVersionPermissionService.hasSyncPermission(
                                permissionNameObject.context.catalogId,
                                permissionNameObject.context.catalogVersion,
                                permissionNameObject.context.targetCatalogVersion
                            );
                        } else {
                            return catalogVersionPermissionService.hasSyncPermissionFromCurrentToActiveCatalogVersion();
                        }
                    });
                    return $q.all(promises).then(onSuccess, onError);
                }
            });

            permissionService.registerRule({
                names: ['se.approval.status.page'],
                verify: function() {
                    return pageService.getCurrentPageInfo().then(function(pageInfo) {
                        return pageInfo.approvalStatus === 'APPROVED';
                    });
                }
            });

            permissionService.registerRule({
                names: [
                    'se.read.page',
                    'se.read.slot',
                    'se.read.component',
                    'se.read.current.catalog.version'
                ],
                verify: function() {
                    return catalogVersionPermissionService.hasReadPermissionOnCurrent();
                }
            });

            permissionService.registerRule({
                names: ['se.page.belongs.to.experience'],
                verify: function() {
                    return sharedDataService.get('experience').then(function(experience) {
                        return (
                            experience.pageContext &&
                            experience.pageContext.catalogVersionUuid ===
                                experience.catalogDescriptor.catalogVersionUuid
                        );
                    });
                }
            });

            /**
             * Show the clone icon:
             * - If a page belonging to an active catalog version is a primary page, whose copyToCatalogsDisabled flag is set to false and has at-least one clonable target.
             * - If a page belonging to a non active catalog version has at-least one clonable target.
             */
            permissionService.registerRule({
                names: ['se.cloneable.page'],
                verify: function() {
                    return sharedDataService.get('experience').then(function(experience) {
                        if (!experience.pageContext) {
                            return false;
                        }
                        var pageUriContext = {
                            CURRENT_CONTEXT_SITE_ID: experience.pageContext.siteId,
                            CURRENT_CONTEXT_CATALOG: experience.pageContext.catalogId,
                            CURRENT_CONTEXT_CATALOG_VERSION: experience.pageContext.catalogVersion
                        };

                        return pageService.getCurrentPageInfo().then(function(pageInfo) {
                            return catalogVersionRestService
                                .getCloneableTargets(pageUriContext)
                                .then(function(targets) {
                                    if (experience.pageContext.active) {
                                        return (
                                            targets.versions.length > 0 &&
                                            pageInfo.defaultPage &&
                                            !pageInfo.copyToCatalogsDisabled
                                        );
                                    }

                                    return targets.versions.length > 0;
                                });
                        });
                    });
                }
            });

            permissionService.registerRule({
                names: ['se.content.catalog.non.active'],
                verify: function() {
                    return catalogService.isContentCatalogVersionNonActive();
                }
            });

            permissionService.registerRule({
                names: ['se.not.versioning.perspective'],
                verify: function() {
                    return cMSModesService.isVersioningPerspectiveActive().then(function(isActive) {
                        return !isActive;
                    });
                }
            });

            permissionService.registerRule({
                names: ['se.version.page.selected'],
                verify: function() {
                    return experienceService.getCurrentExperience().then(function(experience) {
                        return !!experience.versionId;
                    });
                }
            });

            permissionService.registerRule({
                names: ['se.version.page.not.selected'],
                verify: function() {
                    return experienceService.getCurrentExperience().then(function(experience) {
                        return !experience.versionId;
                    });
                }
            });

            permissionService.registerRule({
                names: ['se.catalogversion.has.workflows.enabled'],
                verify: function() {
                    return workflowService.areWorkflowsEnabledOnCurrentCatalogVersion();
                }
            });

            permissionService.registerRule({
                names: ['se.current.page.has.active.workflow'],
                verify: function() {
                    return getCurrentPageActiveWorkflow().then(function(workflow) {
                        return workflow !== null;
                    });
                }
            });

            permissionService.registerRule({
                names: ['se.current.page.has.no.active.workflow'],
                verify: function() {
                    return getCurrentPageActiveWorkflow().then(function(workflow) {
                        return workflow === null;
                    });
                }
            });

            // Attribute Permissions
            permissionService.registerRule({
                names: ['se.has.change.permission.on.page.approval.status'],
                verify: function() {
                    var attributeName = 'approvalStatus';
                    return pageService
                        .getCurrentPageInfo()
                        .then(function(pageInfo) {
                            return attributePermissionsRestService.hasChangePermissionOnAttributesInType(
                                pageInfo.typeCode,
                                [attributeName]
                            );
                        })
                        .then(function(result) {
                            return result[attributeName];
                        });
                }
            });
        }

        function registerRulesForTypeCodeFromContext() {
            var registerTypePermissionRuleForTypeCodeFromContext = function(ruleName, verify) {
                permissionService.registerRule({
                    names: [ruleName],
                    verify: function(permissionNameObjs) {
                        var promises = permissionNameObjs.map(function(permissionNameObject) {
                            return verify([permissionNameObject.context.typeCode]).then(function(
                                UpdatePermission
                            ) {
                                return UpdatePermission[permissionNameObject.context.typeCode];
                            });
                        });
                        return $q.all(promises).then(onSuccess, onError);
                    }
                });
            };

            // check if the current user has change permission on the type provided part of the permission object
            registerTypePermissionRuleForTypeCodeFromContext(
                'se.has.change.permissions.on.type',
                typePermissionsRestService.hasUpdatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            // check if the current user has create permission on the type provided part of the permission object
            registerTypePermissionRuleForTypeCodeFromContext(
                'se.has.create.permissions.on.type',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            // check if the current user has remove permission on the type provided part of the permission object
            registerTypePermissionRuleForTypeCodeFromContext(
                'se.has.remove.permissions.on.type',
                typePermissionsRestService.hasDeletePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
        }

        function registerRulesForCurrentPage() {
            var registerTypePermissionRuleOnCurrentPage = function(ruleName, verify) {
                permissionService.registerRule({
                    names: [ruleName],
                    verify: function() {
                        return pageService.getCurrentPageInfo().then(function(pageInfo) {
                            return verify([pageInfo.typeCode]).then(function(permissionObject) {
                                return permissionObject[pageInfo.typeCode];
                            });
                        });
                    }
                });
            };

            // check if the current user has change permission on the page currently loaded
            registerTypePermissionRuleOnCurrentPage(
                'se.has.change.type.permissions.on.current.page',
                typePermissionsRestService.hasUpdatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            // check if the current user has create permission on the page currently loaded
            registerTypePermissionRuleOnCurrentPage(
                'se.has.create.type.permissions.on.current.page',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            // check if the current user has read permission on the page currently loaded
            registerTypePermissionRuleOnCurrentPage(
                'se.has.read.type.permissions.on.current.page',
                typePermissionsRestService.hasReadPermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
        }

        function registerRulesForTypeCode() {
            var registerTypePermissionRuleForTypeCode = function(ruleName, itemType, verify) {
                permissionService.registerRule({
                    names: [ruleName],
                    verify: function() {
                        return verify([itemType]).then(function(UpdatePermission) {
                            return UpdatePermission[itemType];
                        });
                    }
                });
            };

            // check if the current user has read/create/remove/change permission on the CMSVersion type
            registerTypePermissionRuleForTypeCode(
                'se.has.read.permission.on.version.type',
                'CMSVersion',
                typePermissionsRestService.hasReadPermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.create.permission.on.version.type',
                'CMSVersion',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.remove.permission.on.version.type',
                'CMSVersion',
                typePermissionsRestService.hasDeletePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.change.permission.on.version.type',
                'CMSVersion',
                typePermissionsRestService.hasUpdatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.create.permission.on.abstractcomponent.type',
                'AbstractCMSComponent',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.change.permission.on.contentslotforpage.type',
                'ContentSlotForPage',
                typePermissionsRestService.hasUpdatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            // check if current user has create/change permission on the Workflow type
            registerTypePermissionRuleForTypeCode(
                'se.has.create.permission.on.workflow.type',
                'Workflow',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.change.permission.on.workflow.type',
                'Workflow',
                typePermissionsRestService.hasUpdatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.read.permission.on.workflow.type',
                'Workflow',
                typePermissionsRestService.hasReadPermissionForTypes.bind(
                    typePermissionsRestService
                )
            );

            registerTypePermissionRuleForTypeCode(
                'se.has.create.permission.on.contentslot.type',
                'ContentSlot',
                typePermissionsRestService.hasCreatePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
            registerTypePermissionRuleForTypeCode(
                'se.has.delete.permission.on.contentslot.type',
                'ContentSlot',
                typePermissionsRestService.hasDeletePermissionForTypes.bind(
                    typePermissionsRestService
                )
            );
        }

        function registerRulesForTypeAndQualifier() {
            var registerAttributePermissionRuleForTypeAndQualifier = function(
                ruleName,
                itemType,
                qualifier,
                verify
            ) {
                permissionService.registerRule({
                    names: [ruleName],
                    verify: function() {
                        return verify(itemType, [qualifier]).then(function(data) {
                            return data[qualifier];
                        });
                    }
                });
            };

            registerAttributePermissionRuleForTypeAndQualifier(
                'se.has.change.permission.on.workflow.status',
                'Workflow',
                'status',
                attributePermissionsRestService.hasChangePermissionOnAttributesInType.bind(
                    attributePermissionsRestService
                )
            );
            registerAttributePermissionRuleForTypeAndQualifier(
                'se.has.change.permission.on.workflow.description',
                'Workflow',
                'description',
                attributePermissionsRestService.hasChangePermissionOnAttributesInType.bind(
                    attributePermissionsRestService
                )
            );
        }

        function registerPermissions() {
            permissionService.registerPermission({
                aliases: ['se.add.component'],
                rules: [
                    'se.write.slot',
                    'se.write.component',
                    'se.page.belongs.to.experience',
                    'se.has.change.type.permissions.on.current.page',
                    'se.current.user.can.act.on.page.in.workflow',
                    'se.current.user.can.act.on.workflow.current.action',
                    'se.has.create.permission.on.abstractcomponent.type'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.read.page'],
                rules: ['se.read.page']
            });

            permissionService.registerPermission({
                aliases: ['se.edit.page'],
                rules: ['se.write.page', 'se.current.user.can.act.on.page.in.workflow']
            });

            permissionService.registerPermission({
                aliases: ['se.sync.catalog'],
                rules: ['se.sync.catalog']
            });

            permissionService.registerPermission({
                aliases: ['se.sync.slot.context.menu', 'se.sync.slot.indicator'],
                rules: [
                    'se.sync.catalog',
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.sync.page'],
                rules: [
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.edit.navigation'],
                rules: ['se.write.component']
            });

            permissionService.registerPermission({
                aliases: ['se.context.menu.remove.component'],
                rules: [
                    'se.write.slot',
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.slot.context.menu.shared.icon', 'se.slot.context.menu.unshared.icon'],
                rules: ['se.read.slot', 'se.current.user.can.act.on.page.in.workflow']
            });

            permissionService.registerPermission({
                aliases: ['se.slot.context.menu.visibility'],
                rules: ['se.page.belongs.to.experience']
            });

            permissionService.registerPermission({
                aliases: ['se.clone.page'],
                rules: ['se.cloneable.page', 'se.has.create.type.permissions.on.current.page']
            });

            permissionService.registerPermission({
                aliases: ['se.context.menu.edit.component'],
                rules: [
                    'se.write.component',
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.context.menu.drag.and.drop.component'],
                rules: [
                    'se.write.slot',
                    'se.write.component',
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow',
                    'se.current.user.can.act.on.workflow.current.action',
                    'se.has.change.permission.on.contentslotforpage.type'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.edit.page.link', 'se.delete.page.menu'],
                rules: [
                    'se.write.page',
                    'se.page.belongs.to.experience',
                    'se.not.versioning.perspective',
                    'se.has.change.type.permissions.on.current.page',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.shared.slot.override.options'],
                rules: [
                    'se.write.page',
                    'se.page.belongs.to.experience',
                    'se.not.versioning.perspective',
                    'se.current.user.can.act.on.page.in.workflow',
                    'se.has.create.permission.on.contentslot.type'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.revert.to.shared.slot.link'],
                rules: [
                    'se.write.page',
                    'se.page.belongs.to.experience',
                    'se.not.versioning.perspective',
                    'se.current.user.can.act.on.page.in.workflow',
                    'se.has.delete.permission.on.contentslot.type'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.clone.component'],
                rules: [
                    'se.write.component',
                    'se.page.belongs.to.experience',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.edit.page.type', 'se.delete.page.type', 'se.restore.page.type'],
                rules: ['se.has.change.permissions.on.type']
            });

            permissionService.registerPermission({
                aliases: ['se.clone.page.type'],
                rules: ['se.has.create.permissions.on.type']
            });

            permissionService.registerPermission({
                aliases: ['se.permanently.delete.page.type'],
                rules: ['se.has.remove.permissions.on.type']
            });

            // Version
            permissionService.registerPermission({
                aliases: ['se.version.page'],
                rules: [
                    'se.write.page',
                    'se.page.belongs.to.experience',
                    'se.content.catalog.non.active',
                    'se.has.read.permission.on.version.type',
                    'se.has.read.type.permissions.on.current.page'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.edit.version.page'],
                rules: [
                    'se.write.to.current.catalog.version',
                    'se.has.change.permission.on.version.type',
                    'se.current.user.can.act.on.page.in.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.create.version.page'],
                rules: [
                    'se.version.page.not.selected',
                    'se.page.belongs.to.experience',
                    'se.has.create.permission.on.version.type',
                    'se.has.read.type.permissions.on.current.page'
                ]
            });

            var rulesForVersionRollback = [
                'se.version.page.selected',
                'se.page.belongs.to.experience',
                'se.has.read.permission.on.version.type',
                'se.has.create.permission.on.version.type',
                'se.has.change.type.permissions.on.current.page'
            ];
            permissionService.registerPermission({
                aliases: ['se.rollback.version.page'],
                rules: rulesForVersionRollback
            });

            permissionService.registerPermission({
                // the page versions menu button should be visible even if a version is not selected
                aliases: ['se.rollback.version.page.versions.menu'],
                rules: rulesForVersionRollback.filter(function(rule) {
                    return rule !== 'se.version.page.selected';
                })
            });

            permissionService.registerPermission({
                aliases: ['se.delete.version.page'],
                rules: ['se.has.remove.permission.on.version.type']
            });

            // Workflow
            permissionService.registerPermission({
                aliases: ['se.start.page.workflow'],
                rules: [
                    'se.has.create.permission.on.workflow.type',
                    'se.catalogversion.has.workflows.enabled',
                    'se.current.page.has.no.active.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.view.page.workflowMenu'],
                rules: [
                    'se.has.read.permission.on.workflow.type',
                    'se.current.page.has.active.workflow'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.cancel.page.workflowMenu'],
                rules: [
                    'se.has.change.permission.on.workflow.type',
                    'se.current.page.has.active.workflow',
                    'se.has.change.permission.on.workflow.status'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.edit.workflow.workflowMenu'],
                rules: [
                    'se.has.change.permission.on.workflow.type',
                    'se.current.page.has.active.workflow',
                    'se.has.change.permission.on.workflow.description'
                ]
            });

            permissionService.registerPermission({
                aliases: ['se.force.page.approval'],
                rules: ['se.sync.catalog', 'se.has.change.permission.on.page.approval.status']
            });

            permissionService.registerPermission({
                aliases: ['se.show.page.status'],
                rules: ['se.content.catalog.non.active']
            });

            permissionService.registerPermission({
                aliases: ['se.act.on.page.in.workflow'],
                rules: ['se.current.user.can.act.on.page.in.workflow']
            });
        }

        registerRules();
        registerRulesForTypeCodeFromContext();
        registerRulesForCurrentPage();
        registerRulesForTypeCode();
        registerRulesForTypeAndQualifier();
        registerPermissions();
    });
