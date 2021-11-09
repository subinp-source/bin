/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { browser } from 'protractor';

import { Perspectives } from '../utils/components/Perspectives';
import { Storefront } from '../utils/components/Storefront';
import { SmartEditContractChangeListenerPageObject } from './smartEditContractChangeListenerPageObject';
import { SfBuilderComponentObject } from '../utils/components/SfBuilderComponentObject';

describe('DOM Observer -', () => {
    const SLOT_ID = 'slotWrapper';
    const SLOT_TYPE = 'ContentSlot';
    const RESIZE_SLOT_ADDED_ID = 'resizeSlotDomListenerTest';
    const RESIZE_SLOT_ID = 'topHeaderSlot';
    const NEW_COMPONENT_ID = 'asyncComponent';
    const NEW_COMPONENT_TYPE = 'componentType1';
    const RESIZE_COMPONENT_ALIAS = 'resizeComponentDomListenerTest';
    const SIMPLE_DIV_ELEMENT_ID = 'simpleDivElement';

    beforeEach(async () => {
        await browser.get('test/e2e/smartEditContractChangeListener/index.html');
        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );
    });

    afterEach(async () => {
        await SmartEditContractChangeListenerPageObject.Assertions.overlayAndStoreFrontAreSynced();
    });

    it('WHEN a new component is added and removed THEN the overlay is updated and both slot and component are decorated', async () => {
        await browser.switchToIFrame();
        await Storefront.Assertions.assertDecoratorShowsOnComponent(SLOT_ID, SLOT_TYPE, 'deco3');

        await SfBuilderComponentObject.Actions.addComponent(NEW_COMPONENT_ID, SLOT_ID);
        await Storefront.Assertions.assertComponentInOverlayPresent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            true
        );
        await Storefront.Assertions.assertDecoratorShowsOnComponent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            ['deco1', 'deco2', 'deco3']
        );

        await SfBuilderComponentObject.Actions.removeComponent(NEW_COMPONENT_ID, SLOT_ID);
        await Storefront.Assertions.assertComponentInOverlayPresent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            false
        );
        await Storefront.Assertions.assertDecoratorShowsOnComponent(SLOT_ID, SLOT_TYPE, 'deco3');
        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            ['deco1', 'deco2', 'deco3']
        );
    });

    it('WHEN a component is added and resized THEN both slot and component in overlay are resized and repositioned', async () => {
        await browser.switchToIFrame();

        await SfBuilderComponentObject.Actions.addComponent(RESIZE_COMPONENT_ALIAS, RESIZE_SLOT_ID);
        await SmartEditContractChangeListenerPageObject.Actions.enlargeComponent();

        const slotInStoreFront = await Storefront.Elements.getComponentById(RESIZE_SLOT_ID);
        const slotInOverlay = await Storefront.Elements.getComponentInOverlayById(
            RESIZE_SLOT_ID,
            SLOT_TYPE
        );

        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSameDimensions(
            slotInStoreFront,
            slotInOverlay
        );
        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSamePosition(
            slotInStoreFront,
            slotInOverlay
        );

        const newComponentInStoreFront = await Storefront.Elements.getComponentById(
            RESIZE_COMPONENT_ALIAS
        );
        const newComponentInOverlay = await Storefront.Elements.getComponentInOverlayById(
            RESIZE_COMPONENT_ALIAS,
            NEW_COMPONENT_TYPE
        );

        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSameDimensions(
            newComponentInStoreFront,
            newComponentInOverlay
        );
        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSamePosition(
            newComponentInStoreFront,
            newComponentInOverlay
        );
    });

    it('WHEN a slot with a component is added THEN both slot and component in overlay are resized and repositioned', async () => {
        // otherSlot
        await SfBuilderComponentObject.Actions.addComponent(RESIZE_SLOT_ADDED_ID);
        await SfBuilderComponentObject.Actions.addComponent(
            RESIZE_COMPONENT_ALIAS,
            RESIZE_SLOT_ADDED_ID
        );
        await SmartEditContractChangeListenerPageObject.Actions.enlargeComponent();

        const slotInStoreFront = await Storefront.Elements.getComponentById(RESIZE_SLOT_ADDED_ID);
        await Storefront.Actions.moveToComponent(RESIZE_SLOT_ADDED_ID, SLOT_TYPE);
        const slotInOverlay = await Storefront.Elements.getComponentInOverlayById(
            RESIZE_SLOT_ADDED_ID,
            SLOT_TYPE
        );

        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSameDimensions(
            slotInStoreFront,
            slotInOverlay
        );
        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSamePosition(
            slotInStoreFront,
            slotInOverlay
        );

        const newComponentInStoreFront = await Storefront.Elements.getComponentById(
            RESIZE_COMPONENT_ALIAS
        );
        const newComponentInOverlay = await Storefront.Elements.getComponentInOverlayById(
            RESIZE_COMPONENT_ALIAS,
            NEW_COMPONENT_TYPE
        );

        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSameDimensions(
            newComponentInStoreFront,
            newComponentInOverlay
        );
        await SmartEditContractChangeListenerPageObject.Assertions.elementsHaveSamePosition(
            newComponentInStoreFront,
            newComponentInOverlay
        );
    });

    it('WHEN a slot with component is removed, the overlay counterparts are removed and no decorator shows', async () => {
        await SfBuilderComponentObject.Actions.addComponent(RESIZE_SLOT_ADDED_ID);
        await SfBuilderComponentObject.Actions.addComponent(
            RESIZE_COMPONENT_ALIAS,
            RESIZE_SLOT_ADDED_ID
        );

        await Storefront.Actions.moveToComponent(RESIZE_SLOT_ADDED_ID, SLOT_TYPE);

        await SmartEditContractChangeListenerPageObject.Assertions.overlayAndStoreFrontAreSynced();

        await SfBuilderComponentObject.Actions.removeComponent(RESIZE_SLOT_ADDED_ID);

        await browser.switchToIFrame();

        await Storefront.Assertions.assertComponentInOverlayPresent(
            RESIZE_COMPONENT_ALIAS,
            NEW_COMPONENT_TYPE,
            false
        );
        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            RESIZE_COMPONENT_ALIAS,
            NEW_COMPONENT_TYPE,
            ['deco1', 'deco2', 'deco3']
        );

        await Storefront.Assertions.assertComponentInOverlayPresent(
            RESIZE_SLOT_ADDED_ID,
            SLOT_TYPE,
            false
        );
        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            RESIZE_SLOT_ADDED_ID,
            SLOT_TYPE,
            'deco3'
        );
    });

    it('WHEN a component mutates to another type, a new decorator is applied and the former removed', async () => {
        await SfBuilderComponentObject.Actions.addComponent(NEW_COMPONENT_ID, SLOT_ID);
        await SmartEditContractChangeListenerPageObject.Actions.toggleComponentType();

        await browser.switchToIFrame();

        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            ['deco1', 'deco2', 'deco3']
        );
        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            NEW_COMPONENT_ID,
            NEW_COMPONENT_TYPE,
            'deco2'
        );
    });

    it('WHEN deep-linking to another page THEN the DOM Observer notifies of the change', async () => {
        await browser.switchToIFrame();

        await SmartEditContractChangeListenerPageObject.Assertions.pageHasChanged(
            'paged changed to homepage'
        );

        await SmartEditContractChangeListenerPageObject.Actions.changePage();

        await browser.switchToIFrame();

        await SmartEditContractChangeListenerPageObject.Assertions.pageHasChanged(
            'paged changed to demo_storefront_page_id'
        );
        await Storefront.Assertions.assertDecoratorShowsOnComponent(
            'staticDummyComponent',
            NEW_COMPONENT_TYPE,
            'deco4'
        );
    });

    it('WHEN re-rendering a component THEN the component is still visible in the overlay', async () => {
        await SfBuilderComponentObject.Actions.rerenderComponent('component4', 'bottomHeaderSlot');
        await Storefront.Assertions.assertComponentInOverlayPresent(
            'component4',
            'componentType4',
            true
        );
    });

    it('GIVEN an simple div WHEN it is converted to component THEN it should be rendered', async () => {
        // GIVEN
        await SmartEditContractChangeListenerPageObject.Actions.createSimpleDivElement();
        await browser.switchToIFrame();
        await Storefront.Assertions.assertDecoratorDoesntShowOnComponent(
            SIMPLE_DIV_ELEMENT_ID,
            NEW_COMPONENT_TYPE,
            'deco1'
        );

        // WHEN
        await SmartEditContractChangeListenerPageObject.Actions.convertSimpleDivToComponent();
        await browser.switchToIFrame();

        // THEN
        await Storefront.Assertions.assertDecoratorShowsOnComponent(
            SIMPLE_DIV_ELEMENT_ID,
            NEW_COMPONENT_TYPE,
            'deco1'
        );
    });

    it('GIVEN component visible in overlay WHEN it is converted to simple div THEN it should be removed from overlay', async () => {
        // GIVEN
        await SmartEditContractChangeListenerPageObject.Actions.createSimpleDivElement();
        await SmartEditContractChangeListenerPageObject.Actions.convertSimpleDivToComponent();
        await Storefront.Assertions.assertComponentInOverlayPresent(
            SIMPLE_DIV_ELEMENT_ID,
            NEW_COMPONENT_TYPE,
            true
        );

        // WHEN
        await SmartEditContractChangeListenerPageObject.Actions.convertComponentToSimpleDiv();
        await browser.switchToIFrame();

        // THEN
        await Storefront.Assertions.assertComponentInOverlayPresent(
            SIMPLE_DIV_ELEMENT_ID,
            NEW_COMPONENT_TYPE,
            false
        );
    });
});
