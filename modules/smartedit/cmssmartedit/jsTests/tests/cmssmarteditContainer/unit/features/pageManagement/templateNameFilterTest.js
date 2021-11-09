/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('Page wizard template search filter test', function() {
    var $filter;

    var templates = [
        {
            name: 'Homepage Template'
        },
        {
            name: 'FAQ Template'
        },
        {
            name: 'Contact Us'
        },
        {
            name: 'Mobile Homepage Template'
        },
        {
            name: 'Responsive Homepage Template'
        }
    ];

    beforeEach(angular.mock.module('selectPageTemplateModule'));

    beforeEach(inject(function(_$filter_) {
        $filter = _$filter_;
    }));

    it('name filter returns a couple results', function() {
        var criteria = 'Template';
        var filterResult = [];

        filterResult = $filter('templateNameFilter')(templates, criteria);
        expect(filterResult).not.toBeNull();
        expect(filterResult[0].name).toBe('Homepage Template');
        expect(filterResult.length).toBe(4);
    });

    it('name filter returns no results', function() {
        var criteria = 'testing';
        var filterResult = [];

        filterResult = $filter('templateNameFilter')(templates, criteria);
        expect(filterResult).not.toBeNull();
        expect(filterResult).toEqual([]);
        expect(filterResult.length).toBe(0);
    });

    it('name filter returns one result testing case sensitivity', function() {
        var criteria = 'mob';
        var filterResult = [];

        filterResult = $filter('templateNameFilter')(templates, criteria);
        expect(filterResult[0].name).toBe('Mobile Homepage Template');
        expect(filterResult.length).toBe(1);
    });

    it('criteria is empty, filter returns original templates', function() {
        var criteria = ' ';
        var filterResult = [];

        filterResult = $filter('templateNameFilter')(templates, criteria);
        expect(filterResult.length).toBe(5);
    });

    it('criteria is null, filter returns original templates', function() {
        var filterResult = [];
        var criteria = null;

        filterResult = $filter('templateNameFilter')(templates, criteria);
        expect(filterResult.length).toBe(5);
    });
});
