package com.example.tcsoneapp.retrofit;


import android.content.Context;


import com.example.tcsoneapp.utils.AppConstants;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitService {

    private static ApiInterface service;
    public static final String BASE_URL = "https://devapif5.tcscourier.com:7080/tcs/json/gateway/service/";

    public static RetrofitService newInstance() {
        return new RetrofitService();
    }



    public static ApiInterface getService(Context context) {
        if(service == null){
            service = provideRetrofit(BASE_URL, provideOkHttpClient(),context).create(ApiInterface.class);
        }
        return service;
    }


    private static Retrofit provideRetrofit(String baseURL, OkHttpClient.Builder client, Context context) {
        OkHttpClient.Builder okHttpClient = RetrofitService.UnsafeOkHttpClient.getUnsafeOkHttpClient(context).newBuilder();
        okHttpClient.connectTimeout(1, TimeUnit.MINUTES);
        okHttpClient.writeTimeout(1, TimeUnit.MINUTES);
        okHttpClient.readTimeout(1, TimeUnit.MINUTES);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }


    private static OkHttpClient.Builder provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json").method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        httpClient.connectTimeout(3, TimeUnit.MINUTES);
        httpClient.writeTimeout(3, TimeUnit.MINUTES);
        httpClient.readTimeout(3, TimeUnit.MINUTES);


        return httpClient;
    }

    private static class UnsafeOkHttpClient {
        public static OkHttpClient getUnsafeOkHttpClient(Context context) {

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
//                            return null;
                        }
                    }
            };

            KeyStore keyStore = readKeyStore(context);
            KeyManagerFactory kmf = null;
            final SSLContext sslContext;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            try {
                kmf = KeyManagerFactory.getInstance("X509");
                kmf.init(keyStore, AppConstants.CERTIFICATE_PASSWORD.toCharArray());

                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
                builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

            } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
                e.printStackTrace();
            }

            return builder.build();

        }
    }

    private static KeyStore readKeyStore(Context context) {
        KeyStore ks = null;
        InputStream in = null;
        try {
            ks = KeyStore.getInstance("PKCS12");
            char[] password = AppConstants.CERTIFICATE_PASSWORD.toCharArray();
            in = context.getResources().openRawResource(AppConstants.CERTIFICATE);
                ks.load(in, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ks;
    }



}
