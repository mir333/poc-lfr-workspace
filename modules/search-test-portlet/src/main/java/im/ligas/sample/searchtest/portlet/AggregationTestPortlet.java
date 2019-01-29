package im.ligas.sample.searchtest.portlet;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import im.ligas.sample.searchtest.constants.SearchTestPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ligasm
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/aggregation/view.jsp",
                "javax.portlet.init-param.config-template=/aggregation/configuration.jsp",
                "javax.portlet.name=" + SearchTestPortletKeys.AGGREGATION_TEST,
                "javax.portlet.display-name=AggregationTest",
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class AggregationTestPortlet extends MVCPortlet {

    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AggregationTestPortlet.class);

    @Reference
    private DDMIndexer ddmIndexer;

    @Reference
    private IndexSearcherHelper indexSearcherHelper;

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long structureId = PrefsParamUtil.getLong(renderRequest.getPreferences(), renderRequest, SearchTestPortletKeys.PREF_STRUCTURE_ID, 0);
        String value = renderRequest.getPreferences().getValue(SearchTestPortletKeys.PREF_FIELD_NAME, null);
        if (value != null) {
            String fieldName = ddmIndexer.encodeName(structureId, value, themeDisplay.getLocale());

            try {
                SearchContext searchContext = new SearchContext();
                searchContext.setCompanyId(themeDisplay.getCompanyId());
                BooleanQuery booleanQuery = BooleanQueryFactoryUtil.create(searchContext);

                GroupBy groupBy = new GroupBy(fieldName);
                groupBy.setSize(30);
                searchContext.setGroupBy(groupBy);

                Hits search = indexSearcherHelper.search(searchContext, booleanQuery);

                List<Data> data = new ArrayList<>();
                for (Map.Entry<String, Hits> entry : search.getGroupedHits().entrySet()) {
                    data.add(new Data(entry.getKey(), entry.getValue().getLength()));
                }
                renderRequest.setAttribute("data", data);
            } catch (SearchException e) {
                LOG.error("Unexpected error in search", e);
            }
        }
        super.doView(renderRequest, renderResponse);
    }
}
