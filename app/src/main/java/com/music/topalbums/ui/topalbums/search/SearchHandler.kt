package com.music.topalbums.ui.topalbums.search

import androidx.core.widget.doOnTextChanged
import com.music.topalbums.databinding.ViewTextSearchBinding

class SearchHandler(val binding: ViewTextSearchBinding, onSearchChanged: (String?) -> Unit)
{
    private var oldTextFired: String? = null
    init
    {
        binding.searchEditText.doOnTextChanged { text, start, before, count ->
            val text = binding.searchEditText.text.toString()
            if (text.length >= 3)
            {
                onSearchChanged(text)
                oldTextFired = text
            }
            else
            {
                if(oldTextFired!=null)
                {
                    onSearchChanged(null)
                    oldTextFired = null
                }
                else
                {
                    println("Nothing to fire, text = " + text)
                }
            }
        }
    }

}