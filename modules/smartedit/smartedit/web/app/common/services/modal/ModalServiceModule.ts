/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from '../../di';

import { ModalButtonStyles } from './ModalButtonStyles';
import { ModalButtonActions } from './ModalButtonActions';

/**
 * @ngdoc overview
 * @name modalServiceModule
 * @description
 * Module containing {@link modalServiceModule.service:ModalService}
 */

@SeModule({
    imports: ['ui.bootstrap', 'translationServiceModule', 'functionsModule', 'coretemplates'],
    providers: [
        {
            /**
             * @ngdoc object
             * @name modalServiceModule.object:MODAL_BUTTON_ACTIONS
             *
             * @description
             * Injectable angular constant<br/>
             * Defines the action to be taken after executing a button on a modal window. To be used when adding a button to the modal,
             * either when opening a modal (see {@link modalServiceModule.service:ModalManager#methods_getButtons ModalManager.getButtons()}) or
             * when adding a button to an existing modal (see {@link modalServiceModule.service:ModalService#methods_open modalService.open()})
             *
             * Example:
             * <pre>
             *      myModalManager.addButton({
             *          id: 'button id',
             *          label: 'close_modal',
             *          action: MODAL_BUTTON_ACTIONS.CLOSE
             *      });
             * </pre>
             */

            provide: 'MODAL_BUTTON_ACTIONS',
            useValue: {
                /**
                 * @ngdoc property
                 * @name NONE
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
                 *
                 * @description
                 * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button
                 * no action should be performed.
                 */
                NONE: ModalButtonActions.None,

                /**
                 * @ngdoc property
                 * @name CLOSE
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
                 *
                 * @description
                 * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button,
                 * the modal window should close, and the {@link https://docs.angularjs.org/api/ng/service/$q promise} returned by the modal should be resolved.
                 */
                CLOSE: ModalButtonActions.Close,

                /**
                 * @ngdoc property
                 * @name DISMISS
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
                 *
                 * @description
                 * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button,
                 * the modal window should close, and the {@link https://docs.angularjs.org/api/ng/service/$q promise} returned by the modal should be rejected.
                 */
                DISMISS: ModalButtonActions.Dismiss
            }
        },
        {
            /**
             * @ngdoc object
             * @name modalServiceModule.object:MODAL_BUTTON_STYLES
             *
             * @description
             * Injectable angular constant<br/>
             * Defines the look and feel of a button on a modal window. To be used when adding a button to the modal,
             * either when opening a modal (see {@link modalServiceModule.service:ModalManager#methods_getButtons ModalManager.getButtons()}) or
             * when adding a button to an existing modal (see {@link modalServiceModule.service:ModalService#methods_open modalService.open()})
             *
             * Example:
             * <pre>
             *      myModalManager.addButton({
             *          id: 'button id',
             *          label: 'cancel_button',
             *          style: MODAL_BUTTON_STYLES.SECONDARY
             *      });
             * </pre>
             */
            provide: 'MODAL_BUTTON_STYLES',
            useValue: {
                /**
                 * @ngdoc property
                 * @name DEFAULT
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
                 *
                 * @description
                 * Equivalent to SECONDARY
                 */
                DEFAULT: ModalButtonStyles.Default,
                /**
                 *
                 * @ngdoc property
                 * @name PRIMARY
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
                 *
                 * @description
                 * Indicates to the modal window that this button is the primary button of the modal, such as save or submit,
                 * and should be styled accordingly.
                 */
                PRIMARY: ModalButtonStyles.Primary,
                /**
                 * @ngdoc property
                 * @name SECONDARY
                 * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
                 *
                 * @description
                 * Indicates to the modal window that this button is a secondary button of the modal, such as cancel,
                 * and should be styled accordingly.
                 */
                SECONDARY: ModalButtonStyles.Default
            }
        }
    ]
})
export class ModalServiceModule {}
