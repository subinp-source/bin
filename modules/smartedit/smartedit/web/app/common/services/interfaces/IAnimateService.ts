/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

export abstract class IAnimateService implements angular.animate.IAnimateService {
    addClass(
        element: JQuery<HTMLElement>,
        className: string,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    animate(
        element: JQuery<HTMLElement>,
        from: any,
        to: any,
        className?: string,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    cancel(animationPromise: angular.animate.IAnimationPromise): void {
        return undefined;
    }

    closeAndFlush(): void {
        return undefined;
    }

    enabled(element: JQuery<HTMLElement>, value?: boolean): boolean;
    enabled(value?: boolean): boolean;
    enabled(element?: JQuery | boolean, value?: boolean): boolean {
        return false;
    }

    enter(
        element: JQuery<HTMLElement>,
        parentElement: JQuery<HTMLElement>,
        afterElement?: JQuery<HTMLElement>,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    flush(): void {
        return undefined;
    }

    leave(
        element: JQuery<HTMLElement>,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    move(
        element: JQuery<HTMLElement>,
        parentElement: JQuery<HTMLElement>,
        afterElement?: JQuery<HTMLElement>
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    off(
        event: string,
        container?: JQuery<HTMLElement>,
        callback?: (element?: JQuery, phase?: string) => any
    ): void {
        return undefined;
    }

    on(
        event: string,
        container: JQuery<HTMLElement>,
        callback: (element?: JQuery, phase?: string) => any
    ): void {
        return undefined;
    }

    pin(element: JQuery<HTMLElement>, parentElement: JQuery<HTMLElement>): void {
        return undefined;
    }

    removeClass(
        element: JQuery<HTMLElement>,
        className: string,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }

    setClass(
        element: JQuery<HTMLElement>,
        add: string,
        remove: string,
        options?: angular.animate.IAnimationOptions
    ): angular.animate.IAnimationPromise {
        return undefined;
    }
}
