/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
var assertModalTitleIs = function(modalTitleLabel, modalTitleText) {
    expect(element(by.id(modalTitleLabel)).getText()).toBe(modalTitleText);
};

describe('Test extended modal-service', function() {
    var EC = protractor.ExpectedConditions;

    beforeEach(function() {
        browser.get('test/e2e/modalservice/extended/index.html');
    });

    it('Given a modal configuration with only a title - when the modal opens then it has the correct text', function() {
        browser.click(by.id('modalWithTitle'));
        element(by.css('.modal-content')).isDisplayed();

        assertModalTitleIs('smartedit-modal-title-modal.title.lamp', 'I love lamp ');
    });

    it('Given a modal configuration with title and buttons - when the modal opens then it has title and buttons, ', function() {
        browser.click(by.id('modalWithTitleAndButtons'));
        element(by.css('.modal-content')).isDisplayed();

        expect(element(by.id('smartedit-modal-title-modal.title.lamp')).isDisplayed());

        expect(element(by.id('modalButton')).isDisplayed());
        expect(element(by.id('modalButton')).isEnabled());
        expect(EC.elementToBeClickable(element(by.css('#modalButton'))));
    });

    it('Given a modal configuration with a disabled button - when the modal opens then it has the disabled button, ', function() {
        browser.click(by.id('modalWithDisabledButton'));
        element(by.css('.modal-content')).isDisplayed();

        expect(element(by.id('modalButton')).isDisplayed());
        expect(element(by.id('modalButton')).isEnabled()).toBe(false);
    });
});
