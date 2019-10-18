package com.triton.voxit.Api;

import com.triton.voxit.model.AudioFileUploadRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImageInterface {
    @Multipart
    @POST("audio_upload_file1.php")
    Call<AudioFileUploadRequest> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}
