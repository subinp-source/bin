angular
    .module('personalizationsearchSearchProfilesContextServiceModule', []).
factory('personalizationsearchSearchProfilesContextService', function() {
    var self = this;

    var SearchProfileActionContext = function() {
        this.customizationCode = undefined;
        this.variationCode = undefined;
        this.initialOrder = [];
        this.searchProfilesOrder = [];
    };

    this.searchProfileActionComparer = function(a1, a2) {
        return a1.type === a2.type && a1.searchProfileCode === a2.searchProfileCode && a1.searchProfileCatalog === a2.searchProfileCatalog;
    };

    this.getSearchProfileActionContext = function() {
        return new SearchProfileActionContext();
    };

    this.searchProfileContext = this.getSearchProfileActionContext();

    this.updateSearchActionContext = function(actions) {
        actions.forEach(function(action) {
            var searchProfileActions = self.searchProfileContext.searchProfilesOrder.filter(function(spAction) {
                return self.searchProfileActionComparer(action, spAction);
            });

            if (searchProfileActions.length > 0) {
                searchProfileActions[0].code = action.code;
            }
        });

    };

    self.isDirty = function() {
        return false;
    };

    return self;
});
