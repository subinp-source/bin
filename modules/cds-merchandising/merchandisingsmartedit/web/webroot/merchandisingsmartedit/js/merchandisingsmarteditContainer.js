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
/******/ 	return __webpack_require__(__webpack_require__.s = "./jsTarget/web/features/merchandisingsmarteditContainer/index.ts");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/common/fesm5/http.js":
/*!*********************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/common/fesm5/http.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************/
/*! exports provided: ɵangular_packages_common_http_http_a, ɵangular_packages_common_http_http_b, ɵangular_packages_common_http_http_c, ɵangular_packages_common_http_http_d, ɵangular_packages_common_http_http_g, ɵangular_packages_common_http_http_h, ɵangular_packages_common_http_http_e, ɵangular_packages_common_http_http_f, HttpBackend, HttpHandler, HttpClient, HttpHeaders, HTTP_INTERCEPTORS, JsonpClientBackend, JsonpInterceptor, HttpClientJsonpModule, HttpClientModule, HttpClientXsrfModule, ɵHttpInterceptingHandler, HttpParams, HttpUrlEncodingCodec, HttpRequest, HttpErrorResponse, HttpEventType, HttpHeaderResponse, HttpResponse, HttpResponseBase, HttpXhrBackend, XhrFactory, HttpXsrfTokenExtractor */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(244);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js":
/*!*******************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************/
/*! exports provided: ɵangular_packages_core_core_q, ɵangular_packages_core_core_n, ɵangular_packages_core_core_o, ɵangular_packages_core_core_p, ɵangular_packages_core_core_r, ɵangular_packages_core_core_f, ɵangular_packages_core_core_l, ɵangular_packages_core_core_m, ɵangular_packages_core_core_k, ɵangular_packages_core_core_j, ɵangular_packages_core_core_b, ɵangular_packages_core_core_a, ɵangular_packages_core_core_c, ɵangular_packages_core_core_d, ɵangular_packages_core_core_e, ɵangular_packages_core_core_i, ɵangular_packages_core_core_s, ɵangular_packages_core_core_u, ɵangular_packages_core_core_t, ɵangular_packages_core_core_x, ɵangular_packages_core_core_v, ɵangular_packages_core_core_w, ɵangular_packages_core_core_ba, ɵangular_packages_core_core_bb, ɵangular_packages_core_core_bc, ɵangular_packages_core_core_bd, ɵangular_packages_core_core_be, ɵangular_packages_core_core_bm, ɵangular_packages_core_core_bl, ɵangular_packages_core_core_g, ɵangular_packages_core_core_h, ɵangular_packages_core_core_bg, ɵangular_packages_core_core_bk, ɵangular_packages_core_core_bh, ɵangular_packages_core_core_bi, ɵangular_packages_core_core_bn, ɵangular_packages_core_core_y, ɵangular_packages_core_core_z, createPlatform, assertPlatform, destroyPlatform, getPlatform, PlatformRef, ApplicationRef, createPlatformFactory, NgProbeToken, enableProdMode, isDevMode, APP_ID, PACKAGE_ROOT_URL, PLATFORM_INITIALIZER, PLATFORM_ID, APP_BOOTSTRAP_LISTENER, APP_INITIALIZER, ApplicationInitStatus, DebugElement, DebugEventListener, DebugNode, asNativeElements, getDebugNode, Testability, TestabilityRegistry, setTestabilityGetter, TRANSLATIONS, TRANSLATIONS_FORMAT, LOCALE_ID, MissingTranslationStrategy, ApplicationModule, wtfCreateScope, wtfLeave, wtfStartTimeRange, wtfEndTimeRange, Type, EventEmitter, ErrorHandler, Sanitizer, SecurityContext, Attribute, ANALYZE_FOR_ENTRY_COMPONENTS, ContentChild, ContentChildren, Query, ViewChild, ViewChildren, Component, Directive, HostBinding, HostListener, Input, Output, Pipe, NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, ViewEncapsulation, Version, VERSION, InjectFlags, ɵɵdefineInjectable, defineInjectable, ɵɵdefineInjector, forwardRef, resolveForwardRef, Injectable, Injector, ɵɵinject, inject, INJECTOR, ReflectiveInjector, ResolvedReflectiveFactory, ReflectiveKey, InjectionToken, Inject, Optional, Self, SkipSelf, Host, ɵ0, ɵ1, NgZone, ɵNoopNgZone, RenderComponentType, Renderer, Renderer2, RendererFactory2, RendererStyleFlags2, RootRenderer, COMPILER_OPTIONS, Compiler, CompilerFactory, ModuleWithComponentFactories, ComponentFactory, ɵComponentFactory, ComponentRef, ComponentFactoryResolver, ElementRef, NgModuleFactory, NgModuleRef, NgModuleFactoryLoader, getModuleFactory, QueryList, SystemJsNgModuleLoader, SystemJsNgModuleLoaderConfig, TemplateRef, ViewContainerRef, EmbeddedViewRef, ViewRef, ChangeDetectionStrategy, ChangeDetectorRef, DefaultIterableDiffer, IterableDiffers, KeyValueDiffers, SimpleChange, WrappedValue, platformCore, ɵALLOW_MULTIPLE_PLATFORMS, ɵAPP_ID_RANDOM_PROVIDER, ɵdefaultIterableDiffers, ɵdefaultKeyValueDiffers, ɵdevModeEqual, ɵisListLikeIterable, ɵChangeDetectorStatus, ɵisDefaultChangeDetectionStrategy, ɵConsole, ɵsetCurrentInjector, ɵgetInjectableDef, ɵAPP_ROOT, ɵDEFAULT_LOCALE_ID, ɵivyEnabled, ɵCodegenComponentFactoryResolver, ɵclearResolutionOfComponentResourcesQueue, ɵresolveComponentResources, ɵReflectionCapabilities, ɵRenderDebugInfo, ɵ_sanitizeHtml, ɵ_sanitizeStyle, ɵ_sanitizeUrl, ɵglobal, ɵlooseIdentical, ɵstringify, ɵmakeDecorator, ɵisObservable, ɵisPromise, ɵclearOverrides, ɵinitServicesIfNeeded, ɵoverrideComponentView, ɵoverrideProvider, ɵNOT_FOUND_CHECK_ONLY_ELEMENT_INJECTOR, ɵgetLocalePluralCase, ɵfindLocaleData, ɵLOCALE_DATA, ɵLocaleDataIndex, ɵɵattribute, ɵɵattributeInterpolate1, ɵɵattributeInterpolate2, ɵɵattributeInterpolate3, ɵɵattributeInterpolate4, ɵɵattributeInterpolate5, ɵɵattributeInterpolate6, ɵɵattributeInterpolate7, ɵɵattributeInterpolate8, ɵɵattributeInterpolateV, ɵɵdefineBase, ɵɵdefineComponent, ɵɵdefineDirective, ɵɵdefinePipe, ɵɵdefineNgModule, ɵdetectChanges, ɵrenderComponent, ɵRender3ComponentFactory, ɵRender3ComponentRef, ɵɵdirectiveInject, ɵɵinjectAttribute, ɵɵinjectPipeChangeDetectorRef, ɵɵgetFactoryOf, ɵɵgetInheritedFactory, ɵɵsetComponentScope, ɵɵsetNgModuleScope, ɵɵtemplateRefExtractor, ɵɵProvidersFeature, ɵɵInheritDefinitionFeature, ɵɵNgOnChangesFeature, ɵLifecycleHooksFeature, ɵRender3NgModuleRef, ɵmarkDirty, ɵNgModuleFactory, ɵNO_CHANGE, ɵɵcontainer, ɵɵnextContext, ɵɵelementStart, ɵɵnamespaceHTML, ɵɵnamespaceMathML, ɵɵnamespaceSVG, ɵɵelement, ɵɵlistener, ɵɵtext, ɵɵtextInterpolate, ɵɵtextInterpolate1, ɵɵtextInterpolate2, ɵɵtextInterpolate3, ɵɵtextInterpolate4, ɵɵtextInterpolate5, ɵɵtextInterpolate6, ɵɵtextInterpolate7, ɵɵtextInterpolate8, ɵɵtextInterpolateV, ɵɵembeddedViewStart, ɵɵprojection, ɵɵpipeBind1, ɵɵpipeBind2, ɵɵpipeBind3, ɵɵpipeBind4, ɵɵpipeBindV, ɵɵpureFunction0, ɵɵpureFunction1, ɵɵpureFunction2, ɵɵpureFunction3, ɵɵpureFunction4, ɵɵpureFunction5, ɵɵpureFunction6, ɵɵpureFunction7, ɵɵpureFunction8, ɵɵpureFunctionV, ɵɵgetCurrentView, ɵgetDirectives, ɵgetHostElement, ɵɵrestoreView, ɵɵcontainerRefreshStart, ɵɵcontainerRefreshEnd, ɵɵqueryRefresh, ɵɵviewQuery, ɵɵstaticViewQuery, ɵɵstaticContentQuery, ɵɵloadViewQuery, ɵɵcontentQuery, ɵɵloadContentQuery, ɵɵelementEnd, ɵɵhostProperty, ɵɵproperty, ɵɵpropertyInterpolate, ɵɵpropertyInterpolate1, ɵɵpropertyInterpolate2, ɵɵpropertyInterpolate3, ɵɵpropertyInterpolate4, ɵɵpropertyInterpolate5, ɵɵpropertyInterpolate6, ɵɵpropertyInterpolate7, ɵɵpropertyInterpolate8, ɵɵpropertyInterpolateV, ɵɵupdateSyntheticHostBinding, ɵɵcomponentHostSyntheticListener, ɵɵprojectionDef, ɵɵreference, ɵɵenableBindings, ɵɵdisableBindings, ɵɵallocHostVars, ɵɵelementContainerStart, ɵɵelementContainerEnd, ɵɵelementContainer, ɵɵstyling, ɵɵstyleMap, ɵɵstyleSanitizer, ɵɵclassMap, ɵɵclassMapInterpolate1, ɵɵclassMapInterpolate2, ɵɵclassMapInterpolate3, ɵɵclassMapInterpolate4, ɵɵclassMapInterpolate5, ɵɵclassMapInterpolate6, ɵɵclassMapInterpolate7, ɵɵclassMapInterpolate8, ɵɵclassMapInterpolateV, ɵɵstyleProp, ɵɵstylePropInterpolate1, ɵɵstylePropInterpolate2, ɵɵstylePropInterpolate3, ɵɵstylePropInterpolate4, ɵɵstylePropInterpolate5, ɵɵstylePropInterpolate6, ɵɵstylePropInterpolate7, ɵɵstylePropInterpolate8, ɵɵstylePropInterpolateV, ɵɵstylingApply, ɵɵclassProp, ɵɵelementHostAttrs, ɵɵselect, ɵɵtextBinding, ɵɵtemplate, ɵɵembeddedViewEnd, ɵstore, ɵɵload, ɵɵpipe, ɵwhenRendered, ɵɵi18n, ɵɵi18nAttributes, ɵɵi18nExp, ɵɵi18nStart, ɵɵi18nEnd, ɵɵi18nApply, ɵɵi18nPostprocess, ɵi18nConfigureLocalize, ɵɵi18nLocalize, ɵsetLocaleId, ɵsetClassMetadata, ɵɵresolveWindow, ɵɵresolveDocument, ɵɵresolveBody, ɵcompileComponent, ɵcompileDirective, ɵcompileNgModule, ɵcompileNgModuleDefs, ɵpatchComponentDefWithScope, ɵresetCompiledComponents, ɵflushModuleScopingQueueAsMuchAsPossible, ɵtransitiveScopesFor, ɵcompilePipe, ɵɵsanitizeHtml, ɵɵsanitizeStyle, ɵɵdefaultStyleSanitizer, ɵɵsanitizeScript, ɵɵsanitizeUrl, ɵɵsanitizeResourceUrl, ɵɵsanitizeUrlOrResourceUrl, ɵbypassSanitizationTrustHtml, ɵbypassSanitizationTrustStyle, ɵbypassSanitizationTrustScript, ɵbypassSanitizationTrustUrl, ɵbypassSanitizationTrustResourceUrl, ɵgetLContext, ɵNG_ELEMENT_ID, ɵNG_COMPONENT_DEF, ɵNG_DIRECTIVE_DEF, ɵNG_PIPE_DEF, ɵNG_MODULE_DEF, ɵNG_BASE_DEF, ɵNG_INJECTABLE_DEF, ɵNG_INJECTOR_DEF, ɵcompileNgModuleFactory__POST_R3__, ɵisBoundToModule__POST_R3__, ɵSWITCH_COMPILE_COMPONENT__POST_R3__, ɵSWITCH_COMPILE_DIRECTIVE__POST_R3__, ɵSWITCH_COMPILE_PIPE__POST_R3__, ɵSWITCH_COMPILE_NGMODULE__POST_R3__, ɵgetDebugNode__POST_R3__, ɵSWITCH_COMPILE_INJECTABLE__POST_R3__, ɵSWITCH_IVY_ENABLED__POST_R3__, ɵSWITCH_CHANGE_DETECTOR_REF_FACTORY__POST_R3__, ɵCompiler_compileModuleSync__POST_R3__, ɵCompiler_compileModuleAsync__POST_R3__, ɵCompiler_compileModuleAndAllComponentsSync__POST_R3__, ɵCompiler_compileModuleAndAllComponentsAsync__POST_R3__, ɵSWITCH_ELEMENT_REF_FACTORY__POST_R3__, ɵSWITCH_TEMPLATE_REF_FACTORY__POST_R3__, ɵSWITCH_VIEW_CONTAINER_REF_FACTORY__POST_R3__, ɵSWITCH_RENDERER2_FACTORY__POST_R3__, ɵgetModuleFactory__POST_R3__, ɵregisterNgModuleType, ɵpublishGlobalUtil, ɵpublishDefaultGlobalUtils, ɵcreateInjector, ɵINJECTOR_IMPL__POST_R3__, ɵregisterModuleFactory, ɵEMPTY_ARRAY, ɵEMPTY_MAP, ɵand, ɵccf, ɵcmf, ɵcrt, ɵdid, ɵeld, ɵgetComponentViewDefinitionFactory, ɵinlineInterpolate, ɵinterpolate, ɵmod, ɵmpd, ɵncd, ɵnov, ɵpid, ɵprd, ɵpad, ɵpod, ɵppd, ɵqud, ɵted, ɵunv, ɵvid */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(1);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/index.js":
/*!***********************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/index.js from dll-reference vendor_chunk ***!
  \***********************************************************************************************************************************************************************************************************/
/*! exports provided: Observable, ConnectableObservable, GroupedObservable, observable, Subject, BehaviorSubject, ReplaySubject, AsyncSubject, asapScheduler, asyncScheduler, queueScheduler, animationFrameScheduler, VirtualTimeScheduler, VirtualAction, Scheduler, Subscription, Subscriber, Notification, pipe, noop, identity, isObservable, ArgumentOutOfRangeError, EmptyError, ObjectUnsubscribedError, UnsubscriptionError, TimeoutError, bindCallback, bindNodeCallback, combineLatest, concat, defer, empty, forkJoin, from, fromEvent, fromEventPattern, generate, iif, interval, merge, never, of, onErrorResumeNext, pairs, race, range, throwError, timer, using, zip, EMPTY, NEVER, config */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(66);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/AsyncSubject.js":
/*!***************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/AsyncSubject.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************/
/*! exports provided: AsyncSubject */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(63);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/BehaviorSubject.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/BehaviorSubject.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: BehaviorSubject */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(37);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: InnerSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(64);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Notification.js":
/*!***************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Notification.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************/
/*! exports provided: NotificationKind, Notification */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(92);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Observable.js":
/*!*************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Observable.js from dll-reference vendor_chunk ***!
  \*************************************************************************************************************************************************************************************************************************/
/*! exports provided: Observable */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(4);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: OuterSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(31);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/ReplaySubject.js":
/*!****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/ReplaySubject.js from dll-reference vendor_chunk ***!
  \****************************************************************************************************************************************************************************************************************************/
/*! exports provided: ReplaySubject */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(169);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js":
/*!**********************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************/
/*! exports provided: SubjectSubscriber, Subject, AnonymousSubject */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(23);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js":
/*!*************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js from dll-reference vendor_chunk ***!
  \*************************************************************************************************************************************************************************************************************************/
/*! exports provided: Subscriber, SafeSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(8);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscription.js":
/*!***************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscription.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************/
/*! exports provided: Subscription */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(9);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/SubscribeOnObservable.js":
/*!****************************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/SubscribeOnObservable.js ***!
  \****************************************************************************************************************************************************************************************************/
/*! exports provided: SubscribeOnObservable */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SubscribeOnObservable", function() { return SubscribeOnObservable; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Observable__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Observable */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Observable.js");
/* harmony import */ var _scheduler_asap__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../scheduler/asap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/asap.js");
/* harmony import */ var _util_isNumeric__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/isNumeric */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isNumeric.js");
/** PURE_IMPORTS_START tslib,_Observable,_scheduler_asap,_util_isNumeric PURE_IMPORTS_END */




var SubscribeOnObservable = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SubscribeOnObservable, _super);
    function SubscribeOnObservable(source, delayTime, scheduler) {
        if (delayTime === void 0) {
            delayTime = 0;
        }
        if (scheduler === void 0) {
            scheduler = _scheduler_asap__WEBPACK_IMPORTED_MODULE_2__["asap"];
        }
        var _this = _super.call(this) || this;
        _this.source = source;
        _this.delayTime = delayTime;
        _this.scheduler = scheduler;
        if (!Object(_util_isNumeric__WEBPACK_IMPORTED_MODULE_3__["isNumeric"])(delayTime) || delayTime < 0) {
            _this.delayTime = 0;
        }
        if (!scheduler || typeof scheduler.schedule !== 'function') {
            _this.scheduler = _scheduler_asap__WEBPACK_IMPORTED_MODULE_2__["asap"];
        }
        return _this;
    }
    SubscribeOnObservable.create = function (source, delay, scheduler) {
        if (delay === void 0) {
            delay = 0;
        }
        if (scheduler === void 0) {
            scheduler = _scheduler_asap__WEBPACK_IMPORTED_MODULE_2__["asap"];
        }
        return new SubscribeOnObservable(source, delay, scheduler);
    };
    SubscribeOnObservable.dispatch = function (arg) {
        var source = arg.source, subscriber = arg.subscriber;
        return this.add(source.subscribe(subscriber));
    };
    SubscribeOnObservable.prototype._subscribe = function (subscriber) {
        var delay = this.delayTime;
        var source = this.source;
        var scheduler = this.scheduler;
        return scheduler.schedule(SubscribeOnObservable.dispatch, delay, {
            source: source, subscriber: subscriber
        });
    };
    return SubscribeOnObservable;
}(_Observable__WEBPACK_IMPORTED_MODULE_1__["Observable"]));

//# sourceMappingURL=SubscribeOnObservable.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/combineLatest.js":
/*!***************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/combineLatest.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************************/
/*! exports provided: combineLatest, CombineLatestOperator, CombineLatestSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(163);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/concat.js":
/*!********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/concat.js from dll-reference vendor_chunk ***!
  \********************************************************************************************************************************************************************************************************************************/
/*! exports provided: concat */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(77);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/defer.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/defer.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: defer */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(93);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/empty.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/empty.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: EMPTY, empty, emptyScheduled */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(11);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/from.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/from.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: from */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(16);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/fromArray.js":
/*!***********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/fromArray.js from dll-reference vendor_chunk ***!
  \***********************************************************************************************************************************************************************************************************************************/
/*! exports provided: fromArray */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(27);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/merge.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/merge.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: merge */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(109);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/race.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/race.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: race, RaceOperator, RaceSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(183);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/scalar.js":
/*!********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/scalar.js from dll-reference vendor_chunk ***!
  \********************************************************************************************************************************************************************************************************************************/
/*! exports provided: scalar */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(130);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/throwError.js":
/*!************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/throwError.js from dll-reference vendor_chunk ***!
  \************************************************************************************************************************************************************************************************************************************/
/*! exports provided: throwError */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(124);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/timer.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/timer.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: timer */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(185);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/zip.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/zip.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: zip, ZipOperator, ZipSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(187);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/audit.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/audit.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: audit */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "audit", function() { return audit; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function audit(durationSelector) {
    return function auditOperatorFunction(source) {
        return source.lift(new AuditOperator(durationSelector));
    };
}
var AuditOperator = /*@__PURE__*/ (function () {
    function AuditOperator(durationSelector) {
        this.durationSelector = durationSelector;
    }
    AuditOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new AuditSubscriber(subscriber, this.durationSelector));
    };
    return AuditOperator;
}());
var AuditSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](AuditSubscriber, _super);
    function AuditSubscriber(destination, durationSelector) {
        var _this = _super.call(this, destination) || this;
        _this.durationSelector = durationSelector;
        _this.hasValue = false;
        return _this;
    }
    AuditSubscriber.prototype._next = function (value) {
        this.value = value;
        this.hasValue = true;
        if (!this.throttled) {
            var duration = void 0;
            try {
                var durationSelector = this.durationSelector;
                duration = durationSelector(value);
            }
            catch (err) {
                return this.destination.error(err);
            }
            var innerSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, duration);
            if (!innerSubscription || innerSubscription.closed) {
                this.clearThrottle();
            }
            else {
                this.add(this.throttled = innerSubscription);
            }
        }
    };
    AuditSubscriber.prototype.clearThrottle = function () {
        var _a = this, value = _a.value, hasValue = _a.hasValue, throttled = _a.throttled;
        if (throttled) {
            this.remove(throttled);
            this.throttled = null;
            throttled.unsubscribe();
        }
        if (hasValue) {
            this.value = null;
            this.hasValue = false;
            this.destination.next(value);
        }
    };
    AuditSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex) {
        this.clearThrottle();
    };
    AuditSubscriber.prototype.notifyComplete = function () {
        this.clearThrottle();
    };
    return AuditSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=audit.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/auditTime.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/auditTime.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: auditTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "auditTime", function() { return auditTime; });
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _audit__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./audit */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/audit.js");
/* harmony import */ var _observable_timer__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../observable/timer */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/timer.js");
/** PURE_IMPORTS_START _scheduler_async,_audit,_observable_timer PURE_IMPORTS_END */



function auditTime(duration, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_0__["async"];
    }
    return Object(_audit__WEBPACK_IMPORTED_MODULE_1__["audit"])(function () { return Object(_observable_timer__WEBPACK_IMPORTED_MODULE_2__["timer"])(duration, scheduler); });
}
//# sourceMappingURL=auditTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/buffer.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/buffer.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: buffer */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "buffer", function() { return buffer; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function buffer(closingNotifier) {
    return function bufferOperatorFunction(source) {
        return source.lift(new BufferOperator(closingNotifier));
    };
}
var BufferOperator = /*@__PURE__*/ (function () {
    function BufferOperator(closingNotifier) {
        this.closingNotifier = closingNotifier;
    }
    BufferOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new BufferSubscriber(subscriber, this.closingNotifier));
    };
    return BufferOperator;
}());
var BufferSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferSubscriber, _super);
    function BufferSubscriber(destination, closingNotifier) {
        var _this = _super.call(this, destination) || this;
        _this.buffer = [];
        _this.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(_this, closingNotifier));
        return _this;
    }
    BufferSubscriber.prototype._next = function (value) {
        this.buffer.push(value);
    };
    BufferSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        var buffer = this.buffer;
        this.buffer = [];
        this.destination.next(buffer);
    };
    return BufferSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=buffer.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferCount.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferCount.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: bufferCount */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "bufferCount", function() { return bufferCount; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function bufferCount(bufferSize, startBufferEvery) {
    if (startBufferEvery === void 0) {
        startBufferEvery = null;
    }
    return function bufferCountOperatorFunction(source) {
        return source.lift(new BufferCountOperator(bufferSize, startBufferEvery));
    };
}
var BufferCountOperator = /*@__PURE__*/ (function () {
    function BufferCountOperator(bufferSize, startBufferEvery) {
        this.bufferSize = bufferSize;
        this.startBufferEvery = startBufferEvery;
        if (!startBufferEvery || bufferSize === startBufferEvery) {
            this.subscriberClass = BufferCountSubscriber;
        }
        else {
            this.subscriberClass = BufferSkipCountSubscriber;
        }
    }
    BufferCountOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new this.subscriberClass(subscriber, this.bufferSize, this.startBufferEvery));
    };
    return BufferCountOperator;
}());
var BufferCountSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferCountSubscriber, _super);
    function BufferCountSubscriber(destination, bufferSize) {
        var _this = _super.call(this, destination) || this;
        _this.bufferSize = bufferSize;
        _this.buffer = [];
        return _this;
    }
    BufferCountSubscriber.prototype._next = function (value) {
        var buffer = this.buffer;
        buffer.push(value);
        if (buffer.length == this.bufferSize) {
            this.destination.next(buffer);
            this.buffer = [];
        }
    };
    BufferCountSubscriber.prototype._complete = function () {
        var buffer = this.buffer;
        if (buffer.length > 0) {
            this.destination.next(buffer);
        }
        _super.prototype._complete.call(this);
    };
    return BufferCountSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
var BufferSkipCountSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferSkipCountSubscriber, _super);
    function BufferSkipCountSubscriber(destination, bufferSize, startBufferEvery) {
        var _this = _super.call(this, destination) || this;
        _this.bufferSize = bufferSize;
        _this.startBufferEvery = startBufferEvery;
        _this.buffers = [];
        _this.count = 0;
        return _this;
    }
    BufferSkipCountSubscriber.prototype._next = function (value) {
        var _a = this, bufferSize = _a.bufferSize, startBufferEvery = _a.startBufferEvery, buffers = _a.buffers, count = _a.count;
        this.count++;
        if (count % startBufferEvery === 0) {
            buffers.push([]);
        }
        for (var i = buffers.length; i--;) {
            var buffer = buffers[i];
            buffer.push(value);
            if (buffer.length === bufferSize) {
                buffers.splice(i, 1);
                this.destination.next(buffer);
            }
        }
    };
    BufferSkipCountSubscriber.prototype._complete = function () {
        var _a = this, buffers = _a.buffers, destination = _a.destination;
        while (buffers.length > 0) {
            var buffer = buffers.shift();
            if (buffer.length > 0) {
                destination.next(buffer);
            }
        }
        _super.prototype._complete.call(this);
    };
    return BufferSkipCountSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=bufferCount.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferTime.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferTime.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: bufferTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "bufferTime", function() { return bufferTime; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _util_isScheduler__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/isScheduler */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isScheduler.js");
/** PURE_IMPORTS_START tslib,_scheduler_async,_Subscriber,_util_isScheduler PURE_IMPORTS_END */




function bufferTime(bufferTimeSpan) {
    var length = arguments.length;
    var scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_1__["async"];
    if (Object(_util_isScheduler__WEBPACK_IMPORTED_MODULE_3__["isScheduler"])(arguments[arguments.length - 1])) {
        scheduler = arguments[arguments.length - 1];
        length--;
    }
    var bufferCreationInterval = null;
    if (length >= 2) {
        bufferCreationInterval = arguments[1];
    }
    var maxBufferSize = Number.POSITIVE_INFINITY;
    if (length >= 3) {
        maxBufferSize = arguments[2];
    }
    return function bufferTimeOperatorFunction(source) {
        return source.lift(new BufferTimeOperator(bufferTimeSpan, bufferCreationInterval, maxBufferSize, scheduler));
    };
}
var BufferTimeOperator = /*@__PURE__*/ (function () {
    function BufferTimeOperator(bufferTimeSpan, bufferCreationInterval, maxBufferSize, scheduler) {
        this.bufferTimeSpan = bufferTimeSpan;
        this.bufferCreationInterval = bufferCreationInterval;
        this.maxBufferSize = maxBufferSize;
        this.scheduler = scheduler;
    }
    BufferTimeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new BufferTimeSubscriber(subscriber, this.bufferTimeSpan, this.bufferCreationInterval, this.maxBufferSize, this.scheduler));
    };
    return BufferTimeOperator;
}());
var Context = /*@__PURE__*/ (function () {
    function Context() {
        this.buffer = [];
    }
    return Context;
}());
var BufferTimeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferTimeSubscriber, _super);
    function BufferTimeSubscriber(destination, bufferTimeSpan, bufferCreationInterval, maxBufferSize, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.bufferTimeSpan = bufferTimeSpan;
        _this.bufferCreationInterval = bufferCreationInterval;
        _this.maxBufferSize = maxBufferSize;
        _this.scheduler = scheduler;
        _this.contexts = [];
        var context = _this.openContext();
        _this.timespanOnly = bufferCreationInterval == null || bufferCreationInterval < 0;
        if (_this.timespanOnly) {
            var timeSpanOnlyState = { subscriber: _this, context: context, bufferTimeSpan: bufferTimeSpan };
            _this.add(context.closeAction = scheduler.schedule(dispatchBufferTimeSpanOnly, bufferTimeSpan, timeSpanOnlyState));
        }
        else {
            var closeState = { subscriber: _this, context: context };
            var creationState = { bufferTimeSpan: bufferTimeSpan, bufferCreationInterval: bufferCreationInterval, subscriber: _this, scheduler: scheduler };
            _this.add(context.closeAction = scheduler.schedule(dispatchBufferClose, bufferTimeSpan, closeState));
            _this.add(scheduler.schedule(dispatchBufferCreation, bufferCreationInterval, creationState));
        }
        return _this;
    }
    BufferTimeSubscriber.prototype._next = function (value) {
        var contexts = this.contexts;
        var len = contexts.length;
        var filledBufferContext;
        for (var i = 0; i < len; i++) {
            var context_1 = contexts[i];
            var buffer = context_1.buffer;
            buffer.push(value);
            if (buffer.length == this.maxBufferSize) {
                filledBufferContext = context_1;
            }
        }
        if (filledBufferContext) {
            this.onBufferFull(filledBufferContext);
        }
    };
    BufferTimeSubscriber.prototype._error = function (err) {
        this.contexts.length = 0;
        _super.prototype._error.call(this, err);
    };
    BufferTimeSubscriber.prototype._complete = function () {
        var _a = this, contexts = _a.contexts, destination = _a.destination;
        while (contexts.length > 0) {
            var context_2 = contexts.shift();
            destination.next(context_2.buffer);
        }
        _super.prototype._complete.call(this);
    };
    BufferTimeSubscriber.prototype._unsubscribe = function () {
        this.contexts = null;
    };
    BufferTimeSubscriber.prototype.onBufferFull = function (context) {
        this.closeContext(context);
        var closeAction = context.closeAction;
        closeAction.unsubscribe();
        this.remove(closeAction);
        if (!this.closed && this.timespanOnly) {
            context = this.openContext();
            var bufferTimeSpan = this.bufferTimeSpan;
            var timeSpanOnlyState = { subscriber: this, context: context, bufferTimeSpan: bufferTimeSpan };
            this.add(context.closeAction = this.scheduler.schedule(dispatchBufferTimeSpanOnly, bufferTimeSpan, timeSpanOnlyState));
        }
    };
    BufferTimeSubscriber.prototype.openContext = function () {
        var context = new Context();
        this.contexts.push(context);
        return context;
    };
    BufferTimeSubscriber.prototype.closeContext = function (context) {
        this.destination.next(context.buffer);
        var contexts = this.contexts;
        var spliceIndex = contexts ? contexts.indexOf(context) : -1;
        if (spliceIndex >= 0) {
            contexts.splice(contexts.indexOf(context), 1);
        }
    };
    return BufferTimeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_2__["Subscriber"]));
function dispatchBufferTimeSpanOnly(state) {
    var subscriber = state.subscriber;
    var prevContext = state.context;
    if (prevContext) {
        subscriber.closeContext(prevContext);
    }
    if (!subscriber.closed) {
        state.context = subscriber.openContext();
        state.context.closeAction = this.schedule(state, state.bufferTimeSpan);
    }
}
function dispatchBufferCreation(state) {
    var bufferCreationInterval = state.bufferCreationInterval, bufferTimeSpan = state.bufferTimeSpan, subscriber = state.subscriber, scheduler = state.scheduler;
    var context = subscriber.openContext();
    var action = this;
    if (!subscriber.closed) {
        subscriber.add(context.closeAction = scheduler.schedule(dispatchBufferClose, bufferTimeSpan, { subscriber: subscriber, context: context }));
        action.schedule(state, bufferCreationInterval);
    }
}
function dispatchBufferClose(arg) {
    var subscriber = arg.subscriber, context = arg.context;
    subscriber.closeContext(context);
}
//# sourceMappingURL=bufferTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferToggle.js":
/*!******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferToggle.js ***!
  \******************************************************************************************************************************************************************************************/
/*! exports provided: bufferToggle */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "bufferToggle", function() { return bufferToggle; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscription__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscription */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscription.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/** PURE_IMPORTS_START tslib,_Subscription,_util_subscribeToResult,_OuterSubscriber PURE_IMPORTS_END */




function bufferToggle(openings, closingSelector) {
    return function bufferToggleOperatorFunction(source) {
        return source.lift(new BufferToggleOperator(openings, closingSelector));
    };
}
var BufferToggleOperator = /*@__PURE__*/ (function () {
    function BufferToggleOperator(openings, closingSelector) {
        this.openings = openings;
        this.closingSelector = closingSelector;
    }
    BufferToggleOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new BufferToggleSubscriber(subscriber, this.openings, this.closingSelector));
    };
    return BufferToggleOperator;
}());
var BufferToggleSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferToggleSubscriber, _super);
    function BufferToggleSubscriber(destination, openings, closingSelector) {
        var _this = _super.call(this, destination) || this;
        _this.openings = openings;
        _this.closingSelector = closingSelector;
        _this.contexts = [];
        _this.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(_this, openings));
        return _this;
    }
    BufferToggleSubscriber.prototype._next = function (value) {
        var contexts = this.contexts;
        var len = contexts.length;
        for (var i = 0; i < len; i++) {
            contexts[i].buffer.push(value);
        }
    };
    BufferToggleSubscriber.prototype._error = function (err) {
        var contexts = this.contexts;
        while (contexts.length > 0) {
            var context_1 = contexts.shift();
            context_1.subscription.unsubscribe();
            context_1.buffer = null;
            context_1.subscription = null;
        }
        this.contexts = null;
        _super.prototype._error.call(this, err);
    };
    BufferToggleSubscriber.prototype._complete = function () {
        var contexts = this.contexts;
        while (contexts.length > 0) {
            var context_2 = contexts.shift();
            this.destination.next(context_2.buffer);
            context_2.subscription.unsubscribe();
            context_2.buffer = null;
            context_2.subscription = null;
        }
        this.contexts = null;
        _super.prototype._complete.call(this);
    };
    BufferToggleSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        outerValue ? this.closeBuffer(outerValue) : this.openBuffer(innerValue);
    };
    BufferToggleSubscriber.prototype.notifyComplete = function (innerSub) {
        this.closeBuffer(innerSub.context);
    };
    BufferToggleSubscriber.prototype.openBuffer = function (value) {
        try {
            var closingSelector = this.closingSelector;
            var closingNotifier = closingSelector.call(this, value);
            if (closingNotifier) {
                this.trySubscribe(closingNotifier);
            }
        }
        catch (err) {
            this._error(err);
        }
    };
    BufferToggleSubscriber.prototype.closeBuffer = function (context) {
        var contexts = this.contexts;
        if (contexts && context) {
            var buffer = context.buffer, subscription = context.subscription;
            this.destination.next(buffer);
            contexts.splice(contexts.indexOf(context), 1);
            this.remove(subscription);
            subscription.unsubscribe();
        }
    };
    BufferToggleSubscriber.prototype.trySubscribe = function (closingNotifier) {
        var contexts = this.contexts;
        var buffer = [];
        var subscription = new _Subscription__WEBPACK_IMPORTED_MODULE_1__["Subscription"]();
        var context = { buffer: buffer, subscription: subscription };
        contexts.push(context);
        var innerSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, closingNotifier, context);
        if (!innerSubscription || innerSubscription.closed) {
            this.closeBuffer(context);
        }
        else {
            innerSubscription.context = context;
            this.add(innerSubscription);
            subscription.add(innerSubscription);
        }
    };
    return BufferToggleSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__["OuterSubscriber"]));
//# sourceMappingURL=bufferToggle.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferWhen.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferWhen.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: bufferWhen */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "bufferWhen", function() { return bufferWhen; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscription__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscription */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscription.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subscription,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function bufferWhen(closingSelector) {
    return function (source) {
        return source.lift(new BufferWhenOperator(closingSelector));
    };
}
var BufferWhenOperator = /*@__PURE__*/ (function () {
    function BufferWhenOperator(closingSelector) {
        this.closingSelector = closingSelector;
    }
    BufferWhenOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new BufferWhenSubscriber(subscriber, this.closingSelector));
    };
    return BufferWhenOperator;
}());
var BufferWhenSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](BufferWhenSubscriber, _super);
    function BufferWhenSubscriber(destination, closingSelector) {
        var _this = _super.call(this, destination) || this;
        _this.closingSelector = closingSelector;
        _this.subscribing = false;
        _this.openBuffer();
        return _this;
    }
    BufferWhenSubscriber.prototype._next = function (value) {
        this.buffer.push(value);
    };
    BufferWhenSubscriber.prototype._complete = function () {
        var buffer = this.buffer;
        if (buffer) {
            this.destination.next(buffer);
        }
        _super.prototype._complete.call(this);
    };
    BufferWhenSubscriber.prototype._unsubscribe = function () {
        this.buffer = null;
        this.subscribing = false;
    };
    BufferWhenSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.openBuffer();
    };
    BufferWhenSubscriber.prototype.notifyComplete = function () {
        if (this.subscribing) {
            this.complete();
        }
        else {
            this.openBuffer();
        }
    };
    BufferWhenSubscriber.prototype.openBuffer = function () {
        var closingSubscription = this.closingSubscription;
        if (closingSubscription) {
            this.remove(closingSubscription);
            closingSubscription.unsubscribe();
        }
        var buffer = this.buffer;
        if (this.buffer) {
            this.destination.next(buffer);
        }
        this.buffer = [];
        var closingNotifier;
        try {
            var closingSelector = this.closingSelector;
            closingNotifier = closingSelector();
        }
        catch (err) {
            return this.error(err);
        }
        closingSubscription = new _Subscription__WEBPACK_IMPORTED_MODULE_1__["Subscription"]();
        this.closingSubscription = closingSubscription;
        this.add(closingSubscription);
        this.subscribing = true;
        closingSubscription.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(this, closingNotifier));
        this.subscribing = false;
    };
    return BufferWhenSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));
//# sourceMappingURL=bufferWhen.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/catchError.js":
/*!***********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/catchError.js from dll-reference vendor_chunk ***!
  \***********************************************************************************************************************************************************************************************************************************/
/*! exports provided: catchError */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(97);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineAll.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineAll.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: combineAll */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "combineAll", function() { return combineAll; });
/* harmony import */ var _observable_combineLatest__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/combineLatest */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/combineLatest.js");
/** PURE_IMPORTS_START _observable_combineLatest PURE_IMPORTS_END */

function combineAll(project) {
    return function (source) { return source.lift(new _observable_combineLatest__WEBPACK_IMPORTED_MODULE_0__["CombineLatestOperator"](project)); };
}
//# sourceMappingURL=combineAll.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineLatest.js":
/*!*******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineLatest.js ***!
  \*******************************************************************************************************************************************************************************************/
/*! exports provided: combineLatest */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "combineLatest", function() { return combineLatest; });
/* harmony import */ var _util_isArray__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../util/isArray */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isArray.js");
/* harmony import */ var _observable_combineLatest__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../observable/combineLatest */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/combineLatest.js");
/* harmony import */ var _observable_from__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../observable/from */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/from.js");
/** PURE_IMPORTS_START _util_isArray,_observable_combineLatest,_observable_from PURE_IMPORTS_END */



var none = {};
function combineLatest() {
    var observables = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        observables[_i] = arguments[_i];
    }
    var project = null;
    if (typeof observables[observables.length - 1] === 'function') {
        project = observables.pop();
    }
    if (observables.length === 1 && Object(_util_isArray__WEBPACK_IMPORTED_MODULE_0__["isArray"])(observables[0])) {
        observables = observables[0].slice();
    }
    return function (source) { return source.lift.call(Object(_observable_from__WEBPACK_IMPORTED_MODULE_2__["from"])([source].concat(observables)), new _observable_combineLatest__WEBPACK_IMPORTED_MODULE_1__["CombineLatestOperator"](project)); };
}
//# sourceMappingURL=combineLatest.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concat.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concat.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: concat */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "concat", function() { return concat; });
/* harmony import */ var _observable_concat__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/concat */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/concat.js");
/** PURE_IMPORTS_START _observable_concat PURE_IMPORTS_END */

function concat() {
    var observables = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        observables[_i] = arguments[_i];
    }
    return function (source) { return source.lift.call(_observable_concat__WEBPACK_IMPORTED_MODULE_0__["concat"].apply(void 0, [source].concat(observables))); };
}
//# sourceMappingURL=concat.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatAll.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatAll.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: concatAll */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(96);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMap.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMap.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: concatMap */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(139);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMapTo.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMapTo.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: concatMapTo */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "concatMapTo", function() { return concatMapTo; });
/* harmony import */ var _concatMap__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./concatMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMap.js");
/** PURE_IMPORTS_START _concatMap PURE_IMPORTS_END */

function concatMapTo(innerObservable, resultSelector) {
    return Object(_concatMap__WEBPACK_IMPORTED_MODULE_0__["concatMap"])(function () { return innerObservable; }, resultSelector);
}
//# sourceMappingURL=concatMapTo.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/count.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/count.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: count */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "count", function() { return count; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function count(predicate) {
    return function (source) { return source.lift(new CountOperator(predicate, source)); };
}
var CountOperator = /*@__PURE__*/ (function () {
    function CountOperator(predicate, source) {
        this.predicate = predicate;
        this.source = source;
    }
    CountOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new CountSubscriber(subscriber, this.predicate, this.source));
    };
    return CountOperator;
}());
var CountSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](CountSubscriber, _super);
    function CountSubscriber(destination, predicate, source) {
        var _this = _super.call(this, destination) || this;
        _this.predicate = predicate;
        _this.source = source;
        _this.count = 0;
        _this.index = 0;
        return _this;
    }
    CountSubscriber.prototype._next = function (value) {
        if (this.predicate) {
            this._tryPredicate(value);
        }
        else {
            this.count++;
        }
    };
    CountSubscriber.prototype._tryPredicate = function (value) {
        var result;
        try {
            result = this.predicate(value, this.index++, this.source);
        }
        catch (err) {
            this.destination.error(err);
            return;
        }
        if (result) {
            this.count++;
        }
    };
    CountSubscriber.prototype._complete = function () {
        this.destination.next(this.count);
        this.destination.complete();
    };
    return CountSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=count.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounce.js":
/*!**************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounce.js ***!
  \**************************************************************************************************************************************************************************************/
/*! exports provided: debounce */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "debounce", function() { return debounce; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function debounce(durationSelector) {
    return function (source) { return source.lift(new DebounceOperator(durationSelector)); };
}
var DebounceOperator = /*@__PURE__*/ (function () {
    function DebounceOperator(durationSelector) {
        this.durationSelector = durationSelector;
    }
    DebounceOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DebounceSubscriber(subscriber, this.durationSelector));
    };
    return DebounceOperator;
}());
var DebounceSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DebounceSubscriber, _super);
    function DebounceSubscriber(destination, durationSelector) {
        var _this = _super.call(this, destination) || this;
        _this.durationSelector = durationSelector;
        _this.hasValue = false;
        _this.durationSubscription = null;
        return _this;
    }
    DebounceSubscriber.prototype._next = function (value) {
        try {
            var result = this.durationSelector.call(this, value);
            if (result) {
                this._tryNext(value, result);
            }
        }
        catch (err) {
            this.destination.error(err);
        }
    };
    DebounceSubscriber.prototype._complete = function () {
        this.emitValue();
        this.destination.complete();
    };
    DebounceSubscriber.prototype._tryNext = function (value, duration) {
        var subscription = this.durationSubscription;
        this.value = value;
        this.hasValue = true;
        if (subscription) {
            subscription.unsubscribe();
            this.remove(subscription);
        }
        subscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, duration);
        if (subscription && !subscription.closed) {
            this.add(this.durationSubscription = subscription);
        }
    };
    DebounceSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.emitValue();
    };
    DebounceSubscriber.prototype.notifyComplete = function () {
        this.emitValue();
    };
    DebounceSubscriber.prototype.emitValue = function () {
        if (this.hasValue) {
            var value = this.value;
            var subscription = this.durationSubscription;
            if (subscription) {
                this.durationSubscription = null;
                subscription.unsubscribe();
                this.remove(subscription);
            }
            this.value = null;
            this.hasValue = false;
            _super.prototype._next.call(this, value);
        }
    };
    return DebounceSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=debounce.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounceTime.js":
/*!******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounceTime.js ***!
  \******************************************************************************************************************************************************************************************/
/*! exports provided: debounceTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "debounceTime", function() { return debounceTime; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_scheduler_async PURE_IMPORTS_END */



function debounceTime(dueTime, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_2__["async"];
    }
    return function (source) { return source.lift(new DebounceTimeOperator(dueTime, scheduler)); };
}
var DebounceTimeOperator = /*@__PURE__*/ (function () {
    function DebounceTimeOperator(dueTime, scheduler) {
        this.dueTime = dueTime;
        this.scheduler = scheduler;
    }
    DebounceTimeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DebounceTimeSubscriber(subscriber, this.dueTime, this.scheduler));
    };
    return DebounceTimeOperator;
}());
var DebounceTimeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DebounceTimeSubscriber, _super);
    function DebounceTimeSubscriber(destination, dueTime, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.dueTime = dueTime;
        _this.scheduler = scheduler;
        _this.debouncedSubscription = null;
        _this.lastValue = null;
        _this.hasValue = false;
        return _this;
    }
    DebounceTimeSubscriber.prototype._next = function (value) {
        this.clearDebounce();
        this.lastValue = value;
        this.hasValue = true;
        this.add(this.debouncedSubscription = this.scheduler.schedule(dispatchNext, this.dueTime, this));
    };
    DebounceTimeSubscriber.prototype._complete = function () {
        this.debouncedNext();
        this.destination.complete();
    };
    DebounceTimeSubscriber.prototype.debouncedNext = function () {
        this.clearDebounce();
        if (this.hasValue) {
            var lastValue = this.lastValue;
            this.lastValue = null;
            this.hasValue = false;
            this.destination.next(lastValue);
        }
    };
    DebounceTimeSubscriber.prototype.clearDebounce = function () {
        var debouncedSubscription = this.debouncedSubscription;
        if (debouncedSubscription !== null) {
            this.remove(debouncedSubscription);
            debouncedSubscription.unsubscribe();
            this.debouncedSubscription = null;
        }
    };
    return DebounceTimeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
function dispatchNext(subscriber) {
    subscriber.debouncedNext();
}
//# sourceMappingURL=debounceTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/defaultIfEmpty.js":
/*!***************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/defaultIfEmpty.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************************/
/*! exports provided: defaultIfEmpty */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(94);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delay.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delay.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: delay */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "delay", function() { return delay; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _util_isDate__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/isDate */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isDate.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _Notification__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../Notification */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Notification.js");
/** PURE_IMPORTS_START tslib,_scheduler_async,_util_isDate,_Subscriber,_Notification PURE_IMPORTS_END */





function delay(delay, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_1__["async"];
    }
    var absoluteDelay = Object(_util_isDate__WEBPACK_IMPORTED_MODULE_2__["isDate"])(delay);
    var delayFor = absoluteDelay ? (+delay - scheduler.now()) : Math.abs(delay);
    return function (source) { return source.lift(new DelayOperator(delayFor, scheduler)); };
}
var DelayOperator = /*@__PURE__*/ (function () {
    function DelayOperator(delay, scheduler) {
        this.delay = delay;
        this.scheduler = scheduler;
    }
    DelayOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DelaySubscriber(subscriber, this.delay, this.scheduler));
    };
    return DelayOperator;
}());
var DelaySubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DelaySubscriber, _super);
    function DelaySubscriber(destination, delay, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.delay = delay;
        _this.scheduler = scheduler;
        _this.queue = [];
        _this.active = false;
        _this.errored = false;
        return _this;
    }
    DelaySubscriber.dispatch = function (state) {
        var source = state.source;
        var queue = source.queue;
        var scheduler = state.scheduler;
        var destination = state.destination;
        while (queue.length > 0 && (queue[0].time - scheduler.now()) <= 0) {
            queue.shift().notification.observe(destination);
        }
        if (queue.length > 0) {
            var delay_1 = Math.max(0, queue[0].time - scheduler.now());
            this.schedule(state, delay_1);
        }
        else {
            this.unsubscribe();
            source.active = false;
        }
    };
    DelaySubscriber.prototype._schedule = function (scheduler) {
        this.active = true;
        var destination = this.destination;
        destination.add(scheduler.schedule(DelaySubscriber.dispatch, this.delay, {
            source: this, destination: this.destination, scheduler: scheduler
        }));
    };
    DelaySubscriber.prototype.scheduleNotification = function (notification) {
        if (this.errored === true) {
            return;
        }
        var scheduler = this.scheduler;
        var message = new DelayMessage(scheduler.now() + this.delay, notification);
        this.queue.push(message);
        if (this.active === false) {
            this._schedule(scheduler);
        }
    };
    DelaySubscriber.prototype._next = function (value) {
        this.scheduleNotification(_Notification__WEBPACK_IMPORTED_MODULE_4__["Notification"].createNext(value));
    };
    DelaySubscriber.prototype._error = function (err) {
        this.errored = true;
        this.queue = [];
        this.destination.error(err);
        this.unsubscribe();
    };
    DelaySubscriber.prototype._complete = function () {
        this.scheduleNotification(_Notification__WEBPACK_IMPORTED_MODULE_4__["Notification"].createComplete());
        this.unsubscribe();
    };
    return DelaySubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_3__["Subscriber"]));
