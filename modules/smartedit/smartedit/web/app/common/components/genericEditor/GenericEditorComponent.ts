/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    ElementRef,
    EventEmitter,
    Inject,
    Input,
    OnChanges,
    Output,
    SimpleChanges,
    ViewChild
} from '@angular/core';
import { isNull } from 'lodash';
import { FormGroup } from '@angular/forms';
import { Payload } from '@smart/utils';
import { IUriContext } from '../../services';
import {
    GenericEditorAPI,
    GenericEditorOnSubmitResponse,
    GenericEditorStructure,
    IGenericEditor
} from './types';
import { GenericEditorFactoryService } from './GenericEditorFactoryService';
import { ContentManager } from './components/ContentManager';
import { stringUtils } from '../../utils';
import { SeDowngradeComponent } from '../../di';
import { YJQUERY_TOKEN } from '../../services/vendors/YjqueryModule';

import './genericEditor.scss';
import { GenericEditorState } from '../genericEditor/models';
import { combineLatest, BehaviorSubject } from 'rxjs';
import { filter, take } from 'rxjs/operators';

const changeTriggeringInputs = new Set([
    'id',
    'smarteditComponentId',
    'smarteditComponentType',
    'structureApi',
    'structure',
    'contentApi',
    'content',
    'uriContext',
    'updateCallback',
    'customOnSubmit',
    'editorStackId',
    'modalHeaderTitle'
]);

