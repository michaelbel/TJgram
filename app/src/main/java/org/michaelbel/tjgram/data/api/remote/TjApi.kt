package org.michaelbel.tjgram.data.api.remote

import io.reactivex.Observable
import okhttp3.MultipartBody
import org.michaelbel.tjgram.data.api.results.*
import org.michaelbel.tjgram.data.entities.AttachResponse
import org.michaelbel.tjgram.data.entities.BaseResult
import org.michaelbel.tjgram.data.entities.Entry
import org.michaelbel.tjgram.data.entities.Subsite
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface TjApi {

    @FormUrlEncoded
    @POST("auth/qr")
    fun authQr(@Field("token") token: String): Observable<Response<UserResult>>

    @GET("user/me")
    fun userMe(): Observable<UserResult>

    @GET("timeline/{category}/{sorting}")
    fun timeline(
            @Path("category") category: String,
            @Path("sorting") sorting: String,
            @Query("count") count: Int,
            @Query("offset") offset: Int
    ): Observable<EntriesResult>

    @GET("subsite/{id}/timeline/{sorting}")
    fun subsiteTimeline(
            @Path("id") subsiteId: Long,
            @Path("sorting") sorting: String,
            @Query("count") count: Int,
            @Query("offset") offset: Int
    ): Observable<EntriesResult>

    @FormUrlEncoded
    @POST("entry/complaint")
    fun entryComplaint(@Field("content_id") contentId: Int): Observable<BooleanResult>

    @FormUrlEncoded
    @POST("entry/{id}/likes")
    fun likeEntry(
            @Path("id") entryId: Int,
            @Field("sign") sign: Int
    ): Observable<LikesResult>

    @FormUrlEncoded
    @POST("entry/create")
    fun entryCreate(
            @Field("title") title: String,
            @Field("text") text: String,
            @Field("subsite_id") subsiteId: Long,
            @FieldMap(encoded = false) attaches: Map<String, String>
    ): Observable<EntryResult>

    @Multipart
    @POST("uploader/upload")
    fun uploaderUpload(@Part file: MultipartBody.Part): Observable<BaseResult<ArrayList<AttachResponse>>>

    /*@GET("subsites_list/{type}")
    fun subsitesList(@Path("type") type: String): Observable<BaseResult<ArrayList<Subsite>>>*/

    /*@GET("subsite/{id}")
    fun subsite(@Path("id") subsiteId: Long): Observable<BaseResult<Subsite>>*/

    /*@GET("user/me/favorites/entries")
    fun userMeFavoritesEntries(
        @Query("count") count: Int,
        @Query("offset") offset: Int
    ): Observable<EntriesResult>*/

    /*@GET("user/me/subscriptions/subscribed")
    fun userMeSubscriptionsSubscribed(): Observable<BaseResult<ArrayList<Subsite>>>*/

    /*@GET("timeline/mainpage")
    fun hashtag(
            @Query("hashtag") hashtag: String,
            @Query("count") count: Int,
            @Query("offset") offset: Int
    ): Observable<EntriesResult>*/

    /*@GET("news/default/recent")
    fun timelineNews(
            @Query("count") count: Int,
            @Query("offset") offset: Int
    ): Observable<EntriesResult>*/

    /*@GET("getflashholdedentry")
    fun flashholdedEntry(@Path("id") id: Int): Observable<Entry>*/

    /*@GET("tweets/{mode}")
    fun tweets(
            @Path("mode") mode: String,
            @Query("count") count: Int,
            @Query("offset") offset: Int
    ): Observable<TweetsResult>*/
}