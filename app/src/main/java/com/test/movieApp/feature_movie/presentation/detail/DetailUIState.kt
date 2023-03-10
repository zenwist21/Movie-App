package com.test.movieApp.feature_movie.presentation.detail

import com.test.movieApp.feature_movie.util.DetailType
import com.test.movieApp.core.data.model.TmDbModel
import com.test.movieApp.core.data.model.VideoModel

data class DetailUIState(
    val initialLoading: Boolean = false,
    val loadingMovies: Boolean = false,
    val result: TmDbModel? = null,
    val resultVideo:List<VideoModel> = emptyList(),
    val errorVideo:String? = null,
    val errorResult:String? = null,
    val itemId: Int? = null,
    val type: DetailType? = DetailType.MOVIE
)