/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./jsTarget/web/features/merchandisingsmartedit/index.ts");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js":
/*!*******************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************/
/*! exports provided: ɵangular_packages_core_core_q, ɵangular_packages_core_core_n, ɵangular_packages_core_core_o, ɵangular_packages_core_core_p, ɵangular_packages_core_core_r, ɵangular_packages_core_core_f, ɵangular_packages_core_core_l, ɵangular_packages_core_core_m, ɵangular_packages_core_core_k, ɵangular_packages_core_core_j, ɵangular_packages_core_core_b, ɵangular_packages_core_core_a, ɵangular_packages_core_core_c, ɵangular_packages_core_core_d, ɵangular_packages_core_core_e, ɵangular_packages_core_core_i, ɵangular_packages_core_core_s, ɵangular_packages_core_core_u, ɵangular_packages_core_core_t, ɵangular_packages_core_core_x, ɵangular_packages_core_core_v, ɵangular_packages_core_core_w, ɵangular_packages_core_core_ba, ɵangular_packages_core_core_bb, ɵangular_packages_core_core_bc, ɵangular_packages_core_core_bd, ɵangular_packages_core_core_be, ɵangular_packages_core_core_bm, ɵangular_packages_core_core_bl, ɵangular_packages_core_core_g, ɵangular_packages_core_core_h, ɵangular_packages_core_core_bg, ɵangular_packages_core_core_bk, ɵangular_packages_core_core_bh, ɵangular_packages_core_core_bi, ɵangular_packages_core_core_bn, ɵangular_packages_core_core_y, ɵangular_packages_core_core_z, createPlatform, assertPlatform, destroyPlatform, getPlatform, PlatformRef, ApplicationRef, createPlatformFactory, NgProbeToken, enableProdMode, isDevMode, APP_ID, PACKAGE_ROOT_URL, PLATFORM_INITIALIZER, PLATFORM_ID, APP_BOOTSTRAP_LISTENER, APP_INITIALIZER, ApplicationInitStatus, DebugElement, DebugEventListener, DebugNode, asNativeElements, getDebugNode, Testability, TestabilityRegistry, setTestabilityGetter, TRANSLATIONS, TRANSLATIONS_FORMAT, LOCALE_ID, MissingTranslationStrategy, ApplicationModule, wtfCreateScope, wtfLeave, wtfStartTimeRange, wtfEndTimeRange, Type, EventEmitter, ErrorHandler, Sanitizer, SecurityContext, Attribute, ANALYZE_FOR_ENTRY_COMPONENTS, ContentChild, ContentChildren, Query, ViewChild, ViewChildren, Component, Directive, HostBinding, HostListener, Input, Output, Pipe, NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, ViewEncapsulation, Version, VERSION, InjectFlags, ɵɵdefineInjectable, defineInjectable, ɵɵdefineInjector, forwardRef, resolveForwardRef, Injectable, Injector, ɵɵinject, inject, INJECTOR, ReflectiveInjector, ResolvedReflectiveFactory, ReflectiveKey, InjectionToken, Inject, Optional, Self, SkipSelf, Host, ɵ0, ɵ1, NgZone, ɵNoopNgZone, RenderComponentType, Renderer, Renderer2, RendererFactory2, RendererStyleFlags2, RootRenderer, COMPILER_OPTIONS, Compiler, CompilerFactory, ModuleWithComponentFactories, ComponentFactory, ɵComponentFactory, ComponentRef, ComponentFactoryResolver, ElementRef, NgModuleFactory, NgModuleRef, NgModuleFactoryLoader, getModuleFactory, QueryList, SystemJsNgModuleLoader, SystemJsNgModuleLoaderConfig, TemplateRef, ViewContainerRef, EmbeddedViewRef, ViewRef, ChangeDetectionStrategy, ChangeDetectorRef, DefaultIterableDiffer, IterableDiffers, KeyValueDiffers, SimpleChange, WrappedValue, platformCore, ɵALLOW_MULTIPLE_PLATFORMS, ɵAPP_ID_RANDOM_PROVIDER, ɵdefaultIterableDiffers, ɵdefaultKeyValueDiffers, ɵdevModeEqual, ɵisListLikeIterable, ɵChangeDetectorStatus, ɵisDefaultChangeDetectionStrategy, ɵConsole, ɵsetCurrentInjector, ɵgetInjectableDef, ɵAPP_ROOT, ɵDEFAULT_LOCALE_ID, ɵivyEnabled, ɵCodegenComponentFactoryResolver, ɵclearResolutionOfComponentResourcesQueue, ɵresolveComponentResources, ɵReflectionCapabilities, ɵRenderDebugInfo, ɵ_sanitizeHtml, ɵ_sanitizeStyle, ɵ_sanitizeUrl, ɵglobal, ɵlooseIdentical, ɵstringify, ɵmakeDecorator, ɵisObservable, ɵisPromise, ɵclearOverrides, ɵinitServicesIfNeeded, ɵoverrideComponentView, ɵoverrideProvider, ɵNOT_FOUND_CHECK_ONLY_ELEMENT_INJECTOR, ɵgetLocalePluralCase, ɵfindLocaleData, ɵLOCALE_DATA, ɵLocaleDataIndex, ɵɵattribute, ɵɵattributeInterpolate1, ɵɵattributeInterpolate2, ɵɵattributeInterpolate3, ɵɵattributeInterpolate4, ɵɵattributeInterpolate5, ɵɵattributeInterpolate6, ɵɵattributeInterpolate7, ɵɵattributeInterpolate8, ɵɵattributeInterpolateV, ɵɵdefineBase, ɵɵdefineComponent, ɵɵdefineDirective, ɵɵdefinePipe, ɵɵdefineNgModule, ɵdetectChanges, ɵrenderComponent, ɵRender3ComponentFactory, ɵRender3ComponentRef, ɵɵdirectiveInject, ɵɵinjectAttribute, ɵɵinjectPipeChangeDetectorRef, ɵɵgetFactoryOf, ɵɵgetInheritedFactory, ɵɵsetComponentScope, ɵɵsetNgModuleScope, ɵɵtemplateRefExtractor, ɵɵProvidersFeature, ɵɵInheritDefinitionFeature, ɵɵNgOnChangesFeature, ɵLifecycleHooksFeature, ɵRender3NgModuleRef, ɵmarkDirty, ɵNgModuleFactory, ɵNO_CHANGE, ɵɵcontainer, ɵɵnextContext, ɵɵelementStart, ɵɵnamespaceHTML, ɵɵnamespaceMathML, ɵɵnamespaceSVG, ɵɵelement, ɵɵlistener, ɵɵtext, ɵɵtextInterpolate, ɵɵtextInterpolate1, ɵɵtextInterpolate2, ɵɵtextInterpolate3, ɵɵtextInterpolate4, ɵɵtextInterpolate5, ɵɵtextInterpolate6, ɵɵtextInterpolate7, ɵɵtextInterpolate8, ɵɵtextInterpolateV, ɵɵembeddedViewStart, ɵɵprojection, ɵɵpipeBind1, ɵɵpipeBind2, ɵɵpipeBind3, ɵɵpipeBind4, ɵɵpipeBindV, ɵɵpureFunction0, ɵɵpureFunction1, ɵɵpureFunction2, ɵɵpureFunction3, ɵɵpureFunction4, ɵɵpureFunction5, ɵɵpureFunction6, ɵɵpureFunction7, ɵɵpureFunction8, ɵɵpureFunctionV, ɵɵgetCurrentView, ɵgetDirectives, ɵgetHostElement, ɵɵrestoreView, ɵɵcontainerRefreshStart, ɵɵcontainerRefreshEnd, ɵɵqueryRefresh, ɵɵviewQuery, ɵɵstaticViewQuery, ɵɵstaticContentQuery, ɵɵloadViewQuery, ɵɵcontentQuery, ɵɵloadContentQuery, ɵɵelementEnd, ɵɵhostProperty, ɵɵproperty, ɵɵpropertyInterpolate, ɵɵpropertyInterpolate1, ɵɵpropertyInterpolate2, ɵɵpropertyInterpolate3, ɵɵpropertyInterpolate4, ɵɵpropertyInterpolate5, ɵɵpropertyInterpolate6, ɵɵpropertyInterpolate7, ɵɵpropertyInterpolate8, ɵɵpropertyInterpolateV, ɵɵupdateSyntheticHostBinding, ɵɵcomponentHostSyntheticListener, ɵɵprojectionDef, ɵɵreference, ɵɵenableBindings, ɵɵdisableBindings, ɵɵallocHostVars, ɵɵelementContainerStart, ɵɵelementContainerEnd, ɵɵelementContainer, ɵɵstyling, ɵɵstyleMap, ɵɵstyleSanitizer, ɵɵclassMap, ɵɵclassMapInterpolate1, ɵɵclassMapInterpolate2, ɵɵclassMapInterpolate3, ɵɵclassMapInterpolate4, ɵɵclassMapInterpolate5, ɵɵclassMapInterpolate6, ɵɵclassMapInterpolate7, ɵɵclassMapInterpolate8, ɵɵclassMapInterpolateV, ɵɵstyleProp, ɵɵstylePropInterpolate1, ɵɵstylePropInterpolate2, ɵɵstylePropInterpolate3, ɵɵstylePropInterpolate4, ɵɵstylePropInterpolate5, ɵɵstylePropInterpolate6, ɵɵstylePropInterpolate7, ɵɵstylePropInterpolate8, ɵɵstylePropInterpolateV, ɵɵstylingApply, ɵɵclassProp, ɵɵelementHostAttrs, ɵɵselect, ɵɵtextBinding, ɵɵtemplate, ɵɵembeddedViewEnd, ɵstore, ɵɵload, ɵɵpipe, ɵwhenRendered, ɵɵi18n, ɵɵi18nAttributes, ɵɵi18nExp, ɵɵi18nStart, ɵɵi18nEnd, ɵɵi18nApply, ɵɵi18nPostprocess, ɵi18nConfigureLocalize, ɵɵi18nLocalize, ɵsetLocaleId, ɵsetClassMetadata, ɵɵresolveWindow, ɵɵresolveDocument, ɵɵresolveBody, ɵcompileComponent, ɵcompileDirective, ɵcompileNgModule, ɵcompileNgModuleDefs, ɵpatchComponentDefWithScope, ɵresetCompiledComponents, ɵflushModuleScopingQueueAsMuchAsPossible, ɵtransitiveScopesFor, ɵcompilePipe, ɵɵsanitizeHtml, ɵɵsanitizeStyle, ɵɵdefaultStyleSanitizer, ɵɵsanitizeScript, ɵɵsanitizeUrl, ɵɵsanitizeResourceUrl, ɵɵsanitizeUrlOrResourceUrl, ɵbypassSanitizationTrustHtml, ɵbypassSanitizationTrustStyle, ɵbypassSanitizationTrustScript, ɵbypassSanitizationTrustUrl, ɵbypassSanitizationTrustResourceUrl, ɵgetLContext, ɵNG_ELEMENT_ID, ɵNG_COMPONENT_DEF, ɵNG_DIRECTIVE_DEF, ɵNG_PIPE_DEF, ɵNG_MODULE_DEF, ɵNG_BASE_DEF, ɵNG_INJECTABLE_DEF, ɵNG_INJECTOR_DEF, ɵcompileNgModuleFactory__POST_R3__, ɵisBoundToModule__POST_R3__, ɵSWITCH_COMPILE_COMPONENT__POST_R3__, ɵSWITCH_COMPILE_DIRECTIVE__POST_R3__, ɵSWITCH_COMPILE_PIPE__POST_R3__, ɵSWITCH_COMPILE_NGMODULE__POST_R3__, ɵgetDebugNode__POST_R3__, ɵSWITCH_COMPILE_INJECTABLE__POST_R3__, ɵSWITCH_IVY_ENABLED__POST_R3__, ɵSWITCH_CHANGE_DETECTOR_REF_FACTORY__POST_R3__, ɵCompiler_compileModuleSync__POST_R3__, ɵCompiler_compileModuleAsync__POST_R3__, ɵCompiler_compileModuleAndAllComponentsSync__POST_R3__, ɵCompiler_compileModuleAndAllComponentsAsync__POST_R3__, ɵSWITCH_ELEMENT_REF_FACTORY__POST_R3__, ɵSWITCH_TEMPLATE_REF_FACTORY__POST_R3__, ɵSWITCH_VIEW_CONTAINER_REF_FACTORY__POST_R3__, ɵSWITCH_RENDERER2_FACTORY__POST_R3__, ɵgetModuleFactory__POST_R3__, ɵregisterNgModuleType, ɵpublishGlobalUtil, ɵpublishDefaultGlobalUtils, ɵcreateInjector, ɵINJECTOR_IMPL__POST_R3__, ɵregisterModuleFactory, ɵEMPTY_ARRAY, ɵEMPTY_MAP, ɵand, ɵccf, ɵcmf, ɵcrt, ɵdid, ɵeld, ɵgetComponentViewDefinitionFactory, ɵinlineInterpolate, ɵinterpolate, ɵmod, ɵmpd, ɵncd, ɵnov, ɵpid, ɵprd, ɵpad, ɵpod, ɵppd, ɵqud, ɵted, ɵunv, ɵvid */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(1);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/upgrade/fesm5/static.js":
/*!************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/upgrade/fesm5/static.js from dll-reference vendor_chunk ***!
  \************************************************************************************************************************************************************************************************************************/
