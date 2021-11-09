/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';
import { OPERATION_CONTEXT } from 'smarteditcommons/utils/smarteditconstants';

export function operationContextInteractivePredicate(
    response: HttpErrorResponse,
    operationContext: string
) {
    return operationContext === OPERATION_CONTEXT.INTERACTIVE;
}

export function operationContextNonInteractivePredicate(
    response: HttpErrorResponse,
    operationContext: string
) {
    return lodash.includes(
        [
            OPERATION_CONTEXT.BACKGROUND_TASKS,
            OPERATION_CONTEXT.NON_INTERACTIVE,
            OPERATION_CONTEXT.BATCH_OPERATIONS
        ],
        operationContext
    );
}

export function operationContextCMSPredicate(
    response: HttpErrorResponse,
    operationContext: string
) {
    return operationContext === OPERATION_CONTEXT.CMS;
}

export function operationContextToolingPredicate(
    response: HttpErrorResponse,
    operationContext: string
) {
    return operationContext === OPERATION_CONTEXT.TOOLING;
}
