package rw.kanis.shop.Presentation.ui.activities;

import rw.kanis.shop.Network.response.StripeClientSecretResponse;

public interface StripePaymentView {
    void onClientSecretReceived(StripeClientSecretResponse stripeClientSecretResponse);
}
