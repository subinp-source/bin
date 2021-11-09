/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
unit.mockData.pageDisplayCondition = function() {
    //function values() {
    this.PRIMARY = {
        label: 'page.displaycondition.primary',
        description: 'page.displaycondition.primary.description',
        isPrimary: true
    };
    this.VARIANT = {
        label: 'page.displaycondition.variation',
        description: 'page.displaycondition.variation.description',
        isPrimary: false
    };
    this.ALL = [
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
};