var DelayMessage = /*@__PURE__*/ (function () {
    function DelayMessage(time, notification) {
        this.time = time;
        this.notification = notification;
    }
    return DelayMessage;
}());
//# sourceMappingURL=delay.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delayWhen.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delayWhen.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: delayWhen */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "delayWhen", function() { return delayWhen; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _Observable__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../Observable */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Observable.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_Observable,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */





function delayWhen(delayDurationSelector, subscriptionDelay) {
    if (subscriptionDelay) {
        return function (source) {
            return new SubscriptionDelayObservable(source, subscriptionDelay)
                .lift(new DelayWhenOperator(delayDurationSelector));
        };
    }
    return function (source) { return source.lift(new DelayWhenOperator(delayDurationSelector)); };
}
var DelayWhenOperator = /*@__PURE__*/ (function () {
    function DelayWhenOperator(delayDurationSelector) {
        this.delayDurationSelector = delayDurationSelector;
    }
    DelayWhenOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DelayWhenSubscriber(subscriber, this.delayDurationSelector));
    };
    return DelayWhenOperator;
}());
var DelayWhenSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DelayWhenSubscriber, _super);
    function DelayWhenSubscriber(destination, delayDurationSelector) {
        var _this = _super.call(this, destination) || this;
        _this.delayDurationSelector = delayDurationSelector;
        _this.completed = false;
        _this.delayNotifierSubscriptions = [];
        _this.index = 0;
        return _this;
    }
    DelayWhenSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.destination.next(outerValue);
        this.removeSubscription(innerSub);
        this.tryComplete();
    };
    DelayWhenSubscriber.prototype.notifyError = function (error, innerSub) {
        this._error(error);
    };
    DelayWhenSubscriber.prototype.notifyComplete = function (innerSub) {
        var value = this.removeSubscription(innerSub);
        if (value) {
            this.destination.next(value);
        }
        this.tryComplete();
    };
    DelayWhenSubscriber.prototype._next = function (value) {
        var index = this.index++;
        try {
            var delayNotifier = this.delayDurationSelector(value, index);
            if (delayNotifier) {
                this.tryDelay(delayNotifier, value);
            }
        }
        catch (err) {
            this.destination.error(err);
        }
    };
    DelayWhenSubscriber.prototype._complete = function () {
        this.completed = true;
        this.tryComplete();
        this.unsubscribe();
    };
    DelayWhenSubscriber.prototype.removeSubscription = function (subscription) {
        subscription.unsubscribe();
        var subscriptionIdx = this.delayNotifierSubscriptions.indexOf(subscription);
        if (subscriptionIdx !== -1) {
            this.delayNotifierSubscriptions.splice(subscriptionIdx, 1);
        }
        return subscription.outerValue;
    };
    DelayWhenSubscriber.prototype.tryDelay = function (delayNotifier, value) {
        var notifierSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__["subscribeToResult"])(this, delayNotifier, value);
        if (notifierSubscription && !notifierSubscription.closed) {
            var destination = this.destination;
            destination.add(notifierSubscription);
            this.delayNotifierSubscriptions.push(notifierSubscription);
        }
    };
    DelayWhenSubscriber.prototype.tryComplete = function () {
        if (this.completed && this.delayNotifierSubscriptions.length === 0) {
            this.destination.complete();
        }
    };
    return DelayWhenSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__["OuterSubscriber"]));
var SubscriptionDelayObservable = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SubscriptionDelayObservable, _super);
    function SubscriptionDelayObservable(source, subscriptionDelay) {
        var _this = _super.call(this) || this;
        _this.source = source;
        _this.subscriptionDelay = subscriptionDelay;
        return _this;
    }
    SubscriptionDelayObservable.prototype._subscribe = function (subscriber) {
        this.subscriptionDelay.subscribe(new SubscriptionDelaySubscriber(subscriber, this.source));
    };
    return SubscriptionDelayObservable;
}(_Observable__WEBPACK_IMPORTED_MODULE_2__["Observable"]));
var SubscriptionDelaySubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SubscriptionDelaySubscriber, _super);
    function SubscriptionDelaySubscriber(parent, source) {
        var _this = _super.call(this) || this;
        _this.parent = parent;
        _this.source = source;
        _this.sourceSubscribed = false;
        return _this;
    }
    SubscriptionDelaySubscriber.prototype._next = function (unused) {
        this.subscribeToSource();
    };
    SubscriptionDelaySubscriber.prototype._error = function (err) {
        this.unsubscribe();
        this.parent.error(err);
    };
    SubscriptionDelaySubscriber.prototype._complete = function () {
        this.unsubscribe();
        this.subscribeToSource();
    };
    SubscriptionDelaySubscriber.prototype.subscribeToSource = function () {
        if (!this.sourceSubscribed) {
            this.sourceSubscribed = true;
            this.unsubscribe();
            this.source.subscribe(this.parent);
        }
    };
    return SubscriptionDelaySubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=delayWhen.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/dematerialize.js":
/*!*******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/dematerialize.js ***!
  \*******************************************************************************************************************************************************************************************/
/*! exports provided: dematerialize */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "dematerialize", function() { return dematerialize; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function dematerialize() {
    return function dematerializeOperatorFunction(source) {
        return source.lift(new DeMaterializeOperator());
    };
}
var DeMaterializeOperator = /*@__PURE__*/ (function () {
    function DeMaterializeOperator() {
    }
    DeMaterializeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DeMaterializeSubscriber(subscriber));
    };
    return DeMaterializeOperator;
}());
var DeMaterializeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DeMaterializeSubscriber, _super);
    function DeMaterializeSubscriber(destination) {
        return _super.call(this, destination) || this;
    }
    DeMaterializeSubscriber.prototype._next = function (value) {
        value.observe(this.destination);
    };
    return DeMaterializeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=dematerialize.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinct.js":
/*!**************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinct.js ***!
  \**************************************************************************************************************************************************************************************/
/*! exports provided: distinct, DistinctSubscriber */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "distinct", function() { return distinct; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DistinctSubscriber", function() { return DistinctSubscriber; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function distinct(keySelector, flushes) {
    return function (source) { return source.lift(new DistinctOperator(keySelector, flushes)); };
}
var DistinctOperator = /*@__PURE__*/ (function () {
    function DistinctOperator(keySelector, flushes) {
        this.keySelector = keySelector;
        this.flushes = flushes;
    }
    DistinctOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DistinctSubscriber(subscriber, this.keySelector, this.flushes));
    };
    return DistinctOperator;
}());
var DistinctSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DistinctSubscriber, _super);
    function DistinctSubscriber(destination, keySelector, flushes) {
        var _this = _super.call(this, destination) || this;
        _this.keySelector = keySelector;
        _this.values = new Set();
        if (flushes) {
            _this.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(_this, flushes));
        }
        return _this;
    }
    DistinctSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.values.clear();
    };
    DistinctSubscriber.prototype.notifyError = function (error, innerSub) {
        this._error(error);
    };
    DistinctSubscriber.prototype._next = function (value) {
        if (this.keySelector) {
            this._useKeySelector(value);
        }
        else {
            this._finalizeNext(value, value);
        }
    };
    DistinctSubscriber.prototype._useKeySelector = function (value) {
        var key;
        var destination = this.destination;
        try {
            key = this.keySelector(value);
        }
        catch (err) {
            destination.error(err);
            return;
        }
        this._finalizeNext(key, value);
    };
    DistinctSubscriber.prototype._finalizeNext = function (key, value) {
        var values = this.values;
        if (!values.has(key)) {
            values.add(key);
            this.destination.next(value);
        }
    };
    return DistinctSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));

//# sourceMappingURL=distinct.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilChanged.js":
/*!**************************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilChanged.js ***!
  \**************************************************************************************************************************************************************************************************/
/*! exports provided: distinctUntilChanged */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "distinctUntilChanged", function() { return distinctUntilChanged; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function distinctUntilChanged(compare, keySelector) {
    return function (source) { return source.lift(new DistinctUntilChangedOperator(compare, keySelector)); };
}
var DistinctUntilChangedOperator = /*@__PURE__*/ (function () {
    function DistinctUntilChangedOperator(compare, keySelector) {
        this.compare = compare;
        this.keySelector = keySelector;
    }
    DistinctUntilChangedOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new DistinctUntilChangedSubscriber(subscriber, this.compare, this.keySelector));
    };
    return DistinctUntilChangedOperator;
}());
var DistinctUntilChangedSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](DistinctUntilChangedSubscriber, _super);
    function DistinctUntilChangedSubscriber(destination, compare, keySelector) {
        var _this = _super.call(this, destination) || this;
        _this.keySelector = keySelector;
        _this.hasKey = false;
        if (typeof compare === 'function') {
            _this.compare = compare;
        }
        return _this;
    }
    DistinctUntilChangedSubscriber.prototype.compare = function (x, y) {
        return x === y;
    };
    DistinctUntilChangedSubscriber.prototype._next = function (value) {
        var key;
        try {
            var keySelector = this.keySelector;
            key = keySelector ? keySelector(value) : value;
        }
        catch (err) {
            return this.destination.error(err);
        }
        var result = false;
        if (this.hasKey) {
            try {
                var compare = this.compare;
                result = compare(this.key, key);
            }
            catch (err) {
                return this.destination.error(err);
            }
        }
        else {
            this.hasKey = true;
        }
        if (!result) {
            this.key = key;
            this.destination.next(value);
        }
    };
    return DistinctUntilChangedSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=distinctUntilChanged.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilKeyChanged.js":
/*!*****************************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilKeyChanged.js ***!
  \*****************************************************************************************************************************************************************************************************/
/*! exports provided: distinctUntilKeyChanged */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "distinctUntilKeyChanged", function() { return distinctUntilKeyChanged; });
/* harmony import */ var _distinctUntilChanged__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./distinctUntilChanged */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilChanged.js");
/** PURE_IMPORTS_START _distinctUntilChanged PURE_IMPORTS_END */

function distinctUntilKeyChanged(key, compare) {
    return Object(_distinctUntilChanged__WEBPACK_IMPORTED_MODULE_0__["distinctUntilChanged"])(function (x, y) { return compare ? compare(x[key], y[key]) : x[key] === y[key]; });
}
//# sourceMappingURL=distinctUntilKeyChanged.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/elementAt.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/elementAt.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: elementAt */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "elementAt", function() { return elementAt; });
/* harmony import */ var _util_ArgumentOutOfRangeError__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../util/ArgumentOutOfRangeError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/ArgumentOutOfRangeError.js");
/* harmony import */ var _filter__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./filter */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/filter.js");
/* harmony import */ var _throwIfEmpty__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./throwIfEmpty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throwIfEmpty.js");
/* harmony import */ var _defaultIfEmpty__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./defaultIfEmpty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/defaultIfEmpty.js");
/* harmony import */ var _take__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./take */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/take.js");
/** PURE_IMPORTS_START _util_ArgumentOutOfRangeError,_filter,_throwIfEmpty,_defaultIfEmpty,_take PURE_IMPORTS_END */





function elementAt(index, defaultValue) {
    if (index < 0) {
        throw new _util_ArgumentOutOfRangeError__WEBPACK_IMPORTED_MODULE_0__["ArgumentOutOfRangeError"]();
    }
    var hasDefaultValue = arguments.length >= 2;
    return function (source) {
        return source.pipe(Object(_filter__WEBPACK_IMPORTED_MODULE_1__["filter"])(function (v, i) { return i === index; }), Object(_take__WEBPACK_IMPORTED_MODULE_4__["take"])(1), hasDefaultValue
            ? Object(_defaultIfEmpty__WEBPACK_IMPORTED_MODULE_3__["defaultIfEmpty"])(defaultValue)
            : Object(_throwIfEmpty__WEBPACK_IMPORTED_MODULE_2__["throwIfEmpty"])(function () { return new _util_ArgumentOutOfRangeError__WEBPACK_IMPORTED_MODULE_0__["ArgumentOutOfRangeError"](); }));
    };
}
//# sourceMappingURL=elementAt.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/endWith.js":
/*!*************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/endWith.js ***!
  \*************************************************************************************************************************************************************************************/
/*! exports provided: endWith */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "endWith", function() { return endWith; });
/* harmony import */ var _observable_fromArray__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/fromArray */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/fromArray.js");
/* harmony import */ var _observable_scalar__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../observable/scalar */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/scalar.js");
/* harmony import */ var _observable_empty__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../observable/empty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/empty.js");
/* harmony import */ var _observable_concat__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../observable/concat */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/concat.js");
/* harmony import */ var _util_isScheduler__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../util/isScheduler */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isScheduler.js");
/** PURE_IMPORTS_START _observable_fromArray,_observable_scalar,_observable_empty,_observable_concat,_util_isScheduler PURE_IMPORTS_END */





function endWith() {
    var array = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        array[_i] = arguments[_i];
    }
    return function (source) {
        var scheduler = array[array.length - 1];
        if (Object(_util_isScheduler__WEBPACK_IMPORTED_MODULE_4__["isScheduler"])(scheduler)) {
            array.pop();
        }
        else {
            scheduler = null;
        }
        var len = array.length;
        if (len === 1 && !scheduler) {
            return Object(_observable_concat__WEBPACK_IMPORTED_MODULE_3__["concat"])(source, Object(_observable_scalar__WEBPACK_IMPORTED_MODULE_1__["scalar"])(array[0]));
        }
        else if (len > 0) {
            return Object(_observable_concat__WEBPACK_IMPORTED_MODULE_3__["concat"])(source, Object(_observable_fromArray__WEBPACK_IMPORTED_MODULE_0__["fromArray"])(array, scheduler));
        }
        else {
            return Object(_observable_concat__WEBPACK_IMPORTED_MODULE_3__["concat"])(source, Object(_observable_empty__WEBPACK_IMPORTED_MODULE_2__["empty"])(scheduler));
        }
    };
}
//# sourceMappingURL=endWith.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/every.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/every.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: every */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(412);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaust.js":
/*!*************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaust.js ***!
  \*************************************************************************************************************************************************************************************/
/*! exports provided: exhaust */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "exhaust", function() { return exhaust; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function exhaust() {
    return function (source) { return source.lift(new SwitchFirstOperator()); };
}
var SwitchFirstOperator = /*@__PURE__*/ (function () {
    function SwitchFirstOperator() {
    }
    SwitchFirstOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SwitchFirstSubscriber(subscriber));
    };
    return SwitchFirstOperator;
}());
var SwitchFirstSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SwitchFirstSubscriber, _super);
    function SwitchFirstSubscriber(destination) {
        var _this = _super.call(this, destination) || this;
        _this.hasCompleted = false;
        _this.hasSubscription = false;
        return _this;
    }
    SwitchFirstSubscriber.prototype._next = function (value) {
        if (!this.hasSubscription) {
            this.hasSubscription = true;
            this.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, value));
        }
    };
    SwitchFirstSubscriber.prototype._complete = function () {
        this.hasCompleted = true;
        if (!this.hasSubscription) {
            this.destination.complete();
        }
    };
    SwitchFirstSubscriber.prototype.notifyComplete = function (innerSub) {
        this.remove(innerSub);
        this.hasSubscription = false;
        if (this.hasCompleted) {
            this.destination.complete();
        }
    };
    return SwitchFirstSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=exhaust.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaustMap.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaustMap.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: exhaustMap */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "exhaustMap", function() { return exhaustMap; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _InnerSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../InnerSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/* harmony import */ var _map__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./map */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js");
/* harmony import */ var _observable_from__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../observable/from */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/from.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_InnerSubscriber,_util_subscribeToResult,_map,_observable_from PURE_IMPORTS_END */






function exhaustMap(project, resultSelector) {
    if (resultSelector) {
        return function (source) { return source.pipe(exhaustMap(function (a, i) { return Object(_observable_from__WEBPACK_IMPORTED_MODULE_5__["from"])(project(a, i)).pipe(Object(_map__WEBPACK_IMPORTED_MODULE_4__["map"])(function (b, ii) { return resultSelector(a, b, i, ii); })); })); };
    }
    return function (source) {
        return source.lift(new ExhaustMapOperator(project));
    };
}
var ExhaustMapOperator = /*@__PURE__*/ (function () {
    function ExhaustMapOperator(project) {
        this.project = project;
    }
    ExhaustMapOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new ExhaustMapSubscriber(subscriber, this.project));
    };
    return ExhaustMapOperator;
}());
var ExhaustMapSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](ExhaustMapSubscriber, _super);
    function ExhaustMapSubscriber(destination, project) {
        var _this = _super.call(this, destination) || this;
        _this.project = project;
        _this.hasSubscription = false;
        _this.hasCompleted = false;
        _this.index = 0;
        return _this;
    }
    ExhaustMapSubscriber.prototype._next = function (value) {
        if (!this.hasSubscription) {
            this.tryNext(value);
        }
    };
    ExhaustMapSubscriber.prototype.tryNext = function (value) {
        var result;
        var index = this.index++;
        try {
            result = this.project(value, index);
        }
        catch (err) {
            this.destination.error(err);
            return;
        }
        this.hasSubscription = true;
        this._innerSub(result, value, index);
    };
    ExhaustMapSubscriber.prototype._innerSub = function (result, value, index) {
        var innerSubscriber = new _InnerSubscriber__WEBPACK_IMPORTED_MODULE_2__["InnerSubscriber"](this, undefined, undefined);
        var destination = this.destination;
        destination.add(innerSubscriber);
        Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(this, result, value, index, innerSubscriber);
    };
    ExhaustMapSubscriber.prototype._complete = function () {
        this.hasCompleted = true;
        if (!this.hasSubscription) {
            this.destination.complete();
        }
        this.unsubscribe();
    };
    ExhaustMapSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.destination.next(innerValue);
    };
    ExhaustMapSubscriber.prototype.notifyError = function (err) {
        this.destination.error(err);
    };
    ExhaustMapSubscriber.prototype.notifyComplete = function (innerSub) {
        var destination = this.destination;
        destination.remove(innerSub);
        this.hasSubscription = false;
        if (this.hasCompleted) {
            this.destination.complete();
        }
    };
    return ExhaustMapSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=exhaustMap.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/expand.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/expand.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: expand, ExpandOperator, ExpandSubscriber */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "expand", function() { return expand; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ExpandOperator", function() { return ExpandOperator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ExpandSubscriber", function() { return ExpandSubscriber; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function expand(project, concurrent, scheduler) {
    if (concurrent === void 0) {
        concurrent = Number.POSITIVE_INFINITY;
    }
    if (scheduler === void 0) {
        scheduler = undefined;
    }
    concurrent = (concurrent || 0) < 1 ? Number.POSITIVE_INFINITY : concurrent;
    return function (source) { return source.lift(new ExpandOperator(project, concurrent, scheduler)); };
}
var ExpandOperator = /*@__PURE__*/ (function () {
    function ExpandOperator(project, concurrent, scheduler) {
        this.project = project;
        this.concurrent = concurrent;
        this.scheduler = scheduler;
    }
    ExpandOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new ExpandSubscriber(subscriber, this.project, this.concurrent, this.scheduler));
    };
    return ExpandOperator;
}());

var ExpandSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](ExpandSubscriber, _super);
    function ExpandSubscriber(destination, project, concurrent, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.project = project;
        _this.concurrent = concurrent;
        _this.scheduler = scheduler;
        _this.index = 0;
        _this.active = 0;
        _this.hasCompleted = false;
        if (concurrent < Number.POSITIVE_INFINITY) {
            _this.buffer = [];
        }
        return _this;
    }
    ExpandSubscriber.dispatch = function (arg) {
        var subscriber = arg.subscriber, result = arg.result, value = arg.value, index = arg.index;
        subscriber.subscribeToProjection(result, value, index);
    };
    ExpandSubscriber.prototype._next = function (value) {
        var destination = this.destination;
        if (destination.closed) {
            this._complete();
            return;
        }
        var index = this.index++;
        if (this.active < this.concurrent) {
            destination.next(value);
            try {
                var project = this.project;
                var result = project(value, index);
                if (!this.scheduler) {
                    this.subscribeToProjection(result, value, index);
                }
                else {
                    var state = { subscriber: this, result: result, value: value, index: index };
                    var destination_1 = this.destination;
                    destination_1.add(this.scheduler.schedule(ExpandSubscriber.dispatch, 0, state));
                }
            }
            catch (e) {
                destination.error(e);
            }
        }
        else {
            this.buffer.push(value);
        }
    };
    ExpandSubscriber.prototype.subscribeToProjection = function (result, value, index) {
        this.active++;
        var destination = this.destination;
        destination.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, result, value, index));
    };
    ExpandSubscriber.prototype._complete = function () {
        this.hasCompleted = true;
        if (this.hasCompleted && this.active === 0) {
            this.destination.complete();
        }
        this.unsubscribe();
    };
    ExpandSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this._next(innerValue);
    };
    ExpandSubscriber.prototype.notifyComplete = function (innerSub) {
        var buffer = this.buffer;
        var destination = this.destination;
        destination.remove(innerSub);
        this.active--;
        if (buffer && buffer.length > 0) {
            this._next(buffer.shift());
        }
        if (this.hasCompleted && this.active === 0) {
            this.destination.complete();
        }
    };
    return ExpandSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));

//# sourceMappingURL=expand.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/filter.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/filter.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: filter */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(58);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/finalize.js":
/*!*********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/finalize.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************************/
/*! exports provided: finalize */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(414);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/find.js":
/*!**********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/find.js ***!
  \**********************************************************************************************************************************************************************************/
/*! exports provided: find, FindValueOperator, FindValueSubscriber */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "find", function() { return find; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FindValueOperator", function() { return FindValueOperator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FindValueSubscriber", function() { return FindValueSubscriber; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function find(predicate, thisArg) {
    if (typeof predicate !== 'function') {
        throw new TypeError('predicate is not a function');
    }
    return function (source) { return source.lift(new FindValueOperator(predicate, source, false, thisArg)); };
}
var FindValueOperator = /*@__PURE__*/ (function () {
    function FindValueOperator(predicate, source, yieldIndex, thisArg) {
        this.predicate = predicate;
        this.source = source;
        this.yieldIndex = yieldIndex;
        this.thisArg = thisArg;
    }
    FindValueOperator.prototype.call = function (observer, source) {
        return source.subscribe(new FindValueSubscriber(observer, this.predicate, this.source, this.yieldIndex, this.thisArg));
    };
    return FindValueOperator;
}());

var FindValueSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](FindValueSubscriber, _super);
    function FindValueSubscriber(destination, predicate, source, yieldIndex, thisArg) {
        var _this = _super.call(this, destination) || this;
        _this.predicate = predicate;
        _this.source = source;
        _this.yieldIndex = yieldIndex;
        _this.thisArg = thisArg;
        _this.index = 0;
        return _this;
    }
    FindValueSubscriber.prototype.notifyComplete = function (value) {
        var destination = this.destination;
        destination.next(value);
        destination.complete();
        this.unsubscribe();
    };
    FindValueSubscriber.prototype._next = function (value) {
        var _a = this, predicate = _a.predicate, thisArg = _a.thisArg;
        var index = this.index++;
        try {
            var result = predicate.call(thisArg || this, value, index, this.source);
            if (result) {
                this.notifyComplete(this.yieldIndex ? index : value);
            }
        }
        catch (err) {
            this.destination.error(err);
        }
    };
    FindValueSubscriber.prototype._complete = function () {
        this.notifyComplete(this.yieldIndex ? -1 : undefined);
    };
    return FindValueSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));

//# sourceMappingURL=find.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/findIndex.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/findIndex.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: findIndex */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "findIndex", function() { return findIndex; });
/* harmony import */ var _operators_find__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../operators/find */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/find.js");
/** PURE_IMPORTS_START _operators_find PURE_IMPORTS_END */

function findIndex(predicate, thisArg) {
    return function (source) { return source.lift(new _operators_find__WEBPACK_IMPORTED_MODULE_0__["FindValueOperator"](predicate, source, true, thisArg)); };
}
//# sourceMappingURL=findIndex.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/first.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/first.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: first */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(79);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/groupBy.js":
/*!********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/groupBy.js from dll-reference vendor_chunk ***!
  \********************************************************************************************************************************************************************************************************************************/
/*! exports provided: groupBy, GroupedObservable */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(168);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/ignoreElements.js":
/*!********************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/ignoreElements.js ***!
  \********************************************************************************************************************************************************************************************/
