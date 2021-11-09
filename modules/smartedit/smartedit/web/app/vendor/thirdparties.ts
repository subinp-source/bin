/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
if (!(window as any).Zone) {
    /* tslint:disable-next-line */
    require('zone.js/dist/zone.js');
}

import * as lodash from 'lodash';

declare global {
    interface Window {
        smarteditLodash: lodash.LoDashStatic;
    }
}

window.smarteditLodash = lodash.noConflict();
