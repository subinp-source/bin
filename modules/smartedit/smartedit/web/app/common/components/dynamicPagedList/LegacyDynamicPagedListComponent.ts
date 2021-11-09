/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from '../../di';

/**
 * @ngdoc overview
 * @name dynamicPagedListModule
 * @description
 * @deprecated since 2005
 * # The dynamicPagedListModule
 *
 * The dynamicPagedListModule provides a directive to display a paginated list of items with custom renderers where the data is paged and fetched on demand from the provided API.
 * This directive also allows the user to search and sort the list.
 *
 */

/**
 * @ngdoc directive
 * @name dynamicPagedListModule.directive:DynamicPagedListComponent
 * @scope
 * @restrict E
 * @element dynamic-paged-list
 * @deprecated since 2005
 *
 * @description
 * Deprecated, use {@link smarteditCommonsModule.component:DynamicPagedListComponent}
 * Component responsible for displaying a server-side paginated list of items with custom renderers. It allows the user to search and sort the list.
 *
 * @param {DynamicPagedListConfig} config {@link smarteditCommonsModule.interface:DynamicPagedListConfig config}
 * @param {<String?} mask The string value used to filter the result.
 * @param {OnGetApiEvent} getApi {@link dynamicPagedListModule.interface:OnGetApiEvent OnGetApiEvent} Exposes the dynamic paged list module's api object
 * @param {OnItemsUpdateEvent} onItemsUpdate {@link  dynamicPagedListModule.interface:OnItemsUpdateEvent onItemsUpdate} Exposes the item list
 *
 * @example
 * <em>Example of a <strong>renderers</strong> object</em>
 *
 * <pre>
 *          renderers = {
 *              name: function(item, key) {
 *                  return "<a data-ng-click=\"$ctrl.config.injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>";
 *              }
 *          };
 * </pre>
 *
 * <em>Example of an <strong>injectedContext</strong> object</em>
 * <pre>
 *          injectedContext = {
 *              onLink: function(link) {
 *                  if (link) {
 *                      var experiencePath = this._buildExperiencePath();
 *                      iframeManagerService.setCurrentLocation(link);
 *                      $location.path(experiencePath);
 *                  }
 *              }.bind(this),
 *              dropdownItems: []
 *          };
 * </pre>
 *
 *
 */

@SeComponent({
    selector: 'dynamic-paged-list',
    templateUrl: 'legacyDynamicPagedListTemplate.html',
    inputs: ['config:=', 'mask:=?', 'getApi:&?', 'onItemsUpdate:&?']
})
export class LegacyDynamicPagedListComponent {}
