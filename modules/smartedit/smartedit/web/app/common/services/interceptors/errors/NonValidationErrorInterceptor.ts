/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HttpErrorResponse, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GenericEditorStackService } from '../../../components/genericEditor/services/GenericEditorStackService';
import { IAlertService } from '../../interfaces/IAlertService';
import { BackendError, IHttpErrorInterceptor } from '@smart/utils';
/**
 * @ngdoc service
 * @name httpInterceptorModule.service:nonValidationErrorInterceptor
 * @description
 * Used for HTTP error code 400. It removes all errors of type 'ValidationError' and displays alert messages for non-validation errors.
 */
@Injectable()
export class NonValidationErrorInterceptor<T = any> implements IHttpErrorInterceptor<T> {
    constructor(
        private alertService: IAlertService,
        private genericEditorStackService: GenericEditorStackService
    ) {}

    predicate(request: HttpRequest<T>, response: HttpErrorResponse) {
        return response.status === 400;
    }

    responseError(request: HttpRequest<T>, response: HttpErrorResponse) {
        if (response.error && response.error.errors) {
            response.error.errors
                .filter((error: BackendError) => {
                    const isValidationError = error.type === 'ValidationError';
                    return (
                        !isValidationError ||
                        (isValidationError &&
                            !this.genericEditorStackService.isAnyGenericEditorOpened())
                    );
                })
                .forEach((error: BackendError) => {
                    this.alertService.showDanger({
                        message: error.message || 'se.unknown.request.error',
                        timeout: 10000
                    });
                });
        }
        return Promise.reject(response);
    }
}
