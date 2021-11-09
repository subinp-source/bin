/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('outerExtendingModule', []).run(function(featureService) {
    featureService.addToolbarItem({
        toolbarId: 'smartEditPerspectiveToolbar',
        key: 'dummyToolbar',
        type: 'HYBRID_ACTION',
        nameI18nKey: 'OVVERIDEN_DUMMYTOOLBAR',
        priority: 1,
        section: 'left',
        iconClassName: 'hyicon hyicon-addlg se-toolbar-menu-ddlb--button__icon',
        callback: function() {}
    });
});
