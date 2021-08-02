package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Models.ShippingAddress;
import rw.kanis.shop.Models.User;
import rw.kanis.shop.Network.response.ProfileInfoUpdateResponse;
import rw.kanis.shop.Network.response.ShippingInfoResponse;

import java.util.List;

public interface AccountInfoView {
    void onProfileInfoUpdated(ProfileInfoUpdateResponse profileInfoUpdateResponse);
    void setShippingAddresses(List<ShippingAddress> shippingAddresses);
    void onShippingInfoCreated(ShippingInfoResponse shippingInfoResponse);
    void onShippingInfoDeleted(ShippingInfoResponse shippingInfoResponse);
    void setUpdatedUserInfo(User user);
}
