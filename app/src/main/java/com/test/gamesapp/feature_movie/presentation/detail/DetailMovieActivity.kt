package com.test.gamesapp.feature_movie.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.test.gamesapp.core.base.BaseActivity
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterGenre
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ActivityDetailMovieBinding
import com.test.gamesapp.feature_movie.data.model.TmDbModel
import com.test.gamesapp.feature_movie.util.DetailType
import com.test.gamesapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailMovieActivity : BaseActivity() {

    @Inject
    lateinit var adapterGenre: AdapterGenre
    private var _binding: ActivityDetailMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailMovieViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        binding.vm = viewModel
        initController()
        setContentView(binding.root)
    }

    override fun initController() {
        viewModel.setMovieId(intent.getIntExtra(Constant.itemID, 0),
            DetailType.valueOf(intent.extras?.getString(Constant.itemTYPE) ?: DetailType.MOVIE.name) )
        viewModel.getData()
        usedView.value = mutableListOf(
            binding.iLoading.llcLoading,
            binding.llcMain,
            null,
            binding.iError.llcError,
            null
        )
        initListener()
        initAdapter()
        observe()

    }

    private fun initAdapter() {
        binding.rvGenre.adapter = adapterGenre
    }

    override fun initListener() {
        binding.iToolbar.ivBack.setOnClickListener {
            this.finish()
        }
        binding.btnYt.setOnClickListener {
            when (viewModel.state.value.type) {
                DetailType.MOVIE -> {
                    viewModel.getMovieVideos()
                }
                else -> {
                    viewModel.getTvVideos()
                }
            }
        }
        binding.iError.tvTryAgain.setOnClickListener {
            viewModel.getData()
        }
        binding.tvSeeReview.setOnClickListener {
//            Intent(this, ReviewActivity::class.java).also {
//                it.putExtra(Constant.itemID, viewModel.state.value.itemId)
//                it.putExtra(Constant.itemTYPE, viewModel.state.value.type)
//                startActivity(it)
//            }
        }
    }

    override fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            viewModel.currentLayout.observe(this@DetailMovieActivity) {
                setBaseLayout(it)
            }
            if (state.loadingMovies) binding.progressBar.showView()
            else {
                binding.progressBar.hideView()
                if (!state.errorVideo.isNullOrEmpty())
                {
                    Toast.makeText(
                        this@DetailMovieActivity,
                        if (state.errorVideo == Constant.NO_VIDEO) getString(R.string.no_video) else state.errorVideo,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    if (state.resultVideo.isNotEmpty()) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube://${state.resultVideo[0].key}")
                        )
                        startActivity(intent)
                        viewModel.clearLink()
                    }
                }

            }
            if (state.initialLoading) viewModel.setCurrentLayoutStage(0)
            else {
                if (state.errorResult.isNullOrEmpty() || state.result != null)
                {
                    viewModel.setCurrentLayoutStage(200)
                    setToView(state.result)
                }
                else {
                    viewModel.setCurrentLayoutStage(500)
                    binding.iError.tvMessage.text = state.errorResult
                    binding.iError.tvTryAgain.showView()
                }
            }
        }
    }

    private fun setToView(result: TmDbModel?) {
        if (result != null) {
            binding.apply {
                tvTitle.text = if (!result.title.isNullOrEmpty()) result.title else result.name
                tvDescription.text = result.overview
                imageView.loadImage(
                    this@DetailMovieActivity,
                    Constant.IMAGE_URL + result.backdrop_path
                )
                profileImage.loadImage(
                    this@DetailMovieActivity,
                    Constant.IMAGE_URL + result.poster_path
                )
                adapterGenre.differ.submitList(result.genres)
            }
        }
    }
}