/*! exports provided: ignoreElements */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ignoreElements", function() { return ignoreElements; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function ignoreElements() {
    return function ignoreElementsOperatorFunction(source) {
        return source.lift(new IgnoreElementsOperator());
    };
}
var IgnoreElementsOperator = /*@__PURE__*/ (function () {
    function IgnoreElementsOperator() {
    }
    IgnoreElementsOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new IgnoreElementsSubscriber(subscriber));
    };
    return IgnoreElementsOperator;
}());
var IgnoreElementsSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](IgnoreElementsSubscriber, _super);
    function IgnoreElementsSubscriber() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    IgnoreElementsSubscriber.prototype._next = function (unused) {
    };
    return IgnoreElementsSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=ignoreElements.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/isEmpty.js":
/*!*************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/isEmpty.js ***!
  \*************************************************************************************************************************************************************************************/
/*! exports provided: isEmpty */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isEmpty", function() { return isEmpty; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function isEmpty() {
    return function (source) { return source.lift(new IsEmptyOperator()); };
}
var IsEmptyOperator = /*@__PURE__*/ (function () {
    function IsEmptyOperator() {
    }
    IsEmptyOperator.prototype.call = function (observer, source) {
        return source.subscribe(new IsEmptySubscriber(observer));
    };
    return IsEmptyOperator;
}());
var IsEmptySubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](IsEmptySubscriber, _super);
    function IsEmptySubscriber(destination) {
        return _super.call(this, destination) || this;
    }
    IsEmptySubscriber.prototype.notifyComplete = function (isEmpty) {
        var destination = this.destination;
        destination.next(isEmpty);
        destination.complete();
    };
    IsEmptySubscriber.prototype._next = function (value) {
        this.notifyComplete(false);
    };
    IsEmptySubscriber.prototype._complete = function () {
        this.notifyComplete(true);
    };
    return IsEmptySubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=isEmpty.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/last.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/last.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: last */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(239);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js":
/*!****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js from dll-reference vendor_chunk ***!
  \****************************************************************************************************************************************************************************************************************************/
/*! exports provided: map, MapOperator */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(5);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mapTo.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mapTo.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: mapTo */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "mapTo", function() { return mapTo; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function mapTo(value) {
    return function (source) { return source.lift(new MapToOperator(value)); };
}
var MapToOperator = /*@__PURE__*/ (function () {
    function MapToOperator(value) {
        this.value = value;
    }
    MapToOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new MapToSubscriber(subscriber, this.value));
    };
    return MapToOperator;
}());
var MapToSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](MapToSubscriber, _super);
    function MapToSubscriber(destination, value) {
        var _this = _super.call(this, destination) || this;
        _this.value = value;
        return _this;
    }
    MapToSubscriber.prototype._next = function (x) {
        this.destination.next(this.value);
    };
    return MapToSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=mapTo.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/materialize.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/materialize.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: materialize */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "materialize", function() { return materialize; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _Notification__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../Notification */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Notification.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_Notification PURE_IMPORTS_END */



function materialize() {
    return function materializeOperatorFunction(source) {
        return source.lift(new MaterializeOperator());
    };
}
var MaterializeOperator = /*@__PURE__*/ (function () {
    function MaterializeOperator() {
    }
    MaterializeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new MaterializeSubscriber(subscriber));
    };
    return MaterializeOperator;
}());
var MaterializeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](MaterializeSubscriber, _super);
    function MaterializeSubscriber(destination) {
        return _super.call(this, destination) || this;
    }
    MaterializeSubscriber.prototype._next = function (value) {
        this.destination.next(_Notification__WEBPACK_IMPORTED_MODULE_2__["Notification"].createNext(value));
    };
    MaterializeSubscriber.prototype._error = function (err) {
        var destination = this.destination;
        destination.next(_Notification__WEBPACK_IMPORTED_MODULE_2__["Notification"].createError(err));
        destination.complete();
    };
    MaterializeSubscriber.prototype._complete = function () {
        var destination = this.destination;
        destination.next(_Notification__WEBPACK_IMPORTED_MODULE_2__["Notification"].createComplete());
        destination.complete();
    };
    return MaterializeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=materialize.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/max.js":
/*!*********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/max.js ***!
  \*********************************************************************************************************************************************************************************/
/*! exports provided: max */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "max", function() { return max; });
/* harmony import */ var _reduce__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./reduce */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/reduce.js");
/** PURE_IMPORTS_START _reduce PURE_IMPORTS_END */

function max(comparer) {
    var max = (typeof comparer === 'function')
        ? function (x, y) { return comparer(x, y) > 0 ? x : y; }
        : function (x, y) { return x > y ? x : y; };
    return Object(_reduce__WEBPACK_IMPORTED_MODULE_0__["reduce"])(max);
}
//# sourceMappingURL=max.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/merge.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/merge.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: merge */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "merge", function() { return merge; });
/* harmony import */ var _observable_merge__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/merge */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/merge.js");
/** PURE_IMPORTS_START _observable_merge PURE_IMPORTS_END */

function merge() {
    var observables = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        observables[_i] = arguments[_i];
    }
    return function (source) { return source.lift.call(_observable_merge__WEBPACK_IMPORTED_MODULE_0__["merge"].apply(void 0, [source].concat(observables))); };
}
//# sourceMappingURL=merge.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeAll.js":
/*!*********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeAll.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************************/
/*! exports provided: mergeAll */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(107);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMap.js":
/*!*********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMap.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************************/
/*! exports provided: mergeMap, MergeMapOperator, MergeMapSubscriber */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(26);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMapTo.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMapTo.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: mergeMapTo */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "mergeMapTo", function() { return mergeMapTo; });
/* harmony import */ var _mergeMap__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./mergeMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMap.js");
/** PURE_IMPORTS_START _mergeMap PURE_IMPORTS_END */

function mergeMapTo(innerObservable, resultSelector, concurrent) {
    if (concurrent === void 0) {
        concurrent = Number.POSITIVE_INFINITY;
    }
    if (typeof resultSelector === 'function') {
        return Object(_mergeMap__WEBPACK_IMPORTED_MODULE_0__["mergeMap"])(function () { return innerObservable; }, resultSelector, concurrent);
    }
    if (typeof resultSelector === 'number') {
        concurrent = resultSelector;
    }
    return Object(_mergeMap__WEBPACK_IMPORTED_MODULE_0__["mergeMap"])(function () { return innerObservable; }, concurrent);
}
//# sourceMappingURL=mergeMapTo.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeScan.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeScan.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: mergeScan, MergeScanOperator, MergeScanSubscriber */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "mergeScan", function() { return mergeScan; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MergeScanOperator", function() { return MergeScanOperator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MergeScanSubscriber", function() { return MergeScanSubscriber; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _InnerSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../InnerSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js");
/** PURE_IMPORTS_START tslib,_util_subscribeToResult,_OuterSubscriber,_InnerSubscriber PURE_IMPORTS_END */




function mergeScan(accumulator, seed, concurrent) {
    if (concurrent === void 0) {
        concurrent = Number.POSITIVE_INFINITY;
    }
    return function (source) { return source.lift(new MergeScanOperator(accumulator, seed, concurrent)); };
}
var MergeScanOperator = /*@__PURE__*/ (function () {
    function MergeScanOperator(accumulator, seed, concurrent) {
        this.accumulator = accumulator;
        this.seed = seed;
        this.concurrent = concurrent;
    }
    MergeScanOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new MergeScanSubscriber(subscriber, this.accumulator, this.seed, this.concurrent));
    };
    return MergeScanOperator;
}());

var MergeScanSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](MergeScanSubscriber, _super);
    function MergeScanSubscriber(destination, accumulator, acc, concurrent) {
        var _this = _super.call(this, destination) || this;
        _this.accumulator = accumulator;
        _this.acc = acc;
        _this.concurrent = concurrent;
        _this.hasValue = false;
        _this.hasCompleted = false;
        _this.buffer = [];
        _this.active = 0;
        _this.index = 0;
        return _this;
    }
    MergeScanSubscriber.prototype._next = function (value) {
        if (this.active < this.concurrent) {
            var index = this.index++;
            var destination = this.destination;
            var ish = void 0;
            try {
                var accumulator = this.accumulator;
                ish = accumulator(this.acc, value, index);
            }
            catch (e) {
                return destination.error(e);
            }
            this.active++;
            this._innerSub(ish, value, index);
        }
        else {
            this.buffer.push(value);
        }
    };
    MergeScanSubscriber.prototype._innerSub = function (ish, value, index) {
        var innerSubscriber = new _InnerSubscriber__WEBPACK_IMPORTED_MODULE_3__["InnerSubscriber"](this, undefined, undefined);
        var destination = this.destination;
        destination.add(innerSubscriber);
        Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_1__["subscribeToResult"])(this, ish, value, index, innerSubscriber);
    };
    MergeScanSubscriber.prototype._complete = function () {
        this.hasCompleted = true;
        if (this.active === 0 && this.buffer.length === 0) {
            if (this.hasValue === false) {
                this.destination.next(this.acc);
            }
            this.destination.complete();
        }
        this.unsubscribe();
    };
    MergeScanSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        var destination = this.destination;
        this.acc = innerValue;
        this.hasValue = true;
        destination.next(innerValue);
    };
    MergeScanSubscriber.prototype.notifyComplete = function (innerSub) {
        var buffer = this.buffer;
        var destination = this.destination;
        destination.remove(innerSub);
        this.active--;
        if (buffer.length > 0) {
            this._next(buffer.shift());
        }
        else if (this.active === 0 && this.hasCompleted) {
            if (this.hasValue === false) {
                this.destination.next(this.acc);
            }
            this.destination.complete();
        }
    };
    return MergeScanSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));

//# sourceMappingURL=mergeScan.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/min.js":
/*!*********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/min.js ***!
  \*********************************************************************************************************************************************************************************/
/*! exports provided: min */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "min", function() { return min; });
/* harmony import */ var _reduce__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./reduce */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/reduce.js");
/** PURE_IMPORTS_START _reduce PURE_IMPORTS_END */

function min(comparer) {
    var min = (typeof comparer === 'function')
        ? function (x, y) { return comparer(x, y) < 0 ? x : y; }
        : function (x, y) { return x < y ? x : y; };
    return Object(_reduce__WEBPACK_IMPORTED_MODULE_0__["reduce"])(min);
}
//# sourceMappingURL=min.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: multicast, MulticastOperator */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(238);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/observeOn.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/observeOn.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: observeOn, ObserveOnOperator, ObserveOnSubscriber, ObserveOnMessage */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(225);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/onErrorResumeNext.js":
/*!***********************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/onErrorResumeNext.js ***!
  \***********************************************************************************************************************************************************************************************/
/*! exports provided: onErrorResumeNext, onErrorResumeNextStatic */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "onErrorResumeNext", function() { return onErrorResumeNext; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "onErrorResumeNextStatic", function() { return onErrorResumeNextStatic; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _observable_from__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../observable/from */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/from.js");
/* harmony import */ var _util_isArray__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/isArray */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isArray.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _InnerSubscriber__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../InnerSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_observable_from,_util_isArray,_OuterSubscriber,_InnerSubscriber,_util_subscribeToResult PURE_IMPORTS_END */






function onErrorResumeNext() {
    var nextSources = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        nextSources[_i] = arguments[_i];
    }
    if (nextSources.length === 1 && Object(_util_isArray__WEBPACK_IMPORTED_MODULE_2__["isArray"])(nextSources[0])) {
        nextSources = nextSources[0];
    }
    return function (source) { return source.lift(new OnErrorResumeNextOperator(nextSources)); };
}
function onErrorResumeNextStatic() {
    var nextSources = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        nextSources[_i] = arguments[_i];
    }
    var source = null;
    if (nextSources.length === 1 && Object(_util_isArray__WEBPACK_IMPORTED_MODULE_2__["isArray"])(nextSources[0])) {
        nextSources = nextSources[0];
    }
    source = nextSources.shift();
    return Object(_observable_from__WEBPACK_IMPORTED_MODULE_1__["from"])(source, null).lift(new OnErrorResumeNextOperator(nextSources));
}
var OnErrorResumeNextOperator = /*@__PURE__*/ (function () {
    function OnErrorResumeNextOperator(nextSources) {
        this.nextSources = nextSources;
    }
    OnErrorResumeNextOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new OnErrorResumeNextSubscriber(subscriber, this.nextSources));
    };
    return OnErrorResumeNextOperator;
}());
var OnErrorResumeNextSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](OnErrorResumeNextSubscriber, _super);
    function OnErrorResumeNextSubscriber(destination, nextSources) {
        var _this = _super.call(this, destination) || this;
        _this.destination = destination;
        _this.nextSources = nextSources;
        return _this;
    }
    OnErrorResumeNextSubscriber.prototype.notifyError = function (error, innerSub) {
        this.subscribeToNextSource();
    };
    OnErrorResumeNextSubscriber.prototype.notifyComplete = function (innerSub) {
        this.subscribeToNextSource();
    };
    OnErrorResumeNextSubscriber.prototype._error = function (err) {
        this.subscribeToNextSource();
        this.unsubscribe();
    };
    OnErrorResumeNextSubscriber.prototype._complete = function () {
        this.subscribeToNextSource();
        this.unsubscribe();
    };
    OnErrorResumeNextSubscriber.prototype.subscribeToNextSource = function () {
        var next = this.nextSources.shift();
        if (!!next) {
            var innerSubscriber = new _InnerSubscriber__WEBPACK_IMPORTED_MODULE_4__["InnerSubscriber"](this, undefined, undefined);
            var destination = this.destination;
            destination.add(innerSubscriber);
            Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_5__["subscribeToResult"])(this, next, undefined, undefined, innerSubscriber);
        }
        else {
            this.destination.complete();
        }
    };
    return OnErrorResumeNextSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__["OuterSubscriber"]));
//# sourceMappingURL=onErrorResumeNext.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pairwise.js":
/*!**************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pairwise.js ***!
  \**************************************************************************************************************************************************************************************/
/*! exports provided: pairwise */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "pairwise", function() { return pairwise; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function pairwise() {
    return function (source) { return source.lift(new PairwiseOperator()); };
}
var PairwiseOperator = /*@__PURE__*/ (function () {
    function PairwiseOperator() {
    }
    PairwiseOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new PairwiseSubscriber(subscriber));
    };
    return PairwiseOperator;
}());
var PairwiseSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](PairwiseSubscriber, _super);
    function PairwiseSubscriber(destination) {
        var _this = _super.call(this, destination) || this;
        _this.hasPrev = false;
        return _this;
    }
    PairwiseSubscriber.prototype._next = function (value) {
        if (this.hasPrev) {
            this.destination.next([this.prev, value]);
        }
        else {
            this.hasPrev = true;
        }
        this.prev = value;
    };
    return PairwiseSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=pairwise.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/partition.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/partition.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: partition */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "partition", function() { return partition; });
/* harmony import */ var _util_not__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../util/not */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/not.js");
/* harmony import */ var _filter__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./filter */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/filter.js");
/** PURE_IMPORTS_START _util_not,_filter PURE_IMPORTS_END */


function partition(predicate, thisArg) {
    return function (source) {
        return [
            Object(_filter__WEBPACK_IMPORTED_MODULE_1__["filter"])(predicate, thisArg)(source),
            Object(_filter__WEBPACK_IMPORTED_MODULE_1__["filter"])(Object(_util_not__WEBPACK_IMPORTED_MODULE_0__["not"])(predicate, thisArg))(source)
        ];
    };
}
//# sourceMappingURL=partition.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pluck.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pluck.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: pluck */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "pluck", function() { return pluck; });
/* harmony import */ var _map__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./map */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js");
/** PURE_IMPORTS_START _map PURE_IMPORTS_END */

function pluck() {
    var properties = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        properties[_i] = arguments[_i];
    }
    var length = properties.length;
    if (length === 0) {
        throw new Error('list of properties cannot be empty.');
    }
    return function (source) { return Object(_map__WEBPACK_IMPORTED_MODULE_0__["map"])(plucker(properties, length))(source); };
}
function plucker(props, length) {
    var mapper = function (x) {
        var currentProp = x;
        for (var i = 0; i < length; i++) {
            var p = currentProp[props[i]];
            if (typeof p !== 'undefined') {
                currentProp = p;
            }
            else {
                return undefined;
            }
        }
        return currentProp;
    };
    return mapper;
}
//# sourceMappingURL=pluck.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publish.js":
/*!*************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publish.js ***!
  \*************************************************************************************************************************************************************************************/
/*! exports provided: publish */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "publish", function() { return publish; });
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _multicast__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./multicast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js");
/** PURE_IMPORTS_START _Subject,_multicast PURE_IMPORTS_END */


function publish(selector) {
    return selector ?
        Object(_multicast__WEBPACK_IMPORTED_MODULE_1__["multicast"])(function () { return new _Subject__WEBPACK_IMPORTED_MODULE_0__["Subject"](); }, selector) :
        Object(_multicast__WEBPACK_IMPORTED_MODULE_1__["multicast"])(new _Subject__WEBPACK_IMPORTED_MODULE_0__["Subject"]());
}
//# sourceMappingURL=publish.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishBehavior.js":
/*!*********************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishBehavior.js ***!
  \*********************************************************************************************************************************************************************************************/
/*! exports provided: publishBehavior */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "publishBehavior", function() { return publishBehavior; });
/* harmony import */ var _BehaviorSubject__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../BehaviorSubject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/BehaviorSubject.js");
/* harmony import */ var _multicast__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./multicast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js");
/** PURE_IMPORTS_START _BehaviorSubject,_multicast PURE_IMPORTS_END */


function publishBehavior(value) {
    return function (source) { return Object(_multicast__WEBPACK_IMPORTED_MODULE_1__["multicast"])(new _BehaviorSubject__WEBPACK_IMPORTED_MODULE_0__["BehaviorSubject"](value))(source); };
}
//# sourceMappingURL=publishBehavior.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishLast.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishLast.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: publishLast */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "publishLast", function() { return publishLast; });
/* harmony import */ var _AsyncSubject__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../AsyncSubject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/AsyncSubject.js");
/* harmony import */ var _multicast__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./multicast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js");
/** PURE_IMPORTS_START _AsyncSubject,_multicast PURE_IMPORTS_END */


function publishLast() {
    return function (source) { return Object(_multicast__WEBPACK_IMPORTED_MODULE_1__["multicast"])(new _AsyncSubject__WEBPACK_IMPORTED_MODULE_0__["AsyncSubject"]())(source); };
}
//# sourceMappingURL=publishLast.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishReplay.js":
/*!*******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishReplay.js ***!
  \*******************************************************************************************************************************************************************************************/
/*! exports provided: publishReplay */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "publishReplay", function() { return publishReplay; });
/* harmony import */ var _ReplaySubject__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../ReplaySubject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/ReplaySubject.js");
/* harmony import */ var _multicast__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./multicast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js");
/** PURE_IMPORTS_START _ReplaySubject,_multicast PURE_IMPORTS_END */


function publishReplay(bufferSize, windowTime, selectorOrScheduler, scheduler) {
    if (selectorOrScheduler && typeof selectorOrScheduler !== 'function') {
        scheduler = selectorOrScheduler;
    }
    var selector = typeof selectorOrScheduler === 'function' ? selectorOrScheduler : undefined;
    var subject = new _ReplaySubject__WEBPACK_IMPORTED_MODULE_0__["ReplaySubject"](bufferSize, windowTime, scheduler);
    return function (source) { return Object(_multicast__WEBPACK_IMPORTED_MODULE_1__["multicast"])(function () { return subject; }, selector)(source); };
}
//# sourceMappingURL=publishReplay.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/race.js":
/*!**********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/race.js ***!
  \**********************************************************************************************************************************************************************************/
/*! exports provided: race */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "race", function() { return race; });
/* harmony import */ var _util_isArray__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../util/isArray */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isArray.js");
/* harmony import */ var _observable_race__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../observable/race */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/race.js");
/** PURE_IMPORTS_START _util_isArray,_observable_race PURE_IMPORTS_END */


function race() {
    var observables = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        observables[_i] = arguments[_i];
    }
    return function raceOperatorFunction(source) {
        if (observables.length === 1 && Object(_util_isArray__WEBPACK_IMPORTED_MODULE_0__["isArray"])(observables[0])) {
            observables = observables[0];
        }
        return source.lift.call(_observable_race__WEBPACK_IMPORTED_MODULE_1__["race"].apply(void 0, [source].concat(observables)));
    };
}
//# sourceMappingURL=race.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/reduce.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/reduce.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: reduce */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(166);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/refCount.js":
/*!*********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/refCount.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************************/
/*! exports provided: refCount */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(127);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeat.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeat.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: repeat */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "repeat", function() { return repeat; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _observable_empty__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../observable/empty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/empty.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_observable_empty PURE_IMPORTS_END */



function repeat(count) {
    if (count === void 0) {
        count = -1;
    }
    return function (source) {
        if (count === 0) {
            return Object(_observable_empty__WEBPACK_IMPORTED_MODULE_2__["empty"])();
        }
        else if (count < 0) {
            return source.lift(new RepeatOperator(-1, source));
        }
        else {
            return source.lift(new RepeatOperator(count - 1, source));
        }
    };
}
var RepeatOperator = /*@__PURE__*/ (function () {
    function RepeatOperator(count, source) {
        this.count = count;
        this.source = source;
    }
    RepeatOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new RepeatSubscriber(subscriber, this.count, this.source));
    };
    return RepeatOperator;
}());
var RepeatSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](RepeatSubscriber, _super);
    function RepeatSubscriber(destination, count, source) {
        var _this = _super.call(this, destination) || this;
        _this.count = count;
        _this.source = source;
        return _this;
    }
    RepeatSubscriber.prototype.complete = function () {
        if (!this.isStopped) {
            var _a = this, source = _a.source, count = _a.count;
            if (count === 0) {
                return _super.prototype.complete.call(this);
            }
            else if (count > -1) {
                this.count = count - 1;
            }
            source.subscribe(this._unsubscribeAndRecycle());
        }
    };
    return RepeatSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=repeat.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeatWhen.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeatWhen.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: repeatWhen */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "repeatWhen", function() { return repeatWhen; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subject,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function repeatWhen(notifier) {
    return function (source) { return source.lift(new RepeatWhenOperator(notifier)); };
}
var RepeatWhenOperator = /*@__PURE__*/ (function () {
    function RepeatWhenOperator(notifier) {
        this.notifier = notifier;
    }
    RepeatWhenOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new RepeatWhenSubscriber(subscriber, this.notifier, source));
    };
    return RepeatWhenOperator;
}());
var RepeatWhenSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](RepeatWhenSubscriber, _super);
    function RepeatWhenSubscriber(destination, notifier, source) {
        var _this = _super.call(this, destination) || this;
        _this.notifier = notifier;
        _this.source = source;
        _this.sourceIsBeingSubscribedTo = true;
        return _this;
    }
    RepeatWhenSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.sourceIsBeingSubscribedTo = true;
        this.source.subscribe(this);
    };
    RepeatWhenSubscriber.prototype.notifyComplete = function (innerSub) {
        if (this.sourceIsBeingSubscribedTo === false) {
            return _super.prototype.complete.call(this);
        }
    };
    RepeatWhenSubscriber.prototype.complete = function () {
        this.sourceIsBeingSubscribedTo = false;
        if (!this.isStopped) {
            if (!this.retries) {
                this.subscribeToRetries();
            }
            if (!this.retriesSubscription || this.retriesSubscription.closed) {
                return _super.prototype.complete.call(this);
            }
            this._unsubscribeAndRecycle();
            this.notifications.next();
        }
    };
    RepeatWhenSubscriber.prototype._unsubscribe = function () {
        var _a = this, notifications = _a.notifications, retriesSubscription = _a.retriesSubscription;
        if (notifications) {
            notifications.unsubscribe();
            this.notifications = null;
        }
        if (retriesSubscription) {
            retriesSubscription.unsubscribe();
            this.retriesSubscription = null;
        }
        this.retries = null;
    };
    RepeatWhenSubscriber.prototype._unsubscribeAndRecycle = function () {
        var _unsubscribe = this._unsubscribe;
        this._unsubscribe = null;
        _super.prototype._unsubscribeAndRecycle.call(this);
        this._unsubscribe = _unsubscribe;
        return this;
    };
    RepeatWhenSubscriber.prototype.subscribeToRetries = function () {
        this.notifications = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
        var retries;
        try {
            var notifier = this.notifier;
            retries = notifier(this.notifications);
        }
        catch (e) {
            return _super.prototype.complete.call(this);
        }
        this.retries = retries;
        this.retriesSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(this, retries);
    };
    return RepeatWhenSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));
//# sourceMappingURL=repeatWhen.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retry.js":
/*!***********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retry.js ***!
  \***********************************************************************************************************************************************************************************/
