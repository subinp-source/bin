/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @internal
 */
export const INTERNAL_PROP_NAME = '$$internal';

/**
 * @internal
 * Adds an internal property on the component model for
 * watching property changes on an object.
 */
export class InternalProperty {
    private _map = new Map<string, (value: any) => void>();

    /**
     * Watch property changes.
     *
     * @param {string} property
     * @param fn A function
     */
    watch(property: string, fn: (value: any) => void): () => void {
        this._map.set(property, fn);

        return () => {
            this._map.delete(property);
        };
    }

    /**
     * Trigger prop change.
     *
     * @param {string} property
     * @param value New value
     */
    trigger(property: string, value: any): void {
        if (this._map.has(property)) {
            this._map.get(property)(value);
        }
    }
}

/**
 * @internal
 * Creates a proxied object to listen on property changes
 * for backwards compatibility with object mutations made by
 * widgets. This is used to proxy the model data called component in the
 * generic editor. The component data is the model that is
 * used for submitting to the backend. Old widgets mutate the
 * properties of component object, thus there is not way to
 * listen on properties changes except for the use of the ES6 Proxy
 * API. Some properties that are watched inside of the GenericEditorField
 * update the value of the AbstractForm of Angular used for validation.
 *
 * NOTE:
 * This function uses Proxy which is not supported in IE.
 */
export const proxifyDataObject = (obj: any) => {
    const internal = new InternalProperty();
    Object.defineProperty(obj, INTERNAL_PROP_NAME, {
        get: () => internal
    });
    return new Proxy(obj, {
        set(target, prop, value) {
            target[prop] = value;
            target[INTERNAL_PROP_NAME].trigger(prop, value);

            return true;
        }
    });
};
