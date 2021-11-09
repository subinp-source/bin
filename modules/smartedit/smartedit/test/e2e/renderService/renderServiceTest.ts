/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, ExpectedConditions } from 'protractor';

import { Decorators } from '../utils/components/Decorators';
import { Page } from '../utils/components/Page';
import { Perspectives } from '../utils/components/Perspectives';
import { Storefront } from '../utils/components/Storefront';
import { WhiteToolbarComponentObject } from '../utils/components/WhiteToolbarComponentObject';
import { ExperienceSelectorObject } from '../utils/components/ExperienceSelector';
import { InflectionPoint } from '../utils/components/InflectionPoint';
import { HotKeys } from '../utils/components/HotKeys';

describe('Component and Slot Rendering', () => {
    const EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER = 'Some dirtied content';
    const EXPECTED_CONTENT_1_AFTER_RENDER = 'test component 1';
    const EXPECTED_CONTENT_2_AFTER_RENDER = 'test component 2';

    // displaying the mocked 'render service' storefront with the 'ALL' perspective
    beforeEach(async () => {
        await Page.Actions.getAndWaitForWholeApp('test/e2e/renderService/index.html');
        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );
    });

    it('WHEN the user triggers a re-render from SmartEdit THEN the component is re-rendered with new content', async () => {
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_1_ID)
        );
        expect((await Storefront.Elements.component1()).getText()).toContain(
            EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER,
            'Expected component 1 content to contain default content'
        );
        expect((await Storefront.Elements.component1()).getText()).not.toContain(
            EXPECTED_CONTENT_1_AFTER_RENDER,
            'Expected component 1 not to contain re-rendered content'
        );
        await browser.click(
            await Decorators.Elements.renderDecorator(Storefront.Constants.COMPONENT_1_ID)
        );
        await browser.waitUntil(
            ExpectedConditions.presenceOf(await Storefront.Elements.component1()),
            'Timed out waiting for presence of component 1'
        );
        expect((await Storefront.Elements.component1()).getText()).not.toContain(
            EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER,
            'Expected component 1 content not to contain default content'
        );
        expect((await Storefront.Elements.component1()).getText()).toContain(
            EXPECTED_CONTENT_1_AFTER_RENDER,
            'Expected component 1 to contain re-rendered content'
        );
    });

    it('WHEN the user triggers a re-render from SmartEdit container THEN the component is re-rendered with new content', async () => {
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_1_ID)
        );
        expect((await Storefront.Elements.component1()).getText()).toContain(
            EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER,
            'Expected component 1 content to contain default content'
        );
        expect((await Storefront.Elements.component1()).getText()).not.toContain(
            EXPECTED_CONTENT_1_AFTER_RENDER,
            'Expected component 1 not to contain re-rendered content'
        );
        await browser.click(await WhiteToolbarComponentObject.Elements.renderButton());
        expect((await Storefront.Elements.component1()).getText()).not.toContain(
            EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER,
            'Expected component 1 content not to contain default content'
        );
        expect((await Storefront.Elements.component1()).getText()).toContain(
            EXPECTED_CONTENT_1_AFTER_RENDER,
            'Expected component 1 to contain re-rendered content'
        );
    });

    it('WHEN the user triggers a slot re-render from SmartEdit THEN the slot is re-rendered with content from the storefront', async () => {
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_1_ID)
        );
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_2_ID)
        );
        await browser.click(
            await Decorators.Elements.renderSlotDecorator(Storefront.Constants.TOP_HEADER_SLOT_ID)
        );
        await assertComponent1IsReRendered();
        await assertComponent2IsReRendered();
    });

    it('WHEN the user triggers a slot re-render from SmartEdit Container THEN the slot is re-rendered with content from the storefront', async () => {
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_1_ID)
        );
        await browser.click(
            await Decorators.Elements.dirtyContentDecorator(Storefront.Constants.COMPONENT_2_ID)
        );
        await browser.click(await WhiteToolbarComponentObject.Elements.renderSlotButton());
        await assertComponent1IsReRendered();
        await assertComponent2IsReRendered();
    });

    async function assertComponent1IsReRendered() {
        await browser.waitUntil(async () => {
            const componentText = await (await Storefront.Elements.component1()).getText();

            return (
                componentText.indexOf(EXPECTED_CONTENT_1_AFTER_RENDER) >= 0 &&
                componentText.indexOf(EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER) < 0
            );
        }, 'Expected component to re-render');
    }

    async function assertComponent2IsReRendered() {
        await browser.waitUntil(async () => {
            const componentText = await (await Storefront.Elements.component2()).getText();

            return (
                componentText.indexOf(EXPECTED_CONTENT_2_AFTER_RENDER) >= 0 &&
                componentText.indexOf(EXPECTED_COMPONENT_CONTENT_BEFORE_RENDER) < 0
            );
        }, 'Expected component to re-render');
    }
});

