/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgTreeModule } from '../treeModule/TreeModule';
import { DropdownMenuModule } from '../dropdown/dropdownMenu';
import { EditableListDefaultItem } from './EditableListDefaultItem';
import { EditableListComponent } from './EditableListComponent';

@NgModule({
    imports: [CommonModule, NgTreeModule, DropdownMenuModule],
    declarations: [EditableListComponent, EditableListDefaultItem],
    entryComponents: [EditableListComponent, EditableListDefaultItem],
    exports: [EditableListComponent, EditableListDefaultItem]
})
export class EditableListModule {}
