/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.dynamic.data.mapping.type.date;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeSettings;

import org.osgi.service.component.annotations.Component;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.field.type.display.order:Integer=5",
		"ddm.form.field.type.icon=calendar",
		"ddm.form.field.type.js.class.name=Liferay.DDM.Field.Date",
		"ddm.form.field.type.js.module=liferay-ddm-form-field-date",
		"ddm.form.field.type.label=date-field-type-label",
		"ddm.form.field.type.name=myDate"
	},
	service = DDMFormFieldType.class
)
public class DateDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public Class<? extends DDMFormFieldTypeSettings>
		getDDMFormFieldTypeSettings() {

		return DateDDMFormFieldTypeSettings.class;
	}

	@Override
	public String getName() {
		return "myDate";
	}

}
