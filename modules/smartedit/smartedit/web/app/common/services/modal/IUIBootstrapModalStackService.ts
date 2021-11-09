/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as uib from 'angular-ui-bootstrap';

export abstract class IUIBootstrapModalStackService implements uib.IModalStackService {
    close(modalInstance: uib.IModalInstanceService, result?: any): void {
        return undefined;
    }

    dismiss(modalInstance: uib.IModalInstanceService, reason?: any): void {
        return undefined;
    }

    dismissAll(reason?: any): void {
        return undefined;
    }

    getTop(): uib.IModalStackedMapKeyValuePair {
        return undefined;
    }

    open(modalInstance: uib.IModalInstanceService, modal: any): void {
        return undefined;
    }
}
