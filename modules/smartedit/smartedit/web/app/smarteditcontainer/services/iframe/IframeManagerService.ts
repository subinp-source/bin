/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
    urlUtils,
    IConfiguration,
    ISharedDataService,
    LogService,
    SeDowngradeService,
    WindowUtils,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { DeviceSupport, DEVICE_SUPPORTS } from './DeviceSupportsValue';
import { DeviceOrientation, DEVICE_ORIENTATIONS } from './DeviceOrientationsValue';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:IframeManagerService
 *
 * @description
 * The iFrame Manager service provides methods to load the storefront into an iframe. The preview of the storefront can be loaded for a specified input homepage and a specified preview ticket. The iframe src attribute is updated with that information in order to display the storefront in SmartEdit.
 */
@SeDowngradeService()
export class IframeManagerService {
    private static readonly DEFAULT_PREVIEW_ROUTE: string = 'cx-preview';
    private currentLocation: string;

    constructor(
        private logService: LogService,
        private httpClient: HttpClient,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private windowUtils: WindowUtils,
        private sharedDataService: ISharedDataService
    ) {}

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IframeManagerService#setCurrentLocation
     * @methodOf smarteditServicesModule.service:IframeManagerService
     *
     * @description
     * This method sets the current page location and stores it in the service. The storefront will be loaded with this location.
     *
     * @param {String} URL Location to be stored
     */
    setCurrentLocation(location: string): void {
        this.currentLocation = location;
    }

    getIframe(): JQuery {
        return this.yjQuery(this.windowUtils.getSmarteditIframe());
    }