/* ---------
 [ hotkeys ]
 -------- */

// toggle of SE overlay

describe("Effects of 'mode switch' key press:", () => {
    // displaying the mocked 'render service' storefront with the 'ALL' perspective
    beforeEach(async () => {
        await Page.Actions.getAndWaitForWholeApp('test/e2e/renderService/index.html');
        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );
        await browser.waitForWholeAppToBeReady();
    });

    // overlay
    it(
        'GIVEN the user is not on the Preview perspective' +
            " WHEN the 'mode switch' hotkey gets pressed" +
            ' THEN the SE overlay is hidden and the hotkey notification is shown',
        async () => {
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(true);
            await HotKeys.Actions.pressHotKeyModeSwitch();
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(false);
            await HotKeys.Assertions.assertHotkeyNotificationPresence();
            await HotKeys.Actions.pressHotKeyModeSwitch();
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(true);
            await HotKeys.Assertions.assertHotkeyNotificationAbsence();
        }
    );

    // overlay after navigation
    it(
        'GIVEN the user is not on the Preview perspective' +
            " WHEN the 'mode switch' hotkey gets pressed and the user navigates to another page" +
            ' THEN the SE overlay remains hidden after navigation',
        async () => {
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(true);
            await HotKeys.Actions.pressHotKeyModeSwitch();
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(false);
            await Storefront.Actions.deepLink();
            await Storefront.Assertions.assertSmartEditOverlayDisplayed(false);
        }
    );

    // experience panel
    it(
        'GIVEN the user is not on the Preview perspective and the experience panel is opened' +
            " WHEN the 'mode switch' hotkey gets pressed" +
            ' THEN the experience panel gets closed',
        async () => {
            await ExperienceSelectorObject.Actions.widget.openExperienceSelector();
            await browser.waitForPresence(
                ExperienceSelectorObject.Elements.widget.getExperienceMenu()
            );
            await HotKeys.Actions.pressHotKeyModeSwitch();
            await browser.waitForAbsence(
                ExperienceSelectorObject.Elements.widget.getExperienceMenu()
            );
        }
    );

    // inflection point selector
    it(
        'GIVEN the user is not on the Preview perspective' +
            " WHEN the inflection point menu is opened and the user press the 'mode switch' hotkey" +
            ' THEN the inflection point menu gets closed',
        async () => {
            await InflectionPoint.Actions.openInflectionPointMenu();
            expect(await InflectionPoint.Elements.getInflectionPointMenu().isDisplayed()).toBe(
                true
            );
            await HotKeys.Actions.pressHotKeyModeSwitch();
            expect(await InflectionPoint.Elements.getInflectionPointMenu().isDisplayed()).toBe(
                false
            );
        }
    );

    // perspective selector
    it(
        'GIVEN the user is not on the Preview perspective and the perspective selector is opened' +
            " WHEN the 'mode switch' hotkey gets pressed" +
            ' THEN the perspective selector gets closed',
        async () => {
            await Perspectives.Actions.openPerspectiveSelectorDropdown();
            await Perspectives.Assertions.assertPerspectiveSelectorDropdownDisplayed(true);
            await HotKeys.Actions.pressHotKeyModeSwitch();
            await Perspectives.Assertions.assertPerspectiveSelectorDropdownIsAbsent();
        }
    );
});
