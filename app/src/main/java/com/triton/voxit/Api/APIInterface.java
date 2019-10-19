package com.triton.voxit.Api;

import com.triton.voxit.model.AppliedJockeyResponse;
import com.triton.voxit.model.AudioFileSubmitRequest;
import com.triton.voxit.model.AudioLogResponse;
import com.triton.voxit.model.CheckFollowRequest;
import com.triton.voxit.model.DashboardResponsebean;
import com.triton.voxit.model.DisplayPointsRequest;
import com.triton.voxit.model.FollowResponseRequest;
import com.triton.voxit.model.ForgotPasswordRequest;
import com.triton.voxit.model.GenreResponseData;
import com.triton.voxit.model.GetPopupDataRequest;
import com.triton.voxit.model.HelpResponseData;
import com.triton.voxit.model.HelpText;
import com.triton.voxit.model.InsertPointsRequest;
import com.triton.voxit.model.JockeyListRequest;
import com.triton.voxit.model.LanguageResponseData;
import com.triton.voxit.model.LoginRequest;
import com.triton.voxit.model.NotificationToken;
import com.triton.voxit.model.NotifyResponseData;
import com.triton.voxit.model.OTPRequest;
import com.triton.voxit.model.RecentlyPlayedResponse;
import com.triton.voxit.model.RedeemPointsRequest;
import com.triton.voxit.model.ResetPasswordRequest;
import com.triton.voxit.model.SearchResponseData;
import com.triton.voxit.model.ShareRequest;
import com.triton.voxit.model.SignUpRequest;
import com.triton.voxit.model.SingupData;
import com.triton.voxit.model.TopThreeRequest;
import com.triton.voxit.model.UpdatereferalRequest;
import com.triton.voxit.model.UserLogResponse;
import com.triton.voxit.requestpojo.UpdateJockeyProfilePicRequest;
import com.triton.voxit.responsepojo.ImageFileUploadResponse;
import com.triton.voxit.responsepojo.UpdateJockeyProfilePicResponse;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @POST("signup")
    Call<SignUpRequest>SignupRequestTask(@Body SingupData body);

    @Multipart
    @POST("login")
    Call<LoginRequest>LoginRequestTask(@Part("userId") RequestBody username, @Part("password") RequestBody password);

//    @GET("appdashboard")
//    Call<DashboardResponsebean>DashboardRequestTask();

    @Headers("Content-Type: application/json")
    @POST("appdashboard")
    Call<DashboardResponsebean>DashboardRequestTask(@Body RequestBody jockeyid);

    @GET("language")
    Call<LanguageResponseData> getLanguageData();

    @Headers("Content-Type: application/json")
    @POST("getgenre")
    Call<GenreResponseData> getGenreData(@Body RequestBody langid);

    @Headers("Content-Type: application/json")
    @POST("resetjockeypassword")
    Call<ResetPasswordRequest> getResetPwdData(@Body RequestBody jockey_id);

    @Headers("Content-Type: application/json")
    @POST("displayPoints")
    Call<DisplayPointsRequest> getDisplayPointsData(@Body RequestBody jockey_id);

    @Headers("Content-Type: application/json")
    @POST("search")
    Call<SearchResponseData> getSearchData(@Body RequestBody jockeyid);

    @Headers("Content-Type: application/json")
    @POST("checkusers")
    Call<OTPRequest> OTPRequestTask(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("insertpoints")
    Call<InsertPointsRequest> InsertPointRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("redeemPoints")
    Call<RedeemPointsRequest> RedeemPointRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("updatereferralcode")
    Call<UpdatereferalRequest> UpdateReferralRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("forgotpassword")
    Call<ForgotPasswordRequest> ForgotRequestTask(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("fetchidaudio")
    Call<ShareRequest> shareRequestTask(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("fetchuseraudio")
    Call<JockeyListRequest> getJockeyListData(@Body RequestBody user_jocky_id);

    @Headers("Content-Type: application/json")
    @POST("insertfollwer")
    Call<FollowResponseRequest> FollowResponseRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("checkfollower")
    Call<CheckFollowRequest> CheckFollowResponseRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("favorite_create")
    Call<FollowResponseRequest> FavoriteResponseRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("favorite_check")
    Call<CheckFollowRequest> CheckFavoriteResponseRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("updatetoken")
    Call<NotificationToken> notificationTokenRequest(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("getAllNotificByJockeyId")
    Call<NotifyResponseData> getnotifyData(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("helpAndSupport")
    Call<HelpResponseData> getHelpData(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("becomeVoxitJockey")
    Call<AudioFileSubmitRequest> getAudioSubmit(@Body RequestBody body);
    @GET("getpopup")
    Call<GetPopupDataRequest> getpopupImageData();


    @Headers("Content-Type: application/json")
    @POST("checkbecomeVoxitJockey")
    Call<AppliedJockeyResponse> AppliedJockeyRequestTask(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("recentlyplayedaudios")
    Call<RecentlyPlayedResponse> RecentlyPlayedRequestTask(@Body RequestBody body);

    @GET("gethelpsupporttext")
    Call<HelpText> getHelp();

    @Headers("Content-Type: application/json")
    @POST("userLogUpdate")
    Call<UserLogResponse> UserLogRequestTask(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("audioLogUpdate")
    Call<AudioLogResponse> AudioLogRequestTask(@Body RequestBody body);


    @Multipart
    @POST("image_upload_file.php")
    Call<ImageFileUploadResponse> getImageStroeResponse(@Part MultipartBody.Part file);


    @GET("vCornerQuestions")
    Call<TopThreeRequest> getVCornerQuestionsResponseCall(@Header("Content-Type") String type);



    @POST("updateJockeyProfilePic")
    Call<UpdateJockeyProfilePicResponse> updateJockeyProfilePicResponseCall(@Header("Content-Type") String type, @Body UpdateJockeyProfilePicRequest updateJockeyProfilePicRequest);
}