    isCrossOrigin(): boolean {
        return this.windowUtils.isCrossOrigin(this.currentLocation);
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IframeManagerService#load
     * @methodOf smarteditServicesModule.service:IframeManagerService
     *
     * @description
     * This method loads the storefront within an iframe by setting the src attribute to the specified input URL.
     * If this method is called within the context of a new or updated experience, prior to the loading, it will check if the page exists.
     * If the pages does not exist (the server returns a 404 and a content-type:text/html), the user will be redirected to the homepage of the storefront. Otherwise,
     * the user will be redirected to the requested page for the experience.
     *
     * @param {String} URL The URL of the storefront.
     * @param {Boolean =} checkIfFailingHTML Boolean indicating if we need to check if the page call returns a 404
     * @param {String =} homepageInPreviewMode URL of the storefront homepage in preview mode if it's a new experience
     *
     */
    load(url: string, checkIfFailingHTML?: boolean, pageInPreviewMode?: string): Promise<void> {
        if (checkIfFailingHTML) {
            return this._getPageAsync(url).then(
                () => {
                    this.getIframe().attr('src', url);
                },
                (error: any) => {
                    if (error.status === 404) {
                        this.getIframe().attr('src', pageInPreviewMode);
                        return;
                    }
                    this.logService.error(
                        `IFrameManagerService.load - _getPageAsync failed with error ${error}`
                    );
                }
            );
        } else {
            this.logService.debug('iframeManagerService::load - loading storefront url:', url);
            this.getIframe().attr('src', url);
            return Promise.resolve();
        }
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IframeManagerService#loadPreview
     * @methodOf smarteditServicesModule.service:IframeManagerService
     *
     * @description
     * This method loads the preview of the storefront for a specified input homepage URL or a page from the page list, and for a specified preview ticket.
     * This method will add '/cx-preview' as specified in configuration.storefrontPreviewRoute to the URI and append the preview ticket in the query string.
     * <br/>If it is an initial load,  {@link smarteditServicesModule.service:IframeManagerService#load load} will be called with this modified homepage or page from page list.
     * <br/>If it is a subsequent call, the modified homepage will be called through Ajax to initialize the preview (storefront constraint) and then
     * {@link smarteditServicesModule.service:IframeManagerService#load load} will be called with the current location.
     *
     * @param {String} homePageOrPageFromPageList The URL of the storefront homepage or a page from the page list for a given experience context.
     * @param {String} previewTicket The preview ticket.
     */
    loadPreview(homePageOrPageFromPageList: string, previewTicket: string): Promise<void> {
        this.windowUtils.setTrustedIframeDomain(homePageOrPageFromPageList);
        this.logService.debug('loading storefront iframe with preview ticket:', previewTicket);

        let promiseToResolve;

        if (!/.+\.html/.test(homePageOrPageFromPageList)) {
            // for testing purposes
            promiseToResolve = this._appendURISuffix(homePageOrPageFromPageList);
        } else {
            promiseToResolve = Promise.resolve(homePageOrPageFromPageList);
        }

        return promiseToResolve.then((previewURL: string) => {
            const pageInPreviewMode =
                previewURL +
                (previewURL.indexOf('?') === -1 ? '?' : '&') +
                'cmsTicketId=' +
                previewTicket;

            // If we don't have a current location, or the current location is the homePage or a page from page list, or the current location has a cmsTicketID
            if (this._mustLoadAsSuch(homePageOrPageFromPageList)) {
                return this.load(pageInPreviewMode);
            } else {
                const isCrossOrigin = this.isCrossOrigin();

                /*
                 * check failing HTML only if same origin to prevent CORS errors.
                 * if location to reload in new experience context is different from homepage, one will have to
                 * first load the home page in preview mode and then access the location without preview mode
                 */
                return (isCrossOrigin
                    ? Promise.resolve({})
                    : this._getPageAsync(pageInPreviewMode)
                ).then(
                    () => {
                        // FIXME: use gatewayProxy to load url from the inner
                        return this.load(this.currentLocation, !isCrossOrigin, pageInPreviewMode);
                    },
                    (error: any) => this.logService.error('failed to load preview', error)
                );
            }
        });
    }

    apply(deviceSupport?: DeviceSupport, deviceOrientation?: DeviceOrientation): void {
        let width;
        let height;
        let isVertical = true;

        if (deviceOrientation && deviceOrientation.orientation) {
            isVertical = deviceOrientation.orientation === 'vertical';
        }

        if (deviceSupport) {
            width = isVertical ? deviceSupport.width : deviceSupport.height;
            height = isVertical ? deviceSupport.height : deviceSupport.width;

            // hardcoded the name to default to remove the device skin
            this.getIframe()
                .removeClass()
                .addClass('device-' + (isVertical ? 'vertical' : 'horizontal') + ' device-default');
        } else {
            this.getIframe().removeClass();
        }
        this.getIframe().css({
            width: width || '100%',
            height: height || '100%',
            display: 'block',
            margin: 'auto'
        });
    }

    applyDefault(): void {
        const defaultDeviceSupport = DEVICE_SUPPORTS.find((deviceSupport: DeviceSupport) => {
            return deviceSupport.default;
        });
        const defaultDeviceOrientation = DEVICE_ORIENTATIONS.find(
            (deviceOrientation: DeviceOrientation) => {
                return deviceOrientation.default;
            }
        );
        this.apply(defaultDeviceSupport, defaultDeviceOrientation);
    }

    /*
     * if currentLocation is not set yet, it means that this is a first loading and we are trying to load the homepage,
     * or if the page has a ticket ID but is not the homepage, it means that we try to load a page from the page list.
     * For those scenarios, we want to load the page as such in preview mode.
     */
    private _mustLoadAsSuch(homePageOrPageFromPageList: string): boolean {
        return (
            !this.currentLocation ||
            urlUtils.getURI(homePageOrPageFromPageList) === urlUtils.getURI(this.currentLocation) ||
            'cmsTicketId' in urlUtils.parseQuery(this.currentLocation)
        );
    }

    private _getPageAsync(url: string): Promise<{}> {
        return this.httpClient.get(url, { observe: 'body', responseType: 'text' }).toPromise();
    }

    private _appendURISuffix(url: string): Promise<string> {
        const pair = url.split('?');
        return this.sharedDataService
            .get('configuration')
            .then((configuration: IConfiguration) => {
                if (!configuration || !configuration.storefrontPreviewRoute) {
                    this.logService.debug(
                        "SmartEdit configuration for 'storefrontPreviewRoute' is not found. Fallback to default value: '" +
                            IframeManagerService.DEFAULT_PREVIEW_ROUTE +
                            "'"
                    );
                    return IframeManagerService.DEFAULT_PREVIEW_ROUTE;
                }
                return configuration.storefrontPreviewRoute;
            })
            .then((previewRoute: string) => {
                return (
                    pair[0]
                        .replace(/(.+)([^\/])$/g, '$1$2/' + previewRoute)
                        .replace(/(.+)\/$/g, '$1/' + previewRoute) +
                    (pair.length === 2 ? '?' + pair[1] : '')
                );
            });
    }
}
