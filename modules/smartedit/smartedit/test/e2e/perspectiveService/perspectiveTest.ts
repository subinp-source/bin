/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { Perspectives } from '../utils/components/Perspectives';
import { Storefront } from '../utils/components/Storefront';
import { WhiteToolbarComponentObject } from '../utils/components/WhiteToolbarComponentObject';
import { Login } from '../utils/components/Login';
import { Page } from '../utils/components/Page';
import { HotKeys } from '../utils/components/HotKeys';

const somePerspective = 'somenameI18nKey';
const permissionsPerspective = 'permissionsI18nKey';
const userRestrictedPerspective = 'userRestrictedPerspectiveI18nKey';

describe('Perspectives', () => {
    beforeEach(async () => {
        await browser.executeScript(
            'window.sessionStorage.setItem("HAS_CONFIGURATIONS", arguments[0])',
            true
        );
        await Perspectives.Actions.openAndBeReady();
    });

    afterEach(async () => {
        await browser.switchToParent();
        await browser.clearLocalStorage();
    });

    it('GIVEN application started in default perspective WHEN deep linked THEN default perspective is still selected and component is not present in the overlay', async () => {
        await Perspectives.Assertions.assertPerspectiveActive();

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            false
        );

        await HotKeys.Assertions.assertHotkeyTooltipIconPresent(false);

        await browser.switchToIFrame();
        await browser.pressKey('ESCAPE');
        await Perspectives.Assertions.assertSmarteditOverlayIsPresent();
        await browser.switchToParent();

        // Act
        await Storefront.Actions.deepLink();

        // Assert
        await Perspectives.Assertions.assertPerspectiveActive();

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_2_ID,
            Storefront.Constants.COMPONENT_2_TYPE,
            false
        );

        await Storefront.Assertions.assertComponentHtmlContains(
            Storefront.Constants.COMPONENT_2_ID,
            Storefront.Constants.COMPONENT_2_ID
        );
    });

    it('WHEN new perspective is selected THEN features are activated and decorators are present in overlay', async () => {
        // Act
        await Perspectives.Actions.selectPerspective(somePerspective);

        // Assert
        await Perspectives.Assertions.assertPerspectiveActive();

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            true
        );

        await Storefront.Assertions.assertComponentInOverlayContains(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            'Text_is_been_displayed_TextDisplayDecorator'
        );
    });

    it('IF application is not in preview mode THEN hotkey tooltip icon is present', async () => {
        // Act
        await Perspectives.Actions.selectPerspective(somePerspective);

        // Assert
        await Perspectives.Assertions.assertPerspectiveActive();
        await HotKeys.Assertions.assertHotkeyTooltipIconPresent(true);

        await browser.switchToIFrame();
        await browser.pressKey('ESCAPE');
        await Perspectives.Assertions.assertSmarteditOverlayIsAbsent();
        await browser.switchToParent();
    });

    it('GIVEN application started in default perspective, features are off; WHEN new perspective is selected AND permission service returns true for inner feature THEN feature whose permissions were registered from the inner app is present in overlay and feature is present in outer application', async () => {
        // WHEN
        await togglePerspectiveSessionStorage(true);
        await Perspectives.Actions.selectPerspective(permissionsPerspective);

        // THEN
        await Storefront.Assertions.assertComponentInOverlayContains(
            Storefront.Constants.COMPONENT_3_ID,
            Storefront.Constants.COMPONENT_3_TYPE,
            'Test permission component'
        );

        await Storefront.Assertions.assertComponentInOverlayContains(
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE,
            'Test permission decorator registered inner'
        );

        await WhiteToolbarComponentObject.Assertions.assertButtonPresent('Some Item');
    });

    it('GIVEN application started in default perspective, features are off; WHEN new perspective is selected AND permission service returns false for inner feature THEN feature whose permissions were registered from the inner app is not present in overlay and feature is not present in outer application', async () => {
        // WHEN
        await Perspectives.Actions.selectPerspective(somePerspective);
        await Perspectives.Assertions.assertPerspectiveActive();

        await togglePerspectiveSessionStorage(false);
        await Perspectives.Actions.selectPerspective(permissionsPerspective);

        // THEN
        await Storefront.Assertions.assertComponentInOverlayNotContains(
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE,
            'Test permission decorator registered inner'
        );

        await Storefront.Assertions.assertComponentInOverlayNotContains(
            Storefront.Constants.COMPONENT_3_ID,
            Storefront.Constants.COMPONENT_3_TYPE,
            'Test permission component'
        );

        await WhiteToolbarComponentObject.Assertions.assertButtonNotPresent('Some Item');
    });

    it('GIVEN application started in some perspective, features are on; WHEN new perspective is selected and permission service returns true THEN feature from some perspective is not present and feature new perspective is present', async () => {
        // GIVEN
        await Perspectives.Actions.selectPerspective(somePerspective);
        await Perspectives.Assertions.assertPerspectiveActive();
        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            true
        );
        await togglePerspectiveSessionStorage(true);

        // WHEN
        await Perspectives.Actions.selectPerspective(permissionsPerspective);

        // THEN
        await Storefront.Assertions.assertComponentInOverlayNotContains(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            'true'
        );
        await WhiteToolbarComponentObject.Assertions.assertButtonPresent('Some Item');
    });

    async function togglePerspectiveSessionStorage(enabled: boolean) {
        await browser.switchToIFrame();

        await setPerspectiveSessionStorage(enabled);
        await browser.switchToParent();
        await setPerspectiveSessionStorage(enabled);
    }
    async function setPerspectiveSessionStorage(enabled: boolean) {
        await browser.executeScript(
            'window.sessionStorage.setItem("PERSPECTIVE_SERVICE_RESULT", arguments[0])',
            enabled
        );
    }
});

