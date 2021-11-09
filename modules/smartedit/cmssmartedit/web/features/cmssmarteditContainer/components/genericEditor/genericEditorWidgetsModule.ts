/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { RestrictionsListComponent } from './restrictionsList/RestrictionsListComponent';
import { DuplicatePrimaryNonContentPageComponent } from './pageRestore/duplicatePrimaryNonContentPage/DuplicatePrimaryNonContentPageComponent';
import { DuplicatePrimaryContentPageLabelComponent } from './pageRestore/duplicatePrimaryContentPageLabel/DuplicatePrimaryContentPageLabelComponent';
import { MissingPrimaryContentPageComponent } from './pageRestore/missingPrimaryContentPage/MissingPrimaryContentPageComponent';
import { WorkflowCreateVersionFieldComponent } from './workflowCreateVersionField/WorkflowCreateVersionFieldComponent';
import { PageInfoPageNameComponent } from './pageInfoPageName/pageInfoPageNameComponent';
import { InfoPageNameComponent } from './infoPageNameTemplate/infoPageNameComponent';

/**
 * @ngdoc overview
 * @name genericEditorWidgetsModule
 *
 * @description
 * Module containing all the generic editor widgets.
 */
@SeModule({
    imports: [],
    declarations: [
        RestrictionsListComponent,
        DuplicatePrimaryNonContentPageComponent,
        DuplicatePrimaryContentPageLabelComponent,
        MissingPrimaryContentPageComponent,
        WorkflowCreateVersionFieldComponent,
        PageInfoPageNameComponent,
        InfoPageNameComponent
    ]
})
export class GenericEditorWidgetsModule {}
