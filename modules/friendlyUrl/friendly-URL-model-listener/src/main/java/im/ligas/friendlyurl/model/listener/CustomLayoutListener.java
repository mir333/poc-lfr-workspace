package im.ligas.friendlyurl.model.listener;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.LayoutFriendlyURLLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;

@Component(immediate = true, service = ModelListener.class)
public class CustomLayoutListener extends BaseModelListener<Layout> {

    @Reference
    private LayoutFriendlyURLLocalService friendlyURLLocalService;

    @Override
    public void onBeforeCreate(Layout model) throws ModelListenerException {
        try {
            StringBuilder sb = new StringBuilder();
            List<Layout> ancestors = model.getAncestors();
            Collections.reverse(ancestors);
            if (ancestors != null) {
                for (Layout ancestor : ancestors) {
                    sb.append(ancestor.getFriendlyURL());
                }
            }
            model.setFriendlyURL(sb.toString() + model.getFriendlyURL());
        } catch (PortalException e) {
            throw new ModelListenerException(e);
        }
    }

}
