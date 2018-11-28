package coms.hays.ddl.file.type;


import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordConstants;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetSettings;
import com.liferay.dynamic.data.lists.service.DDLRecordService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.ByteArrayFileInputStream;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = {
		"javax.portlet.name=" + CustomAddRecordFormFilterKeys.DYNAMIC_DATA_LISTS_FORM,
		"mvc.command.name=addRecord",
		"service.ranking:Integer=100"
	},
	service = MVCActionCommand.class
)
public class CustomAddRecordFormAction extends BaseMVCActionCommand {
	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		System.out.println("Control Came Here");
		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		long recordSetId = ParamUtil.getLong(actionRequest, "recordSetId");

		DDLRecordSet recordSet = _ddlRecordSetService.getRecordSet(recordSetId);

		validateCaptcha(actionRequest, recordSet);

		DDMForm ddmForm = getDDMForm(recordSet);
		
		
		DDMFormValues ddmFormValues = _ddmFormValuesFactory.create(
			actionRequest, ddmForm);
		
		for(DDMFormField formField : ddmForm.getDDMFormFields()){
			System.out.println("FormField TYpe--->"+formField.getType());
			System.out.println("FormField NameSpace--->"+formField.getFieldNamespace());
			System.out.println("FormField Name--->"+formField.getName());
			if(formField.getType().equalsIgnoreCase(CustomAddRecordFormFilterKeys.CUSTOM_FILE_TYPE)){
				// execute file logic
				System.out.println("File Fetch Logic");
				
				Map<String, List<DDMFormFieldValue>> formFieldMap = ddmFormValues.getDDMFormFieldValuesMap();
				for(Map.Entry<String,List<DDMFormFieldValue>> entry : formFieldMap.entrySet()){
					 System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
					 for(DDMFormFieldValue formFieldValue : entry.getValue()){
						 System.out.println("Form Field Value"+formFieldValue.getValue());
					 }
			}
				UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
				ByteArrayFileInputStream inputStream = null;
				 try {
		             File file = uploadPortletRequest.getFile("TEstFile");
		             if (!file.exists()) {
		               System.out.println("Empty File");
		            }
		           if ((file != null) && file.exists()) {
		        	   System.out.println("file Name"+file.getName());
		                  inputStream = new ByteArrayFileInputStream(file, 1024);
		                   byte[] data;
		                   try {
		                          data = FileUtil.getBytes(inputStream);
		                     } catch (IOException e) {
		                             e.printStackTrace();
		                      }
		           }
		 }
		 finally {
		 StreamUtil.cleanUp(inputStream);
		 }
			}
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecord.class.getName(), actionRequest);

		DDLRecord ddlRecord = _ddlRecordService.addRecord(
			groupId, recordSetId, DDLRecordConstants.DISPLAY_INDEX_DEFAULT,
			ddmFormValues, serviceContext);

		if (isEmailNotificationEnabled(recordSet)) {
			//Custom Email Code
		}

		DDLRecordSetSettings recordSetSettings = recordSet.getSettingsModel();

		String redirectURL = recordSetSettings.redirectURL();

		if (SessionErrors.isEmpty(actionRequest) &&
			Validator.isNotNull(redirectURL)) {

			String portletId = PortalUtil.getPortletId(actionRequest);

			SessionMessages.add(
				actionRequest, portletId,
				SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);

			actionResponse.sendRedirect(redirectURL);
		}
	}

	protected DDMForm getDDMForm(DDLRecordSet recordSet)
		throws PortalException {

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		return ddmStructure.getDDMForm();
	}

	protected boolean isEmailNotificationEnabled(DDLRecordSet recordSet)
		throws PortalException {

		DDLRecordSetSettings recordSettings = recordSet.getSettingsModel();

		return recordSettings.sendEmailNotification();
	}
	
	@Reference(target = "(&(mvc.command.name=addRecord)(javax.portlet.name="+ CustomAddRecordFormFilterKeys.DYNAMIC_DATA_LISTS_FORM
			+ ")(component.name=com.liferay.dynamic.data.lists.form.web.internal.portlet.action.AddRecordMVCActionCommand))")
	protected MVCActionCommand mvcActionCommand;

	

	@Reference(unbind = "-")
	protected void setDDLRecordService(DDLRecordService ddlRecordService) {
		_ddlRecordService = ddlRecordService;
	}

	@Reference(unbind = "-")
	protected void setDDLRecordSetService(
		DDLRecordSetService ddlRecordSetService) {

		_ddlRecordSetService = ddlRecordSetService;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesFactory(
		DDMFormValuesFactory ddmFormValuesFactory) {

		_ddmFormValuesFactory = ddmFormValuesFactory;
	}

	protected void validateCaptcha(
			ActionRequest actionRequest, DDLRecordSet recordSet)
		throws Exception {

		DDLRecordSetSettings recordSetSettings = recordSet.getSettingsModel();

		if (recordSetSettings.requireCaptcha()) {
			try {
				CaptchaUtil.check(actionRequest);
			}
			catch (CaptchaTextException cte) {
				SessionErrors.add(
					actionRequest, CaptchaTextException.class.getName());

				throw cte;
			}
		}
	}

	private DDLRecordService _ddlRecordService;
	private DDLRecordSetService _ddlRecordSetService;
	private DDMFormValuesFactory _ddmFormValuesFactory;
	
}
