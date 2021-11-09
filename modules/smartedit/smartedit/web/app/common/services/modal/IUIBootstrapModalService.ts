/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as uib from 'angular-ui-bootstrap';
import * as angular from 'angular';

export abstract class IUIBootstrapModalService implements uib.IModalService {
    getPromiseChain(): angular.IPromise<any> {
        return null;
    }

    open(options: uib.IModalSettings): uib.IModalInstanceService {
        return null;
    }
}
