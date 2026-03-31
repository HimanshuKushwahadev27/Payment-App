package com.emi.wallet_service.serviceImpl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.client.UserClient;
import com.emi.wallet_service.entity.User_Payout_Account;
import com.emi.wallet_service.mapper.UserPayoutMapper;
import com.emi.wallet_service.repositories.UserPayoutRepo;
import com.emi.wallet_service.service.UserPayoutAccountService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.BankAccount;
import com.stripe.model.Event;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPayoutAccountServiceImpl implements UserPayoutAccountService{

	private final UserPayoutRepo payoutRepo;
	private final UserPayoutMapper userPayoutMapper;
	private final UserClient userClient;

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

	@Value("${webhook.secret.key}")
	private String webhookSecretKey;
	


	@Override
	public String createOnboardingLink(UUID userKeycloakId) throws StripeException{
		Stripe.apiKey = stripeSecretKey;

		String accountId = userClient.getStripeId(userKeycloakId);

		if (payoutRepo.findByUserKeycloakId(userKeycloakId).isEmpty()) {
				User_Payout_Account account = new User_Payout_Account();
				account.setCreatedAt(Instant.now());
				account.setUpdatedAt(Instant.now());
				account.setStripeAccountId(accountId);
				account.setUserKeycloakId(userKeycloakId);

				payoutRepo.save(account);
		}

	  Map<String, Object> params = new HashMap<>();
    params.put("account", accountId);
    params.put("refresh_url", "http://localhost:4200/reauth");
    params.put("return_url", "http://localhost:4200/success");
    params.put("type", "account_onboarding");

		AccountLink link = AccountLink.create(params);

		return link.getUrl();
	}



	@Override
	public String handleWebhook(String payload, String header) {
	  try {
        Event event = Webhook.constructEvent(payload, header, webhookSecretKey);

        if ("account.updated".equals(event.getType())) {

            Account account = (Account) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow();

            String accountId = account.getId();
						
						User_Payout_Account payout  = payoutRepo
								.findByStripeAccountId(accountId)
								.orElseThrow(() -> new RuntimeException("User not found"));

						payout.setPayoutsEnabled(account.getPayoutsEnabled());
						payout.setChargesEnabled(account.getChargesEnabled());
						payout.setDetailsSubmitted(account.getDetailsSubmitted());
						payout.setUpdatedAt(Instant.now());

						payoutRepo.save(payout);
        }

        return "";

    } catch (Exception e) {
        e.printStackTrace();
    		throw new RuntimeException("Failed to fetch webhook details", e);
    }
	}



	@Override
	public PayoutAccountResponse getStripeStatus(UUID userId) throws StripeException{
		User_Payout_Account userAccount  = payoutRepo.findByUserKeycloakId(userId).orElseThrow(() -> new RuntimeException("User not found"));
		
		try{
			Stripe.apiKey = stripeSecretKey;
			Account account = Account.retrieve(userAccount.getStripeAccountId());
			ExternalAccountCollection externalAccounts = account.getExternalAccounts();

			if (externalAccounts == null || externalAccounts.getData().isEmpty()) {
    		throw new RuntimeException("No bank account found");
			}

			ExternalAccount external = externalAccounts.getData().get(0);
			BankAccount bank = (BankAccount) external;

			String bankName = bank.getBankName();
			String last4 = bank.getLast4();

			return userPayoutMapper.toDto(userAccount, bankName, last4);

		}catch(Exception ex){
			ex.printStackTrace();
    	throw new RuntimeException("Failed to fetch bank details", ex);
		}

	}



}
