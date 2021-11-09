/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ChangeDetectionStrategy,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    Output,
    ViewChild
} from '@angular/core';

/**
 * Represents Search Input for Select Component.
 *
 * @internal
 */
@Component({
    selector: 'se-select-search-input',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.se-select-search-input]': 'true'
    },
    templateUrl: './SearchInputComponent.html'
})
export class SearchInputComponent {
    @Input() isDisabled: boolean;
    @Input() isReadOnly: boolean;
    @Input() placeholder: string;
    @Input() search: string;

    @Output() searchKeyup = new EventEmitter<{ event: Event; value: string }>();
    @Output() searchChange = new EventEmitter<string>();

    @ViewChild('searchInput', { static: false }) searchInput: ElementRef;

    constructor(private elementRef: ElementRef) {}

    get nativeElement() {
        return this.elementRef.nativeElement;
    }

    get inputElement() {
        return this.searchInput.nativeElement;
    }

    public focus(): void {
        this.inputElement.focus();
    }

    public onChange(value: string) {
        this.searchChange.emit(value);
    }

    public onKeyup(event: KeyboardEvent): void {
        const value = (event.target as HTMLInputElement).value;
        this.searchKeyup.emit({ event, value });
    }
}
