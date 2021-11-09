/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { bootstrapService } from './BootstrapService';

/* forbiddenNameSpaces angular.module:false */
angular.element(document).ready(() => {
    bootstrapService.bootstrap();
});
