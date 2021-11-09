/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { SeComponent } from '../../di';

/**
 * @ngdoc overview
 * @name yMessageModule
 * @description
 * Deprecated since 2005. Use se-message (MessageComponent).
 * This module provides the yMessage component, which is responsible for rendering contextual
 * feedback messages for the user actions.
 *
 * @deprecated since 2005.
 */

/**
 *  @ngdoc directive
 *  @name yMessageModule.component:yMessage
 *  @scope
 *  @restrict E
 *  @element yMessage
 *
 *  @deprecated since 2005
 *  @description
 *  Deprecated since 2005. Use Use se-message (MessageComponent)
 *  This component provides contextual feedback messages for the user actions. To provide title and description for the yMessage
 *  use transcluded elements: message-title and message-description.
 *  @param {@String=} messageId Id for the component.
 *  @param {@String} type The type of the component (danger, info, success, warning). Default: info
 */

@SeComponent({
    templateUrl: 'yMessage.html',
    transclude: {
        messageTitle: '?messageTitle',
        messageDescription: '?messageDescription'
    },
    inputs: ['messageId:@', 'type:@']
})
export class YMessageComponent {
    public messageId: string;
    public type: string;

    constructor(
        public $element: JQuery,
        public $compile: angular.ICompileService,
        private $scope: angular.IScope
    ) {}

    $postLink() {
        // Dynamically compile transcluded content and append as Angular content child
        // This is necessary as we are unable to project transcluded content, as AngularJS is throwing error
        // about parent directive missing transclude flag.

        const messageTitleTarget: JQuery = this.$element.find('#y-message-title');
        const messageDescriptionTarget: JQuery = this.$element.find('#y-message-description');
        const messageTitleSource: JQuery = this.$element.find('#y-message-title-source');
        const messageDescriptionSource: JQuery = this.$element.find(
            '#y-message-description-source'
        );

        if (messageTitleSource[0].firstChild) {
            messageTitleTarget.append(
                this.$compile(messageTitleSource[0].firstChild as HTMLElement)(this.$scope)[0]
            );
        }

        if (messageDescriptionSource[0].firstChild) {
            messageDescriptionTarget.append(
                this.$compile(messageDescriptionSource[0].firstChild as HTMLElement)(this.$scope)[0]
            );
        }
    }
}
