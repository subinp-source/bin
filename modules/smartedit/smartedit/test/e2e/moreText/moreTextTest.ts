/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { MoreTextPageObject } from './moreTextObject';

describe('yTextMore', () => {
    const COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT = 'yMoreTextLimitLessThenText';
    const COMPONENT_ID_WITH_TEXT_LIMIT_MORE_THEN_TEXT = 'yMoreTextLimitMoreThenText';
    const COMPONENT_ID_WITH_CUSTOM_LINKS = 'yMoreTextWithCustomLinks';
    const TEXT = 'hello, how are you? What time is it now?';
    const TRUNCATED_TEXT = 'hello, how';
    const ELLIPSIS = '.....';

    beforeEach(async () => {
        await MoreTextPageObject.Actions.openAndBeReady();
        await browser.waitUntilNoModal();
    });

    describe('Angular - ', () => {
        beforeEach(async () => {
            MoreTextPageObject.Elements.Mode = 'ng';
        });

        it(
            'GIVEN a yTextMore component with a text containing more than 10 characters AND limit is 10 AND custom ellipsis' +
                'WHEN the MoreLink is clicked ' +
                'THEN the yTextMore shows the full text AND the button with "LessLink" title AND custom ellipsis is not shown',
            async () => {
                await browser.waitUntilNoModal();
                // GIVEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TRUNCATED_TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'MoreLink'
                );
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    ELLIPSIS
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );

                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'LessLink'
                );
            }
        );

        it(
            'GIVEN a yTextMore component shows the full text AND the button with "LessLink" title is shown AND limit is 10' +
                'WHEN the LessLink is clicked ' +
                'THEN the yTextMore shows truncated text AND the button with "MoreLink" title',
            async () => {
                // GIVEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'LessLink'
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );

                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TRUNCATED_TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'MoreLink'
                );
            }
        );

        it(
            'GIVEN a yTextMore component shows the full text AND limit is more then the text length ' +
                'THEN the yTextMore does not show any button',
            async () => {
                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_MORE_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonIsAbsent(
                    COMPONENT_ID_WITH_TEXT_LIMIT_MORE_THEN_TEXT
                );
            }
        );

        it(
            'GIVEN a yTextMore component with a text containing more than 10 characters AND limit is 10 AND the button with custom MoreLink title is shown ' +
                'WHEN the MoreLink is clicked ' +
                'THEN the yTextMore shows the full text AND the button with custom LessLink title',
            async () => {
                // GIVEN
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_CUSTOM_LINKS,
                    'CustomLinkMore'
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(COMPONENT_ID_WITH_CUSTOM_LINKS);

                // THEN
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_CUSTOM_LINKS,
                    'CustomLinkLess'
                );
            }
        );
    });

    describe('Legacy - ', () => {
        beforeEach(async () => {
            MoreTextPageObject.Elements.Mode = 'legacy';
        });

        it(
            'GIVEN a yTextMore component with a text containing more than 10 characters AND limit is 10 AND custom ellipsis' +
                'WHEN the MoreLink is clicked ' +
                'THEN the yTextMore shows the full text AND the button with "LessLink" title AND custom ellipsis is not shown',
            async () => {
                await browser.waitUntilNoModal();
                // GIVEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TRUNCATED_TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'MoreLink'
                );
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    ELLIPSIS
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );

                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'LessLink'
                );
            }
        );

        it(
            'GIVEN a yTextMore component shows the full text AND the button with "LessLink" title is shown AND limit is 10' +
                'WHEN the LessLink is clicked ' +
                'THEN the yTextMore shows truncated text AND the button with "MoreLink" title',
            async () => {
                // GIVEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'LessLink'
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT
                );

                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    TRUNCATED_TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_TEXT_LIMIT_LESS_THEN_TEXT,
                    'MoreLink'
                );
            }
        );

        it(
            'GIVEN a yTextMore component shows the full text AND limit is more then the text length ' +
                'THEN the yTextMore does not show any button',
            async () => {
                // THEN
                await MoreTextPageObject.Assertions.assertComponentContainsText(
                    COMPONENT_ID_WITH_TEXT_LIMIT_MORE_THEN_TEXT,
                    TEXT
                );
                await MoreTextPageObject.Assertions.assertButtonIsAbsent(
                    COMPONENT_ID_WITH_TEXT_LIMIT_MORE_THEN_TEXT
                );
            }
        );

        it(
            'GIVEN a yTextMore component with a text containing more than 10 characters AND limit is 10 AND the button with custom MoreLink title is shown ' +
                'WHEN the MoreLink is clicked ' +
                'THEN the yTextMore shows the full text AND the button with custom LessLink title',
            async () => {
                // GIVEN
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_CUSTOM_LINKS,
                    'CustomLinkMore'
                );

                // WHEN
                await MoreTextPageObject.Actions.clickOnButton(COMPONENT_ID_WITH_CUSTOM_LINKS);

                // THEN
                await MoreTextPageObject.Assertions.assertButtonContainsTitle(
                    COMPONENT_ID_WITH_CUSTOM_LINKS,
                    'CustomLinkLess'
                );
            }
        );
    });
});
