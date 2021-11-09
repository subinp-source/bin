/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { SeDirective } from '../di';

@SeDirective({
    selector: '[include-replace]',
    require: 'ngInclude'
})
export class IncludeReplaceDirective {
    constructor(private $element: JQuery) {}

    $postLink() {
        this.$element.replaceWith(this.$element.children());
    }
}
