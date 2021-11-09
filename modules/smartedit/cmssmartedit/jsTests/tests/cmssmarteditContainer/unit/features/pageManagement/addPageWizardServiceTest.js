/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('addPageWizardService', function() {
    var $rootScope, addPageWizardService;
    var mocks;
    var uriContext = {
        a: 'b'
    };

    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule('addPageServiceModule')
            .mock('catalogService', 'retrieveUriContext')
            .and.returnResolvedPromise(uriContext)
            .mock('modalWizard', 'open')
            .and.returnValue('')
            .service('addPageWizardService');

        addPageWizardService = harness.service;
        mocks = harness.mocks;
        $rootScope = harness.injected.$rootScope;
    });

    describe('openAddPageWizard', function() {
        it('should delegate to the modal wizard', function() {
            addPageWizardService.openAddPageWizard();
            $rootScope.$digest();
            expect(mocks.modalWizard.open).toHaveBeenCalledWith({
                controller: 'addPageWizardController',
                controllerAs: 'addPageWizardCtl',
                properties: {
                    uriContext: uriContext
                }
            });
        });
    });
});
