/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@nestjs/common';
import { AdjustmentPayload } from './../interfaces';

/** Store information about fixture adjustment */
@Injectable()
export class StorageService {
    private modificationFixtures: Map<number, AdjustmentPayload> = new Map();
    private replacementFixtures: Map<number, AdjustmentPayload> = new Map();

    storeModificationFixture(payload: AdjustmentPayload) {
        const id = this.generateKey(payload);
        const regExpArray = payload.url.map((url: string | RegExp) => new RegExp(url));
        payload.url = regExpArray;
        this.modificationFixtures.set(id, payload);
        return id;
    }

    storeReplacementFixture(payload: AdjustmentPayload) {
        const id = this.generateKey(payload);
        const regExpArray = payload.url.map((url: string | RegExp) => new RegExp(url));
        payload.url = regExpArray;
        this.replacementFixtures.set(id, payload);
        return id;
    }

    replaceFixture(requestURL: string) {
        for (const value of this.replacementFixtures.values()) {
            for (const url of value.url) {
                if (url.test(requestURL)) {
                    return value.replace;
                }
            }
        }
        return undefined;
    }

    modifyFixture(requestURL: string, body: any) {
        for (const value of this.modificationFixtures.values()) {
            for (const url of value.url) {
                if (url.test(requestURL)) {
                    this.updateProperties(body, value.replace);
                }
            }
        }
    }

    removeFixture(fixtureID: string) {
        const id: number = parseInt(fixtureID, 10);
        this.modificationFixtures.delete(id) || this.replacementFixtures.delete(id);
    }

    removeAllFixtures() {
        this.modificationFixtures.clear();
        this.replacementFixtures.clear();
    }

    private updateProperties(fixture: any, newProperties: any) {
        Object.keys(newProperties).forEach((key: string) => {
            let currObject = fixture;
            const subProperties: string[] = key.split('.');
            const len: number = subProperties.length;
            for (let i = 0; i < len - 1; i++) {
                if (!currObject.hasOwnProperty(subProperties[i])) {
                    return;
                }
                currObject = currObject[subProperties[i]];
            }
            currObject[subProperties[len - 1]] = newProperties[key];
        });
    }

    private generateKey(payload: any) {
        const s = JSON.stringify(payload);
        let res = 0;
        for (let i = 0; i < s.length; i++) {
            res = (Math.imul(31, res) + s.charCodeAt(i)) | 0;
        }
        return res;
    }
}
