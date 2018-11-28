package com.liferay.form.field.custom.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Ankit
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.field.type.display.order:Integer=9",
		"ddm.form.field.type.icon=text",
		"ddm.form.field.type.js.class.name=Liferay.DDM.Field.FileUploadFormField",
		"ddm.form.field.type.js.module=file-upload-form-field-form-field",
		"ddm.form.field.type.label=file-upload-form-field-label",
		"ddm.form.field.type.name=FileUploadFormField"
	},
	service = DDMFormFieldType.class
)
public class FileUploadFormFieldDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public String getName() {
		return "FileUploadFormField";
	}

}