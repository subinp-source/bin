/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, EventEmitter, Input, Output } from '@angular/core';
import { throwError, Observable } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';

/**
 * @ngdoc directive
 * @name GenericEditorModule.directive:ContentManager
 *
 * @description
 * Directive solely responsible for handling the submitting of its current data state to
 * an onSave input method and notifying of success and failure.
 *
 * example of usage:
 * ```
 * <form
 *  [contentManager]="{onSave: editor.submit$}"
 *  (onSuccess)="editor.onSuccess($event)"
 *  (onError)="editor.onFailure($event)"
 *  >
 * </form>
 * ```
 * @param {<  Microsyntax =} option object containing the onSave method of type (data: T) => Observable<T>
 * @param {< EventEmitter =} onSuccess outputs the successful result of onSave invocation
 * @param {< EventEmitter =} onError outputs the failing result of onSave invocation
 */

@Directive({
    selector: '[contentManager]'
})
export class ContentManager<T> {
    @Input('contentManager')
    set option(option: { onSave: () => Observable<T> }) {
        this._onSave = option.onSave;
    }

    /**
     * Called when a saving is a success.
     */
    @Output()
    onSuccess = new EventEmitter<T>();

    /**
     * Called when there is an error after saving.
     */
    @Output()
    onError = new EventEmitter<any>();

    /**
     * Submitting state of the manager.
     *
     * @type {boolean}
     */
    submitting = false;
    /**
     *  @internal
     */
    private _onSave: () => Observable<T>;

    save(): Observable<T> {
        this.submitting = true;

        return this._onSave().pipe(
            finalize(() => (this.submitting = false)),
            tap((content) => {
                this.onSuccess.emit(content);
            }),
            catchError((err) => {
                this.onError.emit(err);
                return throwError(err);
            })
        );
    }
}
