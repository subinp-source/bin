/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IContextualMenuButton, IContextualMenuConfiguration } from 'smarteditcommons';
export interface IHiddenComponentMenu {
    buttons: IContextualMenuButton[];
    configuration: IContextualMenuConfiguration;
}
