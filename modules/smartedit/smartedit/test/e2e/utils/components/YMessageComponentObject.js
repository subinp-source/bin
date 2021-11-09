/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var yMessageObject = {
        elements: {
            getYMessageById: function(id) {
                return element(by.css('div[message-id=' + id + ']'));
            },
            getTitle: function(id) {
                return this.getSubElementByClass(id, '.y-message-info-title').getText();
            },
            getDescription: function(id) {
                return this.getSubElementByClass(id, '.y-message-info-description').getText();
            },
            getSubElementByClass: function(id, className) {
                var el = this.getYMessageById(id);
                return el.element(by.css(className));
            },
            getWrapperClasses: function(id) {
                var el = this.getYMessageById(id);
                return el.getAttribute('class');
            }
        },
        actions: {
            openAndBeReady: function() {
                browser.get('test/e2e/yMessage/index.html');
            }
        },
        assertions: {
            assertTitleContainsText: function(id, text) {
                expect(yMessageObject.elements.getTitle(id)).toBe(
                    text,
                    'Expected the title to be present'
                );
            },
            assertDescriptionContainsText: function(id, text) {
                expect(yMessageObject.elements.getDescription(id)).toBe(
                    text,
                    'Expected the description to be present'
                );
            },
            assertInfoTypeWasApplied: function(id) {
                expect(yMessageObject.elements.getWrapperClasses(id)).toContain(
                    'fd-alert--information',
                    'Expected the yMessage to have [fd-alert--information] icon class'
                );
            },
            assertComplexDescriptionWasTranscluded: function(id) {
                expect(
                    yMessageObject.elements.getSubElementByClass(id, '.inner-class').isPresent()
                ).toBe(true, 'Expected the yMessage to have a transcluded element class');
            },
            assertDefaultIdWasProvided: function() {
                expect(
                    yMessageObject.elements
                        .getYMessageById(yMessageObject.constants.YMESSAGE_DEFAULT_ID)
                        .isPresent()
                ).toBe(true, 'Expected the yMessage to use a default id');
            }
        },
        constants: {
            YMESSAGE_WITH_TYPE_ID: 'y-message-id',
            YMESSAGE_WITHOUT_TYPE_ID: 'y-message-id',
            YMESSAGE_WITH_COMPLEX_DESCRIPTION: 'y-message-with-complex-description-id',
            YMESSAGE_DEFAULT_ID: 'y-message-default-id'
        }
    };

    return yMessageObject;
})();
