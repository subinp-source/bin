import * as angular from 'angular';
import {SeDirective} from "smarteditcommons";

@SeDirective({
	selector: '[personalization-infinite-scroll]'
})
export class PersonalizationInfiniteScrollDirectiveOld {

	constructor(
		private $scope: angular.IScope,
		private $element: JQuery<HTMLElement>,
		private $attrs: angular.IAttributes,
		private $rootScope: angular.IScope,
		private yjQuery: JQueryStatic,
		private $window: any,
		private $timeout: any
	) {
	}

	$postLink(): void {

		let checkWhenEnabled: any;
		let handler: any;
		let scrollDistance: any;
		let scrollEnabled: any;

		this.$window = angular.element(this.$window);
		scrollDistance = 0;
		if (this.$attrs.personalizationInfiniteScrollDistance !== null) {
			this.$scope.$parent.$watch(this.$attrs.personalizationInfiniteScrollDistance, (value: string) => {
				scrollDistance = parseInt(value, 10);
				return scrollDistance;
			});
		}
		scrollEnabled = true;
		checkWhenEnabled = false;
		if (this.$attrs.personalizationInfiniteScrollDisabled !== null) {
			this.$scope.$parent.$watch(this.$attrs.personalizationInfiniteScrollDisabled, (value: boolean) => {
				scrollEnabled = !value;
				if (scrollEnabled && checkWhenEnabled) {
					checkWhenEnabled = false;
					return handler();
				}
			});
		}
		this.$rootScope.$on('refreshStart', () => {
			this.$element.animate({
				scrollTop: "0"
			});
		});
		handler = () => {
			let container: any;
			let elementBottom: any;
			let remaining: any;
			let shouldScroll: any;
			let containerBottom: any;

			if (this.$element.children().length <= 0) {
				return;
			}
			container = this.yjQuery(this.$element.children()[0]);
			elementBottom = this.$element.offset().top + this.$element.height();
			containerBottom = container.offset().top + container.height();
			remaining = containerBottom - elementBottom;
			shouldScroll = remaining <= this.$element.height() * scrollDistance;
			if (shouldScroll && scrollEnabled) {
				if (this.$rootScope.$$phase) {
					return this.$scope.$parent.$eval(this.$attrs.personalizationInfiniteScroll);
				} else {
					return this.$scope.$parent.$apply(this.$attrs.personalizationInfiniteScroll);
				}
			} else if (shouldScroll) {
				checkWhenEnabled = true;
				return checkWhenEnabled;
			}
		};
		this.$element.on('scroll', handler);
		this.$scope.$parent.$on('$destroy', () => {
			return this.$window.off('scroll', handler);
		});
		return this.$timeout((() => {
			if (this.$attrs.personalizationInfiniteScrollImmediateCheck) {
				if (this.$scope.$parent.$eval(this.$attrs.personalizationInfiniteScrollImmediateCheck)) {
					return handler();
				}
			} else {
				return handler();
			}
		}), 0);

	}
}
