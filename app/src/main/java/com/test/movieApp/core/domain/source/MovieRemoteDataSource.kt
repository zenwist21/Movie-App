package com.test.movieApp.core.domain.source


import com.test.movieApp.core.data.model.GenreModel
import com.test.movieApp.core.data.model.ReviewDataModel
import com.test.movieApp.core.data.model.TmDbModel
import com.test.movieApp.core.data.model.VideoModel
import com.test.movieApp.core.utils.Resource
import com.test.movieApp.core.data.remote.response.BaseResponse

interface MovieRemoteDataSource {
    suspend fun getListMovie(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<TmDbModel>>>
    suspend fun getMovieGenres(language: String = "en-US"): Resource<MutableList<GenreModel>>

    suspend fun getListTvShow(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<TmDbModel>>>
    suspend fun getTVShowGenres(language: String = "en-US"): Resource<MutableList<GenreModel>>

    suspend fun getDetailTVShow(tvID: Int): Resource<TmDbModel>
    suspend fun getDetailMovie(movieID: Int): Resource<TmDbModel>

    suspend fun getReviewMovie(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<ReviewDataModel>>>
    suspend fun getReviewTV(params: HashMap<String, Any>): Resource<BaseResponse<MutableList<ReviewDataModel>>>

    suspend fun getMovieVideos(movieID: Int): Resource<BaseResponse<MutableList<VideoModel>>>
    suspend fun getTvVideos(tvID: Int): Resource<BaseResponse<MutableList<VideoModel>>>
}