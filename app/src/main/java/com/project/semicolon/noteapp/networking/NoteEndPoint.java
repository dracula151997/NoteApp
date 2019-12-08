package com.project.semicolon.noteapp.networking;

import com.project.semicolon.noteapp.model.NoteResponse;
import com.project.semicolon.noteapp.model.UserResponse;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteEndPoint {
    @FormUrlEncoded
    @POST("notes/user/register")
    Single<UserResponse> register(@Field("device_id")String device_id);

    @GET("notes/all")
    Single<List<NoteResponse>> getAllNotes();

    @FormUrlEncoded
    @POST("notes/new")
    Single<NoteResponse> createNote(@Field("note")String note);

    @PUT("notes/{id}")
    @FormUrlEncoded
    Completable updateNote(@Path("id")int note_id, @Field("note")String note);

    @DELETE("notes/{id}")
    Completable deleteNote(@Path("id")int note_id);
}
