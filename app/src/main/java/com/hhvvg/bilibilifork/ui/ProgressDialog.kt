package com.hhvvg.bilibilifork.ui

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import androidx.appcompat.app.AlertDialog
import com.hhvvg.bilibilifork.databinding.DialogProgressLayoutBinding

/**
 * @author hhvvg
 */
class ProgressDialog(context: Context) : AlertDialog(context) {
    private val binding by lazy { DialogProgressLayoutBinding.inflate(layoutInflater) }
    private var _title: CharSequence? = null
        set(value) {
            field = value
            updateTitle()
        }
    var progress: Int = 0
        set(value) {
            field = value
            binding.progressBar.progress = value
            updateTitle()
        }
    var maxProgress: Int
        set(value) {
            binding.progressBar.max = value
            updateTitle()
        }
        get() = binding.progressBar.max

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    private fun updateTitle() {
        binding.dialogTitle.text = SpannableString("$_title (${progress}/${maxProgress})")
    }

    override fun setTitle(title: CharSequence?) {
        _title = title
        updateTitle()
    }

    override fun setTitle(titleId: Int) {
        _title = context.getString(titleId)
        updateTitle()
    }
}
