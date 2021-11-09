/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';

import {
    CrossFrameEventService,
    EVENTS,
    IAuthenticationService,
    ISessionService,
    SeDowngradeComponent
} from 'smarteditcommons';
import { IframeManagerService } from '../../services/iframe/IframeManagerService';

@SeDowngradeComponent()
@Component({
    selector: 'se-user-account',
    templateUrl: './UserAccountComponent.html'
})
export class UserAccountComponent implements OnInit, OnDestroy {
    public username: string;

    private unregUserChanged: () => void;

    constructor(
        private authenticationService: IAuthenticationService,
        private iframeManagerService: IframeManagerService,
        private crossFrameEventService: CrossFrameEventService,
        private sessionService: ISessionService
    ) {}

    ngOnInit() {
        this.unregUserChanged = this.crossFrameEventService.subscribe(EVENTS.USER_HAS_CHANGED, () =>
            this.getUsername()
        );
        this.getUsername();
    }

    ngOnDestroy() {
        this.unregUserChanged();
    }

    public signOut(): void {
        this.authenticationService.logout();
        this.iframeManagerService.setCurrentLocation(null);
    }

    public getUsername(): void {
        this.sessionService.getCurrentUserDisplayName().then((displayName: string) => {
            this.username = displayName;
        });
    }
}