/*! exports provided: retry */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "retry", function() { return retry; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function retry(count) {
    if (count === void 0) {
        count = -1;
    }
    return function (source) { return source.lift(new RetryOperator(count, source)); };
}
var RetryOperator = /*@__PURE__*/ (function () {
    function RetryOperator(count, source) {
        this.count = count;
        this.source = source;
    }
    RetryOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new RetrySubscriber(subscriber, this.count, this.source));
    };
    return RetryOperator;
}());
var RetrySubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](RetrySubscriber, _super);
    function RetrySubscriber(destination, count, source) {
        var _this = _super.call(this, destination) || this;
        _this.count = count;
        _this.source = source;
        return _this;
    }
    RetrySubscriber.prototype.error = function (err) {
        if (!this.isStopped) {
            var _a = this, source = _a.source, count = _a.count;
            if (count === 0) {
                return _super.prototype.error.call(this, err);
            }
            else if (count > -1) {
                this.count = count - 1;
            }
            source.subscribe(this._unsubscribeAndRecycle());
        }
    };
    return RetrySubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=retry.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retryWhen.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retryWhen.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: retryWhen */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "retryWhen", function() { return retryWhen; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subject,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function retryWhen(notifier) {
    return function (source) { return source.lift(new RetryWhenOperator(notifier, source)); };
}
var RetryWhenOperator = /*@__PURE__*/ (function () {
    function RetryWhenOperator(notifier, source) {
        this.notifier = notifier;
        this.source = source;
    }
    RetryWhenOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new RetryWhenSubscriber(subscriber, this.notifier, this.source));
    };
    return RetryWhenOperator;
}());
var RetryWhenSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](RetryWhenSubscriber, _super);
    function RetryWhenSubscriber(destination, notifier, source) {
        var _this = _super.call(this, destination) || this;
        _this.notifier = notifier;
        _this.source = source;
        return _this;
    }
    RetryWhenSubscriber.prototype.error = function (err) {
        if (!this.isStopped) {
            var errors = this.errors;
            var retries = this.retries;
            var retriesSubscription = this.retriesSubscription;
            if (!retries) {
                errors = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
                try {
                    var notifier = this.notifier;
                    retries = notifier(errors);
                }
                catch (e) {
                    return _super.prototype.error.call(this, e);
                }
                retriesSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(this, retries);
            }
            else {
                this.errors = null;
                this.retriesSubscription = null;
            }
            this._unsubscribeAndRecycle();
            this.errors = errors;
            this.retries = retries;
            this.retriesSubscription = retriesSubscription;
            errors.next(err);
        }
    };
    RetryWhenSubscriber.prototype._unsubscribe = function () {
        var _a = this, errors = _a.errors, retriesSubscription = _a.retriesSubscription;
        if (errors) {
            errors.unsubscribe();
            this.errors = null;
        }
        if (retriesSubscription) {
            retriesSubscription.unsubscribe();
            this.retriesSubscription = null;
        }
        this.retries = null;
    };
    RetryWhenSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        var _unsubscribe = this._unsubscribe;
        this._unsubscribe = null;
        this._unsubscribeAndRecycle();
        this._unsubscribe = _unsubscribe;
        this.source.subscribe(this);
    };
    return RetryWhenSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));
//# sourceMappingURL=retryWhen.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sample.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sample.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: sample */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "sample", function() { return sample; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function sample(notifier) {
    return function (source) { return source.lift(new SampleOperator(notifier)); };
}
var SampleOperator = /*@__PURE__*/ (function () {
    function SampleOperator(notifier) {
        this.notifier = notifier;
    }
    SampleOperator.prototype.call = function (subscriber, source) {
        var sampleSubscriber = new SampleSubscriber(subscriber);
        var subscription = source.subscribe(sampleSubscriber);
        subscription.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(sampleSubscriber, this.notifier));
        return subscription;
    };
    return SampleOperator;
}());
var SampleSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SampleSubscriber, _super);
    function SampleSubscriber() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.hasValue = false;
        return _this;
    }
    SampleSubscriber.prototype._next = function (value) {
        this.value = value;
        this.hasValue = true;
    };
    SampleSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.emitValue();
    };
    SampleSubscriber.prototype.notifyComplete = function () {
        this.emitValue();
    };
    SampleSubscriber.prototype.emitValue = function () {
        if (this.hasValue) {
            this.hasValue = false;
            this.destination.next(this.value);
        }
    };
    return SampleSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=sample.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sampleTime.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sampleTime.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: sampleTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "sampleTime", function() { return sampleTime; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_scheduler_async PURE_IMPORTS_END */



function sampleTime(period, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_2__["async"];
    }
    return function (source) { return source.lift(new SampleTimeOperator(period, scheduler)); };
}
var SampleTimeOperator = /*@__PURE__*/ (function () {
    function SampleTimeOperator(period, scheduler) {
        this.period = period;
        this.scheduler = scheduler;
    }
    SampleTimeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SampleTimeSubscriber(subscriber, this.period, this.scheduler));
    };
    return SampleTimeOperator;
}());
var SampleTimeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SampleTimeSubscriber, _super);
    function SampleTimeSubscriber(destination, period, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.period = period;
        _this.scheduler = scheduler;
        _this.hasValue = false;
        _this.add(scheduler.schedule(dispatchNotification, period, { subscriber: _this, period: period }));
        return _this;
    }
    SampleTimeSubscriber.prototype._next = function (value) {
        this.lastValue = value;
        this.hasValue = true;
    };
    SampleTimeSubscriber.prototype.notifyNext = function () {
        if (this.hasValue) {
            this.hasValue = false;
            this.destination.next(this.lastValue);
        }
    };
    return SampleTimeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
function dispatchNotification(state) {
    var subscriber = state.subscriber, period = state.period;
    subscriber.notifyNext();
    this.schedule(state, period);
}
//# sourceMappingURL=sampleTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/scan.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/scan.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: scan */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(138);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sequenceEqual.js":
/*!*******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sequenceEqual.js ***!
  \*******************************************************************************************************************************************************************************************/
/*! exports provided: sequenceEqual, SequenceEqualOperator, SequenceEqualSubscriber */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "sequenceEqual", function() { return sequenceEqual; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SequenceEqualOperator", function() { return SequenceEqualOperator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SequenceEqualSubscriber", function() { return SequenceEqualSubscriber; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function sequenceEqual(compareTo, comparator) {
    return function (source) { return source.lift(new SequenceEqualOperator(compareTo, comparator)); };
}
var SequenceEqualOperator = /*@__PURE__*/ (function () {
    function SequenceEqualOperator(compareTo, comparator) {
        this.compareTo = compareTo;
        this.comparator = comparator;
    }
    SequenceEqualOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SequenceEqualSubscriber(subscriber, this.compareTo, this.comparator));
    };
    return SequenceEqualOperator;
}());

var SequenceEqualSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SequenceEqualSubscriber, _super);
    function SequenceEqualSubscriber(destination, compareTo, comparator) {
        var _this = _super.call(this, destination) || this;
        _this.compareTo = compareTo;
        _this.comparator = comparator;
        _this._a = [];
        _this._b = [];
        _this._oneComplete = false;
        _this.destination.add(compareTo.subscribe(new SequenceEqualCompareToSubscriber(destination, _this)));
        return _this;
    }
    SequenceEqualSubscriber.prototype._next = function (value) {
        if (this._oneComplete && this._b.length === 0) {
            this.emit(false);
        }
        else {
            this._a.push(value);
            this.checkValues();
        }
    };
    SequenceEqualSubscriber.prototype._complete = function () {
        if (this._oneComplete) {
            this.emit(this._a.length === 0 && this._b.length === 0);
        }
        else {
            this._oneComplete = true;
        }
        this.unsubscribe();
    };
    SequenceEqualSubscriber.prototype.checkValues = function () {
        var _c = this, _a = _c._a, _b = _c._b, comparator = _c.comparator;
        while (_a.length > 0 && _b.length > 0) {
            var a = _a.shift();
            var b = _b.shift();
            var areEqual = false;
            try {
                areEqual = comparator ? comparator(a, b) : a === b;
            }
            catch (e) {
                this.destination.error(e);
            }
            if (!areEqual) {
                this.emit(false);
            }
        }
    };
    SequenceEqualSubscriber.prototype.emit = function (value) {
        var destination = this.destination;
        destination.next(value);
        destination.complete();
    };
    SequenceEqualSubscriber.prototype.nextB = function (value) {
        if (this._oneComplete && this._a.length === 0) {
            this.emit(false);
        }
        else {
            this._b.push(value);
            this.checkValues();
        }
    };
    SequenceEqualSubscriber.prototype.completeB = function () {
        if (this._oneComplete) {
            this.emit(this._a.length === 0 && this._b.length === 0);
        }
        else {
            this._oneComplete = true;
        }
    };
    return SequenceEqualSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));

var SequenceEqualCompareToSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SequenceEqualCompareToSubscriber, _super);
    function SequenceEqualCompareToSubscriber(destination, parent) {
        var _this = _super.call(this, destination) || this;
        _this.parent = parent;
        return _this;
    }
    SequenceEqualCompareToSubscriber.prototype._next = function (value) {
        this.parent.nextB(value);
    };
    SequenceEqualCompareToSubscriber.prototype._error = function (err) {
        this.parent.error(err);
        this.unsubscribe();
    };
    SequenceEqualCompareToSubscriber.prototype._complete = function () {
        this.parent.completeB();
        this.unsubscribe();
    };
    return SequenceEqualCompareToSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=sequenceEqual.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/share.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/share.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: share */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(167);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/shareReplay.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/shareReplay.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: shareReplay */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "shareReplay", function() { return shareReplay; });
/* harmony import */ var _ReplaySubject__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../ReplaySubject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/ReplaySubject.js");
/** PURE_IMPORTS_START _ReplaySubject PURE_IMPORTS_END */

function shareReplay(configOrBufferSize, windowTime, scheduler) {
    var config;
    if (configOrBufferSize && typeof configOrBufferSize === 'object') {
        config = configOrBufferSize;
    }
    else {
        config = {
            bufferSize: configOrBufferSize,
            windowTime: windowTime,
            refCount: false,
            scheduler: scheduler
        };
    }
    return function (source) { return source.lift(shareReplayOperator(config)); };
}
function shareReplayOperator(_a) {
    var _b = _a.bufferSize, bufferSize = _b === void 0 ? Number.POSITIVE_INFINITY : _b, _c = _a.windowTime, windowTime = _c === void 0 ? Number.POSITIVE_INFINITY : _c, useRefCount = _a.refCount, scheduler = _a.scheduler;
    var subject;
    var refCount = 0;
    var subscription;
    var hasError = false;
    var isComplete = false;
    return function shareReplayOperation(source) {
        refCount++;
        if (!subject || hasError) {
            hasError = false;
            subject = new _ReplaySubject__WEBPACK_IMPORTED_MODULE_0__["ReplaySubject"](bufferSize, windowTime, scheduler);
            subscription = source.subscribe({
                next: function (value) { subject.next(value); },
                error: function (err) {
                    hasError = true;
                    subject.error(err);
                },
                complete: function () {
                    isComplete = true;
                    subject.complete();
                },
            });
        }
        var innerSub = subject.subscribe(this);
        this.add(function () {
            refCount--;
            innerSub.unsubscribe();
            if (subscription && !isComplete && useRefCount && refCount === 0) {
                subscription.unsubscribe();
                subscription = undefined;
                subject = undefined;
            }
        });
    };
}
//# sourceMappingURL=shareReplay.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/single.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/single.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: single */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "single", function() { return single; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _util_EmptyError__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/EmptyError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/EmptyError.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_util_EmptyError PURE_IMPORTS_END */



function single(predicate) {
    return function (source) { return source.lift(new SingleOperator(predicate, source)); };
}
var SingleOperator = /*@__PURE__*/ (function () {
    function SingleOperator(predicate, source) {
        this.predicate = predicate;
        this.source = source;
    }
    SingleOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SingleSubscriber(subscriber, this.predicate, this.source));
    };
    return SingleOperator;
}());
var SingleSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SingleSubscriber, _super);
    function SingleSubscriber(destination, predicate, source) {
        var _this = _super.call(this, destination) || this;
        _this.predicate = predicate;
        _this.source = source;
        _this.seenValue = false;
        _this.index = 0;
        return _this;
    }
    SingleSubscriber.prototype.applySingleValue = function (value) {
        if (this.seenValue) {
            this.destination.error('Sequence contains more than one element');
        }
        else {
            this.seenValue = true;
            this.singleValue = value;
        }
    };
    SingleSubscriber.prototype._next = function (value) {
        var index = this.index++;
        if (this.predicate) {
            this.tryNext(value, index);
        }
        else {
            this.applySingleValue(value);
        }
    };
    SingleSubscriber.prototype.tryNext = function (value, index) {
        try {
            if (this.predicate(value, index, this.source)) {
                this.applySingleValue(value);
            }
        }
        catch (err) {
            this.destination.error(err);
        }
    };
    SingleSubscriber.prototype._complete = function () {
        var destination = this.destination;
        if (this.index > 0) {
            destination.next(this.seenValue ? this.singleValue : undefined);
            destination.complete();
        }
        else {
            destination.error(new _util_EmptyError__WEBPACK_IMPORTED_MODULE_2__["EmptyError"]);
        }
    };
    return SingleSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=single.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skip.js":
/*!**********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skip.js ***!
  \**********************************************************************************************************************************************************************************/
/*! exports provided: skip */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "skip", function() { return skip; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function skip(count) {
    return function (source) { return source.lift(new SkipOperator(count)); };
}
var SkipOperator = /*@__PURE__*/ (function () {
    function SkipOperator(total) {
        this.total = total;
    }
    SkipOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SkipSubscriber(subscriber, this.total));
    };
    return SkipOperator;
}());
var SkipSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SkipSubscriber, _super);
    function SkipSubscriber(destination, total) {
        var _this = _super.call(this, destination) || this;
        _this.total = total;
        _this.count = 0;
        return _this;
    }
    SkipSubscriber.prototype._next = function (x) {
        if (++this.count > this.total) {
            this.destination.next(x);
        }
    };
    return SkipSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=skip.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipLast.js":
/*!**************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipLast.js ***!
  \**************************************************************************************************************************************************************************************/
/*! exports provided: skipLast */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "skipLast", function() { return skipLast; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _util_ArgumentOutOfRangeError__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/ArgumentOutOfRangeError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/ArgumentOutOfRangeError.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_util_ArgumentOutOfRangeError PURE_IMPORTS_END */



function skipLast(count) {
    return function (source) { return source.lift(new SkipLastOperator(count)); };
}
var SkipLastOperator = /*@__PURE__*/ (function () {
    function SkipLastOperator(_skipCount) {
        this._skipCount = _skipCount;
        if (this._skipCount < 0) {
            throw new _util_ArgumentOutOfRangeError__WEBPACK_IMPORTED_MODULE_2__["ArgumentOutOfRangeError"];
        }
    }
    SkipLastOperator.prototype.call = function (subscriber, source) {
        if (this._skipCount === 0) {
            return source.subscribe(new _Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"](subscriber));
        }
        else {
            return source.subscribe(new SkipLastSubscriber(subscriber, this._skipCount));
        }
    };
    return SkipLastOperator;
}());
var SkipLastSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SkipLastSubscriber, _super);
    function SkipLastSubscriber(destination, _skipCount) {
        var _this = _super.call(this, destination) || this;
        _this._skipCount = _skipCount;
        _this._count = 0;
        _this._ring = new Array(_skipCount);
        return _this;
    }
    SkipLastSubscriber.prototype._next = function (value) {
        var skipCount = this._skipCount;
        var count = this._count++;
        if (count < skipCount) {
            this._ring[count] = value;
        }
        else {
            var currentIndex = count % skipCount;
            var ring = this._ring;
            var oldValue = ring[currentIndex];
            ring[currentIndex] = value;
            this.destination.next(oldValue);
        }
    };
    return SkipLastSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=skipLast.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipUntil.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipUntil.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: skipUntil */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "skipUntil", function() { return skipUntil; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _InnerSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../InnerSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/InnerSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_InnerSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function skipUntil(notifier) {
    return function (source) { return source.lift(new SkipUntilOperator(notifier)); };
}
var SkipUntilOperator = /*@__PURE__*/ (function () {
    function SkipUntilOperator(notifier) {
        this.notifier = notifier;
    }
    SkipUntilOperator.prototype.call = function (destination, source) {
        return source.subscribe(new SkipUntilSubscriber(destination, this.notifier));
    };
    return SkipUntilOperator;
}());
var SkipUntilSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SkipUntilSubscriber, _super);
    function SkipUntilSubscriber(destination, notifier) {
        var _this = _super.call(this, destination) || this;
        _this.hasValue = false;
        var innerSubscriber = new _InnerSubscriber__WEBPACK_IMPORTED_MODULE_2__["InnerSubscriber"](_this, undefined, undefined);
        _this.add(innerSubscriber);
        _this.innerSubscription = innerSubscriber;
        Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(_this, notifier, undefined, undefined, innerSubscriber);
        return _this;
    }
    SkipUntilSubscriber.prototype._next = function (value) {
        if (this.hasValue) {
            _super.prototype._next.call(this, value);
        }
    };
    SkipUntilSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.hasValue = true;
        if (this.innerSubscription) {
            this.innerSubscription.unsubscribe();
        }
    };
    SkipUntilSubscriber.prototype.notifyComplete = function () {
    };
    return SkipUntilSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=skipUntil.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipWhile.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipWhile.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: skipWhile */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "skipWhile", function() { return skipWhile; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function skipWhile(predicate) {
    return function (source) { return source.lift(new SkipWhileOperator(predicate)); };
}
var SkipWhileOperator = /*@__PURE__*/ (function () {
    function SkipWhileOperator(predicate) {
        this.predicate = predicate;
    }
    SkipWhileOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new SkipWhileSubscriber(subscriber, this.predicate));
    };
    return SkipWhileOperator;
}());
var SkipWhileSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](SkipWhileSubscriber, _super);
    function SkipWhileSubscriber(destination, predicate) {
        var _this = _super.call(this, destination) || this;
        _this.predicate = predicate;
        _this.skipping = true;
        _this.index = 0;
        return _this;
    }
    SkipWhileSubscriber.prototype._next = function (value) {
        var destination = this.destination;
        if (this.skipping) {
            this.tryCallPredicate(value);
        }
        if (!this.skipping) {
            destination.next(value);
        }
    };
    SkipWhileSubscriber.prototype.tryCallPredicate = function (value) {
        try {
            var result = this.predicate(value, this.index++);
            this.skipping = Boolean(result);
        }
        catch (err) {
            this.destination.error(err);
        }
    };
    return SkipWhileSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=skipWhile.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/startWith.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/startWith.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: startWith */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(413);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/subscribeOn.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/subscribeOn.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: subscribeOn */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "subscribeOn", function() { return subscribeOn; });
/* harmony import */ var _observable_SubscribeOnObservable__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/SubscribeOnObservable */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/SubscribeOnObservable.js");
/** PURE_IMPORTS_START _observable_SubscribeOnObservable PURE_IMPORTS_END */

function subscribeOn(scheduler, delay) {
    if (delay === void 0) {
        delay = 0;
    }
    return function subscribeOnOperatorFunction(source) {
        return source.lift(new SubscribeOnOperator(scheduler, delay));
    };
}
var SubscribeOnOperator = /*@__PURE__*/ (function () {
    function SubscribeOnOperator(scheduler, delay) {
        this.scheduler = scheduler;
        this.delay = delay;
    }
    SubscribeOnOperator.prototype.call = function (subscriber, source) {
        return new _observable_SubscribeOnObservable__WEBPACK_IMPORTED_MODULE_0__["SubscribeOnObservable"](source, this.delay, this.scheduler).subscribe(subscriber);
    };
    return SubscribeOnOperator;
}());
//# sourceMappingURL=subscribeOn.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchAll.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchAll.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: switchAll */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "switchAll", function() { return switchAll; });
/* harmony import */ var _switchMap__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./switchMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMap.js");
/* harmony import */ var _util_identity__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../util/identity */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/identity.js");
/** PURE_IMPORTS_START _switchMap,_util_identity PURE_IMPORTS_END */


function switchAll() {
    return Object(_switchMap__WEBPACK_IMPORTED_MODULE_0__["switchMap"])(_util_identity__WEBPACK_IMPORTED_MODULE_1__["identity"]);
}
//# sourceMappingURL=switchAll.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMap.js":
/*!**********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMap.js from dll-reference vendor_chunk ***!
  \**********************************************************************************************************************************************************************************************************************************/
/*! exports provided: switchMap */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(65);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMapTo.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMapTo.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: switchMapTo */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "switchMapTo", function() { return switchMapTo; });
/* harmony import */ var _switchMap__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./switchMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMap.js");
/** PURE_IMPORTS_START _switchMap PURE_IMPORTS_END */

function switchMapTo(innerObservable, resultSelector) {
    return resultSelector ? Object(_switchMap__WEBPACK_IMPORTED_MODULE_0__["switchMap"])(function () { return innerObservable; }, resultSelector) : Object(_switchMap__WEBPACK_IMPORTED_MODULE_0__["switchMap"])(function () { return innerObservable; });
}
//# sourceMappingURL=switchMapTo.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/take.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/take.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: take */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(78);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeLast.js":
/*!*********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeLast.js from dll-reference vendor_chunk ***!
  \*********************************************************************************************************************************************************************************************************************************/
/*! exports provided: takeLast */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(110);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeUntil.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeUntil.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: takeUntil */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "takeUntil", function() { return takeUntil; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function takeUntil(notifier) {
    return function (source) { return source.lift(new TakeUntilOperator(notifier)); };
}
var TakeUntilOperator = /*@__PURE__*/ (function () {
    function TakeUntilOperator(notifier) {
        this.notifier = notifier;
    }
    TakeUntilOperator.prototype.call = function (subscriber, source) {
        var takeUntilSubscriber = new TakeUntilSubscriber(subscriber);
        var notifierSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(takeUntilSubscriber, this.notifier);
        if (notifierSubscription && !takeUntilSubscriber.seenValue) {
            takeUntilSubscriber.add(notifierSubscription);
            return source.subscribe(takeUntilSubscriber);
        }
        return takeUntilSubscriber;
    };
    return TakeUntilOperator;
}());
var TakeUntilSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](TakeUntilSubscriber, _super);
    function TakeUntilSubscriber(destination) {
        var _this = _super.call(this, destination) || this;
        _this.seenValue = false;
        return _this;
    }
    TakeUntilSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.seenValue = true;
        this.complete();
    };
    TakeUntilSubscriber.prototype.notifyComplete = function () {
    };
    return TakeUntilSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=takeUntil.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeWhile.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeWhile.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: takeWhile */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "takeWhile", function() { return takeWhile; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/** PURE_IMPORTS_START tslib,_Subscriber PURE_IMPORTS_END */


function takeWhile(predicate, inclusive) {
    if (inclusive === void 0) {
        inclusive = false;
    }
    return function (source) {
        return source.lift(new TakeWhileOperator(predicate, inclusive));
    };
}
var TakeWhileOperator = /*@__PURE__*/ (function () {
    function TakeWhileOperator(predicate, inclusive) {
        this.predicate = predicate;
        this.inclusive = inclusive;
    }
    TakeWhileOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new TakeWhileSubscriber(subscriber, this.predicate, this.inclusive));
    };
    return TakeWhileOperator;
}());
var TakeWhileSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](TakeWhileSubscriber, _super);
    function TakeWhileSubscriber(destination, predicate, inclusive) {
        var _this = _super.call(this, destination) || this;
        _this.predicate = predicate;
        _this.inclusive = inclusive;
        _this.index = 0;
        return _this;
    }
    TakeWhileSubscriber.prototype._next = function (value) {
        var destination = this.destination;
        var result;
        try {
            result = this.predicate(value, this.index++);
        }
        catch (err) {
            destination.error(err);
            return;
        }
        this.nextOrComplete(value, result);
    };
    TakeWhileSubscriber.prototype.nextOrComplete = function (value, predicateResult) {
        var destination = this.destination;
        if (Boolean(predicateResult)) {
            destination.next(value);
        }
        else {
            if (this.inclusive) {
                destination.next(value);
            }
            destination.complete();
        }
    };
    return TakeWhileSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=takeWhile.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/tap.js":
/*!****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/tap.js from dll-reference vendor_chunk ***!
  \****************************************************************************************************************************************************************************************************************************/
/*! exports provided: tap */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(38);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttle.js":
/*!**************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttle.js ***!
  \**************************************************************************************************************************************************************************************/
