/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('yMessageApp', ['coretemplates', 'templateCacheDecoratorModule', 'yMessageModule']);
angular.module('smarteditloader').requires.push('yMessageApp');
angular.module('smarteditcontainer').requires.push('yMessageApp');
