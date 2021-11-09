/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export {
    IFundamentalModalConfig,
    IFundamentalModalButtonOptions,
    FundamentalModalButtonStyle,
    FundamentalModalButtonAction,
    FundamentalModalManagerService,
    IModalConfig,
    IModalButtonOptions,
    ModalButtonActions,
    ModalButtonStyles,
    IModalService
} from '@smart/utils';

export { defaultButtonOptions } from './constants';
export { modalControllerClassFactory } from './modalControllerClassFactory';
export { ModalManager } from './ModalManager';
export { ModalService } from './ModalService';
export { ModalServiceModule } from './ModalServiceModule';
export * from './IUIBootstrapModalService';
export * from './IUIBootstrapModalStackService';