/*! exports provided: ɵangular_packages_upgrade_static_static_e, ɵangular_packages_upgrade_static_static_c, ɵangular_packages_upgrade_static_static_a, ɵangular_packages_upgrade_static_static_d, ɵangular_packages_upgrade_static_static_b, getAngularJSGlobal, getAngularLib, setAngularJSGlobal, setAngularLib, downgradeComponent, downgradeInjectable, VERSION, downgradeModule, UpgradeComponent, UpgradeModule */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(246);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js":
/*!**********************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************/
/*! exports provided: __extends, __assign, __rest, __decorate, __param, __metadata, __awaiter, __generator, __createBinding, __exportStar, __values, __read, __spread, __spreadArrays, __await, __asyncGenerator, __asyncDelegator, __asyncValues, __makeTemplateObject, __importStar, __importDefault, __classPrivateFieldGet, __classPrivateFieldSet */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(0);

/***/ }),

/***/ "../../smartedit-module/smartedit/smartedit-build/lib/smarteditcommons/src/index.js":
/*!***********************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/smartedit-module/smartedit/smartedit-build/lib/smarteditcommons/src/index.js from dll-reference smarteditcommons ***!
  \***********************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference smarteditcommons */ "dll-reference smarteditcommons"))(1);

/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmartedit/index.ts":
/*!***************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmartedit/index.ts ***!
  \***************************************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _legacymerchandisingsmartedit__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./legacymerchandisingsmartedit */ "./jsTarget/web/features/merchandisingsmartedit/legacymerchandisingsmartedit.ts");
