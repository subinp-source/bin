import { log } from './utils';

/**
 * Data source for Merch Carousel
 */
export class DataService {
	static readonly CONSENT_REFERENCE = 'merchandising-consent-reference';

	private tenant: string;
	private strategy: string;
	private numberToDisplay: number;
	private category: string;
	private facets: string;
	private url: string;
	private siteId: string;
	private language: string;
	private productId: string;
	private consentReference: string;

	constructor(strategy: string, url: string, numberToDisplay: number) {
		this.tenant = this._getTenantId();
		this.strategy = strategy;
		this.numberToDisplay = numberToDisplay;
		this.category = this._getCategory();
		this.facets = this._getFacets();
		this.siteId = this._getSiteId();
		this.language = this._getLanguage();
		this.productId = this._getProductId();
		this.url = this._buildUrl(url);
		this.consentReference = window.localStorage.getItem(DataService.CONSENT_REFERENCE);
	}

	/**
	 * Retrieves products for the carousel
	 *
	 * @returns {Promise<Object>}
	 */
	getProducts() {
		const headers = this.consentReference ? { 'consent-reference': this.consentReference } : null;
		return $.ajax({
			type: 'GET',
			url: this.url,
			headers
		});
	}

	/**
	 * Build URL based on context data
	 * @param {string} rootUrl
	 * @returns {string|null}
	 */
	_buildUrl(rootUrl) {
		let url = null;

		if (!this.strategy || !this.tenant) {
			return null
		}

		url = rootUrl + `/${this.tenant}/strategies/${this.strategy}/products`;

		let queryString = '';
		if (this.category) {
			queryString = this._buildQueryString(queryString, `category=${this.category}`);
		}

		if (this.facets) {
			queryString = this._buildQueryString(queryString, `facets=${this.facets}`);
		}

		if (this.numberToDisplay) {
			queryString = this._buildQueryString(queryString, `pageSize=${this.numberToDisplay}`);
		}

		if(this.siteId) {
			queryString = this._buildQueryString(queryString, `site=${this.siteId}`);
		}

		if(this.language) {
			queryString = this._buildQueryString(queryString, `language=${this.language}`);
		}

		if(this.productId) {
			queryString = this._buildQueryString(queryString, `products=${this.productId}`);
		}

		if (queryString) {
			url = url + `?` + queryString;
		}
		return url;
	}

	/**
	 * Help to update the qs
	 * @param queryString {string}
	 * @param valueToAdd {string}
	 */
	_buildQueryString(queryString: string, valueToAdd: string): string {
		if (queryString) {
			queryString += '&';
		}
		queryString += valueToAdd;
		return queryString;
	}

	/**
	 * Parses tenant ID from the ACC context
	 * @returns {string|null}
	 */
	_getTenantId() {
		let id = null;

		try {
			const data = JSON.parse(ACC.addons.merchandisingaddon.hybrisTenant);
			id = data.properties.hybrisTenant;
		} catch (e) {
			log('JSON parser error', e);
		}

		return id;
	}

	/**
	 * Parse category from the ACC context
	 * @returns {string|null}
	 */
	_getCategory() {
		let id = null;

		try {
			if(ACC.addons.merchandisingaddon.ItemCategory) {
				const data = JSON.parse(ACC.addons.merchandisingaddon.ItemCategory);
				id = data.properties.ItemCategory;
			}
		} catch (e) {
			log('JSON parser error', e);
		}

		return id;
	}

	/**
	 * Parses product ID from the ACC context
	 * @returns {string|null}
	 */
	_getProductId() {
		let id = null;

		try {
			const data = JSON.parse(ACC.addons.merchandisingaddon.product);
			id = data.properties.product;
		} catch (e) {
			log('JSON parser error', e);
		}

		return id;
	}

	/**
	 * Parse facets from the ACC context
	 * @returns {string|null}
	 */
	_getFacets() {
		let facetsString = null;

		try {
			if(ACC.addons.merchandisingaddon.ContextFacets) {
				const data = JSON.parse(ACC.addons.merchandisingaddon.ContextFacets);
				facetsString = '';

				data.properties.ContextFacets.forEach((facetObject) => {
					facetObject.values.forEach((facetValue) => {
						facetsString += `${facetObject.code}:${facetValue}:`;
					});
				});
			}
		} catch (e) {
			log('JSON parser error', e);
		}

		return facetsString;
	}
	 
	 /**
	 * Parses site ID from the ACC context
	 * @returns {string|null}
	 */
	_getSiteId() {
		let id = null;

		try {
			const data = JSON.parse(ACC.addons.merchandisingaddon.siteId);
			id = data.properties.siteId;
		} catch (e) {
			log('JSON parser error', e);
		}

		return id;
	}

	/**
	 * Parses current language in use from the ACC context
	 * @returns {string|null}
	 */
	_getLanguage() {
		let id = null;

		try {
			const data = JSON.parse(ACC.addons.merchandisingaddon.language);
			id = data.properties.language;
		} catch (e) {
			log('JSON parser error', e);
		}

		return id;
	}

	/**
	 * Set the user's consent reference for the carousel and in local storage for future calls
	 *
	 * @param consentReference The user's consent reference
	 */
	setConsentReference(consentReference: string) {
		this.consentReference = consentReference;
		window.localStorage.setItem(DataService.CONSENT_REFERENCE, consentReference);
	}

}
