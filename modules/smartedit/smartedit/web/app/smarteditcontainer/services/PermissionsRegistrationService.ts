/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IExperience,
    IPermissionService,
    ISharedDataService,
    PermissionContext,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService()
export class PermissionsRegistrationService {
    constructor(
        private permissionService: IPermissionService,
        private sharedDataService: ISharedDataService
    ) {}

    /**
     * Method containing registrations of rules and permissions to be used in smartedit workspace
     */
    public registerRulesAndPermissions(): void {
        // Rules
        this.permissionService.registerRule({
            names: ['se.slot.belongs.to.page'],
            verify: (permissionObjects: PermissionContext[]) =>
                this.sharedDataService
                    .get('experience')
                    .then(
                        (experience: IExperience) =>
                            experience.pageContext &&
                            experience.pageContext.catalogVersionUuid ===
                                permissionObjects[0].context.slotCatalogVersionUuid
                    )
        });

        // Permissions
        this.permissionService.registerPermission({
            aliases: ['se.slot.not.external'],
            rules: ['se.slot.belongs.to.page']
        });
    }
}
