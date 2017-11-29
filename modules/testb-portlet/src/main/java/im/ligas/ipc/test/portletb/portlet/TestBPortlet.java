package im.ligas.ipc.test.portletb.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import im.ligas.ipc.test.portletb.constants.TestBPortletKeys;
import org.osgi.service.component.annotations.Component;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ligasm
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=testb-portlet Portlet",
                "javax.portlet.name=" + TestBPortletKeys.TestB,
                "javax.portlet.security-role-ref=power-user,user",
                "javax.portlet.supported-processing-event=testEvent"
        },
        service = Portlet.class
)
public class TestBPortlet extends GenericPortlet {

    private static final String PREF_MY_TEST_PREF = "myTestPref";
    private static Log LOG = LogFactoryUtil.getLog(TestBPortlet.class);

    @Override
    protected void doView(
            RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        PortletPreferences preferences = renderRequest.getPreferences();
        String pref = preferences.getValue(PREF_MY_TEST_PREF, "Not set");

        PrintWriter printWriter = renderResponse.getWriter();

        printWriter.print("Value loaded form preferences "+ pref);
    }

    @ProcessEvent(name = "testEvent")
    public void myEvent(EventRequest request, EventResponse response)
            throws javax.portlet.PortletException,
            java.io.IOException {
        Event event = request.getEvent();
        String value = (String) event.getValue();
        LOG.error("test event received");
        PortletPreferences preferences = request.getPreferences();
        preferences.setValue(PREF_MY_TEST_PREF, value);
        preferences.store();
    }

}
