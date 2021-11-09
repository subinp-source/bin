/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@nestjs/common';
import { cloneDeep } from 'lodash';
import { pageMocks } from '../../fixtures/constants/pages';
import { IPageMock } from '../../fixtures/entities/pages';
import { IVersion } from '../../fixtures/entities/versions';

@Injectable()
export class VersionsService {
    private pageMockVersions: IPageMock[];

    constructor() {
        this.pageMockVersions = cloneDeep(pageMocks);
    }

    filterVersionsByMask(siteId: string, pageUUID: string, mask: string) {
        const pageMock: IPageMock | undefined = this.getPageMock(siteId, pageUUID);

        if (pageMock) {
            return pageMock.versions.filter((version: IVersion) =>
                !!mask ? version.label.toUpperCase().indexOf(mask.toUpperCase()) > -1 : true
            );
        }
        return [];
    }

    updatePageMockVersion(
        siteId: string,
        pageUUID: string,
        versionId: string,
        newVersion: IVersion
    ) {
        const pageMock: IPageMock | undefined = this.getPageMock(siteId, pageUUID);
        if (pageMock) {
            pageMock.versions = pageMock.versions.map((version: IVersion) => {
                const tempVersion = Object.assign({}, version);
                if (tempVersion.uid === versionId) {
                    tempVersion.description = newVersion.description;
                    tempVersion.label = newVersion.label;
                }
                return tempVersion;
            });
        }
    }

    getPageMock(siteId: string, pageUUID: string) {
        return this.pageMockVersions.find(
            (pageMock: IPageMock) => pageMock.siteId === siteId && pageMock.pageUUID === pageUUID
        );
    }

    deletePageVersionByID(versionID: string) {
        this.pageMockVersions[0].versions = this.pageMockVersions[0].versions.filter(
            (version: IVersion) => version.uid !== versionID
        );
    }

    getPageVersions() {
        return this.pageMockVersions;
    }

    sliceVersions(currentPage: number, pageSize: number, versions: IVersion[]) {
        if (!Number.isNaN(currentPage) && !Number.isNaN(pageSize)) {
            return versions.slice(currentPage * pageSize, currentPage * pageSize + pageSize);
        }
        return versions;
    }

    refreshPageMockVersions() {
        this.pageMockVersions = cloneDeep(pageMocks);
    }
}
