
package im.ligas.sample.searchfacet.structure;

import javax.portlet.ActionRequest;
import javax.servlet.ServletContext;

import com.liferay.portal.kernel.search.facet.MultiValueFacetFactory;
import com.liferay.portal.kernel.search.facet.util.FacetFactory;
import com.liferay.portal.search.web.facet.BaseJSPSearchFacet;
import com.liferay.portal.search.web.facet.SearchFacet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.ParamUtil;

@Component(immediate = true, service = SearchFacet.class)
public class WebContentStructureSearchFacet extends BaseJSPSearchFacet {

	@Override
	public FacetConfiguration getDefaultConfiguration(long companyId) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setClassName(getFacetClassName());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("frequencyThreshold", 1);
		jsonObject.put("maxTerms", 10);
		jsonObject.put("showAssetCount", true);

		facetConfiguration.setDataJSONObject(jsonObject);

		facetConfiguration.setFieldName(getFieldName());
		facetConfiguration.setLabel(getLabel());
		facetConfiguration.setOrder(getOrder());
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.2);

		return facetConfiguration;

	}

	@Override
	public String getFacetClassName() {
		return MultiValueFacet.class.getName();
	}

	@Override
	public String getFieldName() {
		return facetFieldName;
	}

	@Override
	public JSONObject getJSONData(ActionRequest actionRequest) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		int frequencyThreshold = ParamUtil.getInteger(actionRequest, getClassName() + "frequencyThreshold", 1);
		int maxTerms = ParamUtil.getInteger(actionRequest, getClassName() + "maxTerms", 10);
		boolean showAssetCount = ParamUtil.getBoolean(actionRequest, getClassName() + "showAssetCount", true);

		jsonObject.put("frequencyThreshold", frequencyThreshold);
		jsonObject.put("maxTerms", maxTerms);
		jsonObject.put("showAssetCount", showAssetCount);

		return jsonObject;
	}

	@Override
	public String getLabel() {
		return "WCM Structure";
	}

	@Override
	public String getTitle() {
		return "WCM";
	}

	@Override
	protected FacetFactory getFacetFactory() {
		return multiValueFacetFactory;
	}

	@Override
	public String getConfigurationJspPath() {
		return "/facets/configuration/wcstructure.jsp";
	}

	@Override
	public String getDisplayJspPath() {
		return "/facets/view/wcstructure.jsp";
	}

	@Override
	@Reference(target = "(osgi.web.symbolicname=com.liferay.portal.search.web)", unbind = "-")
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}


	@Reference
	private MultiValueFacetFactory multiValueFacetFactory;

}