/* harmony import */ var _merchandisingsmartedit__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./merchandisingsmartedit */ "./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit.ts");
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///




/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmartedit/legacymerchandisingsmartedit.ts":
/*!**************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmartedit/legacymerchandisingsmartedit.ts ***!
  \**************************************************************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var angular__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! angular */ "angular");
/* harmony import */ var angular__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(angular__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var merchandisingsmartedit_merchandisingsmartedit_bundle_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! merchandisingsmartedit/merchandisingsmartedit_bundle.js */ "./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit_bundle.js");
/* harmony import */ var merchandisingsmartedit_merchandisingsmartedit_bundle_js__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(merchandisingsmartedit_merchandisingsmartedit_bundle_js__WEBPACK_IMPORTED_MODULE_1__);
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///


angular__WEBPACK_IMPORTED_MODULE_0__["module"]("merchandisingsmartedit", ["smarteditServicesModule"])
    .run(["contextualMenuService", "sharedDataService", function (contextualMenuService, sharedDataService) {
    "ngInject";
    var setUpContextualMenu = function () {
        contextualMenuService.addItems({
            MerchandisingCarouselComponent: [
                {
                    key: "MerchandisingCarouselComponent",
                    i18nKey: "Edit Strategy",
                    action: {
                        callback: function (configuration, event) {
                            sharedDataService
                                .get("contextDrivenServicesMerchandisingUrl")
                                .then(function (url) {
                                var appUrl = "https://" + url;
                                window.open(appUrl);
                            }.bind(this));
                        }
                    },
                    displayClass: "icon-activate"
                }
            ]
        });
    };
    setUpContextualMenu();
}]);


