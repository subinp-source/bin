/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    rarelyChangingContent,
    userEvictionTag,
    Cached,
    CryptographicUtils,
    GatewayProxied,
    IRestService,
    ISessionService,
    IStorageService,
    LogService,
    PREVIOUS_USERNAME_HASH,
    RestServiceFactory,
    SeDowngradeService,
    User,
    WHO_AM_I_RESOURCE_URI
} from 'smarteditcommons';

/* @internal */
interface IWhoAmIData {
    displayName: string;
    uid: string;
}

/* @internal */
interface IUserData {
    uid: string;
    readableLanguages: string[];
    writeableLanguages: string[];
}
/** @internal */

@SeDowngradeService(ISessionService)
@GatewayProxied(
    'getCurrentUsername',
    'getCurrentUserDisplayName',
    'hasUserChanged',
    'setCurrentUsername',
    'getCurrentUser'
)
export class SessionService extends ISessionService {
    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------
    private USER_DATA_URI = '/cmswebservices/v1/users/:userUid';

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    private cachedUserHash: string;
    private whoAmIService: IRestService<IWhoAmIData>;
    private userRestService: IRestService<IUserData>;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    constructor(
        private $log: LogService,
        restServiceFactory: RestServiceFactory,
        private storageService: IStorageService,
        private cryptographicUtils: CryptographicUtils
    ) {
        super();
        this.whoAmIService = restServiceFactory.get<IWhoAmIData>(WHO_AM_I_RESOURCE_URI);
        this.userRestService = restServiceFactory.get<IUserData>(this.USER_DATA_URI);
    }

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------
    public getCurrentUserDisplayName(): Promise<string> {
        return this.getCurrentUserData().then((currentUserData) => currentUserData.displayName);
    }

    public getCurrentUsername(): Promise<string> {
        return this.getCurrentUserData().then((currentUserData) => currentUserData.uid);
    }

    public getCurrentUser(): Promise<User> {
        return this.getCurrentUserData();
    }

    public hasUserChanged(): Promise<boolean> {
        const prevHashPromise = Promise.resolve(
            this.cachedUserHash
                ? this.cachedUserHash
                : this.storageService.getItem(PREVIOUS_USERNAME_HASH)
        );
        return prevHashPromise.then((prevHash: string) => {
            return this.whoAmIService.get({}).then((currentUserData: IWhoAmIData) => {
                return (
                    !!prevHash && prevHash !== this.cryptographicUtils.sha1Hash(currentUserData.uid)
                );
            });
        });
    }

    public setCurrentUsername(): Promise<void> {
        return this.whoAmIService.get({}).then((currentUserData: IWhoAmIData) => {
            // NOTE: For most of SmartEdit operation, it is enough to store the previous user hash in the cache.
            // However, if the page is refreshed the cache is cleaned. Therefore, it's necessary to also store it in
            // a cookie through the storageService.
            this.cachedUserHash = this.cryptographicUtils.sha1Hash(currentUserData.uid);
            this.storageService.setItem(PREVIOUS_USERNAME_HASH, this.cachedUserHash);
        });
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    @Cached({ actions: [rarelyChangingContent], tags: [userEvictionTag] })
    private getCurrentUserData(): Promise<User> {
        return this.whoAmIService
            .get({})
            .then((whoAmIData: IWhoAmIData) => {
                return this.userRestService
                    .get({
                        userUid: whoAmIData.uid
                    })
                    .then((userData: IUserData) => {
                        return {
                            uid: userData.uid,
                            displayName: whoAmIData.displayName,
                            readableLanguages: userData.readableLanguages,
                            writeableLanguages: userData.writeableLanguages
                        };
                    });
            })
            .catch((reason: any) => {
                this.$log.warn("[SessionService]: Can't load session information", reason);
                return null;
            });
    }
}
