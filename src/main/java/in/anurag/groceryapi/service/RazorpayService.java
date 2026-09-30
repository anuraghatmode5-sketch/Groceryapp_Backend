package in.anurag.groceryapi.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;


@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key-secret}")
    private String keySecret;

    private final RazorpayClient razorpayClient;

    public JSONObject createOrder(double amount, String currency) throws Exception {

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", (int) Math.round(amount));
        orderRequest.put("currency", currency);
        orderRequest.put(
                "receipt",
                "order_rcptid_" + System.currentTimeMillis()
        );

        Order order = razorpayClient.orders.create(orderRequest);

        return order.toJson();
    }

    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) throws Exception {

        String data = razorpayOrderId + "|" + razorpayPaymentId;

        return Utils.verifySignature(
                data,
                razorpaySignature,
                keySecret
        );
    }
}