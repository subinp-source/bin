/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { FormBuilderModule, TranslationModule } from '@smart/utils';
import { GenericEditorComponent } from './GenericEditorComponent';
import { FundamentalsModule } from '../../FundamentalsModule';
import { SharedComponentsModule } from '../../SharedComponentsModule';
import { GenericEditorTabComponent } from './components/GenericEditorTabComponent';
import { GenericEditorFieldComponent } from './components/GenericEditorFieldComponent';
import { GenericEditorFieldMessagesComponent } from './components/GenericEditorFieldMessagesComponent';
import { GenericEditorFieldWrapperComponent } from './components/GenericEditorFieldWrapperComponent';
import { GenericEditorFactoryService } from './GenericEditorFactoryService';
import { EditorFieldMappingService } from './services/EditorFieldMappingService';
import { GenericEditorStackService } from './services/GenericEditorStackService';
import { GenericEditorTabService } from './services/GenericEditorTabService';
import { SeValidationMessageParser } from './services/SeValidationMessageParser';
import { FetchEnumDataHandler } from './services/FetchEnumDataHandler';
import { GenericEditorBreadcrumbComponent } from './components/breadcrumb';
import { YjqueryModule } from '../../services/vendors/YjqueryModule';
import { GenericEditorWidgetModule } from './widgets';
import { FormBuilderDirective } from './components/formBuilder/FormBuilderDirective';
import { GenericEditorStateBuilderService } from './services/GenericEditorStateBuilderService';
import { ContentManager } from './components/ContentManager';
import { SubmitBtnDirective } from './components/SubmitButtonDirective';
import { SelectModule } from '../select';

/**
 * Form Builder Setup
 */
import { LocalizedElementComponent } from './components/LocalizedElementComponent';
import { GenericEditorRootTabsComponent } from './components/rootTabs/GenericEditorRootTabsComponent';
import { GenericEditorDynamicFieldComponent } from './components/dynamicField/GenericEditorDynamicFieldComponent';

/**
 * @ngdoc overview
 * @name GenericEditorModule
 */
@NgModule({
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [
        CommonModule,
        YjqueryModule,
        FormsModule,
        ReactiveFormsModule,
        FundamentalsModule,
        SharedComponentsModule,
        GenericEditorWidgetModule,
        SelectModule,
        TranslationModule.forChild(),
        FormBuilderModule.forRoot({
            validators: {
                required: () => Validators.required
            },
            types: {
                tabs: GenericEditorRootTabsComponent,
                localized: LocalizedElementComponent,
                field: GenericEditorDynamicFieldComponent
            }
        })
    ],
    providers: [
        GenericEditorFactoryService,
        EditorFieldMappingService,
        FetchEnumDataHandler,
        GenericEditorStackService,
        GenericEditorTabService,
        SeValidationMessageParser,
        GenericEditorStateBuilderService
    ],
    declarations: [
        GenericEditorDynamicFieldComponent,
        GenericEditorComponent,
        GenericEditorTabComponent,
        LocalizedElementComponent,
        GenericEditorFieldComponent,
        GenericEditorFieldMessagesComponent,
        GenericEditorFieldWrapperComponent,
        GenericEditorBreadcrumbComponent,
        FormBuilderDirective,
        ContentManager,
        GenericEditorRootTabsComponent,
        SubmitBtnDirective
    ],
    entryComponents: [
        GenericEditorComponent,
        GenericEditorTabComponent,
        LocalizedElementComponent,
        GenericEditorFieldComponent,
        GenericEditorFieldMessagesComponent,
        GenericEditorFieldWrapperComponent,
        GenericEditorBreadcrumbComponent
    ],
    exports: [GenericEditorComponent, GenericEditorWidgetModule]
})
export class SeGenericEditorModule {}
