///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///
export const CAROUSEL_VIEWED_EVENT = "cds.merchandising.carouselViewed";
export const CAROUSEL_CLICKED_EVENT = "cds.merchandising.carouselClicked";
const PROFILE_CAROUSEL_VIEW = "CarouselViewed";
const PROFILE_CAROUSEL_CLICKED = "CarouselClicked";

/**
 * ProfileCarouselEvent is an event to be raised to CDS upon carousel view or click.
 */
export class ProfileCarouselEvent {
    private name: string;
    private data: any;
    
    constructor(name: string, data: any) {
        this.name = name;
        this.data = data;
    }
}

/**
 * CarouselEventPayload is a generic carousel event.
 */
export class CarouselEventPayload {
    private data : any;
    constructor(data:any) {
        this.data = data;
    }
    public getData() {
        return this.data;
    }
}

/**
 * CarouselClickEventPayload is an event which handles the data required
 * to raise an event with CDS upon clicking on a carousel slot.
 */
export class CarouselClickEventPayload extends CarouselEventPayload {
    private slot : any;
    private productId : any;
    constructor(data:any, slot:any, productId:any)
    {
        super(data);
        this.slot = slot;
        this.productId = productId;
    }

    public getSlot() {
        return this.slot;
    }

    public getProductId() {
        return this.productId;
    }
}

/**
 * DataLayer is a class for handling receiving incoming caorusel click / view events and
 * formatting them as a profile event, before sending to CDS.
 */
export class DataLayer {
    initialize(){
        window.addEventListener(CAROUSEL_VIEWED_EVENT, (event: CustomEvent) => {
            this.handleViewEvent(event.detail);
        });

        window.addEventListener(CAROUSEL_CLICKED_EVENT, (event: CustomEvent) => {
            this.handleClickEvent(event.detail);
        });
    };

    handleViewEvent = (event : CarouselEventPayload) => {
        const eventData = event.getData();
        const skus = eventData.items.map(product => product.id);
        const payloadData = {
            'carouselId' : eventData.carouselId,
            'strategyId' : eventData.strategy,
            'productSkus' : skus,
            'metadata' : eventData.metadata,
            'carouselName' : eventData.name
        };
        const profileEvent = new ProfileCarouselEvent(PROFILE_CAROUSEL_VIEW, payloadData);
        this.pushEvent(profileEvent);
    }

    handleClickEvent = (event : CarouselClickEventPayload) => {
        const eventData = event.getData();
        const productImageUrl = eventData.items.filter(item => item.id === event.getProductId()).map(item => item.thumbnailImage)[0];
        const payloadData = {
            'carouselId' : eventData.carouselId,
            'strategyId' : eventData.strategy,
            'slotId' : event.getSlot(),
            'sku' : event.getProductId(),
            'imageUrl' : productImageUrl,
            'carouselName' : eventData.name,
            'metadata' : eventData.metadata
        };
        const profileEvent = new ProfileCarouselEvent(PROFILE_CAROUSEL_CLICKED, payloadData);
        this.pushEvent(profileEvent);
    }

    pushEvent = (event : ProfileCarouselEvent) => {
        if(window['Y_TRACKING'] && window['Y_TRACKING'].eventLayer) {
            window['Y_TRACKING'].eventLayer.push(event);
        }
    }
}