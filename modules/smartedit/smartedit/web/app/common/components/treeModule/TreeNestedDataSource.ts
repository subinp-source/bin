/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { BehaviorSubject, Observable } from 'rxjs';

export class TreeNestedDataSource<T> {
    private _data = new BehaviorSubject<T[]>([]);

    get(): Observable<T[]> {
        return this._data.asObservable();
    }

    set(data: T[]) {
        this._data.next(data);
    }
}
