package im.ligas.ipc.test.portletc.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import im.ligas.ipc.test.portletc.constants.TestCPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.ProcessAction;
import javax.xml.namespace.QName;

import org.osgi.service.component.annotations.Component;

/**
 * @author ligasm
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=testc-portlet Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + TestCPortletKeys.TestC,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supported-publishing-event=testEvent"
	},
	service = Portlet.class
)
public class TestCPortlet extends MVCPortlet {

	private static Log LOG = LogFactoryUtil.getLog(TestCPortlet.class);

	@ProcessAction(name = "sendEvent")
	public void process(ActionRequest request, ActionResponse response) {
		String value = ParamUtil.getString(request, "testValue", "");
		LOG.error("Sending Event with "+value);
		response.setEvent("testEvent", value);

	}
}
