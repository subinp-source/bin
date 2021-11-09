/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

export namespace CatalogDetailsPageObject {
    export const Actions = {
        async openAndBeReady(): Promise<void> {
            await browser.get('test/e2e/catalogDetails/index.html');
            await browser.waitForContainerToBeReady();
        }
    };
}
