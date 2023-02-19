import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  

public class CheckbookInvoice {
    
    private static final String API_KEY = "628db778e0f0e25b473f89e717f637ce";
    
    public static void main(String[] args) throws IOException {
        String recipientName = "John Doe";
        double amount = 100.0;
        String description = "Payment for services";
        String address1 = "123 Main St.";
        String address2 = "";
        String city = "Anytown";
        String state = "CA";
        String zip = "12345";
        
        String url = "https://api.checkbook.io/v3/invoice";
        String payload = String.format("{"
                + "\"name\": \"%s\","
                + "\"amount\": %s,"
                + "\"description\": \"%s\","
                + "\"recipient\": {"
                + "\"name\": \"%s\","
                + "\"address1\": \"%s\","
                + "\"address2\": \"%s\","
                + "\"city\": \"%s\","
                + "\"state\": \"%s\","
                + "\"zip\": \"%s\""
                + "}"
                + "}", recipientName, amount, description, recipientName, address1, address2, city, state, zip);
        
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", getAuthHeader());
        connection.setDoOutput(true);
        connection.getOutputStream().write(payload.getBytes());
        
        if (connection.getResponseCode() == 200) {
            String response = new String(connection.getInputStream().readAllBytes());
            String invoiceUrl = new Gson().fromJson(response, CheckbookResponse.class).getUrl();
            System.out.printf("Invoice created: %s", invoiceUrl);
        } else {
            String error = new String(connection.getErrorStream().readAllBytes());
            System.out.printf("Error creating invoice: %s", error);
        }
    }
    
    private static String getAuthHeader() {
        String auth = String.format("%s:", API_KEY);
        byte[] encodedBytes = Base64.getEncoder().encode(auth.getBytes());
        String encodedAuth = new String(encodedBytes);
        return String.format("Basic %s", encodedAuth);
    }
    
    private static class CheckbookResponse {
        private String url;
        
        public String getUrl() {
            return url;
        }
    }
}