describe('Perspectives with user switch', () => {
    beforeEach(async () => {
        await browser.executeScript(
            'window.sessionStorage.setItem("HAS_CONFIGURATIONS", arguments[0])',
            false
        );
        await Page.Actions.getAndWaitForLogin('test/e2e/perspectiveService/index.html');
        await Login.Actions.loginAsAdmin();
        await setHasAccessRestrictedPerspective(true);
    });

    afterEach(async () => {
        await browser.switchToParent();
        await browser.clearLocalStorage();
        await Page.Actions.clearCookies();
    });

    it('GIVEN a user has access to a perspective AND he access it and log out WHEN he log in again THEN the perspective is still selected', async () => {
        await Perspectives.Actions.selectPerspective(userRestrictedPerspective);
        await Perspectives.Assertions.assertPerspectiveActive();

        await Login.Actions.logoutUser();

        await Perspectives.Actions.refreshAndWaitForAngularEnabled();

        await Page.Actions.waitForLoginModal();

        await Login.Actions.loginAsAdmin();

        await Perspectives.Assertions.assertPerspectiveActive();
    });

    it('GIVEN a user has access to a perspective AND he access it and log out WHEN another user with no access to that perspective logs in THEN the default perspective should be selected', async () => {
        await Perspectives.Actions.selectPerspective(userRestrictedPerspective);
        await Perspectives.Assertions.assertPerspectiveActive();

        await Login.Actions.logoutUser();

        await Page.Actions.getAndWaitForLogin('test/e2e/perspectiveService/index.html');

        await Login.Actions.loginAsCmsManager();

        await setHasAccessRestrictedPerspective(false);

        await Perspectives.Assertions.assertPerspectiveActive();
    });

    async function setHasAccessRestrictedPerspective(enabled: boolean): Promise<void> {
        await browser.switchToIFrame();
        await browser.executeScript(
            'window.sessionStorage.setItem("HAS_RESTRICTED_PERSPECTIVE", arguments[0])',
            enabled
        );
        await Perspectives.Actions.openAndBeReady();
    }
});
