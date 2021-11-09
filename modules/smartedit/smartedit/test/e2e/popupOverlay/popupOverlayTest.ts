/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PopupOverlayObject } from './popupOverlayObject';

describe('Popup Overlay - ', () => {
    describe('Legacy -', () => {
        beforeEach(async () => {
            await PopupOverlayObject.Actions.navigate();
            await PopupOverlayObject.Actions.switchToLegacy();
        });

        it('WHEN trigger is set to "click" the popup opens after click', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from controller!');
        });

        it('WHEN trigger is set to "hover" the popup opens on mouseenter', async () => {
            await PopupOverlayObject.Actions.setTriggerToHover();
            await PopupOverlayObject.Actions.hoverOverAnchor();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from controller!');
        });

        it('WHEN trigger is set to "hover" the popup closes on mouseleave', async () => {
            await PopupOverlayObject.Actions.setTriggerToHover();
            await PopupOverlayObject.Actions.hoverOverAnchor();
            await PopupOverlayObject.Actions.hoverAwayAnchor();

            await PopupOverlayObject.Assertions.popupIsAbsent('Hello from controller!');
        });

        it('WHEN trigger is set to "always displayed" the popup is always open', async () => {
            await PopupOverlayObject.Actions.setTriggerToAlwaysDisplayed();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from controller!');
        });

        it('WHEN trigger is set to "always hidden" the popup is always hidden', async () => {
            await PopupOverlayObject.Actions.setTriggerToAlwaysHidden();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.popupIsAbsent('Hello from controller!');
        });

        it('WHEN popup is opened the callback message is visible', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.messageIsDisplayed('On Show');
        });

        it('WHEN popup is closed the callback message is visible', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.messageIsDisplayed('On Hide');
        });
    });
    describe('Angular -', () => {
        beforeEach(async () => {
            await PopupOverlayObject.Actions.navigate();
            await PopupOverlayObject.Actions.switchToAngular();
        });

        it('WHEN trigger is set to "click" the popup opens after click', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from component!');
        });

        it('WHEN trigger is set to "hover" the popup opens on mouseenter', async () => {
            await PopupOverlayObject.Actions.setTriggerToHover();
            await PopupOverlayObject.Actions.hoverOverAnchor();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from component!');
        });

        it('WHEN trigger is set to "hover" the popup closes on mouseleave', async () => {
            await PopupOverlayObject.Actions.setTriggerToHover();
            await PopupOverlayObject.Actions.hoverOverAnchor();
            await PopupOverlayObject.Actions.hoverAwayAnchor();

            await PopupOverlayObject.Assertions.popupIsAbsent('Hello from component!');
        });

        it('WHEN trigger is set to "always displayed" the popup is always open', async () => {
            await PopupOverlayObject.Actions.setTriggerToAlwaysDisplayed();

            await PopupOverlayObject.Assertions.popupIsPresent('Hello from component!');
        });

        it('WHEN trigger is set to "always hidden" the popup is always hidden', async () => {
            await PopupOverlayObject.Actions.setTriggerToAlwaysHidden();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.popupIsAbsent('Hello from component!');
        });

        it('WHEN popup is opened the callback message is visible', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.messageIsDisplayed('On Show');
        });

        it('WHEN popup is closed the callback message is visible', async () => {
            await PopupOverlayObject.Actions.setTriggerToClick();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();
            await PopupOverlayObject.Actions.clickPopupOverlayAnchor();

            await PopupOverlayObject.Assertions.messageIsDisplayed('On Hide');
        });
    });
});
