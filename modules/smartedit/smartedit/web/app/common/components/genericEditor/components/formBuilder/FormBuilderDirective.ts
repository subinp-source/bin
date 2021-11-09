/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Directive,
    EventEmitter,
    Input,
    Output,
    TemplateRef,
    ViewContainerRef
} from '@angular/core';

import { combineLatest, Observable, Subscription } from 'rxjs';
import { Payload } from '@smart/utils';

import { GenericEditorSchema } from '../../types';
import { GenericEditorStateBuilderService } from '../../services/GenericEditorStateBuilderService';
import { GenericEditorState } from '../../models';

@Directive({ selector: '[formBuilder]' })
export class FormBuilderDirective {
    @Input()
    set formBuilder(input: {
        data$: Observable<Payload>;
        schema$: Observable<GenericEditorSchema>;
    }) {
        this._dispose();
        this._subscription = combineLatest(input.data$, input.schema$).subscribe(
            this._onDataStream.bind(this)
        );
    }

    @Output()
    stateCreated = new EventEmitter<GenericEditorState>();

    private _subscription: Subscription;

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainer: ViewContainerRef,
        private stateBuilderService: GenericEditorStateBuilderService
    ) {}

    ngOnDestroy(): void {
        this._dispose();
    }

    private _onDataStream([data, schema]: [Payload | null, GenericEditorSchema | null]): void {
        if (!data || !schema) {
            return;
        }

        /**
         * Destroys all views and recreate the embeddedview.
         */
        this.viewContainer.clear();

        /**
         * No form supported.
         */
        if (!schema.structure) {
            this.viewContainer.createEmbeddedView(this.templateRef, {
                $implicit: null
            });
            return;
        }

        /**
         * Build state, and emit state.
         */
        const state = this.stateBuilderService.buildState(data, schema);
        this.stateCreated.emit(state);

        this.viewContainer.createEmbeddedView(this.templateRef, {
            $implicit: state
        });
    }

    /**
     * @internal
     * Removes subscription and destroyes all views.
     */
    private _dispose(): void {
        this._subscription && this._subscription.unsubscribe();
        this.viewContainer.clear();
    }
}
