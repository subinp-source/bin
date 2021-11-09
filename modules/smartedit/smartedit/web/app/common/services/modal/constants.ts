/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IModalButtonOptions } from './IModalButtonOptions';
import { ModalButtonStyles } from './ModalButtonStyles';
import { ModalButtonActions } from './ModalButtonActions';

export const defaultButtonOptions: IModalButtonOptions = {
    id: 'button.id',
    label: 'button.label',
    action: ModalButtonActions.None,
    style: ModalButtonStyles.Primary,
    disabled: false,
    callback: null
};
