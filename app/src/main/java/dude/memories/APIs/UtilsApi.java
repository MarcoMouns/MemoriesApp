package dude.memories.APIs;

public class UtilsApi {

    public static final String BASE_URL_API = "http://Memories-api.demoday.us/";

    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
