/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { TranslationModule } from '@smart/utils';

import { TreeComponent } from './TreeComponent';
import { TreeNodeRendererComponent } from './TreeNodeRendererComponent';
import { CompileHtmlModule } from '../../directives/CompileHtmlModule';
import { L10nPipeModule } from '../../pipes/l10n';
import { TreeNodeComponent } from './TreeNodeComponent';

/**
 * @ngdoc overview
 * @name TreeModule
 *
 * @description
 * A Angular module organizing the features of tree functionality
 */

@NgModule({
    imports: [
        CompileHtmlModule,
        DragDropModule,
        CommonModule,
        L10nPipeModule,
        TranslationModule.forChild()
    ],
    declarations: [TreeComponent, TreeNodeRendererComponent, TreeNodeComponent],
    exports: [TreeComponent, TreeNodeComponent],
    entryComponents: [TreeComponent, TreeNodeRendererComponent]
})
export class NgTreeModule {}
