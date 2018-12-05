;(function() {
    AUI().applyConfig(
        {
            groups: {
                mymodulesoverride: {
                    base: MODULE_PATH + '/js/',
                    combine: Liferay.AUI.getCombine(),
                    filter: Liferay.AUI.getFilterConfig(),
                    modules: {
                        'my-liferay-ddm-form-renderer-util': {
                            path: 'util.js',
                            condition: {
                                name: 'my-liferay-ddm-form-renderer-util',
                                trigger: 'liferay-ddm-form-renderer-util',
                                when: 'instead'
                            }
                        }
                    },
                    root: MODULE_PATH + '/js/'
                }
            }
        }
    );
})();
