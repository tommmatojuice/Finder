package com.example.projectfinder.ui.retrofit

import com.example.projectfinder.ui.DBClasses.*
import retrofit2.Call
import retrofit2.http.*

interface RestApi
{
    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    fun addUser(@Body user: User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    fun authUser(@Body login: Login): Call<Tokens>

    @Headers("Content-Type: application/json")
    @POST("/api/auth/refresh_token")
    fun refreshToken(
        @Header("refreshToken")
        refreshToken: String,
        @Body refreshPair: RefreshPair
    ): Call<Tokens>

    @Headers("Content-Type: application/json")
    @POST("/api/project/list_suitable")
    fun projectsList(
        @Header("Authorization")
        authorization: String,
        @Body shit: Shit
    ): Call<ProjectsList>

    @Headers("Content-Type: application/json")
    @POST("/api/user/list_suitable")
    fun usersList(
        @Header("Authorization")
        authorization: String,
        @Body project: ProjectsItem
    ): Call<List<User>>

    @Headers("Content-Type: application/json")
    @GET("/api/skill_tag/")
    fun skillTags(
        @Header("Authorization")
        authorization: String
    ): Call<List<SkillTag>>

    @Headers("Content-Type: application/json")
    @GET("/api/user/me")
    fun myInfo(
        @Header("Authorization")
        authorization: String
    ): Call<User>

    @Headers("Content-Type: application/json")
    @GET("/api/project/my")
    fun myProjects(
        @Header("Authorization")
        authorization: String
    ): Call<List<ProjectsItem>>

    @Headers("Content-Type: application/json")
    @POST("/api/user/me")
    fun editMyInfo(
        @Header("Authorization")
        authorization: String,
        @Body user: User
    ): Call<User>

    @Headers("Content-Type: application/json")
    @POST("/api/project/{slug}")
    fun changeProject(
        @Header("Authorization")
        authorization: String,
        @Path("slug")
        slug: String,
        @Body project: ProjectsItem
    ): Call<ProjectsItem>

    @Headers("Content-Type: application/json")
    @POST("/api/project/create")
    fun createProject(
        @Header("Authorization")
        authorization: String,
        @Body project: ProjectsItem
    ): Call<ProjectsItem>

    @Headers("Content-Type: application/json")
    @POST("/api/match/like_project")
    fun likeProject(
        @Header("Authorization")
        authorization: String,
        @Body slug: Slug
    ): Call<Slug>

    @Headers("Content-Type: application/json")
    @POST("/api/match/like_user")
    fun likeUser(
        @Header("Authorization")
        authorization: String,
        @Body slugUser: SlugUser
    ): Call<Slug>

    @Headers("Content-Type: application/json")
    @GET("/api/match/self")
    fun myMatches(
        @Header("Authorization")
        authorization: String
    ): Call<List<Match>>

    @Headers("Content-Type: application/json")
    @GET("/api/user/{username}")
    fun getUser(
        @Header("Authorization")
        authorization: String,
        @Path("username")
        username: String
    ): Call<User>

    @Headers("Content-Type: application/json")
    @GET("/api/project/{slug}")
    fun getProject(
        @Header("Authorization")
        authorization: String,
        @Path("slug")
        slug: String
    ): Call<ProjectsItem>
}