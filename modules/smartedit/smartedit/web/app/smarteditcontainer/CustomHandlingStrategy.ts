/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { UrlHandlingStrategy, UrlTree } from '@angular/router/router';
import { NG_ROUTE_PREFIX } from 'smarteditcommons';

/*
 * any route not starting with 'ng' will be delegated to legacy Angular JS router
 */
export class CustomHandlingStrategy implements UrlHandlingStrategy {
    shouldProcessUrl(url: UrlTree): boolean {
        return url.toString().indexOf(NG_ROUTE_PREFIX + '/') > -1;
    }
    extract(url: UrlTree): UrlTree {
        return url;
    }
    merge(newUrlPart: UrlTree, rawUrl: UrlTree): UrlTree {
        return newUrlPart;
    }
}
