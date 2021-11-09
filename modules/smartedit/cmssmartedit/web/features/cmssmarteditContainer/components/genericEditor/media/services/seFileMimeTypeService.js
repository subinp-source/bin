/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seFileMimeTypeServiceModule', [])
    .factory('seFileReader', function() {
        var read = function(file, config) {
            var fileReader = new FileReader();
            config = config || {};
            fileReader.onloadend = config.onLoadEnd;
            fileReader.onerror = config.onError;

            fileReader.readAsArrayBuffer(file);
            return fileReader;
        };

        return {
            read: read
        };
    })
    .factory('seFileMimeTypeService', function(seFileReader, $q, settingsService) {
        var _validateMimeTypeFromFile = function(loadedFile, mimeTypes) {
            var fileAsBytes = new Uint8Array(loadedFile).subarray(0, 8);
            var header = fileAsBytes.reduce(function(header, byte) {
                var byteAsStr = byte.toString(16);
                if (byteAsStr.length === 1) {
                    byteAsStr = '0' + byteAsStr;
                }
                header += byteAsStr;
                return header;
            }, '');
            return mimeTypes.some(function(mimeTypeCode) {
                return header.toLowerCase().indexOf(mimeTypeCode.toLowerCase()) === 0;
            });
        };

        var isFileMimeTypeValid = function(file) {
            var deferred = $q.defer();
            $q.when(settingsService.getStringList('smartedit.validImageMimeTypeCodes')).then(
                function(mimeTypes) {
                    seFileReader.read(file, {
                        onLoadEnd: function(e) {
                            if (_validateMimeTypeFromFile(e.target.result, mimeTypes)) {
                                deferred.resolve();
                            } else {
                                deferred.reject();
                            }
                        },
                        onError: function() {
                            deferred.reject();
                        }
                    });
                },
                function() {
                    deferred.reject(false);
                }
            );
            return deferred.promise;
        };

        return {
            isFileMimeTypeValid: isFileMimeTypeValid
        };
    });
