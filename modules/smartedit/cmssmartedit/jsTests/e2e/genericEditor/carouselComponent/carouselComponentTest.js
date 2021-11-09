/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('Product Carousel Component - ', function() {
    var page = e2e.pageObjects.GenericEditor;
    var genericEditor = e2e.componentObjects.genericEditor;
    var productCarousel = e2e.componentObjects.productCarousel;

    beforeEach(function() {
        browser.bootstrap(__dirname);
        browser.get('/jsTests/e2e/smartedit.html#!/ng/carousel');
    });

    var componentData = {
        title: {
            EN: 'some title',
            IT: 'some title in italian'
        },
        products: {
            Staged: ['Asterisk SS youth black M'],
            Online: ['Avionics Shades Black']
        },
        categories: {
            Staged: ['Pants'],
            Online: ['Shirts']
        }
    };

    describe('New component with change permissions for attributes products and categories - ', function() {
        beforeEach(function(done) {
            productCarousel.actions.prepareApp(done, true, true);
            browser.waitForAngularEnabled(false);
        });

        it(
            'GIVEN a new product carousel component is added ' +
                'WHEN the editor is opened ' +
                'THEN the editor cannot be saved as it is not dirty ',
            function() {
                // GIVEN
                page.actions.openGenericEditor();

                // THEN
                genericEditor.assertions.saveIsDisabled();
            }
        );

        it(
            'GIVEN a new product carousel component is added ' +
                'WHEN the editor is opened ' +
                'THEN all fields in the editor are displayed correctly',
            function() {
                // GIVEN
                page.actions.openGenericEditor();

                // THEN
                productCarousel.assertions.componentIsEmpty();
            }
        );

        it(
            'GIVEN a new product carousel component is added ' + 'THEN the editor can be saved ',
            function() {
                // GIVEN
                page.actions.openGenericEditor();

                // WHEN
                productCarousel.actions.setProductCarouselData(componentData);
                genericEditor.actions.save();
                // THEN
                page.actions.openGenericEditor();
                productCarousel.assertions.hasRightData(componentData);
            }
        );
    });

    describe('New component with read-only permissions for attributes products and categories - ', function() {
        beforeEach(function(done) {
            productCarousel.actions.prepareApp(done, false, false);
            browser.waitForAngularEnabled(false);
        });

        it(
            'GIVEN a new product carousel component is added ' +
                'WHEN the editor is opened ' +
                'THEN the Add Items buttons are not displayed for product and categories attributes',
            function() {
                // GIVEN
                page.actions.openGenericEditor();

                // THEN
                productCarousel.assertions.productsAddButtonIsNotDisplayed();
                productCarousel.assertions.categoriesAddButtonIsNotDisplayed();
            }
        );
    });

    describe('Existing component - ', function() {
        beforeEach(function(done) {
            productCarousel.actions.prepareApp(done, true, true);
            page.actions.openGenericEditor();
            productCarousel.actions.setProductCarouselData(componentData);
            genericEditor.actions
                .save()
                .then(function() {
                    return browser.waitUntilNoModal();
                })
                .then(function() {
                    browser.waitForAngularEnabled(false);

                    done();
                });
        });

        it(
            'GIVEN the component already exists ' +
                'WHEN the editor is opened ' +
                'THEN the editor cannot be saved as it is not dirty ',
            function() {
                // GIVEN
                page.actions.openGenericEditor();

                // THEN
                genericEditor.assertions.saveIsDisabled();
            }
        );

        it(
            'GIVEN the component already contains products ' +
                'WHEN I reorder products and save ' +
                'THEN their order is persisted',
            function() {
                // GIVEN
                var originalOrder = ['Asterisk SS youth black M', 'Avionics Shades Black'];
                var newOrder = ['Avionics Shades Black', 'Asterisk SS youth black M'];
                page.actions.openGenericEditor();
                productCarousel.assertions.productsAreInRightOrder(originalOrder);

                // WHEN
                productCarousel.actions
                    .moveProductToPosition(0, 1)
                    .then(function() {
                        return genericEditor.actions.save();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    })
                    .then(function() {
                        return page.actions.openGenericEditor();
                    })
                    .then(function() {
                        return productCarousel.assertions.productsAreInRightOrder(newOrder);
                    });
            }
        );

        it(
            'GIVEN the component already contains products ' +
                'WHEN I move products up and save ' +
                'THEN the new list is persisted',
            function() {
                // GIVEN
                var originalOrder = ['Asterisk SS youth black M', 'Avionics Shades Black'];
                var newExpectedOrder = ['Avionics Shades Black', 'Asterisk SS youth black M'];
                page.actions.openGenericEditor();
                productCarousel.assertions.productsAreInRightOrder(originalOrder);

                // WHEN
                productCarousel.actions
                    .moveProductUp(1)
                    .then(function() {
                        return genericEditor.actions.save();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    })
                    .then(function() {
                        return page.actions.openGenericEditor();
                    })
                    .then(function() {
                        return productCarousel.assertions.productsAreInRightOrder(newExpectedOrder);
                    });
            }
        );

        it(
            'GIVEN the component already contains products ' +
                'WHEN I move products down and save ' +
                'THEN the new list is persisted',
            function() {
                // GIVEN
                var originalOrder = ['Asterisk SS youth black M', 'Avionics Shades Black'];
                var newExpectedOrder = ['Avionics Shades Black', 'Asterisk SS youth black M'];
                page.actions.openGenericEditor();
                productCarousel.assertions.productsAreInRightOrder(originalOrder);

                // WHEN
                productCarousel.actions
                    .moveProductDown(0)
                    .then(function() {
                        return genericEditor.actions.save();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    })
                    .then(function() {
                        return page.actions.openGenericEditor();
                    })
                    .then(function() {
                        return productCarousel.assertions.productsAreInRightOrder(newExpectedOrder);
                    });
            }
        );

        it(
            'GIVEN the component already contains products ' +
                'WHEN I remove products and save ' +
                'THEN the new list is persisted',
            function() {
                // GIVEN
                var originalList = ['Asterisk SS youth black M', 'Avionics Shades Black'];
                var newList = ['Asterisk SS youth black M'];
                page.actions.openGenericEditor();
                productCarousel.assertions.productsAreInRightOrder(originalList);

                // WHEN
                productCarousel.actions
                    .deleteProduct(1)
                    .then(function() {
                        return genericEditor.actions.save();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    })
                    .then(function() {
                        return page.actions.openGenericEditor();
                    })
                    .then(function() {
                        return productCarousel.assertions.productsAreInRightOrder(newList);
                    });
            }
        );

        it(
            'GIVEN the component already contains categories ' +
                'WHEN I remove categories and save ' +
                'THEN the new list is persisted',
            function() {
                // GIVEN
                var originalList = ['Pants', 'Shirts'];
                var newList = ['Pants'];
                page.actions.openGenericEditor();
                productCarousel.assertions.categoriesAreInRightOrder(originalList);

                // WHEN
                productCarousel.actions
                    .deleteCategory(1)
                    .then(function() {
                        return genericEditor.actions.save();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    })
                    .then(function() {
                        return page.actions.openGenericEditor();
                    })
                    .then(function() {
                        return productCarousel.assertions.categoriesAreInRightOrder(newList);
                    });
            }
        );

        // NOTE: We are only testing that products are given in the right order, as that can affect
        // how the carousel is displayed. The order of categories though doesn't have any noticeable effect.
    });
});
