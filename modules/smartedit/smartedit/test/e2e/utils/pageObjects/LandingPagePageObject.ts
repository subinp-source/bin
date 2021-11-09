/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace LandingPagePageObject {
    export const Constants = {
        LANDING_PAGE_PATH: 'test/e2e/landingPage/index.html',

        // SITES
        APPAREL_SITE: 'Apparels',
        ELECTRONICS_SITE: 'Electronics',
        TOYS_SITE: 'Toys',
        ACTION_FIGURES_SITE: 'Action Figures',

        // Catalogs
        APPAREL_UK_CATALOG: 'Apparel UK Content Catalog',
        ELECTRONICS_CATALOG: 'Electronics Content Catalog',
        TOYS_CATALOG: 'Toys Content Catalog',
        ACTION_FIGURES_CATALOG: 'Action Figures Content Catalog',

        // Catalog Versions
        ACTIVE_CATALOG_VERSION: 'Online',
        STAGED_CATALOG_VERSION: 'Staged',

        // IDs
        TOYS_SITE_ID: 'toys',
        TOYS_CATALOG_ID: 'toysContentCatalog',
        ACTION_FIGURES_SITE_ID: 'action',
        ACTION_FIGURES_CATALOG_ID: 'actionFiguresContentCatalog'
    };

    export const Elements = {
        // Site Selector
        getSiteSelector(): ElementFinder {
            return element(by.css('y-select[data-id="site"]'));
        },
        getSiteSelectorInput(): ElementFinder {
            return Elements.getSiteSelector().element(
                by.css('item-printer .y-select-default-item')
            );
        },
        async getSiteSelectorValue(): Promise<string> {
            const text = await Elements.getSiteSelectorInput().getText();

            return text;
        },
        getSiteSelectorOptionByName(siteName: string): ElementFinder {
            return Elements.getSiteSelector().element(
                by.cssContainingText('.select2-result-single span', siteName)
            );
        },
        getSiteSelectorSearchField(): ElementFinder {
            return element(by.css('y-select[data-id="site"] input'));
        },

        // Catalogs
        getCatalogsDisplayed(): ElementArrayFinder {
            return element.all(by.css('se-catalog-details'));
        },
        async getNumberOfCatalogsDisplayed(): Promise<number> {
            const count = await Elements.getCatalogsDisplayed().count();

            return count;
        },
        getCatalogByIndex(index: number): ElementFinder {
            return Elements.getCatalogsDisplayed().get(index);
        },
        getCatalogByName(catalogName: string): ElementFinder {
            const catalogByNameXPathSelector =
                "//se-catalog-details[.//*[contains(@class, 'se-catalog-details__header') and text()[contains(.,'" +
                catalogName +
                "')]]]";
            return element(by.xpath(catalogByNameXPathSelector));
        },
        getCatalogVersion(catalogName: string, catalogVersion: string): ElementFinder {
            const catalogVersionXPathSelector =
                "//se-catalog-version-details[.//*[contains(@class, 'se-catalog-version-container__name' ) and text()[contains(., '" +
                catalogVersion +
                "')]]]";

            return Elements.getCatalogByName(catalogName)
                .all(by.xpath(catalogVersionXPathSelector))
                .filter((item: ElementFinder) => {
                    return item.isDisplayed();
                })
                .first();
        },
        getCatalogContainerByName(catalogName: string): ElementFinder {
            return Elements.getCatalogByName(catalogName).element(
                by.css('se-collapsible-container')
            );
        },
        getCatalogTitle(catalogName: string): ElementFinder {
            return Elements.getCatalogByName(catalogName).element(
                by.css('.collapsible-container__header')
            );
        },
        getCatalogThumbnail(catalogName: string): ElementFinder {
            return Elements.getCatalogByName(catalogName).element(
                by.css(
                    'se-catalog-versions-thumbnail-carousel .se-active-catalog-version-container__thumbnail'
                )
            );
        },
        getHomePageLink(catalogName: string, catalogVersion: string): ElementFinder {
            return Elements.getCatalogByName(catalogName)
                .element(by.cssContainingText('.se-catalog-version-container', catalogVersion))
                .element(by.css('se-home-page-link a'));
        },
        getCatalogVersionTemplateByName(
            catalogName: string,
            catalogVersion: string,
            item: string
        ): ElementFinder {
            return Elements.getCatalogVersion(catalogName, catalogVersion).element(
                by.cssContainingText('div', item)
            );
        },

        // Others
        async getBrowserUrl(): Promise<string> {
            const url = await browser.getCurrentUrl();

            return url;
        }
    };

    export const Actions = {
        // Navigation
        async openAndBeReady(): Promise<void> {
            await browser.get(Constants.LANDING_PAGE_PATH);
            await browser.waitForContainerToBeReady();
        },

        // Sites Selector
        async openSiteSelector(): Promise<void> {
            await browser.click(Elements.getSiteSelector());
        },
        async selectSite(siteName: string) {
            await Actions.openSiteSelector();
            await browser.click(Elements.getSiteSelectorOptionByName(siteName));
        },

        // Catalogs
        async clickOnCatalogHeader(catalogName: string): Promise<void> {
            await browser.click(Elements.getCatalogTitle(catalogName));
        },
        async navigateToStorefrontViaThumbnail(catalogName: string): Promise<void> {
            await browser.click(Elements.getCatalogThumbnail(catalogName));

            await browser.waitForWholeAppToBeReady();
            await browser.waitForUrlToMatch(/\/storefront/);
        },
        async navigateToStorefrontViaHomePageLink(
            catalogName: string,
            catalogVersion: string
        ): Promise<void> {
            await browser.click(Elements.getHomePageLink(catalogName, catalogVersion));
            await browser.waitForWholeAppToBeReady();
            await browser.waitForUrlToMatch(/\/storefront/);
        },

        // Note: This is only meant to be used when clicking on a homePage link that don't redirect to another storefront.
        // (our tests only care about the URL being changed appropriately). Thus, it is not necessary to wait for the whole app to be ready.
        async clickOnHomePageLink(catalogName: string, catalogVersion: string): Promise<void> {
            await browser.click(Elements.getHomePageLink(catalogName, catalogVersion));
            await browser.waitForUrlToMatch(/\/storefront/);
        },
        async clickOnParentCatalogHomePageLink(
            catalogName: string,
            catalogVersion: string
        ): Promise<void> {
            await Actions.clickOnCatalogHeader(catalogName);
            await Actions.clickOnHomePageLink(catalogName, catalogVersion);
        },

        // Left Menu
        async navigateToLandingPage(): Promise<void> {
            await browser.click(element(by.css('sites-link a')));
            await browser.waitForContainerToBeReady();
            await browser.waitForUrlToMatch(/^(?!.*storefront)/);
        },

        async goToLandingPageWithSiteId(siteId: string): Promise<void> {
            await browser.setLocation('sites/' + siteId);
            await browser.waitForContainerToBeReady();
        },

        async navigateBack(): Promise<void> {
            await browser.navigate().back();
            await browser.waitForContainerToBeReady();
        },

        async refreshOnLandingPage(): Promise<void> {
            await browser.refresh();
            await browser.waitForContainerToBeReady();
        }
    };

    export const Assertions = {
        // Site
        async expectedSiteIsSelected(siteName: string): Promise<void> {
            const value = await Elements.getSiteSelectorValue();

            expect(value).toBe(siteName, "Selected site doesn't match expected one.");
        },
        async selectedSiteHasRightNumberOfCatalogs(
            expectedNumberOfCatalogs: number
        ): Promise<void> {
            await browser.waitUntil(async () => {
                const actualNumberOfCatalogs = await Elements.getNumberOfCatalogsDisplayed();

                return actualNumberOfCatalogs === expectedNumberOfCatalogs;
            }, 'Expected ' + expectedNumberOfCatalogs + ' catalogs for selected site., got ');
        },

        // Catalog
        async catalogIsExpanded(catalogName: string): Promise<void> {
            expect(await Utils.isCatalogExpanded(catalogName)).toBe(
                true,
                'Expected catalog to be expanded'
            );
        },
        async catalogIsNotExpanded(catalogName: string): Promise<void> {
            expect(await Utils.isCatalogExpanded(catalogName)).toBe(
                false,
                'Expected catalog to be expanded'
            );
        },
        async catalogVersionContainsItem(
            catalogName: string,
            catalogVersion: string,
            item: string
        ): Promise<void> {
            expect(
                await Elements.getCatalogVersionTemplateByName(
                    catalogName,
                    catalogVersion,
                    item
                ).isPresent()
            ).toBe(true, 'Expected template to be displayed in catalog version.');
        },

        // Other
        async assertLandingPageIsDisplayed(): Promise<void> {
            expect(await Elements.getBrowserUrl()).not.toContain('/storefront');
        },
        async assertStorefrontIsLoaded(): Promise<void> {
            expect(await Elements.getBrowserUrl()).toContain('/storefront');
        },
        async assertLandingPageDoesNotHaveSiteInUrl(): Promise<void> {
            expect(await Elements.getBrowserUrl()).not.toContain('/sites');
        },
        async assertListOfOptionsInSitesDropdown(expectedOptions: string[]): Promise<void> {
            const dropdownOptionCssSelector =
                'y-select[data-id="site"] .ui-select-choices-row-inner';

            await browser.waitUntil(async () => {
                const actualOptions = await element
                    .all(by.css(dropdownOptionCssSelector))
                    .map(async (e: ElementFinder) => {
                        try {
                            const text = await e.getText();

                            return text;
                        } catch (e) {
                            return '';
                        }
                    });

                return actualOptions.join(',') === expectedOptions.join(',');
            }, 'Expected dropdown options for site to be ' + expectedOptions);
        },

        async searchAndAssertInDropdown(
            searchTerm: string,
            expectedOptions: string[]
        ): Promise<void> {
            await browser.waitForPresence(Elements.getSiteSelectorSearchField());
            await Elements.getSiteSelectorSearchField().clear();
            await Elements.getSiteSelectorSearchField().sendKeys(searchTerm);
            await Assertions.assertListOfOptionsInSitesDropdown(expectedOptions);
        }
    };

    export const Utils = {
        async isCatalogExpanded(catalogName: string): Promise<boolean> {
            const height = await Elements.getCatalogContainerByName(catalogName)
                .element(by.css('.collapsible-container__content'))
                .getAttribute('clientHeight');

            return height !== '0';
        },

        async hasClass(e: ElementFinder, expectedClass: string): Promise<boolean> {
            const classes = await e.getAttribute('class');

            return classes.split(' ').indexOf(expectedClass) !== -1;
        }
    };
}
