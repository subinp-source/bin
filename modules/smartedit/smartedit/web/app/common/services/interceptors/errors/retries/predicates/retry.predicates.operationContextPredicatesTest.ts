/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    operationContextCMSPredicate,
    operationContextInteractivePredicate,
    operationContextNonInteractivePredicate,
    operationContextToolingPredicate
} from './OperationContextPredicates';
import { OPERATION_CONTEXT } from 'smarteditcommons/utils/smarteditconstants';

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('operation context predicates', function() {
    it('operation context interactive predicate should match INTERACTIVE', function() {
        expect(
            operationContextInteractivePredicate(null, OPERATION_CONTEXT.INTERACTIVE)
        ).toBeTruthy();
    });

    it('operation context non-interactive predicate should match BACKGROUND_TASKS, NON_INTERACTIVE and BATCH_OPERATIONS', function() {
        [
            OPERATION_CONTEXT.BACKGROUND_TASKS,
            OPERATION_CONTEXT.NON_INTERACTIVE,
            OPERATION_CONTEXT.BATCH_OPERATIONS
        ].forEach(function(oc) {
            expect(operationContextNonInteractivePredicate(null, oc)).toBeTruthy();
        });
    });

    it('operation context CMS predicate should match CMS', function() {
        expect(operationContextCMSPredicate(null, OPERATION_CONTEXT.CMS)).toBeTruthy();
    });

    it('operation context tooling predicate should match TOOLING', function() {
        expect(operationContextToolingPredicate(null, OPERATION_CONTEXT.TOOLING)).toBeTruthy();
    });
});
