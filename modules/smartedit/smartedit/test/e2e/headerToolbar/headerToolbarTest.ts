/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { HeaderToolbarComponentObject } from '../utils/components/HeaderToolbarComponentObject';
import { Page } from '../utils/components/Page';
import { LandingPagePageObject } from '../utils/pageObjects/LandingPagePageObject';

describe('Header Toolbar', () => {
    describe('in Storefront', () => {
        beforeEach(async () => {
            await Page.Actions.getAndWaitForWholeApp('test/e2e/headerToolbar/index.html');
        });

        it("GIVEN I am on the storefront and I am not an admin user THEN I don't expect to see configuration centre in header toolbar", async () => {
            await HeaderToolbarComponentObject.Assertions.assertConfigurationCenterIsAbsent();
        });

        it('GIVEN I am on the storefront WHEN I click on Logout on User Account dropdown on header toolbar THEN I expect to log out', async () => {
            await HeaderToolbarComponentObject.Actions.clickOnLogout();
            await HeaderToolbarComponentObject.Assertions.waitForUrlToMatch();
        });

        it('GIVEN I am on the storefront THEN I expect to see language selector in header toolbar', async () => {
            await HeaderToolbarComponentObject.Assertions.assertLanguageSelectorIsPresent(
                '[data-item-key="headerToolbar.languageSelectorTemplate"] button'
            );
        });
    });

    describe('in landing page', () => {
        beforeEach(async () => {
            await LandingPagePageObject.Actions.openAndBeReady();
        });

        it("GIVEN I am on the landing page and I am not an admin user THEN I don't expect to see configuration centre in header toolbar", async () => {
            await HeaderToolbarComponentObject.Assertions.assertConfigurationCenterIsAbsent();
        });

        it('GIVEN I am on the landing page WHEN I click on Logout on User Account dropdown on header toolbar THEN I expect to log out', async () => {
            await HeaderToolbarComponentObject.Actions.clickOnLogout();
            await HeaderToolbarComponentObject.Assertions.waitForUrlToMatch();
        });

        it('GIVEN I am on the landing page THEN I expect to see language selector in header toolbar', async () => {
            await HeaderToolbarComponentObject.Assertions.assertLanguageSelectorIsPresent(
                '[data-item-key="headerToolbar.languageSelectorTemplate"] button'
            );
        });
    });
});
