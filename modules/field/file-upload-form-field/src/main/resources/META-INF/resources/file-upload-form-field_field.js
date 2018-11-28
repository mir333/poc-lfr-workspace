AUI.add(
	'file-upload-form-field-form-field',
	function(A) {
		var FileUploadFormFieldField = A.Component.create(
			{
				ATTRS: {
					type: {
						value: 'file-upload-form-field-form-field'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'file-upload-form-field-form-field'

			}
		);

		Liferay.namespace('DDM.Field').FileUploadFormField = FileUploadFormFieldField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);