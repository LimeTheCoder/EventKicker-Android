package com.example.limethecoder.eventkicker.net;

import android.util.Base64;
import com.example.limethecoder.eventkicker.model.Comment;
import com.example.limethecoder.eventkicker.model.EventItem;
import com.example.limethecoder.eventkicker.model.Like;
import com.example.limethecoder.eventkicker.model.User;
import com.example.limethecoder.eventkicker.net.ApiResponse;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.io.IOException;


public class ServiceManager {
  private static final String BASE_URL = "http://192.168.43.39:3600";

  public static MyApiEndpointInterface newService() {
    return newService(null, null);
  }

  public static MyApiEndpointInterface newService(String username,
                                                  String password) {
    OkHttpClient httpClient = new OkHttpClient();

    if (username != null && password != null) {
      String credentials = username + ":" + password;
      final String basic =
          "Basic " + Base64.encodeToString(credentials.getBytes(),
                                           Base64.NO_WRAP);
      httpClient.interceptors().clear();
      httpClient.interceptors().add(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
          Request original = chain.request();

          Request.Builder requestBuilder = original.newBuilder()
                                                   .header("Authorization",
                                                           basic)
                                                   .header("Content-Type",
                                                           "applicaton/json")
                                                   .method(original.method(),
                                                           original.body());

          Request request = requestBuilder.build();
          return chain.proceed(request);
        }
      });
    }
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    httpClient.interceptors().add(interceptor);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).client(httpClient)
        .build();

    MyApiEndpointInterface apiService =
        retrofit.create(MyApiEndpointInterface.class);
    return apiService;
  }

  public interface MyApiEndpointInterface {

    @POST("/api/users")
    Call<ApiResponse<User>> createUser(@Body User user);

    @POST("/api/events")
    Call<ApiResponse<EventItem>> createEvent(@Body EventItem event);

    @POST("/api/login")
    Call<User> login();

    @GET("/api/events/named")
    Call<ApiResponse<EventItem>> loadEvents();

    @GET("/api/events/{id}")
    Call<ApiResponse<EventItem>> getEvent(@Path("id") int eventId);

    @GET("/api/events/{id}/comments/preview/{count}")
    Call<ApiResponse<Comment>> getEventComments(
        @Path("id") int eventId, @Path("count") int count);

    @GET("/api/events/{id}/comments/preview/5")
    Call<ApiResponse<Comment>> getEventComments(@Path("id") int eventId);

    @POST("/api/comments")
    Call<ApiResponse<Comment>> postComment(@Body Comment comment);

    @GET("/api/events/{id}/comments")
    Call<ApiResponse<Comment>> getAllEventComments(@Path("id") int eventId);

    @GET("/api/events/{id}/likes/count")
    Call<ApiResponse<Integer>> getLikesCount(@Path("id") int eventId);

    @GET("/api/events/{id}/likes/done")
    Call<ApiResponse<Boolean>> isLiked(@Path("id") int eventId);

    @GET("/api/users")
    Call<ApiResponse<EventItem>> loadUsers();

    @GET("/api/users/{id}")
    Call<ApiResponse<User>> getUser(@Path("id") int userId);

    @GET("/api/users/{id}/events")
    Call<ApiResponse<EventItem>> getUserEvents(@Path("id") int userId);

    @POST("/api/likes")
    Call<ApiResponse<Like>> postLike(@Body Like body);
  }

}
