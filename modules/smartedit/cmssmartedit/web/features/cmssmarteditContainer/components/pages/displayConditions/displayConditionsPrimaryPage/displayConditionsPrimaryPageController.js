/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('displayConditionsPrimaryPageControllerModule', ['pageServiceModule'])
    .controller('displayConditionsPrimaryPageController', function(
        $q,
        pageService,
        displayConditionsFacade
    ) {
        this.associatedPrimaryPageModel = null;
        this.associatedPrimaryPageLabelI18nKey = 'se.cms.display.conditions.primary.page.label';

        this.triggerOnPrimaryPageSelect = function() {
            this.onPrimaryPageSelect({
                primaryPage: this.associatedPrimaryPage
            });
        };

        this.fetchStrategy = {
            fetchEntity: function() {
                return $q.when({
                    id: this.associatedPrimaryPage.uid,
                    label: this.associatedPrimaryPage.name
                });
            }.bind(this),
            fetchPage: function(search, pageSize, currentPage) {
                return displayConditionsFacade.getPrimaryPagesForPageType(this.pageType, null, {
                    search: search,
                    pageSize: pageSize,
                    currentPage: currentPage
                });
            }.bind(this)
        };

        this.$onChanges = function(changes) {
            var associatedPrimaryPageChange = changes.associatedPrimaryPage;
            if (associatedPrimaryPageChange && !!associatedPrimaryPageChange.currentValue) {
                this._setAssociatedPrimaryPageSelected(associatedPrimaryPageChange.currentValue);
            }
        };

        this.associatedPrimaryPageModelOnChange = function _associatedPrimaryPageModelOnChange(
            uid
        ) {
            pageService.getPageById(uid).then(
                function(page) {
                    this._setAssociatedPrimaryPageSelected(page);
                    this.triggerOnPrimaryPageSelect();
                }.bind(this)
            );
        }.bind(this);

        this._setAssociatedPrimaryPageSelected = function(page) {
            this.associatedPrimaryPage = page;
            this.associatedPrimaryPageModel = page.uid;
        }.bind(this);
    });