/**
 * @ngdoc component
 * @name GenericEditorModule.component:GenericEditorComponent
 * @scope
 * @restrict E
 * @element se-generic-editor
 *
 * @description
 * Component responsible for generating custom HTML CRUD form for any smarteditComponent type.
 *
 * The controller has a method that creates a new instance for the {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService}
 * and sets the scope of smarteditComponentId and smarteditComponentType to a value that has been extracted from the original DOM element in the storefront.
 *
 * @param {= String} id Id of the current generic editor.
 * @param {= String} smarteditComponentType The SmartEdit component type that is to be created, read, updated, or deleted.
 * @param {= String} smarteditComponentId The identifier of the SmartEdit component that is to be created, read, updated, or deleted.
 * @param {< String =} structureApi The data binding to a REST Structure API that fulfills the contract described in the  {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService} service. Only the Structure API or the local structure must be set.
 * @param {< String =} structure The data binding to a REST Structure JSON that fulfills the contract described in the {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService} service. Only the Structure API or the local structure must be set.
 * @param {= String} contentApi The REST API used to create, read, update, or delete content.
 * @param {= Object} content The model for the generic editor (the initial content when the component is being edited).
 * @param {< Object =} uriContext is an optional parameter and is used to pass the uri Params which can be used in making
 * api calls in custom widgets. It is an optional parameter and if not found, generic editor will find an experience in
 * sharedDataService and set this uriContext.
 * @param {= Function =} submit It exposes the inner submit function to the invoker scope. If this parameter is set, the directive will not display an inner submit button.
 * @param {= Function =} reset It exposes the inner reset function to the invoker scope. If this parameter is set, the directive will not display an inner cancel button.
 * @param {= Function =} isDirty Indicates if the the generic editor is in a pristine state (for example: has been modified).
 * @param {= Function =} isValid Indicates if all of the containing forms and controls in the generic editor are valid.
 * @param {& Function =} getApi Exposes the generic editor's api object
 * @param {< Function =} updateCallback Callback called at the end of a successful submit. It is invoked with two arguments: the pristine object and the response from the server.
 * @param {= Function =} customOnSubmit It exposes the inner onSubmit(newContent: Payload) function to the invoker scope. If the parameter is set, the inner onSubmit function is overridden by the custom function and the custom function must return a promise in the response format expected by the generic editor.
 * @param {< String =} editorStackId When working with nested components, a generic editor can be opened from within another editor. This parameter is used to specify the stack of nested editors.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-generic-editor',
    templateUrl: './GenericEditorComponent.html'
})
export class GenericEditorComponent implements OnChanges {
    @Input() id: string;
    @Input() smarteditComponentId: string;
    @Input() smarteditComponentType: string;
    @Input() structureApi: string;
    @Input() structure: GenericEditorStructure;
    @Input() contentApi: string;
    @Input() content: Payload;
    @Input() uriContext: Promise<IUriContext>;
    @Input() submit: () => void = null;
    @Input() reset: () => void = null;
    @Input() isDirty: () => void = null;
    @Input() isValid: () => void = null;
    @Input() updateCallback: (pristine: Payload, results: Payload) => void;
    @Input() customOnSubmit: (newContent: Payload) => Promise<GenericEditorOnSubmitResponse>;
    @Input() editorStackId: string;
    @Input() modalHeaderTitle: string;

    @Output() getApi: EventEmitter<GenericEditorAPI> = new EventEmitter<GenericEditorAPI>();
    @Output() submitChange: EventEmitter<() => void> = new EventEmitter<() => void>();
    @Output() resetChange: EventEmitter<() => void> = new EventEmitter<() => void>();
    @Output() isValidChange: EventEmitter<() => void> = new EventEmitter<() => void>();
    @Output() isDirtyChange: EventEmitter<() => void> = new EventEmitter<() => void>();

    @ViewChild('nativeForm', { static: false, read: ElementRef }) set nativeForm(
        element: ElementRef
    ) {
        if (!element) {
            return;
        }

        this.formInitialized$.next(element.nativeElement);
    }
    @ViewChild(ContentManager, { static: false }) public contentManager: ContentManager<
        GenericEditorOnSubmitResponse
    >;

    public editor: IGenericEditor = null;
    public componentForm: FormGroup = new FormGroup({});
    private showResetButton: boolean;
    private showSubmitButton: boolean;

    private formInitialized$: BehaviorSubject<HTMLFormElement> = new BehaviorSubject(null);
    private formSet$: BehaviorSubject<boolean> = new BehaviorSubject(false);

    constructor(
        private elementRef: ElementRef<HTMLElement>,
        private genericEditorFactoryService: GenericEditorFactoryService,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic
    ) {}

    ngOnChanges(changes: SimpleChanges): void {
        const hasRelevantChange = !!Object.keys(changes).find((key: string) =>
            changeTriggeringInputs.has(key)
        );

        if (!hasRelevantChange) {
            return;
        }

        if (this.editor) {
            this.editor._finalize();
        }

        const genericEditorConstructor = this.genericEditorFactoryService.getGenericEditorConstructor();

        this.editor = new genericEditorConstructor({
            id: this.id || stringUtils.generateIdentifier(),
            smarteditComponentType: this.smarteditComponentType,
            smarteditComponentId: this.smarteditComponentId,
            editorStackId: this.editorStackId,
            structureApi: this.structureApi,
            structure: this.structure,
            contentApi: this.contentApi,
            updateCallback: this.updateCallback,
            content: this.content,
            uriContext: this.uriContext,
            customOnSubmit: this.customOnSubmit,
            element: this.elementRef.nativeElement
        });

        this.getApi.emit(this.editor.api);
        this.editor.init();
    }

    ngOnInit() {
        // If no @Input is bound to generic editor the fields will be initialized with null, otherwise undefined

        this.showResetButton = isNull(this.reset);
        this.showSubmitButton = isNull(this.submit);

        // Two way data binding legacy support for api exposure

        this.submitChange.emit(() =>
            this.contentManager
                .save()
                .toPromise()
                .then((result) => result.response)
        );
        this.isValidChange.emit(() => this._isValid());
        this.isDirtyChange.emit(() => this._isDirty());
        this.resetChange.emit(() => this._reset());

        combineLatest(this.formSet$, this.formInitialized$)
            .pipe(
                filter((values) => values.every((value) => !!value)),
                take(1)
            )
            .subscribe(([_, el]) => this.editor.watchFormErrors(el));
    }

    ngOnDestroy(): void {
        this.editor._finalize();
    }

    ngAfterViewInit(): void {
        // Prevent enter key from triggering form submit
        this.yjQuery(this.yjQuery('.no-enter-submit')[0]).bind(
            'keypress',
            (key: JQueryEventObject) => {
                return key.key !== 'Enter';
            }
        );
    }

    setFormState(state: GenericEditorState) {
        (this.editor as any).setForm(state);

        this.formSet$.next(true);
    }

    showCommands(): boolean {
        return this.showCancel() || this.showSubmit();
    }

    showCancel(): boolean {
        return (
            this.editor.alwaysShowReset ||
            (this.showResetButton === true && this.editor.isDirty() && this.editor.isValid())
        );
    }

    showSubmit(): boolean {
        return (
            this.editor.alwaysShowSubmit ||
            (this.showSubmitButton === true && this.editor.isDirty() && this.editor.isValid())
        );
    }

    isSubmitDisabled = (): boolean => {
        return this.editor.isSubmitDisabled();
    };

    _reset(): Promise<any> {
        if (this.editor.onReset) {
            this.editor.onReset();
        }
        return this.editor.reset();
    }

    private _isValid(): boolean {
        return this.editor.isValid();
    }

    private _isDirty(): boolean {
        return this.editor ? this.editor.isDirty() : false;
    }
}
