/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, HttpCode, Param, Post, Query } from '@nestjs/common';
import { MediaService } from '../services';
import { IMedia } from '../../fixtures/entities/media';

@Controller()
export class MediaController {
    constructor(private readonly mediaService: MediaService) {}
    @Post('cmswebservices/v1/catalogs/:catalogId/versions/:versionId/media*')
    @HttpCode(200)
    createMediaItem() {
        const mediaCount = this.mediaService.getMediaCount();
        const media: IMedia = {
            id: mediaCount + '',
            uuid: 'more_bckg.png',
            code: 'more_bckg.png',
            description: 'more_bckg.png',
            altText: 'more_bckg.png',
            realFileName: 'more_bckg.png',
            url: '/web/webroot/static-resources/images/more_bckg.png'
        };
        this.mediaService.addMedia(media);
        return media;
    }

    @Get('cmswebservices/v1/media/:uuid')
    getMediaItemByUUID(@Param('uuid') uuid: string) {
        const resultMedia: IMedia | undefined = this.mediaService.getMediaByCode(uuid);
        if (resultMedia) {
            return resultMedia;
        }
        return this.mediaService.getFirstMedia();
    }

    @Get('cmswebservices/v1/media*')
    getMediaItemByNamedQuery(@Query() query: any) {
        let resultMedia: IMedia[] = [];
        if (query.currentPage === '0') {
            const search: Map<string, string> = new Map();
            query.params.split(',').forEach((param: string) => {
                const paramSplit: string[] = param.split(':');
                search.set(paramSplit[0], paramSplit.length === 2 ? paramSplit[1] : '');
            });

            resultMedia = this.mediaService.filterMediaByInput(search.get('code'));
        }
        return { media: resultMedia };
    }
}
