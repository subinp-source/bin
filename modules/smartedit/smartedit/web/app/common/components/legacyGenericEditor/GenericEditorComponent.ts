/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { SeComponent } from 'smarteditcommons/di';
import '../genericEditor/genericEditor.scss';

/**
 * @ngdoc directive
 * @name legacyGenericEditorModule.directive:genericEditor
 * @scope
 * @restrict E
 * @element generic-editor
 * @deprecated since 2005
 *
 * @description
 * @deprecated since 2005
 * Use {@link GenericEditorModule.component:GenericEditorComponent GenericEditorComponent} instead
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
 * @param {= Function =} customOnSubmit It exposes the inner onSubmit function to the invoker scope. If the parameter is set, the inner onSubmit function is overridden by the custom function and the custom function must return a promise in the response format expected by the generic editor.
 * @param {< String =} editorStackId When working with nested components, a generic editor can be opened from within another editor. This parameter is used to specify the stack of nested editors.
 */
@SeComponent({
    templateUrl: 'genericEditorComponentTemplate.html',
    inputs: [
        'id:=',
        'smarteditComponentId:=',
        'smarteditComponentType:=?',
        'contentApi:=',
        'content:=',
        'uriContext',
        'submit:=?',
        'reset:=?',
        'isDirty:=?',
        'isValid:=?',
        'getApi:&?',
        'customOnSubmit:=?',
        'structureApi',
        'structure',
        'updateCallback',
        'editorStackId'
    ]
})
export class GenericEditorComponent {
    public submit: () => void;
    public reset: () => void;
    public isDirty: () => boolean;
    public isValid: () => boolean;

    constructor(private $attrs: angular.IAttributes) {}

    $onInit() {
        this.submit = this.$attrs.hasOwnProperty('submit') ? this.submit : null;
        this.reset = this.$attrs.hasOwnProperty('reset') ? this.reset : null;
        this.isDirty = this.$attrs.hasOwnProperty('isDirty') ? this.isDirty : null;
        this.isValid = this.$attrs.hasOwnProperty('isValid') ? this.isValid : null;
    }
}
