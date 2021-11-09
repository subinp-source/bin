/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
const { resolve } = require('path');

const {
    group,
    webpack: { entry, alias }
} = require('../../smartedit-build/builders');

const commonsAlias = alias(
    'ysmarteditmodulecommons',
    resolve('./jsTarget/web/features/ysmarteditmodulecommons')
);

const smartedit = group(
    commonsAlias,
    alias('ysmarteditmodule', resolve('./jsTarget/web/features/ysmarteditmodule'))
);
const smarteditContainer = group(
    commonsAlias,
    alias('ysmarteditmodulecontainer', resolve('./jsTarget/web/features/ysmarteditmoduleContainer'))
);

module.exports = {
    ySmarteditKarma: () => group(smartedit),
    ySmarteditContainerKarma: () => group(smarteditContainer),
    ySmartedit: () =>
        group(
            smartedit,
            entry({
                ysmarteditmodule: resolve('./jsTarget/web/features/ysmarteditmodule/index.ts')
            })
        ),
    ySmarteditContainer: () =>
        group(
            smarteditContainer,
            entry({
                ysmarteditmoduleContainer: resolve(
                    './jsTarget/web/features/ysmarteditmoduleContainer/index.ts'
                )
            })
        )
};