/*! exports provided: defaultThrottleConfig, throttle */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "defaultThrottleConfig", function() { return defaultThrottleConfig; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "throttle", function() { return throttle; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



var defaultThrottleConfig = {
    leading: true,
    trailing: false
};
function throttle(durationSelector, config) {
    if (config === void 0) {
        config = defaultThrottleConfig;
    }
    return function (source) { return source.lift(new ThrottleOperator(durationSelector, config.leading, config.trailing)); };
}
var ThrottleOperator = /*@__PURE__*/ (function () {
    function ThrottleOperator(durationSelector, leading, trailing) {
        this.durationSelector = durationSelector;
        this.leading = leading;
        this.trailing = trailing;
    }
    ThrottleOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new ThrottleSubscriber(subscriber, this.durationSelector, this.leading, this.trailing));
    };
    return ThrottleOperator;
}());
var ThrottleSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](ThrottleSubscriber, _super);
    function ThrottleSubscriber(destination, durationSelector, _leading, _trailing) {
        var _this = _super.call(this, destination) || this;
        _this.destination = destination;
        _this.durationSelector = durationSelector;
        _this._leading = _leading;
        _this._trailing = _trailing;
        _this._hasValue = false;
        return _this;
    }
    ThrottleSubscriber.prototype._next = function (value) {
        this._hasValue = true;
        this._sendValue = value;
        if (!this._throttled) {
            if (this._leading) {
                this.send();
            }
            else {
                this.throttle(value);
            }
        }
    };
    ThrottleSubscriber.prototype.send = function () {
        var _a = this, _hasValue = _a._hasValue, _sendValue = _a._sendValue;
        if (_hasValue) {
            this.destination.next(_sendValue);
            this.throttle(_sendValue);
        }
        this._hasValue = false;
        this._sendValue = null;
    };
    ThrottleSubscriber.prototype.throttle = function (value) {
        var duration = this.tryDurationSelector(value);
        if (!!duration) {
            this.add(this._throttled = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(this, duration));
        }
    };
    ThrottleSubscriber.prototype.tryDurationSelector = function (value) {
        try {
            return this.durationSelector(value);
        }
        catch (err) {
            this.destination.error(err);
            return null;
        }
    };
    ThrottleSubscriber.prototype.throttlingDone = function () {
        var _a = this, _throttled = _a._throttled, _trailing = _a._trailing;
        if (_throttled) {
            _throttled.unsubscribe();
        }
        this._throttled = null;
        if (_trailing) {
            this.send();
        }
    };
    ThrottleSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.throttlingDone();
    };
    ThrottleSubscriber.prototype.notifyComplete = function () {
        this.throttlingDone();
    };
    return ThrottleSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=throttle.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttleTime.js":
/*!******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttleTime.js ***!
  \******************************************************************************************************************************************************************************************/
/*! exports provided: throttleTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "throttleTime", function() { return throttleTime; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _throttle__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./throttle */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttle.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_scheduler_async,_throttle PURE_IMPORTS_END */




function throttleTime(duration, scheduler, config) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_2__["async"];
    }
    if (config === void 0) {
        config = _throttle__WEBPACK_IMPORTED_MODULE_3__["defaultThrottleConfig"];
    }
    return function (source) { return source.lift(new ThrottleTimeOperator(duration, scheduler, config.leading, config.trailing)); };
}
var ThrottleTimeOperator = /*@__PURE__*/ (function () {
    function ThrottleTimeOperator(duration, scheduler, leading, trailing) {
        this.duration = duration;
        this.scheduler = scheduler;
        this.leading = leading;
        this.trailing = trailing;
    }
    ThrottleTimeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new ThrottleTimeSubscriber(subscriber, this.duration, this.scheduler, this.leading, this.trailing));
    };
    return ThrottleTimeOperator;
}());
var ThrottleTimeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](ThrottleTimeSubscriber, _super);
    function ThrottleTimeSubscriber(destination, duration, scheduler, leading, trailing) {
        var _this = _super.call(this, destination) || this;
        _this.duration = duration;
        _this.scheduler = scheduler;
        _this.leading = leading;
        _this.trailing = trailing;
        _this._hasTrailingValue = false;
        _this._trailingValue = null;
        return _this;
    }
    ThrottleTimeSubscriber.prototype._next = function (value) {
        if (this.throttled) {
            if (this.trailing) {
                this._trailingValue = value;
                this._hasTrailingValue = true;
            }
        }
        else {
            this.add(this.throttled = this.scheduler.schedule(dispatchNext, this.duration, { subscriber: this }));
            if (this.leading) {
                this.destination.next(value);
            }
        }
    };
    ThrottleTimeSubscriber.prototype._complete = function () {
        if (this._hasTrailingValue) {
            this.destination.next(this._trailingValue);
            this.destination.complete();
        }
        else {
            this.destination.complete();
        }
    };
    ThrottleTimeSubscriber.prototype.clearThrottle = function () {
        var throttled = this.throttled;
        if (throttled) {
            if (this.trailing && this._hasTrailingValue) {
                this.destination.next(this._trailingValue);
                this._trailingValue = null;
                this._hasTrailingValue = false;
            }
            throttled.unsubscribe();
            this.remove(throttled);
            this.throttled = null;
        }
    };
    return ThrottleTimeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
function dispatchNext(arg) {
    var subscriber = arg.subscriber;
    subscriber.clearThrottle();
}
//# sourceMappingURL=throttleTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throwIfEmpty.js":
/*!*************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throwIfEmpty.js from dll-reference vendor_chunk ***!
  \*************************************************************************************************************************************************************************************************************************************/
/*! exports provided: throwIfEmpty */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(137);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeInterval.js":
/*!******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeInterval.js ***!
  \******************************************************************************************************************************************************************************************/
/*! exports provided: timeInterval, TimeInterval */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "timeInterval", function() { return timeInterval; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TimeInterval", function() { return TimeInterval; });
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _scan__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./scan */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/scan.js");
/* harmony import */ var _observable_defer__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../observable/defer */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/defer.js");
/* harmony import */ var _map__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./map */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js");
/** PURE_IMPORTS_START _scheduler_async,_scan,_observable_defer,_map PURE_IMPORTS_END */




function timeInterval(scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_0__["async"];
    }
    return function (source) {
        return Object(_observable_defer__WEBPACK_IMPORTED_MODULE_2__["defer"])(function () {
            return source.pipe(Object(_scan__WEBPACK_IMPORTED_MODULE_1__["scan"])(function (_a, value) {
                var current = _a.current;
                return ({ value: value, current: scheduler.now(), last: current });
            }, { current: scheduler.now(), value: undefined, last: undefined }), Object(_map__WEBPACK_IMPORTED_MODULE_3__["map"])(function (_a) {
                var current = _a.current, last = _a.last, value = _a.value;
                return new TimeInterval(value, current - last);
            }));
        });
    };
}
var TimeInterval = /*@__PURE__*/ (function () {
    function TimeInterval(value, interval) {
        this.value = value;
        this.interval = interval;
    }
    return TimeInterval;
}());

//# sourceMappingURL=timeInterval.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeout.js":
/*!*************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeout.js ***!
  \*************************************************************************************************************************************************************************************/
/*! exports provided: timeout */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "timeout", function() { return timeout; });
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _util_TimeoutError__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../util/TimeoutError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/TimeoutError.js");
/* harmony import */ var _timeoutWith__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./timeoutWith */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeoutWith.js");
/* harmony import */ var _observable_throwError__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../observable/throwError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/throwError.js");
/** PURE_IMPORTS_START _scheduler_async,_util_TimeoutError,_timeoutWith,_observable_throwError PURE_IMPORTS_END */




function timeout(due, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_0__["async"];
    }
    return Object(_timeoutWith__WEBPACK_IMPORTED_MODULE_2__["timeoutWith"])(due, Object(_observable_throwError__WEBPACK_IMPORTED_MODULE_3__["throwError"])(new _util_TimeoutError__WEBPACK_IMPORTED_MODULE_1__["TimeoutError"]()), scheduler);
}
//# sourceMappingURL=timeout.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeoutWith.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeoutWith.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: timeoutWith */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "timeoutWith", function() { return timeoutWith; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _util_isDate__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/isDate */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isDate.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_scheduler_async,_util_isDate,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */





function timeoutWith(due, withObservable, scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_1__["async"];
    }
    return function (source) {
        var absoluteTimeout = Object(_util_isDate__WEBPACK_IMPORTED_MODULE_2__["isDate"])(due);
        var waitFor = absoluteTimeout ? (+due - scheduler.now()) : Math.abs(due);
        return source.lift(new TimeoutWithOperator(waitFor, absoluteTimeout, withObservable, scheduler));
    };
}
var TimeoutWithOperator = /*@__PURE__*/ (function () {
    function TimeoutWithOperator(waitFor, absoluteTimeout, withObservable, scheduler) {
        this.waitFor = waitFor;
        this.absoluteTimeout = absoluteTimeout;
        this.withObservable = withObservable;
        this.scheduler = scheduler;
    }
    TimeoutWithOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new TimeoutWithSubscriber(subscriber, this.absoluteTimeout, this.waitFor, this.withObservable, this.scheduler));
    };
    return TimeoutWithOperator;
}());
var TimeoutWithSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](TimeoutWithSubscriber, _super);
    function TimeoutWithSubscriber(destination, absoluteTimeout, waitFor, withObservable, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.absoluteTimeout = absoluteTimeout;
        _this.waitFor = waitFor;
        _this.withObservable = withObservable;
        _this.scheduler = scheduler;
        _this.action = null;
        _this.scheduleTimeout();
        return _this;
    }
    TimeoutWithSubscriber.dispatchTimeout = function (subscriber) {
        var withObservable = subscriber.withObservable;
        subscriber._unsubscribeAndRecycle();
        subscriber.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__["subscribeToResult"])(subscriber, withObservable));
    };
    TimeoutWithSubscriber.prototype.scheduleTimeout = function () {
        var action = this.action;
        if (action) {
            this.action = action.schedule(this, this.waitFor);
        }
        else {
            this.add(this.action = this.scheduler.schedule(TimeoutWithSubscriber.dispatchTimeout, this.waitFor, this));
        }
    };
    TimeoutWithSubscriber.prototype._next = function (value) {
        if (!this.absoluteTimeout) {
            this.scheduleTimeout();
        }
        _super.prototype._next.call(this, value);
    };
    TimeoutWithSubscriber.prototype._unsubscribe = function () {
        this.action = null;
        this.scheduler = null;
        this.withObservable = null;
    };
    return TimeoutWithSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__["OuterSubscriber"]));
//# sourceMappingURL=timeoutWith.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timestamp.js":
/*!***************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timestamp.js ***!
  \***************************************************************************************************************************************************************************************/
/*! exports provided: timestamp, Timestamp */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "timestamp", function() { return timestamp; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Timestamp", function() { return Timestamp; });
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _map__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./map */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js");
/** PURE_IMPORTS_START _scheduler_async,_map PURE_IMPORTS_END */


function timestamp(scheduler) {
    if (scheduler === void 0) {
        scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_0__["async"];
    }
    return Object(_map__WEBPACK_IMPORTED_MODULE_1__["map"])(function (value) { return new Timestamp(value, scheduler.now()); });
}
var Timestamp = /*@__PURE__*/ (function () {
    function Timestamp(value, timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    return Timestamp;
}());

//# sourceMappingURL=timestamp.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/toArray.js":
/*!********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/toArray.js from dll-reference vendor_chunk ***!
  \********************************************************************************************************************************************************************************************************************************/
/*! exports provided: toArray */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(415);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/window.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/window.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: window */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "window", function() { return window; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subject,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function window(windowBoundaries) {
    return function windowOperatorFunction(source) {
        return source.lift(new WindowOperator(windowBoundaries));
    };
}
var WindowOperator = /*@__PURE__*/ (function () {
    function WindowOperator(windowBoundaries) {
        this.windowBoundaries = windowBoundaries;
    }
    WindowOperator.prototype.call = function (subscriber, source) {
        var windowSubscriber = new WindowSubscriber(subscriber);
        var sourceSubscription = source.subscribe(windowSubscriber);
        if (!sourceSubscription.closed) {
            windowSubscriber.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(windowSubscriber, this.windowBoundaries));
        }
        return sourceSubscription;
    };
    return WindowOperator;
}());
var WindowSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WindowSubscriber, _super);
    function WindowSubscriber(destination) {
        var _this = _super.call(this, destination) || this;
        _this.window = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
        destination.next(_this.window);
        return _this;
    }
    WindowSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.openWindow();
    };
    WindowSubscriber.prototype.notifyError = function (error, innerSub) {
        this._error(error);
    };
    WindowSubscriber.prototype.notifyComplete = function (innerSub) {
        this._complete();
    };
    WindowSubscriber.prototype._next = function (value) {
        this.window.next(value);
    };
    WindowSubscriber.prototype._error = function (err) {
        this.window.error(err);
        this.destination.error(err);
    };
    WindowSubscriber.prototype._complete = function () {
        this.window.complete();
        this.destination.complete();
    };
    WindowSubscriber.prototype._unsubscribe = function () {
        this.window = null;
    };
    WindowSubscriber.prototype.openWindow = function () {
        var prevWindow = this.window;
        if (prevWindow) {
            prevWindow.complete();
        }
        var destination = this.destination;
        var newWindow = this.window = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
        destination.next(newWindow);
    };
    return WindowSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));
//# sourceMappingURL=window.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowCount.js":
/*!*****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowCount.js ***!
  \*****************************************************************************************************************************************************************************************/
/*! exports provided: windowCount */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "windowCount", function() { return windowCount; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/** PURE_IMPORTS_START tslib,_Subscriber,_Subject PURE_IMPORTS_END */



function windowCount(windowSize, startWindowEvery) {
    if (startWindowEvery === void 0) {
        startWindowEvery = 0;
    }
    return function windowCountOperatorFunction(source) {
        return source.lift(new WindowCountOperator(windowSize, startWindowEvery));
    };
}
var WindowCountOperator = /*@__PURE__*/ (function () {
    function WindowCountOperator(windowSize, startWindowEvery) {
        this.windowSize = windowSize;
        this.startWindowEvery = startWindowEvery;
    }
    WindowCountOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new WindowCountSubscriber(subscriber, this.windowSize, this.startWindowEvery));
    };
    return WindowCountOperator;
}());
var WindowCountSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WindowCountSubscriber, _super);
    function WindowCountSubscriber(destination, windowSize, startWindowEvery) {
        var _this = _super.call(this, destination) || this;
        _this.destination = destination;
        _this.windowSize = windowSize;
        _this.startWindowEvery = startWindowEvery;
        _this.windows = [new _Subject__WEBPACK_IMPORTED_MODULE_2__["Subject"]()];
        _this.count = 0;
        destination.next(_this.windows[0]);
        return _this;
    }
    WindowCountSubscriber.prototype._next = function (value) {
        var startWindowEvery = (this.startWindowEvery > 0) ? this.startWindowEvery : this.windowSize;
        var destination = this.destination;
        var windowSize = this.windowSize;
        var windows = this.windows;
        var len = windows.length;
        for (var i = 0; i < len && !this.closed; i++) {
            windows[i].next(value);
        }
        var c = this.count - windowSize + 1;
        if (c >= 0 && c % startWindowEvery === 0 && !this.closed) {
            windows.shift().complete();
        }
        if (++this.count % startWindowEvery === 0 && !this.closed) {
            var window_1 = new _Subject__WEBPACK_IMPORTED_MODULE_2__["Subject"]();
            windows.push(window_1);
            destination.next(window_1);
        }
    };
    WindowCountSubscriber.prototype._error = function (err) {
        var windows = this.windows;
        if (windows) {
            while (windows.length > 0 && !this.closed) {
                windows.shift().error(err);
            }
        }
        this.destination.error(err);
    };
    WindowCountSubscriber.prototype._complete = function () {
        var windows = this.windows;
        if (windows) {
            while (windows.length > 0 && !this.closed) {
                windows.shift().complete();
            }
        }
        this.destination.complete();
    };
    WindowCountSubscriber.prototype._unsubscribe = function () {
        this.count = 0;
        this.windows = null;
    };
    return WindowCountSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_1__["Subscriber"]));
//# sourceMappingURL=windowCount.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowTime.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowTime.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: windowTime */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "windowTime", function() { return windowTime; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _scheduler_async__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../scheduler/async */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js");
/* harmony import */ var _Subscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../Subscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscriber.js");
/* harmony import */ var _util_isNumeric__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../util/isNumeric */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isNumeric.js");
/* harmony import */ var _util_isScheduler__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../util/isScheduler */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isScheduler.js");
/** PURE_IMPORTS_START tslib,_Subject,_scheduler_async,_Subscriber,_util_isNumeric,_util_isScheduler PURE_IMPORTS_END */






function windowTime(windowTimeSpan) {
    var scheduler = _scheduler_async__WEBPACK_IMPORTED_MODULE_2__["async"];
    var windowCreationInterval = null;
    var maxWindowSize = Number.POSITIVE_INFINITY;
    if (Object(_util_isScheduler__WEBPACK_IMPORTED_MODULE_5__["isScheduler"])(arguments[3])) {
        scheduler = arguments[3];
    }
    if (Object(_util_isScheduler__WEBPACK_IMPORTED_MODULE_5__["isScheduler"])(arguments[2])) {
        scheduler = arguments[2];
    }
    else if (Object(_util_isNumeric__WEBPACK_IMPORTED_MODULE_4__["isNumeric"])(arguments[2])) {
        maxWindowSize = arguments[2];
    }
    if (Object(_util_isScheduler__WEBPACK_IMPORTED_MODULE_5__["isScheduler"])(arguments[1])) {
        scheduler = arguments[1];
    }
    else if (Object(_util_isNumeric__WEBPACK_IMPORTED_MODULE_4__["isNumeric"])(arguments[1])) {
        windowCreationInterval = arguments[1];
    }
    return function windowTimeOperatorFunction(source) {
        return source.lift(new WindowTimeOperator(windowTimeSpan, windowCreationInterval, maxWindowSize, scheduler));
    };
}
var WindowTimeOperator = /*@__PURE__*/ (function () {
    function WindowTimeOperator(windowTimeSpan, windowCreationInterval, maxWindowSize, scheduler) {
        this.windowTimeSpan = windowTimeSpan;
        this.windowCreationInterval = windowCreationInterval;
        this.maxWindowSize = maxWindowSize;
        this.scheduler = scheduler;
    }
    WindowTimeOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new WindowTimeSubscriber(subscriber, this.windowTimeSpan, this.windowCreationInterval, this.maxWindowSize, this.scheduler));
    };
    return WindowTimeOperator;
}());
var CountedSubject = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](CountedSubject, _super);
    function CountedSubject() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this._numberOfNextedValues = 0;
        return _this;
    }
    CountedSubject.prototype.next = function (value) {
        this._numberOfNextedValues++;
        _super.prototype.next.call(this, value);
    };
    Object.defineProperty(CountedSubject.prototype, "numberOfNextedValues", {
        get: function () {
            return this._numberOfNextedValues;
        },
        enumerable: true,
        configurable: true
    });
    return CountedSubject;
}(_Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]));
var WindowTimeSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WindowTimeSubscriber, _super);
    function WindowTimeSubscriber(destination, windowTimeSpan, windowCreationInterval, maxWindowSize, scheduler) {
        var _this = _super.call(this, destination) || this;
        _this.destination = destination;
        _this.windowTimeSpan = windowTimeSpan;
        _this.windowCreationInterval = windowCreationInterval;
        _this.maxWindowSize = maxWindowSize;
        _this.scheduler = scheduler;
        _this.windows = [];
        var window = _this.openWindow();
        if (windowCreationInterval !== null && windowCreationInterval >= 0) {
            var closeState = { subscriber: _this, window: window, context: null };
            var creationState = { windowTimeSpan: windowTimeSpan, windowCreationInterval: windowCreationInterval, subscriber: _this, scheduler: scheduler };
            _this.add(scheduler.schedule(dispatchWindowClose, windowTimeSpan, closeState));
            _this.add(scheduler.schedule(dispatchWindowCreation, windowCreationInterval, creationState));
        }
        else {
            var timeSpanOnlyState = { subscriber: _this, window: window, windowTimeSpan: windowTimeSpan };
            _this.add(scheduler.schedule(dispatchWindowTimeSpanOnly, windowTimeSpan, timeSpanOnlyState));
        }
        return _this;
    }
    WindowTimeSubscriber.prototype._next = function (value) {
        var windows = this.windows;
        var len = windows.length;
        for (var i = 0; i < len; i++) {
            var window_1 = windows[i];
            if (!window_1.closed) {
                window_1.next(value);
                if (window_1.numberOfNextedValues >= this.maxWindowSize) {
                    this.closeWindow(window_1);
                }
            }
        }
    };
    WindowTimeSubscriber.prototype._error = function (err) {
        var windows = this.windows;
        while (windows.length > 0) {
            windows.shift().error(err);
        }
        this.destination.error(err);
    };
    WindowTimeSubscriber.prototype._complete = function () {
        var windows = this.windows;
        while (windows.length > 0) {
            var window_2 = windows.shift();
            if (!window_2.closed) {
                window_2.complete();
            }
        }
        this.destination.complete();
    };
    WindowTimeSubscriber.prototype.openWindow = function () {
        var window = new CountedSubject();
        this.windows.push(window);
        var destination = this.destination;
        destination.next(window);
        return window;
    };
    WindowTimeSubscriber.prototype.closeWindow = function (window) {
        window.complete();
        var windows = this.windows;
        windows.splice(windows.indexOf(window), 1);
    };
    return WindowTimeSubscriber;
}(_Subscriber__WEBPACK_IMPORTED_MODULE_3__["Subscriber"]));
function dispatchWindowTimeSpanOnly(state) {
    var subscriber = state.subscriber, windowTimeSpan = state.windowTimeSpan, window = state.window;
    if (window) {
        subscriber.closeWindow(window);
    }
    state.window = subscriber.openWindow();
    this.schedule(state, windowTimeSpan);
}
function dispatchWindowCreation(state) {
    var windowTimeSpan = state.windowTimeSpan, subscriber = state.subscriber, scheduler = state.scheduler, windowCreationInterval = state.windowCreationInterval;
    var window = subscriber.openWindow();
    var action = this;
    var context = { action: action, subscription: null };
    var timeSpanState = { subscriber: subscriber, window: window, context: context };
    context.subscription = scheduler.schedule(dispatchWindowClose, windowTimeSpan, timeSpanState);
    action.add(context.subscription);
    action.schedule(state, windowCreationInterval);
}
function dispatchWindowClose(state) {
    var subscriber = state.subscriber, window = state.window, context = state.context;
    if (context && context.action && context.subscription) {
        context.action.remove(context.subscription);
    }
    subscriber.closeWindow(window);
}
//# sourceMappingURL=windowTime.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowToggle.js":
/*!******************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowToggle.js ***!
  \******************************************************************************************************************************************************************************************/
/*! exports provided: windowToggle */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "windowToggle", function() { return windowToggle; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _Subscription__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../Subscription */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subscription.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subject,_Subscription,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */





function windowToggle(openings, closingSelector) {
    return function (source) { return source.lift(new WindowToggleOperator(openings, closingSelector)); };
}
var WindowToggleOperator = /*@__PURE__*/ (function () {
    function WindowToggleOperator(openings, closingSelector) {
        this.openings = openings;
        this.closingSelector = closingSelector;
    }
    WindowToggleOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new WindowToggleSubscriber(subscriber, this.openings, this.closingSelector));
    };
    return WindowToggleOperator;
}());
var WindowToggleSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WindowToggleSubscriber, _super);
    function WindowToggleSubscriber(destination, openings, closingSelector) {
        var _this = _super.call(this, destination) || this;
        _this.openings = openings;
        _this.closingSelector = closingSelector;
        _this.contexts = [];
        _this.add(_this.openSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__["subscribeToResult"])(_this, openings, openings));
        return _this;
    }
    WindowToggleSubscriber.prototype._next = function (value) {
        var contexts = this.contexts;
        if (contexts) {
            var len = contexts.length;
            for (var i = 0; i < len; i++) {
                contexts[i].window.next(value);
            }
        }
    };
    WindowToggleSubscriber.prototype._error = function (err) {
        var contexts = this.contexts;
        this.contexts = null;
        if (contexts) {
            var len = contexts.length;
            var index = -1;
            while (++index < len) {
                var context_1 = contexts[index];
                context_1.window.error(err);
                context_1.subscription.unsubscribe();
            }
        }
        _super.prototype._error.call(this, err);
    };
    WindowToggleSubscriber.prototype._complete = function () {
        var contexts = this.contexts;
        this.contexts = null;
        if (contexts) {
            var len = contexts.length;
            var index = -1;
            while (++index < len) {
                var context_2 = contexts[index];
                context_2.window.complete();
                context_2.subscription.unsubscribe();
            }
        }
        _super.prototype._complete.call(this);
    };
    WindowToggleSubscriber.prototype._unsubscribe = function () {
        var contexts = this.contexts;
        this.contexts = null;
        if (contexts) {
            var len = contexts.length;
            var index = -1;
            while (++index < len) {
                var context_3 = contexts[index];
                context_3.window.unsubscribe();
                context_3.subscription.unsubscribe();
            }
        }
    };
    WindowToggleSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        if (outerValue === this.openings) {
            var closingNotifier = void 0;
            try {
                var closingSelector = this.closingSelector;
                closingNotifier = closingSelector(innerValue);
            }
            catch (e) {
                return this.error(e);
            }
            var window_1 = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
            var subscription = new _Subscription__WEBPACK_IMPORTED_MODULE_2__["Subscription"]();
            var context_4 = { window: window_1, subscription: subscription };
            this.contexts.push(context_4);
            var innerSubscription = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_4__["subscribeToResult"])(this, closingNotifier, context_4);
            if (innerSubscription.closed) {
                this.closeWindow(this.contexts.length - 1);
            }
            else {
                innerSubscription.context = context_4;
                subscription.add(innerSubscription);
            }
            this.destination.next(window_1);
        }
        else {
            this.closeWindow(this.contexts.indexOf(outerValue));
        }
    };
    WindowToggleSubscriber.prototype.notifyError = function (err) {
        this.error(err);
    };
    WindowToggleSubscriber.prototype.notifyComplete = function (inner) {
        if (inner !== this.openSubscription) {
            this.closeWindow(this.contexts.indexOf(inner.context));
        }
    };
    WindowToggleSubscriber.prototype.closeWindow = function (index) {
        if (index === -1) {
            return;
        }
        var contexts = this.contexts;
        var context = contexts[index];
        var window = context.window, subscription = context.subscription;
        contexts.splice(index, 1);
        window.complete();
        subscription.unsubscribe();
    };
    return WindowToggleSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_3__["OuterSubscriber"]));
