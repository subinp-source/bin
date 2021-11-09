/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
var FIELDS_MAPPING = {
    uid: 'uid-shortstring'
};

module.exports = {
    getField: function(fieldName) {
        var fieldID = FIELDS_MAPPING[fieldName] || fieldName;
        return element(by.css('#' + fieldID));
    },
    setFieldValue: function(fieldName, newValue) {
        return this.getField(fieldName)
            .clear()
            .sendKeys(newValue);
    },
    save: function() {
        return element(by.id('save')).click();
    }
};
