/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CollapsiblePageObject } from './collapsiblePageObject';

describe('Collapsible Container - ', () => {
    beforeEach(async () => {
        await CollapsiblePageObject.Actions.navigate();
    });

    describe('Icon Alignment', async () => {
        it('has icon alignment right', async () => {
            await CollapsiblePageObject.Assertions.iconAlignedRight();
        });

        it('has icon alignment left', async () => {
            await CollapsiblePageObject.Actions.clickIconAlignentLeft();

            await CollapsiblePageObject.Assertions.iconAlignedLeft();
        });
    });

    describe('Icon Visibility', () => {
        it('icon is visible', async () => {
            await CollapsiblePageObject.Assertions.iconIsVisible();
        });

        it('icon is not visible', async () => {
            await CollapsiblePageObject.Actions.clickIconInvisible();

            await CollapsiblePageObject.Assertions.iconIsInvisible();
        });
    });

    describe('Header content', () => {
        it('shows text content', async () => {
            await CollapsiblePageObject.Assertions.headerTextVisible();
        });

        it('shows html content', async () => {
            await CollapsiblePageObject.Actions.clickTitleHtml();

            await CollapsiblePageObject.Assertions.headerHtmlVisible();
        });

        it('shows no content', async () => {
            await CollapsiblePageObject.Actions.clickTitleNone();

            await CollapsiblePageObject.Assertions.headerNoneVisible();
        });
    });

    describe('Content visibility', () => {
        it('content is not visible', async () => {
            await CollapsiblePageObject.Assertions.contentIsInvisible();
        });

        it('content is visible after header click', async () => {
            await CollapsiblePageObject.Actions.clickHeader();

            await CollapsiblePageObject.Assertions.contentIsVisible();
        });

        it('content is visible after icon click', async () => {
            await CollapsiblePageObject.Actions.clickHeaderButton();

            await CollapsiblePageObject.Assertions.contentIsVisible();
        });

        it('content is not visible after icon click and then header click', async () => {
            await CollapsiblePageObject.Actions.clickHeaderButton();
            await CollapsiblePageObject.Actions.clickHeader();

            await CollapsiblePageObject.Assertions.contentIsInvisible();
        });

        it('content is not visible after header click and then icon click', async () => {
            await CollapsiblePageObject.Actions.clickHeader();
            await CollapsiblePageObject.Actions.clickHeaderButton();

            await CollapsiblePageObject.Assertions.contentIsInvisible();
        });
    });
});
