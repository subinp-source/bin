/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { UpgradeModule } from '@angular/upgrade/static';
import { InjectionToken } from '@angular/core';

import { SeDowngradeService } from 'smarteditcommons/di';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:TestModeService
 *
 * @description
 * Used to determine whether smartedit is running in a e2e (test) mode
 */
/** @internal */
@SeDowngradeService()
export class TestModeService {
    // Constants
    private static readonly TEST_TOKEN: InjectionToken<boolean> = new InjectionToken<boolean>(
        'TEST_KEY_TOKEN'
    );

    private readonly TEST_KEY: string = 'e2eMode';

    constructor(private upgrade: UpgradeModule) {}

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:TestModeService#isE2EMode
     * @methodOf smarteditServicesModule.service:TestModeService
     *
     * @description
     * returns true if smartedit is running in e2e (test) mode
     *
     * @returns {Boolean} true/false
     */
    public isE2EMode(): boolean {
        return this.isE2EModeLegacy() || this.isE2EModeNg();
    }

    private isE2EModeNg(): boolean {
        try {
            return this.upgrade.injector.get(TestModeService.TEST_TOKEN);
        } catch (e) {
            return false;
        }
    }

    private isE2EModeLegacy(): boolean {
        return (
            this.upgrade.$injector &&
            this.upgrade.$injector.has(this.TEST_KEY) &&
            this.upgrade.$injector.get(this.TEST_KEY)
        );
    }
}
