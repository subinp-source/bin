/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ErrorHandler } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

export class SmarteditErrorHandler extends ErrorHandler {
    private ignorePatterns = [
        /Uncaught[\s]*\(in[\s]*promise\)/,
        /Unhandled[\s]*Promise[\s]*rejection/
    ];

    constructor() {
        super();
    }

    handleError(error: { message: string }) {
        /*
         * original exception occuring in a promise based API won't show here
         * the catch set in ES6 promise decoration is necessary to log them
         */
        const message = error && error.message ? error.message : error;
        if (message && this.ignorePatterns.some((pattern) => pattern.test(message.toString()))) {
            return;
        }

        if (error instanceof HttpErrorResponse && error.status === 401) {
            return;
        }

        super.handleError(error);
    }
}
