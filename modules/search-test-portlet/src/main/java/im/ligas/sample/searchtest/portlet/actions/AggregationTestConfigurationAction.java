package im.ligas.sample.searchtest.portlet.actions;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import im.ligas.sample.searchtest.constants.SearchTestPortletKeys;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + SearchTestPortletKeys.AGGREGATION_TEST,
                "javax.portlet.name=" + SearchTestPortletKeys.RANGE_TEST
        },
        service = ConfigurationAction.class
)
public class AggregationTestConfigurationAction extends DefaultConfigurationAction {
}
