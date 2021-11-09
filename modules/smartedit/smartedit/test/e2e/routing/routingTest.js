/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Test Routing', function() {
    var page;
    beforeEach(function(done) {
        page = require('../utils/components/Page.js');
        var perspectives = require('../utils/components/Perspectives.js');

        return page.actions.getAndWaitForWholeApp('test/e2e/routing').then(function() {
            return perspectives.actions
                .selectPerspective(perspectives.constants.DEFAULT_PERSPECTIVES.ALL)
                .then(function() {
                    return browser.waitForWholeAppToBeReady();
                })
                .then(function() {
                    done();
                });
        });
    });
    afterEach(function() {
        browser.ignoreSynchronization = false;
    });

    it('navigates to a custom view from the smartedit container', function() {
        expect(element(by.css('.se-toolbar--experience')).isPresent()).toBe(true);
        browser.click(by.id('navigateToJSView')).then(function() {
            expect(browser.getCurrentUrl()).toContain('/customView');
            browser.waitForAbsence(element(by.css('se-toolbar-group')));
            expect(element(by.css('.content')).getText()).toBe('custom view 2');
        });
    });

    it('navigates to custom view from the inner smartedit iframe', function() {
        expect(element(by.css('.se-toolbar--experience')).isPresent()).toBe(true);
        browser.switchToIFrame().then(function() {
            browser.click(by.id('navigateToJSViewInner')).then(function() {
                expect(browser.getCurrentUrl()).toContain('/customView');
                browser.waitForAbsence(element(by.css('.se-toolbar--experience')));
                expect(element(by.css('.content')).getText()).toBe('custom view 2');
            });
        });
    });

    it('navigates to custom view from another view from the container', function() {
        browser.setLocation('/test').then(function() {
            browser.click(by.id('navigateToJSView')).then(function() {
                expect(browser.getCurrentUrl()).toContain('/customView');
                expect(element(by.css('.content')).getText()).toBe('custom view 2');
            });
        });
    });

    it('navigates to new view from the custom non smartedit iframe', function() {
        browser.setLocation('/test').then(function() {
            browser.waitForAngularEnabled(false);
            browser.waitUntilNoModal();
            browser.switchToIFrame(false).then(function() {
                browser.click(by.id('navigateToJSViewInner')).then(function() {
                    expect(browser.getCurrentUrl()).toContain('/customView');
                    expect(element(by.css('.content')).getText()).toBe('custom view 2');
                });
            });
        });
    });

    it('should navigate to legacy url through shortcut link', function() {
        browser
            .click(by.cssContainingText('.se-shortcut-link__item', 'CustomViewShortcutLink'))
            .then(function() {
                expect(browser.getCurrentUrl()).toContain('/customView');
                expect(element(by.css('.content')).getText()).toBe('custom view 2');
            });
    });

    it('should navigate to ng url through shortcut link', function() {
        browser
            .click(by.cssContainingText('.se-shortcut-link__item', 'ng test page link'))
            .then(function() {
                expect(browser.getCurrentUrl()).toContain('/ng/a');
                expect(element(by.css('.content')).getText()).toBe('This is a test page');
            });
    });
});
