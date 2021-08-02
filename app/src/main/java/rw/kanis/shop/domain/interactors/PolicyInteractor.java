package rw.kanis.shop.domain.interactors;

import rw.kanis.shop.Models.PolicyContent;

public interface PolicyInteractor {
    interface CallBack {

        void onPolicyLoaded(PolicyContent policyContent);

        void onPolicyLoadError();
    }
}
