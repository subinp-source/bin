/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModalButtonActions } from './ModalButtonActions';
import { ModalButtonStyles } from './ModalButtonStyles';

/**
 * @ngdoc interface
 * @name modalServiceModule.interface:IModalButtonOptions
 *
 * @description
 * Interface for IModalButtonOptions
 */

export interface IModalButtonOptions {
    /**
     * @ngdoc property
     * @name id
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * id: String
     *
     * The key used to identify button
     */
    id: string;

    /**
     * @ngdoc property
     * @name label
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * label: String
     *
     * Translation key
     */

    label: string;
    /**
     * @ngdoc property
     * @name action
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * action: {@link modalServiceModule.object:ModalButtonActions ModalButtonActions}
     *
     * Used to define what action button should perform after click
     */
    action?: ModalButtonActions;
    /**
     * @ngdoc property
     * @name style
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * style: {@link modalServiceModule.object:ModalButtonStyles ModalButtonStyles}
     *
     * Property used to style the button
     */
    style?: ModalButtonStyles;
    /**
     * @ngdoc property
     * @name disabled
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * disabled: Boolean
     *
     * Decides whether button is disabled or not
     */
    disabled?: boolean;

    /**
     * @ngdoc method
     * @name callback
     * @propertyOf modalServiceModule.interface:IModalButtonOptions
     * @description
     * callback: () => void || Promise<any>
     *
     * Method triggered when button is pressed
     */
    callback?: () => void | Promise<any>;
}
