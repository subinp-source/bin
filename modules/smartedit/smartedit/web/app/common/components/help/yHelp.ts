/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from '../../di';

/**
 * @ngdoc directive
 * @name smarteditCommonsModule.component:yHelp
 * @scope
 * @deprecated since 2005, use {@link smarteditCommonsModule.component:HelpComponent HelpComponent}
 * @restrict E
 * @element y-help
 *
 * @description
 * Deprecated, use {@link smarteditCommonsModule.component:HelpComponent HelpComponent}
 */

@SeComponent({
    selector: 'y-help',
    templateUrl: 'yHelpTemplate.html',
    inputs: ['title', 'template', 'templateUrl']
})
export class YHelpComponent {
    public template: string;
    public templateUrl: string;
    public title: string;

    $onChanges(changes: { template: string }) {
        if (this.template && changes.template) {
            this.template = '<div>' + this.template + '</div>';
        }
    }
}
