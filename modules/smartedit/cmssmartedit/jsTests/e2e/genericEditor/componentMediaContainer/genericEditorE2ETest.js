/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('GenericEditor Media Upload Container', function() {
    var path = require('path');
    require('../commonFunctions.js');

    var mediaContainer = e2e.componentObjects.mediaContainer;

    describe('Basic Media Container', function() {
        beforeEach(function() {
            browser.bootstrap(__dirname);
        });

        it(
            'WHEN I select an inflection point and select a file to upload ' +
                'THEN I expect to see the media upload form populated',
            function() {
                selectFileToUpload(
                    'more_bckg.png',
                    '.widescreen .se-media--present input[type="file"]'
                );

                expect(element(by.css('se-media-upload-form')).isPresent()).toBe(true);
                expect(element(by.css('input[name="code"]')).getAttribute('value')).toBe(
                    'more_bckg.png'
                );
                expect(element(by.css('input[name="description"]')).getAttribute('value')).toBe(
                    'more_bckg.png'
                );
                expect(element(by.css('input[name="altText"]')).getAttribute('value')).toBe(
                    'more_bckg.png'
                );
            }
        );

        it(
            'WHEN I select an inflection point with an existing image and upload  ' +
                'THEN I expect to see that inflection point updated with the newly uploaded image',
            function() {
                selectFileToUpload(
                    'more_bckg.png',
                    '.widescreen .se-media--present input[type="file"]'
                );
                clickUpload();

                browser.waitForAbsence(by.css('se-media-upload-form'));
                expect(
                    element(
                        by.css('.widescreen .se-media--present .se-media-preview__image-thumbnail')
                    ).getAttribute('data-ng-src')
                ).toContain('more_bckg.png');
                expect(
                    element(
                        by.css('.widescreen .se-media--present .se-media-preview__image-thumbnail')
                    ).getAttribute('src')
                ).toContain('more_bckg.png');
            }
        );

        it(
            'WHEN I select an inflection point and attempt to upload an invalid file   ' +
                'THEN I expect to see the errors populated',
            function() {
                selectFileToUpload('invalid.doc', '.mobile .se-media--absent input[type="file"]');

                browser.waitForAbsence(by.css('se-media-upload-form'));
                expect(
                    element
                        .all(by.css('.field-errors'))
                        .first()
                        .getText()
                ).toContain('se.upload.file.type.invalid');
            }
        );

        it(
            'WHEN I post invalid media data ' + 'THEN I expect to see the errors populated',
            function() {
                setFieldValue('afield', 'trump');
                save();

                expect(
                    element
                        .all(by.css('.se-help-block--has-error'))
                        .first()
                        .getText()
                ).toContain('No Trump jokes plz.');
            }
        );

        it(
            'WHEN I select an inflection point with no image selected and upload  ' +
                'THEN I expect to see that inflection point updated with the newly uploaded image',
            function() {
                selectFileToUpload('more_bckg.png', '.mobile .se-media--absent input[type="file"]');
                clickUpload();

                browser.waitForAbsence(by.css('se-media-upload-form'));
                expect(
                    element(
                        by.css('.mobile .se-media--present .se-media-preview__image-thumbnail')
                    ).getAttribute('data-ng-src')
                ).toContain('more_bckg.png');
                expect(
                    element(
                        by.css('.mobile .se-media--present .se-media-preview__image-thumbnail')
                    ).getAttribute('src')
                ).toContain('more_bckg.png');
            }
        );
    });

    describe('Advanced Media Container', function() {
        beforeEach(function() {
            browser.bootstrap(__dirname);
            mediaContainer.utils.switchToAdvancedMediaContainer();
        });

        it('WHEN switched to Advanced Media Container THEN should display a list of media containers', function() {
            // THEN
            mediaContainer.assertions.mediaContainerListIsDispalyed();
        });

        it('WHEN open the list of media containers THEN should display the list', function() {
            // WHEN
            mediaContainer.actions.openMediaContainerList();

            // THEN
            mediaContainer.assertions.mediaContainerListHasData(2);
        });

        it('WHEN select the media container from list THEN should display responsive media name field && should display media format fields', function() {
            // WHEN
            mediaContainer.actions.openMediaContainerList();
            mediaContainer.actions.selectMediaContainerFromList(
                'apparel-de-errorpage-pagenotfound'
            );

            // THEN
            mediaContainer.assertions.mediaContainerQualifierFieldDisplayed(
                'media-container-qualifier-mediaContainer_media_en'
            );
            mediaContainer.assertions.mediaFormatIsDisplayed('widescreen');
            mediaContainer.assertions.mediaFormatIsDisplayed('desktop');
            mediaContainer.assertions.mediaFormatIsDisplayed('tablet');
            mediaContainer.assertions.mediaFormatIsDisplayed('mobile');
        });

        it('GIVEN selected media container WHEN clear button is clicked THEN only media container list must be displayed', function() {
            // GIVEN
            mediaContainer.actions.openMediaContainerList();
            mediaContainer.actions.selectMediaContainerFromList(
                'apparel-de-errorpage-pagenotfound'
            );

            // WHEN
            mediaContainer.actions.clearSelectedMediaContainer();

            // THEN
            mediaContainer.assertions.mediaContainerQualifierFieldNotDisplayed(
                'media-container-qualifier-mediaContainer_media_en'
            );
            mediaContainer.assertions.mediaFormatIsNotDisplayed('widescreen');
            mediaContainer.assertions.mediaFormatIsNotDisplayed('desktop');
            mediaContainer.assertions.mediaFormatIsNotDisplayed('tablet');
            mediaContainer.assertions.mediaFormatIsNotDisplayed('mobile');
        });

        it('WHEN new media container is being created THEN should display populated responsive media name field && should display media format fields', function() {
            // WHEN
            mediaContainer.actions.openMediaContainerList();
            mediaContainer.actions.inputTextToSearchFieldOfMediaContainerList(
                'NonExistantMediaContainer'
            );
            mediaContainer.actions.clickCreateNewMediaContainer();

            // THEN
            mediaContainer.assertions.mediaContainerQualifierFieldDisplayed(
                'media-container-qualifier-mediaContainer_media_en'
            );
            mediaContainer.assertions.mediaContainerQualifierFieldContainsValue(
                'media-container-qualifier-mediaContainer_media_en',
                'NonExistantMediaContainer'
            );
            mediaContainer.assertions.mediaFormatIsDisplayed('widescreen');
            mediaContainer.assertions.mediaFormatIsDisplayed('desktop');
            mediaContainer.assertions.mediaFormatIsDisplayed('tablet');
            mediaContainer.assertions.mediaFormatIsDisplayed('mobile');
        });

        it('WHEN existing media container is selected THEN media container qualifier field is readonly', function() {
            // WHEN
            mediaContainer.actions.openMediaContainerList();
            mediaContainer.actions.selectMediaContainerFromList(
                'apparel-de-errorpage-pagenotfound'
            );

            // THEN
            mediaContainer.assertions.mediaContainerQualifierFieldIsReadOnly(
                'media-container-qualifier-mediaContainer_media_en'
            );
        });

        it('WHEN new media container is being created THEN media container qualifier field is editable', function() {
            // WHEN
            mediaContainer.actions.openMediaContainerList();
            mediaContainer.actions.inputTextToSearchFieldOfMediaContainerList(
                'NonExistantMediaContainer'
            );
            mediaContainer.actions.clickCreateNewMediaContainer();

            // THEN
            mediaContainer.assertions.mediaContainerQualifierFieldIsEditable(
                'media-container-qualifier-mediaContainer_media_en'
            );
        });
    });

    function clickUpload() {
        return browser.click(by.css('.se-media-upload-btn__submit'));
    }

    function selectFileToUpload(fileName, selector) {
        browser.waitUntil(
            EC.presenceOf(element(by.css(selector))),
            'File input element not present after 5000ms'
        );
        var absolutePath = path.resolve(__dirname, fileName);
        return element
            .all(by.css(selector))
            .first()
            .sendKeys(absolutePath);
    }

    function setFieldValue(fieldQualifier, value) {
        return browser.sendKeys(
            by.css(
                ".ySEGenericEditorFieldStructure[data-cms-field-qualifier='" +
                    fieldQualifier +
                    "'] input"
            ),
            value
        );
    }

    function save() {
        return browser.click(by.id('submit'));
    }
});
