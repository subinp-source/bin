/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

/**
 * A service that is responsible for indicating the bootstrap status of legacy AngularJS application to ensure the
 * legacy dependencies are available.
 */
@Injectable()
export class AngularJSBootstrapIndicatorService {
    private _smarteditContainerReady: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
        false
    );
    private _smarteditReady: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    public setSmarteditContainerReady(): void {
        this._smarteditContainerReady.next(true);
    }

    public setSmarteditReady(): void {
        this._smarteditReady.next(true);
    }

    /**
     * Notifies about the availability of legacySmarteditContainer
     */
    public onSmarteditContainerReady(): Observable<boolean> {
        return this._smarteditContainerReady.pipe(filter((isReady: boolean) => isReady));
    }

    /**
     * Notifies about the availability of legacySmartedit
     */
    public onSmarteditReady(): Observable<boolean> {
        return this._smarteditReady.pipe(filter((isReady: boolean) => isReady));
    }
}
