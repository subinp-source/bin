/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@nestjs/common';
import { IMedia } from '../../fixtures/entities/media';
import { media } from '../../fixtures/constants/media';

@Injectable()
export class MediaService {
    private currentMedia: IMedia[];

    constructor() {
        this.currentMedia = [...media];
    }

    getMedia() {
        return this.currentMedia;
    }

    getMediaByCode(code: string) {
        return this.currentMedia.find((m: IMedia) => m.code === code);
    }

    getFirstMedia() {
        return this.currentMedia[0];
    }

    filterMediaByInput(input: string | undefined) {
        return input
            ? this.currentMedia.filter(
                  (mediaItem: IMedia) =>
                      Object.values(mediaItem).find((propertyValue: string) =>
                          propertyValue.includes(input)
                      ) !== undefined
              )
            : this.currentMedia;
    }

    addMedia(m: IMedia) {
        this.currentMedia.push(m);
    }

    getMediaCount() {
        return this.currentMedia.length;
    }
}
