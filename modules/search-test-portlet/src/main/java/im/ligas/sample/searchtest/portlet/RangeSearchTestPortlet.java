package im.ligas.sample.searchtest.portlet;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermRangeQueryImpl;
import com.liferay.portal.kernel.service.persistence.PortletUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import im.ligas.sample.searchtest.constants.SearchTestPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static im.ligas.sample.searchtest.constants.SearchTestPortletKeys.NUMBER_SUFFIX;

/**
 * @author ligasm
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/range/view.jsp",
                "javax.portlet.init-param.config-template=/range/configuration.jsp",
                "javax.portlet.name=" + SearchTestPortletKeys.RANGE_TEST,
                "javax.portlet.display-name=RangeTest",
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class RangeSearchTestPortlet extends MVCPortlet {

    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AggregationTestPortlet.class);

    @Reference
    private DDMIndexer ddmIndexer;

    @Reference
    private IndexSearcherHelper indexSearcherHelper;

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String bottom = ParamUtil.getString(renderRequest,"bottom");
        String top = ParamUtil.getString(renderRequest,"top");

        long structureId = PrefsParamUtil.getLong(renderRequest.getPreferences(), renderRequest, SearchTestPortletKeys.PREF_STRUCTURE_ID, 0);
        String minName = renderRequest.getPreferences().getValue(SearchTestPortletKeys.PREF_MIN_FIELD_NAME, null);
        String maxName = renderRequest.getPreferences().getValue(SearchTestPortletKeys.PREF_MAX_FIELD_NAME, null);
        if (minName != null && maxName != null && structureId>0) {
            try {

                String minField = ddmIndexer.encodeName(structureId, minName, themeDisplay.getLocale());
                minField = DocumentImpl.getSortableFieldName(minField + NUMBER_SUFFIX);
                String maxField = ddmIndexer.encodeName(structureId, maxName, themeDisplay.getLocale());
                maxField = DocumentImpl.getSortableFieldName(maxField + NUMBER_SUFFIX);

                SearchContext searchContext = new SearchContext();
                searchContext.setCompanyId(themeDisplay.getCompanyId());

                BooleanQuery booleanQuery = new BooleanQueryImpl();
                booleanQuery.addRequiredTerm(Field.ENTRY_CLASS_NAME, JournalArticle.class.getName());
                booleanQuery.addRequiredTerm(Field.SCOPE_GROUP_ID, String.valueOf(themeDisplay.getScopeGroupId()));
                booleanQuery.addRequiredTerm(Field.CLASS_TYPE_ID, structureId);
                booleanQuery.addRequiredTerm("latest", true);

                TermRangeQuery rangeQuery1 = new TermRangeQueryImpl(minField, null, top, false, true);
                booleanQuery.add(rangeQuery1,BooleanClauseOccur.MUST);
                TermRangeQuery rangeQuery2 = new TermRangeQueryImpl(maxField, bottom, null, true, false);
                booleanQuery.add(rangeQuery2,BooleanClauseOccur.MUST);

                Hits search = indexSearcherHelper.search(searchContext, booleanQuery);

                List<String> data = new ArrayList<>();

                for (Document doc : search.getDocs()) {

                    String sb = doc.get(themeDisplay.getLocale(), Field.TITLE);
                    data.add(sb);

                }
                renderRequest.setAttribute("data", data);
            } catch (SearchException | ParseException e) {
                e.printStackTrace();
            }
        }else{
            renderRequest.setAttribute("noConfig", true);
        }
        super.doView(renderRequest, renderResponse);
    }

    public void submitForm(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException {
        //nothing
    }
}
