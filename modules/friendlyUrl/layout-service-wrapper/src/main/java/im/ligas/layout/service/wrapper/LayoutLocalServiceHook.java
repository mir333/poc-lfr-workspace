/**
 * Copyright 2000-present Liferay, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ligas.layout.service.wrapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutModel;
import com.liferay.portal.kernel.service.LayoutLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.util.StringPool;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Liferay
 */
@Component(service = ServiceWrapper.class)
public class LayoutLocalServiceHook extends LayoutLocalServiceWrapper {

    private static Logger LOG = getLogger(LayoutLocalServiceHook.class);

    public LayoutLocalServiceHook() {
        super(null);
    }


    @Override
    public Layout addLayout(long userId, long groupId, boolean privateLayout, long parentLayoutId,
                            Map<Locale, String> nameMap, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
                            Map<Locale, String> keywordsMap, Map<Locale, String> robotsMap, String type, String typeSettings,
                            boolean hidden, Map<Locale, String> friendlyURLMap, ServiceContext serviceContext) throws PortalException {

        Layout layout = super.addLayout(userId, groupId, privateLayout, parentLayoutId, nameMap, titleMap, descriptionMap, keywordsMap,
                robotsMap, type, typeSettings, hidden, friendlyURLMap, serviceContext);

        if (parentLayoutId != 0) {
            super.updateLayout(groupId, privateLayout, layout.getLayoutId(), parentLayoutId, nameMap, titleMap,
                    descriptionMap, keywordsMap, robotsMap, type, hidden, friendlyURLMap,
                    false, null, serviceContext);
        }

        return layout;
    }

    @Override
    public Layout updateLayout(long groupId, boolean privateLayout, long layoutId, long parentLayoutId,
                               Map<Locale, String> nameMap, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
                               Map<Locale, String> keywordsMap, Map<Locale, String> robotsMap, String type, boolean hidden,
                               Map<Locale, String> friendlyURLMap, boolean iconImage, byte[] iconBytes, ServiceContext serviceContext) throws PortalException {

        Layout layout = super.getLayout(groupId, privateLayout, layoutId);

        LOG.info("Enhancing friendly URL");

        if (parentLayoutId != 0) {
            List<Layout> ancestors = layout.getAncestors();
            Collections.reverse(ancestors);
            final String path = ancestors.stream()
                    .map(LayoutModel::getFriendlyURL)
                    .filter(Objects::nonNull)
                    .map(this::friendlyUrlSufix)
                    .collect(Collectors.joining());
            LOG.info("Prefixing with {}", path);

            friendlyURLMap.entrySet().forEach(e -> {
                String fURL = e.getValue();
                if (fURL != null) {
                    e.setValue(path + friendlyUrlSufix(fURL));
                }
            });

            layout = super.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, nameMap, titleMap,
                    descriptionMap, keywordsMap, robotsMap, type, hidden, friendlyURLMap,
                    iconImage, iconBytes, serviceContext);
        }


        return layout;
    }

    @Override
    public Layout updateParentLayoutIdAndPriority(long plid, long parentPlid, int priority) throws PortalException {
        LOG.info("Update");
        Layout l = super.updateParentLayoutIdAndPriority(plid, parentPlid, priority);
        updateLayout(l.getGroupId(), l.getPrivateLayout(), l.getLayoutId(), l.getParentLayoutId(), l.getNameMap(),
                l.getTitleMap(), l.getDescriptionMap(), l.getKeywordsMap(), l.getRobotsMap(), l.getType(), l.getHidden(),
                l.getFriendlyURLMap(), false, null, new ServiceContext());
        return l;
    }

    private String friendlyUrlSufix(String fullURL) {
        String result = null;
        if (fullURL != null) {
            int i = fullURL.lastIndexOf(StringPool.FORWARD_SLASH);
            if (i > -1) {
                result = fullURL.substring(i);
            }
        }
        return result;
    }

}