//# sourceMappingURL=windowToggle.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowWhen.js":
/*!****************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowWhen.js ***!
  \****************************************************************************************************************************************************************************************/
/*! exports provided: windowWhen */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "windowWhen", function() { return windowWhen; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _Subject__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Subject */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/Subject.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_Subject,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */




function windowWhen(closingSelector) {
    return function windowWhenOperatorFunction(source) {
        return source.lift(new WindowOperator(closingSelector));
    };
}
var WindowOperator = /*@__PURE__*/ (function () {
    function WindowOperator(closingSelector) {
        this.closingSelector = closingSelector;
    }
    WindowOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new WindowSubscriber(subscriber, this.closingSelector));
    };
    return WindowOperator;
}());
var WindowSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WindowSubscriber, _super);
    function WindowSubscriber(destination, closingSelector) {
        var _this = _super.call(this, destination) || this;
        _this.destination = destination;
        _this.closingSelector = closingSelector;
        _this.openWindow();
        return _this;
    }
    WindowSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.openWindow(innerSub);
    };
    WindowSubscriber.prototype.notifyError = function (error, innerSub) {
        this._error(error);
    };
    WindowSubscriber.prototype.notifyComplete = function (innerSub) {
        this.openWindow(innerSub);
    };
    WindowSubscriber.prototype._next = function (value) {
        this.window.next(value);
    };
    WindowSubscriber.prototype._error = function (err) {
        this.window.error(err);
        this.destination.error(err);
        this.unsubscribeClosingNotification();
    };
    WindowSubscriber.prototype._complete = function () {
        this.window.complete();
        this.destination.complete();
        this.unsubscribeClosingNotification();
    };
    WindowSubscriber.prototype.unsubscribeClosingNotification = function () {
        if (this.closingNotification) {
            this.closingNotification.unsubscribe();
        }
    };
    WindowSubscriber.prototype.openWindow = function (innerSub) {
        if (innerSub === void 0) {
            innerSub = null;
        }
        if (innerSub) {
            this.remove(innerSub);
            innerSub.unsubscribe();
        }
        var prevWindow = this.window;
        if (prevWindow) {
            prevWindow.complete();
        }
        var window = this.window = new _Subject__WEBPACK_IMPORTED_MODULE_1__["Subject"]();
        this.destination.next(window);
        var closingNotifier;
        try {
            var closingSelector = this.closingSelector;
            closingNotifier = closingSelector();
        }
        catch (e) {
            this.destination.error(e);
            this.window.error(e);
            return;
        }
        this.add(this.closingNotification = Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_3__["subscribeToResult"])(this, closingNotifier));
    };
    return WindowSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_2__["OuterSubscriber"]));
//# sourceMappingURL=windowWhen.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/withLatestFrom.js":
/*!********************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/withLatestFrom.js ***!
  \********************************************************************************************************************************************************************************************/
/*! exports provided: withLatestFrom */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "withLatestFrom", function() { return withLatestFrom; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../OuterSubscriber */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/OuterSubscriber.js");
/* harmony import */ var _util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../util/subscribeToResult */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js");
/** PURE_IMPORTS_START tslib,_OuterSubscriber,_util_subscribeToResult PURE_IMPORTS_END */



function withLatestFrom() {
    var args = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        args[_i] = arguments[_i];
    }
    return function (source) {
        var project;
        if (typeof args[args.length - 1] === 'function') {
            project = args.pop();
        }
        var observables = args;
        return source.lift(new WithLatestFromOperator(observables, project));
    };
}
var WithLatestFromOperator = /*@__PURE__*/ (function () {
    function WithLatestFromOperator(observables, project) {
        this.observables = observables;
        this.project = project;
    }
    WithLatestFromOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new WithLatestFromSubscriber(subscriber, this.observables, this.project));
    };
    return WithLatestFromOperator;
}());
var WithLatestFromSubscriber = /*@__PURE__*/ (function (_super) {
    tslib__WEBPACK_IMPORTED_MODULE_0__["__extends"](WithLatestFromSubscriber, _super);
    function WithLatestFromSubscriber(destination, observables, project) {
        var _this = _super.call(this, destination) || this;
        _this.observables = observables;
        _this.project = project;
        _this.toRespond = [];
        var len = observables.length;
        _this.values = new Array(len);
        for (var i = 0; i < len; i++) {
            _this.toRespond.push(i);
        }
        for (var i = 0; i < len; i++) {
            var observable = observables[i];
            _this.add(Object(_util_subscribeToResult__WEBPACK_IMPORTED_MODULE_2__["subscribeToResult"])(_this, observable, observable, i));
        }
        return _this;
    }
    WithLatestFromSubscriber.prototype.notifyNext = function (outerValue, innerValue, outerIndex, innerIndex, innerSub) {
        this.values[outerIndex] = innerValue;
        var toRespond = this.toRespond;
        if (toRespond.length > 0) {
            var found = toRespond.indexOf(outerIndex);
            if (found !== -1) {
                toRespond.splice(found, 1);
            }
        }
    };
    WithLatestFromSubscriber.prototype.notifyComplete = function () {
    };
    WithLatestFromSubscriber.prototype._next = function (value) {
        if (this.toRespond.length === 0) {
            var args = [value].concat(this.values);
            if (this.project) {
                this._tryProject(args);
            }
            else {
                this.destination.next(args);
            }
        }
    };
    WithLatestFromSubscriber.prototype._tryProject = function (args) {
        var result;
        try {
            result = this.project.apply(this, args);
        }
        catch (err) {
            this.destination.error(err);
            return;
        }
        this.destination.next(result);
    };
    return WithLatestFromSubscriber;
}(_OuterSubscriber__WEBPACK_IMPORTED_MODULE_1__["OuterSubscriber"]));
//# sourceMappingURL=withLatestFrom.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zip.js":
/*!*********************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zip.js ***!
  \*********************************************************************************************************************************************************************************/
/*! exports provided: zip */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "zip", function() { return zip; });
/* harmony import */ var _observable_zip__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/zip */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/zip.js");
/** PURE_IMPORTS_START _observable_zip PURE_IMPORTS_END */

function zip() {
    var observables = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        observables[_i] = arguments[_i];
    }
    return function zipOperatorFunction(source) {
        return source.lift.call(_observable_zip__WEBPACK_IMPORTED_MODULE_0__["zip"].apply(void 0, [source].concat(observables)));
    };
}
//# sourceMappingURL=zip.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zipAll.js":
/*!************************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zipAll.js ***!
  \************************************************************************************************************************************************************************************/
/*! exports provided: zipAll */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "zipAll", function() { return zipAll; });
/* harmony import */ var _observable_zip__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../observable/zip */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/observable/zip.js");
/** PURE_IMPORTS_START _observable_zip PURE_IMPORTS_END */

function zipAll(project) {
    return function (source) { return source.lift(new _observable_zip__WEBPACK_IMPORTED_MODULE_0__["ZipOperator"](project)); };
}
//# sourceMappingURL=zipAll.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/asap.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/asap.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: asap */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(170);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/scheduler/async.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: async */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(74);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/ArgumentOutOfRangeError.js":
/*!*******************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/ArgumentOutOfRangeError.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************************/
/*! exports provided: ArgumentOutOfRangeError */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(90);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/EmptyError.js":
/*!******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/EmptyError.js from dll-reference vendor_chunk ***!
  \******************************************************************************************************************************************************************************************************************************/
/*! exports provided: EmptyError */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(56);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/TimeoutError.js":
/*!********************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/TimeoutError.js from dll-reference vendor_chunk ***!
  \********************************************************************************************************************************************************************************************************************************/
/*! exports provided: TimeoutError */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(173);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/identity.js":
/*!****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/identity.js from dll-reference vendor_chunk ***!
  \****************************************************************************************************************************************************************************************************************************/
/*! exports provided: identity */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(43);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isArray.js":
/*!***************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isArray.js from dll-reference vendor_chunk ***!
  \***************************************************************************************************************************************************************************************************************************/
/*! exports provided: isArray */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(19);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isDate.js":
/*!*******************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isDate.js ***!
  \*******************************************************************************************************************************************************************************/
/*! exports provided: isDate */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "isDate", function() { return isDate; });
/** PURE_IMPORTS_START  PURE_IMPORTS_END */
function isDate(value) {
    return value instanceof Date && !isNaN(+value);
}
//# sourceMappingURL=isDate.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isNumeric.js":
/*!*****************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isNumeric.js from dll-reference vendor_chunk ***!
  \*****************************************************************************************************************************************************************************************************************************/
/*! exports provided: isNumeric */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(108);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isScheduler.js":
/*!*******************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/isScheduler.js from dll-reference vendor_chunk ***!
  \*******************************************************************************************************************************************************************************************************************************/
/*! exports provided: isScheduler */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(24);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/not.js":
/*!****************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/not.js ***!
  \****************************************************************************************************************************************************************************/
/*! exports provided: not */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "not", function() { return not; });
/** PURE_IMPORTS_START  PURE_IMPORTS_END */
function not(pred, thisArg) {
    function notPred() {
        return !(notPred.pred.apply(notPred.thisArg, arguments));
    }
    notPred.pred = pred;
    notPred.thisArg = thisArg;
    return notPred;
}
//# sourceMappingURL=not.js.map


/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js":
/*!*************************************************************************************************************************************************************************************************************************************!*\
  !*** delegated ./opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/util/subscribeToResult.js from dll-reference vendor_chunk ***!
  \*************************************************************************************************************************************************************************************************************************************/
/*! exports provided: subscribeToResult */
/***/ (function(module, exports, __webpack_require__) {

module.exports = (__webpack_require__(/*! dll-reference vendor_chunk */ "dll-reference vendor_chunk"))(30);

/***/ }),

/***/ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/operators/index.js":
/*!**************************************************************************************************************************************************************************!*\
  !*** /opt/bambooagent/xml-data/build-dir/TEST-GROOT5794-JOB1/sources/modules/npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/operators/index.js ***!
  \**************************************************************************************************************************************************************************/
/*! exports provided: audit, auditTime, buffer, bufferCount, bufferTime, bufferToggle, bufferWhen, catchError, combineAll, combineLatest, concat, concatAll, concatMap, concatMapTo, count, debounce, debounceTime, defaultIfEmpty, delay, delayWhen, dematerialize, distinct, distinctUntilChanged, distinctUntilKeyChanged, elementAt, endWith, every, exhaust, exhaustMap, expand, filter, finalize, find, findIndex, first, groupBy, ignoreElements, isEmpty, last, map, mapTo, materialize, max, merge, mergeAll, mergeMap, flatMap, mergeMapTo, mergeScan, min, multicast, observeOn, onErrorResumeNext, pairwise, partition, pluck, publish, publishBehavior, publishLast, publishReplay, race, reduce, repeat, repeatWhen, retry, retryWhen, refCount, sample, sampleTime, scan, sequenceEqual, share, shareReplay, single, skip, skipLast, skipUntil, skipWhile, startWith, subscribeOn, switchAll, switchMap, switchMapTo, take, takeLast, takeUntil, takeWhile, tap, throttle, throttleTime, throwIfEmpty, timeInterval, timeout, timeoutWith, timestamp, toArray, window, windowCount, windowTime, windowToggle, windowWhen, withLatestFrom, zip, zipAll */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _internal_operators_audit__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../internal/operators/audit */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/audit.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "audit", function() { return _internal_operators_audit__WEBPACK_IMPORTED_MODULE_0__["audit"]; });

/* harmony import */ var _internal_operators_auditTime__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../internal/operators/auditTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/auditTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "auditTime", function() { return _internal_operators_auditTime__WEBPACK_IMPORTED_MODULE_1__["auditTime"]; });

/* harmony import */ var _internal_operators_buffer__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../internal/operators/buffer */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/buffer.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "buffer", function() { return _internal_operators_buffer__WEBPACK_IMPORTED_MODULE_2__["buffer"]; });

/* harmony import */ var _internal_operators_bufferCount__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../internal/operators/bufferCount */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferCount.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "bufferCount", function() { return _internal_operators_bufferCount__WEBPACK_IMPORTED_MODULE_3__["bufferCount"]; });

/* harmony import */ var _internal_operators_bufferTime__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../internal/operators/bufferTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "bufferTime", function() { return _internal_operators_bufferTime__WEBPACK_IMPORTED_MODULE_4__["bufferTime"]; });

/* harmony import */ var _internal_operators_bufferToggle__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../internal/operators/bufferToggle */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferToggle.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "bufferToggle", function() { return _internal_operators_bufferToggle__WEBPACK_IMPORTED_MODULE_5__["bufferToggle"]; });

/* harmony import */ var _internal_operators_bufferWhen__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../internal/operators/bufferWhen */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/bufferWhen.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "bufferWhen", function() { return _internal_operators_bufferWhen__WEBPACK_IMPORTED_MODULE_6__["bufferWhen"]; });

/* harmony import */ var _internal_operators_catchError__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ../internal/operators/catchError */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/catchError.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "catchError", function() { return _internal_operators_catchError__WEBPACK_IMPORTED_MODULE_7__["catchError"]; });

/* harmony import */ var _internal_operators_combineAll__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ../internal/operators/combineAll */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineAll.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "combineAll", function() { return _internal_operators_combineAll__WEBPACK_IMPORTED_MODULE_8__["combineAll"]; });

/* harmony import */ var _internal_operators_combineLatest__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ../internal/operators/combineLatest */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/combineLatest.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "combineLatest", function() { return _internal_operators_combineLatest__WEBPACK_IMPORTED_MODULE_9__["combineLatest"]; });

/* harmony import */ var _internal_operators_concat__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ../internal/operators/concat */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concat.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "concat", function() { return _internal_operators_concat__WEBPACK_IMPORTED_MODULE_10__["concat"]; });

/* harmony import */ var _internal_operators_concatAll__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! ../internal/operators/concatAll */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatAll.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "concatAll", function() { return _internal_operators_concatAll__WEBPACK_IMPORTED_MODULE_11__["concatAll"]; });

/* harmony import */ var _internal_operators_concatMap__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ../internal/operators/concatMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMap.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "concatMap", function() { return _internal_operators_concatMap__WEBPACK_IMPORTED_MODULE_12__["concatMap"]; });

/* harmony import */ var _internal_operators_concatMapTo__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! ../internal/operators/concatMapTo */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/concatMapTo.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "concatMapTo", function() { return _internal_operators_concatMapTo__WEBPACK_IMPORTED_MODULE_13__["concatMapTo"]; });

/* harmony import */ var _internal_operators_count__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(/*! ../internal/operators/count */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/count.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "count", function() { return _internal_operators_count__WEBPACK_IMPORTED_MODULE_14__["count"]; });

/* harmony import */ var _internal_operators_debounce__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(/*! ../internal/operators/debounce */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounce.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "debounce", function() { return _internal_operators_debounce__WEBPACK_IMPORTED_MODULE_15__["debounce"]; });

