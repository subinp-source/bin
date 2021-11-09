/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentType } from '../../entities/components';
import { cmsLinkToComponentAttribute } from './cmsLinkToComponentAttribute.constant';

const isNavigationNodeEditable = true;
const isProductsEditable = true;
const isCategoriesEditable = true;

export const componentTypes: IComponentType[] = [
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmsitem.name.name',
                localized: false,
                paged: false,
                qualifier: 'name',
                required: true
            },
            {
                cmsStructureType: 'PageRestrictionsEditor',
                collection: true,
                editable: true,
                i18nKey: 'type.abstractpage.restrictions.name',
                idAttribute: 'uuid',
                labelAttributes: ['name', 'uid'],
                localized: false,
                paged: true,
                params: {
                    typeCode: 'AbstractRestriction'
                },
                qualifier: 'restrictions',
                required: false
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.contentpage.label.name',
                localized: false,
                paged: false,
                qualifier: 'label',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.abstractpage.title.name',
                localized: true,
                paged: false,
                qualifier: 'title',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: false,
                i18nKey: 'type.cmsitem.uid.name',
                localized: false,
                paged: false,
                qualifier: 'uid',
                required: false
            },
            {
                cmsStructureType: 'DateTime',
                collection: false,
                editable: false,
                i18nKey: 'type.item.creationtime.name',
                localized: false,
                paged: false,
                qualifier: 'creationtime',
                required: true
            },
            {
                cmsStructureType: 'DateTime',
                collection: false,
                editable: false,
                i18nKey: 'type.item.modifiedtime.name',
                localized: false,
                paged: false,
                qualifier: 'modifiedtime',
                required: true
            }
        ],
        category: 'PAGE',
        code: 'ContentPage',
        i18nKey: 'type.contentpage.name',
        name: 'Content Page',
        type: 'contentPageData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'RichText',
                i18nKey: 'type.cmsparagraphcomponent.content.name',
                localized: false,
                qualifier: 'content'
            }
        ],
        category: 'COMPONENT',
        code: 'CMSParagraphComponent',
        i18nKey: 'type.cmsparagraphcomponent.name',
        name: 'Paragraph'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'NavigationNodeSelector',
                i18nKey: 'type.footernavigationcomponent.navigationnode.name',
                localized: false,
                qualifier: 'navigationNode'
            }
        ],
        category: 'COMPONENT',
        code: 'FooterNavigationComponent',
        i18nKey: 'type.footernavigationcomponent.name',
        name: 'Footer Navigation Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'Media',
                i18nKey: 'type.simplebannercomponent.media.name',
                localized: true,
                qualifier: 'media',
                containedTypes: ['Media']
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.simplebannercomponent.urllink.name',
                localized: false,
                qualifier: 'urlLink'
            },
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.simplebannercomponent.external.name',
                localized: false,
                qualifier: 'external'
            }
        ],
        category: 'COMPONENT',
        code: 'SimpleBannerComponent',
        i18nKey: 'type.simplebannercomponent.name',
        name: 'Simple Banner Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                qualifier: 'id',
                i18nKey: 'type.cmsparagraphcomponent.id.name'
            },
            {
                cmsStructureType: 'LongString',
                qualifier: 'headline',
                i18nKey: 'type.cmsparagraphcomponent.headline.name'
            }
        ],
        category: 'NotToBeFound',
        code: 'XYZComponent',
        i18nKey: 'type.xyzcomponent.name',
        name: 'XYZ Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                qualifier: 'id',
                i18nKey: 'type.thesmarteditcomponenttype.id.name'
            },
            {
                cmsStructureType: 'LongString',
                qualifier: 'headline',
                i18nKey: 'type.thesmarteditcomponenttype.headline.name'
            },
            {
                cmsStructureType: 'Boolean',
                qualifier: 'active',
                i18nKey: 'type.thesmarteditcomponenttype.active.name'
            },
            {
                cmsStructureType: 'Date',
                qualifier: 'activationDate',
                i18nKey: 'type.thesmarteditcomponenttype.activationDate.name'
            },
            {
                cmsStructureType: 'RichText',
                qualifier: 'content',
                i18nKey: 'type.thesmarteditcomponenttype.content.name'
            },
            {
                cmsStructureType: 'LinkToggle',
                qualifier: 'linkToggle',
                i18nKey: 'se.editor.linkto.label',
                localized: false
            }
        ],
        category: 'NotToBeFound',
        code: 'thesmarteditComponentType',
        i18nKey: 'type.abccomponent.name',
        name: 'ABC Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'Media',
                qualifier: 'media',
                i18nKey: 'type.typewithmedia.media.name',
                localized: true,
                required: true,
                containedTypes: ['Media']
            }
        ],
        category: 'NotToBeFound',
        code: 'TypeWithMedia',
        i18nKey: 'type.TypeWithMedia.name',
        name: 'TypeWithMedia Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                qualifier: 'afield',
                i18nKey: 'a field',
                localized: false
            },
            {
                cmsStructureType: 'MediaContainer',
                qualifier: 'media',
                i18nKey: 'type.typewithmediacontainer.media.name',
                localized: true,
                required: true,
                options: [
                    {
                        id: 'widescreen',
                        label: 'se.media.format.widescreen'
                    },
                    {
                        id: 'desktop',
                        label: 'se.media.format.desktop'
                    },
                    {
                        id: 'tablet',
                        label: 'se.media.format.tablet'
                    },
                    {
                        id: 'mobile',
                        label: 'se.media.format.mobile'
                    }
                ],
                containedTypes: ['MediaContainer', 'MediaFormat']
            }
        ],
        category: 'NotToBeFound',
        code: 'TypeWithMediaContainer',
        name: 'TypeWithMediaContainer Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                qualifier: 'id',
                i18nKey: 'type.thesmarteditComponentType.id.name',
                localized: false
            },
            {
                cmsStructureType: 'Media',
                qualifier: 'media',
                i18nKey: 'type.thesmarteditComponentType.media.name',
                localized: true,
                required: true,
                containedTypes: ['Media']
            },
            {
                cmsStructureType: 'Enum',
                cmsStructureEnumType: 'de.mypackage.Orientation',
                qualifier: 'orientation',
                i18nKey: 'type.thesmarteditcomponenttype.orientation.name',
                localized: false,
                required: true
            },
            {
                cmsStructureType: 'LongString',
                qualifier: 'headline',
                i18nKey: 'type.thesmarteditComponentType.headline.name',
                localized: false
            },
            {
                cmsStructureType: 'Boolean',
                qualifier: 'active',
                i18nKey: 'type.thesmarteditComponentType.active.name',
                localized: false
            },
            {
                cmsStructureType: 'RichText',
                qualifier: 'content',
                i18nKey: 'type.thesmarteditComponentType.content.name',
                localized: true
            },
            {
                cmsStructureType: 'LinkToggle',
                qualifier: 'linkToggle',
                i18nKey: 'se.editor.linkto.label',
                localized: false
            }
        ],
        category: 'NotToBeFound',
        code: 'componentToValidateType',
        name: 'Validation Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'NavigationNodeSelector',
                qualifier: 'navigationComponent',
                i18nKey: 'type.thesmarteditcomponenttype.navigationComponent.name',
                localized: false,
                required: true,
                editable: isNavigationNodeEditable
            }
        ],
        category: 'NotToBeFound',
        code: 'NavigationComponentType',
        name: 'Navigation Component'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.productcarouselcomponent.title.name',
                localized: true,
                mode: 'DEFAULT',
                paged: false,
                qualifier: 'title',
                required: false
            },
            {
                cmsStructureType: 'MultiProductSelector',
                collection: false,
                editable: isProductsEditable,
                i18nKey: 'type.productcarouselcomponent.products.name',
                localized: false,
                mode: 'DEFAULT',
                paged: false,
                qualifier: 'products',
                required: false
            },
            {
                cmsStructureType: 'MultiCategorySelector',
                collection: false,
                editable: isCategoriesEditable,
                i18nKey: 'type.productcarouselcomponent.categories.name',
                localized: false,
                mode: 'DEFAULT',
                paged: false,
                qualifier: 'categories',
                required: false
            }
        ],
        category: 'COMPONENT',
        code: 'ProductCarouselComponent',
        i18nKey: 'type.productcarouselcomponent.name',
        name: 'Product Carousel',
        type: 'productCarouselComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmslinkcomponent.linkname.name',
                localized: true,
                mode: 'DEFAULT',
                paged: false,
                qualifier: 'linkName',
                required: true
            },
            cmsLinkToComponentAttribute
        ],
        category: 'COMPONENT',
        code: 'CMSLinkComponent',
        i18nKey: 'type.cmslinkcomponent.name',
        name: 'Link',
        type: 'cmsLinkComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'SingleOnlineProductSelector',
                i18nKey: 'name',
                mode: 'DEFAULT',
                qualifier: 'product',
                required: true
            }
        ],
        category: 'COMPONENT',
        code: 'TestSingleOnlineProductSelector',
        i18nKey: 'type.testsingleonlineproductselector.name',
        name: 'TestSingleOnlineProductSelector',
        type: 'testComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'SingleOnlineCategorySelector',
                i18nKey: 'name',
                mode: 'DEFAULT',
                qualifier: 'category',
                required: true
            }
        ],
        category: 'COMPONENT',
        code: 'TestSingleOnlineCategorySelector',
        i18nKey: 'type.testsingleonlinecategoryselector.name',
        name: 'TestSingleOnlineCategorySelector',
        type: 'testComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.abstractcmscomponent.name.name',
                localized: false,
                qualifier: 'name'
            }
        ],
        category: 'NotToBeFound',
        code: 'AbstractCMSComponent',
        i18nKey: 'type.abstractcmscomponent.name',
        name: 'abstractcmscomponent',
        type: 'abstractcmscomponenttype'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.abstractcmscomponent.name.name',
                localized: false,
                qualifier: 'name'
            },
            {
                cmsStructureType: 'CMSItemDropdown',
                i18nKey: 'type.abstractcmscomponent.links.name',
                collection: true,
                localized: false,
                qualifier: 'links',
                idAttribute: 'uuid',
                paged: true,
                required: false,
                params: {
                    typeCode: 'CMSLinkComponent'
                },
                subTypes: {
                    CMSLinkComponent: 'type.cmslinkcomponent.name'
                }
            }
        ],
        category: 'NotToBeFound',
        code: 'componentType1',
        i18nKey: 'type.abstractcmscomponent.name',
        name: 'abstractcmscomponent',
        type: 'componentType1'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.tabset.name.name',
                localized: false,
                qualifier: 'name'
            },
            {
                cmsStructureType: 'CMSItemDropdown',
                i18nKey: 'type.tabset.topbanner.name',
                localized: false,
                idAttribute: 'uuid',
                paged: true,
                qualifier: 'banner',
                params: {
                    typeCode: 'BannerComponent'
                },
                subTypes: {
                    BannerComponent: 'type.banner.name',
                    ResponsiveBannerComponent: 'type.responsivebanner.name'
                }
            },
            {
                cmsStructureType: 'CMSItemDropdown',
                i18nKey: 'type.tabset.tabs.name',
                collection: true,
                localized: false,
                qualifier: 'tabs',
                idAttribute: 'uuid',
                paged: true,
                required: false,
                params: {
                    typeCode: 'CmsTab'
                },
                subTypes: {
                    CmsTab: 'type.cmstab.name'
                }
            },
            {
                cmsStructureType: 'CMSItemDropdown',
                i18nKey: 'type.tabset.links.name',
                collection: true,
                localized: false,
                qualifier: 'links',
                idAttribute: 'uuid',
                paged: true,
                required: false,
                params: {
                    typeCode: 'CMSLinkComponent'
                },
                subTypes: {
                    CMSLinkComponent: 'type.cmslinkcomponent.name'
                }
            }
        ],
        category: 'COMPONENT',
        code: 'TabsetComponent', // Example of a nested component
        i18nKey: 'type.tabset.name',
        name: 'TabsetComponent',
        type: 'TabsetComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.banner.name.name',
                localized: false,
                qualifier: 'name'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.tabset.imagepath.name',
                localized: false,
                qualifier: 'image'
            }
        ],
        category: 'COMPONENT',
        code: 'BannerComponent',
        i18nKey: 'type.tabset.name',
        name: 'BannerComponent',
        type: 'BannerComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.responsivebannercomponent.name.name',
                localized: false,
                qualifier: 'name'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.tabset.imagepath.name',
                localized: false,
                qualifier: 'image'
            },
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.tabset.rotate.name',
                localized: false,
                qualifier: 'rotate'
            }
        ],
        category: 'COMPONENT',
        code: 'ResponsiveBannerComponent',
        i18nKey: 'type.tabset.name',
        name: 'ResponsiveBannerComponent',
        type: 'ResponsiveBannerComponentData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'Boolean',
                i18nKey: 'type.component.abstractcmscomponent.visible.name',
                localized: false,
                qualifier: 'visible',
                postfix: 'se.cms.visible.postfix.text'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.tabset.name.name',
                localized: false,
                qualifier: 'name'
            },
            {
                cmsStructureType: 'ShortString',
                i18nKey: 'type.tabset.title.name',
                localized: false,
                qualifier: 'title'
            },
            {
                cmsStructureType: 'CMSItemDropdown',
                i18nKey: 'type.tabset.tabs.name',
                collection: true,
                localized: false,
                qualifier: 'tabs',
                idAttribute: 'uuid',
                paged: true,
                required: false,
                params: {
                    typeCode: 'CmsTab'
                },
                subTypes: {
                    CmsTab: 'type.cmstab.name'
                }
            }
        ],
        category: 'COMPONENT',
        code: 'CmsTab',
        i18nKey: 'type.tab.name',
        name: 'CmsTab',
        type: 'CmsTabData'
    }
];
