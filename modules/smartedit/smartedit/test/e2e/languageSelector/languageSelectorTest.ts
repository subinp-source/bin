/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ExperienceSelectorObject } from '../utils/components/ExperienceSelector';
import { HeaderToolbarComponentObject } from '../utils/components/HeaderToolbarComponentObject';
import { Login } from '../utils/components/Login';
import { Storefront } from '../utils/components/Storefront';
import { Perspectives } from '../utils/components/Perspectives';
import { browser, by, element } from 'protractor';
import { buildDecoratorName } from '../utils/outerCommon';

export const MAIN_AUTH_SUFFIX = 'L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4';

describe('languageSelector', () => {
    beforeAll(() => {
        browser.isDelayed = true;
    });
    afterAll(() => {
        browser.isDelayed = false;
    });

    beforeEach(async () => {
        await browser.clearLocalStorage();
        await browser.get('test/e2e/languageSelector/index.html');
        await browser.waitForAngularEnabled(false);
    });

    afterEach(async () => {
        await browser.clearLocalStorage();
        await browser.driver.manage().deleteAllCookies();
        await browser.waitForAngularEnabled(true);
    });

    it('GIVEN I am on the login page WHEN I select French language, THEN the first select box in the list is French', async () => {
        // WHEN
        await selectLanguageFromLogin('French');
        await Login.Actions.toggleLanguageSelectorDropdown();

        // ASSERT
        await Login.Assertions.assertLanguageSelectorFirstInList('French');
    });

    it('GIVEN my browser has unsupported language, THEN the translation map is still fetched for that language', async () => {
        await browser.executeScript(
            'window.localStorage.setItem("SELECTED_LANGUAGE", arguments[0])',
            JSON.stringify({
                isoCode: 'kl',
                name: 'Klingon'
            })
        );
        await browser.refresh();
        expect(
            await element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('klName');
        expect(
            await element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('klPassword');
    });

    it('GIVEN I am on the login page WHEN I select English language THEN it should translate the i18n keys', async () => {
        await selectLanguageFromLogin('English');
        expect(
            await element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Name');
        expect(
            await element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Password');
    });

    it('GIVEN I am on the login page WHEN I select French language THEN it should translate the i18n keys', async () => {
        await selectLanguageFromLogin('French');
        expect(
            await element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Nom');
        expect(
            await element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Mot de passe');
    });

    it('GIVEN I am on the login page, AND I select French language, WHEN coming back to the page, THEN it should load the french localization', async () => {
        await selectLanguageFromLogin('French');
        expect(
            await element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Nom');
        await browser.refresh();
        expect(
            await element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Nom');
        expect(
            await element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')
        ).toBe('Mot de passe');
    });

    it('GIVEN I select French Language, AND submitting right credentials, THEN the container should be localized with French', async () => {
        // GIVEN
        await selectLanguageAndLogin('French');

        // THEN

        await browser.waitUntilNoModal();
        await browser.click(ExperienceSelectorObject.Elements.widget.button());

        // ASSERT
        expect(await ExperienceSelectorObject.Elements.catalog.label().getText()).toBe('Catalogue');
        expect(await ExperienceSelectorObject.Elements.dateAndTime.label().getText()).toMatch(
            /Date et Heure/i
        );
        expect(await (await ExperienceSelectorObject.Elements.language.label()).getText()).toBe(
            'Langue'
        );
    });

    it('GIVEN I select French Language, AND submitting right credentials, THEN the store front should be localized with French', async () => {
        // GIVEN
        await selectLanguageAndLogin('French');

        // THEN
        await browser.switchToIFrame();
        await HeaderToolbarComponentObject.Assertions.localizedFieldIsTranslated(
            by.id('localizationField'),
            'Je suis localisée'
        );
        await browser.switchToParent();
    });

    it('GIVEN I logged in smartedit, AND change the language on the header toolbar, THEN the store front should be localized with French', async () => {
        // GIVEN
        await selectLanguageAndLogin('English');

        // THEN
        await selectLanguageFromHeader('French');
        await browser.switchToIFrame();

        // ASSERT
        await HeaderToolbarComponentObject.Assertions.localizedFieldIsTranslated(
            by.id('localizationField'),
            'Je suis localisée'
        );
        await browser.switchToParent();
    });

    it('GIVEN I selected French on the login page, AND I changed it to English on the header toolbar, WHEN I logout THEN the login page should be localized in English', async () => {
        // GIVEN
        await selectLanguageAndLogin('French');
        await selectLanguageFromHeader('English');
        await Login.Actions.logoutUser();

        // THEN

        await Login.Assertions.assertLanguageSelectorLanguage('English');
    });

    it('GIVEN I click on the login page language selector THEN it should be populated with values', async () => {
        await Login.Actions.waitForLanguageSelectorToBePopulated('English');
        await Login.Actions.waitForLanguageSelectorToBePopulated('French');
    });

    it('GIVEN I logged in smartedit, AND change the language on the header toolbar, THEN the decorators should be localized with French', async () => {
        // GIVEN
        await selectLanguageAndLogin('English');

        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );

        await browser.waitForWholeAppToBeReady();

        await selectComponent1ContextualMenu();
        expect(await element(by.css('dummy')).getText()).toBe('dummyText in english');

        await browser.switchToIFrame();
        expect(await element(by.css('dummy-angular')).getText()).toBe('dummyText in english');
        await browser.switchToParent();

        await selectComponent1ContextualMenu();

        // THEN
        await browser.switchToParent();

        await selectLanguageFromHeader('French');

        await browser.switchToIFrame();

        // ASSERT
        await selectComponent1ContextualMenu();
        expect(await element(by.css('dummy')).getText()).toBe('dummyText in french');

        await browser.switchToIFrame();
        expect(await element(by.css('dummy-angular')).getText()).toBe('dummyText in french');
        await browser.switchToParent();
    });

    async function selectLanguageAndLogin(language: string) {
        await element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
        await element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
        await selectLanguageFromLogin(language);
        await browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
        await browser.waitForWholeAppToBeReady();
    }

    async function selectLanguageFromHeader(value: string): Promise<void> {
        const languageSelector = HeaderToolbarComponentObject.Elements.getLanguageSelector(
            '[data-item-key="headerToolbar.languageSelectorTemplate"]'
        );
        await browser.waitForPresence(languageSelector);
        await browser.click(
            languageSelector.element(by.css('.se-toolbar-actions__icon')),
            'icon dropdown not clickable'
        );

        await browser.click(
            languageSelector.element(
                by.cssContainingText('.yToolbarActions__dropdown-element', value)
            ),
            'language select choice with value ' + value + ' not clickable'
        );
    }

    async function selectLanguageFromLogin(value: string): Promise<void> {
        const languageSelector = element(by.css('.su-login-language'));
        await browser.waitForPresence(languageSelector);
        await browser.click(
            languageSelector.element(by.css('.fd-dropdown__control')),
            'Fundamental Dropdown Control toggle not clickable'
        );
        await browser.click(
            languageSelector.element(by.cssContainingText('.fd-menu__item', value)),
            'Fundamental Menu Item with value ' + value + ' not clickable'
        );
    }

    async function selectComponent1ContextualMenu(): Promise<void> {
        await browser.switchToIFrame();
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_1_ID)))
            .perform();
        await browser.click(
            by.id(
                buildDecoratorName(
                    'TEMPLATEURL',
                    Storefront.Constants.COMPONENT_1_ID,
                    Storefront.Constants.COMPONENT_1_TYPE,
                    0
                )
            )
        );
    }
});
