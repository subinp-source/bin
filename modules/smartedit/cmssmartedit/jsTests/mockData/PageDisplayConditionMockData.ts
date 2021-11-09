/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export class PageDisplayCondition {
    PRIMARY = {
        label: 'page.displaycondition.primary',
        description: 'page.displaycondition.primary.description',
        isPrimary: true
    };
    VARIANT = {
        label: 'page.displaycondition.variation',
        description: 'page.displaycondition.variation.description',
        isPrimary: false
    };
    ALL = [
        {
            label: 'page.displaycondition.primary',
            description: 'page.displaycondition.primary.description',
            isPrimary: true
        },
        {
            label: 'page.displaycondition.variation',
            description: 'page.displaycondition.variation.description',
            isPrimary: false
        }
    ];
}