/* harmony import */ var _internal_operators_debounceTime__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(/*! ../internal/operators/debounceTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/debounceTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "debounceTime", function() { return _internal_operators_debounceTime__WEBPACK_IMPORTED_MODULE_16__["debounceTime"]; });

/* harmony import */ var _internal_operators_defaultIfEmpty__WEBPACK_IMPORTED_MODULE_17__ = __webpack_require__(/*! ../internal/operators/defaultIfEmpty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/defaultIfEmpty.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "defaultIfEmpty", function() { return _internal_operators_defaultIfEmpty__WEBPACK_IMPORTED_MODULE_17__["defaultIfEmpty"]; });

/* harmony import */ var _internal_operators_delay__WEBPACK_IMPORTED_MODULE_18__ = __webpack_require__(/*! ../internal/operators/delay */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delay.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "delay", function() { return _internal_operators_delay__WEBPACK_IMPORTED_MODULE_18__["delay"]; });

/* harmony import */ var _internal_operators_delayWhen__WEBPACK_IMPORTED_MODULE_19__ = __webpack_require__(/*! ../internal/operators/delayWhen */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/delayWhen.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "delayWhen", function() { return _internal_operators_delayWhen__WEBPACK_IMPORTED_MODULE_19__["delayWhen"]; });

/* harmony import */ var _internal_operators_dematerialize__WEBPACK_IMPORTED_MODULE_20__ = __webpack_require__(/*! ../internal/operators/dematerialize */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/dematerialize.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "dematerialize", function() { return _internal_operators_dematerialize__WEBPACK_IMPORTED_MODULE_20__["dematerialize"]; });

/* harmony import */ var _internal_operators_distinct__WEBPACK_IMPORTED_MODULE_21__ = __webpack_require__(/*! ../internal/operators/distinct */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinct.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "distinct", function() { return _internal_operators_distinct__WEBPACK_IMPORTED_MODULE_21__["distinct"]; });

/* harmony import */ var _internal_operators_distinctUntilChanged__WEBPACK_IMPORTED_MODULE_22__ = __webpack_require__(/*! ../internal/operators/distinctUntilChanged */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilChanged.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "distinctUntilChanged", function() { return _internal_operators_distinctUntilChanged__WEBPACK_IMPORTED_MODULE_22__["distinctUntilChanged"]; });

/* harmony import */ var _internal_operators_distinctUntilKeyChanged__WEBPACK_IMPORTED_MODULE_23__ = __webpack_require__(/*! ../internal/operators/distinctUntilKeyChanged */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/distinctUntilKeyChanged.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "distinctUntilKeyChanged", function() { return _internal_operators_distinctUntilKeyChanged__WEBPACK_IMPORTED_MODULE_23__["distinctUntilKeyChanged"]; });

/* harmony import */ var _internal_operators_elementAt__WEBPACK_IMPORTED_MODULE_24__ = __webpack_require__(/*! ../internal/operators/elementAt */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/elementAt.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "elementAt", function() { return _internal_operators_elementAt__WEBPACK_IMPORTED_MODULE_24__["elementAt"]; });

/* harmony import */ var _internal_operators_endWith__WEBPACK_IMPORTED_MODULE_25__ = __webpack_require__(/*! ../internal/operators/endWith */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/endWith.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "endWith", function() { return _internal_operators_endWith__WEBPACK_IMPORTED_MODULE_25__["endWith"]; });

/* harmony import */ var _internal_operators_every__WEBPACK_IMPORTED_MODULE_26__ = __webpack_require__(/*! ../internal/operators/every */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/every.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "every", function() { return _internal_operators_every__WEBPACK_IMPORTED_MODULE_26__["every"]; });

/* harmony import */ var _internal_operators_exhaust__WEBPACK_IMPORTED_MODULE_27__ = __webpack_require__(/*! ../internal/operators/exhaust */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaust.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "exhaust", function() { return _internal_operators_exhaust__WEBPACK_IMPORTED_MODULE_27__["exhaust"]; });

/* harmony import */ var _internal_operators_exhaustMap__WEBPACK_IMPORTED_MODULE_28__ = __webpack_require__(/*! ../internal/operators/exhaustMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/exhaustMap.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "exhaustMap", function() { return _internal_operators_exhaustMap__WEBPACK_IMPORTED_MODULE_28__["exhaustMap"]; });

/* harmony import */ var _internal_operators_expand__WEBPACK_IMPORTED_MODULE_29__ = __webpack_require__(/*! ../internal/operators/expand */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/expand.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "expand", function() { return _internal_operators_expand__WEBPACK_IMPORTED_MODULE_29__["expand"]; });

/* harmony import */ var _internal_operators_filter__WEBPACK_IMPORTED_MODULE_30__ = __webpack_require__(/*! ../internal/operators/filter */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/filter.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "filter", function() { return _internal_operators_filter__WEBPACK_IMPORTED_MODULE_30__["filter"]; });

/* harmony import */ var _internal_operators_finalize__WEBPACK_IMPORTED_MODULE_31__ = __webpack_require__(/*! ../internal/operators/finalize */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/finalize.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "finalize", function() { return _internal_operators_finalize__WEBPACK_IMPORTED_MODULE_31__["finalize"]; });

/* harmony import */ var _internal_operators_find__WEBPACK_IMPORTED_MODULE_32__ = __webpack_require__(/*! ../internal/operators/find */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/find.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "find", function() { return _internal_operators_find__WEBPACK_IMPORTED_MODULE_32__["find"]; });

/* harmony import */ var _internal_operators_findIndex__WEBPACK_IMPORTED_MODULE_33__ = __webpack_require__(/*! ../internal/operators/findIndex */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/findIndex.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "findIndex", function() { return _internal_operators_findIndex__WEBPACK_IMPORTED_MODULE_33__["findIndex"]; });

/* harmony import */ var _internal_operators_first__WEBPACK_IMPORTED_MODULE_34__ = __webpack_require__(/*! ../internal/operators/first */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/first.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "first", function() { return _internal_operators_first__WEBPACK_IMPORTED_MODULE_34__["first"]; });

/* harmony import */ var _internal_operators_groupBy__WEBPACK_IMPORTED_MODULE_35__ = __webpack_require__(/*! ../internal/operators/groupBy */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/groupBy.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "groupBy", function() { return _internal_operators_groupBy__WEBPACK_IMPORTED_MODULE_35__["groupBy"]; });

/* harmony import */ var _internal_operators_ignoreElements__WEBPACK_IMPORTED_MODULE_36__ = __webpack_require__(/*! ../internal/operators/ignoreElements */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/ignoreElements.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "ignoreElements", function() { return _internal_operators_ignoreElements__WEBPACK_IMPORTED_MODULE_36__["ignoreElements"]; });

/* harmony import */ var _internal_operators_isEmpty__WEBPACK_IMPORTED_MODULE_37__ = __webpack_require__(/*! ../internal/operators/isEmpty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/isEmpty.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "isEmpty", function() { return _internal_operators_isEmpty__WEBPACK_IMPORTED_MODULE_37__["isEmpty"]; });

/* harmony import */ var _internal_operators_last__WEBPACK_IMPORTED_MODULE_38__ = __webpack_require__(/*! ../internal/operators/last */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/last.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "last", function() { return _internal_operators_last__WEBPACK_IMPORTED_MODULE_38__["last"]; });

/* harmony import */ var _internal_operators_map__WEBPACK_IMPORTED_MODULE_39__ = __webpack_require__(/*! ../internal/operators/map */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/map.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "map", function() { return _internal_operators_map__WEBPACK_IMPORTED_MODULE_39__["map"]; });

/* harmony import */ var _internal_operators_mapTo__WEBPACK_IMPORTED_MODULE_40__ = __webpack_require__(/*! ../internal/operators/mapTo */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mapTo.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "mapTo", function() { return _internal_operators_mapTo__WEBPACK_IMPORTED_MODULE_40__["mapTo"]; });

/* harmony import */ var _internal_operators_materialize__WEBPACK_IMPORTED_MODULE_41__ = __webpack_require__(/*! ../internal/operators/materialize */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/materialize.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "materialize", function() { return _internal_operators_materialize__WEBPACK_IMPORTED_MODULE_41__["materialize"]; });

/* harmony import */ var _internal_operators_max__WEBPACK_IMPORTED_MODULE_42__ = __webpack_require__(/*! ../internal/operators/max */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/max.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "max", function() { return _internal_operators_max__WEBPACK_IMPORTED_MODULE_42__["max"]; });

/* harmony import */ var _internal_operators_merge__WEBPACK_IMPORTED_MODULE_43__ = __webpack_require__(/*! ../internal/operators/merge */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/merge.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "merge", function() { return _internal_operators_merge__WEBPACK_IMPORTED_MODULE_43__["merge"]; });

/* harmony import */ var _internal_operators_mergeAll__WEBPACK_IMPORTED_MODULE_44__ = __webpack_require__(/*! ../internal/operators/mergeAll */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeAll.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "mergeAll", function() { return _internal_operators_mergeAll__WEBPACK_IMPORTED_MODULE_44__["mergeAll"]; });

/* harmony import */ var _internal_operators_mergeMap__WEBPACK_IMPORTED_MODULE_45__ = __webpack_require__(/*! ../internal/operators/mergeMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMap.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "mergeMap", function() { return _internal_operators_mergeMap__WEBPACK_IMPORTED_MODULE_45__["mergeMap"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "flatMap", function() { return _internal_operators_mergeMap__WEBPACK_IMPORTED_MODULE_45__["mergeMap"]; });

/* harmony import */ var _internal_operators_mergeMapTo__WEBPACK_IMPORTED_MODULE_46__ = __webpack_require__(/*! ../internal/operators/mergeMapTo */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeMapTo.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "mergeMapTo", function() { return _internal_operators_mergeMapTo__WEBPACK_IMPORTED_MODULE_46__["mergeMapTo"]; });

/* harmony import */ var _internal_operators_mergeScan__WEBPACK_IMPORTED_MODULE_47__ = __webpack_require__(/*! ../internal/operators/mergeScan */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/mergeScan.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "mergeScan", function() { return _internal_operators_mergeScan__WEBPACK_IMPORTED_MODULE_47__["mergeScan"]; });

/* harmony import */ var _internal_operators_min__WEBPACK_IMPORTED_MODULE_48__ = __webpack_require__(/*! ../internal/operators/min */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/min.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "min", function() { return _internal_operators_min__WEBPACK_IMPORTED_MODULE_48__["min"]; });

/* harmony import */ var _internal_operators_multicast__WEBPACK_IMPORTED_MODULE_49__ = __webpack_require__(/*! ../internal/operators/multicast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/multicast.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "multicast", function() { return _internal_operators_multicast__WEBPACK_IMPORTED_MODULE_49__["multicast"]; });

/* harmony import */ var _internal_operators_observeOn__WEBPACK_IMPORTED_MODULE_50__ = __webpack_require__(/*! ../internal/operators/observeOn */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/observeOn.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "observeOn", function() { return _internal_operators_observeOn__WEBPACK_IMPORTED_MODULE_50__["observeOn"]; });

/* harmony import */ var _internal_operators_onErrorResumeNext__WEBPACK_IMPORTED_MODULE_51__ = __webpack_require__(/*! ../internal/operators/onErrorResumeNext */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/onErrorResumeNext.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "onErrorResumeNext", function() { return _internal_operators_onErrorResumeNext__WEBPACK_IMPORTED_MODULE_51__["onErrorResumeNext"]; });

/* harmony import */ var _internal_operators_pairwise__WEBPACK_IMPORTED_MODULE_52__ = __webpack_require__(/*! ../internal/operators/pairwise */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pairwise.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "pairwise", function() { return _internal_operators_pairwise__WEBPACK_IMPORTED_MODULE_52__["pairwise"]; });

/* harmony import */ var _internal_operators_partition__WEBPACK_IMPORTED_MODULE_53__ = __webpack_require__(/*! ../internal/operators/partition */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/partition.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "partition", function() { return _internal_operators_partition__WEBPACK_IMPORTED_MODULE_53__["partition"]; });

/* harmony import */ var _internal_operators_pluck__WEBPACK_IMPORTED_MODULE_54__ = __webpack_require__(/*! ../internal/operators/pluck */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/pluck.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "pluck", function() { return _internal_operators_pluck__WEBPACK_IMPORTED_MODULE_54__["pluck"]; });

/* harmony import */ var _internal_operators_publish__WEBPACK_IMPORTED_MODULE_55__ = __webpack_require__(/*! ../internal/operators/publish */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publish.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "publish", function() { return _internal_operators_publish__WEBPACK_IMPORTED_MODULE_55__["publish"]; });

/* harmony import */ var _internal_operators_publishBehavior__WEBPACK_IMPORTED_MODULE_56__ = __webpack_require__(/*! ../internal/operators/publishBehavior */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishBehavior.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "publishBehavior", function() { return _internal_operators_publishBehavior__WEBPACK_IMPORTED_MODULE_56__["publishBehavior"]; });

/* harmony import */ var _internal_operators_publishLast__WEBPACK_IMPORTED_MODULE_57__ = __webpack_require__(/*! ../internal/operators/publishLast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishLast.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "publishLast", function() { return _internal_operators_publishLast__WEBPACK_IMPORTED_MODULE_57__["publishLast"]; });

/* harmony import */ var _internal_operators_publishReplay__WEBPACK_IMPORTED_MODULE_58__ = __webpack_require__(/*! ../internal/operators/publishReplay */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/publishReplay.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "publishReplay", function() { return _internal_operators_publishReplay__WEBPACK_IMPORTED_MODULE_58__["publishReplay"]; });

/* harmony import */ var _internal_operators_race__WEBPACK_IMPORTED_MODULE_59__ = __webpack_require__(/*! ../internal/operators/race */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/race.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "race", function() { return _internal_operators_race__WEBPACK_IMPORTED_MODULE_59__["race"]; });

/* harmony import */ var _internal_operators_reduce__WEBPACK_IMPORTED_MODULE_60__ = __webpack_require__(/*! ../internal/operators/reduce */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/reduce.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "reduce", function() { return _internal_operators_reduce__WEBPACK_IMPORTED_MODULE_60__["reduce"]; });

/* harmony import */ var _internal_operators_repeat__WEBPACK_IMPORTED_MODULE_61__ = __webpack_require__(/*! ../internal/operators/repeat */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeat.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "repeat", function() { return _internal_operators_repeat__WEBPACK_IMPORTED_MODULE_61__["repeat"]; });

/* harmony import */ var _internal_operators_repeatWhen__WEBPACK_IMPORTED_MODULE_62__ = __webpack_require__(/*! ../internal/operators/repeatWhen */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/repeatWhen.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "repeatWhen", function() { return _internal_operators_repeatWhen__WEBPACK_IMPORTED_MODULE_62__["repeatWhen"]; });

/* harmony import */ var _internal_operators_retry__WEBPACK_IMPORTED_MODULE_63__ = __webpack_require__(/*! ../internal/operators/retry */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retry.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "retry", function() { return _internal_operators_retry__WEBPACK_IMPORTED_MODULE_63__["retry"]; });

/* harmony import */ var _internal_operators_retryWhen__WEBPACK_IMPORTED_MODULE_64__ = __webpack_require__(/*! ../internal/operators/retryWhen */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/retryWhen.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "retryWhen", function() { return _internal_operators_retryWhen__WEBPACK_IMPORTED_MODULE_64__["retryWhen"]; });

/* harmony import */ var _internal_operators_refCount__WEBPACK_IMPORTED_MODULE_65__ = __webpack_require__(/*! ../internal/operators/refCount */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/refCount.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "refCount", function() { return _internal_operators_refCount__WEBPACK_IMPORTED_MODULE_65__["refCount"]; });

/* harmony import */ var _internal_operators_sample__WEBPACK_IMPORTED_MODULE_66__ = __webpack_require__(/*! ../internal/operators/sample */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sample.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "sample", function() { return _internal_operators_sample__WEBPACK_IMPORTED_MODULE_66__["sample"]; });

/* harmony import */ var _internal_operators_sampleTime__WEBPACK_IMPORTED_MODULE_67__ = __webpack_require__(/*! ../internal/operators/sampleTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sampleTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "sampleTime", function() { return _internal_operators_sampleTime__WEBPACK_IMPORTED_MODULE_67__["sampleTime"]; });

/* harmony import */ var _internal_operators_scan__WEBPACK_IMPORTED_MODULE_68__ = __webpack_require__(/*! ../internal/operators/scan */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/scan.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "scan", function() { return _internal_operators_scan__WEBPACK_IMPORTED_MODULE_68__["scan"]; });

/* harmony import */ var _internal_operators_sequenceEqual__WEBPACK_IMPORTED_MODULE_69__ = __webpack_require__(/*! ../internal/operators/sequenceEqual */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/sequenceEqual.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "sequenceEqual", function() { return _internal_operators_sequenceEqual__WEBPACK_IMPORTED_MODULE_69__["sequenceEqual"]; });

/* harmony import */ var _internal_operators_share__WEBPACK_IMPORTED_MODULE_70__ = __webpack_require__(/*! ../internal/operators/share */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/share.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "share", function() { return _internal_operators_share__WEBPACK_IMPORTED_MODULE_70__["share"]; });

/* harmony import */ var _internal_operators_shareReplay__WEBPACK_IMPORTED_MODULE_71__ = __webpack_require__(/*! ../internal/operators/shareReplay */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/shareReplay.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "shareReplay", function() { return _internal_operators_shareReplay__WEBPACK_IMPORTED_MODULE_71__["shareReplay"]; });

/* harmony import */ var _internal_operators_single__WEBPACK_IMPORTED_MODULE_72__ = __webpack_require__(/*! ../internal/operators/single */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/single.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "single", function() { return _internal_operators_single__WEBPACK_IMPORTED_MODULE_72__["single"]; });

/* harmony import */ var _internal_operators_skip__WEBPACK_IMPORTED_MODULE_73__ = __webpack_require__(/*! ../internal/operators/skip */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skip.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "skip", function() { return _internal_operators_skip__WEBPACK_IMPORTED_MODULE_73__["skip"]; });

/* harmony import */ var _internal_operators_skipLast__WEBPACK_IMPORTED_MODULE_74__ = __webpack_require__(/*! ../internal/operators/skipLast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipLast.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "skipLast", function() { return _internal_operators_skipLast__WEBPACK_IMPORTED_MODULE_74__["skipLast"]; });

/* harmony import */ var _internal_operators_skipUntil__WEBPACK_IMPORTED_MODULE_75__ = __webpack_require__(/*! ../internal/operators/skipUntil */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipUntil.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "skipUntil", function() { return _internal_operators_skipUntil__WEBPACK_IMPORTED_MODULE_75__["skipUntil"]; });

/* harmony import */ var _internal_operators_skipWhile__WEBPACK_IMPORTED_MODULE_76__ = __webpack_require__(/*! ../internal/operators/skipWhile */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/skipWhile.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "skipWhile", function() { return _internal_operators_skipWhile__WEBPACK_IMPORTED_MODULE_76__["skipWhile"]; });

/* harmony import */ var _internal_operators_startWith__WEBPACK_IMPORTED_MODULE_77__ = __webpack_require__(/*! ../internal/operators/startWith */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/startWith.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "startWith", function() { return _internal_operators_startWith__WEBPACK_IMPORTED_MODULE_77__["startWith"]; });

/* harmony import */ var _internal_operators_subscribeOn__WEBPACK_IMPORTED_MODULE_78__ = __webpack_require__(/*! ../internal/operators/subscribeOn */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/subscribeOn.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "subscribeOn", function() { return _internal_operators_subscribeOn__WEBPACK_IMPORTED_MODULE_78__["subscribeOn"]; });

/* harmony import */ var _internal_operators_switchAll__WEBPACK_IMPORTED_MODULE_79__ = __webpack_require__(/*! ../internal/operators/switchAll */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchAll.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "switchAll", function() { return _internal_operators_switchAll__WEBPACK_IMPORTED_MODULE_79__["switchAll"]; });

/* harmony import */ var _internal_operators_switchMap__WEBPACK_IMPORTED_MODULE_80__ = __webpack_require__(/*! ../internal/operators/switchMap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMap.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "switchMap", function() { return _internal_operators_switchMap__WEBPACK_IMPORTED_MODULE_80__["switchMap"]; });

/* harmony import */ var _internal_operators_switchMapTo__WEBPACK_IMPORTED_MODULE_81__ = __webpack_require__(/*! ../internal/operators/switchMapTo */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/switchMapTo.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "switchMapTo", function() { return _internal_operators_switchMapTo__WEBPACK_IMPORTED_MODULE_81__["switchMapTo"]; });

/* harmony import */ var _internal_operators_take__WEBPACK_IMPORTED_MODULE_82__ = __webpack_require__(/*! ../internal/operators/take */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/take.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "take", function() { return _internal_operators_take__WEBPACK_IMPORTED_MODULE_82__["take"]; });

/* harmony import */ var _internal_operators_takeLast__WEBPACK_IMPORTED_MODULE_83__ = __webpack_require__(/*! ../internal/operators/takeLast */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeLast.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "takeLast", function() { return _internal_operators_takeLast__WEBPACK_IMPORTED_MODULE_83__["takeLast"]; });

/* harmony import */ var _internal_operators_takeUntil__WEBPACK_IMPORTED_MODULE_84__ = __webpack_require__(/*! ../internal/operators/takeUntil */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeUntil.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "takeUntil", function() { return _internal_operators_takeUntil__WEBPACK_IMPORTED_MODULE_84__["takeUntil"]; });

/* harmony import */ var _internal_operators_takeWhile__WEBPACK_IMPORTED_MODULE_85__ = __webpack_require__(/*! ../internal/operators/takeWhile */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/takeWhile.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "takeWhile", function() { return _internal_operators_takeWhile__WEBPACK_IMPORTED_MODULE_85__["takeWhile"]; });

/* harmony import */ var _internal_operators_tap__WEBPACK_IMPORTED_MODULE_86__ = __webpack_require__(/*! ../internal/operators/tap */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/tap.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "tap", function() { return _internal_operators_tap__WEBPACK_IMPORTED_MODULE_86__["tap"]; });

/* harmony import */ var _internal_operators_throttle__WEBPACK_IMPORTED_MODULE_87__ = __webpack_require__(/*! ../internal/operators/throttle */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttle.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "throttle", function() { return _internal_operators_throttle__WEBPACK_IMPORTED_MODULE_87__["throttle"]; });

/* harmony import */ var _internal_operators_throttleTime__WEBPACK_IMPORTED_MODULE_88__ = __webpack_require__(/*! ../internal/operators/throttleTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throttleTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "throttleTime", function() { return _internal_operators_throttleTime__WEBPACK_IMPORTED_MODULE_88__["throttleTime"]; });

/* harmony import */ var _internal_operators_throwIfEmpty__WEBPACK_IMPORTED_MODULE_89__ = __webpack_require__(/*! ../internal/operators/throwIfEmpty */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/throwIfEmpty.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "throwIfEmpty", function() { return _internal_operators_throwIfEmpty__WEBPACK_IMPORTED_MODULE_89__["throwIfEmpty"]; });

/* harmony import */ var _internal_operators_timeInterval__WEBPACK_IMPORTED_MODULE_90__ = __webpack_require__(/*! ../internal/operators/timeInterval */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeInterval.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "timeInterval", function() { return _internal_operators_timeInterval__WEBPACK_IMPORTED_MODULE_90__["timeInterval"]; });

/* harmony import */ var _internal_operators_timeout__WEBPACK_IMPORTED_MODULE_91__ = __webpack_require__(/*! ../internal/operators/timeout */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeout.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "timeout", function() { return _internal_operators_timeout__WEBPACK_IMPORTED_MODULE_91__["timeout"]; });

/* harmony import */ var _internal_operators_timeoutWith__WEBPACK_IMPORTED_MODULE_92__ = __webpack_require__(/*! ../internal/operators/timeoutWith */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timeoutWith.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "timeoutWith", function() { return _internal_operators_timeoutWith__WEBPACK_IMPORTED_MODULE_92__["timeoutWith"]; });

/* harmony import */ var _internal_operators_timestamp__WEBPACK_IMPORTED_MODULE_93__ = __webpack_require__(/*! ../internal/operators/timestamp */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/timestamp.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "timestamp", function() { return _internal_operators_timestamp__WEBPACK_IMPORTED_MODULE_93__["timestamp"]; });

/* harmony import */ var _internal_operators_toArray__WEBPACK_IMPORTED_MODULE_94__ = __webpack_require__(/*! ../internal/operators/toArray */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/toArray.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "toArray", function() { return _internal_operators_toArray__WEBPACK_IMPORTED_MODULE_94__["toArray"]; });

/* harmony import */ var _internal_operators_window__WEBPACK_IMPORTED_MODULE_95__ = __webpack_require__(/*! ../internal/operators/window */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/window.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "window", function() { return _internal_operators_window__WEBPACK_IMPORTED_MODULE_95__["window"]; });

/* harmony import */ var _internal_operators_windowCount__WEBPACK_IMPORTED_MODULE_96__ = __webpack_require__(/*! ../internal/operators/windowCount */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowCount.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "windowCount", function() { return _internal_operators_windowCount__WEBPACK_IMPORTED_MODULE_96__["windowCount"]; });

/* harmony import */ var _internal_operators_windowTime__WEBPACK_IMPORTED_MODULE_97__ = __webpack_require__(/*! ../internal/operators/windowTime */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowTime.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "windowTime", function() { return _internal_operators_windowTime__WEBPACK_IMPORTED_MODULE_97__["windowTime"]; });

/* harmony import */ var _internal_operators_windowToggle__WEBPACK_IMPORTED_MODULE_98__ = __webpack_require__(/*! ../internal/operators/windowToggle */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowToggle.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "windowToggle", function() { return _internal_operators_windowToggle__WEBPACK_IMPORTED_MODULE_98__["windowToggle"]; });

/* harmony import */ var _internal_operators_windowWhen__WEBPACK_IMPORTED_MODULE_99__ = __webpack_require__(/*! ../internal/operators/windowWhen */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/windowWhen.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "windowWhen", function() { return _internal_operators_windowWhen__WEBPACK_IMPORTED_MODULE_99__["windowWhen"]; });

/* harmony import */ var _internal_operators_withLatestFrom__WEBPACK_IMPORTED_MODULE_100__ = __webpack_require__(/*! ../internal/operators/withLatestFrom */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/withLatestFrom.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "withLatestFrom", function() { return _internal_operators_withLatestFrom__WEBPACK_IMPORTED_MODULE_100__["withLatestFrom"]; });

/* harmony import */ var _internal_operators_zip__WEBPACK_IMPORTED_MODULE_101__ = __webpack_require__(/*! ../internal/operators/zip */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zip.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "zip", function() { return _internal_operators_zip__WEBPACK_IMPORTED_MODULE_101__["zip"]; });

/* harmony import */ var _internal_operators_zipAll__WEBPACK_IMPORTED_MODULE_102__ = __webpack_require__(/*! ../internal/operators/zipAll */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/internal/operators/zipAll.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "zipAll", function() { return _internal_operators_zipAll__WEBPACK_IMPORTED_MODULE_102__["zipAll"]; });

/** PURE_IMPORTS_START  PURE_IMPORTS_END */








































































































//# sourceMappingURL=index.js.map


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

/***/ "./jsTarget/web/features/merchandisingsmarteditContainer/index.ts":
/*!************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditContainer/index.ts ***!
  \************************************************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _legacymerchandisingsmarteditcontainer__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./legacymerchandisingsmarteditcontainer */ "./jsTarget/web/features/merchandisingsmarteditContainer/legacymerchandisingsmarteditcontainer.ts");
/* harmony import */ var _merchandisingsmarteditcontainer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./merchandisingsmarteditcontainer */ "./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditcontainer.ts");
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///




/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmarteditContainer/legacymerchandisingsmarteditcontainer.ts":
/*!********************************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditContainer/legacymerchandisingsmarteditcontainer.ts ***!
  \********************************************************************************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var angular__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! angular */ "angular");
/* harmony import */ var angular__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(angular__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var merchandisingsmarteditContainer_merchandisingsmarteditContainer_bundle_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! merchandisingsmarteditContainer/merchandisingsmarteditContainer_bundle.js */ "./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditContainer_bundle.js");
/* harmony import */ var merchandisingsmarteditContainer_merchandisingsmarteditContainer_bundle_js__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(merchandisingsmarteditContainer_merchandisingsmarteditContainer_bundle_js__WEBPACK_IMPORTED_MODULE_1__);
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///


angular__WEBPACK_IMPORTED_MODULE_0__["module"]("merchandisingsmarteditContainer", [
    "loadConfigModule",
    "smarteditServicesModule"
])
    .run(["loadConfigManagerService", "sharedDataService", function (loadConfigManagerService, sharedDataService) {
    "ngInject";
    loadConfigManagerService.loadAsObject().then(function (configurations) {
        sharedDataService.set("contextDrivenServicesMerchandisingUrl", configurations.contextDrivenServicesMerchandisingUrl);
    });
}]);


/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditContainer_bundle.js":
/*!*********************************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditContainer_bundle.js ***!
  \*********************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {




/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditcontainer.ts":
/*!**************************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditContainer/merchandisingsmarteditcontainer.ts ***!
  \**************************************************************************************************/
/*! exports provided: MerchandisingSmartEditContainerModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MerchandisingSmartEditContainerModule", function() { return MerchandisingSmartEditContainerModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! smarteditcommons */ "../../smartedit-module/smartedit/smartedit-build/lib/smarteditcommons/src/index.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(smarteditcommons__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var _merchandisingsmarteditcommons__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../merchandisingsmarteditcommons */ "./jsTarget/web/features/merchandisingsmarteditcommons/index.ts");
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///





var MerchandisingSmartEditContainerModule = /** @class */ (function () {
    function MerchandisingSmartEditContainerModule() {
    }
    MerchandisingSmartEditContainerModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(smarteditcommons__WEBPACK_IMPORTED_MODULE_3__["SeEntryModule"])("merchandisingsmarteditContainer"),
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            imports: [],
            declarations: [],
            entryComponents: [],
            providers: [
                {
                    provide: _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HTTP_INTERCEPTORS"],
                    useClass: _merchandisingsmarteditcommons__WEBPACK_IMPORTED_MODULE_4__["MerchandisingExperienceInterceptor"],
                    multi: true
                }
            ]
        })
    ], MerchandisingSmartEditContainerModule);
    return MerchandisingSmartEditContainerModule;
}());



/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmarteditcommons/index.ts":
/*!**********************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditcommons/index.ts ***!
  \**********************************************************************/
/*! exports provided: MerchandisingExperienceInterceptor */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _merchandisingExperienceInterceptor__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./merchandisingExperienceInterceptor */ "./jsTarget/web/features/merchandisingsmarteditcommons/merchandisingExperienceInterceptor.ts");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "MerchandisingExperienceInterceptor", function() { return _merchandisingExperienceInterceptor__WEBPACK_IMPORTED_MODULE_0__["MerchandisingExperienceInterceptor"]; });

///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///



/***/ }),

/***/ "./jsTarget/web/features/merchandisingsmarteditcommons/merchandisingExperienceInterceptor.ts":
/*!***************************************************************************************************!*\
  !*** ./jsTarget/web/features/merchandisingsmarteditcommons/merchandisingExperienceInterceptor.ts ***!
  \***************************************************************************************************/
/*! exports provided: MerchandisingExperienceInterceptor */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MerchandisingExperienceInterceptor", function() { return MerchandisingExperienceInterceptor; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! rxjs */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/index.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs/operators */ "../../npm-ancillary-module/npmancillary/resources/npm/node_modules/rxjs/_esm5/operators/index.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! smarteditcommons */ "../../smartedit-module/smartedit/smartedit-build/lib/smarteditcommons/src/index.js");
/* harmony import */ var smarteditcommons__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(smarteditcommons__WEBPACK_IMPORTED_MODULE_4__);
///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///





var MerchandisingExperienceInterceptor = /** @class */ (function () {
    MerchandisingExperienceInterceptor.$inject = ["sharedDataService"];
    function MerchandisingExperienceInterceptor(sharedDataService) {
        this.sharedDataService = sharedDataService;
    }
    MerchandisingExperienceInterceptor_1 = MerchandisingExperienceInterceptor;
    MerchandisingExperienceInterceptor.prototype.intercept = function (request, next) {
        if (MerchandisingExperienceInterceptor_1.MERCHCMSWEBSERVICES_PATH.test(request.url)) {
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["from"])(this.sharedDataService.get("experience")).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["switchMap"])(function (experience) {
                if (experience) {
                    if (request.url.indexOf(smarteditcommons__WEBPACK_IMPORTED_MODULE_4__["CONTEXT_SITE_ID"]) > -1) {
                        var params = request.params;
                        if (params) {
                            params = params
                                .delete("catalogId")
                                .delete("catalogVersion")
                                .delete("mask");
                        }
                        var updatedUrlRequest = request.clone({
                            url: request.url.replace(smarteditcommons__WEBPACK_IMPORTED_MODULE_4__["CONTEXT_SITE_ID"], experience.catalogDescriptor.siteId),
                            params: params
                        });
                        return next.handle(updatedUrlRequest);
                    }
                }
                return next.handle(request);
            }));
        }
        else {
            return next.handle(request);
        }
    };
    var MerchandisingExperienceInterceptor_1;
    MerchandisingExperienceInterceptor.MERCHCMSWEBSERVICES_PATH = /\/merchandisingcmswebservices/;
    MerchandisingExperienceInterceptor = MerchandisingExperienceInterceptor_1 = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])(),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [smarteditcommons__WEBPACK_IMPORTED_MODULE_4__["ISharedDataService"]])
    ], /* @ngInject */ MerchandisingExperienceInterceptor);
    return MerchandisingExperienceInterceptor;
}());



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
//# sourceMappingURL=merchandisingsmarteditContainer.js.map