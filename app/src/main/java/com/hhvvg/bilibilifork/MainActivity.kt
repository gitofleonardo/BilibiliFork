package com.hhvvg.bilibilifork

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hhvvg.bilibilifork.adapter.AnimeAdapter
import com.hhvvg.bilibilifork.adapter.AnimeItemWrapper
import com.hhvvg.bilibilifork.databinding.ActivityMainBinding
import com.hhvvg.bilibilifork.databinding.DialogInfoInputLayoutBinding
import com.hhvvg.bilibilifork.ui.ProgressDialog
import com.hhvvg.bilibilifork.viewmodel.AnimeSubscribeResult
import com.hhvvg.bilibilifork.viewmodel.LoadAnimeResult
import com.hhvvg.bilibilifork.viewmodel.MainViewModel

/**
 * @author hhvvg
 */
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val animeList = mutableListOf<AnimeItemWrapper>()
    private val adapter: AnimeAdapter by lazy { AnimeAdapter(animeList) }
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.animListRv.adapter = adapter
        binding.animeSrl.setOnRefreshListener {
            if (viewModel.userUid == -1L || viewModel.cookie.isEmpty()) {
                Toast.makeText(this, getString(R.string.uid_and_cookie_hint), Toast.LENGTH_SHORT).show()
                binding.animeSrl.isRefreshing = false
                return@setOnRefreshListener
            }
            viewModel.loadAllSubscribedAnime()
        }
        binding.subscribeBtn.setOnClickListener {
            val animeToSubscribe =
                animeList.filter { it.selected }.map { it.animeItem.season_id.toString() }
            progressDialog?.dismiss()
            progressDialog = ProgressDialog(this)
            progressDialog?.apply {
                show()
                setTitle(R.string.in_progress)
                maxProgress = animeToSubscribe.size
            }
            viewModel.subscribeAnime(animeToSubscribe)
        }
        binding.animListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.subscribeBtn.isExtended) {
                    binding.subscribeBtn.shrink()
                } else if (dy < 0 && !binding.subscribeBtn.isExtended) {
                    binding.subscribeBtn.extend()
                }
            }
        })
        observeAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_input_info -> {
                openInfoInputDialog()
                true
            }
            else -> false
        }
    }

    private fun openInfoInputDialog() {
        val dialogContentBinding = DialogInfoInputLayoutBinding.inflate(layoutInflater)
        if (viewModel.userUid != -1L) {
            dialogContentBinding.uidInput.setText(viewModel.userUid.toString())
        }
        if (viewModel.cookie.isNotEmpty()) {
            dialogContentBinding.cookieInput.setText(viewModel.cookie)
        }
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.uid_and_cookie)
            .setView(dialogContentBinding.root)
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                viewModel.userUid =
                    dialogContentBinding.uidInput.text.toString().toLongOrNull() ?: -1
                viewModel.cookie = dialogContentBinding.cookieInput.text.toString()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeAll() {
        viewModel.loadAnimeResult.observe(this) {
            binding.animeSrl.isRefreshing = false
            when (it) {
                is LoadAnimeResult.LoadFailure -> {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
                is LoadAnimeResult.LoadSuccess -> {
                    val results = it.anime
                    animeList.clear()
                    animeList.addAll(results)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        viewModel.animeSubscribeResult.observe(this) {
            when (it) {
                is AnimeSubscribeResult.SubscribeSuccess -> {
                    Toast.makeText(this, getString(R.string.subscribe_all_success_hint), Toast.LENGTH_SHORT).show()
                    progressDialog?.dismiss()
                }
                is AnimeSubscribeResult.SubscribePartSuccess -> {
                    val failures = it.failures
                    val fSet = failures.toSet()
                    val allAnime = animeList
                    for (i in 0 until allAnime.size) {
                        val anime = allAnime[i]
                        if (!fSet.contains(anime.toString())) {
                            anime.selected = false
                            adapter.notifyItemChanged(i)
                        }
                    }
                    Toast.makeText(this, getString(R.string.part_success_hint, it.success.size, it.failures.size), Toast.LENGTH_SHORT).show()
                    progressDialog?.dismiss()
                }
                is AnimeSubscribeResult.SubscribeFailure -> {
                    Toast.makeText(this, getString(R.string.err_template, it.message), Toast.LENGTH_SHORT).show()
                    progressDialog?.dismiss()
                }
                is AnimeSubscribeResult.SubscribeProgress -> {
                    progressDialog?.progress = it.done
                }
            }
        }
    }
}
