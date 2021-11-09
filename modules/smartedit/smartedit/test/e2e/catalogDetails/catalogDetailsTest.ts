/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { LandingPagePageObject } from '../utils/pageObjects/LandingPagePageObject';
import { CatalogDetailsPageObject } from '../utils/pageObjects/CatalogDetailsPageObject';

describe('Landing page', () => {
    beforeEach(async () => {
        await CatalogDetailsPageObject.Actions.openAndBeReady();
    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded THEN I expect to see the injected tempplate via the bridge', async () => {
        // GIVEN
        const template1Name = 'Hello';
        const template2Name = 'World';

        // THEN
        await LandingPagePageObject.Assertions.catalogVersionContainsItem(
            LandingPagePageObject.Constants.ELECTRONICS_CATALOG,
            LandingPagePageObject.Constants.STAGED_CATALOG_VERSION,
            template1Name
        );
        await LandingPagePageObject.Assertions.catalogVersionContainsItem(
            LandingPagePageObject.Constants.ELECTRONICS_CATALOG,
            LandingPagePageObject.Constants.STAGED_CATALOG_VERSION,
            template2Name
        );

        await LandingPagePageObject.Assertions.catalogVersionContainsItem(
            LandingPagePageObject.Constants.ELECTRONICS_CATALOG,
            LandingPagePageObject.Constants.ACTIVE_CATALOG_VERSION,
            template1Name
        );
        await LandingPagePageObject.Assertions.catalogVersionContainsItem(
            LandingPagePageObject.Constants.ELECTRONICS_CATALOG,
            LandingPagePageObject.Constants.ACTIVE_CATALOG_VERSION,
            template2Name
        );
    });
});
