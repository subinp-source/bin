/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeFactory } from '../di';

declare const $script: any;

export class ScriptUtils {
    injectJS(): {
        getInjector: () => any;
        execute: (conf: { srcs: string[]; callback: SeFactory; index?: number }) => void;
    } {
        function getInjector() {
            return $script;
        }

        return {
            getInjector,
            execute(conf: { srcs: string[]; callback: SeFactory; index?: number }) {
                const srcs = conf.srcs;
                let index = conf.index;
                const callback = conf.callback;
                if (!srcs.length) {
                    callback();
                    return;
                }
                if (index === undefined) {
                    index = 0;
                }
                if (srcs[index] !== undefined) {
                    this.getInjector()(srcs[index], () => {
                        if (index + 1 < srcs.length) {
                            this.execute({
                                srcs,
                                index: index + 1,
                                callback
                            });
                        } else if (typeof callback === 'function') {
                            callback();
                        }
                    });
                }
            }
        };
    }
}

export const scriptUtils = new ScriptUtils();
