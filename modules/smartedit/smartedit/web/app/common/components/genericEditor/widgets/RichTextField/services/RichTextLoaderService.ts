/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from 'smarteditcommons/di';

@SeDowngradeService()
export class RichTextLoaderService {
    private loadPromise: Promise<void>;
    private checkLoadedInterval: number;

    constructor() {
        this.loadPromise = new Promise((resolve) => {
            this.checkLoadedInterval = (setInterval(() => {
                if (CKEDITOR.status === 'loaded') {
                    resolve();
                    clearInterval(this.checkLoadedInterval);
                    this.checkLoadedInterval = null;
                }
            }, 100) as unknown) as number;
        });
    }

    load(): Promise<void> {
        return this.loadPromise;
    }
}
