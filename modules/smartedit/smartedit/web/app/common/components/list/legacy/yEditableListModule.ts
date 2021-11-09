/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from '../../../di';
import { YEditableListComponent } from './yEditableList';
import { TreeModule } from '../../tree';

/**
 * @ngdoc overview
 * @name yEditableListModule
 * @description
 *
 * The yEditableList module contains a component which allows displaying a list of elements. The items in
 * that list can be added, removed, and re-ordered.
 *
 */

@SeModule({
    imports: [TreeModule],
    declarations: [YEditableListComponent]
})
export class YEditableListModule {}
