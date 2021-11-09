/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, HostBinding, HostListener, Input } from '@angular/core';
import { ContentManager } from './ContentManager';

/**
 * @ngdoc directive
 * @name GenericEditorModule.component:SubmitButtonDirective
 * @element [seSubmitBtn]
 *
 * @description
 * Applied on a DOM element, this Directive will trigger a submit of the data stored in
 * the parent {@link GenericEditorModule.directive:ContentManager ContentManager} upon cliking.
 * usage example:
 * ```
 * <form [contentManager]="{onSave: editor.someSubmit}">
 *      <button [seSubmitBtn]="editor.isSubmitDisabled">Submit </button>
 * </form>
 * ```
 * @param {= () => boolean = } seSubmitBtn The optional callback returning a boolean to add more cases for disablement
 */

@Directive({
    selector: '[seSubmitBtn]'
})
export class SubmitBtnDirective {
    @Input('seSubmitBtn')
    public isDisabled: () => boolean;

    constructor(private cm: ContentManager<any>) {}

    /**
     * Modifies the disabled attribute to be disabled when saving.
     */
    @HostBinding('disabled')
    get disabled(): boolean {
        return this.cm.submitting || (this.isDisabled && this.isDisabled());
    }

    /**
     * When the element is clicked the save operation is called in the content manager direcitve.
     */
    @HostListener('click', ['$event'])
    save($event: Event): void {
        $event.preventDefault();
        this.cm.save().subscribe();
    }
}
