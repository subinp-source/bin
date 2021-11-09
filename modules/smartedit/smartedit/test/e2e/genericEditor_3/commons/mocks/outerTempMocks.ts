/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { NgModule } from '@angular/core';

@SeEntryModule('TempMocks')
@NgModule({
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            required: true
                        },
                        {
                            nativeName: 'French',
                            isocode: 'fr',
                            required: true
                        },
                        {
                            nativeName: 'Italian',
                            isocode: 'it'
                        },
                        {
                            nativeName: 'Polish',
                            isocode: 'pl'
                        },
                        {
                            nativeName: 'Hindi',
                            isocode: 'hi'
                        }
                    ]
                });

                const medias = [
                    {
                        id: '1',
                        code: 'contextualmenu_delete_off',
                        description: 'contextualmenu_delete_off',
                        altText: 'contextualmenu_delete_off alttext',
                        realFileName: 'contextualmenu_delete_off.png',
                        url: '/test/e2e/genericEditor/images/contextualmenu_delete_off.png'
                    },
                    {
                        id: '2',
                        code: 'contextualmenu_delete_on',
                        description: 'contextualmenu_delete_on',
                        altText: 'contextualmenu_delete_on alttext',
                        realFileName: 'contextualmenu_delete_on.png',
                        url: '/test/e2e/genericEditor/images/contextualmenu_delete_on.png'
                    },
                    {
                        id: '3',
                        code: 'contextualmenu_edit_off',
                        description: 'contextualmenu_edit_off',
                        altText: 'contextualmenu_edit_off alttext',
                        realFileName: 'contextualmenu_edit_off.png',
                        url: '/test/e2e/genericEditor/images/contextualmenu_edit_off.png'
                    },
                    {
                        id: '3',
                        code: 'contextualmenu_edit_on',
                        description: 'contextualmenu_edit_on',
                        altText: 'contextualmenu_edit_on alttext',
                        realFileName: 'contextualmenu_edit_on.png',
                        url: '/test/e2e/genericEditor/images/contextualmenu_edit_on.png'
                    }
                ];

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/media\/(.+)/
                    )
                    .respond(function(method, url) {
                        const identifier = /media\/(.+)/.exec(url)[1];
                        const filtered = medias.filter(function(media) {
                            return media.code === identifier;
                        });
                        if (filtered.length === 1) {
                            return [200, filtered[0]];
                        } else {
                            return [404, null];
                        }
                    });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/media/)
                    .respond(function(method, url) {
                        return [
                            200,
                            {
                                media: medias
                            }
                        ];
                    });

                httpBackendService.whenGET(/i18n/).passThrough();
                httpBackendService.whenGET(/view/).passThrough(); // calls to storefront render API
                httpBackendService.whenPUT(/contentslots/).passThrough();
                httpBackendService.whenGET(/\.html/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class TempMocks {}
