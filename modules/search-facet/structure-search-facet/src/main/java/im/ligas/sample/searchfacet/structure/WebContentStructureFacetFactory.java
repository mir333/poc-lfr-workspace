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

package im.ligas.sample.searchfacet.structure;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.util.FacetFactory;

public class WebContentStructureFacetFactory implements FacetFactory {

	@Override
	public String getFacetClassName() {
		return WebContentStructureFacet.class.getName();
	}

	@Override
	public Facet newInstance(SearchContext searchContext) {
		return new WebContentStructureFacet(searchContext);
	}

}
