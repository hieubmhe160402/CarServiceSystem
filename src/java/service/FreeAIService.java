package service;

import context.DBContext;
import java.io.*;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class FreeAIService extends DBContext {

    // 🔑 API key của bạn (hãy thay bằng key hợp lệ)
    private static final String GEMINI_API_KEY = "AIzaSyDLl73qSYNQS6LHb3QCqzfPkuhy4ZIHLoQ";

    // ✅ Endpoint API mới nhất
    private static final String GEMINI_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;

    /**
     * Chat với AI (Gemini)
     *
     * @param userMessage Tin nhắn người dùng
     * @param history (tùy chọn) lịch sử hội thoại
     * @return Trả lời từ Gemini
     */
    public static String chatWithGemini(String userMessage) {
        return chatWithAI(userMessage, null);
    }

    public static String chatWithAI(String fullContext, String brand, String model, String odometer, String lastMaintenance) {
        try {
            String systemPrompt = String.format(
                    "Bạn là chuyên gia tư vấn bảo dưỡng ô tô. "
                    + "Xe khách hàng: %s %s, số km: %s, bảo dưỡng gần nhất: %s. "
                    + "Hãy tư vấn cụ thể, chính xác dựa trên thông tin này.",
                    brand, model, odometer, lastMaintenance
            );

            // Gọi API Gemini/OpenAI với system prompt và user message
            return callGeminiAPI(systemPrompt, fullContext);

        } catch (Exception e) {
            return "❌ Lỗi khi kết nối AI: " + e.getMessage();
        }
    }

    private static String callGeminiAPI(String systemPrompt, String userPrompt) throws IOException {
        URL url = new URL(GEMINI_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        // 🧠 Gộp system prompt + user prompt
        String prompt = systemPrompt + "\nNgười dùng: " + userPrompt;

        // ✅ Tạo nội dung JSON gửi đi
        JSONObject content = new JSONObject();
        JSONArray contents = new JSONArray()
                .put(new JSONObject()
                        .put("role", "user")
                        .put("parts", new JSONArray().put(new JSONObject().put("text", prompt))));
        content.put("contents", contents);

        // Gửi request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = content.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Đọc response
        int status = conn.getResponseCode();
        InputStream inputStream = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        if (status != 200) {
            return "❌ Lỗi API Gemini: " + response;
        }

        // ✅ Đọc text trả lời từ Gemini
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }

    public static String chatWithAI(String userMessage, String history) {
        try {
            URL url = new URL(GEMINI_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // 🧠 Prompt gửi đến Gemini
            String prompt = "Bạn là trợ lý kỹ thuật ô tô, chuyên gợi ý lịch bảo dưỡng và sửa chữa, liên quan đến xe của khách hàng. "
                    + "Trả lời ngắn gọn, thân thiện, bằng tiếng Việt có emoji.\n"
                    + (history != null ? ("Lịch sử hội thoại: " + history + "\n") : "")
                    + "Người dùng: " + userMessage;

            JSONObject content = new JSONObject();
            JSONArray contents = new JSONArray()
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("parts", new JSONArray().put(new JSONObject().put("text", prompt))));
            content.put("contents", contents);

            // Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = content.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Đọc phản hồi
            int status = conn.getResponseCode();
            InputStream inputStream = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            if (status != 200) {
                return "❌ Lỗi API Gemini: " + response;
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String aiText = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return aiText;

        } catch (Exception e) {
            e.printStackTrace();
            return "😅 Lỗi khi gọi Gemini API: " + e.getMessage();
        }
    }

    /**
     * ✅ Hàm AI gợi ý nhắc bảo dưỡng (được servlet gọi)
     */
    public static String getMaintenanceAdvice(String brand, String model, double odometer, String lastDate) {
        String message = String.format(
                "Xe %s %s đã đi được %.0f km, lần bảo dưỡng gần nhất là %s. "
                + "Hãy tư vấn tôi nên kiểm tra hoặc thay thế bộ phận nào.",
                brand, model, odometer, lastDate);
        return chatWithAI(message, null);
    }

    /**
     * ⚙️ Test nhanh
     */
    public static void main(String[] args) {
        System.out.println("========== TEST GEMINI ==========");
        String response = getMaintenanceAdvice("Toyota", "Vios", 45000, "2024-11-10");
        System.out.println("Kết quả từ Gemini:\n" + response);
    }
}
