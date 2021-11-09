/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

describe('functionsModule', () => {
    let customTimeout: any;
    let closeOpenModalsOnBrowserBack: any;
    let $uibModalStack: any;

    beforeEach(angular.mock.module('functionsModule'));

    beforeEach(
        angular.mock.module('ui.bootstrap', ($provide: angular.auto.IProvideService) => {
            $uibModalStack = jasmine.createSpyObj('$uibModalStack', ['getTop', 'dismissAll']);
            $provide.value('$uibModalStack', $uibModalStack);
        })
    );

    beforeEach(inject((_customTimeout_, _closeOpenModalsOnBrowserBack_) => {
        customTimeout = _customTimeout_;
        closeOpenModalsOnBrowserBack = _closeOpenModalsOnBrowserBack_;
    }));

    beforeEach(() => {
        // Clock is globally installed somewhere, somehow. We need to uninstall it before
        // installing it for each test in the suite.
        jasmine.clock().install();
    });
    afterEach(() => {
        jasmine.clock().uninstall();
    });

    it('customTimeout will call a specified function after a specified duration (in ms)', () => {
        const duration = 5000;
        const func = jasmine.createSpy('func').and.returnValue(() => {
            return;
        });

        spyOn(window, 'setTimeout').and.callThrough();

        customTimeout(func, duration);
        expect(func).not.toHaveBeenCalled();

        jasmine.clock().tick(2000);
        expect(func).not.toHaveBeenCalled();

        jasmine.clock().tick(3000);
        expect(func).toHaveBeenCalled();

        expect(window.setTimeout).toHaveBeenCalledWith(jasmine.any(Function), 5000);
    });

    it('closeOpenModalsOnBrowserBack will dismiss open modal windows if open', () => {
        $uibModalStack.getTop.and.returnValue({
            modal1: 'modal1'
        });

        closeOpenModalsOnBrowserBack();
        expect($uibModalStack.getTop).toHaveBeenCalled();
        expect($uibModalStack.dismissAll).toHaveBeenCalled();
    });

    it('closeOpenModalsOnBrowserBack will not dismiss modal windows if no window is open', () => {
        $uibModalStack.getTop.and.returnValue();

        closeOpenModalsOnBrowserBack();
        expect($uibModalStack.getTop).toHaveBeenCalled();
        expect($uibModalStack.dismissAll).not.toHaveBeenCalled();
    });
});