/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit.ts":
/*!********************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit.ts ***!
  \********************************************************************************/
/*! exports provided: MerchandisingSmartEditModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MerchandisingSmartEditModule", function() { return MerchandisingSmartEditModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_upgrade_static__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/upgrade/static */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/upgrade/fesm5/static.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! smarteditcommons */ "../../smartedit-module/smartedit/smartedit-build/lib/smarteditcommons/src/index.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(smarteditcommons__WEBPACK_IMPORTED_MODULE_3__);
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///




var MerchandisingSmartEditModule = /** @class */ (function () {
    function MerchandisingSmartEditModule() {
    }
    MerchandisingSmartEditModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(smarteditcommons__WEBPACK_IMPORTED_MODULE_3__["SeEntryModule"])("merchandisingsmartedit"),
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_2__["NgModule"])({
            imports: [_angular_upgrade_static__WEBPACK_IMPORTED_MODULE_1__["UpgradeModule"]],
            declarations: [],
            entryComponents: [],
            providers: []
        })
    ], MerchandisingSmartEditModule);
    return MerchandisingSmartEditModule;
}());



/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit_bundle.js":
/*!***************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmartedit/merchandisingsmartedit_bundle.js ***!
  \***************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {




/***/ }),

/***/ "angular":
/*!**************************!*\
  !*** external "angular" ***!
  \**************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = angular;

/***/ }),

/***/ "dll-reference smarteditcommons":
/*!***********************************!*\
  !*** external "smarteditcommons" ***!
  \***********************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = smarteditcommons;

/***/ }),

/***/ "dll-reference vendor_chunk":
/*!*******************************!*\
  !*** external "vendor_chunk" ***!
  \*******************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = vendor_chunk;

/***/ })

/******/ });
//# sourceMappingURL=merchandisingsmartedit.js.map