import {SeDirective} from "smarteditcommons";

@SeDirective({
	selector: '[personalization-current-element]'
})
export class PersonalizationCurrentElementDirective {

	constructor(
		private $scope: angular.IScope,
		private $element: JQuery<HTMLElement>,
		private $attrs: angular.IAttributes
	) {
	}

	$postLink(): void {
		if (this.$attrs.personalizationCurrentElement) {
			this.$scope.$parent.$eval(this.$attrs.personalizationCurrentElement)(this.$element.parent());
		}
	}
}
