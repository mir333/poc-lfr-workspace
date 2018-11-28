;(function() {
	AUI().applyConfig(
		{
			groups: {
				'file-upload-form-field-group': {
					base: MODULE_PATH + '/',
					combine: Liferay.AUI.getCombine(),
					modules: {
						'file-upload-form-field-form-field': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'file-upload-form-field_field.js',
							requires: [
								'liferay-ddm-form-renderer-field'
							]
						},
						'file-upload-form-field-form-field-template': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'file-upload-form-field.soy.js',
							requires: [
								'soyutils'
							]
						}
					},
					root: MODULE_PATH + '/'
				}
			}
		}
	);
})();