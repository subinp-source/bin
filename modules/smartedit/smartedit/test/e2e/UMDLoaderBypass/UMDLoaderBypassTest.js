/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Bypass UMD module loading', function() {
    var page = require('../utils/components/Page.js');
    var storefront = require('../utils/components/Storefront.js');

    it('WHEN requireJS existing in the storefront then smartedoit will still load properly and requireJS will also still be usable', function(done) {
        page.actions.getAndWaitForWholeApp('test/e2e/UMDLoaderBypass/index.html');
        storefront.actions.goToRequireJsPage();
        browser.switchToIFrame().then(function() {
            browser.waitForAngular().then(function() {
                storefront.actions.clickRequireJsSuccessButton();
                browser
                    .waitForSelectorToContainText(by.id('requirejs-root-element'), 'success')
                    .then(function() {
                        done();
                    });
            });
        });
    });
});
