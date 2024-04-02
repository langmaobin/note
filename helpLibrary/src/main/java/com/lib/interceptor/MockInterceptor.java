//package com.lib.interceptor;
//
//import android.content.Context;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.util.Locale;
//
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Protocol;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_TYPE;
//import static com.lib.base.BaseConstants.MediaTypes.MEDIA_TYPE_JSON;
//
//public class MockInterceptor implements Interceptor {
//
//    private Context context;
//
//    public MockInterceptor(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//
//        URI uri = chain.request().url().uri();
//
//        String requestPath = uri.getPath().toLowerCase(Locale.ENGLISH);
//        String[] requestArray = requestPath.split("/");
//
//        String filename = "";
//
//        if (requestArray.length >= 2) {
//            filename = requestArray[requestArray.length - 1];
//        }
//
//        String fileName = String.format("json/%s.json", filename); // .replace("-", "_")
//
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        InputStream is = null;
//        byte[] buffer;
//        try {
//            is = context.getAssets().open(fileName);
//            buffer = new byte[is.available()];
//            is.read(buffer);
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//
//        String responseString = new String(buffer);
//        return new Response.Builder()
//                .code(200)
//                .message(responseString)
//                .request(chain.request())
//                .protocol(Protocol.HTTP_1_0)
//                .body(ResponseBody.create(MediaType.parse(MEDIA_TYPE_JSON), responseString.getBytes(StandardCharsets.UTF_8)))
//                .addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
//                .build();
//
//    }
//
//}
