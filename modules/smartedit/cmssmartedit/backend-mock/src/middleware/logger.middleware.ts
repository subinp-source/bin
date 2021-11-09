/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export function logger(req: any, res: any, next: any) {
    const oldWrite = res.write;
    const oldEnd = res.end;

    const chunks: any[] = [];

    res.write = (...restArgs: any[]) => {
        chunks.push(Buffer.from(restArgs[0]));
        oldWrite.apply(res, restArgs);
    };

    res.end = (...restArgs: any[]) => {
        if (restArgs[0]) {
            chunks.push(Buffer.from(restArgs[0]));
        }

        if (res.statusCode !== 304) {
            console.log(`${req.method} ${req.originalUrl}`);
            console.log(`${res.statusCode}`);
        }

        oldEnd.apply(res, restArgs);
    };
    next();
}
