/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, ElementRef, Input, OnChanges, Renderer2 } from '@angular/core';

/**
 * Used for rendering dynamic components decorated with @SeCustomComponent.
 * It is meant for configurations that requires component to be sent through MessageGateway in postMessage payload.
 * Instead "component" use "componentName" as a configuration parameter.
 *
 * Due to {@link https://developer.mozilla.org/en-US/docs/Web/API/Window/postMessage postMessageApi},
 * component class / constructor function, cannot be sent through postMessage (it is removed).
 *
 * Note: Component must be also registered in @NgModule entryComponents array.
 * See HotkeyNotificationModule as an example usage.
 *
 * @example
 *
 * @SeCustomComponent()
 * @Component({
 *   selector: 'se-my-custom-component',
 *   templateUrl: './SeMyComponent.html'
 * })
 * export class MyCustomComponent {}
 *
 * @Component({
 *   selector: 'se-my-container',
 *   template: `<div [seCustomComponentOutlet]="'MyCustomComponent'"></div>`
 * })
 * export class MyContainer {}
 *
 * @NgModule({
 *   imports: [CustomComponentOutletDirectiveModule]
 *   declarations: [MyContainer, MyCustomComponent],
 *   entryComponents: [MyContainer, MyCustomComponent]
 * })
 * export class MyModule {}
 *
 * @internal
 */
@Directive({
    selector: '[seCustomComponentOutlet]'
})
export class CustomComponentOutletDirective implements OnChanges {
    /** Component Name corresponding to smarteditDecoratorPayloads registry */
    @Input('seCustomComponentOutlet') componentName: string;
    private component: HTMLElement;
    private hasView = false;

    constructor(private elementRef: ElementRef<HTMLElement>, private renderer: Renderer2) {}

    ngOnChanges() {
        this.updateView(this.componentName);
    }

    private updateView(componentName: string): void {
        if (componentName && !this.hasView) {
            this.createView();
        } else if (!componentName && this.hasView) {
            this.removeView();
        } else if (componentName && this.hasView) {
            this.removeView();
            this.createView();
        }
    }

    private createView(): void {
        const componentMetadata = window.__smartedit__.getComponentDecoratorPayload(
            this.componentName
        );
        this.component = this.renderer.createElement(componentMetadata.selector);
        this.renderer.appendChild(this.elementRef.nativeElement, this.component);

        this.hasView = true;
    }

    private removeView(): void {
        this.renderer.removeChild(this.elementRef.nativeElement, this.component);

        this.hasView = false;
    }
}
