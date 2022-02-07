package com.example.projectfinder.ui.retrofit

import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.projectfinder.ui.DBClasses.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService
{
    fun addUser(user: User, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (User?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(user).enqueue(
                object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("userAdd", "onFailure")
                        progressBar?.isVisible = false
                        progressLayout?.isVisible = false
                        onResult(null)
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.d("userAdd", "onResponse")
                        val addedUser = response.body()
                        progressBar?.isVisible = false
                        progressLayout?.isVisible = false
                        onResult(addedUser)
                    }
                }
        )
    }

    fun authUser(login: Login, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (Tokens?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.authUser(login).enqueue(
                object : Callback<Tokens> {
                    override fun onFailure(call: Call<Tokens>, t: Throwable) {
                        Log.d("tokens", "onFailure")
                        progressBar?.isVisible = false
                        progressLayout?.isVisible = false
                        onResult(null)
                    }

                    override fun onResponse(call: Call<Tokens>, response: Response<Tokens>) {
                        Log.d("tokens", "onResponse")
                        val tokens = response.body()
                        progressBar?.isVisible = false
                        progressLayout?.isVisible = false
                        onResult(tokens)
                    }
                }
        )
    }

    fun refreshToken(pair: RefreshPair, onResult: (Tokens?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.refreshToken(pair.refreshToken, pair).enqueue(
            object : Callback<Tokens> {
                override fun onFailure(call: Call<Tokens>, t: Throwable) {
                    Log.d("refreshToken", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<Tokens>, response: Response<Tokens>) {
                    Log.d("refreshToken", "onResponse")
                    val tokens = response.body()
                    onResult(tokens)
                }
            }
        )
    }

    fun projectsList(authorization: String, shit: Shit, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (ProjectsList?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.projectsList(authorization, shit).enqueue(
            object : Callback<ProjectsList> {
                override fun onFailure(call: Call<ProjectsList>, t: Throwable) {
                    Log.d("projectsList", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<ProjectsList>, response: Response<ProjectsList>) {
                    Log.d("projectsList", "onResponse")
                    Log.d("projectsList", response.errorBody().toString())
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    val projectsList = response.body()
                    onResult(projectsList)
                }
            }
        )
    }

    fun usersList(authorization: String, project: ProjectsItem, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (List<User>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.usersList(authorization, project).enqueue(
            object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("usersList", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    Log.d("usersList", "onResponse")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    Log.d("usersList", response.message())
                    val usersList = response.body()
                    onResult(usersList)
                }
            }
        )
    }

    fun skillTags(authorization: String, onResult: (List<SkillTag>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.skillTags(authorization).enqueue(
            object : Callback<List<SkillTag>> {
                override fun onFailure(call: Call<List<SkillTag>>, t: Throwable) {
                    Log.d("skillTags", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<List<SkillTag>>, response: Response<List<SkillTag>>) {
                    Log.d("skillTags", "onResponse")
                    val skillTags = response.body()
                    onResult(skillTags)
                }
            }
        )
    }

    fun myInfo(authorization: String, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (User?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.myInfo(authorization).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("myInfo", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("myInfo", "onResponse")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    val myInfo = response.body()
                    onResult(myInfo)
                }
            }
        )
    }

    fun myProjects(authorization: String, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (List<ProjectsItem>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.myProjects(authorization).enqueue(
            object : Callback<List<ProjectsItem>> {
                override fun onFailure(call: Call<List<ProjectsItem>>, t: Throwable) {
                    Log.d("myProjects", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<List<ProjectsItem>>, response: Response<List<ProjectsItem>>) {
                    Log.d("myProjects", "onResponse")
                    val myProjects = response.body()
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(myProjects)
                }
            }
        )
    }

    fun editMyInfo(authorization: String, user: User, onResult: (User?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.editMyInfo(authorization, user).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("editMyInfo", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("editMyInfo", "onResponse")
                    val myInfo = response.body()
                    onResult(myInfo)
                }
            }
        )
    }

    fun changeProject(authorization: String, slug: String, project: ProjectsItem, onResult: (ProjectsItem?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.changeProject(authorization, slug, project).enqueue(
            object : Callback<ProjectsItem> {
                override fun onFailure(call: Call<ProjectsItem>, t: Throwable) {
                    Log.d("changeProject", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<ProjectsItem>, response: Response<ProjectsItem>) {
                    Log.d("changeProject", "onResponse")
                    val newProject = response.body()
                    onResult(newProject)
                }
            }
        )
    }

    fun createProject(authorization: String, project: ProjectsItem, onResult: (ProjectsItem?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.createProject(authorization, project).enqueue(
            object : Callback<ProjectsItem> {
                override fun onFailure(call: Call<ProjectsItem>, t: Throwable) {
                    Log.d("createProject", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<ProjectsItem>, response: Response<ProjectsItem>) {
                    Log.d("createProject", "onResponse")
                    val project = response.body()
                    onResult(project)
                }
            }
        )
    }

    fun likeProject(authorization: String, slug: Slug, onResult: (Slug?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.likeProject(authorization, slug).enqueue(
            object : Callback<Slug> {
                override fun onFailure(call: Call<Slug>, t: Throwable) {
                    Log.d("likeProject", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<Slug>, response: Response<Slug>) {
                    Log.d("likeProject", "onResponse")
                    Log.d("likeProject", response.code().toString())
                    val smth = response.body()
                    onResult(smth)
                }
            }
        )
    }

    fun likeUser(authorization: String, slugUser: SlugUser, onResult: (Slug?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.likeUser(authorization, slugUser).enqueue(
            object : Callback<Slug> {
                override fun onFailure(call: Call<Slug>, t: Throwable) {
                    Log.d("likeUser", "onFailure")
                    onResult(null)
                }

                override fun onResponse(call: Call<Slug>, response: Response<Slug>) {
                    Log.d("likeUser", "onResponse")
                    Log.d("likeUser", response.code().toString())
                    val smth = response.body()
                    onResult(smth)
                }
            }
        )
    }

    fun myMatches(authorization: String, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (List<Match>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.myMatches(authorization).enqueue(
            object : Callback<List<Match>> {
                override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                    Log.d("myMatches", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                    Log.d("myMatches", "onResponse")
                    Log.d("myMatches", response.code().toString())
                    val myMatches = response.body()
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(myMatches)
                }
            }
        )
    }

    fun getUser(authorization: String, username: String, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (User?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUser(authorization, username).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("getUser", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("getUser", "onResponse")
                    Log.d("getUser", response.code().toString())
                    val user = response.body()
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(user)
                }
            }
        )
    }

    fun getProject(authorization: String, slug: String, progressBar: ProgressBar?, progressLayout: LinearLayout?, onResult: (ProjectsItem?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getProject(authorization, slug).enqueue(
            object : Callback<ProjectsItem> {
                override fun onFailure(call: Call<ProjectsItem>, t: Throwable) {
                    Log.d("getProject", "onFailure")
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(null)
                }

                override fun onResponse(call: Call<ProjectsItem>, response: Response<ProjectsItem>) {
                    Log.d("getProject", "onResponse")
                    Log.d("getProject", response.code().toString())
                    val project = response.body()
                    progressBar?.isVisible = false
                    progressLayout?.isVisible = false
                    onResult(project)
                }
            }
        )
    }
}