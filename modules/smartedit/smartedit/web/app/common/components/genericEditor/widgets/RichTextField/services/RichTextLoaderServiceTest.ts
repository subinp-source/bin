/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { RichTextLoaderService } from './RichTextLoaderService';

describe('seRichTextLoaderService', () => {
    let seRichTextLoaderService: RichTextLoaderService;

    let originalCKEDITOR: any;

    beforeAll(() => {
        originalCKEDITOR = window.CKEDITOR;
    });

    afterAll(() => {
        window.CKEDITOR = originalCKEDITOR;
    });

    describe('load', () => {
        it('should return a resolved promise when CK Editor reports that it is loaded', async () => {
            window.CKEDITOR = {
                status: 'loaded'
            };

            seRichTextLoaderService = new RichTextLoaderService();

            await seRichTextLoaderService.load();

            expect((seRichTextLoaderService as any).checkLoadedInterval).toBeFalsy();
        });

        it('should return an unresolved promise when CK Editor is not loaded yet', () => {
            window.CKEDITOR = {
                status: 'dummyStatus'
            };
            seRichTextLoaderService = new RichTextLoaderService();

            expect((seRichTextLoaderService as any).checkLoadedInterval).toBeTruthy();
        });
    });
});
