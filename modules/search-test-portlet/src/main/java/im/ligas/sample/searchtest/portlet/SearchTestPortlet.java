package im.ligas.sample.searchtest.portlet;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import im.ligas.sample.searchtest.constants.SearchTestPortletKeys;
import org.osgi.service.component.annotations.Component;

import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ligasm
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + SearchTestPortletKeys.SearchTest,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class SearchTestPortlet extends MVCPortlet {

    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SearchTestPortlet.class);


    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
            boolean sorting = ParamUtil.getBoolean(renderRequest, "sorting", false);
            SearchContext searchContext = new SearchContext();
            searchContext.setCompanyId(themeDisplay.getCompanyId());

            searchContext.setStart(0);
            searchContext.setEnd(10);

            Sort sort = SortFactoryUtil.create("modified_sortable",sorting);
            searchContext.setSorts(sort);


            QueryConfig queryConfig = searchContext.getQueryConfig();
            queryConfig.setHighlightEnabled(false);
            queryConfig.setScoreEnabled(false);

            BooleanQuery booleanQuery = BooleanQueryFactoryUtil.create(searchContext);
            booleanQuery.addTerm(Field.ENTRY_CLASS_NAME, User.class.getName());
            Hits search = IndexSearcherHelperUtil.search(searchContext, booleanQuery);

            List<String> data = new ArrayList<>();

            for (Document doc : search.getDocs()) {

                String sb = doc.get(Field.MODIFIED_DATE) + " "+
                        doc.get(Field.USER_NAME);
                data.add(sb);

            }
            renderRequest.setAttribute("data",data);
        } catch (SearchException | ParseException e) {
            e.printStackTrace();
        }

        super.doView(renderRequest, renderResponse);
    }
}
