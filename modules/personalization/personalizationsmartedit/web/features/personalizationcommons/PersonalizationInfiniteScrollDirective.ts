import {Directive, ElementRef, EventEmitter, Inject, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {fromEvent, Observable, Subscription} from 'rxjs';


@Directive({
	selector: '[persoInfiniteScroll]'
})
export class PersonalizationInfiniteScrollDirective implements OnInit, OnDestroy {

	@Input() scrollPercent: number = 75;
	@Output() onScrollAction = new EventEmitter<any>();

	private scrollEvent: Observable<any>;
	private subscription: Subscription;

	constructor(@Inject(ElementRef) public element: ElementRef) {}

	ngOnInit(): void {
		this.scrollEvent = fromEvent(this.element.nativeElement, 'scroll');

		this.subscription = this.scrollEvent.subscribe((e: any) => {
			if ((e.target.scrollTop + e.target.offsetHeight) / e.target.scrollHeight > this.scrollPercent / 100) {
				this.onScrollAction.emit(null);
			}
		});
	}

	ngOnDestroy(): void {
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
	}

}